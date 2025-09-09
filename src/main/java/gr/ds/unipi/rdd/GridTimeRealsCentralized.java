package gr.ds.unipi.rdd;

import gr.ds.unipi.grid.*;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import scala.Function4;
import scala.Tuple3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GridTimeRealsCentralized {

    public static void main(String args[]) throws IOException {

        double radius = 0.018;
        gr.ds.unipi.grid.Function4 function = NewFunc.datasetA;
//        ReplicationType function = new DatasetA();
        Grid.experiments = "DIAG_PR";
        boolean firstSetHash = true;

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(-124.763068, 17.673976), Point.newPoint(-64.564908, 49.384359)), radius, function);
        String datasetASamplePath = "/Users/nicholaskoutroumanis/Desktop/synth-skewed/AREALM-Sample.csv";
        String datasetBSamplePath = "/Users/nicholaskoutroumanis/Desktop/synth-skewed/SPORTS-Sample.csv";
        String datasetAPath = "/Users/nicholaskoutroumanis/Desktop/synth-skewed/AREALM.csv";
        String datasetBPath = "/Users/nicholaskoutroumanis/Desktop/synth-skewed/SPORTS.csv";


        if(function != NewFunc.datasetA && function != NewFunc.datasetB){
            System.out.println("Sampling started");
            String line;
            BufferedReader br1 = new BufferedReader(new FileReader(datasetASamplePath));
            while((line = br1.readLine())!=null){
                String[] tuple = line.split("\t");
                double x = Double.parseDouble(tuple[1]);
                double y = Double.parseDouble(tuple[2]);
                grid.addPointDatasetA(x,y);
            }
            br1.close();

            BufferedReader br2 = new BufferedReader(new FileReader(datasetBSamplePath));
            while((line = br2.readLine())!=null){
                String[] tuple = line.split("\t");
                double x = Double.parseDouble(tuple[1]);
                double y = Double.parseDouble(tuple[2]);
                grid.addPointDatasetB(x,y);

            }
            br2.close();
        }

        int comparisonsCount = 0;
        int count = 0;
        final Function4<Double, Double, Double, Double, Double> distanceKM = (lon1, lat1, lon2, lat2) -> {
            return Math.sqrt(Math.pow((lon1 - lon2), 2) + Math.pow((lat1 - lat2), 2));
        };

        long startTime = System.currentTimeMillis();

        if(!firstSetHash){
            BufferedReader br2 = new BufferedReader(new FileReader(datasetAPath));
            BufferedReader br1 = new BufferedReader(new FileReader(datasetBPath));

            HashMap<Integer, List<Tuple3<String, Double, Double>>> hashMap = new HashMap();


            String line;
            while((line = br1.readLine())!=null){
                String[] tuple = line.split("\t");
                double x = Double.parseDouble(tuple[1]);
                double y = Double.parseDouble(tuple[2]);
                int[] indexes = grid.getPartitionsBTypeInExecutor(x, y);
                for (int index : indexes) {
                    if(hashMap.containsKey(index)){
                        hashMap.get(index).add(Tuple3.apply(tuple[0], x, y));
                    }else{
                        List<Tuple3<String, Double, Double>> list = new ArrayList<>();
                        list.add(Tuple3.apply(tuple[0], x, y));
                        hashMap.put(index,list);
                    }
                }
            }
            br1.close();

            System.out.println("HashMap has been build with "+hashMap.size()+" elements");
            while((line = br2.readLine())!=null){
                String[] tuple = line.split("\t");
                double x = Double.parseDouble(tuple[1]);
                double y = Double.parseDouble(tuple[2]);
                int[] indexes = grid.getPartitionsATypeInExecutor(x, y);
                for (int index : indexes) {
                    if(hashMap.get(index)!=null){
                        for (Tuple3<String, Double, Double> t : hashMap.get(index)) {
                            comparisonsCount++;
                            if (distanceKM.apply(x, y, t._2(), t._3()) <= radius) {
                                count++;
                            }
                        }
                    }
                }
            }
            br2.close();
        }else{
            BufferedReader br1 = new BufferedReader(new FileReader(datasetAPath));
            BufferedReader br2 = new BufferedReader(new FileReader(datasetBPath));

            HashMap<Integer, List<Tuple3<String, Double, Double>>> hashMap = new HashMap();


            String line;
            while((line = br1.readLine())!=null){
                String[] tuple = line.split("\t");
                double x = Double.parseDouble(tuple[1]);
                double y = Double.parseDouble(tuple[2]);
                int[] indexes = grid.getPartitionsATypeInExecutor(x, y);
                for (int index : indexes) {
                    if(hashMap.containsKey(index)){
                        hashMap.get(index).add(Tuple3.apply(tuple[0], x, y));
                    }else{
                        List<Tuple3<String, Double, Double>> list = new ArrayList<>();
                        list.add(Tuple3.apply(tuple[0], x, y));
                        hashMap.put(index,list);
                    }
                }
            }
            br1.close();

            System.out.println("HashMap has been build with "+hashMap.size()+" elements");
            while((line = br2.readLine())!=null){
                String[] tuple = line.split("\t");
                double x = Double.parseDouble(tuple[1]);
                double y = Double.parseDouble(tuple[2]);
                int[] indexes = grid.getPartitionsBTypeInExecutor(x, y);
                for (int index : indexes) {
                    if(hashMap.get(index)!=null){
                        for (Tuple3<String, Double, Double> t : hashMap.get(index)) {
                            comparisonsCount++;
                            if (distanceKM.apply(x, y, t._2(), t._3()) <= radius) {
                                count++;
                            }
                        }
                    }
                }
            }
            br2.close();
        }

        System.out.println("Time Exec: "+(System.currentTimeMillis()-startTime)/1000);
        System.out.println("Comparisons count: "+comparisonsCount);
        System.out.println("Count: "+count);
    }
}
