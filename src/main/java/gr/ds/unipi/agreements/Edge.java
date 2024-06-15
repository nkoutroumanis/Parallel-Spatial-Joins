package gr.ds.unipi.agreements;

import gr.ds.unipi.TypeSet;

public class Edge<T extends Space> {

    private final T tail;
    private final T head;
    private final TypeSet typeSet;
    private final long weight;
    private boolean isLocked;
    private boolean isEliminated;

    private Edge(T tail, T head, TypeSet typeSet, long weight) {
        this.tail = tail;
        this.head = head;
        this.typeSet = typeSet;
        this.weight = weight;
    }

    public static <T extends Space> Edge<T> newEdge(T tail, T head, TypeSet typeSet, long weight){
        return new Edge<>(tail, head, typeSet, weight);
    }

    public long getWeight() {
        return weight;
    }

    public T getTail() {
        return tail;
    }

    public T getHead() {
        return head;
    }

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