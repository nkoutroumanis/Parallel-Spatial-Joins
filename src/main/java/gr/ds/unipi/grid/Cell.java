package gr.ds.unipi.grid;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.shapes.Circle;
import gr.ds.unipi.shapes.Rectangle;
import scala.Function4;

import java.io.Serializable;
import java.util.*;

import static gr.ds.unipi.grid.Position.*;

public class Cell implements Space {

    private final Rectangle rectangle;

    private long totalNumberOfPointsAType;
    private long totalNumberOfPointsBType;

    //NEW ADDED
    private final long[] datasetASpecialAreas = {0,0,0,0};
    private final long[] datasetBSpecialAreas = {0,0,0,0};

    private final long[] datasetAQuarteredAreas = {0,0,0,0};
    private final long[] datasetBQuarteredAreas = {0,0,0,0};
    //bottom,right,top,left
    //NEW ADDED

    private Cell(Rectangle rectangle){
        this.rectangle = rectangle;
//        bottomLeft = rectangle.getLowerBound();
//        bottomRight = Point.newPoint(rectangle.getUpperBound().getX(),rectangle.getLowerBound().getY());
//        topLeft = Point.newPoint(rectangle.getLowerBound().getX(),rectangle.getUpperBound().getY());
//        topRight = rectangle.getUpperBound();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public static Cell newCell(Rectangle rectangle){
        return new Cell(rectangle);
    }

    public long getNumberOfPointsAType() {
        return totalNumberOfPointsAType;
    }

    public long getNumberOfPointsBType() {
        return totalNumberOfPointsBType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        return rectangle.equals(cell.rectangle);
    }

    @Override
    public int hashCode() {
        return rectangle.hashCode();
    }

    public void addPointToDataset(/*double x, double y, double r,*/ TypeSet dataset, Position position){
        if(dataset.equals(TypeSet.A)){
            totalNumberOfPointsAType++;
        }else{
            totalNumberOfPointsBType++;
        }
        switch(position){
            case TOPRIGHTQUARTER: case TOPRIGHT:
                addToTopSpecialArea(dataset);
                addToRightSpecialArea(dataset);
                addToTopRightQuarterArea(dataset);
                break;
            case TOPLEFTQUARTER: case TOPLEFT:
                addToTopSpecialArea(dataset);
                addToLeftSpecialArea(dataset);
                addToTopLeftQuarterArea(dataset);
                break;
            case BOTTOMRIGHTQUARTER: case BOTTOMRIGHT:
                addToBottomSpecialArea(dataset);
                addToRightSpecialArea(dataset);
                addToBottomRightQuarterArea(dataset);
                break;
            case BOTTOMLEFTQUARTER: case BOTTOMLEFT:
                addToBottomSpecialArea(dataset);
                addToLeftSpecialArea(dataset);
                addToBottomLeftQuarterArea(dataset);
                break;
            case TOP:
                addToTopSpecialArea(dataset);
                break;
            case BOTTOM:
                addToBottomSpecialArea(dataset);
                break;
            case RIGHT:
                addToRightSpecialArea(dataset);
                break;
            case LEFT:
                addToLeftSpecialArea(dataset);
                break;
            default:
                break;
        }

    }
    private void addToBottomSpecialArea(TypeSet dataset){
        if(dataset.equals(TypeSet.A)){
            datasetASpecialAreas[0] = ++datasetASpecialAreas[0];
        }else{
            datasetBSpecialAreas[0] = ++datasetBSpecialAreas[0];
        }
    }

    private void addToRightSpecialArea(TypeSet dataset){
        if(dataset.equals(TypeSet.A)){
            datasetASpecialAreas[1] = ++datasetASpecialAreas[1];
        }else{
            datasetBSpecialAreas[1] = ++datasetBSpecialAreas[1];
        }
    }

    private void addToTopSpecialArea(TypeSet dataset){
        if(dataset.equals(TypeSet.A)){
            datasetASpecialAreas[2] = ++datasetASpecialAreas[2];
        }else{
            datasetBSpecialAreas[2] = ++datasetBSpecialAreas[2];
        }
    }

    private void addToLeftSpecialArea(TypeSet dataset){
        if(dataset.equals(TypeSet.A)){
            datasetASpecialAreas[3] = ++datasetASpecialAreas[3];
        }else{
            datasetBSpecialAreas[3] = ++datasetBSpecialAreas[3];
        }
    }

    private void addToBottomRightQuarterArea(TypeSet dataset){
        if(dataset.equals(TypeSet.A)){
            datasetAQuarteredAreas[0] = ++datasetAQuarteredAreas[0];
        }else{
            datasetBQuarteredAreas[0] = ++datasetBQuarteredAreas[0];
        }
    }

    private void addToTopRightQuarterArea(TypeSet dataset){
        if(dataset.equals(TypeSet.A)){
            datasetAQuarteredAreas[1] = ++datasetAQuarteredAreas[1];
        }else{
            datasetBQuarteredAreas[1] = ++datasetBQuarteredAreas[1];
        }
    }

