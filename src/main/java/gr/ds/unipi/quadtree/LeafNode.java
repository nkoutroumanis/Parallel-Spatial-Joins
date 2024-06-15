package gr.ds.unipi.quadtree;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Space;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class LeafNode extends Node implements Space {

    private final List<Point> datasetA;
    private final List<Point> datasetB;
    private final int id;

    private LeafNode(NonLeafNode parent, Rectangle rectangle) {
        super(parent, rectangle);
        this.datasetA = new ArrayList<>();
        this.datasetB = new ArrayList<>();
        id = -1;
    }

    private LeafNode(LeafNode leafNode, int id) {
        super(leafNode.getParent(), leafNode.getRectangle());
        this.datasetA = leafNode.getDatasetA();
        this.datasetB = leafNode.getDatasetB();
        this.id = id;
    }

    public void insertPoint(Point point, TypeSet typeSet){
        if(typeSet.equals(TypeSet.A)){
            datasetA.add(point);
        }else{
            datasetB.add(point);
        }
    }

    public long getNumberOfPointsAType(){
        return datasetA.size();
    }

    public long getNumberOfPointsBType(){
        return datasetB.size();
    }

    public NonLeafNode getParent() {
        return super.getParent();
    }

    public Rectangle getRectangle(){
        return super.getRectangle();
    }

    public double getWidth(){
        return super.getRectangle().getUpperBound().getX()-super.getRectangle().getLowerBound().getX();
    }

    public double getHeight(){
        return super.getRectangle().getUpperBound().getX()-super.getRectangle().getLowerBound().getX();
    }

    public static LeafNode newLeafNode(NonLeafNode parent, Rectangle rectangle){
        return new LeafNode(parent, rectangle);
    }

    public static LeafNode newLeafNode(LeafNode leafNode, int id){
        return new LeafNode(leafNode, id);
    }

    public List<Point> getDatasetA() {
        return datasetA;
    }

    public List<Point> getDatasetB() {
        return datasetB;
    }

    public String toString(String indent){
        return indent+"id: "+id +" "+super.getRectangle().toString() +" datasetA:"+datasetA.size()+" datasetB:"+datasetB.size();
    }

    public int getId(){
        return id;
    }

}
