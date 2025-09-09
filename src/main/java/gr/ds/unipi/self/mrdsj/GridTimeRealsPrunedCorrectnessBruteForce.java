package gr.ds.unipi.self.mrdsj;

import gr.ds.unipi.SparkLogParser;
import gr.ds.unipi.self.Algorithms;
import gr.ds.unipi.self.agreements.Agreements;
import gr.ds.unipi.self.agreements.Edge;
import gr.ds.unipi.self.agreements.Space;
import gr.ds.unipi.self.grid.Cell;
import gr.ds.unipi.self.grid.ReplicationFunc;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.HashPartitioner;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GridTimeRealsPrunedCorrectnessBruteForce {
    public static void main(String args[]) throws IOException {
        long time =0;
        long sampleTime = 0;
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{GenericMRDSJGrid.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, ReplicationFunc.class, Position.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, /*org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, */ManifestFactory$.MODULE$.Any().getClass()/*, ConcurrentHashMap.class, java.util.function.Function.class*/,java.lang.invoke.SerializedLambda.class});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
            //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
            String path = args[0];
            double radius = Double.parseDouble(args[1]);

            long startJobTime = System.currentTimeMillis();
            //CHANGE HERE
            //Grid grid = Grid.newGeoGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
//            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
            GenericMRDSJGrid grid = GenericMRDSJGrid.newGenericGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, 1,Integer.parseInt(args[4]));
            Broadcast<GenericMRDSJGrid> gridBroadcasted = jsc.broadcast(grid);
            long t1 = (System.currentTimeMillis() - startJobTime);


        JavaRDD<String> rddTxtFiles = jsc.textFile(path + args[2] + ".csv");

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

//        grid.clean();

//        if(flag == 8 || flag ==9){
//            gridBroadcasted.getValue().load();
//        }
//            gridBroadcasted.getValue().initialize();

            JavaPairRDD<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>> pairRDD = rddTuples.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>> list = new ArrayList<>(8);
                    int[] cellIds = gridBroadcasted.getValue().getPartitions(tuple._2(), tuple._3());
                    for (int i = 0; i < cellIds.length; i++) {
                        list.add(new Tuple2<>(cellIds[i], new Tuple2<>(cellIds[0],tuple)));
                    }
                    return list.iterator();
                }
            });

            long count = pairRDD.groupByKey(new HashPartitioner(Integer.parseInt(args[3]))).flatMap(new FlatMapFunction<Tuple2<Integer, Iterable<Tuple2<Integer, Tuple3<String, Double, Double>>>>, Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>>() {
                @Override
                public Iterator<Tuple2<Tuple3<String, Double, Double>, Tuple3<String, Double, Double>>> call(Tuple2<Integer, Iterable<Tuple2<Integer, Tuple3<String, Double, Double>>>> tup) throws Exception {

                    List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results = new ArrayList<>();

                    int[] replicatedObjectsMapCount = new int[8];
                    int homeObjectsCount = 0;
                    List<Tuple3<String,Double,Double>[]> replicatedObjectsList = new ArrayList<>(8);

                    Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> it = tup._2.iterator();
                    while (it.hasNext()) {
                        Tuple2<Integer,Tuple3<String, Double, Double>> obj= it.next();

                        if(obj._1.intValue() == tup._1){
                            homeObjectsCount++;
                        }
                        else{
                            if(Integer.compare(obj._1,tup._1)==-1){
                                if(obj._1==tup._1-1){
                                    replicatedObjectsMapCount[3]++;
                                }else{
                                    replicatedObjectsMapCount[obj._1%3]++;
                                }
                            }else{
                                if(obj._1==tup._1+1){
                                    replicatedObjectsMapCount[4]++;
                                }else{
                                    replicatedObjectsMapCount[(obj._1%3)+5]++;
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
                        Tuple2<Integer,Tuple3<String, Double, Double>> obj= it.next();
                        if(obj._1.intValue() == tup._1){
                            homeObjects[homeObjectsCount++]=obj._2;
                        }else{
                            if(Integer.compare(obj._1,tup._1)==-1){
                                if(obj._1==tup._1-1){
                                    replicatedObjectsList.get(3)[replicatedObjectsMapCount[3]++]=obj._2;
                                }else{
                                    replicatedObjectsList.get(obj._1%3)[replicatedObjectsMapCount[obj._1%3]++]=obj._2;
                                }
                            }else{
                                if(obj._1==tup._1+1){
                                    replicatedObjectsList.get(4)[replicatedObjectsMapCount[4]++]=obj._2;
                                }else{
                                    replicatedObjectsList.get((obj._1%3)+5)[replicatedObjectsMapCount[(obj._1%3)+5]++]=obj._2;
                                }
                            }
                        }
                    }

                    Algorithms.bruteForceSelf(homeObjects, results, radius);

                    for (int i = 0; i < replicatedObjectsList.size(); i++) {
                        Algorithms.bruteForce(homeObjects, replicatedObjectsList.get(i), results, radius);
                    }

                    Tuple3<String,Double,Double>[] list1 = null;
                    Tuple3<String,Double,Double>[] list2 = null;

                    for (Tuple3<String, Double, Double>[] tuple3s : replicatedObjectsList) {
                        if(tuple3s.length>0){
                            int id = gridBroadcasted.getValue().getCellId(tuple3s[0]._2(),tuple3s[0]._3());
                            if(id ==(tup._1 -1) || id ==(tup._1+1)){
                                list1 = tuple3s;
                            }
                            if(id==(tup._1-(int)gridBroadcasted.getValue().getCellsInXAxis()) || id==(tup._1+(int)gridBroadcasted.getValue().getCellsInXAxis())){
                                list2 = tuple3s;
                            }
                        }
                    }

                    if(list1!=null && list2!=null){
                        Algorithms.bruteForce(list1, list2, results, radius);
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
//                    bruteForce(homeObjects, homeObjects, results, radius);
//
//                    List<Map.Entry<Integer,List<Tuple3<String,Double,Double>>>> replicatedObjects = new ArrayList<>(replicatedObjectsMap.entrySet());
//
//                    for (Map.Entry<Integer,List<Tuple3<String, Double, Double>>> tuple3s : replicatedObjects) {
//                        bruteForce1(homeObjects, tuple3s.getValue(), results, radius);
//                    }
//
//                    Map.Entry<Integer,List<Tuple3<String,Double,Double>>> list1 = null;
//                    Map.Entry<Integer,List<Tuple3<String,Double,Double>>> list2 = null;
//
//                    for (Map.Entry<Integer, List<Tuple3<String, Double, Double>>> replicatedObject : replicatedObjects) {
//                        if(replicatedObject.getKey() == (tup._1-1)){
//                            list1 = replicatedObject;
//                        }
//                        if(replicatedObject.getKey()==(tup._1-gridBroadcasted.getValue().getCellsInXAxis())){
//                            list2 = replicatedObject;
//                        }
//                    }
//
//                    if(list1!=null && list2!=null){
//                        bruteForce1(list1.getValue(), list2.getValue(), results, radius);
//                    }

                    return results.iterator();
                }
            }).count();


            time = (System.currentTimeMillis() - startJobTime);
            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();

        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer();
        FileWriter fw = new FileWriter("./timeExec-"+args[2]+"-"+args[1]+"-mrdsj-e-bf.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Math.round(time/1000.0)+","+Math.round((t1)/1000.0)+","+t._1()+","+t._2()+","+t._3()+"\n");
        bw.close();

        System.out.println("Sample Time: "+ sampleTime);
        System.out.println("Time Exec: "+ time);
        System.out.println("Count: "+count);

    }

}
