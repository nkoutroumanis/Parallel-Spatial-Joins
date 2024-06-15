package gr.ds.unipi.rdd;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.grid.*;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;
import scala.Tuple3;
import scala.reflect.ManifestFactory$;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GridTimeDist {

    public static void main(String args[]) throws IOException {
        long time =0;
        long cou=0;
        long sampleTime = 0;
        int repeats = Integer.parseInt(args[8]);
        for (int n = 0; n < repeats; n++) {
            SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                    .registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class, Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, java.util.HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, ManifestFactory$.class, ManifestFactory$.MODULE$.Any().getClass(), ConcurrentHashMap.class});
            SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
            JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
            //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
            String path = args[0];
            double radius = Double.parseDouble(args[1]);
            int flag = Integer.parseInt(args[2]);

            TriFunction<Cell, Cell, Point, Agreement> function = null;
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
//            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), radius, function);
            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564909, 49.384360)), radius, function);

            if (!(flag == 1 || flag == 2) || !args[9].equals("HASH")) {
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

            JavaRDD<String> rddTxtFilesA = jsc.textFile(path + args[3] + ".csv"/*,Integer.parseInt(args[5])*/);
            JavaRDD<String> rddTxtFilesB = jsc.textFile(path + args[4] + ".csv"/*,Integer.parseInt(args[5])*/);

            JavaRDD<Tuple3<String, Double, Double>> rddTuplesA = rddTxtFilesA.map((String line) -> {
                String[] elements = line.split(";");
                return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
            });

            JavaRDD<Tuple3<String, Double, Double>> rddTuplesB = rddTxtFilesB.map((String line) -> {
                String[] elements = line.split(";");
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
            if(args[9].equals("HASH")){
                cp = new CustomPartitioner(Integer.parseInt(args[5]));
            }else if(args[9].equals("LPT1")){
                cp = new LPTPartitioner(grid.getCellsWithCosts1(),Integer.parseInt(args[5]));
            }else if(args[9].equals("LPT2")){
                cp = new LPTPartitioner(grid.getCellsWithCosts2(),Integer.parseInt(args[5]));
            }else{
                try {
                    throw new Exception("No correct partitioner has been set");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            grid.printInfo();

            Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);

            //gridBroadcasted.getValue().load();
            JavaPairRDD<Integer, Tuple3<String, Double, Double>> pairRDDA = rddTuplesA.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple3<String, Double, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>();
                    String[] cellIds = gridBroadcasted.getValue().getPartitionsAType(tuple._2(), tuple._3());
                    for (String cellId : cellIds) {
                        list.add(new Tuple2<>(Integer.parseInt(cellId), tuple));
                    }
                    return list.iterator();
                }
            });

            JavaPairRDD<Integer, Tuple3<String, Double, Double>> pairRDDB = rddTuplesB.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double>, Integer, Tuple3<String, Double, Double>>() {
                @Override
                public Iterator<Tuple2<Integer, Tuple3<String, Double, Double>>> call(Tuple3<String, Double, Double> tuple) throws Exception {
                    List<Tuple2<Integer, Tuple3<String, Double, Double>>> list = new ArrayList<>();
                    String[] cellIds = gridBroadcasted.getValue().getPartitionsBType(tuple._2(), tuple._3());
                    for (String cellId : cellIds) {
                        list.add(new Tuple2<>(Integer.parseInt(cellId), tuple));
                    }
                    return list.iterator();
                }
            });

            JavaPairRDD<Integer, Tuple2<Tuple3<String, Double, Double>, Tuple3<String, Double, Double>>> joinedRDD = pairRDDA.join(pairRDDB, cp);

            long count = joinedRDD.filter((t) -> {
                if (Math.pow((t._2._1._2() - t._2._2._2()), 2) + Math.pow((t._2._1._3() - t._2._2._3()), 2) <= Math.pow(radius, 2)) {
                    return true;
                } else {
                    return false;
                }
            }).count();

            time = time + (System.currentTimeMillis() - startJobTime);
            cou = count;

            gridBroadcasted.unpersist(true);
            jsc.close();
            sparkSession.close();
        }
        System.out.println("Sample Time: "+ sampleTime/repeats);
        System.out.println("Time Exec: "+ time/repeats);
        System.out.println("Count: "+cou);
    }

}