package gr.ds.unipi.sedona;

import gr.ds.unipi.SparkLogParser;
import org.apache.sedona.core.enums.GridType;
import org.apache.sedona.core.enums.IndexType;
import org.apache.sedona.core.formatMapper.WktReader;
import org.apache.sedona.core.spatialOperator.JoinQuery;
import org.apache.sedona.core.spatialOperator.SpatialPredicate;
import org.apache.sedona.core.spatialRDD.SpatialRDD;
import org.apache.sedona.spark.SedonaContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import scala.Tuple3;

public class RangeQueriesJob {
    public static void main(String args[]) throws Exception {

        long time =0;
        long cou = 0;

            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrator", "org.apache.sedona.core.serde.SedonaKryoRegistrator");
            SparkSession sparkSession = SedonaContext.builder().config(sparkConf).getOrCreate();/*SparkSession.builder().config(sparkConf).master("local[*]").getOrCreate()*/
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

            long startJobTime = System.currentTimeMillis();

            SpatialRDD objectRDDA = WktReader.readToGeometryRDD(jsc, args[0], Integer.parseInt(args[1]), false, true);
            SpatialRDD queryWindow = WktReader.readToGeometryRDD(jsc, args[2], Integer.parseInt(args[3]), false, true);

        objectRDDA.analyze();

            objectRDDA.spatialPartitioning(GridType.QUADTREE, Integer.parseInt(args[4]));
            queryWindow.spatialPartitioning(objectRDDA.getPartitioner());
            objectRDDA.buildIndex(IndexType.RTREE, true);

            cou = JoinQuery.SpatialJoinQuery(queryWindow, objectRDDA, true, SpatialPredicate.COVERS).count();
            System.out.println(cou);

            time = (System.currentTimeMillis() - startJobTime);
            jsc.close();
            sparkSession.close();
        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer2();

        System.out.println("Total time exec (sec): "+ time/1000 + " sec");
        System.out.println("Shuffled Remote Bytes Read (MB): " + t._2());
        System.out.println("Summed Peak Memory Execution (MB): " +t._3());

    }
}
