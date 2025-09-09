//package gr.ds.unipi.quadtree;
//
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.Output;
////import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
//import gr.ds.unipi.agreements.Agreements;
//import gr.ds.unipi.TypeSet;
//import gr.ds.unipi.shapes.Circle;
//import gr.ds.unipi.shapes.Point;
//import gr.ds.unipi.shapes.Rectangle;
//
//import java.io.*;
//import java.util.*;
//
//import static java.lang.Math.abs;
//
//public class QuadTree {
//
//    private Node root;
//    private final long cost;
//    private static double r = -1;
//    private int leaves = 1;
//    private final Map<String, Agreements<LeafNode>> agreements;
//
//    private QuadTree(Node root, long cost, double r) {
//        this.root = root;
//        this.cost = cost;
//        this.r = r;
//        if(Double.compare(r,0) != 1){
//            try {
//                throw new Exception("r should be greater than 0");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        agreements = new HashMap<>();
//    }
//
//    private QuadTree(QuadTree quadTree, Node root){
//        this.root = root;
//        cost = quadTree.cost;
//        agreements = new HashMap<>();
//    }
//
//    public static QuadTree newQuadTree(Rectangle rectangle, long cost, double r) {
//        return new QuadTree(LeafNode.newLeafNode(null, rectangle), cost, r);
//    }
//
//    public void insertPointDatasetA(Point point) {
//        insertPoint(root, point, TypeSet.A);
//    }
//
//    public void insertPointDatasetB(Point point) {
//        insertPoint(root, point, TypeSet.B);
//    }
//
//    private void insertPoint(Node node, Point point, TypeSet typeSet) {
//
//        LeafNode leafNode = getLeafNode(node, point);
//        leafNode.insertPoint(point, typeSet);
//        if(checkNodeForSplit(leafNode)){
//            splitLeafNode(leafNode);
//        }
//    }
//
//    private boolean checkNodeForSplit(LeafNode leafNode){
//        if((long) leafNode.getNumberOfPointsAType() * leafNode.getNumberOfPointsBType() > cost){
//            if(leafNode.getWidth() > r*4 && leafNode.getHeight() > r*4){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void splitLeafNode(LeafNode leafNode){
//        NonLeafNode parentNode = leafNode.getParent();
//
//        Point center = Point.newPoint((leafNode.getRectangle().getUpperBound().getX() + leafNode.getRectangle().getLowerBound().getX()) / 2, (leafNode.getRectangle().getUpperBound().getY() + leafNode.getRectangle().getLowerBound().getY()) / 2);
//        Point bottomHalf = Point.newPoint((leafNode.getRectangle().getUpperBound().getX() + leafNode.getRectangle().getLowerBound().getX()) / 2, leafNode.getRectangle().getLowerBound().getY());
//        Point rightHalf = Point.newPoint(leafNode.getRectangle().getUpperBound().getX(),(leafNode.getRectangle().getUpperBound().getY() + leafNode.getRectangle().getLowerBound().getY()) / 2);
//        Point topHalf = Point.newPoint((leafNode.getRectangle().getUpperBound().getX() + leafNode.getRectangle().getLowerBound().getX()) / 2,leafNode.getRectangle().getUpperBound().getY());
//        Point leftHalf = Point.newPoint(leafNode.getRectangle().getLowerBound().getX(),(leafNode.getRectangle().getUpperBound().getY() + leafNode.getRectangle().getLowerBound().getY()) / 2);
//
//        NonLeafNode nonLeafNode = NonLeafNode.newNonLeafNode(parentNode, leafNode.getRectangle());
//        if(parentNode !=null){
//            parentNode.replaceChild(leafNode, nonLeafNode);
//        }else{
//            root = nonLeafNode;
//        }
//
//
//        LeafNode bottomLeft = LeafNode.newLeafNode(nonLeafNode,Rectangle.newRectangle(leafNode.getRectangle().getLowerBound(), center));
//        LeafNode bottomRight = LeafNode.newLeafNode(nonLeafNode,Rectangle.newRectangle(bottomHalf,rightHalf));
//        LeafNode topLeft = LeafNode.newLeafNode(nonLeafNode,Rectangle.newRectangle(leftHalf,topHalf));
//        LeafNode topRight = LeafNode.newLeafNode(nonLeafNode,Rectangle.newRectangle(center,leafNode.getRectangle().getUpperBound()));
//
//        List<LeafNode> newLeafNodes = Arrays.asList(bottomLeft, bottomRight, topLeft, topRight);
//        nonLeafNode.setChildren(newLeafNodes);
//
//        disseminatePoints(leafNode.getDatasetA(), leafNode.getDatasetB(), newLeafNodes);
//    }
//
//    private void disseminatePoints(List<Point> datasetA, List<Point> datasetB, List<LeafNode> leafNodes){
//        for (Point point : datasetA) {
//            for (LeafNode leafNode : leafNodes) {
//                if(leafNode.getRectangle().contains(point)){
//                    leafNode.insertPoint(point, TypeSet.A);
//                    break;
//                }
//            }
//        }
//
//        for (Point point : datasetB) {
//            for (LeafNode leafNode : leafNodes) {
//                if(leafNode.getRectangle().contains(point)){
//                    leafNode.insertPoint(point, TypeSet.B);
//                    break;
//                }
//            }
//        }
//
//        for (LeafNode leafNode : leafNodes) {
//            if(checkNodeForSplit(leafNode)){
//                splitLeafNode(leafNode);
//            }
//        }
//    }
//
//    public void assignIdToLeaves(){
//        traverseTree(root);
//    }
//
//    public void traverseTree(Node node){
//        if(node instanceof NonLeafNode){
//            for (Node child : ((NonLeafNode) node).getChildren()) {
//                traverseTree(child);
//            }
//        }else{
//            LeafNode leafNode = LeafNode.newLeafNode((LeafNode) node, leaves++);
//            node.getParent().replaceChild(node,leafNode);
//        }
//    }
//
////    public void loadAgreements(){
////        loadAgreements(root);
////    }
////
////    private void loadAgreements(Node node){
////        if(node instanceof NonLeafNode){
////            for (Node child : ((NonLeafNode) node).getChildren()) {
////                traverseTree(child);
////            }
////        }else{
////            List<LeafNode> queryResults = new ArrayList<>();
////
////            if(queryResults.size()>4)
////            rangeQuery(node.getRectangle(), );
////            node.getParent().replaceChild(node,leafNode);
////        }
////    }
//
//    public String toString(){
//        StringBuilder sb = new StringBuilder();
//        traverseTree(root, sb, "");
//        return sb.toString();
//    }
//
//    public void traverseTree(Node node, StringBuilder sb, String indent){
//        if(node instanceof NonLeafNode){
//            sb.append(node.toString(indent)).append("\n");
//            for (Node child : ((NonLeafNode) node).getChildren()) {
//                traverseTree(child, sb, indent+" ");
//            }
//        }else{
//            sb.append(node.toString(indent)).append("\n");
//        }
//    }
//
//    public String[] getPartitionsAType(double x, double y){
//        return getPartitions(x, y, TypeSet.A);
//    }
//
//    public String[] getPartitionsBType(double x, double y){
//        return getPartitions(x, y, TypeSet.B);
//    }
//
//    private String getKey(List<LeafNode> partitions){
//        String key = "";
//        for (LeafNode partition : partitions) {
//            key = key + partition.getId()+":";
//        }
//        return key.substring(0, key.length()-1);
//    }
//
//    private Agreements<LeafNode> getAgreements(List<LeafNode> leafNodes){
//        String key = getKey(leafNodes);
//        if(agreements.containsKey(key)){
//            return agreements.get(key);
//        }else{
//            System.out.println("KEY:"+key);
//            //leafNodes.forEach(space-> System.out.println(space.toString()));
//            //Agreements<LeafNode> agreements = Agreements.newAgreements(Collections.unmodifiableList(new ArrayList<>(leafNodes)), Functions.actualCase);
//            //REPLACE THE FOLLOWING LINE
//            Agreements<LeafNode> agreements = Agreements.newAgreements(Collections.unmodifiableList(new ArrayList<>(leafNodes)), null,null);
//
//            agreements.createEdges();
//            this.agreements.put(key, agreements);
//            return agreements;
//        }
//    }
//
//    private String[] leavesToIds(List<LeafNode> leafNodes){
//        String[] partitions = new String[leafNodes.size()];
//        for (int i = 0; i < leafNodes.size(); i++) {
//            partitions[i] = String.valueOf(leafNodes.get(i).getId());
//        }
//        return partitions;
//    }
//
//    private String[] getPartitions(double x, double y, TypeSet typeSet){
//        Point point = Point.newPoint(x,y);
//        Circle circle = Circle.newCircle(point,r);
//
//        List<LeafNode> partitions = null;
//        List<LeafNode> queryResults = new ArrayList<>();
//        rangeQuery(circle.getMbr(), root, queryResults);
//        //System.out.println(Arrays.toString(leavesToIds(partitions)));
//        if(queryResults.size() == 1){
//            partitions = queryResults;
//        }else{
//            int index = -1;
//            for (int i = 0; i < queryResults.size(); i++) {
//                if (queryResults.get(i).getRectangle().contains(point)){
//                    index = i;
//                    break;
//                }
//            }
//            if(queryResults.size()==2){
//                partitions = getAgreements(queryResults).getPartitionsTwoSpacesCase(queryResults.get(index), (index==0)?queryResults.get(1):queryResults.get(0), typeSet);
//            }else if(queryResults.size()==3){
//                int indexRemove = -1;
//                for (int i = 0; i < queryResults.size(); i++) {
//                    if(i != index){
//                        if(intersects(circle, queryResults.get(i).getRectangle())){
//                            continue;
//                        }
//                        if(indexRemove == -1){
//                            indexRemove = i;
//                        }else{
//                            try {
//                                throw new Exception("A second space should not be removed from a case of three spaces");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//
//                Agreements<LeafNode> agreements = getAgreements(queryResults);
//                if(indexRemove !=-1){
//                    LeafNode headNode = queryResults.get(index);
//
//                    //System.out.println("KEY: "+getKey(queryResults)+" "+index);
//
//                    queryResults.remove(indexRemove);
//                    queryResults.remove(headNode);
////                    partitions = agreements.getPartitions(headNode, queryResults.get(0), typeSet);
//                    //System.out.println("x:"+x+" y:"+y);
//                    //System.out.println("Three spaces case - removed");
//                }else{
//                    partitions = agreements.getPartitionsThreeSpacesCase(queryResults.get(index), typeSet);
//                }
//            }else if(queryResults.size() == 4){
//                boolean outerQuarter = false;
//
//                for (LeafNode partition : queryResults) {
//                    if(!intersects(circle, partition.getRectangle())){
//                        outerQuarter = true;
//                        break;
//                    }
//                }
//                partitions = getAgreements(queryResults).getPartitionsFourSpacesCase(queryResults.get(index), typeSet, outerQuarter);
//            }else{
//                try {
//                    throw new Exception("Problem with the range query. Does not fetch the correct number of leaf nodes.");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return leavesToIds(partitions);
//    }
//
//    private void rangeQuery(Rectangle rectangle, Node node, List<LeafNode> leafNodeList){
//        if(node instanceof NonLeafNode){
//            for (Node child : ((NonLeafNode) node).getChildren()) {
//                    if(rectangle.intersectsWith(child.getRectangle())){
//                        rangeQuery(rectangle, child, leafNodeList);
//                    }
//            }
//        }else{
//            leafNodeList.add((LeafNode) node);
//        }
//    }
//
////    public double determineRadiusOfPoint(int k, Point point) {
////        Node node = determineNodeForRadiusEstimation(k, point, root);
////
////        double distance;
////
////        double d1 = harvesine(point.getX(), point.getY(), node.getUpperBoundx(), node.getUpperBoundy());
////        distance = d1;
////
////        double d2 = harvesine(point.getX(), point.getY(), node.getLowerBoundx(), node.getLowerBoundy());
////
////        if (Double.compare(d2, distance) == 1) {
////            distance = d2;
////        }
////
////        double d3 = harvesine(point.getX(), point.getY(), node.getUpperBoundx(), node.getLowerBoundy());
////
////        if (Double.compare(d3, distance) == 1) {
////            distance = d3;
////        }
////
////        double d4 = harvesine(point.getX(), point.getY(), node.getLowerBoundx(), node.getUpperBoundy());
////
////        if (Double.compare(d4, distance) == 1) {
////            distance = d4;
////        }
////
////        return distance;
////
////    }
//
//    private static double harvesine(double lon1, double lat1, double lon2, double lat2) {
//
//        double r = 6378.1;
//
//        double f1 = Math.toRadians(lat1);
//        double f2 = Math.toRadians(lat2);
//
//        double df = Math.toRadians(lat2 - lat1);
//        double dl = Math.toRadians(lon2 - lon1);
//
//        double a = Math.sin(df / 2) * Math.sin(df / 2) + Math.cos(f1) * Math.cos(f2) * Math.sin(dl / 2) * Math.sin(dl / 2);
//
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//
//        return r * c;
//    }
//
//
//
//    private LeafNode getLeafNode(Node node, Point point) {
//        if(node instanceof NonLeafNode){
//            for (Node child : ((NonLeafNode) node).getChildren()) {
//                if(child.getRectangle().contains(point)){
//                    return getLeafNode(child, point);
//                }
//            }
//        }else if(node instanceof LeafNode){
//            if(node.getRectangle().contains(point)){
//                return (LeafNode) node;
//            }
//        }else{
//            try {
//                throw new Exception("Problem for determining the node for point insertion");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    public void serializeQuadTree(String exportPath) throws IOException {
//
//        File file = new File(exportPath);
//        if (file.getParentFile().mkdir()) {
//            file.createNewFile();
//        }
//
//        Kryo kryo = new Kryo();
//        kryo.register(QuadTree.class);
//        kryo.register(Node.class);
//        kryo.register(Point[].class);
//        kryo.register(Point.class);
//        kryo.setReferences(true);
//
//
//        Output output = new Output(new FileOutputStream(exportPath));
//        kryo.writeObject(output, this);
//        output.close();
//
//    }
//
////    public static QuadTree deserializeQuadTree(String pathOfBinFile) throws FileNotFoundException {
////        Kryo kryo = new Kryo();
////        kryo.register(QuadTree.class);
////        kryo.register(Node.class);
////        kryo.register(Point[].class);
////        kryo.register(Point.class);
////        kryo.setReferences(true);
////        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
////
////        Input input = new Input(new FileInputStream(pathOfBinFile));
////
////        QuadTree quadTree = kryo.readObject(input, QuadTree.class);
////        input.close();
////        return quadTree;
////    }
//
//    private static boolean intersects(Circle circle, Rectangle rectangle){
//        double rectangleCenterX = ((rectangle.getUpperBound().getX() + rectangle.getLowerBound().getX())/2);
//        double rectangleCenterY = ((rectangle.getUpperBound().getY() + rectangle.getLowerBound().getY())/2);
//
//        double circleDistanceX = abs(circle.getCenter().getX() - rectangleCenterX);
//        double circleDistanceY = abs(circle.getCenter().getY() - rectangleCenterY);
//
//        if(Double.compare(circleDistanceX,((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/2)) != 1){
//            return true;
//        }
//
//        if(Double.compare(circleDistanceY,((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/2)) != 1){
//            return true;
//        }
//
//        double cornerDistance = Math.pow(circleDistanceX-((rectangle.getUpperBound().getX() - rectangle.getLowerBound().getX())/2),2) + Math.pow(circleDistanceY-((rectangle.getUpperBound().getY() - rectangle.getLowerBound().getY())/2),2);
//
//        if(Double.compare(circle.getCenter().getX(), rectangleCenterX) == -1 && Double.compare(circle.getCenter().getY(), rectangleCenterY) == -1){
//            return (Double.compare(cornerDistance, Math.pow(circle.getRadius(),2)) != 1);
//        }else{
//            return (Double.compare(cornerDistance, Math.pow(circle.getRadius(),2)) == -1);
//        }
//    }
//
//    public static double getR() {
//        return r;
//    }
//
//}
