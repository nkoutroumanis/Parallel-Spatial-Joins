package gr.ds.unipi.quadtree;

import gr.ds.unipi.shapes.Rectangle;

import java.util.List;

public class NonLeafNode extends Node {

    private final Node[] children;

//    private NonLeafNode(NonLeafNode parent, Rectangle rectangle){
//        super(parent, rectangle);
//        children = new Node[4];
//    }

    private NonLeafNode(NonLeafNode parent, Rectangle rectangle){
        super(parent, rectangle);
        children = new Node[4];
    }

    public Node[] getChildren(){
        return children;
    }

    public Node getBottomLeftChild(){
        return children[0];
    }

    public Node getBottomRightChild(){
        return children[1];
    }

    public Node getTopLeftChild(){
        return children[2];
    }

    public Node getTopRightChild(){
        return children[3];
    }

    public static NonLeafNode newNonLeafNode(NonLeafNode parent, Rectangle rectangle){
        return new NonLeafNode(parent, rectangle);
    }

    public void replaceChild(Node nodeToReplace, Node newChild){
        for (int i = 0; i < children.length; i++) {
            if(children[i]==nodeToReplace){
                children[i] = newChild;
                break;
            }
        }
    }

    public void setChildren(List<LeafNode> nodes){
        for (int i = 0; i < nodes.size(); i++) {
            children[i] = nodes.get(i);
        }
    }

    public String toString(String indent){
        return indent+super.getRectangle().toString();
    }

}
