package gr.ds.unipi.epsilongrid;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.grid.*;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.functions$;
import org.apache.spark.sql.types.DataTypes;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.udf;

public class MetricsSynthetic {

    public static void main(String args[]) throws IOException {

        BufferedWriter w = new BufferedWriter(new FileWriter(/*"/Users/nicholaskoutroumanis/Desktop/metrics.txt"*/args[4]));
        w.write(  String.format("%-25s", "radius") + String.format("%-25s", "splits") + String.format("%-25s", "replication") + String.format("%-25s", "min(cost)") + String.format("%-25s", "max(cost)") + String.format("%-25s", "stddev(cost)") + String.format("%-25s", "sum(cost)") + String.format("%-25s", "avg(cost)") + String.format("%-25s", "min(abst)") + String.format("%-25s", "max(abst)") + String.format("%-25s", "stddev(abst)") + String.format("%-25s", "avg(abst)")+String.format("%-25s", "stddev(execCost)")+String.format("%-25s", "max(execCost)")+String.format("%-25s", "avg(execCost)"));
        w.newLine();

        DecimalFormat decf = new DecimalFormat("0.00");

        SparkConf sparkConf = new SparkConf().set("spark.serializer","org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{GenericGrid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class,  Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, java.util.HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, scala.reflect.ManifestFactory$.class,scala.reflect.ManifestFactory$.MODULE$.Any().getClass(), ConcurrentHashMap.class});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[2]")*/.getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

        //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
        String path = args[0];
        double radius = Double.parseDouble(args[1]);

//        EpsilonGrid grid = EpsilonGrid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), radius);
        GenericGrid grid = GenericGrid.newGenericGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, 1,true);

        Dataset<Row> df1 = sparkSession.read().option("delimiter", ";").csv(path + "datasetA"+args[2]+".csv").withColumnRenamed("_c0", "id_1").withColumnRenamed("_c1", "x_1").withColumnRenamed("_c2", "y_1");
        Dataset<Row> df2 = sparkSession.read().option("delimiter", ";").csv(path + "datasetB"+args[3]+".csv").withColumnRenamed("_c0", "id_2").withColumnRenamed("_c1", "x_2").withColumnRenamed("_c2", "y_2");

        //grid.load();

        System.out.println("Start Broadcasting");
        Broadcast<GenericGrid> gridBroadcasted = jsc.broadcast(grid);

        UserDefinedFunction udfCoordinatesToArrayA = udf(
                (String x, String y) -> gridBroadcasted.getValue().getPartitionsAType(Double.parseDouble(x), Double.parseDouble(y)), DataTypes.createArrayType(DataTypes.StringType)
        );

        UserDefinedFunction udfCoordinatesToArrayB = udf(
                (String x, String y) -> gridBroadcasted.getValue().getPartitionsBType(Double.parseDouble(x), Double.parseDouble(y)), DataTypes.createArrayType(DataTypes.StringType)
        );

        df1 = df1.withColumn("partitions", udfCoordinatesToArrayA.apply(col("x_1"),col("y_1")));
        df2 = df2.withColumn("partitions", udfCoordinatesToArrayB.apply(col("x_2"),col("y_2")));


        long replication = 0;

        replication = df1.agg(functions$.MODULE$.sum(functions$.MODULE$.size(functions$.MODULE$.col("partitions")).$minus(1))).collectAsList().get(0).getLong(0);

        df1= df1.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).groupBy("partitions").agg(functions$.MODULE$.count("partitions").as("atype")).toDF();

        replication = replication + df2.agg(functions$.MODULE$.sum(functions$.MODULE$.size(functions$.MODULE$.col("partitions")).$minus(1))).collectAsList().get(0).getLong(0);

        df2 = df2.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).groupBy("partitions").agg(functions$.MODULE$.count("partitions").as("btype")).toDF();

        Dataset<Row> df = df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").na().fill(0, new String[]{"atype", "btype"}).withColumn("cost", functions$.MODULE$.col("atype").multiply(functions$.MODULE$.col("btype")))
                .withColumn("abstraction", functions$.MODULE$.abs(functions$.MODULE$.col("atype").$minus(functions$.MODULE$.col("btype")))).agg(functions$.MODULE$.min("cost"), functions$.MODULE$.max("cost"), functions$.MODULE$.stddev_pop("cost"), functions$.MODULE$.sum("cost"), functions$.MODULE$.avg("cost"), functions$.MODULE$.min("abstraction"), functions$.MODULE$.max("abstraction"), functions$.MODULE$.stddev_pop("abstraction"), functions$.MODULE$.avg("abstraction"));

        //df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").na().fill(0, new String[]{"atype", "btype"}).show();
        //dfModulo.show();

        System.out.println("Replication: "+replication);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sb.append((df.collectAsList().get(0).get(i) instanceof Double)?String.format("%-25s",decf.format(df.collectAsList().get(0).getDouble(i))):String.format("%-25s",df.collectAsList().get(0).getLong(i)));
        }

        df.unpersist();
        w.write(String.format("%-25s",radius) + String.format("%-25s", grid.getCellsInXAxis() + "X" + grid.getCellsInYAxis()) + String.format("%-25s",replication) + sb);
        w.newLine();

        w.close();

        gridBroadcasted.unpersist(true);
        df1.unpersist();
        df2.unpersist();
        sparkSession.close();
    }
}