import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;
import org.apache.spark.storage.StorageLevel;

public class GridTimeDistCLASSIC {

    public static void main(String args[]) {
        SparkSession sparkSession = SparkSession.builder().getOrCreate();
        System.out.println(sparkSession.sparkContext().getConf().get("rdd.executor.instances"));
        String path = args[0];
        double radius = Double.parseDouble(args[1]);

        Dataset<Row> df1 = sparkSession.read().parquet(path + "datasetA.parquet")/*.repartition(functions$.MODULE$.col("partitions"))*/.persist(StorageLevel.MEMORY_ONLY());
        Dataset<Row> df2 = sparkSession.read().parquet(path + "datasetB.parquet")/*.repartition(functions$.MODULE$.col("partitions"))*/.persist(StorageLevel.MEMORY_ONLY());

        df1.count();
        df2.count();

//        for(int i = 0;i<4;i++) {
            long time = System.currentTimeMillis();
            df1 = df1.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions")));
            df2 = df2.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions")));

            long o = df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")), functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")), functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius), functions$.MODULE$.lit(2)))).count();

            time = System.currentTimeMillis() - time;
            System.out.println("Time: " + time);
//        }
        System.out.println("Matched Pairs: "+o);


        System.out.println("Second Execution: ");
        time = System.currentTimeMillis();
        df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")), functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")), functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius), functions$.MODULE$.lit(2)))).count();
        time = System.currentTimeMillis() - time;
        System.out.println("Time: " + time);

        //df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).explain(true);

        df1.unpersist();
        df2.unpersist();
        sparkSession.close();

    }

}