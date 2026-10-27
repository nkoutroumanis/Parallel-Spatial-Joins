package gr.ds.unipi.self.adaptiveGrid;

import gr.ds.unipi.SparkLogParser;
import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.rdd.LPTPartitionerBr;
import gr.ds.unipi.self.Algorithms;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;
import scala.Tuple3;
import scala.reflect.ManifestFactory$;

import java.io.*;
import java.util.*;

public class AdaptiveGridTimeRealsPrunedCorrectnessPlaneSweep {
    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;
        long sampleTime = 0;
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{AdaptiveGrid.class, Cell.class, int[][].class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, /*org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, */ManifestFactory$.MODULE$.Any().getClass()/*, ConcurrentHashMap.class, java.util.function.Function.class*/});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            long startJobTime = System.currentTimeMillis();

            long o = System.currentTimeMillis();

            BufferedReader readerA = new BufferedReader(new FileReader("/home/user/samples-"+args[4]+"/"+args[2]+".csv"));

            List<Point> sampleDataset = new ArrayList<>();

            String strLine;

            while ((strLine = readerA.readLine()) != null)   {
                // Print the content on the console
                String[] line = strLine.split("\t");
                sampleDataset.add(Point.newPoint(Double.parseDouble(line[1]), Double.parseDouble(line[2])));
            }

            AdaptiveGrid grid = AdaptiveGrid.newAdaptiveGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), Integer.parseInt(args[6]), Integer.parseInt(args[7]), radius, sampleDataset);

            readerA.close();
            sampleTime = sampleTime + (System.currentTimeMillis() - o);
            System.out.println("OK with sampling Time:" + (System.currentTimeMillis() - o));

            Partitioner cp = null;
            if(args[5].equals("HASH")){
                cp = new CustomPartitioner(Integer.parseInt(args[3]));
                grid.getCellsWithCosts();
            }else if(args[5].equals("LPT1")){
                System.out.println("NEW Partitioner12");
                Broadcast<Map<Integer, Integer>> lptBroadcasted = jsc.broadcast(LPTPartitionerBr.ltp(grid.getCellsWithCosts(), Integer.parseInt(args[3])));
                cp = new LPTPartitionerBr(lptBroadcasted, Integer.parseInt(args[3]));
    //            cp = new LPTPartitioner(grid.getCellsWithCosts1(),Integer.parseInt(args[3]));
            }else{
                try {
                    throw new Exception("No correct partitioner has been set");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Broadcast<AdaptiveGrid> gridBroadcasted = jsc.broadcast(grid);
            long t1 = (System.currentTimeMillis() - startJobTime);


        JavaRDD<String> rddTxtFiles = jsc.textFile(path + args[2] + ".csv"/*,Integer.parseInt(args[5])*/);

            JavaRDD<Tuple3<String, Double, Double>> rddTuples = rddTxtFiles.map((String line) -> {
                String[] elements = line.split("\t");
                return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
            });

            JavaPairRDD<Integer, Tuple3<String, Double, Double>> pairRDD = rddTuples.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple3<String, Double, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>();
                    int[] cellIds = gridBroadcasted.getValue().getPartitions(tuple._2(), tuple._3());
                    for (int i = 0; i < cellIds.length; i++) {
                        list.add(new Tuple2<>(cellIds[i], tuple));
                    }
                    return list.iterator();
                }
            });

            long count = pairRDD.groupByKey(cp).flatMap(new FlatMapFunction<Tuple2<Integer, Iterable<Tuple3<String, Double, Double>>>, Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>>() {
                @Override
                public Iterator<Tuple2<Tuple3<String, Double, Double>, Tuple3<String, Double, Double>>> call(Tuple2<Integer, Iterable< Tuple3<String, Double, Double>>> tup) throws Exception {

                    List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results = new ArrayList<>();

                    int homeObjectsCount = 0;
                    int replicatedObjectsCount = 0;

                    AdaptiveGrid g = gridBroadcasted.getValue();

                    Iterator<Tuple3<String, Double, Double>> it = tup._2.iterator();
                    while (it.hasNext()) {
                        Tuple3<String, Double, Double> obj= it.next();
                        int cellId = g.getCellId(obj._2(), obj._3());
                        if(cellId == tup._1){
                            homeObjectsCount++;
                        }else{
                            replicatedObjectsCount++;
                        }
                    }

                    Tuple3<String, Double, Double>[] homeObjects = new Tuple3[homeObjectsCount];
                    Tuple3<String, Double, Double>[] replicatedObjects = new Tuple3[replicatedObjectsCount];
                    homeObjectsCount =0;
                    replicatedObjectsCount =0;

                    it = tup._2.iterator();
                    while (it.hasNext()) {
                        Tuple3<String, Double, Double> obj= it.next();
                        int cellId = g.getCellId(obj._2(), obj._3());

                        if(cellId == tup._1){
                            homeObjects[homeObjectsCount++]=obj;
                        }else{
                            replicatedObjects[replicatedObjectsCount++]=obj;
                        }
                    }

                    Arrays.sort(homeObjects,Comparator.comparingDouble(obj->obj._2()));
                    Arrays.sort(replicatedObjects,Comparator.comparingDouble(obj->obj._2()));

                    Algorithms.planeSweepSelf(homeObjects, results, radius);
                    Algorithms.planeSweep(homeObjects, replicatedObjects, results, radius);

                    return results.iterator();
                }
            }).count();


            time = (System.currentTimeMillis() - startJobTime);
            cou = count;

            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();

        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer();
        FileWriter fw = new FileWriter("./timeExec-"+args[2]+"-"+args[1]+"-adaptiveGrid-ps-"+args[6]+"-"+args[7]+".txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Math.round(time/1000.0)+","+Math.round((t1)/1000.0)+","+t._1()+","+t._2()+","+t._3()+"\n");
        bw.close();

        System.out.println("Sample Time: "+ sampleTime);
        System.out.println("Time Exec: "+ time);
        System.out.println("Count: "+cou);

    }

}
