package gr.ds.unipi.self.processing;

import gr.ds.unipi.processing.Function3;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;

import java.util.*;

public class SelfGrid {

    private final Rectangle rectangle;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private final double x;
    private final double y;

    private HashMap<Integer, Cell> cells;

    private final double r;
    private Function3<Cell, Cell, Boolean> function;

    private SelfGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/Function3<Cell, Cell, Boolean> function, boolean geoData){
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
        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX()) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY()) / cellsInYAxis;
        System.out.println("x: "+x +" y:"+y);

        if(!geoData) {
            if (r >= x / 2 || r >= y / 2) {
                try {
                    throw new Exception("Radius is larger than the half length of the side of the cells");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        cells = new HashMap<>();

        this.function = function;

    }

    public void printInfo(){
        System.out.println("Number of cells: "+cells.size());
    }

    public double getR() {
        return r;
    }

        private Rectangle getRectangle(int cellId){

        int yc = cellId/cellsInXAxis;
        int xc = cellId - (yc * cellsInXAxis);

        Point lowerBound = Point.newPoint(rectangle.getLowerBound().getX() + (xc * x), rectangle.getLowerBound().getY() + (yc * y));
//        Point upperBound = Point.newPoint(rectangle.getLowerBound().getX() + ((xc * x) + x), rectangle.getLowerBound().getY() + ((yc * y) + y));
        Point upperBound = Point.newPoint(rectangle.getLowerBound().getX() + (((xc+1) * x)), rectangle.getLowerBound().getY() + (((yc+1) * y)));

        return Rectangle.newRectangle(lowerBound, upperBound);
    }

    public int getCellId(double x, double y) {

        int xc = (int) ((x-rectangle.getLowerBound().getX()) / this.x);
        int yc = (int) ((y-rectangle.getLowerBound().getY()) / this.y);

        return (xc + (yc * cellsInXAxis));
    }

    private Cell getCell(int cellID){
        return cells.getOrDefault(cellID, Cell.newCell(getRectangle(cellID)));
    }

    private boolean hasTop(int cellID){
        if((cellID + cellsInXAxis)< (cellsInXAxis*cellsInYAxis)){
            return true;
        }
        return false;
    }

    private boolean hasBottom(int cellID){
        if((cellID - cellsInXAxis)>= 0){
            return true;
        }
        return false;

    }

    private boolean hasLeft(int cellID){
        if(((cellID -1) >= 0) && ((cellID - 1)%(cellsInXAxis)) != (cellsInXAxis-1)){
            return true;
        }
        return false;
    }

    private boolean hasRight(int cellID){
        if((cellID +1) < (cellsInXAxis*cellsInYAxis) && ((cellID +1)%cellsInXAxis) !=0){
            return true;
        }
        return false;
    }

    public void addPointDataset(double x, double y){
        Cell cell = cells.computeIfAbsent(getCellId(x, y), cellID -> Cell.newCell(getRectangle(cellID))) ;
        cell.addPointToDataset(cell.getPosition(x,y,r));
    }

    public static SelfGrid newGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/  Function3<Cell, Cell, Boolean> function){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new SelfGrid(rectangle, cellsInXAxis, cellsInYAxis, r, function, false);
    }

    public static SelfGrid newGrid(Rectangle rectangle, double r, Function3<Cell, Cell, Boolean> function){
        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(2*r));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }
        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(2*r));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new SelfGrid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static SelfGrid newGridStripesX(Rectangle rectangle, double r, Function3<Cell, Cell, Boolean> function){
        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(2*r));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }
        double cellsInYAxis = 1;
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new SelfGrid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static SelfGrid newGridStripesY(Rectangle rectangle, double r, Function3<Cell, Cell, Boolean> function){
        double cellsInXAxis = 1;
        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(2*r));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new SelfGrid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static SelfGrid newGeoGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/Function3<Cell, Cell, Boolean> function){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new SelfGrid(rectangle, cellsInXAxis, cellsInYAxis, r, function, true);
    }

    public static SelfGrid newGeoGrid(Rectangle rectangle, double r, /*ReplicationType function*/Function3<Cell, Cell, Boolean> function){

        double northLength = Math.cos(Math.toRadians(rectangle.getUpperBound().getY())) *111.321;
        double southLength = Math.cos(Math.toRadians(rectangle.getLowerBound().getY())) *111.321;
        double i;
        System.out.println(northLength +" o "+southLength);

        if(northLength<southLength){
            i = northLength;
        }else{
            i = southLength;
        }

        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(2*(r/i)));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }

        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(2*(r/110.574)));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new SelfGrid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function ,true);
    }

    public long getCellsInXAxis(){
        return cellsInXAxis;
    }
    public long getCellsInYAxis(){
        return cellsInYAxis;
    }

    public HashMap<Integer,Integer> getCellsWithCosts2(){
        HashMap<Integer,Integer> cellsWithCosts = new HashMap<>();

        cells.forEach((cellId,cell)->{
            if(cell.getNumberOfPoints() != 0 && cell.getNumberOfPoints() != 0) {
                cellsWithCosts.put(cellId, cell.getNumberOfPoints() * cell.getNumberOfPoints());
            }
        });
        return cellsWithCosts;
    }

    public HashMap<Integer,Integer> getCellsWithCosts1(){
        long t1 = System.currentTimeMillis();
        HashMap<Integer, Integer> cellsWithCosts = new HashMap<>();

        Iterator o1 = cells.entrySet().iterator();
        while (o1.hasNext()) {
            Map.Entry<Integer, Cell> entry = (Map.Entry)o1.next();
            if(entry.getValue().getNumberOfPoints()==1){
                o1.remove();
            }
        }

        cells.forEach((cellId,cell)->{

            int externals = 0;

            if(hasBottom(cellId) && hasLeft(cellId) && cells.containsKey(cellId - cellsInXAxis - 1)){
                Cell externalCell = getCell(cellId - cellsInXAxis - 1);
                if(!function.apply(externalCell,cell)){
                    externals = externals + externalCell.getNumberOfPointsInTopRightQuarterArea();
                }
            }

            if(hasBottom(cellId)&& cells.containsKey(cellId - cellsInXAxis)){
                Cell externalCell =getCell(cellId - cellsInXAxis);
                if(!function.apply(externalCell,cell)) {
                    externals = externals + externalCell.getNumberOfPointsInTopSpecialArea();
                }
            }

            if(hasBottom(cellId) && hasRight(cellId)&& cells.containsKey(cellId - cellsInXAxis + 1)){
                Cell externalCell =getCell(cellId - cellsInXAxis + 1);
                if(!function.apply(externalCell,cell)) {
                    externals = externals + externalCell.getNumberOfPointsInTopLeftQuarterArea();
                }
            }

            if(hasRight(cellId)&& cells.containsKey(cellId + 1)){
                Cell externalCell =getCell(cellId + 1);
                if(function.apply(cell,externalCell)) {
                    externals = externals + externalCell.getNumberOfPointsInLeftSpecialArea();
                }
            }

            if(hasRight(cellId) && hasTop(cellId)&& cells.containsKey(cellId + cellsInXAxis + 1)){
                Cell externalCell =getCell(cellId + cellsInXAxis + 1);
                if(function.apply(cell,externalCell)) {
                    externals = externals + externalCell.getNumberOfPointsInBottomLeftQuarterArea();
                }
            }

            if(hasTop(cellId)&& cells.containsKey(cellId + cellsInXAxis)){
                Cell externalCell =getCell(cellId + cellsInXAxis);
                if(function.apply(cell,externalCell)) {
                    externals = externals + externalCell.getNumberOfPointsInBottomSpecialArea();
                }
            }

            if(hasTop(cellId) && hasLeft(cellId)&& cells.containsKey(cellId + cellsInXAxis - 1)){
                Cell externalCell =getCell(cellId + cellsInXAxis - 1);
                if(function.apply(cell,externalCell)) {
                    externals = externals + externalCell.getNumberOfPointsInBottomRightQuarterArea();
                }
            }

            if(hasLeft(cellId)&& cells.containsKey(cellId - 1)){
                Cell externalCell =getCell(cellId - 1);
                if(!function.apply(externalCell,cell)) {
                    externals = externals + externalCell.getNumberOfPointsInRightSpecialArea();
                }
            }
//            int cost = (cell.getNumberOfPoints() * cell.getNumberOfPoints()) + (cell.getNumberOfPoints() * externals);
            int cost = ((cell.getNumberOfPoints())*((cell.getNumberOfPoints())-1)/2) + (cell.getNumberOfPoints()*externals);
            if(cost>0) {
                cellsWithCosts.put(cellId, cost);
            }
        });

        System.out.println("Cells size is"+cells.size());
        System.out.println("Time for agreements: "+(System.currentTimeMillis()-t1)/1000);
        return cellsWithCosts;
    }

    public int[] getPartitions(double x, double y){
        int cellId = getCellId(x,y);
        Cell nativeCell = getCell(cellId);

        List<Integer> cells = new ArrayList<>();

        cells.add(cellId);

        switch (nativeCell.getPosition(x,y,r)){
            case TOPRIGHTQUARTER:
                if (hasRight(cellId) && function.apply(nativeCell,getCell(cellId + 1))) {
                    cells.add(cellId + 1);
                }
                if (hasTop(cellId) && function.apply(nativeCell,getCell(cellId + cellsInXAxis))) {
                    cells.add(cellId + cellsInXAxis);
                }
                if (hasTop(cellId) && hasRight(cellId) && function.apply(nativeCell,getCell(cellId + cellsInXAxis + 1))) {
                    cells.add(cellId + cellsInXAxis + 1);
                }
                break;
            case TOPLEFTQUARTER:
                if (hasLeft(cellId) && !function.apply(getCell(cellId - 1),nativeCell)) {
                    cells.add(cellId - 1);
                }
                if (hasTop(cellId) && function.apply(nativeCell,getCell(cellId + cellsInXAxis))) {
                    cells.add(cellId + cellsInXAxis);
                }
                if (hasTop(cellId) && hasLeft(cellId) && function.apply(nativeCell,getCell(cellId + cellsInXAxis - 1))) {
                    cells.add(cellId + cellsInXAxis - 1);
                }
                break;
            case BOTTOMRIGHTQUARTER:
                if (hasBottom(cellId) && !function.apply(getCell(cellId - cellsInXAxis),nativeCell)) {
                    cells.add(cellId - cellsInXAxis);
                }
                if (hasRight(cellId) && function.apply(nativeCell,getCell(cellId + 1))) {
                    cells.add(cellId + 1);
                }
                if (hasBottom(cellId) && hasRight(cellId) && !function.apply(getCell(cellId - cellsInXAxis + 1),nativeCell)) {
                    cells.add(cellId - cellsInXAxis + 1);
                }
                break;
            case BOTTOMLEFTQUARTER:
                if (hasBottom(cellId) && !function.apply(getCell(cellId - cellsInXAxis),nativeCell)) {
                    cells.add(cellId - cellsInXAxis);
                }
                if (hasLeft(cellId) && !function.apply(getCell(cellId - 1),nativeCell)) {
                    cells.add(cellId - 1);
                }
                if (hasBottom(cellId) && hasLeft(cellId) && !function.apply(getCell(cellId - cellsInXAxis - 1),nativeCell)) {
                    cells.add(cellId - cellsInXAxis - 1);
                }
                break;
            case TOPRIGHT:
                if (hasRight(cellId) && function.apply(nativeCell,getCell(cellId + 1))) {
                    cells.add(cellId + 1);
                }
                if (hasTop(cellId) && function.apply(nativeCell,getCell(cellId + cellsInXAxis))) {
                    cells.add(cellId + cellsInXAxis);
                }
                break;
            case TOPLEFT:
                if (hasLeft(cellId) && !function.apply(getCell(cellId - 1),nativeCell)) {
                    cells.add(cellId - 1);
                }
                if (hasTop(cellId) && function.apply(nativeCell,getCell(cellId + cellsInXAxis))) {
                    cells.add(cellId + cellsInXAxis);
                }
                break;
            case BOTTOMRIGHT:
                if (hasBottom(cellId) && !function.apply(getCell(cellId - cellsInXAxis),nativeCell)) {
                    cells.add(cellId - cellsInXAxis);
                }
                if (hasRight(cellId) && function.apply(nativeCell,getCell(cellId + 1))) {
                    cells.add(cellId + 1);
                }
                break;
            case BOTTOMLEFT:
                if (hasBottom(cellId) && !function.apply(getCell(cellId - cellsInXAxis),nativeCell)) {
                    cells.add(cellId - cellsInXAxis);
                }
                if (hasLeft(cellId) && !function.apply(getCell(cellId - 1),nativeCell)) {
                    cells.add(cellId - 1);
                }
                break;
            case LEFT:
                if (hasLeft(cellId) && !function.apply(getCell(cellId - 1),nativeCell)) {
                    cells.add((cellId - 1));
                }
                break;
            case TOP:
                if (hasTop(cellId) && function.apply(nativeCell,getCell(cellId + cellsInXAxis))) {
                    cells.add(cellId + cellsInXAxis);
                }
                break;
            case RIGHT:
                if (hasRight(cellId) && function.apply(nativeCell,getCell(cellId + 1))) {
                    cells.add(cellId + 1);
                }
                break;
            case BOTTOM:
                if (hasBottom(cellId) && !function.apply(getCell(cellId - cellsInXAxis),nativeCell)) {
                    cells.add(cellId - cellsInXAxis);
                }
                break;
        }
        return listToArray(cells);
    }

    public int[] listToArray(List<Integer> list){
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}
