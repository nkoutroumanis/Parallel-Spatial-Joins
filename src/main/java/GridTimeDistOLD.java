import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;
import org.apache.spark.storage.StorageLevel;

import java.util.ArrayList;
import java.util.List;

public class GridTimeDistOLD {

    public static void main(String args[]) {
        SparkSession sparkSession = SparkSession.builder().getOrCreate();
        System.out.println(sparkSession.sparkContext().getConf().get("rdd.executor.instances")
        );
        String path = args[0];
        double radius = Double.parseDouble(args[1]);

        Dataset<Row> df1 = sparkSession.read().parquet(path + "datasetA.parquet")/*.repartition(functions$.MODULE$.col("partitions"))*/.persist(StorageLevel.MEMORY_ONLY());
        Dataset<Row> df2 = sparkSession.read().parquet(path + "datasetB.parquet")/*.repartition(functions$.MODULE$.col("partitions"))*/.persist(StorageLevel.MEMORY_ONLY());

        System.out.println("Matched Pairs: "+df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).count());
        for (int i = 0; i < 3; i++) {
            df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).count();
        }

        List<Long> execTime = new ArrayList<>();
        long time;
        for (int i = 0; i < 10; i++) {
            time = System.currentTimeMillis();
            df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).count();
            time = System.currentTimeMillis()-time;
            execTime.add(time);
        }
        System.out.println(execTime.stream().mapToLong(i->i).average().getAsDouble()/1000);
        System.out.println(((double)execTime.stream().mapToLong(i->i).max().getAsLong())/1000);
        System.out.println(((double)execTime.stream().mapToLong(i->i).min().getAsLong())/1000);

        df1.unpersist();
        df2.unpersist();
        sparkSession.close();
        //df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).explain(true);

    }

}