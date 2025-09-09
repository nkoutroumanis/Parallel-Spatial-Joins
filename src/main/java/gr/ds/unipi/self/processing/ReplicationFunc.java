package gr.ds.unipi.self.processing;

import gr.ds.unipi.processing.Function3;

import java.io.Serializable;

public class ReplicationFunc {

    public static Function3<Cell, Cell, Boolean> lesserPointsinBoundaries = (Function3<Cell, Cell, Boolean> & Serializable) (c1, c2) -> {

        int c1A = 0;
        int c2A = 0;
        boolean direction = true;
        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInRightSpecialArea();
            c2A = c2.getNumberOfPointsInLeftSpecialArea();

            if (c1A <= c2A) {
                direction = true;
            } else {
                direction = false;
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInTopSpecialArea();
            c2A = c2.getNumberOfPointsInBottomSpecialArea();

            if (c1A <= c2A) {
                direction = true;
            } else {
                direction = false;
            }
        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsInBottomLeftQuarterArea();

            if(c1A==c2A){
                direction = false;
            } else if (c1A < c2A) {
                direction = true;
            } else {
                direction = false;
            }
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsInBottomRightQuarterArea();

            if (c1A <= c2A) {
                direction = true;
            } else {
                direction = false;
            }
        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return direction;

    };
}
