package gr.ds.unipi.self.grid;

import gr.ds.unipi.grid.Function4;
import gr.ds.unipi.self.agreements.Edge;
import gr.ds.unipi.shapes.Point;

import java.io.Serializable;

public class ReplicationFunc {

    public static Function4<Cell, Cell, Point, Edge> lesserPointsinBoundaries = (Function4<Cell, Cell, Point, Edge> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        boolean direction = true;
        int cOcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInRightSpecialArea();
            c2A = c2.getNumberOfPointsInLeftSpecialArea();

            if (c1A <= c2A) {
                direction = true;
            } else {
                direction = false;
            }
            if(p.equals(c1.getRectangle().getLowerRightBound()) /*&& p.equals(c2.getRectangle().getLowerBound())*/){
                if(direction){
                    cOcurredCost = c1.getNumberOfPointsInBottomRightQuarterArea() * c2.getNumberOfPoints();
                }else{
                    cOcurredCost = c2.getNumberOfPointsInBottomLeftQuarterArea() * c1.getNumberOfPoints();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) /*&& p.equals(c2.getRectangle().getUpperLeftBound())*/){
                if(direction){
                    cOcurredCost = c1.getNumberOfPointsInTopRightQuarterArea() * c2.getNumberOfPoints();
                }else{
                    cOcurredCost = c2.getNumberOfPointsInTopLeftQuarterArea() * c1.getNumberOfPoints();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInTopSpecialArea();
            c2A = c2.getNumberOfPointsInBottomSpecialArea();

            if (c1A <= c2A) {
                direction = true;
            } else {
                direction = false;
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) /*&& p.equals(c2.getRectangle().getLowerBound())*/){
                if(direction){
                    cOcurredCost = c1.getNumberOfPointsInTopLeftQuarterArea() * c2.getNumberOfPoints();
                }else{
                    cOcurredCost = c2.getNumberOfPointsInBottomLeftQuarterArea() * c1.getNumberOfPoints();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) /*&& p.equals(c2.getRectangle().getLowerRightBound())*/){
                if(direction){
                    cOcurredCost = c1.getNumberOfPointsInTopRightQuarterArea() * c2.getNumberOfPoints();
                }else{
                    cOcurredCost = c2.getNumberOfPointsInBottomRightQuarterArea() * c1.getNumberOfPoints();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsInBottomLeftQuarterArea();

            if(c1A==c2A){
                direction = false;
            } else if (c1A < c2A) {
                direction = true;
//                direction = false;
                cOcurredCost = c1A*c2.getNumberOfPoints();
            } else {
                direction = false;
//                direction = true;
                cOcurredCost = c2A*c1.getNumberOfPoints();
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsInBottomRightQuarterArea();

            if (c1A <= c2A) {
                direction = true;
                cOcurredCost = c1A*c2.getNumberOfPoints();
            } else {
                direction = false;
                cOcurredCost = c2A*c1.getNumberOfPoints();
            }
        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Edge.newEdge(direction, cOcurredCost);

    };

    public static Function4<Cell, Cell, Point, Edge> pointsInCells = (Function4<Cell, Cell, Point, Edge> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        boolean direction = true;
        int cOcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInRightSpecialArea();
            c2A = c2.getNumberOfPointsInLeftSpecialArea();

            if (c1.getNumberOfPoints()>=c2.getNumberOfPoints()) {
                direction = true;
            } else {
                direction = false;
            }
            if(p.equals(c1.getRectangle().getLowerRightBound()) /*&& p.equals(c2.getRectangle().getLowerBound())*/){
                if(direction){
                    cOcurredCost = c1.getNumberOfPointsInBottomRightQuarterArea() * c2.getNumberOfPoints();
                }else{
                    cOcurredCost = c2.getNumberOfPointsInBottomLeftQuarterArea() * c1.getNumberOfPoints();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) /*&& p.equals(c2.getRectangle().getUpperLeftBound())*/){
                if(direction){
                    cOcurredCost = c1.getNumberOfPointsInTopRightQuarterArea() * c2.getNumberOfPoints();
                }else{
                    cOcurredCost = c2.getNumberOfPointsInTopLeftQuarterArea() * c1.getNumberOfPoints();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInTopSpecialArea();
            c2A = c2.getNumberOfPointsInBottomSpecialArea();

            if (c1.getNumberOfPoints()>=c2.getNumberOfPoints()) {
                direction = true;
            } else {
                direction = false;
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) /*&& p.equals(c2.getRectangle().getLowerBound())*/){
                if(direction){
                    cOcurredCost = c1.getNumberOfPointsInTopLeftQuarterArea() * c2.getNumberOfPoints();
                }else{
                    cOcurredCost = c2.getNumberOfPointsInBottomLeftQuarterArea() * c1.getNumberOfPoints();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) /*&& p.equals(c2.getRectangle().getLowerRightBound())*/){
                if(direction){
                    cOcurredCost = c1.getNumberOfPointsInTopRightQuarterArea() * c2.getNumberOfPoints();
                }else{
                    cOcurredCost = c2.getNumberOfPointsInBottomRightQuarterArea() * c1.getNumberOfPoints();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsInBottomLeftQuarterArea();

            if(c1A==c2A){
                direction = false;
            } else if (c1.getNumberOfPoints()>c2.getNumberOfPoints()) {
                direction = true;
//                direction = false;
                cOcurredCost = c1A*c2.getNumberOfPoints();
            } else {
                direction = false;
//                direction = true;
                cOcurredCost = c2A*c1.getNumberOfPoints();
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsInBottomRightQuarterArea();

            if (c1.getNumberOfPoints()>=c2.getNumberOfPoints()) {
                direction = true;
                cOcurredCost = c1A*c2.getNumberOfPoints();
            } else {
                direction = false;
                cOcurredCost = c2A*c1.getNumberOfPoints();
            }
        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Edge.newEdge(direction, cOcurredCost);

    };
}
