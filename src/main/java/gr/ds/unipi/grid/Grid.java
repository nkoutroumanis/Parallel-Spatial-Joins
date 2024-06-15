package gr.ds.unipi.grid;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.shapes.Circle;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;
import scala.Function4;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static gr.ds.unipi.grid.Position.*;

public class Grid {

    private final Rectangle rectangle;
    private final long cellsInXAxis;
    private final long cellsInYAxis;

    private final double x;
    private final double y;

    private final Map<Long, Cell> cells;
    private final Map<Point, Agreements<Cell>> agreements;

    private final double r;
    private TriFunction<Cell, Cell, Point, Agreement> function;

    private final boolean geoData;

    private final Function4<Cell, Double, Double, Double, Position> getPositionFunction;

    private Grid(Rectangle rectangle, long cellsInXAxis, long cellsInYAxis, double r, TriFunction<Cell, Cell, Point, Agreement> function, boolean geoData){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        this.r = r;

        this.geoData = geoData;
        if(geoData){
            getPositionFunction = (Function4<Cell, Double, Double, Double, Position> & Serializable) Cell::getGeoPosition;
        }else{
            getPositionFunction = (Function4<Cell, Double, Double, Double, Position> & Serializable) Cell::getPosition;
        }

        if(Double.compare(r,0) != 1){
            try {
                throw new Exception("r should be greater than 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX() ) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY() ) / cellsInYAxis;
        System.out.println("x: "+x +" y:"+y);

        if(!geoData) {
            if (r >= x / 2 || r >= y / 2) {
                try {
                    throw new Exception("Radius is larger than the half length of the side of the cells");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        cells = new ConcurrentHashMap<>();
        agreements = new ConcurrentHashMap<>();
        this.function = function;

//        forEach((id, rec)->{
//            Cell cell = Cell.newCell(rec);
//            cells.put(id, cell);
//
//            if(agreements.containsKey(rec.getLowerBound())){
//                agreements.get(rec.getLowerBound()).addCell(cell);
//            }else{
//                agreements.put(rec.getLowerBound(), Agreements.newAgreements(cell));
//            }
//
//            if(agreements.containsKey(Point.newPoint(rec.getUpperBound().getX(), rec.getLowerBound().getY()))){
//                agreements.get(Point.newPoint(rec.getUpperBound().getX(), rec.getLowerBound().getY())).addCell(cell);
//            }else{
//                agreements.put(Point.newPoint(rec.getUpperBound().getX(), rec.getLowerBound().getY()), Agreements.newAgreements(cell));
//            }
//
//            if(agreements.containsKey(rec.getUpperBound())){
//                agreements.get(rec.getUpperBound()).addCell(cell);
//            }else{
//                agreements.put(rec.getUpperBound(), Agreements.newAgreements(cell));
//            }
//
//            if(agreements.containsKey(Point.newPoint(rec.getLowerBound().getX(), rec.getUpperBound().getY()))){
//                agreements.get(Point.newPoint(rec.getLowerBound().getX(), rec.getUpperBound().getY())).addCell(cell);
//            }else{
//                agreements.put(Point.newPoint(rec.getLowerBound().getX(), rec.getUpperBound().getY()), Agreements.newAgreements(cell));
//            }
//        });
    }


    public void load(){

//        List<Map.Entry<Long, Cell>> cellsList = cells.entrySet().stream().sorted(new Comparator<Map.Entry<Long, Cell>>() {
//            @Override
//            public int compare(Map.Entry<Long, Cell> o1, Map.Entry<Long, Cell> o2) {
//                return Long.compare(o1.getValue().getNumberOfPointsAType()*o1.getValue().getNumberOfPointsBType(),o2.getValue().getNumberOfPointsAType()*o2.getValue().getNumberOfPointsBType());
//            }
//        }).collect(Collectors.toList());
//
//        cellsList.forEach(element->{
//            System.out.println(element.getKey()+" "+element.getValue().getNumberOfPointsAType()*element.getValue().getNumberOfPointsBType());
//        });
//
//        cellsList.forEach(element->{
//            Cell cell = getCell(element.getKey());
//            getAgreements(element.getKey(), cell.getRectangle().getLowerBound());
//            getAgreements(element.getKey(), cell.getRectangle().getLowerRightBound());
//            getAgreements(element.getKey(), cell.getRectangle().getUpperLeftBound());
//            getAgreements(element.getKey(), cell.getRectangle().getUpperBound());
//        });
//                 -------------------------------------

        HashMap<Long,Cell> copyHashMapCells = new HashMap<>(cells);

        for(long i = 0; i<cellsInXAxis*cellsInYAxis;i++){
            if(copyHashMapCells.containsKey(i)){
                Cell cell = getCell(i);
                getAgreements(i, cell.getRectangle().getLowerBound());
                getAgreements(i, cell.getRectangle().getLowerRightBound());
                getAgreements(i, cell.getRectangle().getUpperLeftBound());
                getAgreements(i, cell.getRectangle().getUpperBound());
            }
        }

        function = NewFunc.datasetA;
//        HashMap<Long,Cell> copyHashMapCells = new HashMap<>(cells);
//
//         cells.entrySet().stream().sorted(new Comparator<Map.Entry<Long, Cell>>() {
//            @Override
//            public int compare(Map.Entry<Long, Cell> o1, Map.Entry<Long, Cell> o2) {
//                return Long.compare(o1.getValue().getNumberOfPointsAType()*o1.getValue().getNumberOfPointsBType(),o2.getValue().getNumberOfPointsAType()*o2.getValue().getNumberOfPointsBType());
//            }
//        }).collect(Collectors.toList()).forEach(element->{
//             if(copyHashMapCells.containsKey(element.getKey())){
//                 Cell cell = getCell(element.getKey());
//                 getAgreements(element.getKey(), cell.getRectangle().getLowerBound());
//                 getAgreements(element.getKey(), cell.getRectangle().getLowerRightBound());
//                 getAgreements(element.getKey(), cell.getRectangle().getUpperLeftBound());
//                 getAgreements(element.getKey(), cell.getRectangle().getUpperBound());
//             }
//         });

    }

    public void printInfo(){
        System.out.println("Number of cells: "+cells.size());
    }
    public void clean(){
        agreements.clear();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

//        if(!edges) {
//            forEach((id, rec) -> {
//                sb.append(id + " " + rec.toString() + " HashCode: " + rec.hashCode() + " - #A: " + getCell(id).getNumberOfPointsAType() + " #B:" + getCell(id).getNumberOfPointsBType() + "\n");
//            });
//        }else{
            forEach((id, rec) -> {
                sb.append(id + " " + rec.toString() + " HashCode: " + rec.hashCode() + " - #A: " + getCell(id).getNumberOfPointsAType() + " #B:" + getCell(id).getNumberOfPointsBType() + "\n");

                if(agreements.containsKey(rec.getLowerBound())){
                    sb.append(agreements.get(rec.getLowerBound()).toString(cellToId)).append("\n");
                }

                Point p1 = Point.newPoint(rec.getUpperBound().getX(), rec.getLowerBound().getY());
                if(agreements.containsKey(p1)){
                    sb.append(agreements.get(p1).toString(cellToId)).append("\n");
                }

                Point p2 = Point.newPoint(rec.getLowerBound().getX(), rec.getUpperBound().getY());
                if(agreements.containsKey(p2)){
                    sb.append(agreements.get(p2).toString(cellToId)).append("\n");
                }

                if(agreements.containsKey(rec.getUpperBound())){
                    sb.append(agreements.get(rec.getUpperBound()).toString(cellToId)).append("\n");
                }
            });
//        }
        return sb.toString();
    }

    public double getR() {
        return r;
    }

//    private Rectangle getRectangle(long cellId){
//
//        long yc = cellId/cellsInXAxis;
//        long xc = cellId - (yc * cellsInXAxis);
//
//        Point lowerBound = Point.newPoint(rectangle.getLowerBound().getX() + (xc * x), rectangle.getLowerBound().getY() + (yc * y));
//        Point upperBound = Point.newPoint(rectangle.getLowerBound().getX() + ((xc + 1) * x), rectangle.getLowerBound().getY() + ((yc + 1) * y));
//
//        return Rectangle.newRectangle(lowerBound, upperBound);
//    }

        private Rectangle getRectangle(long cellId){

        long yc = cellId/cellsInXAxis;
        long xc = cellId - (yc * cellsInXAxis);

        Point lowerBound = Point.newPoint(rectangle.getLowerBound().getX() + (xc * x), rectangle.getLowerBound().getY() + (yc * y));
//        Point upperBound = Point.newPoint(rectangle.getLowerBound().getX() + ((xc * x) + x), rectangle.getLowerBound().getY() + ((yc * y) + y));
        Point upperBound = Point.newPoint(rectangle.getLowerBound().getX() + (((xc+1) * x)), rectangle.getLowerBound().getY() + (((yc+1) * y)));

        return Rectangle.newRectangle(lowerBound, upperBound);
    }

    private long getCellIdFromXcYc(long xc, long yc){
        return (xc + (yc * cellsInXAxis));
    }

    private Pair<Long,Long> getXcYc(double x, double y) {
        long xc = (long) ((x-rectangle.getLowerBound().getX()) / this.x);
        long yc = (long) ((y-rectangle.getLowerBound().getY()) / this.y);
        return new Pair<>(xc, yc);
    }


    private Function<Cell,Long> cellToId = (Function<Cell, Long> & Serializable) (c)-> getCellId((c.getRectangle().getLowerBound().getX()+c.getRectangle().getUpperBound().getX())/2,(c.getRectangle().getLowerBound().getY()+c.getRectangle().getUpperBound().getY())/2);

    private long getCellId(double x, double y) {

        long xc = (long) ((x-rectangle.getLowerBound().getX()) / this.x);
        long yc = (long) ((y-rectangle.getLowerBound().getY()) / this.y);

        return (xc + (yc * cellsInXAxis));
    }

    private long getCellId(Point point) {
        return getCellId(point.getX(), point.getY());
    }

    private Cell getCell(double x, double y){
        return getCell(getCellId(x, y));
    }

    private Cell getCell(long cellID){
        if(cells.containsKey(cellID)){
            return cells.get(cellID);
        }else{
            return getCellSingleThread(cellID);
//            Cell cell = Cell.newCell(getRectangle(cellID));
//            cells.put(cellID, cell);
//            return cell;
        }
    }

    private synchronized Cell getCellSingleThread(long cellID){
        if(cells.containsKey(cellID)){
            return cells.get(cellID);
        }else{
            Cell cell = Cell.newCell(getRectangle(cellID));
            cells.put(cellID, cell);
            return cell;
        }
    }

    private boolean hasTop(long cellID){
        if((cellID + cellsInXAxis)< (cellsInXAxis*cellsInYAxis)){
            return true;
        }
        return false;
    }

    private boolean hasBottom(long cellID){
        if((cellID - cellsInXAxis)>= 0){
            return true;
        }
        return false;

    }

    private boolean hasLeft(long cellID){
        if(((cellID -1) >= 0) && ((cellID - 1)%(cellsInXAxis)) != (cellsInXAxis-1)){
            return true;
        }
        return false;
    }

    private boolean hasRight(long cellID){
        if((cellID +1) < (cellsInXAxis*cellsInYAxis) && ((cellID +1)%cellsInXAxis) !=0){
            return true;
        }
        return false;
    }

    private Agreements<Cell> getAgreements(long cellID, Point point){
            if (agreements.containsKey(point)) {
                return agreements.get(point);
            } else {
                return getAgreementsSingleThread(cellID, point);
            }

    }

    private synchronized Agreements<Cell> getAgreementsSingleThread(long cellID, Point point){
        if (agreements.containsKey(point)) {
            return agreements.get(point);
        }else {

            boolean bottomLeft = false;
            boolean bottomRight = false;
            boolean topLeft = false;
            boolean topRight = false;

            if (getCell(cellID).getRectangle().getLowerBound().equals(point)) {
                bottomLeft = true;
            } else if (getCell(cellID).getRectangle().getUpperBound().equals(point)) {
                topRight = true;
            } else if (Double.compare(getCell(cellID).getRectangle().getLowerBound().getX(), point.getX()) == 0 &&
                    Double.compare(getCell(cellID).getRectangle().getUpperBound().getY(), point.getY()) == 0) {
                topLeft = true;
            } else if (Double.compare(getCell(cellID).getRectangle().getUpperBound().getX(), point.getX()) == 0 &&
                    Double.compare(getCell(cellID).getRectangle().getLowerBound().getY(), point.getY()) == 0) {
                bottomRight = true;
            } else {
                try {
                    throw new Exception("Problem in determining the cell's corner for Agreements creation.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            List<Cell> cells = new ArrayList<>();
            if (bottomLeft && hasBottom(cellID) && hasLeft(cellID)) {
                cells.add(getCell((cellID - cellsInXAxis) - 1l));
            }

            if ((bottomLeft || bottomRight) && hasBottom(cellID)) {
                cells.add(getCell((cellID - cellsInXAxis)));
            }

            if (bottomRight && hasBottom(cellID) && hasRight(cellID)) {
                cells.add(getCell((cellID - cellsInXAxis) + 1l));
            }

            if ((bottomLeft || topLeft) && hasLeft(cellID)) {
                cells.add(getCell((cellID - 1l)));
            }

            //add plain here
            cells.add(getCell(cellID));

            if ((bottomRight || topRight) && hasRight(cellID)) {
                cells.add(getCell(cellID + 1l));
            }

            if (topLeft && hasTop(cellID) && hasLeft(cellID)) {
                cells.add(getCell((cellID + cellsInXAxis) - 1l));
            }

            if ((topLeft || topRight) && hasTop(cellID)) {
                cells.add(getCell((cellID + cellsInXAxis)));
            }

            if (topRight && hasTop(cellID) && hasRight(cellID)) {
                cells.add(getCell((cellID + cellsInXAxis) + 1l));
            }
            //Agreements<Cell> agreements = Agreements.newAgreements(cells, Functions.function);

            Agreements<Cell> agreements = Agreements.newAgreements(cells, point, function);
            agreements.createEdges();
            this.agreements.put(point, agreements);
            return agreements;
        }
    }

//    private Agreements<Cell> getAgreements(long cellID, Point point){
//        if (agreements.containsKey(point)) {
//            return agreements.get(point);
//        } else {
//                if (agreements.containsKey(point)) {
//                    this.notify();
//                    return agreements.get(point);
//                }
//
//                boolean bottomLeft = false;
//                boolean bottomRight = false;
//                boolean topLeft = false;
//                boolean topRight = false;
//
//                if (getCell(cellID).getRectangle().getLowerBound().equals(point)) {
//                    bottomLeft = true;
//                } else if (getCell(cellID).getRectangle().getUpperBound().equals(point)) {
//                    topRight = true;
//                } else if (Double.compare(getCell(cellID).getRectangle().getLowerBound().getX(), point.getX()) == 0 &&
//                        Double.compare(getCell(cellID).getRectangle().getUpperBound().getY(), point.getY()) == 0) {
//                    topLeft = true;
//                } else if (Double.compare(getCell(cellID).getRectangle().getUpperBound().getX(), point.getX()) == 0 &&
//                        Double.compare(getCell(cellID).getRectangle().getLowerBound().getY(), point.getY()) == 0) {
//                    bottomRight = true;
//                } else {
//                    try {
//                        throw new Exception("Problem in determining the cell's corner for Agreements creation.");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                List<Cell> cells = new ArrayList<>();
//                if (bottomLeft && hasBottom(cellID) && hasLeft(cellID)) {
//                    cells.add(getCell((cellID - cellsInXAxis) - 1l));
//                }
//
//                if ((bottomLeft || bottomRight) && hasBottom(cellID)) {
//                    cells.add(getCell((cellID - cellsInXAxis)));
//                }
//
//                if (bottomRight && hasBottom(cellID) && hasRight(cellID)) {
//                    cells.add(getCell((cellID - cellsInXAxis) + 1l));
//                }
//
//                if ((bottomLeft || topLeft) && hasLeft(cellID)) {
//                    cells.add(getCell((cellID - 1l)));
//                }
//
//                //add plain here
//                cells.add(getCell(cellID));
//
//                if ((bottomRight || topRight) && hasRight(cellID)) {
//                    cells.add(getCell(cellID + 1l));
//                }
//
//                if (topLeft && hasTop(cellID) && hasLeft(cellID)) {
//                    cells.add(getCell((cellID + cellsInXAxis) - 1l));
//                }
//
//                if ((topLeft || topRight) && hasTop(cellID)) {
//                    cells.add(getCell((cellID + cellsInXAxis)));
//                }
//
//                if (topRight && hasTop(cellID) && hasRight(cellID)) {
//                    cells.add(getCell((cellID + cellsInXAxis) + 1l));
//                }
//
//                Agreements<Cell> agreements = Agreements.newAgreements(cells, point, function);
//                agreements.createEdges();
//                this.agreements.put(point, agreements);
//                return agreements;
//
//        }
//    }

    private void forEach(BiConsumer<Long,Rectangle> action){
        if (action == null) {
            throw new NullPointerException();
        }
        for(long i=0;i<cellsInXAxis*cellsInYAxis;i++){
            if(cells.containsKey(i)){
                action.accept(i,getRectangle(i));
            }
        }
    }

    public void addPointDatasetA(double x, double y){
        addPointDataset(x, y, TypeSet.A);
    }

    public void addPointDatasetB(double x, double y){
        addPointDataset(x, y, TypeSet.B);
    }

    private void addPointDataset(double x, double y, TypeSet typeSet){
        Cell cell = getCell(x, y);
        cell.addPointToDataset(typeSet, getPositionFunction.apply(cell,x,y,r));
        //getCell(x, y).addPointToDataset(x,y,r,typeSet);
    }

    public String[] getPartitionsAType(double x, double y){
        return getPartitions(x, y, TypeSet.A);
    }

    public String[] getPartitionsBType(double x, double y){
        return getPartitions(x, y, TypeSet.B);
    }

    private List<Cell> getPartitions(Cell cell, long cellID, Agreements<Cell> agreements, Position position, TypeSet typeSet){
        List<Cell> list;
        switch(position){
            case TOP:
                list = agreements.getPartitionsTwoSpacesCase(cell, getCell((cellID + cellsInXAxis)), typeSet);
                break;
            case BOTTOM:
                list = agreements.getPartitionsTwoSpacesCase(cell, getCell((cellID - cellsInXAxis)), typeSet);
                break;
            case RIGHT:
                list = agreements.getPartitionsTwoSpacesCase(cell, getCell((cellID  + 1)), typeSet);
                break;
            case LEFT:
                list = agreements.getPartitionsTwoSpacesCase(cell, getCell((cellID  - 1)), typeSet);
                break;
            default:
                throw new IllegalStateException("Position was not caught.");
        }
        return list;
    }

    private List<Cell> getPartitions(Cell cell, long cellId, Agreements<Cell> agreements, Position position, TypeSet typeSet, boolean outerQuarter){
        switch (position){
            case TOPRIGHT:
                if(hasTop(cellId) && hasRight(cellId)){
                    return agreements.getPartitionsFourSpacesCase(cell, typeSet, outerQuarter);
                }else if(hasTop(cellId)){
                    return getPartitions(cell, cellId, agreements, TOP, typeSet);
                }else if(hasRight(cellId)){
                    return getPartitions(cell, cellId, agreements, RIGHT, typeSet);
                }
                break;
            case TOPLEFT:
                if(hasTop(cellId) && hasLeft(cellId)){
                    return agreements.getPartitionsFourSpacesCase(cell, typeSet, outerQuarter);
                }else if(hasTop(cellId)){
                    return getPartitions(cell, cellId, agreements, TOP, typeSet);
                }else if(hasLeft(cellId)){
                    return getPartitions(cell, cellId, agreements, LEFT, typeSet);
                }
                break;
            case BOTTOMRIGHT:
                if(hasBottom(cellId) && hasRight(cellId)){
                    return agreements.getPartitionsFourSpacesCase(cell, typeSet, outerQuarter);
                }else if(hasBottom(cellId)){
                    return getPartitions(cell, cellId, agreements, BOTTOM, typeSet);
                }else if(hasRight(cellId)){
                    return getPartitions(cell, cellId, agreements, RIGHT, typeSet);
                }
                break;
            case BOTTOMLEFT:
                if(hasBottom(cellId) && hasLeft(cellId)){
                    return agreements.getPartitionsFourSpacesCase(cell, typeSet, outerQuarter);
                }else if(hasBottom(cellId)){
                    return getPartitions(cell, cellId, agreements, BOTTOM, typeSet);
                }else if(hasLeft(cellId)){
                    return getPartitions(cell, cellId, agreements, LEFT, typeSet);
                }
                break;
            default:
                throw new IllegalStateException("Position was not caught.");
        }
        List<Cell> partitions = new ArrayList<>();
        partitions.add(cell);
        return partitions;
        //return Collections.singletonList(cell);
    }

    private List<Cell> checkForExtraPartition(Cell cell, long cellId, Position position1, boolean horizontal1, Position position2, boolean horizontal2, Point point, TypeSet typeSet) {
        List<Cell> extraCells = new ArrayList<>();
        Optional<Cell> optional1;
        Optional<Cell> optional2;
        if(geoData){
            optional1 = checkForExtraGeoPartition(cell, cellId, position1, horizontal1, point, typeSet);
            optional2 = checkForExtraGeoPartition(cell, cellId, position2, horizontal2, point, typeSet);
        }else{
            optional1 = checkForExtraPartition(cell, cellId, position1, horizontal1, point, typeSet);
            optional2 = checkForExtraPartition(cell, cellId, position2, horizontal2, point, typeSet);
        }
        optional1.ifPresent(extraCells::add);
        optional2.ifPresent(extraCells::add);
        return extraCells;
    }

//    private Optional<Cell> checkForExtraGeoPartition(Cell cell, long cellId, Position position, boolean horizontal, Point point, TypeSet typeSet) {
//        switch (position){
//            case TOPRIGHT:
//                if(hasTop(cellId) && hasRight(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
//                    if(extraCell.isPresent()){
//                        if(horizontal){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX()-(r/(Math.cos(Math.toRadians(rectangle.getUpperBound().getY())) * 111.321)),cell.getRectangle().getUpperBound().getY()), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                        }else{
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getUpperBound().getY()-(r/110.574)), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                        }
//                    }
//                }
//                break;
//            case TOPLEFT:
//                if(hasTop(cellId) && hasLeft(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperLeftBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
//                    if(extraCell.isPresent()){
//                        if(horizontal){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX()+(r/(Math.cos(Math.toRadians(rectangle.getUpperLeftBound().getY())) * 111.321)),cell.getRectangle().getUpperLeftBound().getY()), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                        }else{
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX(),cell.getRectangle().getUpperLeftBound().getY()-(r/110.574)), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                        }
//                    }
//                }
//                break;
//            case BOTTOMRIGHT:
//                if(hasBottom(cellId) && hasRight(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerRightBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
//                    if(extraCell.isPresent()){
//                        if(horizontal){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX()-(r/(Math.cos(Math.toRadians(rectangle.getLowerRightBound().getY())) * 111.321)),cell.getRectangle().getLowerRightBound().getY()), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                        }else{
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX(),cell.getRectangle().getLowerRightBound().getY()+(r/110.574)), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                        }
//                    }
//                }
//                break;
//            case BOTTOMLEFT:
//                if(hasBottom(cellId) && hasLeft(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
//                    if(extraCell.isPresent()){
//                        if(horizontal){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX()+(r/(Math.cos(Math.toRadians(rectangle.getLowerBound().getY())) * 111.321)),cell.getRectangle().getLowerBound().getY()), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                        }else{
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getLowerBound().getY()+(r/110.574)), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                        }
//                    }
//                }
//                break;
//            default:
//                throw new IllegalStateException("Position was not caught.");
//        }
//        return Optional.empty();
//    }

        private Optional<Cell> checkForExtraGeoPartition(Cell cell, long cellId, Position position, boolean horizontal, Point point, TypeSet typeSet) {
        switch (position){
            case TOPRIGHT:
                if(hasTop(cellId) && hasRight(cellId)){
                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
                    if(extraCell.isPresent()){
                        if(horizontal){
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX()-(r/(Math.cos(Math.toRadians(rectangle.getUpperBound().getY())) * 111.321)),cell.getRectangle().getUpperBound().getY()), r).containsInclusiveHaversine(point)){
                                return extraCell;
                            }
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getUpperBound().getY()), 2*r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
                        }else{
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getUpperBound().getY()-(r/110.574)), r).containsInclusiveHaversine(point)){
                                return extraCell;
                            }
                        }
                    }
                }
                break;
            case TOPLEFT:
                if(hasTop(cellId) && hasLeft(cellId)){
                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperLeftBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
                    if(extraCell.isPresent()){
                        if(horizontal){
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX()+(r/(Math.cos(Math.toRadians(rectangle.getUpperLeftBound().getY())) * 111.321)),cell.getRectangle().getUpperLeftBound().getY()), r).containsInclusiveHaversine(point)){
                                return extraCell;
                            }
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX(),cell.getRectangle().getUpperLeftBound().getY()), 2*r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
                        }else{
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX(),cell.getRectangle().getUpperLeftBound().getY()-(r/110.574)), r).containsInclusiveHaversine(point)){
                                return extraCell;
                            }
                        }
                    }
                }
                break;
            case BOTTOMRIGHT:
                if(hasBottom(cellId) && hasRight(cellId)){
                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerRightBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
                    if(extraCell.isPresent()){
                        if(horizontal){
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX(),cell.getRectangle().getLowerRightBound().getY()), 2*r).containsInclusiveHaversine(point)){
                                return extraCell;
                            }
                        }else{
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX(),cell.getRectangle().getLowerRightBound().getY()+(r/110.574)), r).containsInclusiveHaversine(point)){
                                return extraCell;
                            }
                        }
                    }
                }
                break;
            case BOTTOMLEFT:
                if(hasBottom(cellId) && hasLeft(cellId)){
                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
                    if(extraCell.isPresent()){
                        if(horizontal){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX()+(r/(Math.cos(Math.toRadians(rectangle.getLowerBound().getY())) * 111.321)),cell.getRectangle().getLowerBound().getY()), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getLowerBound().getY()), 2*r).containsInclusiveHaversine(point)){
                                return extraCell;
                            }
                        }else{
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getLowerBound().getY()+(r/110.574)), r).containsInclusiveHaversine(point)){
                                return extraCell;
                            }
                        }
                    }
                }
                break;
            default:
                throw new IllegalStateException("Position was not caught.");
        }
        return Optional.empty();
    }

