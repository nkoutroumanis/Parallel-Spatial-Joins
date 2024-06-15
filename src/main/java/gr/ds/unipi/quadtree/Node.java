package gr.ds.unipi.quadtree;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    private final NonLeafNode parent;
    private final Rectangle rectangle;

    protected Node(NonLeafNode parent, Rectangle rectangle){
        this.parent = parent;
        this.rectangle = rectangle;
    }

    protected NonLeafNode getParent() {
        return parent;
    }

    protected Rectangle getRectangle(){
        return rectangle;
    }

    public abstract String toString(String indent);

    //
//    private final Rectangle rectangle;
//    private int numberOfContainedPoints ;
//    private boolean hasChildrenQuadrants = false;
//    private List<Point> datasetA;
//    private List<Point> datasetB;
//
//    private Node parent;
//    private Node topLeftChildQuadrant;
//    private Node topRightChildQuadrant;
//    private Node bottomRightChildQuadrant;
//    private Node bottomLeftChildQuadrant;
//
//    private Node(Node parent, Rectangle rectangle) {
//        this.parent = parent;
//        this.rectangle = rectangle;
//        this.datasetA = new ArrayList<>();
//        this.datasetB = new ArrayList<>();
//    }
//
//    public void insertPoint(Point point, TypeSet typeSet){
//        if(typeSet.equals(TypeSet.A)){
//            datasetA.add(point);
//        }else{
//            datasetB.add(point);
//        }
//    }
//
//    public int getNumberOfPointsA(){
//        return datasetA.size();
//    }
//
//    public int getNumberOfPointsB(){
//        return datasetB.size();
//    }
//
//    public Node getParent() {
//        return parent;
//    }
//
//    public Rectangle getRectangle(){
//        return rectangle;
//    }
//
//    public double getWidth(){
//        return rectangle.getUpperBound().getX()-rectangle.getLowerBound().getX();
//    }
//
//    public double getHeight(){
//        return rectangle.getUpperBound().getX()-rectangle.getLowerBound().getX();
//    }
//
//    public boolean hasChildrenQuadrants() {
//        return hasChildrenQuadrants;
//    }
//
//    public int getNumberOfContainedPoints() {
//        return numberOfContainedPoints;
//    }
//
//    public Node getTopLeftChildQuadrant() {
//        return topLeftChildQuadrant;
//    }
//
//    public void increaseByOneNumberOfContainedPoints(){
//        numberOfContainedPoints++;
//    }
//
//    public void setChildQuadrants(Node topLeftChildQuadrant, Node topRightChildQuadrant, Node bottomRightChildQuadrant, Node bottomLeftChildQuadrant){
//        this.topLeftChildQuadrant = topLeftChildQuadrant;
//        this.topRightChildQuadrant = topRightChildQuadrant;
//        this.bottomRightChildQuadrant = bottomRightChildQuadrant;
//        this.bottomLeftChildQuadrant = bottomLeftChildQuadrant;
//        hasChildrenQuadrants = true;
//    }
//
//    public Node getTopRightChildQuadrant() {
//        return topRightChildQuadrant;
//    }
//
//    public Node getBottomRightChildQuadrant() {
//        return bottomRightChildQuadrant;
//    }
//
//    public Node getBottomLeftChildQuadrant() {
//        return bottomLeftChildQuadrant;
//    }
//
//    public static Node newNode(Node parent, Rectangle rectangle){
//        return new Node(parent, rectangle);
//    }
//
//    public double getUpperBoundx() {
//        return upperBoundx;
//    }
//
//    public double getUpperBoundy() {
//        return upperBoundy;
//    }
//
//    public double getLowerBoundx() {
//        return lowerBoundx;
//    }
//
//    public double getLowerBoundy() {
//        return lowerBoundy;
//    }
//
//    public boolean intersects(Point point){
//        if(((Double.compare(lowerBoundx, point.getX()) != 1) && (Double.compare(upperBoundx, point.getX()) == 1)) &&
//                ((Double.compare(lowerBoundy, point.getY()) != 1) && (Double.compare(upperBoundy, point.getY()) == 1))){
//            return true;
//        }
//        return false;
//    }
//
//    public Point[] getPoints() {
//        return points;
//    }
//
//    public void setPoints(Point[] points) {
//        this.points = points;
//    }
//
//    public String toString(){
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("Top Left");
//        return "Node ["+lowerBoundx + ","+lowerBoundy +"], ["+upperBoundx +","+upperBoundy +"]" +" - has "+ getNumberOfContainedPoints() + " array:"+getPoints();
////        if(hasChildrenQuadrants==true){
////
////
////            return "Node ["+lowerBoundx + ","+lowerBoundy +"], ["+upperBoundx +","+upperBoundy +"]" +" - has "+ getNumberOfContainedPoints() + " array:"+getPoints();
////        }
////        else{
////            return "Leaf Node ["+lowerBoundx + ","+lowerBoundy +"], ["+upperBoundx +","+upperBoundy +"]" +" - has "+ getNumberOfContainedPoints() + " array:"+getPoints();
////        }
//
//
//    }
}