    private void addToTopLeftQuarterArea(TypeSet dataset){
        if(dataset.equals(TypeSet.A)){
            datasetAQuarteredAreas[2] = ++datasetAQuarteredAreas[2];

        }else{
            datasetBQuarteredAreas[2] = ++datasetBQuarteredAreas[2];
        }
    }

    private void addToBottomLeftQuarterArea(TypeSet dataset){
        if(dataset.equals(TypeSet.A)){
            datasetAQuarteredAreas[3] = ++datasetAQuarteredAreas[3];
        }else{
            datasetBQuarteredAreas[3] = ++datasetBQuarteredAreas[3];
        }
    }

    public long getNumberOfPointsAInBottomSpecialArea(){
            return datasetASpecialAreas[0];
    }

    public long getNumberOfPointsAInRightSpecialArea(){
            return datasetASpecialAreas[1];
    }

    public long getNumberOfPointsAInTopSpecialArea(){
            return datasetASpecialAreas[2];
    }

    public long getNumberOfPointsAInLeftSpecialArea(){
            return datasetASpecialAreas[3];
    }

    public long getNumberOfPointsAInBottomRightQuarterArea(){
            return datasetAQuarteredAreas[0];
    }

    public long getNumberOfPointsAInTopRightQuarterArea(){
            return datasetAQuarteredAreas[1];
    }

    public long getNumberOfPointsAInTopLeftQuarterArea(){
            return datasetAQuarteredAreas[2];
    }

    public long getNumberOfPointsAInBottomLeftQuarterArea(){
            return datasetAQuarteredAreas[3];
    }

    public long getNumberOfPointsBInBottomSpecialArea(){
            return datasetBSpecialAreas[0];
    }

    public long getNumberOfPointsBInRightSpecialArea(){
            return datasetBSpecialAreas[1];
    }

    public long getNumberOfPointsBInTopSpecialArea(){
            return datasetBSpecialAreas[2];
    }

    public long getNumberOfPointsBInLeftSpecialArea(){
            return datasetBSpecialAreas[3];
    }

    public long getNumberOfPointsBInBottomRightQuarterArea(){
            return datasetBQuarteredAreas[0];
    }

    public long getNumberOfPointsBInTopRightQuarterArea(){
            return datasetBQuarteredAreas[1];
    }

    public long getNumberOfPointsBInTopLeftQuarterArea(){
            return datasetBQuarteredAreas[2];
    }

    public long getNumberOfPointsBInBottomLeftQuarterArea(){
            return datasetBQuarteredAreas[3];
    }

    private boolean isInBottomSpecialArea(double y, double r){
        if(Double.compare(y, rectangle.getLowerBound().getY() + r) == -1){
            return true;
        }
        return false;
    }

    private boolean isInRightSpecialArea(double x, double r){
        if(Double.compare(x, rectangle.getUpperBound().getX() - r) != -1){
            return true;
        }
        return false;
    }

    private boolean isInTopSpecialArea(double y, double r){
        if(Double.compare(y, rectangle.getUpperBound().getY() - r) != -1){
            return true;
        }
        return false;
    }

    private boolean isInLeftSpecialArea(double x, double r){
        if(Double.compare(x, rectangle.getLowerBound().getX() + r) == -1){
            return true;
        }
        return false;
    }

    private boolean isInBottomRightQuarterArea(double x, double y, double r){
        if(Circle.compare(rectangle.getUpperBound().getX(),rectangle.getLowerBound().getY(),r,x,y) == -1){
            return true;
        }
        return false;
    }

    private boolean isInTopRightQuarterArea(double x, double y, double r){
        if(Circle.compare(rectangle.getUpperBound().getX(),rectangle.getUpperBound().getY(),r,x,y) != 1){
            return true;
        }
        return false;
    }

    private boolean isInTopLeftQuarterArea(double x, double y, double r){
        if(Circle.compare(rectangle.getLowerBound().getX(),rectangle.getUpperBound().getY(),r,x,y) == -1){
            return true;
        }
        return false;
    }

    private boolean isInBottomLeftQuarterArea(double x, double y, double r){
        if(Circle.compare(rectangle.getLowerBound().getX(),rectangle.getLowerBound().getY(),r,x,y) == -1){
            return true;
        }
        return false;
    }

    public Position getPosition(double x, double y, double r){
        if(isInBottomSpecialArea(y, r)){
            if(isInRightSpecialArea(x, r)){
                if(isInBottomRightQuarterArea(x,y, r)){
                    return BOTTOMRIGHTQUARTER;
                }
                return BOTTOMRIGHT;
            }else if(isInLeftSpecialArea(x, r)){
                if(isInBottomLeftQuarterArea(x,y, r)){
                    return BOTTOMLEFTQUARTER;
                }
                return BOTTOMLEFT;
            }
            return BOTTOM;
        } else if(isInTopSpecialArea(y, r)){
            if(isInRightSpecialArea(x, r)){
                if(isInTopRightQuarterArea(x,y, r)){
                    return TOPRIGHTQUARTER;
                }
                return TOPRIGHT;
            }else if(isInLeftSpecialArea(x, r)){
                if(isInTopLeftQuarterArea(x,y, r)){
                    return TOPLEFTQUARTER;
                }
                return TOPLEFT;
            }
            return TOP;
        }
        else if(isInRightSpecialArea(x, r)){
            return RIGHT;
        }
        else if(isInLeftSpecialArea(x, r)){
            return LEFT;
        }else{
            return PLAIN;
        }
    }

