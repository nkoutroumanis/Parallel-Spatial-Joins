package gr.ds.unipi.grid;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreements;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Position;
import gr.ds.unipi.shapes.Rectangle;
import gr.ds.unipi.shapes.Circle;

import java.util.*;
import java.util.function.BiConsumer;

import static gr.ds.unipi.shapes.Position.*;

public class Grid {

    private final Rectangle rectangle;
    private final int cellsInXAxis;
    private final int cellsInYAxis;

    private final double x;
    private final double y;

    private HashMap<Integer, Cell> cells;
    private HashMap<Integer, Agreements> agreements;
//    private ConcurrentHashMap<Integer, Cell> cells;

//    private ConcurrentHashMap<Point, Agreements> agreements;
    private final double r;
    private Function4<Cell, Cell, Point, Agreement> function;
//    private ReplicationType function;

//    private final boolean geoData;

//    private final Function4<Cell, Double, Double, Double, Position> getPositionFunction;

    private Grid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/Function4<Cell, Cell, Point, Agreement> function, boolean geoData){
        this.rectangle = rectangle;
        this.cellsInXAxis = cellsInXAxis;
        this.cellsInYAxis = cellsInYAxis;
        this.r = r;

//        this.geoData = geoData;
//        if(geoData){
//            getPositionFunction = (Function4<Cell, Double, Double, Double, Position> & Serializable) Cell::getGeoPosition;
//        }else{
//            getPositionFunction = (Function4<Cell, Double, Double, Double, Position> & Serializable) Cell::getPosition;
//        }

        if(Double.compare(r,0) != 1){
            try {
                throw new Exception("r should be greater than 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        x = (rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX()) / cellsInXAxis;
        y = (rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY()) / cellsInYAxis;
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
        cells = new HashMap<>();
        agreements = new HashMap<>();
//        cells = Collections.synchronizedMap(new HashMap<>());
//        agreements = Collections.synchronizedMap(new HashMap<>());
//        cells = new ConcurrentHashMap<>(16, 0.75f, 2);
//        agreements = new ConcurrentHashMap<>(16, 0.75f, 2);

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

//    public void initialize(){
////        cells = new HashMap<>();
////        agreements = new HashMap<>();
//    cells = new ConcurrentHashMap<>(16, 0.75f, 2);
//    agreements = new ConcurrentHashMap<>(16, 0.75f, 2);
//    }


    public void load(){
        long t1 = System.currentTimeMillis();

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

//        HashMap<Integer,Cell> copyHashMapCells = new HashMap<>(cells);

        Iterator<Map.Entry<Integer, Cell>> o = new HashMap(cells).entrySet()/*.parallelStream()*/.iterator();

        while(o.hasNext()) {
            Map.Entry<Integer, Cell> entry = o.next();
            int cellID = entry.getKey();
            Cell cell = entry.getValue();
            getAgreements(cellID, cell.getRectangle().getLowerBound(), 0);
            getAgreements(cellID, cell.getRectangle().getLowerRightBound(),1);
            getAgreements(cellID, cell.getRectangle().getUpperLeftBound(),2);
            getAgreements(cellID, cell.getRectangle().getUpperBound(), 3);
        }
//        function = NewFunc.datasetA;

        System.out.println("Aggrements size was"+agreements.size());
        Iterator o1 = new HashMap(agreements).entrySet()/*.parallelStream()*/.iterator();
        while (o1.hasNext()) {
            Map.Entry<Integer, Agreements> e = (Map.Entry<Integer, Agreements>) o1.next();
            if(e.getValue().haveAllSameType()){
                if(e.getValue().hasTypeA()){
                    agreements.remove(e.getKey());
                }else if(e.getValue().hasTypeB()){
                    agreements.replace(e.getKey(), null);
                }else{
                    try {
                        throw new Exception("Must have type A or B");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        System.out.println("Aggrements size is"+agreements.size());
        cells.clear();


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
        System.out.println("Time for agreements: "+(System.currentTimeMillis()-t1)/1000);

    }

    public void printInfo(){
        System.out.println("Number of cells: "+cells.size());
    }
    public void clean(){
        agreements.clear();
    }

//    public String toString(){
//        StringBuilder sb = new StringBuilder();
//
////        if(!edges) {
////            forEach((id, rec) -> {
////                sb.append(id + " " + rec.toString() + " HashCode: " + rec.hashCode() + " - #A: " + getCell(id).getNumberOfPointsAType() + " #B:" + getCell(id).getNumberOfPointsBType() + "\n");
////            });
////        }else{
//            forEach((id, rec) -> {
//                sb.append(id + " " + rec.toString() + " HashCode: " + rec.hashCode() + " - #A: " + getCell(id).getNumberOfPointsAType() + " #B:" + getCell(id).getNumberOfPointsBType() + "\n");
//
//                if(agreements.containsKey(rec.getLowerBound())){
//                    sb.append(agreements.get(rec.getLowerBound()).toString(cellToId)).append("\n");
//                }
//
//                Point p1 = Point.newPoint(rec.getUpperBound().getX(), rec.getLowerBound().getY());
//                if(agreements.containsKey(p1)){
//                    sb.append(agreements.get(p1).toString(cellToId)).append("\n");
//                }
//
//                Point p2 = Point.newPoint(rec.getLowerBound().getX(), rec.getUpperBound().getY());
//                if(agreements.containsKey(p2)){
//                    sb.append(agreements.get(p2).toString(cellToId)).append("\n");
//                }
//
//                if(agreements.containsKey(rec.getUpperBound())){
//                    sb.append(agreements.get(rec.getUpperBound()).toString(cellToId)).append("\n");
//                }
//            });
////        }
//        return sb.toString();
//    }

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

        private Rectangle getRectangle(int cellId){

        int yc = cellId/cellsInXAxis;
        int xc = cellId - (yc * cellsInXAxis);

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

        public int cellToId(Cell cell){
            return getCellId((cell.getRectangle().getLowerBound().getX()+cell.getRectangle().getUpperBound().getX())/2,(cell.getRectangle().getLowerBound().getY()+cell.getRectangle().getUpperBound().getY())/2);
        }

//    private Function<Cell,Long> cellToId = (Function<Cell, Long> & Serializable) (c)-> getCellId((c.getRectangle().getLowerBound().getX()+c.getRectangle().getUpperBound().getX())/2,(c.getRectangle().getLowerBound().getY()+c.getRectangle().getUpperBound().getY())/2);

    public int getCellId(double x, double y) {

        int xc = (int) ((x-rectangle.getLowerBound().getX()) / this.x);
        int yc = (int) ((y-rectangle.getLowerBound().getY()) / this.y);

        return (xc + (yc * cellsInXAxis));
    }

    private int getCellId(Point point) {
        return getCellId(point.getX(), point.getY());
    }

    private Cell getCell(double x, double y){
        return getCell(getCellId(x, y));
    }

    private Cell getCell(int cellID){
//        if(cells.containsKey(cellID)){
//            return cells.get(cellID);
//        }else{
////            return getCellSingleThread(cellID);
//            return Cell.newCell(getRectangle(cellID));
//            cells.put(cellID, cell);
//            return cell;
            return cells.computeIfAbsent(cellID, (c)->{return Cell.newCell(getRectangle(c));});
//        }
    }

    private Cell getCellInExecutor(int cellID){
//        if(cells.containsKey(cellID)){
//            return cells.get(cellID);
//        }else{
//            return getCellSingleThread(cellID);
            return Cell.newCell(getRectangle(cellID));
//        }
    }

//    public Function<Long, Cell> mappingCell = (Function<Long, Cell>  & Serializable) (cellID) -> {
//        return Cell.newCell(getRectangle(cellID));
//    };

//    private /*synchronized*/ Cell getCellSingleThread(long cellID){
////        if(cells.containsKey(cellID)){
////            return cells.get(cellID);
////        }else{
//            Cell cell = Cell.newCell(getRectangle(cellID));
//            cells.putIfAbsent(cellID, cell);
//            return cell;
////        }
//    }

    private boolean hasTop(int cellID){
        if((cellID + cellsInXAxis)< (cellsInXAxis*cellsInYAxis)){
            return true;
        }
        return false;
    }

    private boolean hasBottom(int cellID){
        if((cellID - cellsInXAxis)>= 0){
            return true;
        }
        return false;

    }

    private boolean hasLeft(int cellID){
        if(((cellID -1) >= 0) && ((cellID - 1)%(cellsInXAxis)) != (cellsInXAxis-1)){
            return true;
        }
        return false;
    }

    private boolean hasRight(int cellID){
        if((cellID +1) < (cellsInXAxis*cellsInYAxis) && ((cellID +1)%cellsInXAxis) !=0){
            return true;
        }
        return false;
    }

    private Agreements getAgreements(int cellID, Point point, int index){

        return agreements.computeIfAbsent(getAggID(cellID, index), (p)->{

                    List<Cell> cells = new ArrayList<>();

//                    if (getCell(cellID).getRectangle().getLowerBound().equals(point)) {
//                        if (hasBottom(cellID) && hasLeft(cellID)) {
//                            cells.add(getCell((cellID - cellsInXAxis) - 1));
//                        }
//                        if (hasBottom(cellID)) {
//                            cells.add(getCell((cellID - cellsInXAxis)));
//                        }
//                        if (hasLeft(cellID)) {
//                            cells.add(getCell((cellID - 1)));
//                        }
//                        cells.add(getCell(cellID));
//
//
//                    } else if (getCell(cellID).getRectangle().getUpperBound().equals(point)) {
//                        cells.add(getCell(cellID));
//                        if (hasRight(cellID)) {
//                            cells.add(getCell(cellID + 1));
//                        }
//                        if (hasTop(cellID)) {
//                            cells.add(getCell((cellID + cellsInXAxis)));
//                        }
//                        if (hasTop(cellID) && hasRight(cellID)) {
//                            cells.add(getCell((cellID + cellsInXAxis) + 1));
//                        }
//
//                    } else if (Double.compare(getCell(cellID).getRectangle().getLowerBound().getX(), point.getX()) == 0 &&
//                            Double.compare(getCell(cellID).getRectangle().getUpperBound().getY(), point.getY()) == 0) {
//                        if (hasLeft(cellID)) {
//                            cells.add(getCell((cellID - 1)));
//                        }
//                        cells.add(getCell(cellID));
//                        if (hasTop(cellID) && hasLeft(cellID)) {
//                            cells.add(getCell((cellID + cellsInXAxis) - 1));
//                        }
//
//                        if (hasTop(cellID)) {
//                            cells.add(getCell((cellID + cellsInXAxis)));
//                        }
//
//                    } else if (Double.compare(getCell(cellID).getRectangle().getUpperBound().getX(), point.getX()) == 0 &&
//                            Double.compare(getCell(cellID).getRectangle().getLowerBound().getY(), point.getY()) == 0) {
//                        if (hasBottom(cellID)) {
//                            cells.add(getCell((cellID - cellsInXAxis)));
//                        }
//                        if (hasBottom(cellID) && hasRight(cellID)) {
//                            cells.add(getCell((cellID - cellsInXAxis) + 1));
//                        }
//                        cells.add(getCell(cellID));
//                        if (hasRight(cellID)) {
//                            cells.add(getCell(cellID + 1));
//                        }
//
//                    } else {
//                        try {
//                            throw new Exception("Problem in determining the cell's corner for Agreements creation.");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                switch (index){
                    case 0:
                        if (hasBottom(cellID) && hasLeft(cellID)) {
                            cells.add(getCell((cellID - cellsInXAxis) - 1));
                        }
                        if (hasBottom(cellID)) {
                            cells.add(getCell((cellID - cellsInXAxis)));
                        }
                        if (hasLeft(cellID)) {
                            cells.add(getCell((cellID - 1)));
                        }
                        cells.add(getCell(cellID));
                        break;
                    case 1:
                        if (hasBottom(cellID)) {
                            cells.add(getCell((cellID - cellsInXAxis)));
                        }
                        if (hasBottom(cellID) && hasRight(cellID)) {
                            cells.add(getCell((cellID - cellsInXAxis) + 1));
                        }
                        cells.add(getCell(cellID));
                        if (hasRight(cellID)) {
                            cells.add(getCell(cellID + 1));
                        }
                        break;
                    case 2:
                        if (hasLeft(cellID)) {
                            cells.add(getCell((cellID - 1)));
                        }
                        cells.add(getCell(cellID));
                        if (hasTop(cellID) && hasLeft(cellID)) {
                            cells.add(getCell((cellID + cellsInXAxis) - 1));
                        }
                        if (hasTop(cellID)) {
                            cells.add(getCell((cellID + cellsInXAxis)));
                        }
                        break;
                    case 3:
                        cells.add(getCell(cellID));
                        if (hasRight(cellID)) {
                            cells.add(getCell(cellID + 1));
                        }
                        if (hasTop(cellID)) {
                            cells.add(getCell((cellID + cellsInXAxis)));
                        }
                        if (hasTop(cellID) && hasRight(cellID)) {
                            cells.add(getCell((cellID + cellsInXAxis) + 1));
                        }
                        break;
                    default:
                        try {
                            throw new Exception("");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                }

                    int[] cellsIdReference;
                    if(cells.size()==4){
                        cellsIdReference = new int[2];
                        cellsIdReference[0] = cellToId(cells.get(0));
                        cellsIdReference[1] = cellToId(cells.get(2));
                    }else if(cells.size()==2){
                        cellsIdReference = new int[2];
                        cellsIdReference[0] = cellToId(cells.get(0));
                        cellsIdReference[1] = cellToId(cells.get(1));
                    }else if(cells.size()==1){
                        cellsIdReference = new int[1];
                        cellsIdReference[0] = cellToId(cells.get(0));
                    }
                    else{
                        try {
                            throw new Exception("Cell size list should have at least one element.");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    Agreements aggr = Agreements.newAgreements(cells.size(), cellsIdReference/*cells.stream().mapToInt(this::cellToId).toArray()*/, cells, point, function);
                    if(!aggr.haveAllSameType()) {
                        aggr.createEdges();
                    }
                    return  aggr;
                });
    }

//    private Agreements getAgreements(int cellID, Point point, int index){
////            if (agreements.containsKey(point)) {
////                return agreements.get(point);
////            } else {
//
//////                return getAgreementsSingleThread(cellID, point);
////                List<Cell> cells = new ArrayList<>();
////
////                switch (index){
////                    case 0:
////                        if (hasBottom(cellID) && hasLeft(cellID)) {
////                            cells.add(getCell((cellID - cellsInXAxis) - 1));
////                        }
////                        if (hasBottom(cellID)) {
////                            cells.add(getCell((cellID - cellsInXAxis)));
////                        }
////                        if (hasLeft(cellID)) {
////                            cells.add(getCell((cellID - 1)));
////                        }
////                        cells.add(getCell(cellID));
////                        break;
////                    case 1:
////                        if (hasBottom(cellID)) {
////                            cells.add(getCell((cellID - cellsInXAxis)));
////                        }
////                        if (hasBottom(cellID) && hasRight(cellID)) {
////                            cells.add(getCell((cellID - cellsInXAxis) + 1));
////                        }
////                        cells.add(getCell(cellID));
////                        if (hasRight(cellID)) {
////                            cells.add(getCell(cellID + 1));
////                        }
////                        break;
////                    case 2:
////                        if (hasLeft(cellID)) {
////                            cells.add(getCell((cellID - 1)));
////                        }
////                        cells.add(getCell(cellID));
////                        if (hasTop(cellID) && hasLeft(cellID)) {
////                            cells.add(getCell((cellID + cellsInXAxis) - 1));
////                        }
////
////                        if (hasTop(cellID)) {
////                            cells.add(getCell((cellID + cellsInXAxis)));
////                        }
////                        break;
////                    case 3:
////                        cells.add(getCell(cellID));
////                        if (hasRight(cellID)) {
////                            cells.add(getCell(cellID + 1));
////                        }
////                        if (hasTop(cellID)) {
////                            cells.add(getCell((cellID + cellsInXAxis)));
////                        }
////                        if (hasTop(cellID) && hasRight(cellID)) {
////                            cells.add(getCell((cellID + cellsInXAxis) + 1));
////                        }
////                        break;
////                    default:
////                        try {
////                            throw new Exception("");
////                        } catch (Exception e) {
////                            throw new RuntimeException(e);
////                        }
////                }
////
////                Agreements<Cell> agreements = Agreements.newAgreements(cells, point, function);
////                this.agreements.put(point, agreements);
////                return agreements;
//
//        return agreements.computeIfAbsent(point, (p)->{
//
//            List<Cell> cells = new ArrayList<>();
//
//            switch (index){
//                case 0:
//                    if (hasBottom(cellID) && hasLeft(cellID)) {
//                        cells.add(getCell((cellID - cellsInXAxis) - 1));
//                    }
//                    if (hasBottom(cellID)) {
//                        cells.add(getCell((cellID - cellsInXAxis)));
//                    }
//                    if (hasLeft(cellID)) {
//                        cells.add(getCell((cellID - 1)));
//                    }
//                    cells.add(getCell(cellID));
//                    break;
//                case 1:
//                    if (hasBottom(cellID)) {
//                        cells.add(getCell((cellID - cellsInXAxis)));
//                    }
//                    if (hasBottom(cellID) && hasRight(cellID)) {
//                        cells.add(getCell((cellID - cellsInXAxis) + 1));
//                    }
//                    cells.add(getCell(cellID));
//                    if (hasRight(cellID)) {
//                        cells.add(getCell(cellID + 1));
//                    }
//                    break;
//                case 2:
//                    if (hasLeft(cellID)) {
//                        cells.add(getCell((cellID - 1)));
//                    }
//                    cells.add(getCell(cellID));
//                    if (hasTop(cellID) && hasLeft(cellID)) {
//                        cells.add(getCell((cellID + cellsInXAxis) - 1));
//                    }
//
//                    if (hasTop(cellID)) {
//                        cells.add(getCell((cellID + cellsInXAxis)));
//                    }
//                    break;
//                case 3:
//                    cells.add(getCell(cellID));
//                    if (hasRight(cellID)) {
//                        cells.add(getCell(cellID + 1));
//                    }
//                    if (hasTop(cellID)) {
//                        cells.add(getCell((cellID + cellsInXAxis)));
//                    }
//                    if (hasTop(cellID) && hasRight(cellID)) {
//                        cells.add(getCell((cellID + cellsInXAxis) + 1));
//                    }
//                    break;
//                default:
//                    try {
//                        throw new Exception("");
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//            }
//
//            int[] cellsIdReference;
//            if(cells.size()==4){
//                cellsIdReference = new int[2];
//                cellsIdReference[0] = cellToId(cells.get(0));
//                cellsIdReference[1] = cellToId(cells.get(2));
//            }else if(cells.size()==2){
//                cellsIdReference = new int[2];
//                cellsIdReference[0] = cellToId(cells.get(0));
//                cellsIdReference[1] = cellToId(cells.get(1));
//            }else if(cells.size()==1){
//                cellsIdReference = new int[1];
//                cellsIdReference[0] = cellToId(cells.get(0));
//            }
//            else{
//                try {
//                    throw new Exception("Cell size list should have at least one element.");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            Agreements aggr = Agreements.newAgreements(cells.size(), cellsIdReference/*cells.stream().mapToInt(this::cellToId).toArray()*/, cells, point, function);
//            aggr.createEdges();
//            return aggr;
//        });
////            }
//    }

//    private /*synchronized*/ Agreements getAgreementsSingleThread(int cellID, Point point){;
////                if (agreements.containsKey(point)) {
////            return agreements.get(point);
////        }else {
//
////            boolean bottomLeft = false;
////            boolean bottomRight = false;
////            boolean topLeft = false;
////            boolean topRight = false;
//            List<Cell> cells = new ArrayList<>();
//
//            if (getCell(cellID).getRectangle().getLowerBound().equals(point)) {
////                bottomLeft = true;
//                if (hasBottom(cellID) && hasLeft(cellID)) {
//                    cells.add(getCell((cellID - cellsInXAxis) - 1));
//                }
//                if (hasBottom(cellID)) {
//                    cells.add(getCell((cellID - cellsInXAxis)));
//                }
//                if (hasLeft(cellID)) {
//                    cells.add(getCell((cellID - 1)));
//                }
//                cells.add(getCell(cellID));
//
//
//            } else if (getCell(cellID).getRectangle().getUpperBound().equals(point)) {
////                topRight = true;
//                cells.add(getCell(cellID));
//                if (hasRight(cellID)) {
//                    cells.add(getCell(cellID + 1));
//                }
//                if (hasTop(cellID)) {
//                    cells.add(getCell((cellID + cellsInXAxis)));
//                }
//                if (hasTop(cellID) && hasRight(cellID)) {
//                    cells.add(getCell((cellID + cellsInXAxis) + 1));
//                }
//
//            } else if (Double.compare(getCell(cellID).getRectangle().getLowerBound().getX(), point.getX()) == 0 &&
//                    Double.compare(getCell(cellID).getRectangle().getUpperBound().getY(), point.getY()) == 0) {
////                topLeft = true;
//                if (hasLeft(cellID)) {
//                    cells.add(getCell((cellID - 1)));
//                }
//                cells.add(getCell(cellID));
//                if (hasTop(cellID) && hasLeft(cellID)) {
//                    cells.add(getCell((cellID + cellsInXAxis) - 1));
//                }
//
//                if (hasTop(cellID)) {
//                    cells.add(getCell((cellID + cellsInXAxis)));
//                }
//
//            } else if (Double.compare(getCell(cellID).getRectangle().getUpperBound().getX(), point.getX()) == 0 &&
//                    Double.compare(getCell(cellID).getRectangle().getLowerBound().getY(), point.getY()) == 0) {
////                bottomRight = true;
//                if (hasBottom(cellID)) {
//                    cells.add(getCell((cellID - cellsInXAxis)));
//                }
//                if (hasBottom(cellID) && hasRight(cellID)) {
//                    cells.add(getCell((cellID - cellsInXAxis) + 1));
//                }
//                cells.add(getCell(cellID));
//                if (hasRight(cellID)) {
//                    cells.add(getCell(cellID + 1));
//                }
//
//            } else {
//                try {
//                    throw new Exception("Problem in determining the cell's corner for Agreements creation.");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
////            List<Cell> cells = new ArrayList<>();
////            if (bottomLeft && hasBottom(cellID) && hasLeft(cellID)) {
////                cells.add(getCell((cellID - cellsInXAxis) - 1l));
////            }
////
////            if ((bottomLeft || bottomRight) && hasBottom(cellID)) {
////                cells.add(getCell((cellID - cellsInXAxis)));
////            }
////
////            if (bottomRight && hasBottom(cellID) && hasRight(cellID)) {
////                cells.add(getCell((cellID - cellsInXAxis) + 1l));
////            }
////
////            if ((bottomLeft || topLeft) && hasLeft(cellID)) {
////                cells.add(getCell((cellID - 1l)));
////            }
////
////            //add plain here
////            cells.add(getCell(cellID));
////
////            if ((bottomRight || topRight) && hasRight(cellID)) {
////                cells.add(getCell(cellID + 1l));
////            }
////
////            if (topLeft && hasTop(cellID) && hasLeft(cellID)) {
////                cells.add(getCell((cellID + cellsInXAxis) - 1l));
////            }
////
////            if ((topLeft || topRight) && hasTop(cellID)) {
////                cells.add(getCell((cellID + cellsInXAxis)));
////            }
////
////            if (topRight && hasTop(cellID) && hasRight(cellID)) {
////                cells.add(getCell((cellID + cellsInXAxis) + 1l));
////            }
//
//
//        int[] cellsIdReference;
//        if(cells.size()==4){
//            cellsIdReference = new int[2];
//            cellsIdReference[0] = cellToId(cells.get(0));
//            cellsIdReference[1] = cellToId(cells.get(2));
//        }else if(cells.size()==2){
//            cellsIdReference = new int[2];
//            cellsIdReference[0] = cellToId(cells.get(0));
//            cellsIdReference[1] = cellToId(cells.get(1));
//        }else if(cells.size()==1){
//            cellsIdReference = new int[1];
//            cellsIdReference[0] = cellToId(cells.get(0));
//        }
//        else{
//            try {
//                throw new Exception("Cell size list should have at least one element.");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//            Agreements agreements = Agreements.newAgreements(cells.size(), cellsIdReference, /*cells.stream().mapToInt(this::cellToId).toArray(),*/ cells, point, function);
//            agreements.createEdges();
//            this.agreements.putIfAbsent(point, agreements);
//            return agreements;
////        }
//    }

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

    private void forEach(BiConsumer<Integer,Rectangle> action){
        if (action == null) {
            throw new NullPointerException();
        }
        for(int i=0;i<cellsInXAxis*cellsInYAxis;i++){
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
        cell.addPointToDataset(typeSet, cell.getPosition(x,y,r));//getPositionFunction.apply(cell,x,y,r));
        //getCell(x, y).addPointToDataset(x,y,r,typeSet);
    }

//    public int[] getPartitionsAType(double x, double y){
//        return getPartitions(x, y, TypeSet.A);
//    }
//
//    public int[] getPartitionsBType(double x, double y){
//        return getPartitions(x, y, TypeSet.B);
//    }

    public int[] getPartitionsATypeInExecutor(double x, double y){
        return getPartitionsInExecutor(x, y, TypeSet.A);
    }

    public int[] getPartitionsBTypeInExecutor(double x, double y){
        return getPartitionsInExecutor(x, y, TypeSet.B);
    }

//    private List<Cell> getPartitions(Cell cell, long cellID, Agreements<Cell> agreements, Position position, TypeSet typeSet){
//        List<Cell> list;
//        switch(position){
//            case TOP:
//                list = agreements.getPartitionsTwoSpacesCase(cell, getCell((cellID + cellsInXAxis)), typeSet);
//                break;
//            case BOTTOM:
//                list = agreements.getPartitionsTwoSpacesCase(cell, getCell((cellID - cellsInXAxis)), typeSet);
//                break;
//            case RIGHT:
//                list = agreements.getPartitionsTwoSpacesCase(cell, getCell((cellID  + 1)), typeSet);
//                break;
//            case LEFT:
//                list = agreements.getPartitionsTwoSpacesCase(cell, getCell((cellID  - 1)), typeSet);
//                break;
//            default:
//                throw new IllegalStateException("Position was not caught.");
//        }
//        return list;
//    }

//    private void getPartitions(int cellID, Agreements agreements, Position position, TypeSet typeSet, List<Cell> partitions){
//        switch(position){
//            case TOP:
//                agreements.getPartitionsTwoSpacesCase(getCell((cellID + cellsInXAxis)), typeSet, partitions);
//                break;
//            case BOTTOM:
//                agreements.getPartitionsTwoSpacesCase( getCell((cellID - cellsInXAxis)), typeSet, partitions);
//                break;
//            case RIGHT:
//                agreements.getPartitionsTwoSpacesCase(getCell((cellID  + 1)), typeSet, partitions);
//                break;
//            case LEFT:
//                agreements.getPartitionsTwoSpacesCase(getCell((cellID  - 1)), typeSet, partitions);
//                break;
//            default:
//                throw new IllegalStateException("Position was not caught.");
//        }
//    }

    private void getPartitions(int cellID, Agreements agreements, Position position, TypeSet typeSet, List<Integer> partitions){
        switch(position){
            case TOP:
                agreements.getPartitionsTwoSpacesCase((cellID + cellsInXAxis), typeSet, partitions);
                break;
            case BOTTOM:
                agreements.getPartitionsTwoSpacesCase((cellID - cellsInXAxis), typeSet, partitions);
                break;
            case RIGHT:
                agreements.getPartitionsTwoSpacesCase((cellID  + 1), typeSet, partitions);
                break;
            case LEFT:
                agreements.getPartitionsTwoSpacesCase((cellID  - 1), typeSet, partitions);
                break;
            default:
                throw new IllegalStateException("Position was not caught.");
        }
    }

//    private List<Cell> getPartitions(Cell cell, long cellId, Agreements<Cell> agreements, Position position, TypeSet typeSet, boolean outerQuarter){
//        switch (position){
//            case TOPRIGHT:
//                if(hasTop(cellId) && hasRight(cellId)){
//                    return agreements.getPartitionsFourSpacesCase(cell, typeSet, outerQuarter);
//                }else if(hasTop(cellId)){
//                    return getPartitions(cell, cellId, agreements, TOP, typeSet);
//                }else if(hasRight(cellId)){
//                    return getPartitions(cell, cellId, agreements, RIGHT, typeSet);
//                }
//                break;
//            case TOPLEFT:
//                if(hasTop(cellId) && hasLeft(cellId)){
//                    return agreements.getPartitionsFourSpacesCase(cell, typeSet, outerQuarter);
//                }else if(hasTop(cellId)){
//                    return getPartitions(cell, cellId, agreements, TOP, typeSet);
//                }else if(hasLeft(cellId)){
//                    return getPartitions(cell, cellId, agreements, LEFT, typeSet);
//                }
//                break;
//            case BOTTOMRIGHT:
//                if(hasBottom(cellId) && hasRight(cellId)){
//                    return agreements.getPartitionsFourSpacesCase(cell, typeSet, outerQuarter);
//                }else if(hasBottom(cellId)){
//                    return getPartitions(cell, cellId, agreements, BOTTOM, typeSet);
//                }else if(hasRight(cellId)){
//                    return getPartitions(cell, cellId, agreements, RIGHT, typeSet);
//                }
//                break;
//            case BOTTOMLEFT:
//                if(hasBottom(cellId) && hasLeft(cellId)){
//                    return agreements.getPartitionsFourSpacesCase(cell, typeSet, outerQuarter);
//                }else if(hasBottom(cellId)){
//                    return getPartitions(cell, cellId, agreements, BOTTOM, typeSet);
//                }else if(hasLeft(cellId)){
//                    return getPartitions(cell, cellId, agreements, LEFT, typeSet);
//                }
//                break;
//            default:
//                throw new IllegalStateException("Position was not caught.");
//        }
//        List<Cell> partitions = new ArrayList<>();
//        partitions.add(cell);
//        return partitions;
//        //return Collections.singletonList(cell);
//    }

//    private void getPartitions(int cellId, Agreements agreements, Position position, TypeSet typeSet, boolean outerQuarter, List<Cell> partitions){
//        switch (position){
//            case TOPRIGHT:
////                if(agreements.getSpacesNum()==4){
//                    agreements.getPartitionsFourSpacesCase(0, typeSet, outerQuarter, partitions);
////                }else if(hasTop(cellId)){
////                    getPartitions(cellId, agreements, TOP, typeSet, partitions);
////                }else if(hasRight(cellId)){
////                    getPartitions(cellId, agreements, RIGHT, typeSet, partitions);
////                }
//                break;
//            case TOPLEFT:
////                if(agreements.getSpacesNum()==4){
//                    agreements.getPartitionsFourSpacesCase(1, typeSet, outerQuarter, partitions);
////                }else if(hasTop(cellId)){
////                    getPartitions(cellId, agreements, TOP, typeSet, partitions);
////                }else if(hasLeft(cellId)){
////                    getPartitions(cellId, agreements, LEFT, typeSet, partitions);
////                }
//                break;
//            case BOTTOMRIGHT:
////                if(agreements.getSpacesNum()==4){
//                    agreements.getPartitionsFourSpacesCase(2, typeSet, outerQuarter, partitions);
////                }else if(hasBottom(cellId)){
////                    getPartitions(cellId, agreements, BOTTOM, typeSet, partitions);
////                }else if(hasRight(cellId)){
////                    getPartitions(cellId, agreements, RIGHT, typeSet, partitions);
////                }
//                break;
//            case BOTTOMLEFT:
////                if(agreements.getSpacesNum()==4){
//                    agreements.getPartitionsFourSpacesCase(3, typeSet, outerQuarter, partitions);
////                }else if(hasBottom(cellId)){
////                    getPartitions(cellId, agreements, BOTTOM, typeSet, partitions);
////                }else if(hasLeft(cellId)){
////                    getPartitions(cellId, agreements, LEFT, typeSet, partitions);
////                }
//                break;
//            default:
//                throw new IllegalStateException("Position was not caught.");
//        }
//    }

    private void getPartitions(int cellId, Agreements agreements, Position position, TypeSet typeSet, boolean outerQuarter, List<Integer> partitions){
        switch (position){
            case TOPRIGHT:
//                if(agreements.getSpacesNum()==4){
                agreements.getPartitionsFourSpacesCase(0, typeSet, outerQuarter, partitions);
//                }else if(hasTop(cellId)){
//                    getPartitions(cellId, agreements, TOP, typeSet, partitions);
//                }else if(hasRight(cellId)){
//                    getPartitions(cellId, agreements, RIGHT, typeSet, partitions);
//                }
                break;
            case TOPLEFT:
//                if(agreements.getSpacesNum()==4){
                agreements.getPartitionsFourSpacesCase(1, typeSet, outerQuarter, partitions);
//                }else if(hasTop(cellId)){
//                    getPartitions(cellId, agreements, TOP, typeSet, partitions);
//                }else if(hasLeft(cellId)){
//                    getPartitions(cellId, agreements, LEFT, typeSet, partitions);
//                }
                break;
            case BOTTOMRIGHT:
//                if(agreements.getSpacesNum()==4){
                agreements.getPartitionsFourSpacesCase(2, typeSet, outerQuarter, partitions);
//                }else if(hasBottom(cellId)){
//                    getPartitions(cellId, agreements, BOTTOM, typeSet, partitions);
//                }else if(hasRight(cellId)){
//                    getPartitions(cellId, agreements, RIGHT, typeSet, partitions);
//                }
                break;
            case BOTTOMLEFT:
//                if(agreements.getSpacesNum()==4){
                agreements.getPartitionsFourSpacesCase(3, typeSet, outerQuarter, partitions);
//                }else if(hasBottom(cellId)){
//                    getPartitions(cellId, agreements, BOTTOM, typeSet, partitions);
//                }else if(hasLeft(cellId)){
//                    getPartitions(cellId, agreements, LEFT, typeSet, partitions);
//                }
                break;
            default:
                throw new IllegalStateException("Position was not caught.");
        }
    }

//    private List<Cell> checkForExtraPartition(Cell cell, long cellId, Position position1, boolean horizontal1, Position position2, boolean horizontal2, Point point, TypeSet typeSet) {
//        List<Cell> extraCells = new ArrayList<>();
//        Optional<Cell> optional1;
//        Optional<Cell> optional2;
//        if(geoData){
//            optional1 = checkForExtraGeoPartition(cell, cellId, position1, horizontal1, point, typeSet);
//            optional2 = checkForExtraGeoPartition(cell, cellId, position2, horizontal2, point, typeSet);
//        }else{
//            optional1 = checkForExtraPartition(cell, cellId, position1, horizontal1, point, typeSet);
//            optional2 = checkForExtraPartition(cell, cellId, position2, horizontal2, point, typeSet);
//        }
//        optional1.ifPresent(extraCells::add);
//        optional2.ifPresent(extraCells::add);
//        return extraCells;
//    }

//    private void checkForExtraPartitionSuppArea(Cell cell, int cellId, Position position, boolean horizontal1, double x, double y, TypeSet typeSet, List<Cell> list) {
////        if(!geoData){
//            checkForExtraPartition(cell, cellId, position, horizontal1, x, y, typeSet, list);
////        }else{
////            //checkForExtraGeoPartition(cell, cellId, position, horizontal1, point, typeSet, list);
////        }
//    }
//
//    private void checkForExtraPartition(Cell cell, int cellId, Position position, boolean horizontal, double x, double y, TypeSet typeSet, List<Cell> partitions) {
//        int extraCell;
//        Agreements aggr = null;
//        switch (position){
//            case TOPRIGHT:
//                aggr = getAgreements(cellId, cell.getRectangle().getUpperBound(),3);
//                if(aggr.getSpacesNum() ==4){
//                    if(horizontal){
//                        extraCell = aggr.getExtraPartition(4, 1, 5, typeSet);
//                    }else{
//                        extraCell = aggr.getExtraPartition(2, 3, 5, typeSet);
//                    }
////                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
//                    if(extraCell!=-1){
//                        if(horizontal){
//                            if(Circle.newCircle(cell.getRectangle().getUpperBound().getX()-r,cell.getRectangle().getUpperBound().getY(), r).containsInclusive(x, y)){
//                                partitions.add(getCell(extraCell));
//                            }
//                        }else{
//                            if(Circle.newCircle(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getUpperBound().getY()-r, r).containsInclusive(x, y)){
//                                partitions.add(getCell(extraCell));
//                            }
//                        }
//                    }
//                }
//                break;
//            case TOPLEFT:
//                aggr = getAgreements(cellId, cell.getRectangle().getUpperLeftBound(),2);
//                if(aggr.getSpacesNum() ==4){
//                    if(horizontal){
//                        extraCell = aggr.getExtraPartition(10, 2, 7, typeSet);
//                    }else{
//                        extraCell = aggr.getExtraPartition(1, 7, 9, typeSet);
//                    }
////                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperLeftBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
//                    if(extraCell!=-1){
//                        if(horizontal){
//                            if(Circle.newCircle(cell.getRectangle().getUpperLeftBound().getX()+r,cell.getRectangle().getUpperLeftBound().getY(), r).containsInclusive(x, y)){
//                                partitions.add(getCell(extraCell));
//                            }
//                        }else{
//                            if(Circle.newCircle(cell.getRectangle().getUpperLeftBound().getX(),cell.getRectangle().getUpperLeftBound().getY()-r, r).containsInclusive(x, y)){
//                                partitions.add(getCell(extraCell));
//                            }
//                        }
//                    }
//                }
//                break;
//            case BOTTOMRIGHT:
//                aggr = getAgreements(cellId, cell.getRectangle().getLowerRightBound(),1);
//
//                if(aggr.getSpacesNum() ==4){
//                    if(horizontal){
//                        extraCell = aggr.getExtraPartition(3, 8, 11, typeSet);
//                    }else{
//                        extraCell = aggr.getExtraPartition(12, 4, 8, typeSet);
//                    }
////                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerRightBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
//                    if(extraCell!=-1){
//                        if(horizontal){
//                            if(Circle.newCircle(cell.getRectangle().getLowerRightBound().getX()-r,cell.getRectangle().getLowerRightBound().getY(), r).containsInclusive(x, y)){
//                                partitions.add(getCell(extraCell));
//                            }
//                        }else{
//                            if(Circle.newCircle(cell.getRectangle().getLowerRightBound().getX(),cell.getRectangle().getLowerRightBound().getY()+r, r).containsInclusive(x, y)){
//                                partitions.add(getCell(extraCell));
//                            }
//                        }
//                    }
//                }
//                break;
//            case BOTTOMLEFT:
//                aggr = getAgreements(cellId, cell.getRectangle().getLowerBound(),0);
//
//                if(aggr.getSpacesNum() ==4){
//                    if(horizontal){
//                        extraCell = aggr.getExtraPartition(9, 6, 12, typeSet);
//                    }else{
//                        extraCell = aggr.getExtraPartition(11, 6, 10, typeSet);
//                    }
////                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
//                    if(extraCell!=-1){
//                        if(horizontal){
//                            if(Circle.newCircle(cell.getRectangle().getLowerBound().getX()+r,cell.getRectangle().getLowerBound().getY(), r).containsInclusive(x, y)){
//                                partitions.add(getCell(extraCell));
//                            }
//                        }else{
//                            if(Circle.newCircle(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getLowerBound().getY()+r, r).containsInclusive(x, y)){
//                                partitions.add(getCell(extraCell));
//                            }
//                        }
//                    }
//                }
//                break;
//            default:
//                throw new IllegalStateException("Position was not caught.");
//        }
//    }



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

//        private Optional<Cell> checkForExtraGeoPartition(Cell cell, long cellId, Position position, boolean horizontal, Point point, TypeSet typeSet) {
//        switch (position){
//            case TOPRIGHT:
//                if(hasTop(cellId) && hasRight(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
//                    if(extraCell.isPresent()){
//                        if(horizontal){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX()-(r/(Math.cos(Math.toRadians(rectangle.getUpperBound().getY())) * 111.321)),cell.getRectangle().getUpperBound().getY()), r).containsInclusiveHaversine(point)){
//                                return extraCell;
//                            }
////                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getUpperBound().getY()), 2*r).containsInclusiveHaversine(point)){
////                                return extraCell;
////                            }
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
////                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX(),cell.getRectangle().getUpperLeftBound().getY()), 2*r).containsInclusiveHaversine(point)){
////                                return extraCell;
////                            }
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
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX(),cell.getRectangle().getLowerRightBound().getY()), 2*r).containsInclusiveHaversine(point)){
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
////                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX()+(r/(Math.cos(Math.toRadians(rectangle.getLowerBound().getY())) * 111.321)),cell.getRectangle().getLowerBound().getY()), r).containsInclusiveHaversine(point)){
////                                return extraCell;
////                            }
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getLowerBound().getY()), 2*r).containsInclusiveHaversine(point)){
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

//    private Optional<Cell> checkForExtraPartition(Cell cell, long cellId, Position position, boolean horizontal, Point point, TypeSet typeSet) {
//        switch (position){
//            case TOPRIGHT:
//                if(hasTop(cellId) && hasRight(cellId)){
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
//                    if(extraCell.isPresent()){
//                        if(horizontal){
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX()-r,cell.getRectangle().getUpperBound().getY()), r).containsInclusive(point)){
//                                return extraCell;
//                            }
//                        }else{
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperBound().getX(),cell.getRectangle().getUpperBound().getY()-r), r).containsInclusive(point)){
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
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX()+r,cell.getRectangle().getUpperLeftBound().getY()), r).containsInclusive(point)){
//                                return extraCell;
//                            }
//                        }else{
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getUpperLeftBound().getX(),cell.getRectangle().getUpperLeftBound().getY()-r), r).containsInclusive(point)){
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
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX()-r,cell.getRectangle().getLowerRightBound().getY()), r).containsInclusive(point)){
//                                return extraCell;
//                            }
//                        }else{
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerRightBound().getX(),cell.getRectangle().getLowerRightBound().getY()+r), r).containsInclusive(point)){
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
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX()+r,cell.getRectangle().getLowerBound().getY()), r).containsInclusive(point)){
//                                return extraCell;
//                            }
//                        }else{
//                            if(Circle.newCircle(Point.newPoint(cell.getRectangle().getLowerBound().getX(),cell.getRectangle().getLowerBound().getY()+r), r).containsInclusive(point)){
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

//    private int[] getPartitions(double x, double y, TypeSet typeSet){
//        return null;
////        int cellID = getCellId(x,y);
////        Cell cell = getCell(cellID);
////
////        List<Cell> partitions = new ArrayList<>();
////        partitions.add(cell);
////        Agreements aggr = null;
////        switch(cell.getPosition(x,y,r)){
////            case PLAIN:
////                break;
////            case TOPRIGHTQUARTER:
////                aggr = getAgreements(cellID, cell.getRectangle().getUpperBound(),3);
////                if(aggr.getSpacesNum()==4){
////                    getPartitions(cellID, aggr, TOPRIGHT, typeSet, false, partitions);
////                }else if(hasTop(cellID)){
////                    getPartitions(cellID, aggr, TOP, typeSet, partitions);
////                }else if(hasRight(cellID)){
////                    getPartitions(cellID, aggr, RIGHT, typeSet, partitions);
////                }
////                checkForExtraPartitionSuppArea(cell, cellID, TOPLEFT, true, x,y, typeSet, partitions);
////                checkForExtraPartitionSuppArea(cell, cellID, BOTTOMRIGHT, false, x,y, typeSet, partitions);
////                break;
////            case TOPLEFTQUARTER:
////                aggr = getAgreements(cellID, Point.newPoint(cell.getRectangle().getLowerBound().getX(), cell.getRectangle().getUpperBound().getY()),2);
////                if(aggr.getSpacesNum()==4) {
////                    getPartitions(cellID, aggr, TOPLEFT, typeSet, false, partitions);
////                }else if(hasTop(cellID)){
////                    getPartitions(cellID, aggr, TOP, typeSet, partitions);
////                }else if(hasLeft(cellID)){
////                    getPartitions(cellID, aggr, LEFT, typeSet, partitions);
////                }
////                checkForExtraPartitionSuppArea(cell, cellID, TOPRIGHT, true, x, y, typeSet, partitions);
////                checkForExtraPartitionSuppArea(cell, cellID, BOTTOMLEFT, false, x, y, typeSet, partitions);
////                break;
////            case BOTTOMRIGHTQUARTER:
////                aggr = getAgreements(cellID, Point.newPoint(cell.getRectangle().getUpperBound().getX(), cell.getRectangle().getLowerBound().getY()),1);
////                if(aggr.getSpacesNum()==4) {
////                    getPartitions(cellID, aggr, BOTTOMRIGHT, typeSet, false, partitions);
////                } else if(hasBottom(cellID)){
////                    getPartitions(cellID, aggr, BOTTOM, typeSet, partitions);
////                }else if(hasRight(cellID)){
////                    getPartitions(cellID, aggr, RIGHT, typeSet, partitions);
////                }
////                checkForExtraPartitionSuppArea(cell, cellID, BOTTOMLEFT, true, x, y, typeSet, partitions);
////                checkForExtraPartitionSuppArea(cell, cellID, TOPRIGHT, false, x, y, typeSet, partitions);
////                break;
////            case BOTTOMLEFTQUARTER:
////                aggr = getAgreements(cellID, cell.getRectangle().getLowerBound(),0);
////                if(aggr.getSpacesNum()==4) {
////                    getPartitions(cellID, aggr, BOTTOMLEFT, typeSet, false, partitions);
////                }else if(hasBottom(cellID)){
////                    getPartitions(cellID, aggr, BOTTOM, typeSet, partitions);
////                }else if(hasLeft(cellID)){
////                    getPartitions(cellID, aggr, LEFT, typeSet, partitions);
////                }
////                checkForExtraPartitionSuppArea(cell, cellID, BOTTOMRIGHT, true, x, y, typeSet, partitions);
////                checkForExtraPartitionSuppArea(cell, cellID, TOPLEFT, false, x, y, typeSet, partitions);
////                break;
////            case TOPRIGHT:
////                aggr = getAgreements(cellID, cell.getRectangle().getUpperBound(),3);
////                if(aggr.getSpacesNum()==4) {
////                    getPartitions(cellID, aggr, TOPRIGHT, typeSet, true, partitions);
////                }else if(hasTop(cellID)){
////                    getPartitions(cellID, aggr, TOP, typeSet, partitions);
////                }else if(hasRight(cellID)){
////                    getPartitions(cellID, aggr, RIGHT, typeSet, partitions);
////                }
////                checkForExtraPartition(cell, cellID, TOPLEFT, true, x, y, typeSet, partitions);
////                checkForExtraPartition(cell, cellID, BOTTOMRIGHT, false, x, y, typeSet, partitions);
////                break;
////            case TOPLEFT:
////                aggr = getAgreements(cellID, Point.newPoint(cell.getRectangle().getLowerBound().getX(), cell.getRectangle().getUpperBound().getY()),2);
////                if(aggr.getSpacesNum()==4) {
////                    getPartitions(cellID, aggr, TOPLEFT, typeSet, true, partitions);
////                }else if(hasTop(cellID)){
////                    getPartitions(cellID, aggr, TOP, typeSet, partitions);
////                }else if(hasLeft(cellID)){
////                    getPartitions(cellID, aggr, LEFT, typeSet, partitions);
////                }
////                checkForExtraPartition(cell, cellID, TOPRIGHT, true, x, y, typeSet, partitions);
////                checkForExtraPartition(cell, cellID, BOTTOMLEFT, false, x, y, typeSet, partitions);
////                break;
////            case BOTTOMRIGHT:
////                aggr = getAgreements(cellID, Point.newPoint(cell.getRectangle().getUpperBound().getX(), cell.getRectangle().getLowerBound().getY()),1);
////                if(aggr.getSpacesNum()==4) {
////                    getPartitions(cellID, aggr, BOTTOMRIGHT, typeSet, true, partitions);
////                }else if(hasBottom(cellID)){
////                    getPartitions(cellID, aggr, BOTTOM, typeSet, partitions);
////                }else if(hasRight(cellID)){
////                    getPartitions(cellID, aggr, RIGHT, typeSet, partitions);
////                }
////                checkForExtraPartition(cell, cellID, BOTTOMLEFT, true, x, y, typeSet, partitions);
////                checkForExtraPartition(cell, cellID, TOPRIGHT, false, x, y, typeSet, partitions);
////                    break;
////            case BOTTOMLEFT:
////                aggr = getAgreements(cellID, cell.getRectangle().getLowerBound(),0);
////                if(aggr.getSpacesNum()==4) {
////                    getPartitions(cellID, aggr, BOTTOMLEFT, typeSet, true, partitions);
////                }else if(hasBottom(cellID)){
////                    getPartitions(cellID, aggr, BOTTOM, typeSet, partitions);
////                }else if(hasLeft(cellID)){
////                    getPartitions(cellID, aggr, LEFT, typeSet, partitions);
////                }
////                checkForExtraPartition(cell, cellID, BOTTOMRIGHT, true, x, y, typeSet, partitions);
////                checkForExtraPartition(cell, cellID, TOPLEFT, false, x, y, typeSet, partitions);
////                    break;
////            case TOP:
////                if(hasTop(cellID)){
////                    getAgreements(cellID, cell.getRectangle().getUpperBound(),3).getPartitionsNEW(0, 2, typeSet, partitions);
////                }
////                checkForExtraPartition(cell, cellID, TOPLEFT, true, x,y, typeSet,partitions);
////                checkForExtraPartition(cell, cellID, TOPRIGHT, true, x,y, typeSet,partitions);
////                break;
////            case BOTTOM:
////                if(hasBottom(cellID)){
////                    getAgreements(cellID, cell.getRectangle().getLowerBound(),0).getPartitionsNEW(3, 1, typeSet, partitions);
////                }
////                checkForExtraPartition(cell, cellID, BOTTOMLEFT, true, x,y, typeSet,partitions);
////                checkForExtraPartition(cell, cellID, BOTTOMRIGHT, true, x,y, typeSet,partitions);
////
////                break;
////            case RIGHT:
////                if(hasRight(cellID)){
////                    getAgreements(cellID, cell.getRectangle().getUpperBound(),3).getPartitionsNEW(0, 1, typeSet, partitions);
////                }
////                checkForExtraPartition(cell, cellID, TOPRIGHT, false, x,y, typeSet,partitions);
////                checkForExtraPartition(cell, cellID, BOTTOMRIGHT, false, x,y, typeSet,partitions);
////                break;
////            case LEFT:
////                if(hasLeft(cellID)){
////                    getAgreements(cellID, cell.getRectangle().getLowerBound(),0).getPartitionsNEW(3, 2, typeSet, partitions);
////                }
////                checkForExtraPartition(cell, cellID, TOPLEFT, false, x,y, typeSet,partitions);
////                checkForExtraPartition(cell, cellID, BOTTOMLEFT, false, x,y, typeSet,partitions);
////                break;
////            default: throw new RuntimeException("No correct position");
////        }
////        return getPartitionsId(partitions);
//    }

    private String[] getPartitionsId(List<Cell> cells){
        String[] partitions = new String[cells.size()];
        for (int i = 0; i < cells.size(); i++) {
            partitions[i] = String.valueOf(cellToId(cells.get(i)));//String.valueOf(cellToId.apply(cells.get(i)));
        }
        return partitions;
    }

    public int getPartitionId(Cell cell){
        return cellToId(cell);//String.valueOf(cellToId.apply(cells.get(i)));
    }


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

    public static Grid newGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/  Function4<Cell, Cell, Point, Agreement> function){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new Grid(rectangle, cellsInXAxis, cellsInYAxis, r, function, false);
    }

    public static Grid newGrid(Rectangle rectangle, double r, Function4<Cell, Cell, Point, Agreement> function){
        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(2*r));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }
        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(2*r));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new Grid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static Grid newGridStripesX(Rectangle rectangle, double r, Function4<Cell, Cell, Point, Agreement> function){
        double cellsInXAxis = ((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/(2*r));
        if(cellsInXAxis%1 ==0){
            cellsInXAxis = cellsInXAxis - 1;
        }
        double cellsInYAxis = 1;
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new Grid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static Grid newGridStripesY(Rectangle rectangle, double r, Function4<Cell, Cell, Point, Agreement> function){
        double cellsInXAxis = 1;
        double cellsInYAxis = ((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/(2*r));
        if(cellsInYAxis%1 ==0){
            cellsInYAxis = cellsInYAxis - 1;
        }
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new Grid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function, false);
    }

    public static Grid newGeoGrid(Rectangle rectangle, int cellsInXAxis, int cellsInYAxis, double r, /*ReplicationType function*/Function4<Cell, Cell, Point, Agreement> function){
        System.out.println("Cells in X axis:"+ cellsInXAxis);
        System.out.println("Cells in Y axis:"+ cellsInYAxis);
        return new Grid(rectangle, cellsInXAxis, cellsInYAxis, r, function, true);
    }

    public static Grid newGeoGrid(Rectangle rectangle, double r, /*ReplicationType function*/Function4<Cell, Cell, Point, Agreement> function){

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
        System.out.println("Cells in X axis:"+ (int) cellsInXAxis);
        System.out.println("Cells in Y axis:"+ (int) cellsInYAxis);

        return new Grid(rectangle, (int) cellsInXAxis, (int) cellsInYAxis, r, function ,true);
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

    public HashMap<Integer,Integer> getCellsWithCosts2(){
        HashMap<Integer,Integer> cellsWithCosts = new HashMap<>();

        cells.forEach((cellId,cell)->{
            if(cell.getNumberOfPointsAType() != 0 && cell.getNumberOfPointsBType() != 0) {
                cellsWithCosts.put(cellId, cell.getNumberOfPointsAType() * cell.getNumberOfPointsBType());
            }
        });
        return cellsWithCosts;
    }

    public HashMap<Integer,Integer> getCellsWithCosts1(){
        long t1 = System.currentTimeMillis();
        HashMap<Integer, Integer> cellsWithCosts = new HashMap<>();

//        cells.forEach((k,v)-> {
//            if(v.getNumberOfPointsAType()  == 0 || (v.getNumberOfPointsBType()) == 0) {
//                cells.remove(k);
//            }
//        });

//        Iterator<Map.Entry<Integer, Cell>> o2 = new HashMap<>(cells).entrySet().parallelStream().iterator();
//        while(o2.hasNext()) {
//            Map.Entry<Integer, Cell> entry = o2.next();
//            if ((entry.getValue().getNumberOfPointsAType()) == 0 || (entry.getValue().getNumberOfPointsBType()) == 0) {
//                cells.remove(entry.getKey());
//            }
//        }

        List<Integer> cellsIds = new ArrayList<>(cells.size());
        cells.forEach((a,b)->{
            cellsIds.add(a);
        });

//        Iterator<Map.Entry<Integer, Cell>> o = cells.entrySet().iterator();
//        while(o.hasNext()){
//            Map.Entry<Integer, Cell> entry = o.next();
//            int cellID = entry.getKey();
//            Cell cell = entry.getValue();
        for (Integer cellsId : cellsIds) {
            int cellID = cellsId;
            Cell cell = getCell(cellsId);

//            if((cell.getNumberOfPointsAType()) != 0 && (cell.getNumberOfPointsBType()) != 0) {

                int extraA = 0;
                int extraB = 0;

                if (hasBottom(cellID) && hasLeft(cellID)) {
                    Cell cellN = getCell((cellID - cellsInXAxis) - 1);
                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound(),0).getEdge((cellID - cellsInXAxis) - 1, cellID);
//                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound(),0).getEdge(5);
                    if (!e.isEliminated()) {
                        if (e.getTypeSet().equals(TypeSet.A)) {
                            extraA = extraA + cellN.getNumberOfPointsAInTopRightQuarterArea();
                        } else {
                            extraB = extraB + cellN.getNumberOfPointsBInTopRightQuarterArea();

                        }
                    }
                }

                if (hasBottom(cellID) && hasRight(cellID)) {
                    Cell cellN = getCell((cellID - cellsInXAxis) + 1);
                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerRightBound(),1).getEdge((cellID - cellsInXAxis) + 1, cellID);
//                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerRightBound(),1).getEdge(7);
                    if (!e.isEliminated()) {
                        if (e.getTypeSet().equals(TypeSet.A)) {
                            extraA = extraA + cellN.getNumberOfPointsAInTopLeftQuarterArea();
                        } else {
                            extraB = extraB + cellN.getNumberOfPointsBInTopLeftQuarterArea();

                        }
                    }
                }

                if (hasTop(cellID) && hasLeft(cellID)) {
                    Cell cellN = getCell((cellID + cellsInXAxis) - 1);
                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperLeftBound(),2).getEdge((cellID + cellsInXAxis) - 1, cellID);
//                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperLeftBound(),2).getEdge(8);
                    if (!e.isEliminated()) {
                        if (e.getTypeSet().equals(TypeSet.A)) {
                            extraA = extraA + cellN.getNumberOfPointsAInBottomRightQuarterArea();
                        } else {
                            extraB = extraB + cellN.getNumberOfPointsBInBottomRightQuarterArea();

                        }
                    }
                }

                if (hasTop(cellID) && hasRight(cellID)) {
                    Cell cellN = getCell((cellID + cellsInXAxis) + 1);
                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound(),3).getEdge((cellID + cellsInXAxis) + 1, cellID);
//                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound(),3).getEdge(6);
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
                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound(),0).getEdge((cellID - cellsInXAxis), cellID);
                    Edge e1 = getAgreements(cellID, cell.getRectangle().getLowerRightBound(),1).getEdge((cellID - cellsInXAxis), cellID);
//                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound(),0).getEdge(hasLeft(cellID)?9:1);
//                    Edge e1 = getAgreements(cellID, cell.getRectangle().getLowerRightBound(),1).getEdge(hasRight(cellID)?3:1);
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
                    Cell cellN = getCell((cellID - 1));
                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound(),0).getEdge((cellID - 1), cellID);
                    Edge e1 = getAgreements(cellID, cell.getRectangle().getUpperLeftBound(),2).getEdge((cellID - 1), cellID);
