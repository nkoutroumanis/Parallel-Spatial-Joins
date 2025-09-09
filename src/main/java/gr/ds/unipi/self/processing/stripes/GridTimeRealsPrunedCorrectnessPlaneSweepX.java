package gr.ds.unipi.self.processing.stripes;

import gr.ds.unipi.SparkLogParser;
import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.rdd.LPTPartitioner;
import gr.ds.unipi.rdd.LPTPartitionerBr;
import gr.ds.unipi.self.Algorithms;
import gr.ds.unipi.self.processing.Cell;
import gr.ds.unipi.self.processing.ReplicationFunc;
import gr.ds.unipi.self.processing.SelfGrid;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Position;
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

public class GridTimeRealsPrunedCorrectnessPlaneSweepX {
    public static void main(String args[]) throws IOException {
        long time =0;
        long sampleTime = 0;
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{SelfGrid.class, Cell.class, ReplicationFunc.class, Position.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, /*org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, */ManifestFactory$.MODULE$.Any().getClass()/*, ConcurrentHashMap.class, java.util.function.Function.class*/});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
            //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            long startJobTime = System.currentTimeMillis();
            //CHANGE HERE
            //Grid grid = Grid.newGeoGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
//            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
            SelfGrid grid = SelfGrid.newGridStripesX(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, ReplicationFunc.lesserPointsinBoundaries);

            long o = System.currentTimeMillis();

            BufferedReader readerA = new BufferedReader(new FileReader("/home/user/samples-"+args[4]+"/"+args[2]+".csv"));

            String strLine;

            while ((strLine = readerA.readLine()) != null)   {
                // Print the content on the console
                String[] line = strLine.split("\t");
                grid.addPointDataset(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
            }

            readerA.close();
            sampleTime = sampleTime + (System.currentTimeMillis() - o);
            System.out.println("OK with sampling Time:" + (System.currentTimeMillis() - o));

            grid.printInfo();

            Partitioner cp = null;
            if(args[5].equals("HASH")){
                cp = new CustomPartitioner(Integer.parseInt(args[3]));
            }else if(args[5].equals("LPT1")){
                System.out.println("NEW Partitioner");
                Broadcast<Map<Integer, Integer>> lptBroadcasted = jsc.broadcast(LPTPartitionerBr.ltp(grid.getCellsWithCosts1(), Integer.parseInt(args[3])));
                cp = new LPTPartitionerBr(lptBroadcasted, Integer.parseInt(args[3]));
    //            cp = new LPTPartitioner(grid.getCellsWithCosts1(),Integer.parseInt(args[3]));
            }else if(args[5].equals("LPT2")){
                cp = new LPTPartitioner(grid.getCellsWithCosts2(),Integer.parseInt(args[3]));
            }else{
                try {
                    throw new Exception("No correct partitioner has been set");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            grid.printInfo();

            Broadcast<SelfGrid> gridBroadcasted = jsc.broadcast(grid);
            long t1 = (System.currentTimeMillis() - startJobTime);

        JavaRDD<String> rddTxtFiles = jsc.textFile(path + args[2] + ".csv"/*,Integer.parseInt(args[5])*/);

            JavaRDD<Tuple3<String, Double, Double>> rddTuples = rddTxtFiles.map((String line) -> {
                String[] elements = line.split("\t");
                return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
            });

//            if (!(flag == 1 || flag == 2)) {
//
//                long o = System.currentTimeMillis();
//                rddTuplesA.sample(false, Double.parseDouble(args[7]), 261).collect().forEach((tuple) ->
//                        grid.addPointDatasetA(tuple._2(), tuple._3())
//                );
//
//                rddTuplesB.sample(false, Double.parseDouble(args[7]), 261).collect().forEach((tuple) ->
//                        grid.addPointDatasetB(tuple._2(), tuple._3())
//                );
//                sampleTime = sampleTime + (System.currentTimeMillis() - o);
//                System.out.println("OK with sampling Time:" + (System.currentTimeMillis() - o));
//                if (flag == 8 || flag == 9) {
//                    grid.load();
//                }
//            }


//        if(flag == 8 || flag ==9){
//            gridBroadcasted.getValue().load();
//        }
//            gridBroadcasted.getValue().initialize();

            JavaPairRDD<Integer, Tuple3<String, Double, Double>> pairRDD = rddTuples.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple3<String, Double, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>(3);
                    int[] cellIds = gridBroadcasted.getValue().getPartitions(tuple._2(), tuple._3());
                    for (int i = 0; i < cellIds.length; i++) {
                        list.add(new Tuple2<>(cellIds[i], tuple));
                    }
                    return list.iterator();
                }
            });

