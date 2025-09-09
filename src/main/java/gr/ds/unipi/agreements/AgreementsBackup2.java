//package gr.ds.unipi.agreements;
//
//import gr.ds.unipi.TypeSet;
//import gr.ds.unipi.grid.Cell;
//import gr.ds.unipi.grid.Function6;
//import gr.ds.unipi.shapes.Point;
//
//import java.util.AbstractMap;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AgreementsBackup2 {
//
//    private final int[] spaces;//Four spaces have 1 element (id of bottom left cell). Two spaces have 2 elements (ids of the two cells).
//    private final Edge[] edges;
//
//    public void checkforsymmetry(){
//        for (int i = 0; i < edges.length; i=i+2) {
//            if(edges[i].getTypeSet()!=edges[i+1].getTypeSet()){
//                System.out.println("SYMMETRY IS BROKEN");
//            }
//        }
//    }
//
//    public int getSpacesNum(){
//        return spaces.length;
//    }
//
//    private AgreementsBackup2(int[] spaces, List<Cell> cells, Point point, /*ReplicationType function*/Function6<Integer, Integer, Cell, Cell, Point,Agreement> function){
//        this.spaces = spaces;
//        switch (spaces.length){
//            case 1:
//                edges = new Edge[0];
//                break;
//            case 2:
//                edges = new Edge[2];
//                break;
//            case 3:
//                edges = new Edge[6];
//                break;
//            case 4:
//                edges = new Edge[12];
//                break;
//            default:
//                edges = null;
//                try {
//                    throw new Exception("Unexpected number of spaces for creating edges.");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//        }
////        if(spaces.size()==1){
////            edges = new Edge[0];
////        }else if(spaces.size()==2){
////            edges = new Edge[2];
////        }else if(spaces.size()==3){
////            edges = new Edge[6];
////        }else if(spaces.size()==4){
////            edges = new Edge[12];
////        }else{
////            edges = null;
////            try {
////                throw new Exception("Unexpected number of spaces for creating edges.");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
//
//        int k = 0;
//
//        for (int i = 0; i< cells.size(); i++){
//            for(int y = i+1; y< cells.size(); y++){
////                Agreement<T> agreement = function.getAgreement(spaces.get(i), spaces.get(y), point);
//                Agreement agreement = function.apply(spaces[i], spaces[y],cells.get(i), cells.get(y), point);
//
//                //Agreement<T> agreement = gr.ds.unipi.agreements.Agreement.newAgreement(gr.ds.unipi.agreements.Edge.newEdge(spaces.get(i), spaces.get(y), TypeSet.A, 0), gr.ds.unipi.agreements.Edge.newEdge(spaces.get(y), spaces.get(i), TypeSet.A, 0));
//
//                edges[k++] = agreement.getEdgeA();
//                edges[k++] = agreement.getEdgeB();
//            }
//        }
//
//    }
//
////    public void createEdges(){
////        //eliminations
////        if(spaces.size()==4 || spaces.size()==3){
////
////            if(Grid.experiments.equals("DIAG_COMP")){
////                perimiterCompliance();
////            }
////
////            List<AbstractMap.SimpleEntry<Long,Integer>> edgesSorted = new ArrayList<>();
////            for (int i = 0; i < edges.length; i++) {
////                edgesSorted.add(new AbstractMap.SimpleEntry(edges[i].getWeight(), i+1));
////            }
////            edgesSorted.sort((e1, e2)->{
////                return e2.getKey().compareTo(e1.getKey());
////            });
////
////
////            List<AbstractMap.SimpleEntry<Long, Integer>> diagonal = new ArrayList<>();
////            if(Grid.experiments.equals("DIAG_COMP") || Grid.experiments.equals("DIAG_PR") || Grid.experiments.equals("DIAG_ONLY")) {
////                edgesSorted.forEach(e -> {
////                    if (e.getValue() == 5 || e.getValue() == 6 || e.getValue() == 7 || e.getValue() == 8) {
////                        diagonal.add(e);
////                    }
////                });
////
////                edgesSorted.removeIf(e -> {
////                    if (e.getValue() == 5 || e.getValue() == 6 || e.getValue() == 7 || e.getValue() == 8) {
////                        return true;
////                    }
////                    return false;
////                });
////            }
////
////
////            if(spaces.size()==4){
////                if(Grid.experiments.equals("DIAG_COMP") || Grid.experiments.equals("DIAG_PR") || Grid.experiments.equals("DIAG_ONLY")) {
////                    diagonal.sort((e1, e2) -> {
////                        return e2.getKey().compareTo(e1.getKey());
////                    });
////                    diagonal.forEach(e -> checkForEliminationFourSpacesCase(e.getValue()));
////                }
////
//////                if(!(Grid.experiments.equals("DIAG_ONLY") || Grid.experiments.isEmpty())) {
////                if(!Grid.experiments.equals("DIAG_ONLY")) {
////                    edgesSorted.forEach(e -> {
////                        checkForEliminationFourSpacesCase(e.getValue());
////                    });
////                }
////            }else if(spaces.size()==3){
////                edgesSorted.forEach(e->{
////                    checkForEliminationThreeSpacesCase(e.getValue());
////                });
////            }
////
////        }
////    }
//
//    public void createEdges(){
//        //eliminations
//        if(spaces.length==4 || spaces.length==3){
//
//            List<AbstractMap.SimpleEntry<Long,Integer>> edgesSorted = new ArrayList<>();
//            for (int i = 0; i < edges.length; i++) {
//                edgesSorted.add(new AbstractMap.SimpleEntry(edges[i].getWeight(), i+1));
//            }
//            edgesSorted.sort((e1, e2)->{
//                return e2.getKey().compareTo(e1.getKey());
//            });
//
//
//            List<AbstractMap.SimpleEntry<Long, Integer>> diagonal = new ArrayList<>();
//                edgesSorted.forEach(e -> {
//                    if (e.getValue() == 5 || e.getValue() == 6 || e.getValue() == 7 || e.getValue() == 8) {
//                        diagonal.add(e);
//                    }
//                });
//
//                edgesSorted.removeIf(e -> {
//                    if (e.getValue() == 5 || e.getValue() == 6 || e.getValue() == 7 || e.getValue() == 8) {
//                        return true;
//                    }
//                    return false;
//                });
//
//            if(spaces.length==4){
//                    diagonal.sort((e1, e2) -> {
//                        return e2.getKey().compareTo(e1.getKey());
//                    });
//                    diagonal.forEach(e -> checkForEliminationFourSpacesCase(e.getValue()));
//
//                    edgesSorted.forEach(e -> {
//                        checkForEliminationFourSpacesCase(e.getValue());
//                    });
//            }else if(spaces.length==3){
//                edgesSorted.forEach(e->{
//                    checkForEliminationThreeSpacesCase(e.getValue());
//                });
//            }
//
//        }
//    }
//
//    private void checkForEliminationFourSpacesCase(int edge, int edgeSameType1, int edgeDifferentType1, int edgeSameType2, int edgeDifferentType2){
//        if(!edges[edge-1].isLocked()){
//            boolean g1 = canBeEliminated(edge, edgeSameType1, edgeDifferentType1);
//            boolean g2 = canBeEliminated(edge, edgeSameType2, edgeDifferentType2);
//
//            if(g1 && g2){
////                long l = Math.max(Math.max(edges[edgeSameType1-1].getWeight(), edges[edgeSameType2-1].getWeight()), Math.max(edges[edgeDifferentType1-1].getWeight(), edges[edgeDifferentType2-1].getWeight()));
////                if(edges[edgeSameType1 - 1].getWeight() == l || edges[edgeDifferentType1 - 1].getWeight() == l){
////                    System.out.println("YEP1");
////                    eliminate(edge, edgeSameType2, edgeDifferentType2);
////                }else{
////                    System.out.println("YEP2");
////                    eliminate(edge, edgeSameType1, edgeDifferentType1);
////                }
////                long c1Cost = (edges[edgeSameType1-1].getHead().getNumberOfPointsAType() +edges[edgeDifferentType1-1].getHead().getNumberOfPointsAType())*(edges[edgeSameType1-1].getHead().getNumberOfPointsBType() + edges[edgeDifferentType1-1].getHead().getNumberOfPointsBType());
////                long c2Cost = (edges[edgeSameType2-1].getHead().getNumberOfPointsAType() +edges[edgeDifferentType2-1].getHead().getNumberOfPointsAType())*(edges[edgeSameType2-1].getHead().getNumberOfPointsBType() + edges[edgeDifferentType2-1].getHead().getNumberOfPointsBType());;
////                if(c1Cost>c2Cost){
////                    eliminate(edge, edgeSameType2, edgeDifferentType2);
////                }else{
////                    eliminate(edge, edgeSameType1, edgeDifferentType1);
////                }
//                if(edges[edgeSameType1-1].getWeight()+edges[edgeDifferentType1-1].getWeight()<edges[edgeSameType2-1].getWeight()+ edges[edgeDifferentType2-1].getWeight()){
//                    eliminate(edge, edgeSameType2, edgeDifferentType2);
//                }else{
//                    eliminate(edge, edgeSameType1, edgeDifferentType1);
//                }
//            }else if(g1){
//                eliminate(edge, edgeSameType1, edgeDifferentType1);
//            }else if(g2){
//                eliminate(edge, edgeSameType2, edgeDifferentType2);
//            }
//        }
//    }
//
//    private void eliminate(int edge, int edgeSameType, int edgeDifferentType){
////        if((edge==5) || (edge==6)  || (edge==7) || (edge==8)){
////            System.out.println("DIAGONAL ELIMINATION");
////        }else{
////            System.out.println("PERIMITER ELIMINATE");
////        }
//        edges[edge-1].eliminate();
//        edges[edgeSameType-1].lock();
//        edges[edgeDifferentType-1].lock();
//    }
//
//    private boolean canBeEliminated(int edge, int edgeSameType, int edgeDifferentType){
//        if(edges[edge-1].getTypeSet().equals(edges[edgeSameType-1].getTypeSet()) && (!edges[edgeSameType-1].isEliminated()) &&
//                (!edges[edge-1].getTypeSet().equals(edges[edgeDifferentType-1].getTypeSet())) && (!edges[edgeDifferentType-1].isEliminated())  ){
//            return true;
//        }
//        return false;
//    }
//
//    private void checkForEliminationFourSpacesCase(int index){
//        switch(index){
//            case 1:
//                checkForEliminationFourSpacesCase(1,3,7,5,9);
//                break;
//            case 2:
//                checkForEliminationFourSpacesCase(2,7,3,9,5);
//                break;
//            case 3:
//                checkForEliminationFourSpacesCase(3,1,8,5,11);
//                break;
//            case 4:
//                checkForEliminationFourSpacesCase(4,8,1,11,5);
//                break;
//            case 5:
//                checkForEliminationFourSpacesCase(5,1,10,3,12);
//                break;
//            case 6:
//                checkForEliminationFourSpacesCase(6,10,1,12,3);
//                break;
//            case 7:
//                checkForEliminationFourSpacesCase(7,9,11,2,4);
//                break;
//            case 8:
//                checkForEliminationFourSpacesCase(8,11,9,4,2);
//                break;
//            case 9:
//                checkForEliminationFourSpacesCase(9,2,6,7,12);
//                break;
//            case 10:
//                checkForEliminationFourSpacesCase(10,6,2,12,7);
//                break;
//            case 11:
//                checkForEliminationFourSpacesCase(11,8,10,4,6);
//                break;
//            case 12:
//                checkForEliminationFourSpacesCase(12,6,4,10,8);
//                break;
//            default:
//                try {
//                    throw new Exception("The index for edge elimination is out of bounds.");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//        }
//    }
//
//    private void checkForEliminationThreeSpacesCase(int edge, int edgeSameType, int edgeDifferentType){
//        if(!edges[edge-1].isLocked()){
//            if(canBeEliminated(edge, edgeSameType, edgeDifferentType)){
//                eliminate(edge, edgeSameType, edgeDifferentType);
//            }
//        }
//    }
//
//    private void checkForEliminationThreeSpacesCase(int index){
//        switch(index){
//            case 1:
//                checkForEliminationThreeSpacesCase(1,3,5);
//                break;
//            case 2:
//                checkForEliminationThreeSpacesCase(2,5,3);
//                break;
//            case 3:
//                checkForEliminationThreeSpacesCase(3,1,6);
//                break;
//            case 4:
//                checkForEliminationThreeSpacesCase(4,6,1);
//                break;
//            case 5:
//                checkForEliminationThreeSpacesCase(5,2,4);
//                break;
//            case 6:
//                checkForEliminationThreeSpacesCase(6,4,2);
//                break;
//            default:
//                try {
//                    throw new Exception("The index for edge elimination is out of bounds.");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//        }
//    }
//
//
////    public List<T> getPartitions(T tailSpace, T headSpace, TypeSet typeSet){
////
////        //System.out.println("SIZE"+spaces.size());
////        List<T> partitions = new ArrayList<>();
////        int tailSpaceIndex = spaces.indexOf(tailSpace);
////        partitions.add(spaces.get(tailSpaceIndex));
////
////        if(spaces.length==2){
////            if(edges[tailSpaceIndex].getHead().equals(headSpace)){
////                if(edges[tailSpaceIndex].getTypeSet().equals(typeSet)){
////                    partitions.add(headSpace);
////                }
////            }else{
////                try {
////                    throw new Exception("For the case of 2 spaces, there was a problem for determining the agreement among a tail and a head space.");
////                } catch (Exception exception) {
////                    exception.printStackTrace();
////                }
////            }
////        }else if(spaces.length==3){
////            Optional<T> o;
////            if(tailSpaceIndex == 0){
////                o = threeSpacesCasePartition(headSpace, 1, 3, typeSet);
////                o.ifPresent(partitions::add);
////            }else if(tailSpaceIndex == 1){
////                o = threeSpacesCasePartition(headSpace, 2, 5, typeSet);
////                o.ifPresent(partitions::add);
////            }else if(tailSpaceIndex == 2){
////                o = threeSpacesCasePartition(headSpace, 4, 6, typeSet);
////                o.ifPresent(partitions::add);
////            }else{
////                try {
////                    throw new Exception("Unexpected tail index for determining the agreement");
////                } catch (Exception exception) {
////                    exception.printStackTrace();
////                }
////            }
////        } else if(spaces.length==4){
////            Optional<T> o;
////            if(tailSpaceIndex == 0){
////                o = fourSpacesCasePartition(headSpace,1,3,5, typeSet);
////                o.ifPresent(partitions::add);
////            }else if(tailSpaceIndex == 1){
////                o = fourSpacesCasePartition(headSpace,2,7,9, typeSet);
////                o.ifPresent(partitions::add);
////            }else if(tailSpaceIndex == 2){
////                o = fourSpacesCasePartition(headSpace,4,8 ,11, typeSet);
////                o.ifPresent(partitions::add);
////            }else if(tailSpaceIndex == 3){
////                o = fourSpacesCasePartition(headSpace,6, 10, 12, typeSet);
////                o.ifPresent(partitions::add);
////            }else{
////                try {
////                    throw new Exception("Unexpected tail index for determining the agreement");
////                } catch (Exception exception) {
////                    exception.printStackTrace();
////                }
////            }
////        }
////        else{
////            try {
////                throw new Exception("Unexpected number of spaces for determining an agreement among a tail and a head space.");
////            } catch (Exception exception) {
////                exception.printStackTrace();
////            }
////        }
////        return partitions;
////    }
//
//    private Edge getEdge(int index){
//        return edges[index-1];
//    }
//
////    public void getPartitionsNEW(int tailSpaceIndexLocal, int headSpaceIndexLocal, TypeSet typeSet, List<Cell> list) {
////        if (spaces.length == 4) {
////            if (tailSpaceIndexLocal == 0) {
////                switch (headSpaceIndexLocal) {
////                    case 1:
////                        if(getEdge(1).getTypeSet().equals(typeSet)){
//////                            list.add((Cell) spaces[1]);
////                        }
////                        break;
////                    case 2:
////                        if(getEdge(3).getTypeSet().equals(typeSet)){
//////                            list.add((Cell) spaces[2]);
////                        }
////                        break;
////                    case 3:
////                        if(getEdge(5).getTypeSet().equals(typeSet)){
//////                            list.add((Cell) spaces[3]);
////                        }
////                        break;
////                    default:
////                        try {
////                            throw new Exception("The case should be 1, 2 or 3.");
////                        } catch (Exception e) {
////                            throw new RuntimeException(e);
////                        }                    }
////                } else if (tailSpaceIndexLocal == 3) {
////                    switch (headSpaceIndexLocal) {
////                        case 0:
////                            if(getEdge(6).getTypeSet().equals(typeSet)){
//////                                list.add((Cell) spaces[0]);
////                            }
////                            break;
////                        case 1:
////                            if(getEdge(10).getTypeSet().equals(typeSet)){
//////                                list.add((Cell) spaces[1]);
////                            }
////                            break;
////                        case 2:
////                            if(getEdge(12).getTypeSet().equals(typeSet)){
//////                                list.add((Cell) spaces[2]);
////                            }
////                            break;
////                        default:
////                            try {
////                                throw new Exception("The case should be 0, 1 or 2.");
////                            } catch (Exception e) {
////                                throw new RuntimeException(e);
////                            }
////                    }
////                } else {
////                    try {
////                        throw new Exception("Should be either 0 or 3 always.");
////                    } catch (Exception exception) {
////                        exception.printStackTrace();
////                    }
////                }
////            } else if (spaces.length == 2) {
////                if (edges[0].getTypeSet().equals(typeSet)) {
////                    if (tailSpaceIndexLocal == 0) {
//////                        list.add((Cell) spaces[1]);
////                    }else{
//////                        list.add((Cell) spaces[0]);
////                    }
////                }
////            } else {
////                try {
////                    throw new Exception("Unexpected number of spaces for determining an agreement among a tail and a head space.");
////                } catch (Exception exception) {
////                    exception.printStackTrace();
////                }
////            }
////    }
//
////    private Optional<T> fourSpacesCasePartition(T headSpace, int edge1, int edge2, int edge3, TypeSet typeSet){
////
////        Optional<T> o = null;
////        if(edges[edge1-1].getHead().equals(headSpace)){
////            if(edges[edge1-1].getTypeSet().equals(typeSet)){
////                o = Optional.of(edges[edge1-1].getHead());
////            }
////            else{
////                o = Optional.empty();
////            }
////        }
////        else if(edges[edge2-1].getHead().equals(headSpace)){
////            if(edges[edge2-1].getTypeSet().equals(typeSet)){
////                o = Optional.of(edges[edge2-1].getHead());
////            }
////            else{
////                o = Optional.empty();
////            }
////        }
////        else if(edges[edge3-1].getHead().equals(headSpace)){
////            if(edges[edge3-1].getTypeSet().equals(typeSet)){
////                o = Optional.of(edges[edge3-1].getHead());
////            }
////            else{
////                o = Optional.empty();
////            }
////        }
////        else {
////            try {
////                throw new Exception("For the case of 4 spaces, there was a problem for determining the agreement for a head space.");
////            } catch (Exception exception) {
////                exception.printStackTrace();
////            }
////        }
////        return o;
////    }
//
////    private Optional<T> threeSpacesCasePartition(T headSpace, int edge1, int edge2, TypeSet typeSet){
////
////        Optional<T> o = null;
////        if(edges[edge1-1].getHead().equals(headSpace)){
////            if(edges[edge1-1].getTypeSet().equals(typeSet)){
////                o = Optional.of(edges[edge1-1].getHead());
////            }
////            else{
////                o = Optional.empty();
////            }
////        }
////        else if(edges[edge2-1].getHead().equals(headSpace)){
////            if(edges[edge2-1].getTypeSet().equals(typeSet)){
////                o = Optional.of(edges[edge2-1].getHead());
////            }
////            else{
////                o = Optional.empty();
////            }
////        } else {
////            try {
////                throw new Exception("For the case of 3 spaces, there was a problem for determining the agreement for a head space.");
////            } catch (Exception exception) {
////                exception.printStackTrace();
////            }
////        }
////        return o;
////    }
//
//    //get the hashcodes for passing a point on a given space
////    public List<T> getPartitions(T space, TypeSet typeSet, boolean outerQuarter){
////        int spaceIndex = spaces.indexOf(space);
////
////        List<T> partitions = null;
////
////        if(spaces.size()==1){
////            partitions = new ArrayList<>();
////            partitions.add(spaces.get(spaceIndex));
////        }else if(spaces.size()==2){
////            partitions = new ArrayList<>();
////            partitions.add(spaces.get(spaceIndex));
////            if(edges[spaceIndex].getTypeSet().equals(typeSet)){
////                partitions.add(edges[spaceIndex].getHead());
////            }
////        }else if(spaces.size()==4){
////            if(spaceIndex == 0){
////                partitions = fourSpacesCasePartitions(0,1,3,5, typeSet, outerQuarter, 5);
////            }else if(spaceIndex == 1){
////                partitions = fourSpacesCasePartitions(1,2,7,9, typeSet, outerQuarter, 7);
////            }else if(spaceIndex == 2){
////                partitions = fourSpacesCasePartitions(2,4,8 ,11, typeSet, outerQuarter, 8);
////            }else if(spaceIndex == 3){
////                partitions = fourSpacesCasePartitions(3,6, 10, 12, typeSet, outerQuarter, 6);
////            }else{
////                try {
////                    throw new Exception("Unexpected space index for determining its agreements");
////                } catch (Exception exception) {
////                    exception.printStackTrace();
////                }
////            }
////        }else{
////            try {
////                throw new Exception("Unexpected number of spaces for determining the agreements of a space.");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
////        return partitions;
////    }
//
////    public List<T> getPartitionsFourSpacesCase(T space, TypeSet typeSet, boolean outerQuarter){
////        int spaceIndex = spaces.indexOf(space);
////
////        List<T> partitions = null;
////
////        if(spaces.length==4){
////            if(spaceIndex == 0){
////                partitions = fourSpacesCasePartitions(0,1,3,5, typeSet, outerQuarter, 5);
////            }else if(spaceIndex == 1){
////                partitions = fourSpacesCasePartitions(1,2,7,9, typeSet, outerQuarter, 7);
////            }else if(spaceIndex == 2){
////                partitions = fourSpacesCasePartitions(2,4,8 ,11, typeSet, outerQuarter, 8);
////            }else if(spaceIndex == 3){
////                partitions = fourSpacesCasePartitions(3,6, 10, 12, typeSet, outerQuarter, 6);
////            }else{
////                try {
////                    throw new Exception("Unexpected space index for determining its agreements");
////                } catch (Exception exception) {
////                    exception.printStackTrace();
////                }
////            }
////        }else{
////            try {
////                throw new Exception("Unexpected number of spaces for determining the agreements of a space.");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
////        return partitions;
////    }
//
////    public void getPartitionsFourSpacesCase(int spaceIndex, TypeSet typeSet, boolean outerQuarter, List<Cell> partitions){
////        switch(spaceIndex){
////            case 0:
////                fourSpacesCasePartitions(1,3,5, typeSet, outerQuarter, partitions);
////                break;
////            case 1:
////                fourSpacesCasePartitions(2,9,7, typeSet, outerQuarter, partitions);
////                break;
////            case 2:
////                fourSpacesCasePartitions(4,11 ,8, typeSet, outerQuarter, partitions);
////                break;
////            case 3:
////                fourSpacesCasePartitions(10, 12, 6, typeSet, outerQuarter, partitions);
////                break;
////            default:
////                try {
////                    throw new Exception("Unexpected space index for determining its agreements");
////                } catch (Exception e) {
////                    throw new RuntimeException(e);
////                }
////        }
////    }
//
//    public void getPartitionsFourSpacesCase(int spaceIndex, TypeSet typeSet, boolean outerQuarter, List<String> partitions){
//        switch(spaceIndex){
//            case 0:
//                fourSpacesCasePartitions(1,3,5, typeSet, outerQuarter, partitions);
//                break;
//            case 1:
//                fourSpacesCasePartitions(2,9,7, typeSet, outerQuarter, partitions);
//                break;
//            case 2:
//                fourSpacesCasePartitions(4,11 ,8, typeSet, outerQuarter, partitions);
//                break;
//            case 3:
//                fourSpacesCasePartitions(10, 12, 6, typeSet, outerQuarter, partitions);
//                break;
//            default:
//                try {
//                    throw new Exception("Unexpected space index for determining its agreements");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//        }
//    }
//
//
////    public List<T> getPartitionsThreeSpacesCase(T space, TypeSet typeSet){
////        int spaceIndex = spaces.indexOf(space);
////
////        List<T> partitions = null;
////
////        if(spaces.size()==3){
////            if(spaceIndex == 0){
////                partitions = threeSpacesCasePartitions(0,1,3, typeSet);
////            }else if(spaceIndex == 1){
////                partitions = threeSpacesCasePartitions(1,2,5, typeSet);
////            }else if(spaceIndex == 2){
////                partitions = threeSpacesCasePartitions(2,4,6 , typeSet);
////            }else{
////                try {
////                    throw new Exception("Unexpected space index for determining its agreements");
////                } catch (Exception exception) {
////                    exception.printStackTrace();
////                }
////            }
////        }else{
////            try {
////                throw new Exception("Unexpected number of spaces for determining the agreements of a space.");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
////        return partitions;
////    }
//
////    public List<T> getPartitionsTwoSpacesCase(T tailSpace, T headSpace, TypeSet typeSet){
////        int tailSpaceIndex = spaces.indexOf(tailSpace);
////
////        List<T> partitions = new ArrayList<>();
////        partitions.add(spaces[tailSpaceIndex]);
////
////        if(spaces.length==2){
////            if(edges[tailSpaceIndex].getHead().equals(headSpace)){
////                if(edges[tailSpaceIndex].getTypeSet().equals(typeSet)){
////                    partitions.add(headSpace);
////                }
////            }else{
////                try {
////                    throw new Exception("For the case of 2 spaces, there was a problem for determining the agreement among a tail and a head space.");
////                } catch (Exception exception) {
////                    exception.printStackTrace();
////                }
////            }
////        }
////        else{
////            try {
////                throw new Exception("Unexpected number of spaces for determining an agreement among a tail and a head space.");
////            } catch (Exception exception) {
////                exception.printStackTrace();
////            }
////        }
////        return partitions;
////    }
//
////    public void getPartitionsTwoSpacesCase(Cell headSpace, TypeSet typeSet, List<Cell> partitions){
////                if(edges[0].getTypeSet().equals(typeSet)){
////                    partitions.add(headSpace);
////                }
////    }
//
//    public void getPartitionsTwoSpacesCase(int headSpace, TypeSet typeSet, List<String> partitions){
//        if(edges[0].getTypeSet().equals(typeSet)){
//            partitions.add(String.valueOf(headSpace));
//        }
//    }
//
//    public static AgreementsBackup2 newAgreements(int[] spaces, List<Cell> cells, Point point, /*ReplicationType function*/Function6<Integer, Integer, Cell, Cell,Point, Agreement> function){
//        return new AgreementsBackup2(spaces, cells, point, function);
//    }
//
////    private List<T> fourSpacesCasePartitions(int spaceIndex, int edge1, int edge2, int edge3, TypeSet typeSet, boolean outerQuarter, int diagonalEdge){
////        List<T> partitions = new ArrayList<>();
////        partitions.add(spaces[spaceIndex]);
////        if(edges[edge1-1].getTypeSet().equals(typeSet) && (!edges[edge1-1].isEliminated() /*|| outerQuarter*/)){
////            partitions.add(edges[edge1-1].getHead());
////        }
////        if(edges[edge2-1].getTypeSet().equals(typeSet) && (!edges[edge2-1].isEliminated() /*|| outerQuarter*/)){
////            partitions.add(edges[edge2-1].getHead());
////        }
////        if(edges[edge3-1].getTypeSet().equals(typeSet) && (!edges[edge3-1].isEliminated() /*|| outerQuarter*/)){
////            partitions.add(edges[edge3-1].getHead());
////        }
////
////        if(outerQuarter){
////            if(!edges[diagonalEdge-1].isLocked() ){
////                partitions.remove(edges[diagonalEdge-1].getHead());
////            }
////        }
////
////        return partitions;
////    }
//
//
////    private void fourSpacesCasePartitions(int edge1, int edge2, int edge3, TypeSet typeSet, boolean outerQuarter, List<Cell> partitions){
////        //the third edge is always the diagonal
////        if(edges[edge1-1].getTypeSet().equals(typeSet) && (!edges[edge1-1].isEliminated() /*|| outerQuarter*/)){
//////            partitions.add((Cell) edges[edge1-1].getHead());
////        }
////        if(edges[edge2-1].getTypeSet().equals(typeSet) && (!edges[edge2-1].isEliminated() /*|| outerQuarter*/)){
//////            partitions.add((Cell) edges[edge2-1].getHead());
////        }
////
////        if(edges[edge3-1].getTypeSet().equals(typeSet)){
////            if(!outerQuarter){
////                if(!edges[edge3-1].isEliminated()){
//////                    partitions.add((Cell) edges[edge3-1].getHead());
////                }
////            }else{
////                if(edges[edge3-1].isLocked()){
//////                    partitions.add((Cell) edges[edge3-1].getHead());
////                }
////            }
////        }
////    }
//
//    private void fourSpacesCasePartitions(int edge1, int edge2, int edge3, TypeSet typeSet, boolean outerQuarter, List<String> partitions){
//        //the third edge is always the diagonal
//        if(edges[edge1-1].getTypeSet().equals(typeSet) && (!edges[edge1-1].isEliminated() /*|| outerQuarter*/)){
//            partitions.add(String.valueOf(edges[edge1-1].getHead()));
//        }
//        if(edges[edge2-1].getTypeSet().equals(typeSet) && (!edges[edge2-1].isEliminated() /*|| outerQuarter*/)){
//            partitions.add(String.valueOf(edges[edge2-1].getHead()));
//        }
//
//        if(edges[edge3-1].getTypeSet().equals(typeSet)){
//            if(!outerQuarter){
//                if(!edges[edge3-1].isEliminated()){
//                    partitions.add(String.valueOf(edges[edge3-1].getHead()));
//                }
//            }else{
//                if(edges[edge3-1].isLocked()){
//                    partitions.add(String.valueOf(edges[edge3-1].getHead()));
//                }
//            }
//        }
//    }
//
//
////    private List<T> threeSpacesCasePartitions(int spaceIndex, int edge1, int edge2, TypeSet typeSet){
////        List<T> partitions = new ArrayList<>();
////        partitions.add(spaces.get(spaceIndex));
////        if(edges[edge1-1].getTypeSet().equals(typeSet) && (!edges[edge1-1].isEliminated())){
////            partitions.add(edges[edge1-1].getHead());
////        }
////        if(edges[edge2-1].getTypeSet().equals(typeSet) && (!edges[edge2-1].isEliminated())){
////            partitions.add(edges[edge2-1].getHead());
////        }
////        return partitions;
////    }
//
////    public String toString(Function<T, Long> spaceToId){
////        StringBuilder sb = new StringBuilder();
////        sb.append("Spaces: ");
////        spaces.forEach(c-> sb.append(spaceToId.apply(c)).append(", "));
////        sb.deleteCharAt(sb.lastIndexOf(","));
////        sb.append("\n");
////
////        for (Edge<T> edge : edges) {
////            sb.append(spaceToId.apply(edge.getTail())).append(" -> ").append(spaceToId.apply(edge.getHead())).append(" TypeSet: ").append(edge.getTypeSet()).append(" Weight: ").append(edge.getWeight());
////            if(edge.isEliminated()){
////                sb.append(" (Eliminated)");
////            }
////
////            if(edge.isLocked()){
////                sb.append(" (Locked)");
////            }
////            sb.append("\n");
////        }
////        return sb.toString();
////    }
//
////    public Optional<T> getExtraPartition(T space1, T space2, TypeSet typeSet){
////
////        Edge<T> edge = getEdge(space2, space1);
////
////        if(edge.getTypeSet() != typeSet && edge.isEliminated()) {
////            List<T> spaces = new ArrayList<>(this.spaces);
////            spaces.remove(space1);
////            spaces.remove(space2);
////            Edge<T> edge1 = getEdge(space1,spaces.get(0));
////            Edge<T> edge2 = getEdge(space1,spaces.get(1));
////
//////            if(edge1.isLocked() && edge1.getTypeSet().equals(typeSet) && edge2.isEliminated() ){
//////                return Optional.of(edge1.getHead());
//////            }else if(edge2.isLocked() && edge2.getTypeSet().equals(typeSet) && edge1.isEliminated()){
//////                return Optional.of(edge2.getHead());
//////            }else if(edge1.isLocked() && !edge2.isLocked()){
//////                return Optional.of(edge1.getHead());
//////            }else if(edge2.isLocked() && !edge1.isLocked()){
//////                return Optional.of(edge2.getHead());
//////            }
////
////            if(edge1.isLocked() && edge1.getTypeSet().equals(typeSet) && !(edge2.isLocked() && edge2.getTypeSet().equals(typeSet))){
////                return Optional.of(edge1.getHead());
////            }else if(edge2.isLocked() && edge2.getTypeSet().equals(typeSet) && !(edge1.isLocked() && edge1.getTypeSet().equals(typeSet))){
////                return Optional.of(edge2.getHead());
////            }
////
////            try {
////                throw new Exception("No locked space found");
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////            return null;
////        }
////        return Optional.empty();
////    }
//
//    public int getExtraPartition(int edgeIndexMarked, int edgeIndexLocked1, int edgeIndexLocked2, TypeSet typeSet){
//
//        Edge edge = getEdge(edgeIndexMarked);
//
//        if(edge.getTypeSet() != typeSet && edge.isEliminated()) {
//            Edge edge1 = getEdge(edgeIndexLocked1);
//            Edge edge2 = getEdge(edgeIndexLocked2);
//
//            if(edge1.isLocked() && edge1.getTypeSet().equals(typeSet) /*&& !(edge2.isLocked() && edge2.getTypeSet().equals(typeSet))*/){//the second condition is entailed to be true if the first is true
////                return Optional.of(edge1.getHead());
//                return edge1.getHead();
//
//            }else /*if(edge2.isLocked() && edge2.getTypeSet().equals(typeSet) && !(edge1.isLocked() && edge1.getTypeSet().equals(typeSet)))*/{//the second condition is entailed to be true if the first is true
////                return Optional.of(edge2.getHead());
//                return edge2.getHead();
//            }
////            return null;
//        }
////        return Optional.empty();
//       return -1;
//    }
//
//    public Edge getEdge(int tail, int head){
//        for (Edge edge : edges) {
//            if(edge.getHead() == head && edge.getTail() == tail){
//                return edge;
//            }
//        }
//        try {
//            throw new Exception("No edge found for the given spaces");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void perimiterCompliance(){
//        if(edges[0].getTypeSet().equals(edges[2].getTypeSet())
//                && edges[2].getTypeSet().equals(edges[8].getTypeSet())
//                && edges[8].getTypeSet().equals(edges[10].getTypeSet())){
//
//
//
//            edges[4] = Edge.newEdge( edges[4].getTail(), edges[4].getHead(), edges[0].getTypeSet(), edges[4].getWeight());
//            edges[5] = Edge.newEdge( edges[5].getTail(), edges[5].getHead(), edges[0].getTypeSet(), edges[5].getWeight());
//            edges[6] = Edge.newEdge( edges[6].getTail(), edges[6].getHead(), edges[0].getTypeSet(), edges[6].getWeight());
//            edges[7] = Edge.newEdge( edges[7].getTail(), edges[7].getHead(), edges[0].getTypeSet(), edges[7].getWeight());
//        }else if((edges[0].getTypeSet().equals(edges[2].getTypeSet()) && edges[0].getTypeSet().equals(edges[8].getTypeSet()))
//                || (edges[0].getTypeSet().equals(edges[2].getTypeSet()) && edges[0].getTypeSet().equals(edges[10].getTypeSet()))
//                || (edges[0].getTypeSet().equals(edges[8].getTypeSet()) && edges[0].getTypeSet().equals(edges[10].getTypeSet()))){
//            //10
//            edges[4] = Edge.newEdge( edges[4].getTail(), edges[4].getHead(), edges[0].getTypeSet(), edges[4].getWeight());
//            edges[5] = Edge.newEdge( edges[5].getTail(), edges[5].getHead(), edges[0].getTypeSet(), edges[5].getWeight());
//            edges[6] = Edge.newEdge( edges[6].getTail(), edges[6].getHead(), edges[0].getTypeSet(), edges[6].getWeight());
//            edges[7] = Edge.newEdge( edges[7].getTail(), edges[7].getHead(), edges[0].getTypeSet(), edges[7].getWeight());
//        }else if(edges[2].getTypeSet().equals(edges[8].getTypeSet()) && edges[2].getTypeSet().equals(edges[10].getTypeSet())){
//            //0
//            edges[4] = Edge.newEdge( edges[4].getTail(), edges[4].getHead(), edges[2].getTypeSet(), edges[4].getWeight());
//            edges[5] = Edge.newEdge( edges[5].getTail(), edges[5].getHead(), edges[2].getTypeSet(), edges[5].getWeight());
//            edges[6] = Edge.newEdge( edges[6].getTail(), edges[6].getHead(), edges[2].getTypeSet(), edges[6].getWeight());
//            edges[7] = Edge.newEdge( edges[7].getTail(), edges[7].getHead(), edges[2].getTypeSet(), edges[7].getWeight());
//        }
//    }
//
//    public void getPartitionsNEW(int tailSpaceIndexLocal, int headSpaceIndexLocal, TypeSet typeSet, List<String> list) {
//        if (spaces.length== 4) {
//            if (tailSpaceIndexLocal == 0) {
//                switch (headSpaceIndexLocal) {
//                    case 1:
//                        if(getEdge(1).getTypeSet().equals(typeSet)){
//                            list.add(String.valueOf(spaces[1]));
//                        }
//                        break;
//                    case 2:
//                        if(getEdge(3).getTypeSet().equals(typeSet)){
//                            list.add(String.valueOf(spaces[2]));
//                        }
//                        break;
//                    case 3:
//                        if(getEdge(5).getTypeSet().equals(typeSet)){
//                            list.add(String.valueOf(spaces[3]));
//                        }
//                        break;
//                    default:
//                        try {
//                            throw new Exception("The case should be 1, 2 or 3.");
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }                    }
//            } else if (tailSpaceIndexLocal == 3) {
//                switch (headSpaceIndexLocal) {
//                    case 0:
//                        if(getEdge(6).getTypeSet().equals(typeSet)){
//                            list.add(String.valueOf(spaces[0]));
//                        }
//                        break;
//                    case 1:
//                        if(getEdge(10).getTypeSet().equals(typeSet)){
//                            list.add(String.valueOf(spaces[1]));
//                        }
//                        break;
//                    case 2:
//                        if(getEdge(12).getTypeSet().equals(typeSet)){
//                            list.add(String.valueOf(spaces[2]));
//                        }
//                        break;
//                    default:
//                        try {
//                            throw new Exception("The case should be 0, 1 or 2.");
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                }
//            } else {
//                try {
//                    throw new Exception("Should be either 0 or 3 always.");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        } else if (spaces.length == 2) {
//            if (edges[0].getTypeSet().equals(typeSet)) {
//                if (tailSpaceIndexLocal == 0) {
//                    list.add(String.valueOf(spaces[1]));
//                }else{
//                    list.add(String.valueOf(spaces[0]));
//                }
//            }
//        } else {
//            try {
//                throw new Exception("Unexpected number of spaces for determining an agreement among a tail and a head space.");
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//    }
//
//    public boolean haveAllTypeA(){
//        if (spaces.length == 4) {
//            return edges[0].getTypeSet().equals(TypeSet.A) &&
//                    edges[2].getTypeSet().equals(TypeSet.A) &&
//                    edges[4].getTypeSet().equals(TypeSet.A) &&
//                    edges[6].getTypeSet().equals(TypeSet.A) &&
//                    edges[8].getTypeSet().equals(TypeSet.A) &&
//                    edges[10].getTypeSet().equals(TypeSet.A);
//        }
//        else if (spaces.length == 2){
//            return edges[0].getTypeSet().equals(TypeSet.A);
//        }
//        else if(spaces.length==1){
//            return false;
//        }
//        try {
//            throw new Exception("Spaces should be 4, 2 or 1");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public boolean haveAllTypeB(){
//        if (spaces.length == 4) {
//            return edges[0].getTypeSet().equals(TypeSet.B) &&
//                    edges[2].getTypeSet().equals(TypeSet.B) &&
//                    edges[4].getTypeSet().equals(TypeSet.B) &&
//                    edges[6].getTypeSet().equals(TypeSet.B) &&
//                    edges[8].getTypeSet().equals(TypeSet.B) &&
//                    edges[10].getTypeSet().equals(TypeSet.B);
//        }
//        else if (spaces.length == 2){
//            return edges[0].getTypeSet().equals(TypeSet.B);
//        }
//        else if(spaces.length==1){
//            return false;
//        }
//        try {
//            throw new Exception("Spaces should be 4, 2 or 1");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
