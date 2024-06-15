package gr.ds.unipi.agreements;

public class Agreement<T extends Space> {

    private final Edge<T> edgeA;
    private final Edge<T> edgeB;

    private Agreement(Edge<T> edgeA, Edge<T> edgeB){
        this.edgeA = edgeA;
        this.edgeB = edgeB;
    }

    public Edge<T> getEdgeA() {
        return edgeA;
    }

    public Edge<T> getEdgeB() {
        return edgeB;
    }

    public static <T extends Space> Agreement<T> newAgreement(Edge<T> edgeA, Edge<T> edgeB){
        return new Agreement<T>(edgeA, edgeB);
    }

    public static <T extends Space> Agreement<T> reverseAgreement(Agreement<T> agreement){
        return new Agreement<T>(agreement.edgeB, agreement.edgeA);
    }}
