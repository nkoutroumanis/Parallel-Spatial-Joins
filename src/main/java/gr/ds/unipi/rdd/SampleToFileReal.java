package gr.ds.unipi.rdd;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.grid.Cell;
import gr.ds.unipi.grid.Grid;
import gr.ds.unipi.grid.NewFunc;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Function4;
import scala.Tuple3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SampleToFileReal {
    public static void main(String args[]) throws IOException {

        File file = new File("/home/user/samples/"+args[1]+".csv");
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/user/samples/"+args[1]+".csv"));

        SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, scala.reflect.ManifestFactory$.class, scala.reflect.ManifestFactory$.MODULE$.Any().getClass(), ConcurrentHashMap.class, Function4.class});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

        JavaRDD<String> rddTxtFiles = jsc.textFile(args[0] + args[1] + ".csv");

        JavaRDD<Tuple3<String, Double, Double>> rddTuples = rddTxtFiles.map((String line) -> {
            String[] elements = line.split("\t");
            return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
        });

        rddTuples.sample(false, Double.parseDouble(args[2]), 261).collect().forEach(tuple->{
            try {
                writer.write(tuple._1()+"\t"+tuple._2()+"\t"+tuple._3());
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.close();
        jsc.stop();
        sparkSession.stop();
    }
}
