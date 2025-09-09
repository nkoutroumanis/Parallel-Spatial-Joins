package gr.ds.unipi.self.processing;

import gr.ds.unipi.self.agreements.Space;
import gr.ds.unipi.shapes.Circle;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.shapes.Rectangle;

import static gr.ds.unipi.shapes.Position.*;

public class Cell implements Space {

    private final Rectangle rectangle;

    private int totalNumberOfPointsType;

    //NEW ADDED
    private final int[] datasetSpecialAreas = {0,0,0,0};

    private final int[] datasetQuarteredAreas = {0,0,0,0};
    //bottom,right,top,left

    private Cell(Rectangle rectangle){
        this.rectangle = rectangle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public static Cell newCell(Rectangle rectangle){
        return new Cell(rectangle);
    }

    public int getNumberOfPoints() {
        return totalNumberOfPointsType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        return rectangle.getLowerBound().equals(cell.rectangle.getLowerBound());
    }

    @Override
    public int hashCode() {
        return rectangle.getLowerBound().hashCode();
    }

    public void addPointToDataset(/*double x, double y, double r,*/ Position position){
            totalNumberOfPointsType++;
        switch(position){
            case TOPRIGHTQUARTER: case TOPRIGHT:
                addToTopSpecialArea();
                addToRightSpecialArea();
                addToTopRightQuarterArea();
                break;
            case TOPLEFTQUARTER: case TOPLEFT:
                addToTopSpecialArea();
                addToLeftSpecialArea();
                addToTopLeftQuarterArea();
                break;
            case BOTTOMRIGHTQUARTER: case BOTTOMRIGHT:
                addToBottomSpecialArea();
                addToRightSpecialArea();
                addToBottomRightQuarterArea();
                break;
            case BOTTOMLEFTQUARTER: case BOTTOMLEFT:
                addToBottomSpecialArea();
                addToLeftSpecialArea();
                addToBottomLeftQuarterArea();
                break;
            case TOP:
                addToTopSpecialArea();
                break;
            case BOTTOM:
                addToBottomSpecialArea();
                break;
            case RIGHT:
                addToRightSpecialArea();
                break;
            case LEFT:
                addToLeftSpecialArea();
                break;
            default:
                break;
        }

    }
    private void addToBottomSpecialArea(){
            datasetSpecialAreas[0] = ++datasetSpecialAreas[0];
    }

    private void addToRightSpecialArea(){
            datasetSpecialAreas[1] = ++datasetSpecialAreas[1];
    }

    private void addToTopSpecialArea(){
            datasetSpecialAreas[2] = ++datasetSpecialAreas[2];
    }

    private void addToLeftSpecialArea(){
            datasetSpecialAreas[3] = ++datasetSpecialAreas[3];
    }

    private void addToBottomRightQuarterArea(){
            datasetQuarteredAreas[0] = ++datasetQuarteredAreas[0];
    }

    private void addToTopRightQuarterArea(){
            datasetQuarteredAreas[1] = ++datasetQuarteredAreas[1];
    }

    private void addToTopLeftQuarterArea(){
            datasetQuarteredAreas[2] = ++datasetQuarteredAreas[2];
    }

    private void addToBottomLeftQuarterArea(){
            datasetQuarteredAreas[3] = ++datasetQuarteredAreas[3];
    }

    public int getNumberOfPointsInBottomSpecialArea(){
            return datasetSpecialAreas[0];
    }

    public int getNumberOfPointsInRightSpecialArea(){
            return datasetSpecialAreas[1];
    }

    public int getNumberOfPointsInTopSpecialArea(){
            return datasetSpecialAreas[2];
    }

    public int getNumberOfPointsInLeftSpecialArea(){
            return datasetSpecialAreas[3];
    }

    public int getNumberOfPointsInBottomRightQuarterArea(){
            return datasetQuarteredAreas[0];
    }

    public int getNumberOfPointsInTopRightQuarterArea(){
            return datasetQuarteredAreas[1];
    }

    public int getNumberOfPointsInTopLeftQuarterArea(){
            return datasetQuarteredAreas[2];
    }

    public int getNumberOfPointsInBottomLeftQuarterArea(){
            return datasetQuarteredAreas[3];
    }

//    public int getNumberOfPointsInBottomSpecialArea(){
//        return totalNumberOfPointsType/2;
//    }
//
//    public int getNumberOfPointsInRightSpecialArea(){
//        return totalNumberOfPointsType/2;
//    }
//
//    public int getNumberOfPointsInTopSpecialArea(){
//        return totalNumberOfPointsType/2;
//    }
//
//    public int getNumberOfPointsInLeftSpecialArea(){
//        return totalNumberOfPointsType/2;
//    }
//
//    public int getNumberOfPointsInBottomRightQuarterArea(){ return (int)((totalNumberOfPointsType*Math.PI)/16); }
//
//    public int getNumberOfPointsInTopRightQuarterArea(){
//        return (int)((totalNumberOfPointsType*Math.PI)/16);
//    }
//
//    public int getNumberOfPointsInTopLeftQuarterArea(){
//        return (int)((totalNumberOfPointsType*Math.PI)/16);
//    }
//
//    public int getNumberOfPointsInBottomLeftQuarterArea(){
//        return (int)((totalNumberOfPointsType*Math.PI)/16);
//    }

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
