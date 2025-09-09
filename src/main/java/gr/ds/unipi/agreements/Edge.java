package gr.ds.unipi.agreements;

import gr.ds.unipi.TypeSet;

public class Edge {

//    private final int tail;
//    private final int head;
    private final TypeSet typeSet;
    private final int weight;
    private boolean isLocked;
    private boolean isEliminated;

    private Edge(/*int tail, int head,*/ TypeSet typeSet, int weight) {
//        this.tail = tail;
//        this.head = head;
        this.typeSet = typeSet;
        this.weight = weight;
    }

    public static Edge newEdge(/*int tail, int head,*/ TypeSet typeSet, int weight){
        return new Edge(/*tail, head,*/ typeSet, weight);
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

    public TypeSet getTypeSet() {
        return typeSet;
    }

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
}