    private boolean isInBottomSpecialGeoArea(double x, double y, double r){
        if(Double.compare(haversineDistanceKm(x,y,x,rectangle.getLowerBound().getY()), r) == -1){
            return true;
        }
        return false;
    }

    private boolean isInRightSpecialGeoArea(double x, double y, double r){
        if(Double.compare(haversineDistanceKm(x,y,rectangle.getUpperBound().getX(),y), r) != 1){
            return true;
        }
        return false;
    }

    private boolean isInTopSpecialGeoArea(double x, double y, double r){
        if(Double.compare(haversineDistanceKm(x,y,x,rectangle.getUpperBound().getY()), r) != 1){
            return true;
        }
        return false;
    }

    private boolean isInLeftSpecialGeoArea(double x, double y, double r){
        if(Double.compare(haversineDistanceKm(x,y,rectangle.getLowerBound().getX(),y), r) == -1){
            return true;
        }
        return false;
    }

    private boolean isInBottomRightQuarterGeoArea(double x, double y, double r){
        if(Double.compare(haversineDistanceKm(rectangle.getUpperBound().getX(),rectangle.getLowerBound().getY(),x,y),r) == -1){
            return true;
        }
        return false;
    }

    private boolean isInTopRightQuarterGeoArea(double x, double y, double r){
        if(Double.compare(haversineDistanceKm(rectangle.getUpperBound().getX(),rectangle.getUpperBound().getY(),x,y),r) != 1){
            return true;
        }
        return false;
    }

    private boolean isInTopLeftQuarterGeoArea(double x, double y, double r){
        if(Double.compare(haversineDistanceKm(rectangle.getLowerBound().getX(),rectangle.getUpperBound().getY(),x,y),r) == -1){
            return true;
        }
        return false;
    }

    private boolean isInBottomLeftQuarterGeoArea(double x, double y, double r){
        if(Double.compare(haversineDistanceKm(rectangle.getLowerBound().getX(),rectangle.getLowerBound().getY(),x,y),r) == -1){
            return true;
        }
        return false;
    }

    public Position getGeoPosition(double x, double y, double r){
        if(isInBottomSpecialGeoArea(x, y, r)){
            if(isInRightSpecialGeoArea(x, y, r)){
                if(isInBottomRightQuarterGeoArea(x,y, r)){
                    return BOTTOMRIGHTQUARTER;
                }
                return BOTTOMRIGHT;
            }else if(isInLeftSpecialGeoArea(x, y, r)){
                if(isInBottomLeftQuarterGeoArea(x,y, r)){
                    return BOTTOMLEFTQUARTER;
                }
                return BOTTOMLEFT;
            }
            return BOTTOM;
        } else if(isInTopSpecialGeoArea(x, y, r)){
            if(isInRightSpecialGeoArea(x, y, r)){
                if(isInTopRightQuarterGeoArea(x,y, r)){
                    return TOPRIGHTQUARTER;
                }
                return TOPRIGHT;
            }else if(isInLeftSpecialGeoArea(x, y, r)){
                if(isInTopLeftQuarterGeoArea(x,y, r)){
                    return TOPLEFTQUARTER;
                }
                return TOPLEFT;
            }
            return TOP;
        }
        else if(isInRightSpecialGeoArea(x, y, r)){
            return RIGHT;
        }
        else if(isInLeftSpecialGeoArea(x, y, r)){
            return LEFT;
        }else{
            return PLAIN;
        }
    }

      private HashMap<Integer, TypeSet> neighbors = new HashMap<>();
//    private long extraNumberOfPointsAType = 0;
//    private long extraNumberOfPointsBType = 0;

//    public long getExtraNumberOfPointsAType(){
//        return extraNumberOfPointsAType;
//    }
//
//    public long getExtraNumberOfPointsBType(){
//        return extraNumberOfPointsBType;
//    }

    public boolean containsNeighboor(int cellHash){
        return neighbors.containsKey(cellHash);
    }

    public void addNeighboor(int cellHash, TypeSet typeset){
        neighbors.put(cellHash, typeset);
    }

    public TypeSet getNeighborTypeSet(int cellHash){
        return neighbors.get(cellHash);
    }

    public void addExtraNumberOfPointsAType(long numberOfPoints){
        totalNumberOfPointsAType = totalNumberOfPointsAType + numberOfPoints;
    }

    public void addExtraNumberOfPointsBType(long numberOfPoints){
        totalNumberOfPointsBType = totalNumberOfPointsBType + numberOfPoints;
    }

    public static double haversineDistanceKm(double lon1, double lat1, double lon2, double lat2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }
}
