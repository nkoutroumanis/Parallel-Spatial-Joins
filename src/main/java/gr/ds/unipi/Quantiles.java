package gr.ds.unipi;

import org.apache.spark.ml.feature.QuantileDiscretizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.List;

public class Quantiles {
    public static void main(String args[]){
        SparkSession sparkSession = SparkSession.builder().master("local[*]").getOrCreate();
        //JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

        List<Row> data = Arrays.asList(
                RowFactory.create(0, 18.0),
                RowFactory.create(1, 19.0),
                RowFactory.create(2, 8.0),
                RowFactory.create(3, 5.0),
                RowFactory.create(4, 2.2)
        );

        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("hour", DataTypes.DoubleType, false, Metadata.empty())
        });

        Dataset<Row> df = sparkSession.createDataFrame(data, schema);

        QuantileDiscretizer discretizer = new QuantileDiscretizer()
                .setInputCol("hour")
                .setOutputCol("result")
                .setNumBuckets(4);

        Dataset<Row> result = discretizer.fit(df).transform(df);
        result.show(false);
    }
}
