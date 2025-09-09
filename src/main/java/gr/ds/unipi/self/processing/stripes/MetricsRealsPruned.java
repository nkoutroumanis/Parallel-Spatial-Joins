package gr.ds.unipi.self.processing.stripes;

import gr.ds.unipi.self.agreements.Agreements;
import gr.ds.unipi.self.agreements.Edge;
import gr.ds.unipi.self.agreements.Space;
import gr.ds.unipi.self.grid.Cell;
import gr.ds.unipi.self.processing.ReplicationFunc;
import gr.ds.unipi.self.processing.SelfGrid;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.functions$;
import org.apache.spark.sql.types.DataTypes;
import scala.Tuple3;
import scala.reflect.ManifestFactory$;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.udf;

public class MetricsRealsPruned {

    public static void main(String args[]) throws IOException {

        BufferedWriter w = new BufferedWriter(new FileWriter("./metrics-"+args[2] + "-"+args[1]+"-"+"sgoa-1D-x"+".txt"));
        w.write(  String.format("%-25s", "radius") + String.format("%-25s", "splits") + String.format("%-25s", "replication") + String.format("%-25s", "min(cost)") + String.format("%-25s", "max(cost)") + String.format("%-25s", "stddev(cost)") + String.format("%-25s", "sum(cost)") + String.format("%-25s", "avg(cost)") + String.format("%-25s", "min(abst)") + String.format("%-25s", "max(abst)") + String.format("%-25s", "stddev(abst)") + String.format("%-25s", "avg(abst)")+String.format("%-25s", "stddev(execCost)")+String.format("%-25s", "max(execCost)")+String.format("%-25s", "avg(execCost)"));
        w.newLine();

        DecimalFormat decf = new DecimalFormat("0.00");

        SparkConf sparkConf = new SparkConf().set("spark.serializer","org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{SelfGrid.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, ReplicationFunc.class, Position.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, gr.ds.unipi.self.processing.Cell.class, /*org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, */ManifestFactory$.MODULE$.Any().getClass()/*, ConcurrentHashMap.class, java.util.function.Function.class*/});

        SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[2]")*/.getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

        //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
        String path = args[0];
        double radius = Double.parseDouble(args[1]);

        //CHANGE HERE
        //Grid grid = Grid.newGeoGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
        SelfGrid grid = SelfGrid.newGridStripesX(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, ReplicationFunc.lesserPointsinBoundaries);

        Dataset<Row> df1 = sparkSession.read().option("delimiter", "\t").csv(path +args[2]+".csv").withColumnRenamed("_c0", "id_1").withColumnRenamed("_c1", "x_1").withColumnRenamed("_c2", "y_1");

        JavaRDD<String> rddTxtFilesA = jsc.textFile(path +args[2]+".csv");

        JavaRDD<Tuple3<String, Double, Double>> rddTuplesA = rddTxtFilesA.map((String line)->{
            String[] elements = line.split("\t");
            return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
        });

            //for(int i=0;i<90;i++){
        List<Tuple3<String, Double, Double>> sampleTuples = rddTuplesA.sample(false, Double.parseDouble(args[3]),261).collect();

        System.out.println(sampleTuples.size());
        sampleTuples.forEach((tuple) -> grid.addPointDataset(tuple._2(), tuple._3()));

        grid.printInfo();
        grid.getCellsWithCosts1();
        grid.printInfo();

        Broadcast<SelfGrid> gridBroadcasted = jsc.broadcast(grid);
        System.out.println("Broadcasted");

        UserDefinedFunction udfCoordinatesToArray = udf(
                (String x, String y) -> gridBroadcasted.getValue().getPartitions(Double.parseDouble(x), Double.parseDouble(y)), DataTypes.createArrayType(DataTypes.IntegerType)
        );

        UserDefinedFunction udfCoordinatesToHomeId = udf(
                (String x, String y) -> gridBroadcasted.getValue().getPartitions(Double.parseDouble(x), Double.parseDouble(y)), DataTypes.IntegerType
        );


        df1 = df1.withColumn("partitions", udfCoordinatesToArray.apply(col("x_1"),col("y_1")));
        df1 = df1.withColumn("homeCell", udfCoordinatesToHomeId.apply(col("x_1"),col("y_1")));


        long replication = 0;

        replication = df1.agg(functions$.MODULE$.sum(functions$.MODULE$.size(functions$.MODULE$.col("partitions")).$minus(1))).collectAsList().get(0).getLong(0);

        df1= df1.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions"))).groupBy("partitions").agg(functions$.MODULE$.count("partitions").as("atype"));

        Dataset<Row> df = df1;

//        Dataset<Row> df = df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").na().fill(0, new String[]{"atype", "btype"}).withColumn("cost", functions$.MODULE$.col("atype").multiply(functions$.MODULE$.col("btype")))
//                .withColumn("abstraction", functions$.MODULE$.abs(functions$.MODULE$.col("atype").$minus(functions$.MODULE$.col("btype")))).agg(functions$.MODULE$.min("cost"), functions$.MODULE$.max("cost"), functions$.MODULE$.stddev_pop("cost"), functions$.MODULE$.sum("cost"), functions$.MODULE$.avg("cost"), functions$.MODULE$.min("abstraction"), functions$.MODULE$.max("abstraction"), functions$.MODULE$.stddev_pop("abstraction"), functions$.MODULE$.avg("abstraction")).cache();

        ///System.out.println("PRINT DATAFRAME");
        //df.show();

        //Dataset<Row> dfModulo = df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").na().fill(0, new String[]{"atype", "btype"}).drop(df2.col("partitions")).withColumn("cost", functions$.MODULE$.col("atype").multiply(functions$.MODULE$.col("btype"))).groupBy(functions$.MODULE$.col("partitions").mod(Integer.parseInt(args[5]))).agg(functions$.MODULE$.sum("cost")).agg(functions$.MODULE$.stddev_pop("sum(cost)"), functions$.MODULE$.max("sum(cost)"), functions$.MODULE$.avg("sum(cost)"));

        System.out.println("Replication: "+replication);
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 9; i++) {
//            sb.append((df.collectAsList().get(0).get(i) instanceof Double)?String.format("%-25s",decf.format(df.collectAsList().get(0).getDouble(i))):String.format("%-25s",df.collectAsList().get(0).getLong(i)));
//        }

//        for (int i = 0; i < 3; i++) {
//            sb.append((dfModulo.collectAsList().get(0).get(i) instanceof Double)?String.format("%-25s",decf.format(dfModulo.collectAsList().get(0).getDouble(i))):String.format("%-25s",dfModulo.collectAsList().get(0).getLong(i)));
//        }

        df.unpersist();
        w.write(String.format("%-25s",radius) + String.format("%-25s", grid.getCellsInXAxis() + "X" + grid.getCellsInYAxis()) + String.format("%-25s",replication));
        w.newLine();

        w.close();

        gridBroadcasted.unpersist(true);
        df1.unpersist();
        sparkSession.close();
    }
}