package gr.ds.unipi.self.agreements;

import java.util.Objects;

public class Edge {

//    private final int tail;
//    private final int head;
    private boolean directionCode;
    private final int weight;
    private boolean isLocked;
    private boolean isEliminated;

    private Edge(/*int tail, int head,*/ boolean directionCode, int weight) {
//        this.tail = tail;
//        this.head = head;
        this.directionCode = directionCode;
        this.weight = weight;
    }

    public static Edge newEdge(/*int tail, int head,*/ boolean directionCode, int weight){
        return new Edge(/*tail, head,*/ directionCode, weight);
    }

    public int getWeight() {
        return weight;
    }

//    public int getTail() {
//        return tail;
//    }
//
//    public int getHead() {
//        return head;
//    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isEliminated() {
        return isEliminated;
    }

    public void lock() {
//        if(isLocked){
//            try {
//                throw new Exception("The edge is already locked");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }else{
            isLocked = true;
//        }
    }

    public void eliminate() {
        if(isEliminated){
            try {
                throw new Exception("The edge is already eliminated");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            isEliminated = true;
        }
    }

    public boolean getDirectionCode() {
        return directionCode;
    }

    public void reverseEdge(){
        directionCode = !directionCode;
    }

    public String toString() {
        return "Direction: " + directionCode + ", isLocked: " + isLocked+ ", isEliminated: " + isEliminated + ", weight: " + weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return directionCode == edge.directionCode && isLocked == edge.isLocked && isEliminated == edge.isEliminated;
    }

    @Override
    public int hashCode() {
        return Objects.hash(directionCode, isLocked, isEliminated);
    }
}