//    private Optional<Cell> checkForExtraGeoPartition(Cell cell, long cellId, Position position, boolean horizontal, Point point, TypeSet typeSet) {
//        switch (position){
//            case TOPRIGHT:
//                if(hasTop(cellId) && hasRight(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
//                    if(extraCell.isPresent()){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getUpperBound().getY()), 2*r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//
//                    }
//                }
//                break;
//            case TOPLEFT:
//                if(hasTop(cellId) && hasLeft(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperLeftBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
//                    if(extraCell.isPresent()){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX(),cell.getRectangle().getUpperLeftBound().getY()), 2*r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                    }
//                }
//                break;
//            case BOTTOMRIGHT:
//                if(hasBottom(cellId) && hasRight(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerRightBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
//                    if(extraCell.isPresent()){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX(),cell.getRectangle().getLowerRightBound().getY()), 2*r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                    }
//                }
//                break;
//            case BOTTOMLEFT:
//                if(hasBottom(cellId) && hasLeft(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
//                    if(extraCell.isPresent()){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getLowerBound().getY()), 2*r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
//                    }
//                }
//                break;
//            default:
//                throw new IllegalStateException("Position was not caught.");
//        }
//        return Optional.empty();
//    }

    private Optional<Cell> checkForExtraPartition(Cell cell, long cellId, Position position, boolean horizontal, Point point, TypeSet typeSet) {
        switch (position){
            case TOPRIGHT:
                if(hasTop(cellId) && hasRight(cellId)){
                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
                    if(extraCell.isPresent()){
                        if(horizontal){
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX()-r,cell.getRectangle().getUpperBound().getY()), r).containsInclusive(point)){
                                return extraCell;
                            }
                        }else{
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getUpperBound().getY()-r), r).containsInclusive(point)){
                                return extraCell;
                            }
                        }
                    }
                }
                break;
            case TOPLEFT:
                if(hasTop(cellId) && hasLeft(cellId)){
                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperLeftBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
                    if(extraCell.isPresent()){
                        if(horizontal){
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX()+r,cell.getRectangle().getUpperLeftBound().getY()), r).containsInclusive(point)){
                                return extraCell;
                            }
                        }else{
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX(),cell.getRectangle().getUpperLeftBound().getY()-r), r).containsInclusive(point)){
                                return extraCell;
                            }
                        }
                    }
                }
                break;
            case BOTTOMRIGHT:
                if(hasBottom(cellId) && hasRight(cellId)){
                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerRightBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
                    if(extraCell.isPresent()){
                        if(horizontal){
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX()-r,cell.getRectangle().getLowerRightBound().getY()), r).containsInclusive(point)){
                                return extraCell;
                            }
                        }else{
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX(),cell.getRectangle().getLowerRightBound().getY()+r), r).containsInclusive(point)){
                                return extraCell;
                            }
                        }
                    }
                }
                break;
            case BOTTOMLEFT:
                if(hasBottom(cellId) && hasLeft(cellId)){
                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
                    if(extraCell.isPresent()){
                        if(horizontal){
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX()+r,cell.getRectangle().getLowerBound().getY()), r).containsInclusive(point)){
                                return extraCell;
                            }
                        }else{
                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getLowerBound().getY()+r), r).containsInclusive(point)){
                                return extraCell;
                            }
                        }
                    }
                }
                break;
            default:
                throw new IllegalStateException("Position was not caught.");
        }
        return Optional.empty();
    }

    private String[] getPartitions(double x, double y, TypeSet typeSet){
        long cellID = getCellId(x,y);
        Cell cell = getCell(cellID);

        List<Cell> partitions = null;
        //System.out.println(getCell(4371246).getRectangle().toString());
        switch(getPositionFunction.apply(cell, x, y, r)){
        //switch(cell.getPosition(x, y, r)){
            case PLAIN:
                partitions = new ArrayList<>();
                partitions.add(cell);
                //System.out.println(x+","+y);
                break;
            case TOPRIGHTQUARTER:
                partitions = getPartitions(cell, cellID, getAgreements(cellID, cell.getRectangle().getUpperBound()), TOPRIGHT, typeSet, false);
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPLEFT, true, BOTTOMRIGHT, false, Point.newPoint(x,y), typeSet));
                break;
            case TOPLEFTQUARTER:
                partitions = getPartitions(cell, cellID, getAgreements(cellID, Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getUpperBound().getY())), TOPLEFT, typeSet, false);
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPRIGHT, true, BOTTOMLEFT, false, Point.newPoint(x,y), typeSet));
                break;
            case BOTTOMRIGHTQUARTER:
                partitions = getPartitions(cell, cellID, getAgreements(cellID, Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getLowerBound().getY())), BOTTOMRIGHT, typeSet, false);
                partitions.addAll(checkForExtraPartition(cell, cellID, BOTTOMLEFT, true, TOPRIGHT, false, Point.newPoint(x,y), typeSet));
                break;
            case BOTTOMLEFTQUARTER:
                partitions = getPartitions(cell, cellID, getAgreements(cellID, cell.getRectangle().getLowerBound()), BOTTOMLEFT, typeSet, false);
                partitions.addAll(checkForExtraPartition(cell, cellID, BOTTOMRIGHT, true, TOPLEFT, false, Point.newPoint(x,y), typeSet));
                break;
            case TOPRIGHT:
                partitions = getPartitions(cell, cellID, getAgreements(cellID, cell.getRectangle().getUpperBound()), TOPRIGHT, typeSet, true);
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPLEFT, true, BOTTOMRIGHT, false, Point.newPoint(x,y), typeSet));
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPRIGHT, true, TOPRIGHT, false, Point.newPoint(x,y), typeSet));

                break;
            case TOPLEFT:
                partitions = getPartitions(cell, cellID, getAgreements(cellID, Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getUpperBound().getY())), TOPLEFT, typeSet, true);
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPRIGHT, true, BOTTOMLEFT, false, Point.newPoint(x,y), typeSet));
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPLEFT, true, TOPLEFT, false, Point.newPoint(x,y), typeSet));

                break;
            case BOTTOMRIGHT:
                partitions = getPartitions(cell, cellID, getAgreements(cellID, Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getLowerBound().getY())), BOTTOMRIGHT, typeSet, true);
                partitions.addAll(checkForExtraPartition(cell, cellID, BOTTOMLEFT, true, TOPRIGHT, false, Point.newPoint(x,y), typeSet));
                partitions.addAll(checkForExtraPartition(cell, cellID, BOTTOMRIGHT, true, BOTTOMRIGHT, false, Point.newPoint(x,y), typeSet));

                break;
            case BOTTOMLEFT:
                partitions = getPartitions(cell, cellID, getAgreements(cellID, cell.getRectangle().getLowerBound()), BOTTOMLEFT, typeSet, true);
                partitions.addAll(checkForExtraPartition(cell, cellID, BOTTOMRIGHT, true, TOPLEFT, false, Point.newPoint(x,y), typeSet));
                partitions.addAll(checkForExtraPartition(cell, cellID, BOTTOMLEFT, true, BOTTOMLEFT, false, Point.newPoint(x,y), typeSet));

                break;
            case TOP:
                if(hasTop(cellID)){
                    partitions = getAgreements(cellID, cell.getRectangle().getUpperBound()).getPartitions(cell, getCell((cellID + cellsInXAxis)), typeSet);
                }else{
                    partitions = new ArrayList<>();
                    partitions.add(cell);
//                    partitions = Collections.singletonList(cell);
                }
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPLEFT, true, TOPRIGHT, true, Point.newPoint(x,y), typeSet));
                break;
            case BOTTOM:
                if(hasBottom(cellID)){
                    partitions = getAgreements(cellID, cell.getRectangle().getLowerBound()).getPartitions(cell, getCell((cellID - cellsInXAxis)), typeSet);
                }else{
                    partitions = new ArrayList<>();
                    partitions.add(cell);
//                    partitions = Collections.singletonList(cell);
                }
                partitions.addAll(checkForExtraPartition(cell, cellID, BOTTOMLEFT, true, BOTTOMRIGHT, true, Point.newPoint(x,y), typeSet));
                break;
            case RIGHT:
                if(hasRight(cellID)){
                    partitions = getAgreements(cellID, cell.getRectangle().getUpperBound()).getPartitions(cell, getCell((cellID  + 1)), typeSet);
                }else{
                    partitions = new ArrayList<>();
                    partitions.add(cell);
//                    partitions = Collections.singletonList(cell);
                }
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPRIGHT, false, BOTTOMRIGHT, false, Point.newPoint(x,y), typeSet));

                break;
            case LEFT:
                if(hasLeft(cellID)){
                    partitions = getAgreements(cellID, cell.getRectangle().getLowerBound()).getPartitions(cell, getCell((cellID  - 1)), typeSet);
                }else{
                    partitions = new ArrayList<>();
                    partitions.add(cell);
//                    partitions = Collections.singletonList(cell);
                }
                partitions.addAll(checkForExtraPartition(cell, cellID, TOPLEFT, false, BOTTOMLEFT, false, Point.newPoint(x,y), typeSet));

                break;
            default: throw new RuntimeException("No correct position");
        }
        return Arrays.stream(getPartitionsId(partitions)).distinct().toArray(String[]::new);
    }

    private String[] getPartitionsId(List<Cell> cells){
        //System.out.println(Arrays.toString(cells.toArray()));
        String[] partitions = new String[cells.size()];
        for (int i = 0; i < cells.size(); i++) {
            //System.out.println(cells.get(i).getRectangle().toString());
            partitions[i] = String.valueOf(cellToId.apply(cells.get(i)));
        }
        return partitions;
    }

