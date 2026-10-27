package gr.ds.unipi.self.adaptiveGrid;

class Cell {

    private int dataset;

    public static Cell newCell() {
        return new Cell();
    }

    public void increaseCounterDataset(){
        dataset++;
    }

    public int getDataset() {
        return dataset;
    }

}
