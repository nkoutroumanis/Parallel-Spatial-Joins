import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;
import org.apache.spark.storage.StorageLevel;
import scala.reflect.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridTime {

    public static void main(String args[]) throws Exception {
        SparkSession sparkSession = SparkSession.builder().master("local[*]").getOrCreate();
        sparkSession.sparkContext().setLogLevel("ERROR");

        sparkSession.conf().set("rdd.sql.shuffle.partitions", Integer.parseInt(args[3]));

        if(args[0].equals("true")){
            sparkSession.conf().set("rdd.sql.join.preferSortMergeJoin",true);
            sparkSession.conf().set("rdd.sql.autoBroadcastJoinThreshold",-1);
        }else{
            if(!args[0].equals("false")){
                throw new Exception("");
            }
        }

        double radius = Double.parseDouble(args[1]);
        System.out.println(radius);

        String path = args[2];

        Dataset<Row> df1 = sparkSession.read().option("delimiter", ";").csv(path + "datasetA.csv").withColumnRenamed("_c0", "x_1").withColumnRenamed("_c1", "y_1")
                .withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("_c2"), "\\[", "")).withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("partitions"), "\\]", ""))
                .drop("_c2").withColumn("partitions", functions$.MODULE$.split(functions$.MODULE$.col("partitions"), ", ")).withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions")))/*.repartition(functions$.MODULE$.col("partitions"))*/.cache();

        Dataset<Row> df2 = sparkSession.read().option("delimiter", ";").csv(path + "datasetB.csv").withColumnRenamed("_c0", "x_2").withColumnRenamed("_c1", "y_2")
                .withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("_c2"), "\\[", "")).withColumn("partitions", functions$.MODULE$.regexp_replace(functions$.MODULE$.col("partitions"), "\\]", ""))
                .drop("_c2").withColumn("partitions", functions$.MODULE$.split(functions$.MODULE$.col("partitions"), ", ")).withColumn("partitions", functions$.MODULE$.explode(functions$.MODULE$.col("partitions")))/*.repartition(functions$.MODULE$.col("partitions"))*/.cache();

        System.out.println("Dataframe1 size: "+ df1.count() +" Partitions: "+df1.rdd().getNumPartitions());
        System.out.println("Dataframe2 size: "+ df2.count() +" Partitions: "+df2.rdd().getNumPartitions());

        System.out.println("Matched Pairs: "+df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).count());
        for (int i = 0; i < 5; i++) {
            df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).count();
        }

        List<Long> execTime = new ArrayList<>();
        long time;
        for (int i = 0; i < 20; i++) {
            time = System.currentTimeMillis();
            df1.join(df2, df1.col("partitions").$eq$eq$eq(df2.col("partitions")), "inner").filter(functions$.MODULE$.pow(functions$.MODULE$.col("x_1").$minus(functions$.MODULE$.col("x_2")),functions$.MODULE$.lit(2)).$plus(functions$.MODULE$.pow(functions$.MODULE$.col("y_1").$minus(functions$.MODULE$.col("y_2")),functions$.MODULE$.lit(2))).$less$eq(functions$.MODULE$.pow(functions$.MODULE$.lit(radius),functions$.MODULE$.lit(2))) ).count();

            execTime.add(System.currentTimeMillis()-time);
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