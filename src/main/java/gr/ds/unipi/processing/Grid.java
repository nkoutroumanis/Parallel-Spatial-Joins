package gr.ds.unipi.processing;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.shapes.Circle;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;

import java.util.*;


public class Grid {

    private final Rectangle rectangle;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private final double x;
    private final double y;

    private HashMap<Integer, Cell> cells;

    private final double r;
    private Function3<Cell, Cell, TypeSet> function;

    private Grid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/Function3<Cell, Cell, TypeSet> function, boolean geoData){
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

//    private int getCellId(Point point) {
//        return getCellId(point.getX(), point.getY());
//    }

//    private Cell getCell(double x, double y){
//        return getCell(getCellId(x, y));
//    }

    private Cell getCell(int cellID){
        return cells.getOrDefault(cellID, Cell.newCell(getRectangle(cellID)));
//        if(cells.containsKey(cellID)){
//            return cells.get(cellID);
//        }else{
//            return Cell.newCell(getRectangle(cellID));
//        }
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

    public void addPointDatasetA(double x, double y){
        addPointDataset(x, y, TypeSet.A);
    }

    public void addPointDatasetB(double x, double y){
        addPointDataset(x, y, TypeSet.B);
    }

    private void addPointDataset(double x, double y, TypeSet typeSet){
        Cell cell = cells.computeIfAbsent(getCellId(x, y), cellID -> Cell.newCell(getRectangle(cellID))) ;
        cell.addPointToDataset(typeSet, cell.getPosition(x,y,r));
    }

    public int[] getPartitionsAType(double x, double y){
        return getPartitions(x, y, TypeSet.A);
    }

    public int[] getPartitionsBType(double x, double y){
        return getPartitions(x, y, TypeSet.B);
    }

    public static Grid newGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/  Function3<Cell, Cell, TypeSet> function){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new Grid(rectangle, cellsInXAxis, cellsInYAxis, r, function, false);
    }

    public static Grid newGrid(Rectangle rectangle, double r, Function3<Cell, Cell, TypeSet> function){
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

        return new Grid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static Grid newGridStripesX(Rectangle rectangle, double r, Function3<Cell, Cell, TypeSet> function){
        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(2*r));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }
        double cellsInYAxis = 1;
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new Grid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static Grid newGridStripesY(Rectangle rectangle, double r, Function3<Cell, Cell, TypeSet> function){
        double cellsInXAxis = 1;
        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(2*r));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new Grid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static Grid newGeoGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/Function3<Cell, Cell, TypeSet> function){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new Grid(rectangle, cellsInXAxis, cellsInYAxis, r, function, true);
    }

    public static Grid newGeoGrid(Rectangle rectangle, double r, /*ReplicationType function*/Function3<Cell, Cell, TypeSet> function){

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

        return new Grid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function ,true);
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
            if(cell.getNumberOfPointsAType() != 0 && cell.getNumberOfPointsBType() != 0) {
                cellsWithCosts.put(cellId, cell.getNumberOfPointsAType() * cell.getNumberOfPointsBType());
            }
        });
        return cellsWithCosts;
    }

    public HashMap<Integer,Integer> getCellsWithCosts1(){
        long t1 = System.currentTimeMillis();
        HashMap<Integer, Integer> cellsWithCosts = new HashMap<>();

        cells.forEach((cellId,cell)->{

            int externalsA = 0;
            int externalsB = 0;

            if(hasBottom(cellId) && hasLeft(cellId) && cells.containsKey(cellId - cellsInXAxis - 1)){
                Cell externalCell = getCell(cellId - cellsInXAxis - 1);
                if(function.apply(externalCell,cell)==TypeSet.A){
                    externalsA = externalsA + externalCell.getNumberOfPointsAInTopRightQuarterArea();
                }else{
                    externalsB = externalsB + externalCell.getNumberOfPointsBInTopRightQuarterArea();
                }
            }

            if(hasBottom(cellId)&& cells.containsKey(cellId - cellsInXAxis)){
                Cell externalCell =getCell(cellId - cellsInXAxis);
                if(function.apply(externalCell,cell)==TypeSet.A) {
                    externalsA = externalsA + externalCell.getNumberOfPointsAInTopSpecialArea();
                }else{
                    externalsB = externalsB + externalCell.getNumberOfPointsBInTopSpecialArea();
                }
            }

            if(hasBottom(cellId) && hasRight(cellId)&& cells.containsKey(cellId - cellsInXAxis + 1)){
                Cell externalCell =getCell(cellId - cellsInXAxis + 1);
                if(function.apply(externalCell,cell)==TypeSet.A) {
                    externalsA = externalsA + externalCell.getNumberOfPointsAInTopLeftQuarterArea();
                }else{
                    externalsB = externalsB + externalCell.getNumberOfPointsBInTopLeftQuarterArea();
                }
            }

            if(hasRight(cellId)&& cells.containsKey(cellId + 1)){
                Cell externalCell =getCell(cellId + 1);
                if(function.apply(cell,externalCell)==TypeSet.A) {
                    externalsA = externalsA + externalCell.getNumberOfPointsAInLeftSpecialArea();
                }else{
                    externalsB = externalsB + externalCell.getNumberOfPointsBInLeftSpecialArea();
                }
            }

            if(hasRight(cellId) && hasTop(cellId)&& cells.containsKey(cellId + cellsInXAxis + 1)){
                Cell externalCell =getCell(cellId + cellsInXAxis + 1);
                if(function.apply(cell,externalCell)==TypeSet.A) {
                    externalsA = externalsA + externalCell.getNumberOfPointsAInBottomLeftQuarterArea();
                }else{
                    externalsB = externalsB + externalCell.getNumberOfPointsBInBottomLeftQuarterArea();
                }
            }

            if(hasTop(cellId)&& cells.containsKey(cellId + cellsInXAxis)){
                Cell externalCell =getCell(cellId + cellsInXAxis);
                if(function.apply(cell,externalCell)==TypeSet.A) {
                    externalsA = externalsA + externalCell.getNumberOfPointsAInBottomSpecialArea();
                }else{
                    externalsB = externalsB + externalCell.getNumberOfPointsBInBottomSpecialArea();
                }
            }

            if(hasTop(cellId) && hasLeft(cellId)&& cells.containsKey(cellId + cellsInXAxis - 1)){
                Cell externalCell =getCell(cellId + cellsInXAxis - 1);
                if(function.apply(cell,externalCell)==TypeSet.A) {
                    externalsA = externalsA + externalCell.getNumberOfPointsAInBottomRightQuarterArea();
                }else{
                    externalsB = externalsB + externalCell.getNumberOfPointsBInBottomRightQuarterArea();
                }
            }

            if(hasLeft(cellId)&& cells.containsKey(cellId - 1)){
                Cell externalCell =getCell(cellId - 1);
                if(function.apply(externalCell,cell)==TypeSet.A) {
                    externalsA = externalsA + externalCell.getNumberOfPointsAInRightSpecialArea();
                }else{
                    externalsB = externalsB + externalCell.getNumberOfPointsBInRightSpecialArea();
                }
            }
            int cost = (cell.getNumberOfPointsAType() * cell.getNumberOfPointsBType()) + (cell.getNumberOfPointsAType() * externalsB) + (cell.getNumberOfPointsBType() * externalsA);
            if(cost>0) {
                cellsWithCosts.put(cellId, cost);
            }
        });

        System.out.println("Cells size is"+cells.size());
        System.out.println("Time for agreements: "+(System.currentTimeMillis()-t1)/1000);
        return cellsWithCosts;
    }

    private int[] getPartitions(double x, double y, TypeSet typeSet){
        int cellId = getCellId(x,y);
        Cell nativeCell = getCell(cellId);

        List<Integer> cells = new ArrayList<>();
//        Rectangle rectangle = nativeCell.getRectangle();// getRectangle(getCellId(x,y));

        cells.add(cellId);

        switch (nativeCell.getPosition(x,y,r)){
            case TOPRIGHTQUARTER:
                if (hasRight(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + 1)))) {
                    cells.add(cellId + 1);
                }
                if (hasTop(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis)))) {
                    cells.add(cellId + cellsInXAxis);
                }
                if (hasTop(cellId) && hasRight(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis + 1)))) {
                    cells.add(cellId + cellsInXAxis + 1);
                }
                break;
            case TOPLEFTQUARTER:
                if (hasLeft(cellId) && typeSet.equals(function.apply(getCell(cellId - 1),nativeCell))) {
                    cells.add(cellId - 1);
                }
                if (hasTop(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis)))) {
                    cells.add(cellId + cellsInXAxis);
                }
                if (hasTop(cellId) && hasLeft(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis - 1)))) {
                    cells.add(cellId + cellsInXAxis - 1);
                }
                break;
            case BOTTOMRIGHTQUARTER:
                if (hasBottom(cellId) && typeSet.equals(function.apply(getCell(cellId - cellsInXAxis),nativeCell))) {
                    cells.add(cellId - cellsInXAxis);
                }
                if (hasRight(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + 1)))) {
                    cells.add(cellId + 1);
                }
                if (hasBottom(cellId) && hasRight(cellId) && typeSet.equals(function.apply(getCell(cellId - cellsInXAxis + 1),nativeCell))) {
                    cells.add(cellId - cellsInXAxis + 1);
                }
                break;
            case BOTTOMLEFTQUARTER:
                if (hasBottom(cellId) && typeSet.equals(function.apply(getCell(cellId - cellsInXAxis),nativeCell))) {
                    cells.add(cellId - cellsInXAxis);
                }
                if (hasLeft(cellId) && typeSet.equals(function.apply(getCell(cellId - 1),nativeCell))) {
                    cells.add(cellId - 1);
                }
                if (hasBottom(cellId) && hasLeft(cellId) && typeSet.equals(function.apply(getCell(cellId - cellsInXAxis - 1),nativeCell))) {
                    cells.add(cellId - cellsInXAxis - 1);
                }
                break;
            case TOPRIGHT:
                if (hasRight(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + 1)))) {
                    cells.add(cellId + 1);
                }
                if (hasTop(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis)))) {
                    cells.add(cellId + cellsInXAxis);
                }
                break;
            case TOPLEFT:
                if (hasLeft(cellId) && typeSet.equals(function.apply(getCell(cellId - 1),nativeCell))) {
                    cells.add(cellId - 1);
                }
                if (hasTop(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis)))) {
                    cells.add(cellId + cellsInXAxis);
                }
                break;
            case BOTTOMRIGHT:
                if (hasBottom(cellId) && typeSet.equals(function.apply(getCell(cellId - cellsInXAxis),nativeCell))) {
                    cells.add(cellId - cellsInXAxis);
                }
                if (hasRight(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + 1)))) {
                    cells.add(cellId + 1);
                }
                break;
            case BOTTOMLEFT:
                if (hasBottom(cellId) && typeSet.equals(function.apply(getCell(cellId - cellsInXAxis),nativeCell))) {
                    cells.add(cellId - cellsInXAxis);
                }
                if (hasLeft(cellId) && typeSet.equals(function.apply(getCell(cellId - 1),nativeCell))) {
                    cells.add(cellId - 1);
                }
                break;
            case LEFT:
                if (hasLeft(cellId) && typeSet.equals(function.apply(getCell(cellId - 1),nativeCell))) {
                    cells.add((cellId - 1));
                }
                break;
            case TOP:
                if (hasTop(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis)))) {
                    cells.add(cellId + cellsInXAxis);
                }
                break;
            case RIGHT:
                if (hasRight(cellId) && typeSet.equals(function.apply(nativeCell,getCell(cellId + 1)))) {
                    cells.add(cellId + 1);
                }
                break;
            case BOTTOM:
                if (hasBottom(cellId) && typeSet.equals(function.apply(getCell(cellId - cellsInXAxis),nativeCell))) {
                    cells.add(cellId - cellsInXAxis);
                }
                break;
        }

