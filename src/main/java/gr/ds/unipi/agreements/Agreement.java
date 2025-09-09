package gr.ds.unipi.agreements;


public class Agreement {

    private final Edge edgeA;
    private final Edge edgeB;

    private Agreement(Edge edgeA, Edge edgeB){
        this.edgeA = edgeA;
        this.edgeB = edgeB;
    }

    public Edge getEdgeA() {
        return edgeA;
    }

    public Edge getEdgeB() {
        return edgeB;
    }

    public static Agreement newAgreement(Edge edgeA, Edge edgeB){
        return new Agreement(edgeA, edgeB);
    }

    public static Agreement reverseAgreement(Agreement agreement){
        return new Agreement(agreement.edgeB, agreement.edgeA);
    }}
