package gr.ds.unipi.grid;

import gr.ds.unipi.grid.Grid;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GridTest {

    @Test
    public void hoopTesting() {
        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0, 0), Point.newPoint(140, 120)), 7, 6, 0.1, NewFunc.datasetA);

        grid.getCellsInHoop(Point.newPoint(110, 10),6).forEach(System.out::println);

        grid.getCellsIds(Rectangle.newRectangle(Point.newPoint(70,50), Point.newPoint(110,90))).forEach(System.out::println);

    }

    @Test
    public void example(){
        List<Integer> removed = Arrays.asList(new Integer[2]);
        removed.add(3);
    }
    @Test
    public void newGrid() {
        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0,0), Point.newPoint(100,100)),2,2,20, NewFunc.datasetA);
        grid.addPointDatasetA(50,50);
        grid.addPointDatasetA(51,51);
        grid.addPointDatasetB(50,50);
        grid.addPointDatasetB(51,51);


        grid.addPointDatasetA(1,1);
        grid.addPointDatasetA(2,2);
        grid.addPointDatasetB(11,11);
        grid.addPointDatasetB(12,12);

        grid.addPointDatasetA(21,81);
        grid.addPointDatasetB(20,80);
        grid.addPointDatasetB(21,81);

        grid.addPointDatasetA(80,20);
        grid.addPointDatasetA(81,21);
        grid.addPointDatasetB(81,21);

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

        System.out.println("a1:"+Arrays.toString(grid.getPartitionsAType(40, 10))+ " a11:"+Arrays.toString(grid.getPartitionsAType(60, 10)));
        System.out.println("a2:"+Arrays.toString(grid.getPartitionsAType(40, 19))+ " a12:"+Arrays.toString(grid.getPartitionsAType(60, 19)));
        System.out.println("a3:"+Arrays.toString(grid.getPartitionsAType(40, 25))+ " a13:"+Arrays.toString(grid.getPartitionsAType(60, 25)));
        System.out.println("a4:"+Arrays.toString(grid.getPartitionsAType(40, 31))+ " a14:"+Arrays.toString(grid.getPartitionsAType(60, 31)));
        System.out.println("a5:"+Arrays.toString(grid.getPartitionsAType(40, 40))+ " a15:"+Arrays.toString(grid.getPartitionsAType(60, 40)));
        System.out.println("a6:"+Arrays.toString(grid.getPartitionsAType(40, 60))+ " a16:"+Arrays.toString(grid.getPartitionsAType(60, 60)));
        System.out.println("a7:"+Arrays.toString(grid.getPartitionsAType(40, 69))+ " a17:"+Arrays.toString(grid.getPartitionsAType(60, 69)));
        System.out.println("a8:"+Arrays.toString(grid.getPartitionsAType(40, 75))+ " a18:"+Arrays.toString(grid.getPartitionsAType(60, 75)));
        System.out.println("a9:"+Arrays.toString(grid.getPartitionsAType(40, 81))+ " a19:"+Arrays.toString(grid.getPartitionsAType(60, 81)));
        System.out.println("a10:"+Arrays.toString(grid.getPartitionsAType(40, 90))+ " a20:"+Arrays.toString(grid.getPartitionsAType(60, 90)));

        System.out.println();
        System.out.println("b1:"+Arrays.toString(grid.getPartitionsAType(25, 25))+ " b5:"+Arrays.toString(grid.getPartitionsAType(75, 25)));
        System.out.println("b2:"+Arrays.toString(grid.getPartitionsAType(25, 45))+ " b6:"+Arrays.toString(grid.getPartitionsAType(75, 45)));
        System.out.println("b3:"+Arrays.toString(grid.getPartitionsAType(25, 55))+ " b7:"+Arrays.toString(grid.getPartitionsAType(75, 55)));
        System.out.println("b4:"+Arrays.toString(grid.getPartitionsAType(25, 75))+ " b8:"+Arrays.toString(grid.getPartitionsAType(75, 75)));

        //System.out.println(grid.toString());

    }

    @Test
    public void errorCase() {

        final long cellsInXAxis = 499;
        final long cellsInYAxis = 499;

        final double x = (100d/cellsInXAxis);
        final double y =  (100d/cellsInYAxis);

        long xc = (long) ((75 / x));
        long yc = (long) ((75 / y));
        System.out.println(xc +" "+yc);
        System.out.println("Code: "+ (xc + (yc * cellsInXAxis)));
        System.out.println("Rectangle: ("+(xc*x)+","+(yc*x)+" ) ("+(((xc)*x)+x)+","+(((yc)*y)+y)+" )");


        long xc1 = (long) ((x*xc)/ x);
        long yc1 = (long) ((y*yc) / y);
        System.out.println(((x*xc)/ x)+" "+ ((y*yc) / y));
        System.out.println(xc1 +" "+yc1);
        System.out.println("Code: "+ (xc1 + (yc1 * cellsInXAxis)));
        System.out.println("Rectangle: ("+(xc1*x)+","+(yc1*x)+" ) ("+(((xc1)*x)+x)+","+(((yc1)*y)+y)+" )");

    }

    @Test
    public void errorCaseFixed() {

        final long cellsInXAxis = 499;
        final long cellsInYAxis = 499;

        double x = (100d/cellsInXAxis);
        double y =  (100d/cellsInYAxis);

        x = (Math.floor(x * Math.pow(10,12)))/Math.pow(10,12);
        y = (Math.floor(y * Math.pow(10,12)))/Math.pow(10,12);

        long xc = (long) ((74.9498997995220 / x));
        long yc = (long) ((74.9498997995220 / y));
        System.out.println(xc +" "+yc);
        System.out.println("Code: "+ (xc + (yc * cellsInXAxis)));
        System.out.println("Rectangle: ("+(xc*x)+","+(yc*x)+" ) ("+(((xc)*x)+x)+","+(((yc)*y)+y)+" )");


        long xc1 = (long) ((x*xc)/ x);
        long yc1 = (long) ((y*yc) / y);
        System.out.println(((x*xc)/ x)+" "+ ((y*yc) / y));
        System.out.println(xc1 +" "+yc1);
        System.out.println("Code: "+ (xc1 + (yc1 * cellsInXAxis)));
        System.out.println("Rectangle: ("+(xc1*x)+","+(yc1*x)+" ) ("+(((xc1)*x)+x)+","+(((yc1)*y)+y)+" )");


        long xc2 = (long) ((x*373)/ x);
        long yc2 = (long) ((y*373) / y);
        System.out.println(((x*373)/ x)+" "+ ((y*373) / y));
        System.out.println(xc2 +" "+yc2);
        System.out.println("Code: "+ (xc2 + (yc2 * cellsInXAxis)));
        System.out.println("Rectangle: ("+(xc2*x)+","+(yc2*x)+" ) ("+(((xc2)*x)+x)+","+(((yc2)*y)+y)+" )");

    }

    @Test
    public void testNumbers1() {

        double value = 974.94989901781082;
        value = (Math.floor(value * Math.pow(10,5)))/Math.pow(10,5);
        System.out.println(value);
    }
    @Test
    public void testNumbers2() {
        final long cellsInXAxis = 499;
        final long cellsInYAxis = 499;

        double x = (100d/cellsInXAxis);
        double y =  (100d/cellsInYAxis);

        x = (Math.floor(x * Math.pow(10,12)))/Math.pow(10,12);
        y = (Math.floor(y * Math.pow(10,12)))/Math.pow(10,12);

        long xc = (long) ((40 / x));
        long yc = (long) ((10 / y));
        System.out.println(xc + (yc * cellsInXAxis));
    }

    @Test
    public void testNumbers3() {
        final long cellsInXAxis = 499;
        final long cellsInYAxis = 499;

        double x = (100d/cellsInXAxis);
        double y =  (100d/cellsInYAxis);

        x = (Math.floor(x * Math.pow(10,12)))/Math.pow(10,12);
        y = (Math.floor(y * Math.pow(10,13)))/Math.pow(10,12);

        long xc = (long) ((40 / x));
        long yc = (long) ((90 / y));

        System.out.println(xc +" "+yc);

        System.out.println(xc + (yc * cellsInXAxis));
        System.out.println("Rectangle: ("+(xc*x)+","+(yc*x)+" ) ("+(((xc)*x)+x)+","+(((yc)*y)+y)+" )");


        long xc1 = (long) ((x*xc)/ x);
        long yc1 = (long) ((y*yc) / y);
        System.out.println(xc1 +" "+yc1);
        System.out.println("Code: "+ (xc1 + (yc1 * cellsInXAxis)));
        System.out.println("Rectangle: ("+(xc1*x)+","+(yc1*x)+" ) ("+(((xc1)*x)+x)+","+(((yc1)*y)+y)+" )");

    }

}