//    private List<Cell> formList(Cell o, Optional<Cell> o1){
//        List<Cell> partitions = new ArrayList<>();
//        partitions.add(o);
//        if(o1.isPresent()){
//            partitions.add(o1.get());
//        }
//        return partitions;
//    }
//
//    private Optional<Cell> getPartition(Cell cell, long cellID, TypeSet typeSet, Position position){
//        Optional o = null;
//        switch(position){
//            case TOP:
//                if(hasTop(cellID)){
//                    o = getAgreements(cellID, cell.getRectangle().getUpperBound()).getPartition(cell, getCell((cellID + cellsInXAxis)),typeSet);
//                }
//                else{
//                    o = Optional.empty();
//                }
//                break;
//            case BOTTOM:
//                if(hasBottom(cellID)){
//                    o = getAgreements(cellID, cell.getRectangle().getLowerBound()).getPartition(cell,getCell((cellID - cellsInXAxis)),typeSet);
//                }
//                else{
//                    o = Optional.empty();
//                }
//                break;
//            case RIGHT:
//                if(hasRight(cellID)){
//                    o = getAgreements(cellID, cell.getRectangle().getUpperBound()).getPartition(cell,getCell((cellID  + 1)),typeSet);
//                }
//                else{
//                    o = Optional.empty();
//                }
//                break;
//            case LEFT:
//                if(hasLeft(cellID)){
//                    o = getAgreements(cellID, cell.getRectangle().getLowerBound()).getPartition(cell,getCell((cellID  - 1)),typeSet);
//                }
//                else{
//                    o = Optional.empty();
//                }
//                break;
//        }
//        return o;
//    }

    public static Grid newGrid(Rectangle rectangle, long cellsInXAxis, long cellsInYAxis, double r, TriFunction<Cell, Cell, Point, Agreement> function){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new Grid(rectangle, cellsInXAxis, cellsInYAxis, r, function, false);
    }

    public static Grid newGrid(Rectangle rectangle, double r, TriFunction<Cell, Cell, Point, Agreement> function){
        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(2*r));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }
        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(2*r));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (long) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (long) cellsInYAxis);


        return new Grid(rectangle, (long) cellsInXAxis, (long) cellsInYAxis, r, function, false);
    }

    public static Grid newGeoGrid(Rectangle rectangle, long cellsInXAxis, long cellsInYAxis, double r, TriFunction<Cell, Cell, Point, Agreement> function){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new Grid(rectangle, cellsInXAxis, cellsInYAxis, r, function, true);
    }

    public static Grid newGeoGrid(Rectangle rectangle, double r, TriFunction<Cell, Cell, Point, Agreement> function){

        double northLength = Math.cos(Math.toRadians(rectangle.getUpperBound().getY())) *111.321;
        double southLength = Math.cos(Math.toRadians(rectangle.getLowerBound().getY())) *111.321;
        double i;
        System.out.println(northLength +" o "+southLength);

        if(northLength<southLength){
            i = northLength;
        }else{
            i = southLength;
        }

        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(2*(r/i)));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }

        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(2*(r/110.574)));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (long) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (long) cellsInYAxis);

        return new Grid(rectangle, (long) cellsInXAxis, (long) cellsInYAxis, r, function ,true);
    }

    public long getCellsInXAxis(){
        return cellsInXAxis;
    }
    public long getCellsInYAxis(){
        return cellsInYAxis;
    }

