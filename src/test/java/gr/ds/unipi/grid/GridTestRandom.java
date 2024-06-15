package gr.ds.unipi.grid;

import gr.ds.unipi.grid.Grid;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class GridTestRandom {

    @Test
    public void newGrid() {
        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0,0), Point.newPoint(100,100)),0.05, NewFunc.datasetA);
        Random random = new Random();

        long startTime = System.currentTimeMillis();
        for(int i = 0;i< 4000000;i++){
            grid.addPointDatasetA((99d) * random.nextDouble(), (99d) * random.nextDouble());
            grid.addPointDatasetB((99d) * random.nextDouble(), (99d) * random.nextDouble());
        }
        for(int i = 0;i< 4000000;i++){
            grid.addPointDatasetA((50d) * random.nextDouble(), (50d) * random.nextDouble());
        }
        System.out.println("inserted");

        System.out.println("Time: "+(System.currentTimeMillis()-startTime)/1000);

        System.out.println("a1:"+Arrays.toString(grid.getPartitionsBType(40, 10))+ " a11:"+Arrays.toString(grid.getPartitionsBType(60, 10)));
        System.out.println("a2:"+Arrays.toString(grid.getPartitionsBType(40, 19))+ " a12:"+Arrays.toString(grid.getPartitionsBType(60, 19)));
        System.out.println("a3:"+Arrays.toString(grid.getPartitionsBType(40, 25))+ " a13:"+Arrays.toString(grid.getPartitionsBType(60, 25)));
        System.out.println("a4:"+Arrays.toString(grid.getPartitionsBType(40, 31))+ " a14:"+Arrays.toString(grid.getPartitionsBType(60, 31)));
        System.out.println("a5:"+Arrays.toString(grid.getPartitionsBType(40, 40))+ " a15:"+Arrays.toString(grid.getPartitionsBType(60, 40)));
        System.out.println("a6:"+Arrays.toString(grid.getPartitionsBType(40, 60))+ " a16:"+Arrays.toString(grid.getPartitionsBType(60, 60)));
        System.out.println("a7:"+Arrays.toString(grid.getPartitionsBType(40, 69))+ " a17:"+Arrays.toString(grid.getPartitionsBType(60, 69)));
        System.out.println("a8:"+Arrays.toString(grid.getPartitionsBType(40, 75))+ " a18:"+Arrays.toString(grid.getPartitionsBType(60, 75)));
        System.out.println("a9:"+Arrays.toString(grid.getPartitionsBType(40, 81))+ " a19:"+Arrays.toString(grid.getPartitionsBType(60, 81)));
        System.out.println("a10:"+Arrays.toString(grid.getPartitionsBType(40, 90))+ " a20:"+Arrays.toString(grid.getPartitionsBType(60, 90)));

        System.out.println();
        System.out.println("b1:"+Arrays.toString(grid.getPartitionsBType(25, 25))+ " b5:"+Arrays.toString(grid.getPartitionsBType(75, 25)));
        System.out.println("b2:"+Arrays.toString(grid.getPartitionsBType(25, 45))+ " b6:"+Arrays.toString(grid.getPartitionsBType(75, 45)));
        System.out.println("b3:"+Arrays.toString(grid.getPartitionsBType(25, 55))+ " b7:"+Arrays.toString(grid.getPartitionsBType(75, 55)));
        System.out.println("b4:"+Arrays.toString(grid.getPartitionsBType(25, 75))+ " b8:"+Arrays.toString(grid.getPartitionsBType(75, 75)));
    }
}