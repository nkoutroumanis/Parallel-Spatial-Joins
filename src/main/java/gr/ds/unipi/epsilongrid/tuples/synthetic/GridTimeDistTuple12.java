package gr.ds.unipi.epsilongrid.tuples.synthetic;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.epsilongrid.GenericGrid;
import gr.ds.unipi.grid.Cell;
import gr.ds.unipi.grid.NewFunc;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import scala.Tuple15;
import scala.Tuple2;
import scala.reflect.ManifestFactory$;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static gr.ds.unipi.rdd.tuples.GridTimeRealsPrunedTuple2.randomString;

public class GridTimeDistTuple12 {
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
            GenericGrid grid = GenericGrid.newGenericGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, 1, true);

            JavaRDD<String> rddTxtFilesA = jsc.textFile(path + args[2] + ".csv"/*,Integer.parseInt(args[5])*/);
            JavaRDD<String> rddTxtFilesB = jsc.textFile(path + args[3] + ".csv"/*,Integer.parseInt(args[5])*/);

            Random random = new Random();

            JavaRDD<Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>> rddTuplesA = rddTxtFilesA.map((String line) -> {
                String[] elements = line.split(";");
                return new Tuple15<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000);
            });

            JavaRDD<Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>> rddTuplesB = rddTxtFilesB.map((String line) -> {
                String[] elements = line.split(";");
                return new Tuple15<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]), randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000, randomString(random, 8), random.nextDouble()*1000000);
            });

            Broadcast<GenericGrid> gridBroadcasted = jsc.broadcast(grid);

            //gridBroadcasted.getValue().load();
            JavaPairRDD<Integer, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>> pairRDDA = rddTuplesA.flatMapToPair(new PairFlatMapFunction<Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>, Integer, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>>> call(Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>>> list = new ArrayList<>();
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsAType(tuple._2(), tuple._3());
                    for (int cellId : cellIds) {
                        list.add(new Tuple2<>(cellId, tuple));
                    }
                    return list.iterator();
                }
            });

            JavaPairRDD<Integer, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>> pairRDDB = rddTuplesB.flatMapToPair(new PairFlatMapFunction<Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>, Integer, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>>> call(Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>>> list = new ArrayList<>();
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsBType(tuple._2(), tuple._3());
                    for (int cellId : cellIds) {
                        list.add(new Tuple2<>(cellId, tuple));
                    }
                    return list.iterator();
                }
            });

            JavaPairRDD<Integer, Tuple2<Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>, Tuple15<String, Double, Double, String, Double, String, Double, String, Double, String, Double, String, Double, String, Double>>> joinedRDD = pairRDDA.join(pairRDDB, new HashPartitioner(Integer.parseInt(args[4])));

            long count = joinedRDD.filter((t) -> {
                if (Math.pow((t._2._1._2() - t._2._2._2()), 2) + Math.pow((t._2._1._3() - t._2._2._3()), 2) <= Math.pow(radius, 2)) {
                    return true;
                } else {
                    return false;
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
