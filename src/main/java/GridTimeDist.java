import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;
import org.apache.spark.storage.StorageLevel;

import java.util.ArrayList;
import java.util.List;

public class GridTimeDist {

    public static void main(String args[]) {
        SparkSession sparkSession = SparkSession.builder().getOrCreate();
        System.out.println(sparkSession.sparkContext().getConf().get("rdd.executor.instances"));
        String path = args[0];
        double radius = Double.parseDouble(args[1]);

        Dataset<Row> df1 = sparkSession.read().parquet(path + "datasetA.parquet")/*.repartition(functions$.MODULE$.col("partitions"))*/.persist(StorageLevel.MEMORY_ONLY());
        Dataset<Row> df2 = sparkSession.read().parquet(path + "datasetB.parquet")/*.repartition(functions$.MODULE$.col("partitions"))*/.persist(StorageLevel.MEMORY_ONLY());

        df1.count();
        df2.count();

        long explodeTime = System.currentTimeMillis();
        df1= df1.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions")));
        df2 = df2.withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions")));

        df1.count();
        df2.count();
        explodeTime = System.currentTimeMillis()-explodeTime;
        System.out.println("Explode Time: "+explodeTime);

        long repartitionTime = System.currentTimeMillis();
        df1= df1.repartition(functions$.MODULE$.col("partitions"));
        df2= df2.repartition(functions$.MODULE$.col("partitions"));

        df1.count();
        df2.count();
        repartitionTime = System.currentTimeMillis()-repartitionTime;
        System.out.println("Repartition Time: "+repartitionTime);

        long joinTime = System.currentTimeMillis();
        long o = df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).count();
        joinTime = System.currentTimeMillis()-joinTime;
        System.out.println("Join Time: "+joinTime);

        System.out.println("Overall Time: "+(explodeTime+repartitionTime+joinTime));
        System.out.println("Matched Pairs: "+o);


        //df1.join(df2, df1.col("partitions").equalTo(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).explain(true);

        df1.unpersist();
        df2.unpersist();
        sparkSession.close();

    }

}