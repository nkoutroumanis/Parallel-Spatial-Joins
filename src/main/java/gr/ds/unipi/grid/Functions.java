package gr.ds.unipi.grid;

import gr.ds.unipi.TypeSet;
import gr.ds.unipi.agreements.Agreement;
import gr.ds.unipi.agreements.Edge;

import java.util.function.BiFunction;

class Functions {

    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> datasetA = (c1, c2) -> {
        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, 0), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, 0));
    };

    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> datasetB = (c1, c2) -> {
        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, 0), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, 0));
    };

//    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> bestCase = (c1, c2) -> {
//        long a1 = c1.getNumberOfPointsAType() + c2.getNumberOfPointsAType();
//        long b1 = c1.getNumberOfPointsBType() + c2.getNumberOfPointsBType();
//
//        long a2 = Math.abs(c1.getNumberOfPointsAType() - c2.getNumberOfPointsAType());
//        long b2 = Math.abs(c1.getNumberOfPointsBType() - c2.getNumberOfPointsBType());
//
//        long a = a1 * b2;
//        long b = b1 * a2;
//
//        if(a <= b){
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, a1 * c2.getNumberOfPointsBType()), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, a1 * c1.getNumberOfPointsBType()));
//        }else{
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, b1 * c2.getNumberOfPointsAType()), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, b1 * c1.getNumberOfPointsAType()));
//        }
//    };

    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> worstCase = (c1, c2) -> {
        long a1 = c1.getNumberOfPointsAType() + c2.getNumberOfPointsAType();
        long b1 = c1.getNumberOfPointsBType() + c2.getNumberOfPointsBType();

        long a2 = Math.abs(c1.getNumberOfPointsAType() - c2.getNumberOfPointsAType());
        long b2 = Math.abs(c1.getNumberOfPointsBType() - c2.getNumberOfPointsBType());

        long a = a1 * b2;
        long b = b1 * a2;

        if(a <= b){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, a1 * c2.getNumberOfPointsBType()), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, a1 * c1.getNumberOfPointsBType()));
        }else{
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, b1 * c2.getNumberOfPointsAType()), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, b1 * c1.getNumberOfPointsAType()));
        }
    };

    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> case1 = (c1, c2) -> {

        long c1A = 0;
        long c2A = 0;
        long c1B = 0;
        long c2B = 0;

        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long c1AgreementA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
        long c2AgreementA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();

        long c1AgreementB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
        long c2AgreementB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();
//        long c1AgreementA = (/*c1.getNumberOfPointsAType() +*/ c2A) * c1.getNumberOfPointsBType();
//        long c2AgreementA = (/*c2.getNumberOfPointsAType() +*/ c1A) * c2.getNumberOfPointsBType();
//
//        long c1AgreementB = (/*c1.getNumberOfPointsBType() +*/ c2B) * c1.getNumberOfPointsAType();
//        long c2AgreementB = (/*c2.getNumberOfPointsBType() +*/ c1B) * c2.getNumberOfPointsAType();

        if((c1AgreementA+c2AgreementA)<=(c1AgreementB+c2AgreementB)){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
        }else if((c1AgreementA+c2AgreementA)>(c1AgreementB+c2AgreementB)){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
        }else{
            if(Math.abs((c1AgreementA-c2AgreementA))<=Math.abs(c1AgreementB-c2AgreementB)){
                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
            }
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
        }
    };

    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> actualCaseVariation2 = (c1, c2) -> {

        long c1A = 0;
        long c2A = 0;
        long c1B = 0;
        long c2B = 0;

        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long c1AgreementA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
        long c2AgreementA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();

        long c1AgreementB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
        long c2AgreementB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();
        //System.out.println("c1A: "+c1A+" "+"c2A: "+c2A+" "+"c1B: "+c1B+" "+"c2B: "+c2B+" ");
        //System.out.println("c1AgreementA: "+c1AgreementA+" "+"c2AgreementA: "+c2AgreementA+" "+"c1AgreementB: "+c1AgreementB+" "+"c2AgreementB: "+c2AgreementB+" ");
        //System.out.println("c1.getTotalNumberOfPointsAType(): "+c1.getTotalNumberOfPointsAType()+" "+"c2A: "+c2A+" "+"c1.getTotalNumberOfPointsBType(): "+c1.getTotalNumberOfPointsBType());

        if((c1A+c2A)<(c1B+c2B)){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
        }else if((c1A+c2A)>(c1B+c2B)){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
        }else{
            if(Math.abs((c1A-c2A))<=Math.abs(c1B-c2B)){
                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
            }
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
        }
    };

    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> MYCASE = (c1, c2) -> {

        long c1A = 0;
        long c2A = 0;
        long c1B = 0;
        long c2B = 0;


        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        long c1AgreementAA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
        long c2AgreementAA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();

        long c1AgreementBB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
        long c2AgreementBB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();

        long c1AgreementA = (c2A) * c1.getNumberOfPointsBType();
        long c2AgreementA = (c1A) * c2.getNumberOfPointsBType();

        long c1AgreementB = (c2B) * c1.getNumberOfPointsAType();
        long c2AgreementB = (c1B) * c2.getNumberOfPointsAType();
        //System.out.println("c1A: "+c1A+" "+"c2A: "+c2A+" "+"c1B: "+c1B+" "+"c2B: "+c2B+" ");
        //System.out.println("c1AgreementA: "+c1AgreementA+" "+"c2AgreementA: "+c2AgreementA+" "+"c1AgreementB: "+c1AgreementB+" "+"c2AgreementB: "+c2AgreementB+" ");
        //System.out.println("c1.getTotalNumberOfPointsAType(): "+c1.getTotalNumberOfPointsAType()+" "+"c2A: "+c2A+" "+"c1.getTotalNumberOfPointsBType(): "+c1.getTotalNumberOfPointsBType());

        if((c1AgreementAA+c2AgreementAA)<=(c1AgreementBB+c2AgreementBB)){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
        }else {
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
        }
    };

    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> actualCaseVariation4 = (c1, c2) -> {

        long c1A = 0;
        long c2A = 0;
        long c1B = 0;
        long c2B = 0;

        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long c1AgreementA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
        long c2AgreementA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();

        long c1AgreementB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
        long c2AgreementB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();
        //System.out.println("c1A: "+c1A+" "+"c2A: "+c2A+" "+"c1B: "+c1B+" "+"c2B: "+c2B+" ");
        //System.out.println("c1AgreementA: "+c1AgreementA+" "+"c2AgreementA: "+c2AgreementA+" "+"c1AgreementB: "+c1AgreementB+" "+"c2AgreementB: "+c2AgreementB+" ");
        //System.out.println("c1.getTotalNumberOfPointsAType(): "+c1.getTotalNumberOfPointsAType()+" "+"c2A: "+c2A+" "+"c1.getTotalNumberOfPointsBType(): "+c1.getTotalNumberOfPointsBType());

        if(Math.abs((c1A-c2A))<Math.abs((c1B-c2B))){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
        }else if(Math.abs((c1A-c2A))>Math.abs((c1B-c2B))){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
        }else{
            if((c1A+c2A)<=(c1B+c2B)){
                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
            }
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
        }
    };