//        if(hasBottom(cellId) && hasLeft(cellId) && isInBottomLeftQuarterArea(rectangle,x,y,r)){
//            if(typeSet.equals(function.apply(getCell(cellId - cellsInXAxis - 1),nativeCell))){
//                cells.add(cellId - cellsInXAxis - 1);
//            }
//        }
//
//        if(hasBottom(cellId) && isInBottomSpecialArea(rectangle,y,r)){
//            if(typeSet.equals(function.apply(getCell(cellId - cellsInXAxis),nativeCell))) {
//                cells.add(cellId - cellsInXAxis);
//            }
//        }
//
//        if(hasBottom(cellId) && hasRight(cellId) && isInBottomRightQuarterArea(rectangle,x,y,r)){
//            if(typeSet.equals(function.apply(getCell(cellId - cellsInXAxis + 1),nativeCell))) {
//                cells.add(cellId - cellsInXAxis + 1);
//            }
//        }
//
//        if(hasRight(cellId) && isInRightSpecialArea(rectangle,x,r)){
//            if(typeSet.equals(function.apply(nativeCell,getCell(cellId + 1)))) {
//                cells.add(cellId + 1);
//            }
//        }
//
//        if(hasRight(cellId) && hasTop(cellId) && isInTopRightQuarterArea(rectangle,x,y,r)){
//            if(typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis + 1)))) {
//                cells.add(cellId + cellsInXAxis + 1);
//            }
//        }
//
//        if(hasTop(cellId) && isInTopSpecialArea(rectangle,y,r)){
//            if(typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis)))) {
//                cells.add(cellId + cellsInXAxis);
//            }
//        }
//
//        if(hasTop(cellId) && hasLeft(cellId) && isInTopLeftQuarterArea(rectangle,x,y,r)){
//            if(typeSet.equals(function.apply(nativeCell,getCell(cellId + cellsInXAxis - 1)))) {
//                cells.add(cellId + cellsInXAxis - 1);
//            }
//        }
//
//        if(hasLeft(cellId) && isInLeftSpecialArea(rectangle, x,r)){
//            if(typeSet.equals(function.apply(getCell(cellId - 1),nativeCell))) {
//                cells.add(cellId - 1);
//            }
//        }

        return listToArray(cells);//cells.stream().map(l -> String.valueOf(l.longValue())).collect(Collectors.toList()).toArray(new String[0]);
    }



    public int[] listToArray(List<Integer> list){
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}