//                    Edge e = getAgreements(cellID, cell.getRectangle().getLowerBound(),0).getEdge(hasBottom(cellID)?11:1);
//                    Edge e1 = getAgreements(cellID, cell.getRectangle().getUpperLeftBound(),2).getEdge(hasTop(cellID)?1:1);
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
                    Cell cellN = getCell(cellID + 1);
                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound(),3).getEdge(cellID + 1, cellID);
                    Edge e1 = getAgreements(cellID, cell.getRectangle().getLowerRightBound(),1).getEdge(cellID + 1, cellID);
//                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound(),3).getEdge(hasTop(cellID)?2:2);
//                    Edge e1 = getAgreements(cellID, cell.getRectangle().getLowerRightBound(),1).getEdge(hasBottom(cellID)?12:2);

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
                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound(),3).getEdge((cellID + cellsInXAxis), cellID);
                    Edge e1 = getAgreements(cellID, cell.getRectangle().getUpperLeftBound(),2).getEdge((cellID + cellsInXAxis), cellID);
//                    Edge e = getAgreements(cellID, cell.getRectangle().getUpperBound(),3).getEdge(hasRight(cellID)?4:2);
//                    Edge e1 = getAgreements(cellID, cell.getRectangle().getUpperLeftBound(),2).getEdge(hasLeft(cellID)?10:2);
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
                    int k = (cell.getNumberOfPointsAType() + extraA) * (cell.getNumberOfPointsBType() + extraB);
                    if(k>=1) {
                        cellsWithCosts.put(cellID, k);
                    }