//    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> actualCaseWithMemory = (c1, c2) -> {
//
//        long c1A = 0;
//        long c2A = 0;
//        long c1B = 0;
//        long c2B = 0;
//
//        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInRightSpecialArea();
//            c2A = c2.getNumberOfPointsAInLeftSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInRightSpecialArea();
//            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
//        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInTopSpecialArea();
//            c2A = c2.getNumberOfPointsAInBottomSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInTopSpecialArea();
//            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
//        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
//            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();
//
//            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
//            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();
//
//        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
//            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
//            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();
//
//            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
//            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
//
//        }
//        else{
//            try {
//                throw new Exception("Problem with cells' adjacency");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if((c1A+c2A)<(c1B+c2B)){
//            if(c1.containsNeighboor(c2)){
//                long c1AgreementA = (c1.getNumberOfPointsAType() + c1.getExtraNumberOfPointsAType()) * c1.getNumberOfPointsBType();
//                long c2AgreementA = (c2.getNumberOfPointsAType() + c2.getExtraNumberOfPointsAType()) * c2.getNumberOfPointsBType();
//
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
//            }else{
//                c1.addNeighboor(c2);
//                c2.addNeighboor(c1);
//
//                c1.addExtraNumberOfPointsAType(c2A);
//                c2.addExtraNumberOfPointsAType(c1A);
//            }
//            long c1AgreementA = (c1.getNumberOfPointsAType() + c1.getExtraNumberOfPointsAType()) * c1.getNumberOfPointsBType();
//            long c2AgreementA = (c2.getNumberOfPointsAType() + c2.getExtraNumberOfPointsAType()) * c2.getNumberOfPointsBType();
//
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
//        }else if((c1A+c2A)>(c1B+c2B)){
//            if(c1.containsNeighboor(c2)){
//                long c1AgreementB = (c1.getNumberOfPointsBType() + c1.getExtraNumberOfPointsBType()) * c1.getNumberOfPointsAType();
//                long c2AgreementB = (c2.getNumberOfPointsBType() + c2.getExtraNumberOfPointsBType()) * c2.getNumberOfPointsAType();
//
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
//            }else{
//                c1.addNeighboor(c2);
//                c2.addNeighboor(c1);
//
//                c1.addExtraNumberOfPointsBType(c2B);
//                c2.addExtraNumberOfPointsBType(c1B);
//            }
//
//            long c1AgreementB = (c1.getNumberOfPointsBType() + c1.getExtraNumberOfPointsBType()) * c1.getNumberOfPointsAType();
//            long c2AgreementB = (c2.getNumberOfPointsBType() + c2.getExtraNumberOfPointsBType()) * c2.getNumberOfPointsAType();
//
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
//        }else{
//            if(Math.abs((c1A-c2A))<=Math.abs(c1B-c2B)){
//                if(c1.containsNeighboor(c2)){
//                    long c1AgreementA = (c1.getNumberOfPointsAType() + c1.getExtraNumberOfPointsAType()) * c1.getNumberOfPointsBType();
//                    long c2AgreementA = (c2.getNumberOfPointsAType() + c2.getExtraNumberOfPointsAType()) * c2.getNumberOfPointsBType();
//
//                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
//                }else{
//                    c1.addNeighboor(c2);
//                    c2.addNeighboor(c1);
//
//                    c1.addExtraNumberOfPointsAType(c2A);
//                    c2.addExtraNumberOfPointsAType(c1A);
//                }
//                long c1AgreementA = (c1.getNumberOfPointsAType() + c1.getExtraNumberOfPointsAType()) * c1.getNumberOfPointsBType();
//                long c2AgreementA = (c2.getNumberOfPointsAType() + c2.getExtraNumberOfPointsAType()) * c2.getNumberOfPointsBType();
//
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
//            }
//            if(c1.containsNeighboor(c2)){
//                long c1AgreementB = (c1.getNumberOfPointsBType() + c1.getExtraNumberOfPointsBType()) * c1.getNumberOfPointsAType();
//                long c2AgreementB = (c2.getNumberOfPointsBType() + c2.getExtraNumberOfPointsBType()) * c2.getNumberOfPointsAType();
//
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
//            }else{
//                c1.addNeighboor(c2);
//                c2.addNeighboor(c1);
//
//                c1.addExtraNumberOfPointsBType(c2B);
//                c2.addExtraNumberOfPointsBType(c1B);
//            }
//
//            long c1AgreementB = (c1.getNumberOfPointsBType() + c1.getExtraNumberOfPointsBType()) * c1.getNumberOfPointsAType();
//            long c2AgreementB = (c2.getNumberOfPointsBType() + c2.getExtraNumberOfPointsBType()) * c2.getNumberOfPointsAType();
//
//            return Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
//        }
//    };
//
//    static BiFunction<Cell, Cell, Agreement> trial = (c1, c2) -> {
//
//        long c1A = 0;
//        long c2A = 0;
//        long c1B = 0;
//        long c2B = 0;
//
//        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInRightSpecialArea();
//            c2A = c2.getNumberOfPointsAInLeftSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInRightSpecialArea();
//            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
//        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInTopSpecialArea();
//            c2A = c2.getNumberOfPointsAInBottomSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInTopSpecialArea();
//            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
//        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
//            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();
//
//            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
//            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();
//
//        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
//            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
//            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();
//
//            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
//            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
//
//        }
//        else{
//            try {
//                throw new Exception("Problem with cells' adjacency");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        long c1AgreementA = (c1.getNumberOfPointsAType() + c2A) * (c1.getNumberOfPointsBType());
//        long c2AgreementA = (c2.getNumberOfPointsAType() + c1A) * (c2.getNumberOfPointsBType());
//
//        long c1AgreementB = (c1.getNumberOfPointsBType() + c2B) * (c1.getNumberOfPointsAType());
//        long c2AgreementB = (c2.getNumberOfPointsBType() + c1B) * (c2.getNumberOfPointsAType());
//
//        long a = c2A*c1.getNumberOfPointsBType();
//        long b = c1A*c2.getNumberOfPointsBType();
//        long c = c2B*c1.getNumberOfPointsAType();
//        long d = c1B*c2.getNumberOfPointsAType();
//
//
////        if((Math.abs((a-b))>Math.abs(c-d)) && !(Math.abs((c1AgreementA-c2AgreementA))>Math.abs(c1AgreementB-c2AgreementB))){
////            System.out.println("is diff");
////            try {
////                throw new Exception("EXC");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
//
//        if((a+b)<(c+d)){
//            return Agreement.newAgreement(Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
//        }else if((a+b)>(c+d)){
//            return Agreement.newAgreement(Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
//        }else{
//            if(Math.abs((c1A-c2A))>Math.abs(c1B-c2B)){
//                return Agreement.newAgreement(Edge.newEdge(c1, c2, TypeSet.A, c2AgreementA), Edge.newEdge(c2, c1, TypeSet.A, c1AgreementA));
//            }
//            return Agreement.newAgreement(Edge.newEdge(c1, c2, TypeSet.B, c2AgreementB), Edge.newEdge(c2, c1, TypeSet.B, c1AgreementB));
//        }
//    };


    //static BiFunction<Cell, Cell, Agreement> function;

