package gr.ds.unipi.self.sedona;

import gr.ds.unipi.SparkLogParser;
import org.apache.sedona.common.enums.FileDataSplitter;
import org.apache.sedona.core.enums.GridType;
import org.apache.sedona.core.spatialOperator.JoinQuery;
import org.apache.sedona.core.spatialOperator.SpatialPredicate;
import org.apache.sedona.core.spatialRDD.CircleRDD;
import org.apache.sedona.core.spatialRDD.PointRDD;
import org.apache.sedona.spark.SedonaContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.locationtech.jts.geom.Envelope;
import scala.Tuple3;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class DistanceJoinReal {
    public static void main(String args[]) throws Exception {

        long time =0;
        long cou=0;

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

            Envelope envelope = new Envelope(-124.763068, -64.564909, 17.673976, 49.384360);
            objectRDDA.analyze(envelope, Integer.parseInt(args[3]));

            CircleRDD circleRDD = new CircleRDD(objectRDDA, radius);

            circleRDD.analyze(envelope,Integer.parseInt(args[3]));

            circleRDD.spatialPartitioning(GridType.QUADTREE, Integer.parseInt(args[4]));
            objectRDDA.spatialPartitioning(circleRDD.getPartitioner());

            JavaPairRDD rp = JoinQuery.DistanceJoinQueryFlat(objectRDDA, circleRDD, true, SpatialPredicate.COVERED_BY).filter((g)->{return (g._1.getUserData().hashCode()>g._2.getUserData().hashCode());});
            cou = rp.count();
            time = (System.currentTimeMillis() - startJobTime);

            jsc.close();
            sparkSession.close();

        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer();
        FileWriter fw = new FileWriter("./timeExec-"+args[2]+"-"+args[1]+"-sedona.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Math.round(time/1000.0)+","+t._1()+","+t._2()+","+t._3()+"\n");
        bw.close();

        System.out.println("Time Exec: "+ time);
        System.out.println("Count: " + cou);
    }
}
