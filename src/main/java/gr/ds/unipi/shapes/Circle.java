package gr.ds.unipi.shapes;

import gr.ds.unipi.grid.Cell;

public class Circle {

    private final Point center;
    private final double radius;

    private Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public static Circle newCircle(Point center, double radius){
        return new Circle(center, radius);
    }

    public static int compare(double cx, double cy, double r, double x, double y){
        return Double.compare(Math.pow((cx-x), 2) + Math.pow((cy-y), 2), Math.pow(r,2));
    }

    public Rectangle getMbr(){
        return Rectangle.newRectangle(Point.newPoint(center.getX()-radius,center.getY()-radius), Point.newPoint(center.getX()+radius,center.getY()+radius));
    }

    public boolean containsInclusive(Point point){
        return Double.compare(Math.pow(center.getX()-point.getX(), 2)+Math.pow(center.getY()-point.getY(), 2), Math.pow(radius, 2)) != 1;
    }

    public boolean containsExclusive(Point point){
        return Double.compare(Math.pow(center.getX()-point.getX(), 2)+Math.pow(center.getY()-point.getY(), 2), Math.pow(radius, 2)) == -1;
    }

    public boolean containsInclusiveHaversine(Point point){
        return Double.compare(Cell.haversineDistanceKm(center.getX(), center.getY(), point.getX(), point.getY()), radius) != 1;
    }
//
//    public boolean containsExclusive(Point point){
//        return Double.compare(Cell.haversineDistanceKm(center.getX(), center.getY(), point.getX(), point.getY()), radius) == -1;
//    }

    public String toString(){
        return "Circle ("+center.getX()+", "+center.getY()+"), "+radius;
    }
}