//    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> dokimastiko = (c1, c2) -> {
//
//        long c1A = 0;
//        long c2A = 0;
//        long c1B = 0;
//        long c2B = 0;
//
//        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInRightSpecialArea();
//            c2A = c2.getNumberOfPointsAInLeftSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInRightSpecialArea();
//            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
//        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInTopSpecialArea();
//            c2A = c2.getNumberOfPointsAInBottomSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInTopSpecialArea();
//            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
//        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
//            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();
//
//            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
//            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();
//
//        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
//            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
//            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();
//
//            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
//            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
//
//        }
//        else{
//            try {
//                throw new Exception("Problem with cells' adjacency");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
////        long c1AgreementAA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
////        long c2AgreementAA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();
////
////        long c1AgreementBB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
////        long c2AgreementBB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();
//
//        long c1AgreementAA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
//        long c2AgreementAA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();
//
//        long c1AgreementBB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
//        long c2AgreementBB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();
//
//        long c1Cost = c1.getNumberOfPointsAType() * c1.getNumberOfPointsBType();
//        long c2Cost = c2.getNumberOfPointsAType() * c2.getNumberOfPointsBType();
////        long max = Math.max(c1Cost, c2Cost);
////        double percentage = (Math.abs(c1Cost-c2Cost)*100d)/max;
//        long max = Math.max(c1Cost, c2Cost);
//
//        double thresh = (Math.abs(Grid.average-(Grid.average+Grid.std))/((double)(Grid.average+Grid.average+Grid.std)/2d))*100;
//
//        //System.out.println("Thresh: "+thresh);
//        double percentage;
//        if(c1Cost+c2Cost==0){
//            percentage = 0;
//        }else{
//            percentage = ((double)Math.abs(c1Cost-c2Cost)/((double)(c1Cost+c2Cost)/2d))*100;
//        }
//
////        if(c1Cost>c2Cost){
////            if(c1.getNumberOfPointsAType()<c1.getNumberOfPointsBType()){
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////            }else{
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////            }
////        }else{
////            if(c2.getNumberOfPointsAType()<c2.getNumberOfPointsBType()){
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////
////            }else{
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////            }
////        }
// //       if(/*percentage>=10Math.abs(c1Cost-c2Cost)>Grid.std*/!(c1Cost>Grid.average-(Grid.std/2) && c1Cost<Grid.average+(Grid.std/2)) || (!(c2Cost>Grid.average-(Grid.std/2) && c2Cost<Grid.average+(Grid.std/2)))) {
// //           System.out.println(c1Cost +" "+ c2Cost+" "+percentage);
//
//            //System.out.println("c1A: "+c1A+" "+"c2A: "+c2A+" "+"c1B: "+c1B+" "+"c2B: "+c2B+" ");
//            //System.out.println("c1AgreementA: "+c1AgreementA+" "+"c2AgreementA: "+c2AgreementA+" "+"c1AgreementB: "+c1AgreementB+" "+"c2AgreementB: "+c2AgreementB+" ");
//            //System.out.println("c1.getTotalNumberOfPointsAType(): "+c1.getTotalNumberOfPointsAType()+" "+"c2A: "+c2A+" "+"c1.getTotalNumberOfPointsBType(): "+c1.getTotalNumberOfPointsBType());
//
////            if ((c1AgreementAA + c2AgreementAA) < (c1AgreementBB + c2AgreementBB) && Math.abs((c1AgreementAA - c2AgreementAA)) < Math.abs(c1AgreementBB - c2AgreementBB)) {
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////            } else if ((c1AgreementAA + c2AgreementAA) > (c1AgreementBB + c2AgreementBB) && Math.abs((c1AgreementAA - c2AgreementAA)) > Math.abs(c1AgreementBB - c2AgreementBB)) {
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////            }
////        else if((c1AgreementA+c2AgreementA)<(c1AgreementB+c2AgreementB) && Math.abs((c1AgreementA-c2AgreementA))<Math.abs(c1AgreementB-c2AgreementB)){
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////        }else if((c1AgreementA+c2AgreementA)>(c1AgreementB+c2AgreementB) && Math.abs((c1AgreementA-c2AgreementA))>Math.abs(c1AgreementB-c2AgreementB)){
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////        }
//
////        else if((c1AgreementAA+c2AgreementAA)<(c1AgreementBB+c2AgreementBB)){
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////        }
//
////        System.out.println("c1AgreementAA:"+c1AgreementAA + " c2AgreementAA:"+c2AgreementAA);
////        System.out.println("c1AgreementBB:"+c1AgreementBB +" c2AgreementBB:" +c2AgreementBB);
////        System.out.println("Cell1 " + "A:"+c1.getNumberOfPointsAType()+" B:"+c1.getNumberOfPointsBType());
////        System.out.println("Cell2 " + "A:"+c2.getNumberOfPointsAType()+" B:"+c2.getNumberOfPointsBType());
////
////        System.out.println("Replicated Cell1 " + "A:"+c2A+" B:"+c2B);
////        System.out.println("Replicated Cell2 " + "A:"+c1A+" B:"+c1B);
//
////        if(Math.abs((c1AgreementAA-c2AgreementAA))>Math.abs(c1AgreementBB-c2AgreementBB)){
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////        }else /*if(Math.abs((c1AgreementAA-c2AgreementAA))>Math.abs(c1AgreementBB-c2AgreementBB))*/{
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////        }
//
////        if((c1AgreementAA+c2AgreementAA)<(c1AgreementBB+c2AgreementBB) && Math.abs((c1AgreementAA-c2AgreementAA))<Math.abs(c1AgreementBB-c2AgreementBB)){
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////        }else if((c1AgreementAA+c2AgreementAA)>(c1AgreementBB+c2AgreementBB) && Math.abs((c1AgreementAA-c2AgreementAA))>Math.abs(c1AgreementBB-c2AgreementBB)){
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////        }
////            if ((c1A + c2A) <= (c1B + c2B)) {
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////            } else /*if((c1A + c2A) > (c1B + c2B))*/ {
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////            }
//            if ((c1.getNumberOfPointsAType() + c2.getNumberOfPointsAType() ) <= (c1.getNumberOfPointsBType() + c2.getNumberOfPointsBType() )) {
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//            } else /*if((c1A + c2A) > (c1B + c2B))*/ {
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//            }
//
////return null;
//
////        if((c2A * c1.getNumberOfPointsBType()) + (c1A * c2.getNumberOfPointsBType())< (c2B * c1.getNumberOfPointsAType()) + (c1B * c2.getNumberOfPointsAType()) ){
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////        }else{
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////        }
////        else{
////        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////        }
////        else{
////            if(Math.abs((c1AgreementAA-c2AgreementAA))<Math.abs(c1AgreementBB-c2AgreementBB)){
////                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////            }
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////        }
////        }
////        else{
////            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////        }
//
//    };


    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> unini = (c1, c2) -> {

        long c1A = 0;
        long c2A = 0;
        long c1B = 0;
        long c2B = 0;

        if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){

            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();
            long c1AgreementAA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
            long c2AgreementAA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();

            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));

        }else if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
            long c1AgreementBB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
            long c2AgreementBB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();

            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));

        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long c1AgreementAA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
        long c2AgreementAA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();

        long c1AgreementBB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
        long c2AgreementBB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();

        if((c1AgreementAA+c2AgreementAA)<(c1AgreementBB+c2AgreementBB)){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
        }else if((c1AgreementAA+c2AgreementAA)>(c1AgreementBB+c2AgreementBB)){
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
        }else{
            if(Math.abs((c1AgreementAA-c2AgreementAA))<=Math.abs(c1AgreementBB-c2AgreementBB)){
                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
            }else{
                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
            }
        }

    };

    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> dok = (c1, c2) -> {

        long c1A = 0;
        long c2A = 0;
        long c1B = 0;
        long c2B = 0;

        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInRightSpecialArea();
            c2A = c2.getNumberOfPointsAInLeftSpecialArea();

            c1B = c1.getNumberOfPointsBInRightSpecialArea();
            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopSpecialArea();
            c2A = c2.getNumberOfPointsAInBottomSpecialArea();

            c1B = c1.getNumberOfPointsBInTopSpecialArea();
            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();

            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();

        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();

            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();

        }
        else{
            try {
                throw new Exception("Problem with cells' adjacency");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long c1AgreementAA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
        long c2AgreementAA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();

        long c1AgreementBB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
        long c2AgreementBB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();

//        System.out.println(c1.getNumberOfPointsAType()+ " "+ c1.getNumberOfPointsBType());
//        System.out.println(c2.getNumberOfPointsAType()+ " "+ c2.getNumberOfPointsBType());
        //if(c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() >= (Grid.average - (Grid.error)) && c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() <= (Grid.average + ( Grid.error))){
            //if(c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType() >= (Grid.average - ( Grid.error)) && c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType() <= (Grid.average + ( Grid.error))){
//                if(c1.getNumberOfPointsAType() >= (Grid.avgA - ( Grid.errorA)) && c1.getNumberOfPointsAType() <= (Grid.avgA + ( Grid.errorA))){
//                    if(c1.getNumberOfPointsBType() >= (Grid.avgB - ( Grid.errorB)) && c1.getNumberOfPointsBType() <= (Grid.avgB + ( Grid.errorB))){
//                        if(c2.getNumberOfPointsAType() >= (Grid.avgA - (Grid.errorA)) && c2.getNumberOfPointsAType() <= (Grid.avgA + ( Grid.errorA))){
//                            if(c2.getNumberOfPointsBType() >= (Grid.avgB - (Grid.errorB)) && c2.getNumberOfPointsBType() <= (Grid.avgB + ( Grid.errorB))){
//                                if(c1.getNumberOfPointsAType() != 0 && c1.getNumberOfPointsBType() != 0 && c2.getNumberOfPointsAType() != 0 && c2.getNumberOfPointsBType() != 0
//                                && c1.getNumberOfPointsAType() != 1 && c1.getNumberOfPointsBType() != 1 && c2.getNumberOfPointsAType() != 1 && c2.getNumberOfPointsBType() != 1){
//                                if(Grid.stdA<=Grid.stdB){
//                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//                                }else{
//                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//                                } }
////                                double a = Math.abs(Grid.stdA - Math.abs(c1.getNumberOfPointsAType() - c2.getNumberOfPointsAType()));
////                                double b =Math.abs(Grid.stdB -Math.abs(c1.getNumberOfPointsBType() - c2.getNumberOfPointsBType()));
////                                System.out.println(a);
////                                System.out.println(b);
////                                if(a<b){
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////
////                                }else{
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////                                }
////                                double l = Math.max(Math.max(c1.getNumberOfPointsAType(), c2.getNumberOfPointsAType()),Math.max(c1.getNumberOfPointsBType(), c2.getNumberOfPointsBType()));
////                                if( l == c1.getNumberOfPointsBType() || l == c2.getNumberOfPointsBType()){
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////
////                                }else{
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////                                }
//                            }
//                        }
//                    }
//                }
            //}
        //}

//        if(Math.abs(c1.getNumberOfPointsAType() -c2.getNumberOfPointsAType()) > Math.abs(c1.getNumberOfPointsBType() -c2.getNumberOfPointsBType()) ){
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        }else{
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }

//        if(Math.abs((c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType()) - (c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType()))< Grid.std + Grid.error) {
//            if (Math.abs(c1.getNumberOfPointsAType() - c2.getNumberOfPointsAType()) < Grid.stdA + Grid.errorA) {
//                if (Math.abs(c1.getNumberOfPointsBType() - c2.getNumberOfPointsBType()) < Grid.stdB + Grid.errorB) {
//                                if(Grid.stdA<=Grid.stdB){
//                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//                                }else{
//                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//                                }
//                }
//            }
//        }

//        long c1AgreementA = (c2A) * c1.getNumberOfPointsBType();
//        long c2AgreementA = (c1A) * c2.getNumberOfPointsBType();
//
//        long c1AgreementB = (c2B) * c1.getNumberOfPointsAType();
//        long c2AgreementB = (c1B) * c2.getNumberOfPointsAType();



//        long c1Cost = c1.getNumberOfPointsAType() * c1.getNumberOfPointsBType();
//        long c2Cost = c2.getNumberOfPointsAType() * c2.getNumberOfPointsBType();
////        long max = Math.max(c1Cost, c2Cost);
////        double percentage = (Math.abs(c1Cost-c2Cost)*100d)/max;
//        long max = Math.max(c1Cost, c2Cost);
//
//        double thresh = (Math.abs(Grid.average-(Grid.average+Grid.std))/((double)(Grid.average+Grid.average+Grid.std)/2d))*100;
//
//        //System.out.println("Thresh: "+thresh);
//        double percentage;
//        if(c1Cost+c2Cost==0){
//            percentage = 0;
//        }else{
//            percentage = ((double)Math.abs(c1Cost-c2Cost)/((double)(c1Cost+c2Cost)/2d))*100;
//        }

//        if(c1Cost>c2Cost){
//            if(c1.getNumberOfPointsAType()<c1.getNumberOfPointsBType()){
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//            }else{
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//
//            }
//        }else{
//            if(c2.getNumberOfPointsAType()<c2.getNumberOfPointsBType()){
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//
//            }else{
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//
//            }
//        }
        //       if(/*percentage>=10Math.abs(c1Cost-c2Cost)>Grid.std*/!(c1Cost>Grid.average-(Grid.std/2) && c1Cost<Grid.average+(Grid.std/2)) || (!(c2Cost>Grid.average-(Grid.std/2) && c2Cost<Grid.average+(Grid.std/2)))) {
        //           System.out.println(c1Cost +" "+ c2Cost+" "+percentage);

        //System.out.println("c1A: "+c1A+" "+"c2A: "+c2A+" "+"c1B: "+c1B+" "+"c2B: "+c2B+" ");
        //System.out.println("c1AgreementA: "+c1AgreementA+" "+"c2AgreementA: "+c2AgreementA+" "+"c1AgreementB: "+c1AgreementB+" "+"c2AgreementB: "+c2AgreementB+" ");
        //System.out.println("c1.getTotalNumberOfPointsAType(): "+c1.getTotalNumberOfPointsAType()+" "+"c2A: "+c2A+" "+"c1.getTotalNumberOfPointsBType(): "+c1.getTotalNumberOfPointsBType());

//        if ((c1AgreementAA + c2AgreementAA) < (c1AgreementBB + c2AgreementBB) && Math.abs((c1AgreementAA - c2AgreementAA)) < Math.abs(c1AgreementBB - c2AgreementBB)) {
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        } else if ((c1AgreementAA + c2AgreementAA) > (c1AgreementBB + c2AgreementBB) && Math.abs((c1AgreementAA - c2AgreementAA)) > Math.abs(c1AgreementBB - c2AgreementBB)) {
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }

