package gr.ds.unipi.rdd;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.grid.*;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaFutureAction;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import scala.Tuple3;
import scala.reflect.ManifestFactory$;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GridTimeDistOLD {

    public static void main(String args[]) {
        SparkConf sparkConf = new SparkConf().set("spark.serializer","org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Grid.class, Agreement.class, Agreements.class, Edge.class, Edge[].class,  Space.class, Cell.class, NewFunc.class, Position.class, TypeSet.class, java.util.HashMap.class, Point.class, Rectangle.class, ArrayList.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, scala.reflect.ManifestFactory$.class,scala.reflect.ManifestFactory$.MODULE$.Any().getClass() });
        SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[*]")*/.getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());

        //System.out.println(sparkSession.sparkContext().getConf().get("spark.executor.instances"));
        String path = args[0];
        double radius = Double.parseDouble(args[1]);
        int flag = Integer.parseInt(args[2]);

        TriFunction<Cell, Cell, Point, Agreement> function = null;
        if(flag==1){
            function = NewFunc.datasetA;
        }else if(flag==2){
            function = NewFunc.datasetB;
        }else if(flag==3){
            function = NewFunc.costBasedCombinedWithBoundaries;
        }else if(flag == 4){
            function = NewFunc.lesserPointsinBoundaries;
        }else if(flag == 5){
            function = NewFunc.dok;
        }else if(flag == 6){
            function = NewFunc.dok1;
        }else if(flag == 7){
            function = NewFunc.dok2;
        }else if(flag == 8){
            function = NewFunc.costBasedBackpropagation;
        }else if(flag == 9){
            function = NewFunc.case3Backpropagation;
        }
        else{
            try {
                throw new Exception("Wrong Flag");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(args[6].equals("EMPTY")){
            Grid.experiments = "";
        }else if(args[6].equals("DIAG_ONLY")){
            Grid.experiments = "DIAG_ONLY";
        }else if(args[6].equals("DIAG_PR")){
            Grid.experiments = "DIAG_PR";
        }else if(args[6].equals("DIAG_COMP")){
            Grid.experiments = "DIAG_COMP";
        }else{
            try {
                throw new Exception("seventh argument has not been set correctly");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CustomPartitioner cp = new CustomPartitioner(Integer.parseInt(args[5]));

        long startIndexingTime = System.currentTimeMillis();
        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), radius, function);

        JavaRDD<String> rddTxtFilesA = jsc.textFile(path + "datasetA"+args[3]+".csv");
        JavaRDD<String> rddTxtFilesB = jsc.textFile(path + "datasetB"+args[4]+".csv");

        JavaRDD<Tuple3<String, Double, Double>> rddTuplesA = rddTxtFilesA.map((String line)->{
            String[] elements = line.split(";");
            return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
        });

        JavaRDD<Tuple3<String, Double, Double>> rddTuplesB = rddTxtFilesB.map((String line)->{
            String[] elements = line.split(";");
            return new Tuple3<>(elements[0], Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
        });

        if(!(flag == 1 || flag ==2)) {
            //System.out.println("A: "+rddTuplesA.sample(false, 0.01,261).count() +" B:"+rddTuplesB.sample(false, 0.01,261).count());

            rddTuplesA.sample(false, 0.01,261).collect().forEach((tuple) ->
                    grid.addPointDatasetA(tuple._2(), tuple._3())
            );

            rddTuplesB.sample(false, 0.01,261).collect().forEach((tuple) ->
                    grid.addPointDatasetB(tuple._2(), tuple._3())
            );
            System.out.println("OK with sampling");
        }
        grid.load();

        Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);

        JavaPairRDD<Integer,Tuple3<String, Double, Double>> pairRDDA = rddTuplesA.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double> , Integer, Tuple3<String, Double, Double>>() {
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

        JavaPairRDD<Integer,Tuple3<String, Double, Double>> pairRDDB = rddTuplesB.flatMapToPair(new PairFlatMapFunction<Tuple3<String, Double, Double> , Integer, Tuple3<String, Double, Double>>() {
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

        System.out.println("Count: "+ pairRDDA.count()+" "+pairRDDB.count() +" ");

        System.out.println("Indexing Time: "+(System.currentTimeMillis()-startIndexingTime));

        long loadTimeMemory = System.currentTimeMillis();
        pairRDDA = pairRDDA.persist(StorageLevel.MEMORY_ONLY_SER());
        pairRDDB = pairRDDB.persist(StorageLevel.MEMORY_ONLY_SER());

         pairRDDA.count();
         pairRDDB.count();

        gridBroadcasted.unpersist(true);
        System.out.println("Load Time Memory: "+(System.currentTimeMillis()-loadTimeMemory));


        long startJoinTime = System.currentTimeMillis();
        pairRDDA = pairRDDA.partitionBy(cp);
        pairRDDB = pairRDDB.partitionBy(cp);

        JavaPairRDD<Integer, Tuple2<Tuple3<String, Double, Double>,Tuple3<String, Double, Double>>> joinedRDD = pairRDDA.join(pairRDDB, cp);

        long count = joinedRDD.filter((t)-> {
            if(Math.pow((t._2._1._2()-t._2._2._2()),2)+Math.pow((t._2._1._3()-t._2._2._3()),2)<=Math.pow(radius,2)){
                return true;
            }else {
                return false;
            }
        }).count();
        System.out.println("Joining Time: "+(System.currentTimeMillis()-startJoinTime));
        System.out.println("Count: "+count);

        //gridBroadcasted.unpersist(true);
        pairRDDA.unpersist();
        pairRDDB.unpersist();

        jsc.stop();
        sparkSession.stop();
    }

}