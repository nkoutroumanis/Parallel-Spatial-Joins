package gr.ds.unipi.quadtree;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Edge;
import gr.ds.unipi.shapes.Circle;
import gr.ds.unipi.shapes.Point;
import gr.ds.unipi.shapes.Rectangle;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Functions {

    static BiFunction<LeafNode, LeafNode, Agreement> datasetA = (c1, c2) -> {
        return Agreement.newAgreement(Edge.newEdge(c1, c2, TypeSet.A, 0), Edge.newEdge(c2, c1, TypeSet.A, 0));
    };

    static BiFunction<LeafNode, LeafNode, Agreement> datasetB = (c1, c2) -> {
        return Agreement.newAgreement(Edge.newEdge(c1, c2, TypeSet.B, 0), Edge.newEdge(c2, c1, TypeSet.B, 0));
    };

    static BiFunction<LeafNode, LeafNode, Agreement> actualCase = (c1, c2) -> {

        long a1 = 0;
        long b1 = 0;
        long a2 = 0;
        long b2 = 0;

        if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            Circle circle = Circle.newCircle(c1.getRectangle().getUpperLeftBound(), QuadTree.getR());
            System.out.println(circle);
            a1 = queryLeafPoints(c1.getDatasetA(), circle, false);
            b1 = queryLeafPoints(c1.getDatasetB(), circle, false);
            a2 = queryLeafPoints(c2.getDatasetA(), circle, false);
            b2 = queryLeafPoints(c2.getDatasetB(), circle, false);
        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            Circle circle = Circle.newCircle(c1.getRectangle().getUpperBound(), QuadTree.getR());
            System.out.println(circle);
            a1 = queryLeafPoints(c1.getDatasetA(), circle, true);
            b1 = queryLeafPoints(c1.getDatasetB(), circle, true);
            a2 = queryLeafPoints(c2.getDatasetA(), circle, false);
            b2 = queryLeafPoints(c2.getDatasetB(), circle, false);
        }else if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getUpperLeftBound())){
            System.out.println("RECALL1");
             return Agreement.reverseAgreement(Functions.actualCase.apply(c2,c1));
        }else if(c1.getRectangle().getLowerBound().equals(c2.getRectangle().getUpperBound())){
            System.out.println("RECALL2");
            return Agreement.reverseAgreement(Functions.actualCase.apply(c2,c1));
        }else if(Double.compare(c1.getRectangle().getUpperBound().getY(), c2.getRectangle().getLowerBound().getY())==0){
            //top
            List<Double> values = Stream.of(c1.getRectangle().getLowerBound().getX(), c1.getRectangle().getUpperBound().getX(), c2.getRectangle().getLowerBound().getX(), c2.getRectangle().getUpperBound().getX()).sorted(Comparator.comparingDouble(d -> d)).distinct().collect(Collectors.toList());
            if(values.size() == 4){
                values.remove(0);
                values.remove(2);
            }else if(values.size()==3){
                if(Double.compare(c1.getRectangle().getLowerBound().getX(), c2.getRectangle().getLowerBound().getX())==0){
                    values.remove(2);
                }else if(Double.compare(c1.getRectangle().getUpperBound().getX(), c2.getRectangle().getUpperBound().getX())==0){
                    values.remove(0);
                }else{
                    try {
                        throw new Exception("One of the two X values of the two spaces should be equal");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(values.size() != 2){
                try {
                    throw new Exception("2 values should exist in the list");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Rectangle c1Rect = Rectangle.newRectangle(Point.newPoint(values.get(0),c1.getRectangle().getUpperBound().getY()- QuadTree.getR()),Point.newPoint(values.get(1),c1.getRectangle().getUpperBound().getY()));
            Rectangle c2Rect = Rectangle.newRectangle(Point.newPoint(values.get(0),c2.getRectangle().getLowerBound().getY()),Point.newPoint(values.get(1),c2.getRectangle().getLowerBound().getY() + QuadTree.getR()));

            Circle circle1 = Circle.newCircle(Point.newPoint(values.get(0), c1.getRectangle().getUpperBound().getY()), QuadTree.getR());
            Circle circle2 = Circle.newCircle(Point.newPoint(values.get(1), c1.getRectangle().getUpperBound().getY()), QuadTree.getR());

            System.out.println(c1Rect);
            System.out.println(c2Rect);
            System.out.println(circle1);
            System.out.println(circle2);
            System.out.print(c1.getId()+" ");

            if(Double.compare(c1.getRectangle().getLowerBound().getX(),  values.get(0)) == 0 && Double.compare(c1.getRectangle().getUpperBound().getX(),  values.get(1)) == 0){
                System.out.print("c1Rect ");
                a1 = queryLeafPoints(c1.getDatasetA(), c1Rect);
                b1 = queryLeafPoints(c1.getDatasetB(), c1Rect);
            }else if(Double.compare(c1.getRectangle().getLowerBound().getX(),  values.get(0)) == 0){
                System.out.print("c1Rect ");
                System.out.print("circle2 ");
                a1 = queryLeafPoints(c1.getDatasetA(), c1Rect, circle2, true);
                b1 = queryLeafPoints(c1.getDatasetB(), c1Rect, circle2, true);
            }else if(Double.compare(c1.getRectangle().getUpperBound().getX(),  values.get(1)) == 0){
                System.out.print("c1Rect ");
                System.out.print("circle1 ");
                a1 = queryLeafPoints(c1.getDatasetA(), c1Rect, circle1, true);
                b1 = queryLeafPoints(c1.getDatasetB(), c1Rect, circle1, true);
            }else{
                System.out.print("c1Rect ");
                System.out.print("circle1 ");
                System.out.print("circle2 ");
                a1 = queryLeafPoints(c1.getDatasetA(), c1Rect, circle1, circle2, true);
                b1 = queryLeafPoints(c1.getDatasetB(), c1Rect, circle1, circle2, true);
            }
            System.out.print(c2.getId()+" ");

            if(Double.compare(c2.getRectangle().getLowerBound().getX(),  values.get(0)) == 0 && Double.compare(c2.getRectangle().getUpperBound().getX(),  values.get(1)) == 0){
                System.out.print("c2Rect ");
                a2 = queryLeafPoints(c2.getDatasetA(), c2Rect);
                b2 = queryLeafPoints(c2.getDatasetB(), c2Rect);
            }else if(Double.compare(c2.getRectangle().getLowerBound().getX(),  values.get(0)) == 0){
                System.out.print("c2Rect ");
                System.out.print("circle2 ");
                a2 = queryLeafPoints(c2.getDatasetA(), c2Rect, circle2, false);
                b2 = queryLeafPoints(c2.getDatasetB(), c2Rect, circle2, false);
            }else if(Double.compare(c2.getRectangle().getUpperBound().getX(),  values.get(1)) == 0){
                System.out.print("c2Rect ");
                System.out.print("circle1 ");
                a2 = queryLeafPoints(c2.getDatasetA(), c2Rect, circle1, false);
                b2 = queryLeafPoints(c2.getDatasetB(), c2Rect, circle1, false);
            }else{
                System.out.print("c2Rect ");
                System.out.print("circle1 ");
                System.out.print("circle2 ");
                a2 = queryLeafPoints(c2.getDatasetA(), c2Rect, circle1, circle2, false);
                b2 = queryLeafPoints(c2.getDatasetB(), c2Rect, circle1, circle2, false);
            }
        }else if(Double.compare(c1.getRectangle().getUpperBound().getX(), c2.getRectangle().getLowerBound().getX())==0){
            //right
            List<Double> values = Stream.of(c1.getRectangle().getLowerBound().getY(), c1.getRectangle().getUpperBound().getY(), c2.getRectangle().getLowerBound().getY(), c2.getRectangle().getUpperBound().getY()).sorted(Comparator.comparingDouble(d -> d)).distinct().collect(Collectors.toList());
            if(values.size() == 4){
                values.remove(0);
                values.remove(2);
            }else if(values.size()==3){
                if(Double.compare(c1.getRectangle().getLowerBound().getY(), c2.getRectangle().getLowerBound().getY())==0){
                    values.remove(2);
                }else if(Double.compare(c1.getRectangle().getUpperBound().getY(), c2.getRectangle().getUpperBound().getY())==0){
                    values.remove(0);
                }else{
                    try {
                        throw new Exception("One of the two Y values of the two spaces should be equal");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(values.size() != 2){
                try {
                    throw new Exception("2 values should exist in the list");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Rectangle c1Rect = Rectangle.newRectangle(Point.newPoint(c1.getRectangle().getUpperBound().getX() - QuadTree.getR(),values.get(0)),Point.newPoint(c1.getRectangle().getUpperBound().getX(),values.get(1)));
            Rectangle c2Rect = Rectangle.newRectangle(Point.newPoint(c2.getRectangle().getLowerBound().getX(),values.get(0)),Point.newPoint(c2.getRectangle().getLowerBound().getX() + QuadTree.getR(),values.get(1)));

            Circle circle1 = Circle.newCircle(Point.newPoint(c1.getRectangle().getUpperBound().getX(), values.get(0)), QuadTree.getR());
            Circle circle2 = Circle.newCircle(Point.newPoint(c1.getRectangle().getUpperBound().getX(), values.get(1)), QuadTree.getR());

            System.out.println(c1Rect);
            System.out.println(c2Rect);
            System.out.println(circle1);
            System.out.println(circle2);

            System.out.print(c1.getId()+" ");
            if(Double.compare(c1.getRectangle().getLowerBound().getY(),  values.get(0)) == 0 && Double.compare(c1.getRectangle().getUpperBound().getY(),  values.get(1)) == 0){
                System.out.print("c1Rect ");
                a1 = queryLeafPoints(c1.getDatasetA(), c1Rect);
                b1 = queryLeafPoints(c1.getDatasetB(), c1Rect);
            }else if(Double.compare(c1.getRectangle().getLowerBound().getY(),  values.get(0)) == 0){
                System.out.print("c1Rect ");
                System.out.print("circle2 ");

                a1 = queryLeafPoints(c1.getDatasetA(), c1Rect, circle2, true);
                b1 = queryLeafPoints(c1.getDatasetB(), c1Rect, circle2, true);
            }else if(Double.compare(c1.getRectangle().getUpperBound().getY(),  values.get(1)) == 0){
                System.out.print("c1Rect ");
                System.out.print("circle1 ");
                a1 = queryLeafPoints(c1.getDatasetA(), c1Rect, circle1, true);
                b1 = queryLeafPoints(c1.getDatasetB(), c1Rect, circle1, true);
            }else{
                System.out.print("c1Rect ");
                System.out.print("circle1 ");
                System.out.print("circle2 ");
                a1 = queryLeafPoints(c1.getDatasetA(), c1Rect, circle1, circle2, true);
                b1 = queryLeafPoints(c1.getDatasetB(), c1Rect, circle1, circle2, true);
            }

            System.out.print(c2.getId()+" ");
            if(Double.compare(c2.getRectangle().getLowerBound().getY(),  values.get(0)) == 0 && Double.compare(c2.getRectangle().getUpperBound().getY(),  values.get(1)) == 0){
                System.out.print("c2Rect ");
                a2 = queryLeafPoints(c2.getDatasetA(), c2Rect);
                b2 = queryLeafPoints(c2.getDatasetB(), c2Rect);
            }else if(Double.compare(c2.getRectangle().getLowerBound().getY(),  values.get(0)) == 0){
                System.out.print("c2Rect ");
                System.out.print("circle2 ");
                a2 = queryLeafPoints(c2.getDatasetA(), c2Rect, circle2, false);
                b2 = queryLeafPoints(c2.getDatasetB(), c2Rect, circle2, false);
            }else if(Double.compare(c2.getRectangle().getUpperBound().getY(),  values.get(1)) == 0){
                System.out.print("c2Rect ");
                System.out.print("circle1 ");
                a2 = queryLeafPoints(c2.getDatasetA(), c2Rect, circle1, false);
                b2 = queryLeafPoints(c2.getDatasetB(), c2Rect, circle1, false);
            }else{
                System.out.print("c2Rect ");
                System.out.print("circle1 ");
                System.out.print("circle2 ");
                a2 = queryLeafPoints(c2.getDatasetA(), c2Rect, circle1, circle2, false);
                b2 = queryLeafPoints(c2.getDatasetB(), c2Rect, circle1, circle2, false);
            }

        }else if(Double.compare(c1.getRectangle().getLowerBound().getY(), c2.getRectangle().getUpperBound().getY())==0){
            //bottom
            System.out.println("RECALL3");
            return Agreement.reverseAgreement(Functions.actualCase.apply(c2,c1));
        }else if(Double.compare(c1.getRectangle().getLowerBound().getX(), c2.getRectangle().getUpperBound().getX())==0){
            //left
            System.out.println("RECALL4");
            return Agreement.reverseAgreement(Functions.actualCase.apply(c2,c1));
        }else{
            try {
                throw new Exception("The spaces of the leaves are not touching");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("a1="+a1+" b1="+b1+" a2="+a2+" b2="+b2);
        return Agreement.newAgreement(Edge.newEdge(c1, c2, TypeSet.A, 0), Edge.newEdge(c2, c1, TypeSet.A, 0));

    };

    private static long queryLeafPoints(List<Point> points, Rectangle rectangle, Circle circle1, Circle circle2, boolean isInclusive){
        Set<Long> set = queryLeafPointsRectangle(points, rectangle);
        set.addAll(queryLeafPointsCircle(points, circle1, isInclusive));
        set.addAll(queryLeafPointsCircle(points, circle2, isInclusive));
        return set.size();
    }

    private static long queryLeafPoints(List<Point> points, Rectangle rectangle, Circle circle, boolean isInclusive){
        Set<Long> set = queryLeafPointsRectangle(points, rectangle);
        set.addAll(queryLeafPointsCircle(points, circle, isInclusive));
        return set.size();
    }

    private static long queryLeafPoints(List<Point> points, Rectangle rectangle){
        return queryLeafPointsRectangle(points, rectangle).size();
    }

    private static long queryLeafPoints(List<Point> points, Circle circle, boolean isInclusive){
        return queryLeafPointsCircle(points, circle, isInclusive).size();
    }

    private static Set<Long> queryLeafPointsRectangle(List<Point> points, Rectangle rectangle){
        AtomicLong i = new AtomicLong(1);
        return points.stream().map(point->{ return new AbstractMap.SimpleImmutableEntry<>(i.getAndIncrement(), point);
        }).filter(entry->rectangle.contains(entry.getValue())).map(AbstractMap.SimpleImmutableEntry::getKey).collect(Collectors.toSet());
    }

    private static Set<Long> queryLeafPointsCircle(List<Point> points, Circle circle, boolean isInclusive){
        AtomicLong i = new AtomicLong(1);
        Stream<AbstractMap.SimpleImmutableEntry<Long,Point>> stream = points.stream().map(point->{ return new AbstractMap.SimpleImmutableEntry<>(i.getAndIncrement(), point); });
        if(isInclusive){
            return stream.filter(entry->circle.containsInclusive(entry.getValue())).map(AbstractMap.SimpleImmutableEntry::getKey).collect(Collectors.toSet());
        }
        return stream.filter(entry->circle.containsExclusive(entry.getValue())).map(AbstractMap.SimpleImmutableEntry::getKey).collect(Collectors.toSet());
    }

}
