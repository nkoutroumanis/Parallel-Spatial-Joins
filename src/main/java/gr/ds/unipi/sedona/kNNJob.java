package gr.ds.unipi.sedona;

import org.apache.sedona.core.enums.IndexType;
import org.apache.sedona.core.formatMapper.WktReader;
import org.apache.sedona.core.spatialOperator.KNNQuery;
import org.apache.sedona.core.spatialRDD.SpatialRDD;
import org.apache.sedona.spark.SedonaContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import java.util.List;

public class kNNJob {
    public static void main(String args[]) throws Exception {

        long time =0;
        int k = Integer.parseInt(args[4]);

            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrator", "org.apache.sedona.core.serde.SedonaKryoRegistrator");
            SparkSession sparkSession = SedonaContext.builder().config(sparkConf).getOrCreate();/*SparkSession.builder().config(sparkConf).master("local[*]").getOrCreate()*/
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

        List<Point> queryPoints = WktReader.readToGeometryRDD(jsc, args[0], Integer.parseInt(args[1]), false, true).getRawSpatialRDD().map(geom -> (Point) geom).collect();
        long startJobTime = System.currentTimeMillis();

        SpatialRDD spatialRDDB = WktReader.readToGeometryRDD(jsc, args[2], Integer.parseInt(args[3]), false, true);

        boolean buildOnSpatialPartitionedRDD = false;
        spatialRDDB.buildIndex(IndexType.RTREE, buildOnSpatialPartitionedRDD);

        int counter = 1;
        for (Point point : queryPoints) {
            List<Geometry> geometries = KNNQuery.SpatialKnnQuery(spatialRDDB, point, k, true);
            System.out.println(counter++);
//            StringBuilder sb = new StringBuilder();
//            for (Geometry geometry : geometries) {
//                sb.append(geometry.toText()+", ");
//            }
//            if (sb.length() > 0) {
//                sb.setLength(sb.length() - 2);
//            }
//            System.out.println(sb);
        }

            time = (System.currentTimeMillis() - startJobTime);
            jsc.close();
            sparkSession.close();
        System.out.println("Total time exec (sec): "+ time/1000 + " sec");

    }
}
