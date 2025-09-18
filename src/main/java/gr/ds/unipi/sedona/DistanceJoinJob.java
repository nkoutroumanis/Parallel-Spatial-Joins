package gr.ds.unipi.sedona;

import gr.ds.unipi.SparkLogParser;
import org.apache.sedona.common.enums.FileDataSplitter;
import org.apache.sedona.core.enums.GridType;
import org.apache.sedona.core.enums.IndexType;
import org.apache.sedona.core.formatMapper.WktReader;
import org.apache.sedona.core.spatialOperator.JoinQuery;
import org.apache.sedona.core.spatialOperator.SpatialPredicate;
import org.apache.sedona.core.spatialRDD.CircleRDD;
import org.apache.sedona.core.spatialRDD.PointRDD;
import org.apache.sedona.core.spatialRDD.SpatialRDD;
import org.apache.sedona.spark.SedonaContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import scala.Tuple3;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class DistanceJoinJob {
    public static void main(String args[]) throws Exception {

        long time =0;
        long cou=0;

            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrator", "org.apache.sedona.core.serde.SedonaKryoRegistrator");

            SparkSession sparkSession = SedonaContext.builder().config(sparkConf).getOrCreate();//SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

            double radius = Double.parseDouble(args[5]);

            long startJobTime = System.currentTimeMillis();
        JavaRDD<Point> spatialRDDA = WktReader.readToGeometryRDD(jsc, args[0], Integer.parseInt(args[1]), false, true).getRawSpatialRDD().map(geom -> (Point) geom);
        JavaRDD<Point> spatialRDDB = WktReader.readToGeometryRDD(jsc, args[2], Integer.parseInt(args[3]), false, true).getRawSpatialRDD().map(geom -> (Point) geom);

        PointRDD objectRDDA = new PointRDD(spatialRDDA);
        PointRDD objectRDDB = new PointRDD(spatialRDDB);

//        objectRDDA.analyze();

            CircleRDD circleRDD = new CircleRDD(objectRDDA, radius);

            circleRDD.analyze();
            circleRDD.spatialPartitioning(GridType.QUADTREE, Integer.parseInt(args[4]));
            objectRDDB.spatialPartitioning(circleRDD.getPartitioner());
        objectRDDB.buildIndex(IndexType.RTREE, true);

            cou = JoinQuery.DistanceJoinQueryFlat(objectRDDB, circleRDD, true, SpatialPredicate.COVERED_BY).count();

            time = time + (System.currentTimeMillis() - startJobTime);

        System.out.println("Matched pairs: "+cou);

        jsc.close();
        sparkSession.close();
        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer2();

        System.out.println("Total time exec (sec): "+ time/1000 + " sec");
        System.out.println("Shuffled Remote Bytes Read (MB): " + t._2());
        System.out.println("Summed Peak Memory Execution (MB): " +t._3());
    }
}
