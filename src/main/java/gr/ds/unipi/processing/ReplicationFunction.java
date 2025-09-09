package gr.ds.unipi.processing;

import gr.ds.unipi.TypeSet;

import java.io.Serializable;

public class ReplicationFunction {

    public static Function3<gr.ds.unipi.processing.Cell, gr.ds.unipi.processing.Cell, TypeSet> lesserPointsinBoundaries = (Function3<gr.ds.unipi.processing.Cell, gr.ds.unipi.processing.Cell, TypeSet> & Serializable) (c1, c2) -> {

        int c1A = 0;
        int c2A = 0;
        int c1B = 0;
        int c2B = 0;

        TypeSet set = null;

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

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();
            if ((c1A + c2A) <= (c1B + c2B)) {
                set = TypeSet.A;
            } else {
                set = TypeSet.B;
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
            if ((c1A + c2A) <= (c1B + c2B)) {
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
        return set;
    };
}
