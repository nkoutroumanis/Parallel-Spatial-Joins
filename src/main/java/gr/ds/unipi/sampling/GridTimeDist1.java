package gr.ds.unipi.sampling;

import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.grid.Cell;
import gr.ds.unipi.grid.Grid;
import gr.ds.unipi.grid.NewFunc;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.udf;

public class GridTimeDist1 {

    public static void main(String args[]) {
        SparkConf sparkConf = new SparkConf()/*.set("spark.serializer","org.apache.spark.serializer.KryoSerializer")*/.registerKryoClasses(new Class[]{Grid.class});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[2]")*/.getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(sparkSession.sparkContext());

        String path = args[0];
        double radius = Double.parseDouble(args[1]);
        int flag = Integer.parseInt(args[2]);

//        ReplicationType function = null;
//        if (flag == 1) {
//            function = new DatasetA();
//        }
            gr.ds.unipi.grid.Function4<Cell, Cell, Point, Agreement> function = null;
            if (flag == 1) {
                function = NewFunc.datasetA;
            } else if (flag == 2) {
                function = NewFunc.datasetB;
            } else if (flag == 3) {
                function = NewFunc.costBasedCombinedWithBoundaries;
            } else if (flag == 4) {
                function = NewFunc.lesserPointsinBoundaries;
            } else if (flag == 5) {
                function = NewFunc.dok;
            } else if (flag == 6) {
                function = NewFunc.dok1;
            } else if (flag == 7) {
                function = NewFunc.dok2;
            } else if (flag == 8) {
                function = NewFunc.costBasedBackpropagation;
            } else if (flag == 9) {
                function = NewFunc.case3Backpropagation;
            } else {
                try {
                    throw new Exception("Wrong Flag");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        Grid.experiments = "DIAG_PR";

        long startIndexingTime = System.currentTimeMillis();
        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), radius, function);

        Dataset<Row> df1 = sparkSession.read().option("delimiter", ";").csv(path + "datasetA.csv").withColumnRenamed("_c0", "id_1").withColumnRenamed("_c1", "x_1").withColumnRenamed("_c2", "y_1");
        Dataset<Row> df2 = sparkSession.read().option("delimiter", ";").csv(path + "datasetB.csv").withColumnRenamed("_c0", "id_2").withColumnRenamed("_c1", "x_2").withColumnRenamed("_c2", "y_2");

        df1.sample(false, 0.00002, 200).collectAsList().forEach(row-> System.out.println((String) row.getAs("id_1")));
        df2.sample(false, 0.00002, 200).collectAsList().forEach(row-> System.out.println((String) row.getAs("id_2")));

        df1.unpersist();
        df2.unpersist();
        sparkSession.close();
    }



}