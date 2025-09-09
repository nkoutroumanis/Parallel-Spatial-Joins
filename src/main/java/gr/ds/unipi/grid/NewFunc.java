package gr.ds.unipi.grid;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.shapes.Point;

import java.io.Serializable;

public class NewFunc {
    //public static TriFunction<Integer, Integer, Cell, Cell, Point, Agreement> function;

    public static Function4<Cell, Cell, Point, Agreement> datasetA = (Function4<Cell, Cell, Point, Agreement>) (c1, c2, p) -> {
        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(TypeSet.A, 0), gr.ds.unipi.agreements.Edge.newEdge(TypeSet.A, 0));
    };

    public static Function4<Cell, Cell, Point, Agreement> datasetB = (Function4<Cell, Cell, Point, Agreement> & Serializable) (c1, c2, p) -> {
        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(TypeSet.B, 0), gr.ds.unipi.agreements.Edge.newEdge(TypeSet.B, 0));
    };

    public static Function4<Cell, Cell, Point, Agreement> lesserPointsinBoundaries = (Function4<Cell, Cell, Point, Agreement> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        int c1B = 0;
        int c2B = 0;

        TypeSet set = null;

        int c1OcurredCost = 0;
        int c2OcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
            if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }
            if(p.equals(c1.getRectangle().getLowerRightBound()) /*&& p.equals(c2.getRectangle().getLowerBound())*/){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInBottomRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInBottomRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) /*&& p.equals(c2.getRectangle().getUpperLeftBound())*/){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInTopLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInTopLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
            if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) /*&& p.equals(c2.getRectangle().getLowerBound())*/){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopLeftQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopLeftQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) /*&& p.equals(c2.getRectangle().getLowerRightBound())*/){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomRightQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomRightQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();
            if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
                c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                c2OcurredCost = c1A*c2.getNumberOfPointsBType();
            } else {
                set = TypeSet.B;
                c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                c2OcurredCost = c1B*c2.getNumberOfPointsAType();
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
            if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
                c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                c2OcurredCost = c1A*c2.getNumberOfPointsBType();
            } else {
                set = TypeSet.B;
                c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                c2OcurredCost = c1B*c2.getNumberOfPointsAType();
            }
        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(set, c2OcurredCost), gr.ds.unipi.agreements.Edge.newEdge(set, c1OcurredCost));

    };

    public static Function4<Cell, Cell, Point, Agreement> costBasedCombinedWithBoundaries = (Function4<Cell, Cell, Point, Agreement> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        int c1B = 0;
        int c2B = 0;

        TypeSet set = null;

        int c1OcurredCost = 0;
        int c2OcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
            if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) < (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType()) /*&&
                    (c1.getNumberOfPointsAType()!=0) && (c1.getNumberOfPointsBType()!=0) && (c2.getNumberOfPointsAType()!=0) && (c2.getNumberOfPointsBType()!=0)*/) {
                set = TypeSet.A;
            } else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) > (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType()) /*&&
                    (c1.getNumberOfPointsAType()!=0) && (c1.getNumberOfPointsBType()!=0) && (c2.getNumberOfPointsAType()!=0) && (c2.getNumberOfPointsBType()!=0)*/) {
                set = TypeSet.B;
            } else if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }
            if(p.equals(c1.getRectangle().getLowerRightBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInBottomRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInBottomRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getUpperLeftBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInTopLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInTopLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
            if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) < (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType()) /*&&
                    (c1.getNumberOfPointsAType()!=0) && (c1.getNumberOfPointsBType()!=0) && (c2.getNumberOfPointsAType()!=0) && (c2.getNumberOfPointsBType()!=0)*/) {
                set = TypeSet.A;
            } else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) > (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType()) /*&&
                    (c1.getNumberOfPointsAType()!=0) && (c1.getNumberOfPointsBType()!=0) && (c2.getNumberOfPointsAType()!=0) && (c2.getNumberOfPointsBType()!=0)*/) {
                set = TypeSet.B;
            } else if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopLeftQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopLeftQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getLowerRightBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomRightQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomRightQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();


            if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) < (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType()) /*&&
                    (c1.getNumberOfPointsAType()!=0) && (c1.getNumberOfPointsBType()!=0) && (c2.getNumberOfPointsAType()!=0) && (c2.getNumberOfPointsBType()!=0)*/) {
                set = TypeSet.A;
                c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                c2OcurredCost = c1A*c2.getNumberOfPointsBType();
            } else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) > (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType()) /*&&
                    (c1.getNumberOfPointsAType()!=0) && (c1.getNumberOfPointsBType()!=0) && (c2.getNumberOfPointsAType()!=0) && (c2.getNumberOfPointsBType()!=0)*/) {
                set = TypeSet.B;
                c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                c2OcurredCost = c1B*c2.getNumberOfPointsAType();
            } else if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
                c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                c2OcurredCost = c1A*c2.getNumberOfPointsBType();
            } else {
                set = TypeSet.B;
                c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                c2OcurredCost = c1B*c2.getNumberOfPointsAType();
            }

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
            if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) < (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType()) /*&&
                    (c1.getNumberOfPointsAType()!=0) && (c1.getNumberOfPointsBType()!=0) && (c2.getNumberOfPointsAType()!=0) && (c2.getNumberOfPointsBType()!=0)*/) {
                set = TypeSet.A;
                c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                c2OcurredCost = c1A*c2.getNumberOfPointsBType();
            } else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) > (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType()) /*&&
                    (c1.getNumberOfPointsAType()!=0) && (c1.getNumberOfPointsBType()!=0) && (c2.getNumberOfPointsAType()!=0) && (c2.getNumberOfPointsBType()!=0)*/) {
                set = TypeSet.B;
                c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                c2OcurredCost = c1B*c2.getNumberOfPointsAType();
            } else if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
                c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                c2OcurredCost = c1A*c2.getNumberOfPointsBType();
            } else {
                set = TypeSet.B;
                c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                c2OcurredCost = c1B*c2.getNumberOfPointsAType();
            }
        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(set, c2OcurredCost), gr.ds.unipi.agreements.Edge.newEdge(set, c1OcurredCost));

    };

    public static Function4<Cell, Cell, Point, Agreement> dok = (Function4<Cell, Cell, Point, Agreement> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        int c1B = 0;
        int c2B = 0;

        TypeSet set = null;

        int c1OcurredCost = 0;
        int c2OcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInRightSpecialArea();
//            c2A = c2.getNumberOfPointsAInLeftSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInRightSpecialArea();
//            c2B = c2.getNumberOfPointsBInLeftSpecialArea();

            if(c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() >= c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType()){
                if(c1.getNumberOfPointsAType()<=c1.getNumberOfPointsBType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;

                }
            }else{
                if(c2.getNumberOfPointsAType()<=c2.getNumberOfPointsBType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;
                }
            }

            if(p.equals(c1.getRectangle().getLowerRightBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInBottomRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInBottomRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getUpperLeftBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInTopLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInTopLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInTopSpecialArea();
//            c2A = c2.getNumberOfPointsAInBottomSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInTopSpecialArea();
//            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
            if(c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() >= c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType()){
                if(c1.getNumberOfPointsAType()<=c1.getNumberOfPointsBType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;

                }
            }else{
                if(c2.getNumberOfPointsAType()<=c2.getNumberOfPointsBType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;
                }
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopLeftQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopLeftQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getLowerRightBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomRightQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomRightQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();

            if(c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() >= c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType()){
                if(c1.getNumberOfPointsAType()<=c1.getNumberOfPointsBType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }else{
                if(c2.getNumberOfPointsAType()<=c2.getNumberOfPointsBType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

            if(c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() >= c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType()){
                if(c1.getNumberOfPointsAType()<=c1.getNumberOfPointsBType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }else{
                if(c2.getNumberOfPointsAType()<=c2.getNumberOfPointsBType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(set, c2OcurredCost), gr.ds.unipi.agreements.Edge.newEdge(set, c1OcurredCost));

    };

    public static Function4<Cell, Cell, Point, Agreement> dok1 = (Function4<Cell, Cell, Point, Agreement> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        int c1B = 0;
        int c2B = 0;

        TypeSet set = null;

        int c1OcurredCost = 0;
        int c2OcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInRightSpecialArea();
//            c2A = c2.getNumberOfPointsAInLeftSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInRightSpecialArea();
//            c2B = c2.getNumberOfPointsBInLeftSpecialArea();

            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()>=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;

                }
            }else{
                if(c2.getNumberOfPointsBType()>=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;
                }
            }

            if(p.equals(c1.getRectangle().getLowerRightBound()) /*&& p.equals(c2.getRectangle().getLowerBound())*/){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInBottomRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInBottomRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) /*&& p.equals(c2.getRectangle().getUpperLeftBound())*/){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInTopLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInTopLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInTopSpecialArea();
//            c2A = c2.getNumberOfPointsAInBottomSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInTopSpecialArea();
//            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()>=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;

                }
            }else{
                if(c2.getNumberOfPointsBType()>=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;
                }
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) /*&& p.equals(c2.getRectangle().getLowerBound())*/){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopLeftQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopLeftQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) /*&& p.equals(c2.getRectangle().getLowerRightBound())*/){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomRightQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomRightQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();


            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()>=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }else{
                if(c2.getNumberOfPointsBType()>=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }


        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()>=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }else{
                if(c2.getNumberOfPointsBType()>=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(set, c2OcurredCost), gr.ds.unipi.agreements.Edge.newEdge(set, c1OcurredCost));

    };


    public static Function4<Cell, Cell, Point, Agreement> dok2 = (Function4<Cell, Cell, Point, Agreement> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        int c1B = 0;
        int c2B = 0;

        TypeSet set = null;

        int c1OcurredCost = 0;
        int c2OcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();

            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()<=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;

                }
            }else{
                if(c2.getNumberOfPointsBType()<=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;
                }
            }

            if(p.equals(c1.getRectangle().getLowerRightBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInBottomRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInBottomRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getUpperLeftBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInTopLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInTopLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()<=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;

                }
            }else{
                if(c2.getNumberOfPointsBType()<=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;
                }
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopLeftQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopLeftQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getLowerRightBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomRightQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomRightQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();


            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()<=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }else{
                if(c2.getNumberOfPointsBType()<=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }


        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()<=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }else{
                if(c2.getNumberOfPointsBType()<=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(set, c2OcurredCost), gr.ds.unipi.agreements.Edge.newEdge(set, c1OcurredCost));

    };


    public static Function4<Cell, Cell, Point, Agreement> costBasedBackpropagation = (Function4<Cell, Cell, Point, Agreement> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        int c1B = 0;
        int c2B = 0;

        TypeSet set = null;

        int c1OcurredCost = 0;
        int c2OcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
            if(c1.containsNeighboor(c2.hashCode())){
                set = c1.getNeighborTypeSet(c2.hashCode());
            }
            else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) < (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType())) {
                set = TypeSet.A;
            } else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) > (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType())) {
                set = TypeSet.B;
            }
            else if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }
            if(p.equals(c1.getRectangle().getLowerRightBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInBottomRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInBottomRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getUpperLeftBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInTopLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInTopLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
            if(c1.containsNeighboor(c2.hashCode())){
                set = c1.getNeighborTypeSet(c2.hashCode());
            }
            else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) < (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType())) {
                set = TypeSet.A;
            } else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) > (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType())) {
                set = TypeSet.B;
            }
            else if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopLeftQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopLeftQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getLowerRightBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomRightQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomRightQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();


            if(c1.containsNeighboor(c2.hashCode())){
                set = c1.getNeighborTypeSet(c2.hashCode());
            }
            else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) < (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType())) {
                set = TypeSet.A;
            } else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) > (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType())) {
                set = TypeSet.B;
            }
            else if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
            if(c1.containsNeighboor(c2.hashCode())){
                set = c1.getNeighborTypeSet(c2.hashCode());
            }
            else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) < (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType())) {
                set = TypeSet.A;
            } else if ((c1A*c2.getNumberOfPointsBType() + c2A*c1.getNumberOfPointsBType()) > (c1B*c2.getNumberOfPointsAType() + c2B*c1.getNumberOfPointsAType())) {
                set = TypeSet.B;
            }
            else if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }
        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(!c1.containsNeighboor(c2.hashCode())){
            c1.addNeighboor(c2.hashCode(),set);
            c2.addNeighboor(c1.hashCode(),set);
            if(set.equals(TypeSet.A)){
                c1.addExtraNumberOfPointsAType(c2A);
                c2.addExtraNumberOfPointsAType(c1A);
            }else if(set.equals(TypeSet.B)){
                c1.addExtraNumberOfPointsBType(c2B);
                c2.addExtraNumberOfPointsBType(c1B);
            }else{
                try {
                    throw new Exception("Problem with cells' typeset");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(set, c2OcurredCost), gr.ds.unipi.agreements.Edge.newEdge(set, c1OcurredCost));

    };

    public static Function4<Cell, Cell, Point, Agreement> case3Backpropagation = (Function4<Cell, Cell, Point, Agreement> & Serializable) (c1, c2, p) -> {

        int c1A = 0;
        int c2A = 0;
        int c1B = 0;
        int c2B = 0;

        TypeSet set = null;

        int c1OcurredCost = 0;
        int c2OcurredCost = 0;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
            if(c1.containsNeighboor(c2.hashCode())){
                set = c1.getNeighborTypeSet(c2.hashCode());
            }
            else if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()>=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;

                }
            }else{
                if(c2.getNumberOfPointsBType()>=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;
                }
            }

            if(p.equals(c1.getRectangle().getLowerRightBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInBottomRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInBottomRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getUpperLeftBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInTopLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInTopLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
            if(c1.containsNeighboor(c2.hashCode())){
                set = c1.getNeighborTypeSet(c2.hashCode());
            }
            else if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()>=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;

                }
            }else{
                if(c2.getNumberOfPointsBType()>=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                }else{
                    set = TypeSet.B;
                }
            }
            if(p.equals(c1.getRectangle().getUpperLeftBound()) && p.equals(c2.getRectangle().getLowerBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomLeftQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopLeftQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomLeftQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopLeftQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else if(p.equals(c1.getRectangle().getUpperBound()) && p.equals(c2.getRectangle().getLowerRightBound())){
                if(set.equals(TypeSet.A)){
                    c1OcurredCost = c2.getNumberOfPointsAInBottomRightQuarterArea() * c1.getNumberOfPointsBType();
                    c2OcurredCost = c1.getNumberOfPointsAInTopRightQuarterArea() * c2.getNumberOfPointsBType();
                }else{
                    c1OcurredCost = c2.getNumberOfPointsBInBottomRightQuarterArea() * c1.getNumberOfPointsAType();
                    c2OcurredCost = c1.getNumberOfPointsBInTopRightQuarterArea() * c2.getNumberOfPointsAType();
                }
            }else{
                try {
                    throw new Exception("Problem with cells' adjacency");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();


            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()>=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }else{
                if(c2.getNumberOfPointsBType()>=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }


        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

            if(Math.abs(c1.getNumberOfPointsAType()-c1.getNumberOfPointsBType()) >= Math.abs(c2.getNumberOfPointsAType()-c2.getNumberOfPointsBType())){
                if(c1.getNumberOfPointsBType()>=c1.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }else{
                if(c2.getNumberOfPointsBType()>=c2.getNumberOfPointsAType()){
                    set = TypeSet.A;
                    c1OcurredCost = c2A*c1.getNumberOfPointsBType();
                    c2OcurredCost = c1A*c2.getNumberOfPointsBType();
                }else{
                    set = TypeSet.B;
                    c1OcurredCost = c2B*c1.getNumberOfPointsAType();
                    c2OcurredCost = c1B*c2.getNumberOfPointsAType();
                }
            }

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if(!c1.containsNeighboor(c2.hashCode())){
            c1.addNeighboor(c2.hashCode(),set);
            c2.addNeighboor(c1.hashCode(),set);
            if(set.equals(TypeSet.A)){
                c1.addExtraNumberOfPointsAType(c2A);
                c2.addExtraNumberOfPointsAType(c1A);
            }else if(set.equals(TypeSet.B)){
                c1.addExtraNumberOfPointsBType(c2B);
                c2.addExtraNumberOfPointsBType(c1B);
            }else{
                try {
                    throw new Exception("Problem with cells' typeset");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(set, c2OcurredCost), gr.ds.unipi.agreements.Edge.newEdge(set, c1OcurredCost));

    };



}
