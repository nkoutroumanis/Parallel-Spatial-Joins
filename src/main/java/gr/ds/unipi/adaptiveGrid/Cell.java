package gr.ds.unipi.adaptiveGrid;

class Cell {

    private int datasetA;
    private int datasetB;

    public static Cell newCell() {
        return new Cell();
    }

    public void increaseCounterDatasetA(){
        datasetA++;
    }

    public void increaseCounterDatasetB(){
        datasetB++;
    }

    public int getDatasetA() {
        return datasetA;
    }

    public int getDatasetB() {
        return datasetB;
    }
}