//    public static double getSurfaceCell(Cell cell){
//        return (cell.getRectangle().getUpperBound().getX()-cell.getRectangle().getLowerBound().getX())*(cell.getRectangle().getUpperBound().getY()-cell.getRectangle().getLowerBound().getY());
//    }
//
//    public static double average;
//
//    public static double averageCost;
//    public static double stdCost;
//    private void set(){
//
//        List<Long> costs = new ArrayList<>();
//        cells.forEach((id,cell)->costs.add(cell.getNumberOfPointsAType()*cell.getNumberOfPointsBType()));
//        averageCost = cells.values().stream().filter(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()>0).mapToDouble(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()).average().getAsDouble();
//        stdCost = stdDev(cells.values().stream().filter(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()>0).mapToDouble(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()).toArray());
//        System.out.println("averageCost "+averageCost);
//        System.out.println("stdCost "+stdCost);
//        cells.values().stream().mapToDouble(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()).sorted().boxed().collect(Collectors.toList()).forEach(cost-> System.out.println(cost));
        //        if(cells.size()==cellsInXAxis*cellsInYAxis){
//            average = cells.values().stream().mapToLong(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()).average().getAsDouble();
//        }else{
//            List<Long> av = new ArrayList<>();
//            for(int i = cells.size();i<cellsInXAxis*cellsInYAxis;i++){
//                av.add(0L);
//            }
//            List<Long> a = cells.values().stream().mapToLong(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()).boxed().collect(Collectors.toList());
//            a.addAll(av);
//
//            average = a.stream().mapToLong(i->i).average().getAsDouble();
//        }
//
//        if(cells.size()==cellsInXAxis*cellsInYAxis){
//            std = stdDev(cells.values().stream().mapToDouble(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()).toArray());
//        }
//        else{
//            List<Double> av = new ArrayList<>();
//            for(int i = cells.size();i<cellsInXAxis*cellsInYAxis;i++){
//                av.add(0d);
//            }
//            List<Double> a = cells.values().stream().mapToDouble(c->c.getNumberOfPointsAType()*c.getNumberOfPointsBType()).boxed().collect(Collectors.toList());
//            a.addAll(av);
//            System.out.println(a.size());
//            std = stdDev(a.stream().mapToDouble(i->i).toArray());
//        }
//        System.out.println(average);
//
//        System.out.println(std);
//
//        List<Long> points = new ArrayList<>();
//        cells.forEach((id, cell)->points.add(cell.getNumberOfPointsAType()));
//
//        avgA=points.stream().mapToDouble(i->i).average().getAsDouble();
//        stdA= stdDev(points.stream().mapToDouble(i->i).toArray());
//
//        System.out.println("Average A:"+ points.stream().mapToDouble(i->i).average().getAsDouble());
//        System.out.println("Std A:"+ stdDev(points.stream().mapToDouble(i->i).toArray()));
//
//        points.clear();
//        cells.forEach((id, cell)->points.add(cell.getNumberOfPointsBType()));
//
//        avgB=points.stream().mapToDouble(i->i).average().getAsDouble();
//        stdB= stdDev(points.stream().mapToDouble(i->i).toArray());
//
//        System.out.println("Average B:"+ points.stream().mapToDouble(i->i).average().getAsDouble());
//        System.out.println("Std B:"+ stdDev(points.stream().mapToDouble(i->i).toArray()));
//
//        //cells.forEach((id, cell)->System.out.println(cell.getNumberOfPointsAType()* cell.getNumberOfPointsBType()));
//        //errorA = 2.576*Grid.stdA;//2.576*Grid.stdA; //+ 2.576*(stdA/Math.sqrt(cellsInXAxis*cellsInYAxis));//1.96*(stdA/Math.sqrt(cellsInXAxis*cellsInYAxis));
//        //errorB = 2.576*Grid.stdB;//2.576*Grid.stdB; //+ 2.576*(stdA/Math.sqrt(cellsInXAxis*cellsInYAxis));//1.96*(stdB/Math.sqrt(cellsInXAxis*cellsInYAxis));
//        //error = 3*std;///Math.sqrt(cellsInXAxis*cellsInYAxis));
//        System.out.println("errorA "+errorA);
//        System.out.println("errorB "+errorB);
//        System.out.println("error "+error);
//    }

