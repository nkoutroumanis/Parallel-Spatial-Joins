package gr.ds.unipi.sedona;

import org.apache.sedona.common.enums.FileDataSplitter;
import org.apache.sedona.core.enums.GridType;
import org.apache.sedona.core.spatialOperator.JoinQuery;
import org.apache.sedona.core.spatialOperator.SpatialPredicate;
import org.apache.sedona.core.spatialRDD.CircleRDD;
import org.apache.sedona.core.spatialRDD.PointRDD;
import org.apache.sedona.spark.SedonaContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.locationtech.jts.geom.Envelope;

public class DistanceJoinReal {
    public static void main(String args[]) throws Exception {

        int repeats = Integer.parseInt(args[5]);
        long time =0;
        long cou=0;

        for (int n = 0; n < repeats; n++) {
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrator", "org.apache.sedona.core.serde.SedonaKryoRegistrator");
//                .registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, scala.reflect.ManifestFactory$.class, scala.reflect.ManifestFactory$.MODULE$.Any().getClass(), ConcurrentHashMap.class, Function4.class});
//                .registerKryoClasses(new Class[]{scala.collection.immutable.Map$.MODULE$.getClass(),scala.collection.immutable.Map.class,scala.collection.immutable.Map$.class,scala.collection.immutable.Map.class, scala.collection.immutable.Seq.class});
//        Map$.MODULE$.apply(null);


            SparkSession sparkSession = SedonaContext.builder().config(sparkConf).getOrCreate();//SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            long startJobTime = System.currentTimeMillis();

            PointRDD objectRDDA = new PointRDD(jsc, path + args[2] + ".csv", 1, FileDataSplitter.TAB, true);
            PointRDD objectRDDB = new PointRDD(jsc, path + args[3] + ".csv", 1, FileDataSplitter.TAB, true);

//        objectRDDA.getRawSpatialRDD().take(10).forEach(i-> System.out.println(i));
            Envelope envelope = new Envelope(-124.763068, -64.564909, 17.673976, 49.384360);
//            objectRDDA.analyze(envelope, 200000000);
            objectRDDB.analyze(envelope, 42700000);

            CircleRDD circleRDD = new CircleRDD(objectRDDA, radius);

            circleRDD.analyze(envelope,94100000);

            circleRDD.spatialPartitioning(GridType.QUADTREE, Integer.parseInt(args[4]));
            objectRDDB.spatialPartitioning(circleRDD.getPartitioner());

//        System.out.println("The number of points in first data set is "+circleRDD.rawSpatialRDD.count());
//        System.out.println("The number of points in first data set is "+circleRDD.spatialPartitionedRDD.count());
//        System.out.println("The number of points in second data set is "+objectRDDB.rawSpatialRDD.count());
//        System.out.println("The number of points in second data set is "+objectRDDB.spatialPartitionedRDD.count());

            cou = JoinQuery.DistanceJoinQueryFlat(objectRDDB, circleRDD, true, SpatialPredicate.COVERED_BY).count();

            time = time + (System.currentTimeMillis() - startJobTime);

            jsc.close();
            sparkSession.close();
            Thread.sleep(10000);
        }
        System.out.println("Time Exec: "+ time/repeats);
        System.out.println("Count: " + cou);
    }
}