//        double l = Math.max(Math.max(c1.getNumberOfPointsAType(), c2.getNumberOfPointsAType()),Math.max(c1.getNumberOfPointsBType(), c2.getNumberOfPointsBType()));
//        if( l == c1.getNumberOfPointsAType() || l == c2.getNumberOfPointsAType()){
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//
//        }else{
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//
//        }
//        else if((c1AgreementA+c2AgreementA)<(c1AgreementB+c2AgreementB) && Math.abs((c1AgreementA-c2AgreementA))<Math.abs(c1AgreementB-c2AgreementB)){
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        }else if((c1AgreementA+c2AgreementA)>(c1AgreementB+c2AgreementB) && Math.abs((c1AgreementA-c2AgreementA))>Math.abs(c1AgreementB-c2AgreementB)){
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }

//        else if((c1AgreementAA+c2AgreementAA)<(c1AgreementBB+c2AgreementBB)){
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        }

//        System.out.println("c1AgreementAA:"+c1AgreementAA + " c2AgreementAA:"+c2AgreementAA);
//        System.out.println("c1AgreementBB:"+c1AgreementBB +" c2AgreementBB:" +c2AgreementBB);
//        System.out.println("Cell1 " + "A:"+c1.getNumberOfPointsAType()+" B:"+c1.getNumberOfPointsBType());
//        System.out.println("Cell2 " + "A:"+c2.getNumberOfPointsAType()+" B:"+c2.getNumberOfPointsBType());
//
//        System.out.println("Replicated Cell1 " + "A:"+c2A+" B:"+c2B);
//        System.out.println("Replicated Cell2 " + "A:"+c1A+" B:"+c1B);

