package gr.ds.unipi.grid;

import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

public class GridRunSkewed2 {

    @Test
    public void newGrid() throws IOException {
        SparkSession sparkSession = SparkSession.builder().master("local[*]").getOrCreate();

//        Grid.experiments = "DIAG_COMP";
        Grid.experiments = "DIAG_PR";
//        Grid.experiments = "DIAG_ONLY";
//        Grid.experiments = "DEFAULT";

        //NewFunc.function = NewFunc.costBasedCombinedWithBoundaries;
                double r1 = 1.05;
                //r1 = ((Math.floor(r1 * Math.pow(10, 2))) / Math.pow(10, 2));
                System.out.println(r1);
                Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), r1, NewFunc.costBasedCombinedWithBoundaries);

                String line = null;

                String pathDatasetA = "/Users/nicholaskoutroumanis/Desktop/joins/skewed/datasetA.csv";
                String pathDatasetB = "/Users/nicholaskoutroumanis/Desktop/joins/skewed/datasetB.csv";

                String pathNewDatasetA = "/Users/nicholaskoutroumanis/Desktop/joins/datasetA.csv";
                String pathNewDatasetB = "/Users/nicholaskoutroumanis/Desktop/joins/datasetB.csv";

                BufferedReader bf = new BufferedReader(new FileReader(pathDatasetA));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    grid.addPointDatasetA(Double.parseDouble(record[0]), Double.parseDouble(record[1]));
                }
                bf.close();

                bf = new BufferedReader(new FileReader(pathDatasetB));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    grid.addPointDatasetB(Double.parseDouble(record[0]), Double.parseDouble(record[1]));
                }
                bf.close();

                grid.checkforsymmetry();
                BufferedWriter wr = new BufferedWriter(new FileWriter(pathNewDatasetA));
                bf = new BufferedReader(new FileReader(pathDatasetA));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    wr.write(line + ";" + Arrays.toString(grid.getPartitionsAType(Double.parseDouble(record[0]), Double.parseDouble(record[1]))));
                    wr.newLine();
                }
                bf.close();
                wr.close();

                wr = new BufferedWriter(new FileWriter(pathNewDatasetB));
                bf = new BufferedReader(new FileReader(pathDatasetB));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    wr.write(line + ";" + Arrays.toString(grid.getPartitionsBType(Double.parseDouble(record[0]), Double.parseDouble(record[1]))));
                    wr.newLine();
                }
                bf.close();
                wr.close();

        long replication = 0;
        Dataset<Row> df1 = sparkSession.read().option("delimiter", ";").csv("/Users/nicholaskoutroumanis/Desktop/joins/datasetA.csv").withColumnRenamed("_c0", "x").withColumnRenamed("_c1", "y")
                .withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("_c2"), "\\[", "")).withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("partitions"), "\\]", ""))
                .drop("_c2").withColumn("partitions", functions$.MODULE$.split(functions$.MODULE$.col("partitions"), ", "));

        replication = df1.agg(functions$.MODULE$.sum(functions$.MODULE$.size(functions$.MODULE$.col("partitions")).$minus(1))).collectAsList().get(0).getLong(0);

        df1= df1.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).groupBy("partitions").agg(functions$.MODULE$.count("partitions").as("atype")).toDF();

        Dataset<Row> df2 = sparkSession.read().option("delimiter", ";").csv("/Users/nicholaskoutroumanis/Desktop/joins/datasetB.csv").withColumnRenamed("_c0", "x").withColumnRenamed("_c1", "y")
                .withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("_c2"), "\\[", "")).withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("partitions"), "\\]", ""))
                .drop("_c2").withColumn("partitions", functions$.MODULE$.split(functions$.MODULE$.col("partitions"), ", "));

        replication = replication + df2.agg(functions$.MODULE$.sum(functions$.MODULE$.size(functions$.MODULE$.col("partitions")).$minus(1))).collectAsList().get(0).getLong(0);

        df2 = df2.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).groupBy("partitions").agg(functions$.MODULE$.count("partitions").as("btype")).toDF();

        Dataset<Row> df = df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").na().fill(1, new String[]{"atype", "btype"}).withColumn("cost", functions$.MODULE$.col("atype").multiply(functions$.MODULE$.col("btype")))
                .withColumn("abstraction", functions$.MODULE$.abs(functions$.MODULE$.col("atype").$minus(functions$.MODULE$.col("btype")))).agg(functions$.MODULE$.min("cost"), functions$.MODULE$.max("cost"), functions$.MODULE$.stddev_pop("cost"), functions$.MODULE$.sum("cost"), functions$.MODULE$.avg("cost"), functions$.MODULE$.min("abstraction"), functions$.MODULE$.max("abstraction"), functions$.MODULE$.stddev_pop("abstraction"), functions$.MODULE$.avg("abstraction"));

        System.out.println("Replication: " +replication);
        df.show();
    }
}