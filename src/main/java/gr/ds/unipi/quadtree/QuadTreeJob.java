package gr.ds.unipi.quadtree;

import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;

import java.util.Arrays;
import java.util.Random;

public class QuadTreeJob {

    public static void main(String args[]){

        QuadTree quadTree = QuadTree.newQuadTree(Rectangle.newRectangle(Point.newPoint(0,0),Point.newPoint(100,100)), 14, 6);

        quadTree.insertPointDatasetA(Point.newPoint(23,65));
        quadTree.insertPointDatasetA(Point.newPoint(34,76));
        quadTree.insertPointDatasetA(Point.newPoint(15,87));
        quadTree.insertPointDatasetB(Point.newPoint(23,65));
        quadTree.insertPointDatasetB(Point.newPoint(34,76));
        quadTree.insertPointDatasetB(Point.newPoint(15,87));

        quadTree.insertPointDatasetA(Point.newPoint(78,84));
        quadTree.insertPointDatasetA(Point.newPoint(80,74));
        quadTree.insertPointDatasetA(Point.newPoint(90,90));
        quadTree.insertPointDatasetB(Point.newPoint(78,84));
        quadTree.insertPointDatasetB(Point.newPoint(80,74));
        quadTree.insertPointDatasetB(Point.newPoint(90,90));

        quadTree.insertPointDatasetA(Point.newPoint(78,34));
        quadTree.insertPointDatasetA(Point.newPoint(80,20));
        quadTree.insertPointDatasetA(Point.newPoint(90,10));
        quadTree.insertPointDatasetB(Point.newPoint(78,34));
        quadTree.insertPointDatasetB(Point.newPoint(80,20));
        quadTree.insertPointDatasetB(Point.newPoint(90,10));

        quadTree.insertPointDatasetA(Point.newPoint(12,12));
        quadTree.insertPointDatasetB(Point.newPoint(12,12));

        quadTree.insertPointDatasetA(Point.newPoint(15,32));
        quadTree.insertPointDatasetB(Point.newPoint(15,32));

        quadTree.insertPointDatasetA(Point.newPoint(35,15));
        quadTree.insertPointDatasetB(Point.newPoint(35,15));

        quadTree.insertPointDatasetA(Point.newPoint(36,47));
        quadTree.insertPointDatasetB(Point.newPoint(36,47));

        quadTree.insertPointDatasetA(Point.newPoint(45,45));
        quadTree.insertPointDatasetB(Point.newPoint(45,45));

        quadTree.insertPointDatasetA(Point.newPoint(45,30));
        quadTree.insertPointDatasetB(Point.newPoint(45,30));

        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));
        quadTree.insertPointDatasetA(Point.newPoint(36,36));
        quadTree.insertPointDatasetB(Point.newPoint(36,36));

        quadTree.insertPointDatasetA(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetB(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetA(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetB(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetA(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetB(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetA(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetB(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetA(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetB(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetA(Point.newPoint(62.5,87.5));
        quadTree.insertPointDatasetB(Point.newPoint(62.5,87.5));

        quadTree.insertPointDatasetA(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetB(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetA(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetB(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetA(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetB(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetA(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetB(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetA(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetB(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetA(Point.newPoint(87.5,62.5));
        quadTree.insertPointDatasetA(Point.newPoint(87.5,62.5));

        quadTree.assignIdToLeaves();
        System.out.println(quadTree.toString());

        Random r = new Random();
        for(int i=0;i<1000000;i++)
        {
            quadTree.getPartitionsBType(Math.random()*100,Math.random()*100);
            //System.out.println(Arrays.toString(quadTree.getPartitionsBType(Math.random()*100,Math.random()*100)));
        }
        //System.out.println(Arrays.toString(quadTree.getPartitionsBType(50, 49)));

        //        quadTree.insertPoint(Point.newPoint(1,1));
//        quadTree.insertPoint(Point.newPoint(3,3));
//        quadTree.insertPoint(Point.newPoint(4,4));
//        quadTree.insertPoint(Point.newPoint(4,1));
//        quadTree.insertPoint(Point.newPoint(5,5));
//        quadTree.insertPoint(Point.newPoint(5.5,5.5));
//        quadTree.insertPoint(Point.newPoint(6.6,6.7));

//        System.out.println(quadTree.determineRadiusOfPoint(1, Point.newPoint(0,0)));
//        System.out.println("Number of contained points of Tree "+ quadTree.getNumberOfInsertedPoints());
//        System.out.println(QuadTree.o);


    }

}
