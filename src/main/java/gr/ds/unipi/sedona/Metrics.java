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

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Metrics {
    public static void main(String args[]) throws Exception {

        BufferedWriter w = new BufferedWriter(new FileWriter("./metrics-"+args[2] +"-"+args[3]+"-"+args[1]+"-"+"sedona"+".txt"));
        w.write(  String.format("%-25s", "radius") + String.format("%-25s", "replication"));
        w.newLine();

        SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrator", "org.apache.sedona.core.serde.SedonaKryoRegistrator");
//                .registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, scala.reflect.ManifestFactory$.class, scala.reflect.ManifestFactory$.MODULE$.Any().getClass(), ConcurrentHashMap.class, Function4.class});
//                .registerKryoClasses(new Class[]{scala.collection.immutable.Map$.MODULE$.getClass(),scala.collection.immutable.Map.class,scala.collection.immutable.Map$.class,scala.collection.immutable.Map.class, scala.collection.immutable.Seq.class});
//        Map$.MODULE$.apply(null);


            SparkSession sparkSession = SedonaContext.builder().config(sparkConf).getOrCreate();//SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            PointRDD objectRDDA = new PointRDD(jsc, path + args[2] + ".csv", 1, FileDataSplitter.TAB, true);
            PointRDD objectRDDB = new PointRDD(jsc, path + args[3] + ".csv", 1, FileDataSplitter.TAB, true);

//        objectRDDA.getRawSpatialRDD().take(10).forEach(i-> System.out.println(i));
            Envelope envelope = new Envelope(-124.763068, -64.564909, 17.673976, 49.384360);
//            objectRDDA.analyze(envelope, 200000000);
            objectRDDB.analyze(envelope, Integer.parseInt(args[6]));

            CircleRDD circleRDD = new CircleRDD(objectRDDA, radius);

            circleRDD.analyze(envelope,Integer.parseInt(args[5]));

            circleRDD.spatialPartitioning(GridType.QUADTREE, Integer.parseInt(args[4]));
            objectRDDB.spatialPartitioning(circleRDD.getPartitioner());
            long replication = circleRDD.spatialPartitionedRDD.count()-circleRDD.rawSpatialRDD.count();
            replication = replication + (objectRDDB.spatialPartitionedRDD.count()-objectRDDB.rawSpatialRDD.count());


            System.out.println(replication);
//        System.out.println("The number of points in first data set is "+circleRDD.rawSpatialRDD.count());
//        System.out.println("The number of points in first data set is "+circleRDD.spatialPartitionedRDD.count());
//        System.out.println("The number of points in second data set is "+objectRDDB.rawSpatialRDD.count());
//        System.out.println("The number of points in second data set is "+objectRDDB.spatialPartitionedRDD.count());
//            cou = JoinQuery.DistanceJoinQueryFlat(objectRDDB, circleRDD, true, SpatialPredicate.COVERED_BY).count();

            w.write(String.format("%-25s", radius) +String.format("%-25s", replication));
            w.newLine();
            w.close();

            jsc.close();
            sparkSession.close();
    }
}
