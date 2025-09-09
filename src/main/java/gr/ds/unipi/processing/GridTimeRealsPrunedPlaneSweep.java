package gr.ds.unipi.processing;

import gr.ds.unipi.Algorithms;
import gr.ds.unipi.TypeSet;
import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.rdd.LPTPartitioner;
import gr.ds.unipi.rdd.LPTPartitionerBr;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.Partitioner;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GridTimeRealsPrunedPlaneSweep {

    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;
        long sampleTime = 0;
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{gr.ds.unipi.processing.Grid.class, gr.ds.unipi.processing.Cell.class, ReplicationFunction.class, Position.class, TypeSet.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, /*org.apache.spark.util.collection.CompactBuffer.class, scala.reflect.ManifestFactory$.class,*/ scala.reflect.ManifestFactory$.MODULE$.Any().getClass()/*, ConcurrentHashMap.class, Function4.class*/});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            long startJobTime = System.currentTimeMillis();
            //CHANGE HERE
            //Grid grid = Grid.newGeoGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
//            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, ReplicationFunction.lesserPointsinBoundaries);

                long o = System.currentTimeMillis();

                BufferedReader readerA = new BufferedReader(new FileReader("/home/user/samples-"+args[4]+"/"+args[2]+".csv"));
                BufferedReader readerB = new BufferedReader(new FileReader("/home/user/samples-"+args[4]+"/"+args[3]+".csv"));

                String strLine;

                while ((strLine = readerA.readLine()) != null)   {
                    // Print the content on the console
                    String[] line = strLine.split("\t");
                    grid.addPointDatasetA(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
                }

                while ((strLine = readerB.readLine()) != null)   {
                    // Print the content on the console
                    String[] line = strLine.split("\t");
                    grid.addPointDatasetB(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
                }

                readerA.close();
                readerB.close();
                sampleTime = sampleTime + (System.currentTimeMillis() - o);
                System.out.println("OK with sampling Time:" + (System.currentTimeMillis() - o));


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

            grid.printInfo();

        Partitioner cp = null;
        if(args[6].equals("HASH")){
            cp = new CustomPartitioner(Integer.parseInt(args[5]));
        }else if(args[6].equals("LPT1")){
            Broadcast<Map<Integer, Integer>> lptBroadcasted = jsc.broadcast(LPTPartitionerBr.ltp(grid.getCellsWithCosts1(), Integer.parseInt(args[5])));
            cp = new LPTPartitionerBr(lptBroadcasted, Integer.parseInt(args[5]));
//            cp = new LPTPartitioner(grid.getCellsWithCosts1(),Integer.parseInt(args[5]));
        }else{
            try {
                throw new Exception("No correct partitioner has been set");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        grid.printInfo();

            Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);

            JavaPairRDD<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>> pairRDDA = rddTuplesA.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>> list = new ArrayList<>(4);
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsAType(tuple._2(), tuple._3());
                    for (int i = 0; i < cellIds.length; i++) {
                        list.add(new Tuple2<>(cellIds[i], new Tuple2<>(cellIds[0],tuple)));
                    }
                    return list.iterator();
                }
            });

            JavaPairRDD<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>> pairRDDB = rddTuplesB.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer,Tuple2<Integer,Tuple3<String, Double, Double>>>> list = new ArrayList<>(4);
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsBType(tuple._2(), tuple._3());
                    for (int i = 0; i < cellIds.length; i++) {
                        list.add(new Tuple2<>(cellIds[i], new Tuple2<>(cellIds[0],tuple)));
                    }
                    return list.iterator();
                }
            });

             long count = pairRDDA.cogroup(pairRDDB,cp).flatMap(new FlatMapFunction<Tuple2<Integer, Tuple2<Iterable<Tuple2<Integer,Tuple3<String, Double, Double>>>, Iterable<Tuple2<Integer,Tuple3<String, Double, Double>>>>>, Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>>() {
                 @Override
                 public Iterator<Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>> call(Tuple2<Integer, Tuple2<Iterable<Tuple2<Integer,Tuple3<String, Double, Double>>>, Iterable<Tuple2<Integer,Tuple3<String, Double, Double>>>>> tup) throws Exception {

                     int nativeObjectsASize=0, nativeObjectsBSize=0, externalObjectsASize=0, externalObjectsBSize=0;

                     for (Tuple2<Integer, Tuple3<String, Double, Double>> obj : tup._2._1) {
                         if(obj._1.equals(tup._1)){
                             nativeObjectsASize++;
                         }else{
                             externalObjectsASize++;
                         }
                     }

                     for (Tuple2<Integer, Tuple3<String, Double, Double>> obj : tup._2._2) {
                         if(obj._1.equals(tup._1)){
                             nativeObjectsBSize++;
                         }else{
                             externalObjectsBSize++;
                         }
                     }

                     Tuple3<String, Double, Double>[] nativeObjectsA = new Tuple3[nativeObjectsASize];
                     Tuple3<String, Double, Double>[]  nativeObjectsB = new Tuple3[nativeObjectsBSize];

                     Tuple3<String, Double, Double>[]  externalObjectsA = new Tuple3[externalObjectsASize];
                     Tuple3<String, Double, Double>[]  externalObjectsB = new Tuple3[externalObjectsBSize];

                     nativeObjectsASize=0;
                     nativeObjectsBSize=0;
                     externalObjectsASize=0;
                     externalObjectsBSize=0;

                     for (Tuple2<Integer, Tuple3<String, Double, Double>> obj : tup._2._1) {
                         if(obj._1.equals(tup._1)){
                             nativeObjectsA[nativeObjectsASize++]=obj._2;
                         }else{
                             externalObjectsA[externalObjectsASize++]=obj._2;
                         }
                     }

                     for (Tuple2<Integer, Tuple3<String, Double, Double>> obj : tup._2._2) {
                         if(obj._1.equals(tup._1)){
                             nativeObjectsB[nativeObjectsBSize++]=obj._2;
                         }else{
                             externalObjectsB[externalObjectsBSize++]=obj._2;
                         }
                     }

                     Arrays.sort(nativeObjectsA,(Comparator.comparingDouble(obj->obj._2())));
                     Arrays.sort(nativeObjectsB,(Comparator.comparingDouble(obj->obj._2())));

                     Arrays.sort(externalObjectsA,(Comparator.comparingDouble(obj->obj._2())));
                     Arrays.sort(externalObjectsB,(Comparator.comparingDouble(obj->obj._2())));
                     
                     List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results = new ArrayList<>();
                     Algorithms.planeSweep(nativeObjectsA, nativeObjectsB, results, radius);
                     Algorithms.planeSweep(nativeObjectsA, externalObjectsB, results, radius);
                     Algorithms.planeSweep(nativeObjectsB, externalObjectsA, results, radius);

                     return results.iterator();

                 }
             }).count();



            time = time + (System.currentTimeMillis() - startJobTime);
            cou = count;

            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        System.out.println("Sample Time: "+ sampleTime);
        System.out.println("Time Exec: "+ time);
        System.out.println("Count: "+cou);

    }
}
