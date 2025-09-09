package gr.ds.unipi.twoEpsilongrid.eHalfReplication;

import gr.ds.unipi.SparkLogParser;
import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.epsilongrid.GenericGrid;
import gr.ds.unipi.grid.Cell;
import gr.ds.unipi.grid.NewFunc;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;
import scala.Tuple3;
import scala.reflect.ManifestFactory$;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GridTimeDistRealsPlaneSweep {
    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;
        long sampleTime = 0;
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{GenericGrid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, java.util.HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, ManifestFactory$.MODULE$.Any().getClass(), ConcurrentHashMap.class});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
            //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            long startJobTime = System.currentTimeMillis();
//            EpsilonGrid grid = EpsilonGrid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), radius);
            GenericGrid grid = GenericGrid.newGenericGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, 2, false);
        Broadcast<GenericGrid> gridBroadcasted = jsc.broadcast(grid);

        long t1 = System.currentTimeMillis() - startJobTime;

            JavaRDD<String> rddTxtFilesA = jsc.textFile(path + args[2] + ".csv"/*,Integer.parseInt(args[5])*/);
            JavaRDD<String> rddTxtFilesB = jsc.textFile(path + args[3] + ".csv"/*,Integer.parseInt(args[5])*/);

            JavaRDD<Tuple3<String, Double, Double>> rddTuplesA = rddTxtFilesA.map((String line) -> {
                String[] elements = line.split("\t");
                return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
            });

            JavaRDD<Tuple3<String, Double, Double>> rddTuplesB = rddTxtFilesB.map((String line) -> {
                String[] elements = line.split("\t");
                return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
            });


            //gridBroadcasted.getValue().load();
            JavaPairRDD<Integer, Tuple3<String, Double, Double>> pairRDDA = rddTuplesA.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple3<String, Double, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>(4);
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsAType(tuple._2(), tuple._3());
                    for (int cellId : cellIds) {
                        list.add(new Tuple2<>(cellId, tuple));
                    }
                    return list.iterator();
                }
            });

            JavaPairRDD<Integer, Tuple3<String, Double, Double>> pairRDDB = rddTuplesB.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple3<String, Double, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>(4);
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsBType(tuple._2(), tuple._3());
                    for (int cellId : cellIds) {
                        list.add(new Tuple2<>(cellId, tuple));
                    }
                    return list.iterator();
                }
            });

            long count = pairRDDA.cogroup(pairRDDB,new CustomPartitioner(Integer.parseInt(args[4]))).flatMap(new FlatMapFunction<Tuple2<Integer, Tuple2<Iterable<Tuple3<String, Double, Double>>, Iterable<Tuple3<String, Double, Double>>>>, Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>>() {
                @Override
                 public Iterator<Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>> call(Tuple2<Integer, Tuple2<Iterable<Tuple3<String, Double, Double>>, Iterable<Tuple3<String, Double, Double>>>> tup) throws Exception {

                    int objectsASize=0, objectsBSize=0;
                    for (Tuple3<String, Double, Double> object : tup._2()._1) {
                        objectsASize++;
                    }

                    for (Tuple3<String, Double, Double> object : tup._2()._2) {
                        objectsBSize++;
                    }

                    Tuple3<String, Double, Double>[] objectsA = new Tuple3[objectsASize];
                    Tuple3<String, Double, Double>[] objectsB = new Tuple3[objectsBSize];

                    objectsASize=0;
                    objectsBSize=0;

                    for (Tuple3<String, Double, Double> objectA : tup._2()._1) {
                        objectsA[objectsASize++] = objectA;
                    }

                    for (Tuple3<String, Double, Double> objectB : tup._2()._2) {
                        objectsB[objectsBSize++] = objectB;
                    }

                    Arrays.sort(objectsA,(Comparator.comparingDouble(obj->obj._2())));
                    Arrays.sort(objectsB,(Comparator.comparingDouble(obj->obj._2())));

                    List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results = new ArrayList<>();


                    int indexA = 0;
                    int indexB = 0;

                    while(indexA<objectsA.length && indexB<objectsB.length){
                        if(objectsA[indexA]._2()-(radius/2)<objectsB[indexB]._2()-(radius/2)){

                            int innerIndexB = indexB;
                            while(innerIndexB<objectsB.length && (objectsA[indexA]._2()+(radius/2)>=objectsB[innerIndexB]._2()-(radius/2))){

                                if((Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))) {
                                    double xMean = (objectsA[indexA]._2() + objectsB[innerIndexB]._2())/2;
                                    double yMean = (objectsA[indexA]._3() + objectsB[innerIndexB]._3())/2;
                                    if (gridBroadcasted.getValue().getCellId(xMean, yMean)==tup._1) {
                                        results.add(Tuple2.apply(objectsA[indexA], objectsB[innerIndexB]));
                                    }
                                }
                                innerIndexB++;
                            }
                            indexA++;
                        }else{

                            int innerIndexA = indexA;
                            while(innerIndexA<objectsA.length && (objectsB[indexB]._2()+(radius/2)>=objectsA[innerIndexA]._2()-(radius/2))){

                                if((Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))) {
                                    double xMean = (objectsA[innerIndexA]._2() + objectsB[indexB]._2())/2;
                                    double yMean = (objectsA[innerIndexA]._3() + objectsB[indexB]._3())/2;
                                    if (gridBroadcasted.getValue().getCellId(xMean, yMean)==tup._1) {
                                        results.add(Tuple2.apply(objectsA[innerIndexA], objectsB[indexB]));
                                    }
                                }
                                innerIndexA++;
                            }
                            indexB++;
                        }
                    }

                    return results.iterator();
                }
            }).count();


            time = time + (System.currentTimeMillis() - startJobTime);
            cou = count;

//            System.out.println(joinedRDD.filter((t) -> {
//                if (Math.pow((t._2._1._2() - t._2._2._2()), 2) + Math.pow((t._2._1._3() - t._2._2._3()), 2) <= Math.pow(radius, 2)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }).toDebugString());

            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();

        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer();
        FileWriter fw = new FileWriter("./timeExec-"+args[2]+"-"+args[3]+"-"+args[1]+"-twoEpsilonHalf-ps.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Math.round(time/1000.0)+","+Math.round((t1)/1000.0)+","+t._1()+","+t._2()+","+t._3()+"\n");
        bw.close();

        System.out.println("Sample Time: "+ sampleTime);
        System.out.println("Time Exec: "+ time);
        System.out.println("Count: "+cou);
    }
}