//        if(Math.abs((c1AgreementAA-c2AgreementAA))<Math.abs(c1AgreementBB-c2AgreementBB)){
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        }else /*if(Math.abs((c1AgreementAA-c2AgreementAA))>Math.abs(c1AgreementBB-c2AgreementBB))*/{
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }
//        if ((c1AgreementA + c2AgreementA) <= (c1AgreementB + c2AgreementB)) {
//            if((c1A + c2A)>(c1B + c2B)){
//                System.out.println("c1A: "+c1A+" "+"c2A: "+c2A+" "+"c1B: "+c1B+" "+"c2B: "+c2B+" ");
//                System.out.println("c1AgreementA: "+c1AgreementA+" "+"c2AgreementA: "+c2AgreementA+" "+"c1AgreementB: "+c1AgreementB+" "+"c2AgreementB: "+c2AgreementB+" ");
//                System.out.println("c1.getTotalNumberOfPointsAType(): "+c1.getNumberOfPointsAType()+" c1.getTotalNumberOfPointsBType(): "+c1.getNumberOfPointsBType());
//                System.out.println("c2.getTotalNumberOfPointsAType(): "+c2.getNumberOfPointsAType()+" c2.getTotalNumberOfPointsBType(): "+c2.getNumberOfPointsBType());
//            }
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        } else {
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }
        if ((c1A + c2A) <= (c1B + c2B)) {
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
        } else {
            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
        }


