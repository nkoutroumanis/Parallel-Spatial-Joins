package gr.ds.unipi.grid;

import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;
import org.apache.spark.storage.StorageLevel;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridRunSkewedTime {

    @Test
    public void newGrid() throws IOException {
        SparkSession sparkSession = SparkSession.builder().master("local[*]").getOrCreate();


        sparkSession.conf().set("rdd.sql.join.preferSortMergeJoin",true);
        sparkSession.conf().set("rdd.sql.autoBroadcastJoinThreshold",0);
                double r1 = 1.05;
                System.out.println(r1);

                String pathNewDatasetA = "/Users/nicholaskoutroumanis/Desktop/joins/datasetA.csv";
                String pathNewDatasetB = "/Users/nicholaskoutroumanis/Desktop/joins/datasetB.csv";

                Dataset<Row> df1 = sparkSession.read().option("delimiter", ";").csv(pathNewDatasetA).withColumnRenamed("_c0", "x_1").withColumnRenamed("_c1", "y_1")
                .withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("_c2"), "\\[", "")).withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("partitions"), "\\]", ""))
                .drop("_c2").withColumn("partitions", functions$.MODULE$.split(functions$.MODULE$.col("partitions"), ", ")).withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).persist(StorageLevel.MEMORY_ONLY());

        Dataset<Row> df2 = sparkSession.read().option("delimiter", ";").csv(pathNewDatasetB).withColumnRenamed("_c0", "x_2").withColumnRenamed("_c1", "y_2")
                .withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("_c2"), "\\[", "")).withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("partitions"), "\\]", ""))
                .drop("_c2").withColumn("partitions", functions$.MODULE$.split(functions$.MODULE$.col("partitions"), ", ")).withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).persist(StorageLevel.MEMORY_ONLY());

        System.out.println("Matched Pairs: "+df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(r1),functions$.MODULE$.lit(2))) ).count());
        for (int i = 0; i < 2; i++) {
            df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(r1),functions$.MODULE$.lit(2))) ).count();
        }

        List<Long> execTime = new ArrayList<>();
        long time;
        for (int i = 0; i < 30; i++) {
            time = System.currentTimeMillis();
            df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(r1),functions$.MODULE$.lit(2))) ).count();
            execTime.add(System.currentTimeMillis()-time);
        }
        System.out.println(execTime.stream().mapToLong(i->i).average().getAsDouble()/1000);
        System.out.println(((double)execTime.stream().mapToLong(i->i).max().getAsLong())/1000);
        System.out.println(((double)execTime.stream().mapToLong(i->i).min().getAsLong())/1000);

        //df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(r1),functions$.MODULE$.lit(2))) ).explain(true);

    }

}