//
//    public static double error;
//    public static double errorA;
//    public static double errorB;
//
//    public static double avgA;
//    public static double avgB;
//    public static double stdA;
//    public static double stdB;
//
//    public static double std;

    public static String experiments ="";
//    public static double stdDev(double... sd) {
//        double sum = 0;
//        double newSum = 0;
//
//        for (int i = 0; i < sd.length; i++) {
//            sum = sum + sd[i];
//        }
//        double mean = (sum) / (sd.length);
//
//        for (int j = 0; j < sd.length; j++) {
//            // put the calculation right in there
//            newSum = newSum + ((sd[j] - mean) * (sd[j] - mean));
//        }
//        double squaredDiffMean = (newSum) / (sd.length);
//        return (Math.sqrt(squaredDiffMean));
//    }

    public void checkforsymmetry(){
        //set();
        agreements.forEach((p,a)->a.checkforsymmetry());
    }

    public HashMap<Long,Long> getCellsWithCosts2(){
        HashMap<Long,Long> cellsWithCosts = new HashMap<>();

        cells.forEach((cellId,cell)->{
            if(cell.getNumberOfPointsAType() != 0 && cell.getNumberOfPointsBType() != 0) {
                cellsWithCosts.put(cellId, cell.getNumberOfPointsAType() * cell.getNumberOfPointsBType());
            }
        });
        return cellsWithCosts;
    }

    public HashMap<Long,Long> getCellsWithCosts1(){
        HashMap<Long,Long> cellsWithCosts = new HashMap<>();

        cells.forEach((cellID,cell)->{

            if((cell.getNumberOfPointsAType()) != 0 && (cell.getNumberOfPointsBType()) != 0) {

                long extraA = 0;
                long extraB = 0;

                if (hasBottom(cellID) && hasLeft(cellID)) {
                    Cell cellN = getCell((cellID - cellsInXAxis) - 1l);

                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound()).getEdge(cellN, cell);
                    if (!e.isEliminated()) {
                        if (e.getTypeSet().equals(TypeSet.A)) {
                            extraA = extraA + cellN.getNumberOfPointsAInTopRightQuarterArea();
                        } else {
                            extraB = extraB + cellN.getNumberOfPointsBInTopRightQuarterArea();

                        }
                    }
                }

                if (hasBottom(cellID) && hasRight(cellID)) {
                    Cell cellN = getCell((cellID - cellsInXAxis) + 1l);
                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerRightBound()).getEdge(cellN, cell);
                    if (!e.isEliminated()) {
                        if (e.getTypeSet().equals(TypeSet.A)) {
                            extraA = extraA + cellN.getNumberOfPointsAInTopLeftQuarterArea();
                        } else {
                            extraB = extraB + cellN.getNumberOfPointsBInTopLeftQuarterArea();

                        }
                    }
                }

                if (hasTop(cellID) && hasLeft(cellID)) {
                    Cell cellN = getCell((cellID + cellsInXAxis) - 1l);
                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperLeftBound()).getEdge(cellN, cell);
                    if (!e.isEliminated()) {
                        if (e.getTypeSet().equals(TypeSet.A)) {
                            extraA = extraA + cellN.getNumberOfPointsAInBottomRightQuarterArea();
                        } else {
                            extraB = extraB + cellN.getNumberOfPointsBInBottomRightQuarterArea();

                        }
                    }
                }

                if (hasTop(cellID) && hasRight(cellID)) {
                    Cell cellN = getCell((cellID + cellsInXAxis) + 1l);
                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound()).getEdge(cellN, cell);
                    if (!e.isEliminated()) {
                        if (e.getTypeSet().equals(TypeSet.A)) {
                            extraA = extraA + cellN.getNumberOfPointsAInBottomLeftQuarterArea();
                        } else {
                            extraB = extraB + cellN.getNumberOfPointsBInBottomLeftQuarterArea();

                        }
                    }
                }


                if (hasBottom(cellID)) {
                    Cell cellN = getCell((cellID - cellsInXAxis));
                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound()).getEdge(cellN, cell);
                    Edge e1 = getAgreements(cellID, cell.getRectangle().getLowerRightBound()).getEdge(cellN, cell);

                    if (e.getTypeSet().equals(TypeSet.A)) {
                        extraA = extraA + cellN.getNumberOfPointsAInTopSpecialArea();
                        if (e.isEliminated()) {
                            extraA = extraA - cellN.getNumberOfPointsAInTopLeftQuarterArea();
                        }
                        if (e1.isEliminated()) {
                            extraA = extraA - cellN.getNumberOfPointsAInTopRightQuarterArea();
                        }

                    } else {
                        extraB = extraB + cellN.getNumberOfPointsBInTopSpecialArea();
                        if (e.isEliminated()) {
                            extraB = extraB - cellN.getNumberOfPointsBInTopLeftQuarterArea();
                        }
                        if (e1.isEliminated()) {
                            extraB = extraB - cellN.getNumberOfPointsBInTopRightQuarterArea();
                        }
                    }
                }


                if (hasLeft(cellID)) {
                    Cell cellN = getCell((cellID - 1l));
                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound()).getEdge(cellN, cell);
                    Edge e1 = getAgreements(cellID, cell.getRectangle().getUpperLeftBound()).getEdge(cellN, cell);

                    if (e.getTypeSet().equals(TypeSet.A)) {
                        extraA = extraA + cellN.getNumberOfPointsAInRightSpecialArea();
                        if (e.isEliminated()) {
                            extraA = extraA - cellN.getNumberOfPointsAInBottomRightQuarterArea();
                        }
                        if (e1.isEliminated()) {
                            extraA = extraA - cellN.getNumberOfPointsAInTopRightQuarterArea();
                        }

                    } else {
                        extraB = extraB + cellN.getNumberOfPointsBInRightSpecialArea();
                        if (e.isEliminated()) {
                            extraB = extraB - cellN.getNumberOfPointsBInBottomRightQuarterArea();
                        }
                        if (e1.isEliminated()) {
                            extraB = extraB - cellN.getNumberOfPointsBInTopRightQuarterArea();
                        }
                    }
                }

                if (hasRight(cellID)) {
                    Cell cellN = getCell(cellID + 1l);
                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound()).getEdge(cellN, cell);
                    Edge e1 = getAgreements(cellID, cell.getRectangle().getLowerRightBound()).getEdge(cellN, cell);

                    if (e.getTypeSet().equals(TypeSet.A)) {
                        extraA = extraA + cellN.getNumberOfPointsAInLeftSpecialArea();
                        if (e.isEliminated()) {
                            extraA = extraA - cellN.getNumberOfPointsAInTopLeftQuarterArea();
                        }
                        if (e1.isEliminated()) {
                            extraA = extraA - cellN.getNumberOfPointsAInBottomLeftQuarterArea();
                        }

                    } else {
                        extraB = extraB + cellN.getNumberOfPointsBInLeftSpecialArea();
                        if (e.isEliminated()) {
                            extraB = extraB - cellN.getNumberOfPointsBInTopLeftQuarterArea();
                        }
                        if (e1.isEliminated()) {
                            extraB = extraB - cellN.getNumberOfPointsBInBottomLeftQuarterArea();
                        }
                    }
                }


                if (hasTop(cellID)) {
                    Cell cellN = getCell((cellID + cellsInXAxis));
                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound()).getEdge(cellN, cell);
                    Edge e1 = getAgreements(cellID, cell.getRectangle().getUpperLeftBound()).getEdge(cellN, cell);

                    if (e.getTypeSet().equals(TypeSet.A)) {
                        extraA = extraA + cellN.getNumberOfPointsAInBottomSpecialArea();
                        if (e.isEliminated()) {
                            extraA = extraA - cellN.getNumberOfPointsAInBottomRightQuarterArea();
                        }
                        if (e1.isEliminated()) {
                            extraA = extraA - cellN.getNumberOfPointsAInBottomLeftQuarterArea();
                        }

                    } else {
                        extraB = extraB + cellN.getNumberOfPointsBInBottomSpecialArea();
                        if (e.isEliminated()) {
                            extraB = extraB - cellN.getNumberOfPointsBInBottomRightQuarterArea();
                        }
                        if (e1.isEliminated()) {
                            extraB = extraB - cellN.getNumberOfPointsBInBottomLeftQuarterArea();
                        }
                    }
                }

                if ((cell.getNumberOfPointsAType() + extraA) != 0 && (cell.getNumberOfPointsBType() + extraB) != 0) {
                    cellsWithCosts.put(cellID, (cell.getNumberOfPointsAType() + extraA) * cell.getNumberOfPointsBType() + extraB);
                }
            }
        });
        return cellsWithCosts;
    }

    public List<Pair<Long, Long>> getCellsIds(Rectangle rectangle){

        if(Double.compare(rectangle.getLowerBound().getX(),this.rectangle.getLowerBound().getX()) == -1
                || Double.compare(rectangle.getLowerBound().getY(),this.rectangle.getLowerBound().getY()) == -1
                || Double.compare(rectangle.getUpperBound().getX(),this.rectangle.getUpperBound().getX()) != -1
                || Double.compare(rectangle.getUpperBound().getY(),this.rectangle.getUpperBound().getY()) != -1){
            try {
                throw new Exception("The given rectangle in not wholly included in the grid space");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Pair<Long, Long>> ranges = new ArrayList<>();

        long bottomLeft = getCellId(rectangle.getLowerBound());
        long bottomRight = getCellId(rectangle.getUpperBound().getX(), rectangle.getLowerBound().getY());
        long topRight = getCellId(rectangle.getUpperBound());

        long diff= bottomRight- bottomLeft;

        for(long i = bottomRight;i<=topRight;i=i+cellsInXAxis){
            ranges.add(new Pair<>(i-diff,i));
        }
        return ranges;
    }

    public List<Pair<Long, Long>> getCellsInHoop(Point point, int hoop){

        Pair<Long, Long> pair = getXcYc(point.getX(), point.getY());
        List<Pair<Long, Long>> results = new ArrayList<>();
        long pointXc = pair.getKey();
        long pointYc = pair.getValue();

        if(hoop<0){
            try {
                throw new Exception("Hoop should be zero or positive number");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(hoop==0){
            results.add(new Pair<>(getCellIdFromXcYc(pointXc,pointYc),getCellIdFromXcYc(pointXc,pointYc)));
            return results;
        }

        if(pointYc-hoop>=0 && pointYc-hoop<cellsInYAxis){
            //traverse xAxis lower
            long min= Long.MAX_VALUE;
            long max= Long.MIN_VALUE;

            for(long xc = pointXc-hoop;xc<=pointXc+hoop;xc=xc+1){
                if(xc>=0 && xc<cellsInXAxis){
                    if(min>xc){
                        min = xc;
                    }

                    if(max<xc){
                        max =xc;
                    }
                }
            }
//            if(pointXc-k>=0 && pointXc-k<cellsInXAxis){
//                System.out.println("IN"+ (pointYc-k) +" "+ (pointYc-k+1));
//                results.add(new Pair<>(getCellIdFromXcYc(min,pointYc-k), getCellIdFromXcYc(min, pointYc-k+1)));
//            }else{
                results.add(new Pair<>(getCellIdFromXcYc(min,pointYc-hoop), getCellIdFromXcYc(max, pointYc-hoop)));
//            }
        }

//        if(pointXc-k>=0 && pointXc-k<cellsInXAxis
//                && pointXc+k>=0 && pointXc+k<cellsInXAxis ){
//            for(long yc=pointYc-k+1;yc<=pointYc+k-2;yc=yc+1){
//                results.add(new Pair<>(getCellIdFromXcYc(pointXc+k,yc), getCellIdFromXcYc(pointXc-k,yc+1)));
//            }
//
//        }
        /*else*/ if(pointXc-hoop>=0 && pointXc-hoop<cellsInXAxis){
            for(long yc=pointYc-hoop+1;yc<=pointYc+hoop-1;yc=yc+1){
                if(yc>=0 && yc<cellsInYAxis) {
                    results.add(new Pair<>(getCellIdFromXcYc(pointXc - hoop, yc), getCellIdFromXcYc(pointXc - hoop, yc)));
                }
            }
        }/*else*/ if(pointXc+hoop>=0 && pointXc+hoop<cellsInXAxis){
            for(long yc=pointYc-hoop+1;yc<=pointYc+hoop-1;yc=yc+1){
                if(yc>=0 && yc<cellsInYAxis) {
                    results.add(new Pair<>(getCellIdFromXcYc(pointXc + hoop, yc), getCellIdFromXcYc(pointXc + hoop, yc)));
                }
            }
        }

        if(pointYc+hoop>=0 && pointYc+hoop<cellsInYAxis){
            //traverse xAxis upper
            long min= Long.MAX_VALUE;
            long max= Long.MIN_VALUE;

            for(long xc = pointXc-hoop;xc<=pointXc+hoop;xc=xc+1){
                if(xc>=0 && xc<cellsInXAxis){
                    if(min>xc){
                        min = xc;
                    }

                    if(max<xc){
                        max =xc;
                    }
                }
            }
//            if(pointXc+k>=0 && pointXc+k<cellsInXAxis){
//                results.add(new Pair<>(getCellIdFromXcYc(max,pointYc+k-1), getCellIdFromXcYc(max, pointYc+k)));
//            }else{
                results.add(new Pair<>(getCellIdFromXcYc(min,pointYc+hoop), getCellIdFromXcYc(max, pointYc+hoop)));
            //}
        }
        return results;
    }

}
