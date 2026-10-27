package gr.ds.unipi.self.adaptiveGrid;

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

    private AdaptiveGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, List<Point> dataset){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        this.r = r;

        List<Point> samples = new ArrayList<>(dataset);

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

        dataset.forEach(p->{
            Cell c = cells.computeIfAbsent(getCellId(p.getX(), p.getY()), cellID -> Cell.newCell());
            c.increaseCounterDataset();
        });

//        dataset.forEach(p->{ cells[findFloorIndexX(p.getX())][findFloorIndexY(p.getY())]++;});
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

    public static AdaptiveGrid newAdaptiveGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double radius, List<Point> dataset){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new AdaptiveGrid(rectangle, cellsInXAxis, cellsInYAxis, radius, dataset);
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

    public int[] getPartitions(double x, double y){
        List<Integer> ids = new ArrayList<>();
        int xc = getXStripeId(x);
        int yc = getYStripeId(y);

        int minX = getXStripeId(x-r);
        int maxX = getXStripeId(x+r);
        int minY = getYStripeId(y-r);
        int maxY = getYStripeId(y+r);

        ids.add(getCellIdFromXcYc(xc,yc));

        for(int i=minX; i<xc; i++){
            if(getTypeSet(xc,yc,i,yc)){
                ids.add(getCellIdFromXcYc(i,yc));
            }
        }

        for(int i=xc+1; i<=maxX; i++){
            if(getTypeSet(xc,yc,i,yc)){
                ids.add(getCellIdFromXcYc(i,yc));
            }
        }

        for(int i=minY; i<yc; i++){
            if(getTypeSet(xc,yc,xc,i)){
                ids.add(getCellIdFromXcYc(xc,i));
            }
        }

        for(int i=yc+1; i<=maxY; i++){
            if(getTypeSet(xc,yc,xc,i)){
                ids.add(getCellIdFromXcYc(xc,i));
            }
        }

        for(int i=minX; i<xc; i++){
            for(int j=minY; j<yc; j++){
                if(getTypeSet(xc,yc,i,j)){
                    if(Double.compare(distance(splitsX[i], splitsY[j], x, y), r)==-1){
                        ids.add(getCellIdFromXcYc(i,j));
                    }
                }
            }
        }

        for(int i=xc+1; i<=maxX; i++){
            for(int j=yc+1; j<=maxY; j++){
                if(getTypeSet(xc,yc,i,j)){
                    if(Double.compare(distance(splitsX[i-1], splitsY[j-1], x, y), r)!=1){
                        ids.add(getCellIdFromXcYc(i,j));
                    }
                }
            }
        }

        for(int i=minX; i<xc; i++){
            for(int j=yc+1; j<=maxY; j++){
                if(getTypeSet(xc,yc,i,j)){
                    if(Double.compare(distance(splitsX[i], splitsY[j-1], x, y), r)==-1){
                        ids.add(getCellIdFromXcYc(i,j));
                    }
                }
            }
        }


        for(int i=xc+1; i<=maxX; i++) {
            for(int j=minY; j<yc; j++){
                if(getTypeSet(xc,yc,i,j)){
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

    private boolean getTypeSet(int xc1, int yc1, int xc2, int yc2){

        Cell cell1 = cells.get(getCellIdFromXcYc(xc1,yc1));
        Cell cell2 = cells.get(getCellIdFromXcYc(xc2,yc2));

        int c1 = 0;
        int c2 = 0;

        if(cell1!=null){
            c1 = cell1.getDataset();
        }
        if(cell2!=null){
            c2 = cell2.getDataset();
        }

        if(c1<c2){
            return true;
        }
        if(c1==c2 && getCellIdFromXcYc(xc1,yc1)<getCellIdFromXcYc(xc2,yc2)){
            return true;
        }
        return false;
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
            int co =  cell.getDataset();
            if(co!=0){
                costs.put(c, co*co);
            }
        });
//        HashMap<Integer,Integer> costs = new HashMap<>(cells.length*cells[0].length);
//        for (int i = 0; i < cells.length; i++) {
//            for (int j = 0; j < cells[0].length; j++) {
//                int c =  cells[i][j]*cells[i][j];
//                if(c!=0){
//                    costs.put(getCellIdFromXcYc(i,j),c);
//                }
//            }
//        }
        return costs;
    }
}
