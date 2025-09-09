package gr.ds.unipi.rdd.variant.stripes;

import gr.ds.unipi.Algorithms;
import gr.ds.unipi.SparkLogParser;
import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.grid.Cell;
import gr.ds.unipi.grid.Function4;
import gr.ds.unipi.grid.Grid;
import gr.ds.unipi.grid.NewFunc;
import gr.ds.unipi.rdd.CustomPartitioner;
import gr.ds.unipi.rdd.LPTPartitioner;
import gr.ds.unipi.rdd.LPTPartitionerBr;
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

public class GridTimeRealsPrunedPlaneSweepX {
    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;
        long sampleTime = 0;
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, /*org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, */ManifestFactory$.MODULE$.Any().getClass()/*, ConcurrentHashMap.class, java.util.function.Function.class*/});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
            //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
            String path = args[0];
            double radius = Double.parseDouble(args[1]);
            int flag = Integer.parseInt(args[2]);

//            ReplicationType function = null;
//            if (flag == 1) {
//                function = new DatasetA();
//            }
            Function4<Cell, Cell, Point, Agreement> function = null;
            if (flag == 1) {
                function = NewFunc.datasetA;
            } else if (flag == 2) {
                function = NewFunc.datasetB;
            } else if (flag == 3) {
                function = NewFunc.costBasedCombinedWithBoundaries;
            } else if (flag == 4) {
                function = NewFunc.lesserPointsinBoundaries;
            } else if (flag == 5) {
                function = NewFunc.dok;
            } else if (flag == 6) {
                function = NewFunc.dok1;
            } else if (flag == 7) {
                function = NewFunc.dok2;
            } else if (flag == 8) {
                function = NewFunc.costBasedBackpropagation;
            } else if (flag == 9) {
                function = NewFunc.case3Backpropagation;
            } else {
                try {
                    throw new Exception("Wrong Flag");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (args[6].equals("EMPTY")) {
                Grid.experiments = "";
            } else if (args[6].equals("DIAG_ONLY")) {
                Grid.experiments = "DIAG_ONLY";
            } else if (args[6].equals("DIAG_PR")) {
                Grid.experiments = "DIAG_PR";
            } else if (args[6].equals("DIAG_COMP")) {
                Grid.experiments = "DIAG_COMP";
            } else {
                try {
                    throw new Exception("seventh argument has not been set correctly");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            long startJobTime = System.currentTimeMillis();
            //CHANGE HERE
            //Grid grid = Grid.newGeoGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
//            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
            Grid grid = Grid.newGridStripesX(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, function);

            if (!(flag == 1 || flag == 2) || !args[8].equals("HASH")) {
                long o = System.currentTimeMillis();

                BufferedReader readerA = new BufferedReader(new FileReader("/home/user/samples-"+args[7]+"/"+args[3]+".csv"));
                BufferedReader readerB = new BufferedReader(new FileReader("/home/user/samples-"+args[7]+"/"+args[4]+".csv"));

                String strLine;

                while ((strLine = readerA.readLine()) != null)   {
                    // Print the content on the console
                    String[] line = strLine.split("\t");
                    grid.addPointDatasetA(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
                }

                while ((strLine = readerB.readLine()) != null)   {
                    // Print the content on the console
                    String[] line = strLine.split("\t");
                    grid.addPointDatasetB(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
                }

                readerA.close();
                readerB.close();
                sampleTime = sampleTime + (System.currentTimeMillis() - o);
                System.out.println("OK with sampling Time:" + (System.currentTimeMillis() - o));
                if (flag == 8 || flag == 9) {
                    grid.load();
                }
            }

        grid.printInfo();

        Partitioner cp = null;
        if(args[8].equals("HASH")){
            cp = new CustomPartitioner(Integer.parseInt(args[5]));
        }else if(args[8].equals("LPT1")){
            System.out.println("NEW Partitioner");
            Broadcast<Map<Integer, Integer>> lptBroadcasted = jsc.broadcast(LPTPartitionerBr.ltp(grid.getCellsWithCosts1(), Integer.parseInt(args[5])));
            cp = new LPTPartitionerBr(lptBroadcasted, Integer.parseInt(args[5]));
//            cp = new LPTPartitioner(grid.getCellsWithCosts1(),Integer.parseInt(args[5]));
        }else if(args[8].equals("LPT2")){
            cp = new LPTPartitioner(grid.getCellsWithCosts2(),Integer.parseInt(args[5]));
        }else{
            try {
                throw new Exception("No correct partitioner has been set");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        grid.printInfo();
//        grid.clean();

        //long startBroadcastTime = System.currentTimeMillis();
        Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);
        //System.out.println("Broadcast Time:" + (System.currentTimeMillis() - startBroadcastTime));

        long t1 = System.currentTimeMillis() - startJobTime;

            JavaRDD<String> rddTxtFilesA = jsc.textFile(path + args[3] + ".csv"/*,Integer.parseInt(args[5])*/);
            JavaRDD<String> rddTxtFilesB = jsc.textFile(path + args[4] + ".csv"/*,Integer.parseInt(args[5])*/);

            JavaRDD<Tuple3<String, Double, Double>> rddTuplesA = rddTxtFilesA.map((String line) -> {
                String[] elements = line.split("\t");
                return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
            });

            JavaRDD<Tuple3<String, Double, Double>> rddTuplesB = rddTxtFilesB.map((String line) -> {
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

            JavaPairRDD<Integer, Tuple3<String, Double, Double>> pairRDDA = rddTuplesA.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple3<String, Double, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>();
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsATypeInExecutor(tuple._2(), tuple._3());
                    for (int cellId : cellIds) {
                        list.add(new Tuple2<>(cellId, tuple));
                    }
                    return list.iterator();
                }
            });

            JavaPairRDD<Integer, Tuple3<String, Double, Double>> pairRDDB = rddTuplesB.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple3<String, Double, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>();
                    int[] cellIds = gridBroadcasted.getValue().getPartitionsBTypeInExecutor(tuple._2(), tuple._3());
                    for (int cellId : cellIds) {
                        list.add(new Tuple2<>(cellId, tuple));
                    }
                    return list.iterator();
                }
            });

            long count = pairRDDA.cogroup(pairRDDB,cp).flatMap(new FlatMapFunction<Tuple2<Integer, Tuple2<Iterable<Tuple3<String, Double, Double>>, Iterable<Tuple3<String, Double, Double>>>>, Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>>() {
                @Override
                 public Iterator<Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>> call(Tuple2<Integer, Tuple2<Iterable<Tuple3<String, Double, Double>>, Iterable<Tuple3<String, Double, Double>>>> tup) throws Exception {

                    Grid g = gridBroadcasted.getValue();
                    int nativeObjectsASize=0, nativeObjectsBSize=0, externalObjectsASize=0, externalObjectsBSize=0;

                    for (Tuple3<String, Double, Double> obj : tup._2._1) {
                        if(g.getCellId(obj._2(), obj._3()) == tup._1){
                            nativeObjectsASize++;
                        }else{
                            externalObjectsASize++;
                        }
                    }

                    for (Tuple3<String, Double, Double> obj : tup._2._2) {
                        if(g.getCellId(obj._2(), obj._3()) == tup._1){
                            nativeObjectsBSize++;
                        }else{
                            externalObjectsBSize++;
                        }
                    }

                    Tuple3<String, Double, Double>[] nativeObjectsA = new Tuple3[nativeObjectsASize];
                    Tuple3<String, Double, Double>[]  nativeObjectsB = new Tuple3[nativeObjectsBSize];

                    Tuple3<String, Double, Double>[]  externalObjectsA = new Tuple3[externalObjectsASize];
                    Tuple3<String, Double, Double>[]  externalObjectsB = new Tuple3[externalObjectsBSize];

                    nativeObjectsASize=0;
                    nativeObjectsBSize=0;
                    externalObjectsASize=0;
                    externalObjectsBSize=0;

                    for (Tuple3<String, Double, Double> obj : tup._2._1) {
                        if(g.getCellId(obj._2(), obj._3()) == tup._1){
                            nativeObjectsA[nativeObjectsASize++]=obj;
                        }else{
                            externalObjectsA[externalObjectsASize++]=obj;
                        }
                    }

                    for (Tuple3<String, Double, Double> obj : tup._2._2) {
                        if(g.getCellId(obj._2(), obj._3()) == tup._1){
                            nativeObjectsB[nativeObjectsBSize++]=obj;
                        }else{
                            externalObjectsB[externalObjectsBSize++]=obj;
                        }
                    }

                    Arrays.sort(nativeObjectsA,Comparator.comparingDouble(obj->obj._3()));
                    Arrays.sort(nativeObjectsB,Comparator.comparingDouble(obj->obj._3()));

                    Arrays.sort(externalObjectsA,Comparator.comparingDouble(obj->obj._3()));
                    Arrays.sort(externalObjectsB,Comparator.comparingDouble(obj->obj._3()));

                    List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results = new ArrayList<>();

                    Algorithms.planeSweepOnY(nativeObjectsA, nativeObjectsB, results, radius);
                    Algorithms.planeSweepOnY(nativeObjectsA, externalObjectsB, results, radius);
                    Algorithms.planeSweepOnY(nativeObjectsB, externalObjectsA, results, radius);
                    return results.iterator();
                }
            }).count();

            time = time + (System.currentTimeMillis() - startJobTime);
            cou = count;
        System.out.println("Counted: "+cou);

            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();

        Tuple3<String, String, String> t = SparkLogParser.fileLogAnalyzer();
        FileWriter fw = new FileWriter("./timeExec-"+args[3]+"-"+args[4]+"-"+args[1]+"-1D-x.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Math.round(time/1000.0)+","+Math.round((t1)/1000.0)+","+t._1()+","+t._2()+","+t._3()+"\n");
        bw.close();

    }
//    private static void planeSweep(List<Tuple3<String, Double, Double>> objectsA, List<Tuple3<String, Double, Double>> objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
//        int indexA = 0;
//        int indexB = 0;
//
//        while(indexA<objectsA.size() && indexB<objectsB.size()){
//            if(objectsA.get(indexA)._3()-(radius/2)<objectsB.get(indexB)._3()-(radius/2)){
//
//                int innerIndexB = indexB;
//                while(innerIndexB<objectsB.size() && (objectsA.get(indexA)._3()+(radius/2)>=objectsB.get(innerIndexB)._3()-(radius/2))){
//
//                    if((Math.pow((objectsA.get(indexA)._2() - objectsB.get(innerIndexB)._2()), 2) + Math.pow((objectsA.get(indexA)._3()- objectsB.get(innerIndexB)._3()), 2) <= Math.pow(radius, 2))){
////                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
//                        results.add(Tuple2.apply(objectsA.get(indexA), objectsB.get(innerIndexB)));
//                    }
//                    innerIndexB++;
//                }
//                indexA++;
//            }else{
//
//                int innerIndexA = indexA;
//                while(innerIndexA<objectsA.size() && (objectsB.get(indexB)._3()+(radius/2)>=objectsA.get(innerIndexA)._3()-(radius/2))){
//
//                    if((Math.pow((objectsA.get(innerIndexA)._2() - objectsB.get(indexB)._2()), 2) + Math.pow((objectsA.get(innerIndexA)._3()- objectsB.get(indexB)._3()), 2) <= Math.pow(radius, 2))){
////                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
//                        results.add(Tuple2.apply(objectsA.get(innerIndexA), objectsB.get(indexB)));
//                    }
//                    innerIndexA++;
//                }
//                indexB++;
//            }
//        }
//    }
}
