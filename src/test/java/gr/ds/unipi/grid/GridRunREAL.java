package gr.ds.unipi.grid;

import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;
import org.junit.Test;

import java.io.*;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GridRunREAL {

    @Test
    public void newGrid() throws IOException {
        SparkSession sparkSession = SparkSession.builder().master("local[*]").getOrCreate();
        //List<Map.Entry<String, Functions>> s = Arrays.asList(new Map.Entry[]{new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/trial.txt", Functions.trial)});

        List<Map.Entry<String, Map.Entry<NewFunc, String>>> s = Arrays.asList(new Map.Entry[]{
                new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/universalA.txt", new AbstractMap.SimpleEntry(NewFunc.datasetA, "")), new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/universalB.txt", new AbstractMap.SimpleEntry(NewFunc.datasetB, "")),
                new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/lesserPointsinBoundariesDEFAULT.txt", new AbstractMap.SimpleEntry(NewFunc.lesserPointsinBoundaries, "DEFAULT")), new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/lesserPointsinBoundariesDIAG_PR.txt", new AbstractMap.SimpleEntry(NewFunc.lesserPointsinBoundaries, "DIAG_PR")), new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/lesserPointsinBoundariesDIAG_ONLY.txt", new AbstractMap.SimpleEntry(NewFunc.lesserPointsinBoundaries, "DIAG_ONLY")),
                new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/costBasedCombinedWithBoundariesDEFAULT.txt", new AbstractMap.SimpleEntry(NewFunc.costBasedCombinedWithBoundaries, "DEFAULT")), new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/costBasedCombinedWithBoundariesDIAG_PR.txt", new AbstractMap.SimpleEntry(NewFunc.costBasedCombinedWithBoundaries, "DIAG_PR")), new AbstractMap.SimpleEntry("/Users/nicholaskoutroumanis/Desktop/joins/costs/costBasedCombinedWithBoundariesDIAG_ONLY.txt", new AbstractMap.SimpleEntry(NewFunc.costBasedCombinedWithBoundaries, "DIAG_ONLY"))
        });

        DecimalFormat decf = new DecimalFormat("0.000");

        for (Map.Entry<String, Map.Entry<NewFunc, String>> entry : s) {

            String path = entry.getKey();
            //NewFunc.function = (TriFunction<Cell, Cell, Point, Agreement>) entry.getValue().getKey();
            Grid.experiments = entry.getValue().getValue();

            BufferedWriter w = new BufferedWriter(new FileWriter(path));
            w.write(  String.format("%-18s", "radius") + String.format("%-18s", "splits") + String.format("%-18s", "replication") + String.format("%-18s", "min(cost)") + String.format("%-18s", "max(cost)") + String.format("%-18s", "stddev_pop(cost)") + String.format("%-18s", "sum(cost)") + String.format("%-18s", "avg(cost)") + String.format("%-18s", "min(abst)") + String.format("%-18s", "max(abst)") + String.format("%-18s", "stddev_pop(abst)") + String.format("%-18s", "avg(abst)"));
            w.newLine();
            for (double r = 25; r <= 100; r = r + 4) {

                double r1 = (r/6378.1);
                r1 = ((Math.floor(r1 * Math.pow(10, 4))) / Math.pow(10, 4));
                System.out.println(r1);
                Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(23.184,37.69), Point.newPoint(24.10,38.34)), r1,(Function4<Cell, Cell, Point, Agreement>) entry.getValue().getKey());

                String line = null;

                String pathDatasetA = "/Users/nicholaskoutroumanis/Desktop/joins/real/cafe.csv";
                String pathDatasetB = "/Users/nicholaskoutroumanis/Desktop/joins/real/fuel.csv";

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
                    wr.write(line + ";" + Arrays.toString(grid.getPartitionsATypeInExecutor(Double.parseDouble(record[0]), Double.parseDouble(record[1]))));
                    wr.newLine();
                }
                bf.close();
                wr.close();

                wr = new BufferedWriter(new FileWriter(pathNewDatasetB));
                bf = new BufferedReader(new FileReader(pathDatasetB));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    wr.write(line + ";" + Arrays.toString(grid.getPartitionsBTypeInExecutor(Double.parseDouble(record[0]), Double.parseDouble(record[1]))));
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

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 9; i++) {
                    sb.append((df.collectAsList().get(0).get(i) instanceof Double)?String.format("%-18s",decf.format(df.collectAsList().get(0).getDouble(i))):String.format("%-18s",df.collectAsList().get(0).getLong(i)));
                }

                w.write(String.format("%-18s",r) + String.format("%-18s", grid.getCellsInXAxis() + "X" + grid.getCellsInYAxis()) + String.format("%-18s",replication) + sb);
                w.newLine();

            }
            w.close();
        }
    }
}