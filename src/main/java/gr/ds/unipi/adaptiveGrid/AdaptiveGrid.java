package gr.ds.unipi.adaptiveGrid;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AdaptiveGrid {

    private final Rectangle rectangle;
    private final double[] splitsX;
    private final double[] splitsY;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

//    private final Cell[][] cells;
    private final HashMap<Integer, Cell> cells;

    private final double r;

    private AdaptiveGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, List<Point> datasetA, List<Point> datasetB){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        this.r = r;

        List<Point> samples = new ArrayList<>(datasetA);
        samples.addAll(datasetB);

        splitsX = new double[cellsInXAxis-1];
        splitsY = new double[cellsInYAxis-1];

        samples.sort(Comparator.comparingDouble(Point::getX));
        for (int k = 1; k < cellsInXAxis; k++) {
            splitsX[k-1] = samples.get((int) Math.floor(k * ((samples.size() - 1) / (double) cellsInXAxis))).getX();
        }

        samples.sort(Comparator.comparingDouble(Point::getY));
        for (int k = 1; k < cellsInYAxis; k++) {
            splitsY[k-1] = samples.get((int) Math.floor(k * ((samples.size() - 1) / (double) cellsInYAxis))).getY();
        }

//        cells = new Cell[cellsInXAxis][cellsInYAxis];
        cells = new HashMap<>();

//        for (int i = 0; i < cells.length; i++) {
//            for (int j = 0; j < cells[0].length; j++) {
//                cells[i][j]= Cell.newCell();
//            }
//        }

        datasetA.forEach(p->{
            Cell c = cells.computeIfAbsent(getCellId(p.getX(), p.getY()), cellID -> Cell.newCell());
            c.increaseCounterDatasetA();
        });
        datasetB.forEach(p->{
            Cell c = cells.computeIfAbsent(getCellId(p.getX(), p.getY()), cellID -> Cell.newCell());
            c.increaseCounterDatasetB();
        });
//        datasetA.forEach(p->{
//            cells[findFloorIndexX(p.getX())][findFloorIndexY(p.getY())].increaseCounterDatasetA();
//        });
//        datasetB.forEach(p->{
//            cells[findFloorIndexX(p.getX())][findFloorIndexY(p.getY())].increaseCounterDatasetB();
//        });

    }

    public int getCellId(double x, double y) {
        int xc = findFloorIndexX(x);
        int yc = findFloorIndexY(y);

        return (xc + (yc * cellsInXAxis));
    }
    public int findFloorIndexX(double target) {
        int left = 0, right = splitsX.length - 1;
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (splitsX[mid] <= target) {
                result = mid+1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    public int findFloorIndexY(double target) {
        int left = 0, right = splitsY.length - 1;
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (splitsY[mid] <= target) {
                result = mid+1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    public static AdaptiveGrid newAdaptiveGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double radius, List<Point> datasetA, List<Point> datasetB){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new AdaptiveGrid(rectangle, cellsInXAxis, cellsInYAxis, radius, datasetA, datasetB);
    }

    public int getCellsInXAxis(){
        return cellsInXAxis;
    }
    public int getCellsInYAxis(){
        return cellsInYAxis;
    }

    public Rectangle getRectangle() {return rectangle;}

    public int getXStripeId(double x){
        return findFloorIndexX(x);
    }

    public int getYStripeId(double y){
        return findFloorIndexY(y);
    }

    public int getCellIdFromXcYc(int xc, int yc){
        return (xc + (yc * cellsInXAxis));
    }

//    public Cell[][] getCells() {
//        return cells;
//    }


    public int[] getPartitionsTypeA(double x, double y){
        return getPartitionsDataset(x,y, TypeSet.A);
    }

    public int[] getPartitionsTypeB(double x, double y){
        return getPartitionsDataset(x,y, TypeSet.B);
    }

    private int[] getPartitionsDataset(double x, double y, TypeSet typeSet){
        List<Integer> ids = new ArrayList<>();
        int xc = getXStripeId(x);
        int yc = getYStripeId(y);

        int minX = getXStripeId(x-r);
        int maxX = getXStripeId(x+r);
        int minY = getYStripeId(y-r);
        int maxY = getYStripeId(y+r);

        ids.add(getCellIdFromXcYc(xc,yc));

        for(int i=minX; i<xc; i++){
            if(getTypeSet(xc,yc,i,yc).equals(typeSet)){
                ids.add(getCellIdFromXcYc(i,yc));
            }
        }

        for(int i=xc+1; i<=maxX; i++){
            if(getTypeSet(xc,yc,i,yc).equals(typeSet)){
                ids.add(getCellIdFromXcYc(i,yc));
            }
        }

        for(int i=minY; i<yc; i++){
            if(getTypeSet(xc,yc,xc,i).equals(typeSet)){
                ids.add(getCellIdFromXcYc(xc,i));
            }
        }

        for(int i=yc+1; i<=maxY; i++){
            if(getTypeSet(xc,yc,xc,i).equals(typeSet)){
                ids.add(getCellIdFromXcYc(xc,i));
            }
        }

        for(int i=minX; i<xc; i++){
            for(int j=minY; j<yc; j++){
                if(getTypeSet(xc,yc,i,j).equals(typeSet)){
                    if(Double.compare(distance(splitsX[i], splitsY[j], x, y), r)==-1){
                        ids.add(getCellIdFromXcYc(i,j));
                    }
                }
            }
        }

        for(int i=xc+1; i<=maxX; i++){
            for(int j=yc+1; j<=maxY; j++){
                if(getTypeSet(xc,yc,i,j).equals(typeSet)){
                    if(Double.compare(distance(splitsX[i-1], splitsY[j-1], x, y), r)!=1){
                        ids.add(getCellIdFromXcYc(i,j));
                    }
                }
            }
        }

        for(int i=minX; i<xc; i++){
            for(int j=yc+1; j<=maxY; j++){
                if(getTypeSet(xc,yc,i,j).equals(typeSet)){
                    if(Double.compare(distance(splitsX[i], splitsY[j-1], x, y), r)==-1){
                        ids.add(getCellIdFromXcYc(i,j));
                    }
                }
            }
        }


        for(int i=xc+1; i<=maxX; i++) {
            for(int j=minY; j<yc; j++){
                if(getTypeSet(xc,yc,i,j).equals(typeSet)){
                    if(Double.compare(distance(splitsX[i-1], splitsY[j], x, y), r)==-1){
                        ids.add(getCellIdFromXcYc(i,j));
                    }
                }
            }
        }

        return listToArray(ids);
    }

    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1-x2),2)+ Math.pow((y1-y2),2));
    }

    private TypeSet getTypeSet(int xc1, int yc1, int xc2, int yc2){

        int c1A = 0;
        int c1B = 0;
        int c2A = 0;
        int c2B = 0;

        Cell c1 = cells.get(getCellIdFromXcYc(xc1,yc1));
        Cell c2 = cells.get(getCellIdFromXcYc(xc2,yc2));

        if(c1!=null){
            c1A = c1.getDatasetA();
            c1B = c1.getDatasetB();
        }
        if(c2!=null){
            c2A = c2.getDatasetA();
            c2B = c2.getDatasetB();
        }

        if(c1A + c2A > c1B + c2B){
            return TypeSet.B;
        }
        return TypeSet.A;
//        if((cells[xc1][yc1].getDatasetA()+cells[xc2][yc2].getDatasetA())>(cells[xc1][yc1].getDatasetB()+cells[xc2][yc2].getDatasetB())){
//            return TypeSet.B;
//        }
//        return TypeSet.A;
    }

    public int[] listToArray(List<Integer> list){
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public HashMap<Integer,Integer> getCellsWithCosts(){
        HashMap<Integer,Integer> costs = new HashMap<>(cells.size());
        cells.forEach((c, cell)->{
            int co =  cell.getDatasetA()*cell.getDatasetB();
            if(co!=0){
                costs.put(c,co);
            }
        });
//        HashMap<Integer,Integer> costs = new HashMap<>(cells.length*cells[0].length);
//        for (int i = 0; i < cells.length; i++) {
//            for (int j = 0; j < cells[0].length; j++) {
//                int c =  cells[i][j].getDatasetA()*cells[i][j].getDatasetB();
//                if(c!=0){
//                    costs.put(getCellIdFromXcYc(i,j),c);
//                }
//            }
//        }
        return costs;
    }

}
