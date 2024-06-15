package gr.ds.unipi.dataframes;

import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.grid.Cell;
import gr.ds.unipi.grid.Grid;
import gr.ds.unipi.grid.NewFunc;
import gr.ds.unipi.grid.TriFunction;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.functions$;
import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.storage.StorageLevel;
import scala.reflect.ClassTag;

import java.util.ArrayList;

public class GridTimeDist {

    public static void main(String args[]) {
        SparkConf sparkConf = new SparkConf().set("spark.serializer","org.apache.spark.serializer.KryoSerializer");//.set("spark.kryo.registrationRequired", "true")
                //.registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class,  Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, java.util.HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, scala.reflect.ManifestFactory$.class,scala.reflect.ManifestFactory$.MODULE$.Any().getClass(), org.apache.spark.sql.catalyst.InternalRow[].class});
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
        Grid.experiments = "DIAG_COMP";

        long startIndexingTime = System.currentTimeMillis();
        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), radius, function);

        Dataset<Row> df1 = sparkSession.read().option("delimiter", ";").csv(path + "datasetA"+args[3]+".csv").withColumnRenamed("_c0", "id_1").withColumnRenamed("_c1", "x_1").withColumnRenamed("_c2", "y_1").orderBy(rand());
        Dataset<Row> df2 = sparkSession.read().option("delimiter", ";").csv(path + "datasetB"+args[4]+".csv").withColumnRenamed("_c0", "id_2").withColumnRenamed("_c1", "x_2").withColumnRenamed("_c2", "y_2").orderBy(rand());

        df1.count();
        df2.count();
        if(!(flag == 1 || flag ==2)) {
            System.out.println("Sampling");
            df1.sample(false, 0.01).collectAsList().forEach((row) ->
                    grid.addPointDatasetA(Double.parseDouble((String) row.getAs("x_1")), Double.parseDouble((String) row.getAs("y_1")))
            );
            df2.sample(false, 0.01).collectAsList().forEach(row ->
                    grid.addPointDatasetB(Double.parseDouble((String) row.getAs("x_2")), Double.parseDouble((String) row.getAs("y_2")))
            );
            System.out.println("Finished with the sampling");
        }
        grid.load();

        Broadcast<Grid> gridBroadcasted = sparkSession.sparkContext().broadcast(grid, ClassTag.apply(Grid.class));
        UserDefinedFunction udfCoordinatesToArrayA = udf(
                (String x, String y) -> gridBroadcasted.getValue().getPartitionsAType(Double.parseDouble(x), Double.parseDouble(y)), DataTypes.createArrayType(DataTypes.StringType)
        );

        UserDefinedFunction udfCoordinatesToArrayB = udf(
                (String x, String y) -> gridBroadcasted.getValue().getPartitionsBType(Double.parseDouble(x), Double.parseDouble(y)), DataTypes.createArrayType(DataTypes.StringType)
        );

        df1 = df1.withColumn("partitions", udfCoordinatesToArrayA.apply(col("x_1"),col("y_1")));
        df2 = df2.withColumn("partitions", udfCoordinatesToArrayB.apply(col("x_2"),col("y_2")));

        df1.count();
        df2.count();

        System.out.println("Indexing Time: "+(System.currentTimeMillis()-startIndexingTime));
        df1.persist(StorageLevel.MEMORY_ONLY_SER());
        df2.persist(StorageLevel.MEMORY_ONLY_SER());

        df1.count();
        df2.count();

        long startJoinTime = System.currentTimeMillis();

        df1= df1.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions")));
        df2 = df2.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions")));
        System.out.println(df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).count());
        System.out.println("Joining Time: "+(System.currentTimeMillis()-startJoinTime));

        df1.unpersist();
        df2.unpersist();
        sparkSession.stop();
    }



}