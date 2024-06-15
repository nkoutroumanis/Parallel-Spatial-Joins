package gr.ds.unipi.sampling;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.grid.*;
import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import scala.Tuple3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GridTimeDist {

    public static void main(String args[]) {
        SparkConf sparkConf = new SparkConf().set("spark.serializer","org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class,  Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, java.util.HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(sparkSession.sparkContext());

        //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
        String path = args[0];
        double radius = Double.parseDouble(args[1]);
        int flag = Integer.parseInt(args[2]);

        TriFunction<Cell, Cell, Point, Agreement> function = null;
        if(flag==1){
            function = NewFunc.datasetA;
        }else if(flag==2){
            function = NewFunc.datasetB;
        }else if(flag==3){
            function = NewFunc.costBasedCombinedWithBoundaries;
        }else if(flag == 4){
            function = NewFunc.lesserPointsinBoundaries;
        }else if(flag == 5){
            function = NewFunc.dok;
        }else if(flag == 6){
            function = NewFunc.dok1;
        }else if(flag == 7){
            function = NewFunc.dok2;
        }else if(flag == 8){
            function = NewFunc.costBasedBackpropagation;
        }else if(flag == 9){
            function = NewFunc.case3Backpropagation;
        }
        else{
            try {
                throw new Exception("Wrong Flag");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Grid.experiments = "DIAG_PR";
        CustomPartitioner cp = new CustomPartitioner(24);

        long startIndexingTime = System.currentTimeMillis();
        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), radius, function);

        JavaRDD<String> rddTxtFilesA = jsc.textFile(path + "datasetA.csv");
        JavaRDD<String> rddTxtFilesB = jsc.textFile(path + "datasetB.csv");

        rddTxtFilesA.sample(false, 0.00002, 200).collect().forEach(t-> System.out.println(t));
        rddTxtFilesB.sample(false, 0.00002, 200).collect().forEach(t-> System.out.println(t));

        jsc.close();
        sparkSession.close();
    }

}