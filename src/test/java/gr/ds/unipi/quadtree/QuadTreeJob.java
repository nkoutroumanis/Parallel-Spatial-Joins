//package gr.ds.unipi.quadtree;
//
//import gr.ds.unipi.shapes.Point;
//import gr.ds.unipi.shapes.Rectangle;
//import org.junit.jupiter.api.Test;
//
//import java.util.Random;
//
//public class QuadTreeJob {
//
//    @Test
//    public void test(){
//
//        QuadTree quadTree = QuadTree.newQuadTree(Rectangle.newRectangle(Point.newPoint(0,0),Point.newPoint(100,100)), 14, 6);
//
//        Random r = new Random();
//        for(int i=0;i<20000000;i++)
//        {
//            quadTree.insertPointDatasetA(Point.newPoint(Math.random()*100,Math.random()*100));
//            quadTree.insertPointDatasetB(Point.newPoint(Math.random()*100,Math.random()*100));
//            //System.out.println(Arrays.toString(quadTree.getPartitionsBType(Math.random()*100,Math.random()*100)));
//        }
//        quadTree.assignIdToLeaves();
//        System.out.println(quadTree.toString());
//        for(int i=0;i<30000000;i++){
//            quadTree.getPartitionsAType(Math.random()*100,Math.random()*100);
//            quadTree.getPartitionsBType(Math.random()*100,Math.random()*100);
//        }
//
//        //System.out.println(Arrays.toString(quadTree.getPartitionsBType(50, 49)));
//
//        //        quadTree.insertPoint(Point.newPoint(1,1));
////        quadTree.insertPoint(Point.newPoint(3,3));
////        quadTree.insertPoint(Point.newPoint(4,4));
////        quadTree.insertPoint(Point.newPoint(4,1));
////        quadTree.insertPoint(Point.newPoint(5,5));
////        quadTree.insertPoint(Point.newPoint(5.5,5.5));
////        quadTree.insertPoint(Point.newPoint(6.6,6.7));
//
////        System.out.println(quadTree.determineRadiusOfPoint(1, Point.newPoint(0,0)));
////        System.out.println("Number of contained points of Tree "+ quadTree.getNumberOfInsertedPoints());
////        System.out.println(QuadTree.o);
//
//
//    }
//
//}
