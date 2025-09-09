package gr.ds.unipi.self;

import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.rdd.LPTPartitioner;
import gr.ds.unipi.rdd.LPTPartitionerBr;
import gr.ds.unipi.self.agreements.Agreements;
import gr.ds.unipi.self.agreements.Edge;
import gr.ds.unipi.self.agreements.Space;
import gr.ds.unipi.self.grid.Cell;
import gr.ds.unipi.self.grid.ReplicationFunc;
import gr.ds.unipi.self.grid.SelfGrid;
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
import scala.reflect.ManifestFactory$;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CorrectnessTest {
    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;

            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{SelfGrid.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, ReplicationFunc.class, Position.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, /*org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, */ManifestFactory$.MODULE$.Any().getClass()/*, ConcurrentHashMap.class, java.util.function.Function.class*/});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
            //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            long startJobTime = System.currentTimeMillis();

            JavaRDD<String> rddTxtFilesA = jsc.textFile(path + args[2] + ".csv"/*,Integer.parseInt(args[5])*/);

            List<Tuple3<String, Double, Double>> objectsB = rddTxtFilesA.map((String line) -> {
                String[] elements = line.split("\t");
                return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
            }).collect();

            long count = 0;
            for (int i = 0; i < objectsB.size()-1; i++) {
                for (int j = i+1; j < objectsB.size(); j++) {
                    if((Math.pow((objectsB.get(i)._2() - objectsB.get(j)._2()), 2) + Math.pow((objectsB.get(i)._3()- objectsB.get(j)._3()), 2) <= Math.pow(radius, 2))){
                        count++;
                    }
                }
            }

            time = time + (System.currentTimeMillis() - startJobTime);
            cou = count;

            jsc.close();
            sparkSession.close();

        System.out.println("Time Exec: "+ time);
        System.out.println("Count: "+cou);

    }

}
