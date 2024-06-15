package gr.ds.unipi.grid;

import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GridTestYarn {

    @Test
    public void example(){
        List<Integer> removed = Arrays.asList(new Integer[2]);
        removed.add(3);
    }
    @Test
    public void newGrid() {
        Grid.experiments = "DIAG_PR";
        //NewFunc.function = NewFunc.datasetA;

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(0,0), Point.newPoint(100,100)),0.5, NewFunc.datasetA);
        System.out.println(Arrays.toString(grid.getPartitionsAType(80.190668, 26.035558)));
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