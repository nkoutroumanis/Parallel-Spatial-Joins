import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.grid.Cell;
import gr.ds.unipi.grid.Grid;
import gr.ds.unipi.grid.NewFunc;
import gr.ds.unipi.grid.TriFunction;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GridRunSkewed {

    public static void main(String[] args) throws IOException {
        SparkSession sparkSession = SparkSession.builder().master("local[*]").getOrCreate();

        String path = args[0];
        double radius = Double.parseDouble(args[1]);
        DecimalFormat decf = new DecimalFormat("0.00");

        Files.createDirectories(Paths.get(path+File.separator+decf.format(radius)));
        Files.createDirectories(Paths.get(path+File.separator+decf.format(radius)+File.separator+"universalA"));
        Files.createDirectories(Paths.get(path+File.separator+decf.format(radius)+File.separator+"universalB"));
        Files.createDirectories(Paths.get(path+File.separator+decf.format(radius)+File.separator+"cost"));
        Files.createDirectories(Paths.get(path+File.separator+decf.format(radius)+File.separator+"lpib"));

        List<Map.Entry<String, Map.Entry<NewFunc, String>>> s = Arrays.asList(new Map.Entry[]{
                new AbstractMap.SimpleEntry(path+File.separator+decf.format(radius)+File.separator+"universalA"+File.separator, new AbstractMap.SimpleEntry(NewFunc.datasetA, "")),
                new AbstractMap.SimpleEntry(path+File.separator+decf.format(radius)+File.separator+"universalB"+File.separator, new AbstractMap.SimpleEntry(NewFunc.datasetB, "")),
                new AbstractMap.SimpleEntry(path+File.separator+decf.format(radius)+File.separator+"cost"+File.separator, new AbstractMap.SimpleEntry(NewFunc.costBasedCombinedWithBoundaries, "DIAG_PR")),
                new AbstractMap.SimpleEntry(path+File.separator+decf.format(radius)+File.separator+"lpib"+File.separator, new AbstractMap.SimpleEntry(NewFunc.lesserPointsinBoundaries, "DIAG_PR"))
        });

        for (Map.Entry<String, Map.Entry<NewFunc, String>> entry : s) {

            //NewFunc.function = (TriFunction<Cell, Cell, Point, Agreement>) entry.getValue().getKey();

            BufferedWriter w = new BufferedWriter(new FileWriter(entry.getKey()+"metrics.txt"));
            w.write(  String.format("%-18s", "radius") + String.format("%-18s", "splits") + String.format("%-18s", "replication") + String.format("%-18s", "min(cost)") + String.format("%-18s", "max(cost)") + String.format("%-18s", "stddev_pop(cost)") + String.format("%-18s", "sum(cost)") + String.format("%-18s", "avg(cost)") + String.format("%-18s", "min(abst)") + String.format("%-18s", "max(abst)") + String.format("%-18s", "stddev_pop(abst)") + String.format("%-18s", "avg(abst)"));
            w.newLine();
            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), radius,(TriFunction<Cell, Cell, Point, Agreement>) entry.getValue().getKey());

            String line = null;

                BufferedReader bf = new BufferedReader(new FileReader(path+File.separator+"datasetA.csv"));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    grid.addPointDatasetA(Double.parseDouble(record[1]), Double.parseDouble(record[2]));
                }
                bf.close();

                bf = new BufferedReader(new FileReader(path+File.separator+"datasetB.csv"));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    grid.addPointDatasetB(Double.parseDouble(record[1]), Double.parseDouble(record[2]));
                }
                bf.close();

                BufferedWriter wr = new BufferedWriter(new FileWriter(entry.getKey()+"datasetA.csv"));
                bf = new BufferedReader(new FileReader(path+File.separator+"datasetA.csv"));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    wr.write(line + ";" + Arrays.toString(grid.getPartitionsAType(Double.parseDouble(record[1]), Double.parseDouble(record[2]))));
                    wr.newLine();
                }
                bf.close();
                wr.close();

                wr = new BufferedWriter(new FileWriter(entry.getKey()+"datasetB.csv"));
                bf = new BufferedReader(new FileReader(path+File.separator+"datasetB.csv"));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    wr.write(line + ";" + Arrays.toString(grid.getPartitionsBType(Double.parseDouble(record[1]), Double.parseDouble(record[2]))));
                    wr.newLine();
                }
                bf.close();
                wr.close();

                long replication = 0;
                Dataset<Row> df1 = sparkSession.read().option("delimiter", ";").csv(entry.getKey()+"datasetA.csv").withColumnRenamed("_c0", "id_1").withColumnRenamed("_c1", "x").withColumnRenamed("_c2", "y")
                        .withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("_c3"), "\\[", "")).withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("partitions"), "\\]", ""))
                        .drop("_c3").withColumn("partitions", functions$.MODULE$.split(functions$.MODULE$.col("partitions"), ", "));

                replication = df1.agg(functions$.MODULE$.sum(functions$.MODULE$.size(functions$.MODULE$.col("partitions")).$minus(1))).collectAsList().get(0).getLong(0);

                df1= df1.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).groupBy("partitions").agg(functions$.MODULE$.count("partitions").as("atype")).toDF();

                Dataset<Row> df2 = sparkSession.read().option("delimiter", ";").csv(entry.getKey()+"datasetB.csv").withColumnRenamed("_c0", "id_2").withColumnRenamed("_c1", "x").withColumnRenamed("_c2", "y")
                        .withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("_c3"), "\\[", "")).withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("partitions"), "\\]", ""))
                        .drop("_c3").withColumn("partitions", functions$.MODULE$.split(functions$.MODULE$.col("partitions"), ", "));

                replication = replication + df2.agg(functions$.MODULE$.sum(functions$.MODULE$.size(functions$.MODULE$.col("partitions")).$minus(1))).collectAsList().get(0).getLong(0);

                df2 = df2.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).groupBy("partitions").agg(functions$.MODULE$.count("partitions").as("btype")).toDF();

                Dataset<Row> df = df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").na().fill(1, new String[]{"atype", "btype"}).withColumn("cost", functions$.MODULE$.col("atype").multiply(functions$.MODULE$.col("btype")))
                        .withColumn("abstraction", functions$.MODULE$.abs(functions$.MODULE$.col("atype").$minus(functions$.MODULE$.col("btype")))).agg(functions$.MODULE$.min("cost"), functions$.MODULE$.max("cost"), functions$.MODULE$.stddev_pop("cost"), functions$.MODULE$.sum("cost"), functions$.MODULE$.avg("cost"), functions$.MODULE$.min("abstraction"), functions$.MODULE$.max("abstraction"), functions$.MODULE$.stddev_pop("abstraction"), functions$.MODULE$.avg("abstraction"));

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 9; i++) {
                    sb.append((df.collectAsList().get(0).get(i) instanceof Double)?String.format("%-18s",decf.format(df.collectAsList().get(0).getDouble(i))):String.format("%-18s",df.collectAsList().get(0).getLong(i)));
                }

                w.write(String.format("%-18s",radius) + String.format("%-18s", grid.getCellsInXAxis() + "X" + grid.getCellsInYAxis()) + String.format("%-18s",replication) + sb);
                w.newLine();

            w.close();
        }
    }
}