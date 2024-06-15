package gr.ds.unipi.rdd;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.grid.*;
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
import scala.Function4;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.convert.JavaCollectionWrappers;
import scala.collection.convert.JavaCollectionWrappers$;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GridTimeRealsPrunedBr {

    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;
        int repeats = Integer.parseInt(args[4]);

        for (int n = 0; n < repeats; n++) {
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");//.set("spark.kryo.registrationRequired", "true")
                    //.registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, scala.reflect.ManifestFactory$.class, scala.reflect.ManifestFactory$.MODULE$.Any().getClass(), ConcurrentHashMap.class, Function4.class, JavaCollectionWrappers$.class, JavaCollectionWrappers.class});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

            //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            long startJobTime = System.currentTimeMillis();

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

//            if (!(flag == 1 || flag == 2)) {
//
//                long o = System.currentTimeMillis();
//                rddTuplesA.sample(false, Double.parseDouble(args[7]), 261).collect().forEach((tuple) ->
//                        grid.addPointDatasetA(tuple._2(), tuple._3())
//                );
//
//                rddTuplesB.sample(false, Double.parseDouble(args[7]), 261).collect().forEach((tuple) ->
//                        grid.addPointDatasetB(tuple._2(), tuple._3())
//                );
//                sampleTime = sampleTime + (System.currentTimeMillis() - o);
//                System.out.println("OK with sampling Time:" + (System.currentTimeMillis() - o));
//                if (flag == 8 || flag == 9) {
//                    grid.load();
//                }
//            }

            //grid.printInfo();

            Broadcast<List<Tuple3<String,Double,Double>>> broadcasted = jsc.broadcast(rddTuplesA.collect());

            final Function4<Double, Double, Double, Double, Double> distanceKM = (Function4<Double, Double, Double, Double, Double> & Serializable) (lon1, lat1, lon2, lat2) -> {
                double dLat = Math.toRadians(lat2 - lat1);
                double dLon = Math.toRadians(lon2 - lon1);

                lat1 = Math.toRadians(lat1);
                lat2 = Math.toRadians(lat2);

                double a = Math.pow(Math.sin(dLat / 2), 2) +
                        Math.pow(Math.sin(dLon / 2), 2) *
                                Math.cos(lat1) *
                                Math.cos(lat2);
                double rad = 6371;
                double c = 2 * Math.asin(Math.sqrt(a));
                return rad * c;
            };

            JavaRDD<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results = rddTuplesB.repartition(96).flatMap(new FlatMapFunction<Tuple3<String, Double, Double>, Tuple2<Tuple3<String, Double, Double>, Tuple3<String, Double, Double>>>() {
                @Override
                public Iterator<Tuple2<Tuple3<String, Double, Double>, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple1) throws Exception {
                    List<Tuple2<Tuple3<String, Double, Double>, Tuple3<String, Double, Double>>> list = new ArrayList<>();

                    broadcasted.value().forEach(tuple2->{
                        if (distanceKM.apply(tuple1._2(), tuple1._3(), tuple2._2(), tuple2._3()) <= radius) {
                            list.add(new Tuple2<>(tuple1, tuple2));
                        }
                    });
                    return list.iterator();
                }
            });

            cou = results.count();

            time = time + (System.currentTimeMillis() - startJobTime);

//            System.out.println("Job Time: " + (System.currentTimeMillis() - startJobTime));
//            System.out.println("Count: " + count);

            jsc.stop();
            sparkSession.stop();
        }
        System.out.println("Time Exec: "+ time/repeats);
        System.out.println("Count: "+cou);
    }
}