            long count = pairRDD.groupByKey(cp).flatMap(new FlatMapFunction<Tuple2<Integer, Iterable<Tuple3<String, Double, Double>>>, Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>>()  {
                @Override
                public Iterator<Tuple2<Tuple3<String, Double, Double>, Tuple3<String, Double, Double>>> call(Tuple2<Integer, Iterable< Tuple3<String, Double, Double>>> tup) throws Exception {

                    List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results = new ArrayList<>();

                    int[] replicatedObjectsMapCount = new int[2];
                    int homeObjectsCount = 0;
                    List<Tuple3<String,Double,Double>[]> replicatedObjectsList = new ArrayList<>(2);

                    SelfGrid g = gridBroadcasted.getValue();

                    Iterator<Tuple3<String, Double, Double>> it = tup._2.iterator();
                    while (it.hasNext()) {
                        Tuple3<String, Double, Double> obj= it.next();
                        int cellId = g.getCellId(obj._2(), obj._3());
                        if(cellId == tup._1){
                            homeObjectsCount++;
                        }
                        else if(cellId==tup._1-1){
                            replicatedObjectsMapCount[0]++;
                        }
                        else if(cellId==tup._1+1){
                            replicatedObjectsMapCount[1]++;
                        }
                        else {
                            throw new Exception("Error in determining cell id");
                        }
                    }

                    Tuple3<String, Double, Double>[] homeObjects = new Tuple3[homeObjectsCount];
                    homeObjectsCount = 0;

                    for (int i = 0; i < replicatedObjectsMapCount.length; i++) {
                        replicatedObjectsList.add(new Tuple3[replicatedObjectsMapCount[i]]);
                        replicatedObjectsMapCount[i] = 0;
                    }

                    it = tup._2.iterator();
                    while (it.hasNext()) {
                        Tuple3<String, Double, Double> obj= it.next();
                        int cellId = g.getCellId(obj._2(), obj._3());

                        if(cellId == tup._1){
                            homeObjects[homeObjectsCount++]=obj;
                        }
                        else if(cellId==tup._1-1){
                            replicatedObjectsList.get(0)[replicatedObjectsMapCount[0]++]=obj;
                        }
                        else if(cellId==tup._1+1){
                            replicatedObjectsList.get(1)[replicatedObjectsMapCount[1]++]=obj;
                        }
                        else {
                            throw new Exception("Error in determining cell id");
                        }
                    }

                    Arrays.sort(homeObjects,Comparator.comparingDouble(obj->obj._3()));

                    for (Tuple3<String, Double, Double>[] tuple3s : replicatedObjectsList) {
                        Arrays.sort(tuple3s, Comparator.comparingDouble(obj->obj._3()));
                    }

                    Algorithms.planeSweepSelfOnY(homeObjects, results, radius);

                    for (int i = 0; i < replicatedObjectsList.size(); i++) {
                        Algorithms.planeSweepOnY(homeObjects, replicatedObjectsList.get(i), results, radius);
                    }

//                    List<Tuple3<String, Double, Double>> homeObjects = new ArrayList<>();
//                    Map<Integer,List<Tuple3<String,Double,Double>>> replicatedObjectsMap = new HashMap<>();
//
//                    tup._2.forEach(obj->{
//                        if(obj._1.equals(tup._1)){
//                            homeObjects.add(obj._2);
//                        }else{
//                            if(replicatedObjectsMap.containsKey(obj._1)){
//                                replicatedObjectsMap.get(obj._1).add(obj._2);
//                            }else{
//                                List<Tuple3<String,Double,Double>> replList = new ArrayList<>();
//                                replList.add(obj._2);
//                                replicatedObjectsMap.put(obj._1, replList);
//                            }
//                        }
//                    });
//
//                    homeObjects.sort(Comparator.comparingDouble(obj->obj._3()));
//                    replicatedObjectsMap.forEach((in, l)->{
//                        l.sort(Comparator.comparingDouble(obj->obj._3()));
//                    });
//
//                    planeSweep(homeObjects, homeObjects, results, radius);
//
//                    List<Map.Entry<Integer,List<Tuple3<String,Double,Double>>>> replicatedObjects = new ArrayList<>(replicatedObjectsMap.entrySet());
//
//                    for (Map.Entry<Integer,List<Tuple3<String, Double, Double>>> tuple3s : replicatedObjects) {
//                        planeSweep1(homeObjects, tuple3s.getValue(), results, radius);
//                    }

                    return results.iterator();
                }
            }).count();


            time = (System.currentTimeMillis() - startJobTime);

            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();

        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer();
        FileWriter fw = new FileWriter("./timeExec-"+args[2]+"-"+args[1]+"-sgoa-1D-x.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Math.round(time/1000.0)+","+Math.round((t1)/1000.0)+","+t._1()+","+t._2()+","+t._3()+"\n");
        bw.close();

        System.out.println("Sample Time: "+ sampleTime);
        System.out.println("Time Exec: "+ time);
        System.out.println("Count: "+count);

    }
}