//        if((c2A * c1.getNumberOfPointsBType()) + (c1A * c2.getNumberOfPointsBType())< (c2B * c1.getNumberOfPointsAType()) + (c1B * c2.getNumberOfPointsAType()) ){
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        }else{
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }
//        else{
//        return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        }
//        else{
//            if(Math.abs((c1AgreementAA-c2AgreementAA))<Math.abs(c1AgreementBB-c2AgreementBB)){
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//            }
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }
//        }
//        else{
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        }

    };
//    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> trial = (c1, c2) -> {
//
//        long c1A = 0;
//        long c2A = 0;
//        long c1B = 0;
//        long c2B = 0;
//
//        if(c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())){
//
//            c1A = (long) ((((c1.getRectangle().getUpperBound().getY()-c1.getRectangle().getLowerBound().getY()) * Grid.getR()) * c1.getNumberOfPointsAType()) / Grid.getSurfaceCell(c1));
//            c2A = (long) ((((c1.getRectangle().getUpperBound().getY()-c1.getRectangle().getLowerBound().getY()) * Grid.getR()) * c2.getNumberOfPointsAType()) / Grid.getSurfaceCell(c1));
//
//            c1B = (long) ((((c1.getRectangle().getUpperBound().getY()-c1.getRectangle().getLowerBound().getY()) * Grid.getR()) * c1.getNumberOfPointsBType()) / Grid.getSurfaceCell(c1));
//            c2B = (long) ((((c1.getRectangle().getUpperBound().getY()-c1.getRectangle().getLowerBound().getY()) * Grid.getR()) * c2.getNumberOfPointsBType()) / Grid.getSurfaceCell(c1));
//        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())){
//            c1A = (long) ((((c1.getRectangle().getUpperBound().getX()-c1.getRectangle().getLowerBound().getX()) * Grid.getR()) * c1.getNumberOfPointsAType()) / Grid.getSurfaceCell(c1));
//            c2A = (long) ((((c1.getRectangle().getUpperBound().getX()-c1.getRectangle().getLowerBound().getX()) * Grid.getR()) * c2.getNumberOfPointsAType()) / Grid.getSurfaceCell(c1));
//
//            c1B = (long) ((((c1.getRectangle().getUpperBound().getX()-c1.getRectangle().getLowerBound().getX()) * Grid.getR()) * c1.getNumberOfPointsBType()) / Grid.getSurfaceCell(c1));
//            c2B = (long) ((((c1.getRectangle().getUpperBound().getX()-c1.getRectangle().getLowerBound().getX()) * Grid.getR()) * c2.getNumberOfPointsBType()) / Grid.getSurfaceCell(c1));
//        }else if(c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())){
//
//            c1A = (long) ((((Math.PI * Math.pow(Grid.getR(),2))/4) * c1.getNumberOfPointsAType()) / Grid.getSurfaceCell(c1));
//            c2A = (long) ((((Math.PI * Math.pow(Grid.getR(),2))/4) * c2.getNumberOfPointsAType()) / Grid.getSurfaceCell(c1));
//
//            c1B = (long) ((((Math.PI * Math.pow(Grid.getR(),2))/4) * c1.getNumberOfPointsBType()) / Grid.getSurfaceCell(c1));
//            c2B = (long) ((((Math.PI * Math.pow(Grid.getR(),2))/4) * c2.getNumberOfPointsBType()) / Grid.getSurfaceCell(c1));
//
//        }else if(c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())){
//            c1A = (long) ((((Math.PI * Math.pow(Grid.getR(),2))/4) * c1.getNumberOfPointsAType()) / Grid.getSurfaceCell(c1));
//            c2A = (long) ((((Math.PI * Math.pow(Grid.getR(),2))/4) * c2.getNumberOfPointsAType()) / Grid.getSurfaceCell(c1));
//
//            c1B = (long) ((((Math.PI * Math.pow(Grid.getR(),2))/4) * c1.getNumberOfPointsBType()) / Grid.getSurfaceCell(c1));
//            c2B = (long) ((((Math.PI * Math.pow(Grid.getR(),2))/4) * c2.getNumberOfPointsBType()) / Grid.getSurfaceCell(c1));
//
//        }
//        else{
//            try {
//                throw new Exception("Problem with cells' adjacency");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        long c1AgreementAA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
//        long c2AgreementAA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();
//
//        long c1AgreementBB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
//        long c2AgreementBB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();
//
//        //if(c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() >= (Grid.average - ( Grid.error)) && c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() <= (Grid.average + ( Grid.error))){
//        //if(c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType() >= (Grid.average - ( Grid.error)) && c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType() <= (Grid.average + ( Grid.error))){
//        if(c1.getNumberOfPointsAType() >= (Grid.avgA - ( Grid.errorA)) && c1.getNumberOfPointsAType() <= (Grid.avgA + ( Grid.errorA))){
//            if(c1.getNumberOfPointsBType() >= (Grid.avgB - ( Grid.errorB)) && c1.getNumberOfPointsBType() <= (Grid.avgB + ( Grid.errorB))){
//                if(c2.getNumberOfPointsAType() >= (Grid.avgA - (Grid.errorA)) && c2.getNumberOfPointsAType() <= (Grid.avgA + ( Grid.errorA))){
//                    if(c2.getNumberOfPointsBType() >= (Grid.avgB - (Grid.errorB)) && c2.getNumberOfPointsBType() <= (Grid.avgB + ( Grid.errorB))){
//                        if(c1.getNumberOfPointsAType() != 0 && c1.getNumberOfPointsBType() != 0 && c2.getNumberOfPointsAType() != 0 && c2.getNumberOfPointsBType() != 0
//                                && c1.getNumberOfPointsAType() != 1 && c1.getNumberOfPointsBType() != 1 && c2.getNumberOfPointsAType() != 1 && c2.getNumberOfPointsBType() != 1){
//                            System.out.println("OK");
//                            if(Grid.stdA<=Grid.stdB){
//                                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//                            }else{
//                                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//                            } }
////                                double a = Math.abs(Grid.stdA - Math.abs(c1.getNumberOfPointsAType() - c2.getNumberOfPointsAType()));
////                                double b =Math.abs(Grid.stdB -Math.abs(c1.getNumberOfPointsBType() - c2.getNumberOfPointsBType()));
////                                System.out.println(a);
////                                System.out.println(b);
////                                if(a<b){
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////
////                                }else{
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////                                }
////                                double l = Math.max(Math.max(c1.getNumberOfPointsAType(), c2.getNumberOfPointsAType()),Math.max(c1.getNumberOfPointsBType(), c2.getNumberOfPointsBType()));
////                                if( l == c1.getNumberOfPointsBType() || l == c2.getNumberOfPointsBType()){
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////
////                                }else{
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////                                }
//                    }
//                }
//            }
//        }
//        if ((c1.getNumberOfPointsAType() + c2.getNumberOfPointsAType()) <= (c1.getNumberOfPointsBType() + c2.getNumberOfPointsBType())) {
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        } else {
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }
//    };

