package gr.ds.unipi.epsilongrid;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.shapes.Circle;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class EpsilonGrid {
    private final Rectangle rectangle;
    private final long cellsInXAxis;
    private final long cellsInYAxis;

    private final double x;
    private final double y;

    private final double r;

    public static EpsilonGrid newGrid(Rectangle rectangle, double r){
        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(r));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }
        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(r));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (long) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (long) cellsInYAxis);

        return new EpsilonGrid(rectangle, (long) cellsInXAxis, (long) cellsInYAxis, r);
    }
    private EpsilonGrid(Rectangle rectangle, long cellsInXAxis, long cellsInYAxis, double r){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        this.r = r;

        if(Double.compare(r,0) != 1){
            try {
                throw new Exception("r should be greater than 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX() ) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY() ) / cellsInYAxis;
        System.out.println("x: "+x +" y:"+y);
    }

    private boolean hasTop(long cellID){
        if((cellID + cellsInXAxis)< (cellsInXAxis*cellsInYAxis)){
            return true;
        }
        return false;
    }

    private boolean hasBottom(long cellID){
        if((cellID - cellsInXAxis)>= 0){
            return true;
        }
        return false;

    }

    private boolean hasLeft(long cellID){
        if(((cellID -1) >= 0) && ((cellID - 1)%(cellsInXAxis)) != (cellsInXAxis-1)){
            return true;
        }
        return false;
    }

    private boolean hasRight(long cellID){
        if((cellID +1) < (cellsInXAxis*cellsInYAxis) && ((cellID +1)%cellsInXAxis) !=0){
            return true;
        }
        return false;
    }

    private long getCellId(double x, double y) {

        long xc = (long) ((x-rectangle.getLowerBound().getX()) / this.x);
        long yc = (long) ((y-rectangle.getLowerBound().getY()) / this.y);

        return (xc + (yc * cellsInXAxis));
    }

    private Rectangle getRectangle(long cellId){

        long yc = cellId/cellsInXAxis;
        long xc = cellId - (yc * cellsInXAxis);

        Point lowerBound = Point.newPoint(rectangle.getLowerBound().getX() + (xc * x), rectangle.getLowerBound().getY() + (yc * y));
        Point upperBound = Point.newPoint(rectangle.getLowerBound().getX() + (((xc+1) * x)), rectangle.getLowerBound().getY() + (((yc+1) * y)));

        return Rectangle.newRectangle(lowerBound, upperBound);
    }

    public String[] getPartitionsAType(double x, double y){
        return getPartitions(x, y, TypeSet.A);
    }

    public String[] getPartitionsBType(double x, double y){
        return getPartitions(x, y, TypeSet.B);
    }


    private String[] getPartitions(double x, double y, TypeSet typeSet){
        long cellId = getCellId(x,y);
        if (typeSet.equals(TypeSet.B)) {
            return new String[]{String.valueOf(cellId)};
        }

        List<Long> cells = new ArrayList<>();
        Rectangle rectangle = getRectangle(getCellId(x,y));

        cells.add(cellId);

        if(hasBottom(cellId) && hasLeft(cellId) && isInBottomLeftQuarterArea(rectangle,x,y,r)){
            cells.add(cellId - cellsInXAxis - 1);
        }

        if(hasBottom(cellId) && isInBottomSpecialArea(rectangle,y,r)){
            cells.add(cellId - cellsInXAxis);
        }

        if(hasBottom(cellId) && hasRight(cellId) && isInBottomRightQuarterArea(rectangle,x,y,r)){
            cells.add(cellId - cellsInXAxis +1);
        }

        if(hasRight(cellId) && isInRightSpecialArea(rectangle,x,r)){
            cells.add(cellId +1);
        }

        if(hasRight(cellId) && hasTop(cellId) && isInTopRightQuarterArea(rectangle,x,y,r)){
            cells.add(cellId + cellsInXAxis +1);
        }

        if(hasTop(cellId) && isInTopSpecialArea(rectangle,y,r)){
            cells.add(cellId + cellsInXAxis);
        }

        if(hasTop(cellId) && hasLeft(cellId) && isInTopLeftQuarterArea(rectangle,x,y,r)){
            cells.add(cellId + cellsInXAxis - 1);
        }

        if(hasLeft(cellId) && isInLeftSpecialArea(rectangle, x,r)){
            cells.add(cellId - 1);
        }

        return cells.stream().map(l -> String.valueOf(l.longValue())).collect(Collectors.toList()).toArray(new String[0]);
    }

    private boolean isInBottomSpecialArea(Rectangle rectangle, double y, double r){
        if(Double.compare(y, rectangle.getLowerBound().getY() + r) == -1){
            return true;
        }
        return false;
    }

    private boolean isInRightSpecialArea(Rectangle rectangle, double x, double r){
        if(Double.compare(x, rectangle.getUpperBound().getX() - r) != -1){
            return true;
        }
        return false;
    }

    private boolean isInTopSpecialArea(Rectangle rectangle, double y, double r){
        if(Double.compare(y, rectangle.getUpperBound().getY() - r) != -1){
            return true;
        }
        return false;
    }

    private boolean isInLeftSpecialArea(Rectangle rectangle, double x, double r){
        if(Double.compare(x, rectangle.getLowerBound().getX() + r) == -1){
            return true;
        }
        return false;
    }

    private boolean isInBottomRightQuarterArea(Rectangle rectangle, double x, double y, double r){
        if(Circle.compare(rectangle.getUpperBound().getX(),rectangle.getLowerBound().getY(),r,x,y) == -1){
            return true;
        }
        return false;
    }

    private boolean isInTopRightQuarterArea(Rectangle rectangle, double x, double y, double r){
        if(Circle.compare(rectangle.getUpperBound().getX(),rectangle.getUpperBound().getY(),r,x,y) != 1){
            return true;
        }
        return false;
    }

    private boolean isInTopLeftQuarterArea(Rectangle rectangle, double x, double y, double r){
        if(Circle.compare(rectangle.getLowerBound().getX(),rectangle.getUpperBound().getY(),r,x,y) == -1){
            return true;
        }
        return false;
    }

    private boolean isInBottomLeftQuarterArea(Rectangle rectangle, double x, double y, double r){
        if(Circle.compare(rectangle.getLowerBound().getX(),rectangle.getLowerBound().getY(),r,x,y) == -1){
            return true;
        }
        return false;
    }

    public long getCellsInXAxis(){
        return cellsInXAxis;
    }

    public long getCellsInYAxis(){
        return cellsInYAxis;
    }

}