//                    cellsWithCosts.put(cellID, (cell.getNumberOfPointsAType() + extraA) * (cell.getNumberOfPointsBType() + extraB));
                }
//                else{
//                    if(cell.getNumberOfPointsAType() + extraA == 0 && cell.getNumberOfPointsBType() + extraB != 0){
//                        cellsWithCosts.put(cellID, (cell.getNumberOfPointsBType() + extraB));
//
//                    }else if(cell.getNumberOfPointsAType() + extraA != 0 && cell.getNumberOfPointsBType() + extraB == 0){
//                        cellsWithCosts.put(cellID, cell.getNumberOfPointsAType() + extraA);
//                    }
//                }
//            }
        }

        System.out.println("Aggrements size was"+agreements.size());
        Iterator o1 = agreements.entrySet()/*.parallelStream()*/.iterator();
        while (o1.hasNext()) {
            Map.Entry<Integer, Agreements> e = (Map.Entry<Integer, Agreements>) o1.next();
            if(e.getValue().haveAllSameType()){
                if(e.getValue().hasTypeA()){
                    o1.remove();
//                    agreements.remove(e.getKey());
                }else if(e.getValue().hasTypeB()){
                    agreements.replace(e.getKey(), null);
                }else{
                    try {
                        throw new Exception("Must have type A or B");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        System.out.println("Aggrements size is"+agreements.size());
        cells.clear();
//        agreements.clear();
        System.out.println("Time for agreements: "+(System.currentTimeMillis()-t1)/1000);
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

    private void getPartitionsWithoutAgreements(int cellID, Position position, List<Integer> partitions){
        switch (position){
            case TOPRIGHTQUARTER:
                if (hasRight(cellID)) {
                    partitions.add(cellID + 1);
                }
                if (hasTop(cellID)) {
                    partitions.add(cellID + cellsInXAxis);
                }
                if (hasTop(cellID) && hasRight(cellID)) {
                    partitions.add(cellID + cellsInXAxis + 1);
                }
                break;
            case TOPLEFTQUARTER:
                if (hasLeft(cellID)) {
                    partitions.add(cellID - 1);
                }
                if (hasTop(cellID)) {
                    partitions.add(cellID + cellsInXAxis);
                }
                if (hasTop(cellID) && hasLeft(cellID)) {
                    partitions.add(cellID + cellsInXAxis - 1);
                }
                break;
            case BOTTOMRIGHTQUARTER:
                if (hasBottom(cellID)) {
                    partitions.add(cellID - cellsInXAxis);
                }
                if (hasRight(cellID)) {
                    partitions.add(cellID + 1);
                }
                if (hasBottom(cellID) && hasRight(cellID)) {
                    partitions.add(cellID - cellsInXAxis + 1);
                }
                break;
            case BOTTOMLEFTQUARTER:
                if (hasBottom(cellID)) {
                    partitions.add(cellID - cellsInXAxis);
                }
                if (hasLeft(cellID)) {
                    partitions.add(cellID - 1);
                }
                if (hasBottom(cellID) && hasLeft(cellID)) {
                    partitions.add(cellID - cellsInXAxis - 1);
                }
                break;
            case TOPRIGHT:
                if (hasRight(cellID)) {
                    partitions.add(cellID + 1);
                }
                if (hasTop(cellID)) {
                    partitions.add(cellID + cellsInXAxis);
                }
                break;
            case TOPLEFT:
                if (hasLeft(cellID)) {
                    partitions.add(cellID - 1);
                }
                if (hasTop(cellID)) {
                    partitions.add(cellID + cellsInXAxis);
                }
                break;
            case BOTTOMRIGHT:
                if (hasBottom(cellID)) {
                    partitions.add(cellID - cellsInXAxis);
                }
                if (hasRight(cellID)) {
                    partitions.add(cellID + 1);
                }
                break;
            case BOTTOMLEFT:
                if (hasBottom(cellID)) {
                    partitions.add(cellID - cellsInXAxis);
                }
                if (hasLeft(cellID)) {
                    partitions.add(cellID - 1);
                }
                break;
            case LEFT:
                if (hasLeft(cellID)) {
                    partitions.add((cellID - 1));
                }
                break;
            case TOP:
                if (hasTop(cellID)) {
                    partitions.add(cellID + cellsInXAxis);
                }
                break;
            case RIGHT:
                if (hasRight(cellID)) {
                    partitions.add(cellID + 1);
                }
                break;
            case BOTTOM:
                if (hasBottom(cellID)) {
                    partitions.add(cellID - cellsInXAxis);
                }
                break;
            default: throw new RuntimeException("No correct position");
        }
    }

    private int getAggID(int cellID, int index){
        int aggID=-1;
        int yc = cellID/cellsInXAxis;
        int xc = cellID - (yc * cellsInXAxis);

        switch(index){
            case 0:
                aggID = (xc)+(yc * (cellsInXAxis+1));
                break;
            case 1:
                 aggID = (xc+1)+(yc * (cellsInXAxis+1));
                 break;
            case 2:
                aggID = xc+((yc+1) * (cellsInXAxis+1));
                break;
            case 3:
                aggID = (xc+1)+((yc+1) * (cellsInXAxis+1));
                break;
            default:
                try {
                    throw new Exception("Problem in the detection of aggID");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        return aggID;
    }

    private int[] getPartitionsInExecutor(double x, double y, TypeSet typeSet){
        int cellID = getCellId(x,y);
        Cell cell = getCellInExecutor(cellID);
        List<Integer> partitions = new ArrayList<>();
        Agreements aggr;
        partitions.add(getPartitionId(cell));

        switch(cell.getPosition(x,y,r)){
            case PLAIN:
                break;
            case TOPRIGHTQUARTER:
                if(!existsInAgreements(getAggID(cellID, 3))){
                    if(typeSet.equals(TypeSet.A)) {
                        getPartitionsWithoutAgreements(cellID,TOPRIGHTQUARTER, partitions);
                    }
                }else {
                    aggr = getAgreementsInExecutor(getAggID(cellID, 3));
                    if(aggr==null){
                        if(typeSet.equals(TypeSet.B)) {
                            getPartitionsWithoutAgreements(cellID,TOPRIGHTQUARTER, partitions);
                        }
                    }
                    else if(aggr.getSpacesNum()==4){
                        getPartitions(cellID, aggr, TOPRIGHT, typeSet, false, partitions);
                    }else if(hasTop(cellID)){
                        getPartitions(cellID, aggr, TOP, typeSet, partitions);
                    }else if(hasRight(cellID)){
                        getPartitions(cellID, aggr, RIGHT, typeSet, partitions);
                    }
                }
                checkForExtraPartitionSuppArea(cell, cellID, TOPLEFT, true, x,y, typeSet, partitions);
                checkForExtraPartitionSuppArea(cell, cellID, BOTTOMRIGHT, false, x,y, typeSet, partitions);
                break;
            case TOPLEFTQUARTER:
                if(!existsInAgreements(getAggID(cellID, 2))){
                    if(typeSet.equals(TypeSet.A)) {
                        getPartitionsWithoutAgreements(cellID,TOPLEFTQUARTER, partitions);
                    }
                }else {
                    aggr = getAgreementsInExecutor(getAggID(cellID, 2));
                    if(aggr==null){
                        if(typeSet.equals(TypeSet.B)) {
                            getPartitionsWithoutAgreements(cellID,TOPLEFTQUARTER, partitions);
                        }
                    }
                    else if(aggr.getSpacesNum()==4) {
                        getPartitions(cellID, aggr, TOPLEFT, typeSet, false, partitions);
                    }else if(hasTop(cellID)){
                        getPartitions(cellID, aggr, TOP, typeSet, partitions);
                    }else if(hasLeft(cellID)){
                        getPartitions(cellID, aggr, LEFT, typeSet, partitions);
                    }
                }
                checkForExtraPartitionSuppArea(cell, cellID, TOPRIGHT, true, x, y, typeSet, partitions);
                checkForExtraPartitionSuppArea(cell, cellID, BOTTOMLEFT, false, x, y, typeSet, partitions);
                break;
            case BOTTOMRIGHTQUARTER:
                if(!existsInAgreements(getAggID(cellID, 1))){
                    if(typeSet.equals(TypeSet.A)) {
                        getPartitionsWithoutAgreements(cellID,BOTTOMRIGHTQUARTER, partitions);
                    }
                }else {
                aggr = getAgreementsInExecutor(getAggID(cellID, 1));
                    if(aggr==null){
                        if(typeSet.equals(TypeSet.B)) {
                            getPartitionsWithoutAgreements(cellID,BOTTOMRIGHTQUARTER, partitions);
                        }
                    }
                    else if(aggr.getSpacesNum()==4) {
                        getPartitions(cellID, aggr, BOTTOMRIGHT, typeSet, false, partitions);
                    } else if(hasBottom(cellID)){
                        getPartitions(cellID, aggr, BOTTOM, typeSet, partitions);
                    }else if(hasRight(cellID)){
                        getPartitions(cellID, aggr, RIGHT, typeSet, partitions);
                    }
                }
                checkForExtraPartitionSuppArea(cell, cellID, BOTTOMLEFT, true, x, y, typeSet, partitions);
                checkForExtraPartitionSuppArea(cell, cellID, TOPRIGHT, false, x, y, typeSet, partitions);
                break;
            case BOTTOMLEFTQUARTER:
                if(!existsInAgreements(getAggID(cellID, 0))){
                    if(typeSet.equals(TypeSet.A)) {
                        getPartitionsWithoutAgreements(cellID,BOTTOMLEFTQUARTER, partitions);
                    }
                }else {
                    aggr = getAgreementsInExecutor(getAggID(cellID, 0));
                    if(aggr==null){
                        if(typeSet.equals(TypeSet.B)) {
                            getPartitionsWithoutAgreements(cellID,BOTTOMLEFTQUARTER, partitions);
                        }
                    }
                    else if(aggr.getSpacesNum()==4) {
                        getPartitions(cellID, aggr, BOTTOMLEFT, typeSet, false, partitions);
                    }else if(hasBottom(cellID)){
                        getPartitions(cellID, aggr, BOTTOM, typeSet, partitions);
                    }else if(hasLeft(cellID)){
                        getPartitions(cellID, aggr, LEFT, typeSet, partitions);
                    }
                }
                checkForExtraPartitionSuppArea(cell, cellID, BOTTOMRIGHT, true, x, y, typeSet, partitions);
                checkForExtraPartitionSuppArea(cell, cellID, TOPLEFT, false, x, y, typeSet, partitions);
                break;
            case TOPRIGHT:
                if(!existsInAgreements(getAggID(cellID, 3))){
                    if(typeSet.equals(TypeSet.A)) {
                        getPartitionsWithoutAgreements(cellID,TOPRIGHT, partitions);
                    }
                }else {
                    aggr = getAgreementsInExecutor(getAggID(cellID, 3));
                    if(aggr==null){
                        if(typeSet.equals(TypeSet.B)) {
                            getPartitionsWithoutAgreements(cellID,TOPRIGHT, partitions);
                        }
                    }
                    else if(aggr.getSpacesNum()==4) {
                        getPartitions(cellID, aggr, TOPRIGHT, typeSet, true, partitions);
                    }else if(hasTop(cellID)){
                        getPartitions(cellID, aggr, TOP, typeSet, partitions);
                    }else if(hasRight(cellID)){
                        getPartitions(cellID, aggr, RIGHT, typeSet, partitions);
                    }
                }
                checkForExtraPartition(cell, cellID, TOPLEFT, true, x, y, typeSet, partitions);
                checkForExtraPartition(cell, cellID, BOTTOMRIGHT, false, x, y, typeSet, partitions);
                break;
            case TOPLEFT:
                if(!existsInAgreements(getAggID(cellID, 2))){
                    if(typeSet.equals(TypeSet.A)) {
                        getPartitionsWithoutAgreements(cellID,TOPLEFT, partitions);
                    }
                }else {
                    aggr = getAgreementsInExecutor(getAggID(cellID, 2));
                    if(aggr==null){
                        if(typeSet.equals(TypeSet.B)) {
                            getPartitionsWithoutAgreements(cellID,TOPLEFT, partitions);
                        }
                    }
                    else if(aggr.getSpacesNum()==4) {
                        getPartitions(cellID, aggr, TOPLEFT, typeSet, true, partitions);
                    }else if(hasTop(cellID)){
                        getPartitions(cellID, aggr, TOP, typeSet, partitions);
                    }else if(hasLeft(cellID)){
                        getPartitions(cellID, aggr, LEFT, typeSet, partitions);
                    }
                }
                checkForExtraPartition(cell, cellID, TOPRIGHT, true, x, y, typeSet, partitions);
                checkForExtraPartition(cell, cellID, BOTTOMLEFT, false, x, y, typeSet, partitions);
                break;
            case BOTTOMRIGHT:
                if(!existsInAgreements(getAggID(cellID, 1))){
                    if(typeSet.equals(TypeSet.A)) {
                        getPartitionsWithoutAgreements(cellID,BOTTOMRIGHT, partitions);
                    }
                }else {
                    aggr = getAgreementsInExecutor(getAggID(cellID, 1));
                    if(aggr==null){
                        if(typeSet.equals(TypeSet.B)) {
                            getPartitionsWithoutAgreements(cellID,BOTTOMRIGHT, partitions);
                        }
                    }
                    else if(aggr.getSpacesNum()==4) {
                        getPartitions(cellID, aggr, BOTTOMRIGHT, typeSet, true, partitions);
                    }else if(hasBottom(cellID)){
                        getPartitions(cellID, aggr, BOTTOM, typeSet, partitions);
                    }else if(hasRight(cellID)){
                        getPartitions(cellID, aggr, RIGHT, typeSet, partitions);
                    }
                }
                checkForExtraPartition(cell, cellID, BOTTOMLEFT, true, x, y, typeSet, partitions);
                checkForExtraPartition(cell, cellID, TOPRIGHT, false, x, y, typeSet, partitions);
                break;
            case BOTTOMLEFT:
                if(!existsInAgreements(getAggID(cellID, 0))){
                    if(typeSet.equals(TypeSet.A)) {
                        getPartitionsWithoutAgreements(cellID,BOTTOMLEFT, partitions);
                    }
                }else {
                    aggr = getAgreementsInExecutor(getAggID(cellID, 0));
                    if(aggr==null){
                        if(typeSet.equals(TypeSet.B)) {
                            getPartitionsWithoutAgreements(cellID,BOTTOMLEFT, partitions);
                        }
                    }
                    else if(aggr.getSpacesNum()==4) {
                        getPartitions(cellID, aggr, BOTTOMLEFT, typeSet, true, partitions);
                    }else if(hasBottom(cellID)){
                        getPartitions(cellID, aggr, BOTTOM, typeSet, partitions);
                    }else if(hasLeft(cellID)){
                        getPartitions(cellID, aggr, LEFT, typeSet, partitions);
                    }
                }
                checkForExtraPartition(cell, cellID, BOTTOMRIGHT, true, x, y, typeSet, partitions);
                checkForExtraPartition(cell, cellID, TOPLEFT, false, x, y, typeSet, partitions);
                break;
            case TOP:
                if(hasTop(cellID)){
                    if(!existsInAgreements(getAggID(cellID, 3))){
                        if(typeSet.equals(TypeSet.A)){
                            partitions.add(cellID + cellsInXAxis);
                        }
                    }else{
                    aggr = getAgreementsInExecutor(getAggID(cellID, 3));
                    if(aggr!=null){
                        aggr.getPartitionsNEW(0, 2, typeSet, partitions);
                    }else if(typeSet.equals(TypeSet.B)){
                        partitions.add(cellID + cellsInXAxis);
                    }}
                }
                checkForExtraPartition(cell, cellID, TOPLEFT, true, x,y, typeSet,partitions);
                checkForExtraPartition(cell, cellID, TOPRIGHT, true, x,y, typeSet,partitions);
                break;
            case BOTTOM:
                if(hasBottom(cellID)){
                    if(!existsInAgreements(getAggID(cellID, 0))){
                        if(typeSet.equals(TypeSet.A)){
                            partitions.add(cellID - cellsInXAxis);
                        }
                    }else{
                    aggr = getAgreementsInExecutor(getAggID(cellID, 0));
                    if(aggr!=null){
                        aggr.getPartitionsNEW(3, 1, typeSet, partitions);
                    }else if(typeSet.equals(TypeSet.B)){
                        partitions.add(cellID - cellsInXAxis);
                    }}
                }
                checkForExtraPartition(cell, cellID, BOTTOMLEFT, true, x,y, typeSet,partitions);
                checkForExtraPartition(cell, cellID, BOTTOMRIGHT, true, x,y, typeSet,partitions);
                break;
            case RIGHT:
                if(hasRight(cellID)){
                    if(!existsInAgreements(getAggID(cellID, 3))){
                        if(typeSet.equals(TypeSet.A)){
                            partitions.add(cellID + 1);
                        }
                    }else{
                    aggr = getAgreementsInExecutor(getAggID(cellID, 3));
                    if(aggr!=null){
                        aggr.getPartitionsNEW(0, 1, typeSet, partitions);
                    }else if(typeSet.equals(TypeSet.B)){
                        partitions.add(cellID + 1);
                    }}
                }
                checkForExtraPartition(cell, cellID, TOPRIGHT, false, x,y, typeSet,partitions);
                checkForExtraPartition(cell, cellID, BOTTOMRIGHT, false, x,y, typeSet,partitions);
                break;
            case LEFT:
                if(hasLeft(cellID)){
                    if(!existsInAgreements(getAggID(cellID, 0))){
                        if(typeSet.equals(TypeSet.A)){
                            partitions.add(cellID - 1);
                        }
                    }else{
                    aggr = getAgreementsInExecutor(getAggID(cellID, 0));
                    if(aggr!=null){
                        aggr.getPartitionsNEW(3, 2, typeSet, partitions);
                    }else if(typeSet.equals(TypeSet.B)){
                        partitions.add(cellID - 1);
                    }}
                }
                checkForExtraPartition(cell, cellID, TOPLEFT, false, x,y, typeSet,partitions);
                checkForExtraPartition(cell, cellID, BOTTOMLEFT, false, x,y, typeSet,partitions);
                break;
            default: throw new RuntimeException("No correct position");
        }
        return listToArray(partitions);
    }

    private void checkForExtraPartitionSuppArea(Cell cell, int cellID, Position position, boolean horizontal1, double x, double y, TypeSet typeSet, List<Integer> list) {
//        if(!geoData){
        checkForExtraPartition(cell, cellID, position, horizontal1, x, y, typeSet, list);
//        }else{
//            //checkForExtraGeoPartition(cell, cellId, position, horizontal1, point, typeSet, list);
//        }
    }

    private void checkForExtraPartition(Cell cell, int cellID, Position position, boolean horizontal, double x, double y, TypeSet typeSet, List<Integer> partitions) {
        int extraCell;
        Agreements aggr = null;
        switch (position){
            case TOPRIGHT:
                aggr = getAgreementsInExecutor(getAggID(cellID, 3));
                if(aggr!=null) {
                    if (aggr.getSpacesNum() == 4) {
                        if (horizontal) {
                            extraCell = aggr.getExtraPartition(4, 1, 5, typeSet);
                        } else {
                            extraCell = aggr.getExtraPartition(2, 3, 5, typeSet);
                        }
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
                        if (extraCell != -1) {
                            if (horizontal) {
                                if (Circle.newCircle(cell.getRectangle().getUpperBound().getX() - r, cell.getRectangle().getUpperBound().getY(), r).containsInclusive(x, y)) {
                                    partitions.add(extraCell);
                                }
                            } else {
                                if (Circle.newCircle(cell.getRectangle().getUpperBound().getX(), cell.getRectangle().getUpperBound().getY() - r, r).containsInclusive(x, y)) {
                                    partitions.add(extraCell);
                                }
                            }
                        }
                    }
                }
                break;
            case TOPLEFT:
                aggr = getAgreementsInExecutor(getAggID(cellID, 2));
                if(aggr!=null) {
                    if (aggr.getSpacesNum() == 4) {
                        if (horizontal) {
                            extraCell = aggr.getExtraPartition(10, 2, 7, typeSet);
                        } else {
                            extraCell = aggr.getExtraPartition(1, 7, 9, typeSet);
                        }
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getUpperLeftBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId + cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
                        if (extraCell != -1) {
                            if (horizontal) {
                                if (Circle.newCircle(cell.getRectangle().getUpperLeftBound().getX() + r, cell.getRectangle().getUpperLeftBound().getY(), r).containsInclusive(x, y)) {
                                    partitions.add(extraCell);
                                }
                            } else {
                                if (Circle.newCircle(cell.getRectangle().getUpperLeftBound().getX(), cell.getRectangle().getUpperLeftBound().getY() - r, r).containsInclusive(x, y)) {
                                    partitions.add(extraCell);
                                }
                            }
                        }
                    }
                }
                break;
            case BOTTOMRIGHT:
                aggr = getAgreementsInExecutor(getAggID(cellID, 1));
                if(aggr!=null) {

                    if (aggr.getSpacesNum() == 4) {
                        if (horizontal) {
                            extraCell = aggr.getExtraPartition(3, 8, 11, typeSet);
                        } else {
                            extraCell = aggr.getExtraPartition(12, 4, 8, typeSet);
                        }
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerRightBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  + 1))), typeSet);
                        if (extraCell != -1) {
                            if (horizontal) {
                                if (Circle.newCircle(cell.getRectangle().getLowerRightBound().getX() - r, cell.getRectangle().getLowerRightBound().getY(), r).containsInclusive(x, y)) {
                                    partitions.add(extraCell);
                                }
                            } else {
                                if (Circle.newCircle(cell.getRectangle().getLowerRightBound().getX(), cell.getRectangle().getLowerRightBound().getY() + r, r).containsInclusive(x, y)) {
                                    partitions.add(extraCell);
                                }
                            }
                        }
                    }
                }
                break;
            case BOTTOMLEFT:
                aggr = getAgreementsInExecutor(getAggID(cellID, 0));
                if(aggr!=null) {

                    if (aggr.getSpacesNum() == 4) {
                        if (horizontal) {
                            extraCell = aggr.getExtraPartition(9, 6, 12, typeSet);
                        } else {
                            extraCell = aggr.getExtraPartition(11, 6, 10, typeSet);
                        }
//                    Optional<Cell> extraCell = getAgreements(cellId, cell.getRectangle().getLowerBound()).getExtraPartition(cell,(horizontal)?(getCell((cellId - cellsInXAxis))):(getCell((cellId  - 1))), typeSet);
                        if (extraCell != -1) {
                            if (horizontal) {
                                if (Circle.newCircle(cell.getRectangle().getLowerBound().getX() + r, cell.getRectangle().getLowerBound().getY(), r).containsInclusive(x, y)) {
                                    partitions.add(extraCell);
                                }
                            } else {
                                if (Circle.newCircle(cell.getRectangle().getLowerBound().getX(), cell.getRectangle().getLowerBound().getY() + r, r).containsInclusive(x, y)) {
                                    partitions.add(extraCell);
                                }
                            }
                        }
                    }
                }
                break;
            default:
                throw new IllegalStateException("Position was not caught.");
        }
    }

    private Agreements getAgreementsInExecutor(int aggID) {
//        if (cells.containsKey(cellID)) {
//
//
//            return agreements.computeIfAbsent(point, (p)->{
//
//                List<Cell> cells = new ArrayList<>();
//
//                switch (index){
//                    case 0:
//                        if (hasBottom(cellID) && hasLeft(cellID)) {
//                            cells.add(getCell((cellID - cellsInXAxis) - 1));
//                        }
//                        if (hasBottom(cellID)) {
//                            cells.add(getCell((cellID - cellsInXAxis)));
//                        }
//                        if (hasLeft(cellID)) {
//                            cells.add(getCell((cellID - 1)));
//                        }
//                        cells.add(getCell(cellID));
//                        break;
//                    case 1:
//                        if (hasBottom(cellID)) {
//                            cells.add(getCell((cellID - cellsInXAxis)));
//                        }
//                        if (hasBottom(cellID) && hasRight(cellID)) {
//                            cells.add(getCell((cellID - cellsInXAxis) + 1));
//                        }
//                        cells.add(getCell(cellID));
//                        if (hasRight(cellID)) {
//                            cells.add(getCell(cellID + 1));
//                        }
//                        break;
//                    case 2:
//                        if (hasLeft(cellID)) {
//                            cells.add(getCell((cellID - 1)));
//                        }
//                        cells.add(getCell(cellID));
//                        if (hasTop(cellID) && hasLeft(cellID)) {
//                            cells.add(getCell((cellID + cellsInXAxis) - 1));
//                        }
//
//                        if (hasTop(cellID)) {
//                            cells.add(getCell((cellID + cellsInXAxis)));
//                        }
//                        break;
//                    case 3:
//                        cells.add(getCell(cellID));
//                        if (hasRight(cellID)) {
//                            cells.add(getCell(cellID + 1));
//                        }
//                        if (hasTop(cellID)) {
//                            cells.add(getCell((cellID + cellsInXAxis)));
//                        }
//                        if (hasTop(cellID) && hasRight(cellID)) {
//                            cells.add(getCell((cellID + cellsInXAxis) + 1));
//                        }
//                        break;
//                    default:
//                        try {
//                            throw new Exception("");
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                }
//                Agreements aggr = Agreements.newAgreements(cells.stream().mapToInt(this::cellToId).toArray(), cells, point, function);
//                aggr.createEdges();
//                return aggr;
//            });
//    }
//        return null;
        return agreements.get(aggID);
    }

    public boolean existsInAgreements(int aggID){
        return agreements.containsKey(aggID);
    }

    public int[] listToArray(List<Integer> list){
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

}