//    static BiFunction<Cell, Cell, gr.ds.unipi.agreements.Agreement> another = (c1, c2) -> {
//
//        long c1A = 0;
//        long c2A = 0;
//        long c1B = 0;
//        long c2B = 0;
//
//        if (c1.getRectangle().getLowerRightBound().equals(c2.getRectangle().getLowerBound())) {
//            c1A = c1.getNumberOfPointsAInRightSpecialArea();
//            c2A = c2.getNumberOfPointsAInLeftSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInRightSpecialArea();
//            c2B = c2.getNumberOfPointsBInLeftSpecialArea();
//        } else if (c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerBound())) {
//            c1A = c1.getNumberOfPointsAInTopSpecialArea();
//            c2A = c2.getNumberOfPointsAInBottomSpecialArea();
//
//            c1B = c1.getNumberOfPointsBInTopSpecialArea();
//            c2B = c2.getNumberOfPointsBInBottomSpecialArea();
//        } else if (c1.getRectangle().getUpperBound().equals(c2.getRectangle().getLowerBound())) {
//            c1A = c1.getNumberOfPointsAInTopRightQuarterArea();
//            c2A = c2.getNumberOfPointsAInBottomLeftQuarterArea();
//
//            c1B = c1.getNumberOfPointsBInTopRightQuarterArea();
//            c2B = c2.getNumberOfPointsBInBottomLeftQuarterArea();
//
//        } else if (c1.getRectangle().getUpperLeftBound().equals(c2.getRectangle().getLowerRightBound())) {
//            c1A = c1.getNumberOfPointsAInTopLeftQuarterArea();
//            c2A = c2.getNumberOfPointsAInBottomRightQuarterArea();
//
//            c1B = c1.getNumberOfPointsBInTopLeftQuarterArea();
//            c2B = c2.getNumberOfPointsBInBottomRightQuarterArea();
//
//        } else {
//            try {
//                throw new Exception("Problem with cells' adjacency");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        long c1AgreementAA = (c1.getNumberOfPointsAType() + c2A) * c1.getNumberOfPointsBType();
//        long c2AgreementAA = (c2.getNumberOfPointsAType() + c1A) * c2.getNumberOfPointsBType();
//
//        long c1AgreementBB = (c1.getNumberOfPointsBType() + c2B) * c1.getNumberOfPointsAType();
//        long c2AgreementBB = (c2.getNumberOfPointsBType() + c1B) * c2.getNumberOfPointsAType();
//
//        if (Math.abs(c1.getNumberOfPointsAType() - c2.getNumberOfPointsAType()) <= Grid.stdA*2.576 && Math.abs(c1.getNumberOfPointsBType() - c2.getNumberOfPointsBType()) <= Grid.stdB*2.576) {
//            //if(c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() >= (Grid.average - (Grid.std + Grid.error)) && c1.getNumberOfPointsAType()*c1.getNumberOfPointsBType() <= (Grid.average + (Grid.std + Grid.error))){
//            //if(c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType() >= (Grid.average - (Grid.std + Grid.error)) && c2.getNumberOfPointsAType()*c2.getNumberOfPointsBType() <= (Grid.average + (Grid.std + Grid.error))){
//
//            if (Grid.stdA <= Grid.stdB) {
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//            } else {
//                return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//            }
////                                double a = Math.abs(Grid.stdA - Math.abs(c1.getNumberOfPointsAType() - c2.getNumberOfPointsAType()));
////                                double b =Math.abs(Grid.stdB -Math.abs(c1.getNumberOfPointsBType() - c2.getNumberOfPointsBType()));
////                                System.out.println(a);
////                                System.out.println(b);
////                                if(a<b){
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////
////                                }else{
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////                                }
////                                double l = Math.max(Math.max(c1.getNumberOfPointsAType(), c2.getNumberOfPointsAType()),Math.max(c1.getNumberOfPointsBType(), c2.getNumberOfPointsBType()));
////                                if( l == c1.getNumberOfPointsBType() || l == c2.getNumberOfPointsBType()){
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
////
////                                }else{
////                                    return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
////
////                                }
//        }
//
//
//        if ((c1A + c2A) <= (c1B + c2B)) {
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.A, c2AgreementAA), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.A, c1AgreementAA));
//        } else {
//            return gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(c1, c2, TypeSet.B, c2AgreementBB), gr.ds.unipi.agreements.Edge.newEdge(c2, c1, TypeSet.B, c1AgreementBB));
//        }
//
//    };

}
