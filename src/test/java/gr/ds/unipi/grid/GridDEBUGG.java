package gr.ds.unipi.grid;

import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions$;
import org.junit.Test;
import scala.reflect.internal.Trees;

import java.io.*;
import java.util.Arrays;

public class GridDEBUGG {

    @Test
    public void newGrid() throws IOException {
                //NewFunc.function = NewFunc.datasetA;
                double r1 = 2.3;
                //r1 = ((Math.floor(r1 * Math.pow(10, 2))) / Math.pow(10, 2));
                System.out.println(r1);
                Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(100, 100)), r1, NewFunc.datasetA);

                String line = null;

                String pathDatasetA = "/Users/nicholaskoutroumanis/Desktop/joins/skewed/datasetA.csv";
                String pathDatasetB = "/Users/nicholaskoutroumanis/Desktop/joins/skewed/datasetB.csv";

                String pathNewDatasetA = "/Users/nicholaskoutroumanis/Desktop/joins/datasetA.csv";
                String pathNewDatasetB = "/Users/nicholaskoutroumanis/Desktop/joins/datasetB.csv";

                BufferedReader bf = new BufferedReader(new FileReader(pathDatasetA));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    grid.addPointDatasetA(Double.parseDouble(record[0]), Double.parseDouble(record[1]));
                }
                bf.close();

                bf = new BufferedReader(new FileReader(pathDatasetB));
                while ((line = bf.readLine()) != null) {
                    String[] record = line.split(";");
                    grid.addPointDatasetB(Double.parseDouble(record[0]), Double.parseDouble(record[1]));
                }
                bf.close();

                grid.checkforsymmetry();
        System.out.println(Arrays.toString(grid.getPartitionsATypeInExecutor(24.527145678047727,26.0246608480878)));
        System.out.println(Arrays.toString(grid.getPartitionsBTypeInExecutor(22.646116576767454, 25.765539676176193)));

    }
}