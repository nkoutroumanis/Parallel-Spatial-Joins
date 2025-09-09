package gr.ds.unipi.self;

import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.rdd.LPTPartitioner;
import gr.ds.unipi.rdd.LPTPartitionerBr;
import gr.ds.unipi.self.agreements.Agreements;
import gr.ds.unipi.self.agreements.Edge;
import gr.ds.unipi.self.agreements.Space;
import gr.ds.unipi.self.grid.Cell;
import gr.ds.unipi.self.grid.ReplicationFunc;
import gr.ds.unipi.self.grid.SelfGrid;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GridTimeRealsPrunedCorrectnessPlaneSweep {
    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;
        long sampleTime = 0;
        int repeats = Integer.parseInt(args[5]);
        for (int n = 0; n < repeats; n++) {
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{SelfGrid.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, ReplicationFunc.class, Position.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, /*org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, */ManifestFactory$.MODULE$.Any().getClass()/*, ConcurrentHashMap.class, java.util.function.Function.class*/});
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

            grid.printInfo();

        Partitioner cp = null;
        if(args[6].equals("HASH")){
            cp = new CustomPartitioner(Integer.parseInt(args[3]));
            grid.getCellsWithCosts1();
        }else if(args[6].equals("LPT1")){
            System.out.println("NEW Partitioner");
            Broadcast<Map<Integer, Integer>> lptBroadcasted = jsc.broadcast(LPTPartitionerBr.ltp(grid.getCellsWithCosts1(), Integer.parseInt(args[3])));
            cp = new LPTPartitionerBr(lptBroadcasted, Integer.parseInt(args[3]));
//            cp = new LPTPartitioner(grid.getCellsWithCosts1(),Integer.parseInt(args[3]));
        }else if(args[6].equals("LPT2")){
            cp = new LPTPartitioner(grid.getCellsWithCosts2(),Integer.parseInt(args[3]));
        }else{
            try {
                throw new Exception("No correct partitioner has been set");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        grid.printInfo();
            grid.printInfoAgreements();

//        grid.clean();

            //long startBroadcastTime = System.currentTimeMillis();
            Broadcast<SelfGrid> gridBroadcasted = jsc.broadcast(grid);
            //System.out.println("Broadcast Time:" + (System.currentTimeMillis() - startBroadcastTime));

//        if(flag == 8 || flag ==9){
//            gridBroadcasted.getValue().load();
//        }
//            gridBroadcasted.getValue().initialize();

            JavaPairRDD<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>> pairRDD = rddTuples.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple2<Integer,Tuple3<String, Double, Double>>>> list = new ArrayList<>();
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsInExecutor(tuple._2(), tuple._3());
                    for (int i = 0; i < cellIds.length; i++) {
                        list.add(new Tuple2<>(cellIds[i], new Tuple2<>(cellIds[0],tuple)));
                    }
                    return list.iterator();
                }
            });

            long count = pairRDD.groupByKey(cp).flatMap(new FlatMapFunction<Tuple2<Integer, Iterable<Tuple2<Integer, Tuple3<String, Double, Double>>>>, Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>>() {
                @Override
                public Iterator<Tuple2<Tuple3<String, Double, Double>, Tuple3<String, Double, Double>>> call(Tuple2<Integer, Iterable<Tuple2<Integer, Tuple3<String, Double, Double>>>> tup) throws Exception {

                    List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results = new ArrayList<>();

                    List<Tuple3<String, Double, Double>> homeObjects = new ArrayList<>();
                    Map<Integer,List<Tuple3<String,Double,Double>>> replicatedObjectsMap = new HashMap<>();

                    tup._2.forEach(obj->{
                        if(obj._1.equals(tup._1)){
                            homeObjects.add(obj._2);
                        }else{
                            if(replicatedObjectsMap.containsKey(obj._1)){
                                replicatedObjectsMap.get(obj._1).add(obj._2);
                            }else{
                                List<Tuple3<String,Double,Double>> replList = new ArrayList<>();
                                replList.add(obj._2);
                                replicatedObjectsMap.put(obj._1, replList);
                            }
                        }
                    });

                    homeObjects.sort(Comparator.comparingDouble(obj->obj._2()));
                    replicatedObjectsMap.forEach((in, l)->{
                        l.sort(Comparator.comparingDouble(obj->obj._2()));
                    });

                    planeSweep(homeObjects, homeObjects, results, radius);

                    List<Map.Entry<Integer,List<Tuple3<String,Double,Double>>>> replicatedObjects = new ArrayList<>(replicatedObjectsMap.entrySet());

                    for (Map.Entry<Integer,List<Tuple3<String, Double, Double>>> tuple3s : replicatedObjects) {
                        planeSweep1(homeObjects, tuple3s.getValue(), results, radius);
                    }

                    for (int i = 0; i < replicatedObjects.size(); i++) {
                        for (int j = i + 1; j < replicatedObjects.size(); j++) {
                            if(Math.abs(replicatedObjects.get(i).getKey()-replicatedObjects.get(j).getKey())==1 ||
                                    (Math.abs(replicatedObjects.get(i).getKey()-replicatedObjects.get(j).getKey())>=gridBroadcasted.getValue().getCellsInXAxis()-1 && Math.abs(replicatedObjects.get(i).getKey()-replicatedObjects.get(j).getKey())<=gridBroadcasted.getValue().getCellsInXAxis()+1)) {
                                planeSweep1(replicatedObjects.get(i).getValue(), replicatedObjects.get(j).getValue(), results, radius);
                            }
                        }
                    }
                    return results.iterator();
                }
            }).count();


            time = time + (System.currentTimeMillis() - startJobTime);
            cou = count;

            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Sample Time: "+ sampleTime/repeats);
        System.out.println("Time Exec: "+ time/repeats);
        System.out.println("Count: "+cou);

    }

    private static void planeSweep(List<Tuple3<String, Double, Double>> objectsA, List<Tuple3<String, Double, Double>> objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){


        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.size() && indexB<objectsB.size()){
            if(objectsA.get(indexA)._2()-(radius/2)<objectsB.get(indexB)._2()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.size() && (objectsA.get(indexA)._2()+(radius/2)>=objectsB.get(innerIndexB)._2()-(radius/2))){

                    if(indexA!=innerIndexB && (Math.pow((objectsA.get(indexA)._2() - objectsB.get(innerIndexB)._2()), 2) + Math.pow((objectsA.get(indexA)._3()- objectsB.get(innerIndexB)._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA.get(indexA), objectsB.get(innerIndexB)));
                    }
                    innerIndexB++;
                }

                indexA++;

                //extra command for self join
                indexB++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.size() && (objectsB.get(indexB)._2()+(radius/2)>=objectsA.get(innerIndexA)._2()-(radius/2))){

                    if(innerIndexA!=indexB && (Math.pow((objectsA.get(innerIndexA)._2() - objectsB.get(indexB)._2()), 2) + Math.pow((objectsA.get(innerIndexA)._3()- objectsB.get(indexB)._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA.get(innerIndexA), objectsB.get(indexB)));
                    }
                    innerIndexA++;
                }
                indexB++;

                //extra command for self join
                indexA++;
            }
        }
    }

    private static void planeSweep1(List<Tuple3<String, Double, Double>> objectsA, List<Tuple3<String, Double, Double>> objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.size() && indexB<objectsB.size()){
            if(objectsA.get(indexA)._2()-(radius/2)<objectsB.get(indexB)._2()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.size() && (objectsA.get(indexA)._2()+(radius/2)>=objectsB.get(innerIndexB)._2()-(radius/2))){

                    if((Math.pow((objectsA.get(indexA)._2() - objectsB.get(innerIndexB)._2()), 2) + Math.pow((objectsA.get(indexA)._3()- objectsB.get(innerIndexB)._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA.get(indexA), objectsB.get(innerIndexB)));
                    }
                    innerIndexB++;
                }
                indexA++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.size() && (objectsB.get(indexB)._2()+(radius/2)>=objectsA.get(innerIndexA)._2()-(radius/2))){

                    if((Math.pow((objectsA.get(innerIndexA)._2() - objectsB.get(indexB)._2()), 2) + Math.pow((objectsA.get(innerIndexA)._3()- objectsB.get(indexB)._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA.get(innerIndexA), objectsB.get(indexB)));
                    }
                    innerIndexA++;
                }
                indexB++;
            }
        }
    }
}
