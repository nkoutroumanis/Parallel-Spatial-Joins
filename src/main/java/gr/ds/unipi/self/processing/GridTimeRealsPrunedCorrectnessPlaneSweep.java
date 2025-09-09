package gr.ds.unipi.self.processing;

import gr.ds.unipi.SparkLogParser;
import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.rdd.LPTPartitioner;
import gr.ds.unipi.rdd.LPTPartitionerBr;
import gr.ds.unipi.self.Algorithms;
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

public class GridTimeRealsPrunedCorrectnessPlaneSweep {
    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;
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
            SelfGrid grid = SelfGrid.newGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, ReplicationFunc.lesserPointsinBoundaries);

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
                grid.getCellsWithCosts1();
            }else if(args[5].equals("LPT1")){
                System.out.println("NEW Partitioner12");
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
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>(4);
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

                    int[] replicatedObjectsMapCount = new int[8];
                    int homeObjectsCount = 0;
                    List<Tuple3<String,Double,Double>[]> replicatedObjectsList = new ArrayList<>(8);

                    SelfGrid g = gridBroadcasted.getValue();

                    Iterator<Tuple3<String, Double, Double>> it = tup._2.iterator();
                    while (it.hasNext()) {
                        Tuple3<String, Double, Double> obj= it.next();
                        int cellId = g.getCellId(obj._2(), obj._3());
                        if(cellId == tup._1){
                            homeObjectsCount++;
                        }
                        else{
                            if(Integer.compare(cellId,tup._1)==-1){
                                if(cellId==tup._1-1){
                                    replicatedObjectsMapCount[3]++;
                                }else{
                                    replicatedObjectsMapCount[cellId%3]++;
                                }
                            }else{
                                if(cellId==tup._1+1){
                                    replicatedObjectsMapCount[4]++;
                                }else{
                                    replicatedObjectsMapCount[(cellId%3)+5]++;
                                }
                            }
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
                        }else{
                            if(Integer.compare(cellId,tup._1)==-1){
                                if(cellId==tup._1-1){
                                    replicatedObjectsList.get(3)[replicatedObjectsMapCount[3]++]=obj;
                                }else{
                                    replicatedObjectsList.get(cellId%3)[replicatedObjectsMapCount[cellId%3]++]=obj;
                                }
                            }else{
                                if(cellId==tup._1+1){
                                    replicatedObjectsList.get(4)[replicatedObjectsMapCount[4]++]=obj;
                                }else{
                                    replicatedObjectsList.get((cellId%3)+5)[replicatedObjectsMapCount[(cellId%3)+5]++]=obj;
                                }
                            }
                        }
                    }

                    Arrays.sort(homeObjects,Comparator.comparingDouble(obj->obj._2()));

                    for (Tuple3<String, Double, Double>[] tuple3s : replicatedObjectsList) {
                        Arrays.sort(tuple3s, Comparator.comparingDouble(obj->obj._2()));
                    }

                    Algorithms.planeSweepSelf(homeObjects, results, radius);

                    for (int i = 0; i < replicatedObjectsList.size(); i++) {
                            Algorithms.planeSweep(homeObjects, replicatedObjectsList.get(i), results, radius);
                    }

                    return results.iterator();
                }
            }).count();


            time = (System.currentTimeMillis() - startJobTime);
            cou = count;

            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();

        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer();
        FileWriter fw = new FileWriter("./timeExec-"+args[2]+"-"+args[1]+"-sgoa-ps.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Math.round(time/1000.0)+","+Math.round((t1)/1000.0)+","+t._1()+","+t._2()+","+t._3()+"\n");
        bw.close();

        System.out.println("Sample Time: "+ sampleTime);
        System.out.println("Time Exec: "+ time);
        System.out.println("Count: "+cou);

    }

}
