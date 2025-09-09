package gr.ds.unipi.self.agreements;

import gr.ds.unipi.grid.Function4;
import gr.ds.unipi.self.grid.Cell;
import gr.ds.unipi.shapes.Point;

import java.util.*;

public class Agreements {

    private final int[] cellsIdReference;//Four spaces have 1 element (id of bottom left cell). Two spaces have 2 elements (ids of the two cells).
    private Edge[] edges;

    public int getSpacesNum(){
        switch (edges.length){
            case 0:
                return 1;
            case 1:
                return 2;
            case 3:
                return 3;
            case 6:
                return 4;
            default:
                try {
                    throw new Exception("Wrong number of edges for determining the number of spaces.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return -1;
    }

    public void setEdges(Edge[] edges){
        this.edges = edges;
    }

    public Edge[] getEdges(){
        return this.edges;
    }

    private Agreements(int numOfSpaces, int[] cellsIdReference, List<Cell> cells, Point point, /*ReplicationType function*/Function4<Cell, Cell, Point, Edge> function){
        this.cellsIdReference = cellsIdReference;
        switch (numOfSpaces){
            case 1:
                edges = new Edge[0];
                break;
            case 2:
                edges = new Edge[1];
                break;
            case 3:
                edges = new Edge[3];
                break;
            case 4:
                edges = new Edge[6];
                break;
            default:
                edges = null;
                try {
                    throw new Exception("Unexpected number of spaces for creating edges.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        int k = 0;

        for (int i = 0; i< cells.size(); i++){
            for(int y = i+1; y< cells.size(); y++){
                edges[k++] = function.apply(cells.get(i), cells.get(y), point);
            }
        }

    }

//    public void createEdges(){
//        //eliminations
//        if(spaces.size()==4 || spaces.size()==3){
//
//            if(Grid.experiments.equals("DIAG_COMP")){
//                perimiterCompliance();
//            }
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
//            if(Grid.experiments.equals("DIAG_COMP") || Grid.experiments.equals("DIAG_PR") || Grid.experiments.equals("DIAG_ONLY")) {
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
//            }
//
//
//            if(spaces.size()==4){
//                if(Grid.experiments.equals("DIAG_COMP") || Grid.experiments.equals("DIAG_PR") || Grid.experiments.equals("DIAG_ONLY")) {
//                    diagonal.sort((e1, e2) -> {
//                        return e2.getKey().compareTo(e1.getKey());
//                    });
//                    diagonal.forEach(e -> checkForEliminationFourSpacesCase(e.getValue()));
//                }
//
////                if(!(Grid.experiments.equals("DIAG_ONLY") || Grid.experiments.isEmpty())) {
//                if(!Grid.experiments.equals("DIAG_ONLY")) {
//                    edgesSorted.forEach(e -> {
//                        checkForEliminationFourSpacesCase(e.getValue());
//                    });
//                }
//            }else if(spaces.size()==3){
//                edgesSorted.forEach(e->{
//                    checkForEliminationThreeSpacesCase(e.getValue());
//                });
//            }
//
//        }
//    }

    public void checkingForMarking(){
        if(!((getEdge(1).getDirectionCode() && !getEdge(2).getDirectionCode() && getEdge(4).getDirectionCode()) ||
                (!getEdge(1).getDirectionCode() && getEdge(2).getDirectionCode() && !getEdge(4).getDirectionCode()))) {

            if(!(getEdge(1).isEliminated() || getEdge(2).isEliminated() ||getEdge(4).isEliminated())){
                try {
                    throw new Exception("There is a problem "+getNumberOfCyclics()+" "+getSubgraphCase());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(!((getEdge(4).getDirectionCode() && !getEdge(5).getDirectionCode() && getEdge(6).getDirectionCode()) ||
                (!getEdge(4).getDirectionCode() && getEdge(5).getDirectionCode() && !getEdge(6).getDirectionCode()))) {

            if(!(getEdge(4).isEliminated() || getEdge(5).isEliminated() ||getEdge(6).isEliminated())){
                try {
                    throw new Exception("There is a problem "+getNumberOfCyclics()+" "+getSubgraphCase());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

        if(!((getEdge(1).getDirectionCode() && !getEdge(3).getDirectionCode() && getEdge(5).getDirectionCode()) ||
                (!getEdge(1).getDirectionCode() && getEdge(3).getDirectionCode() && !getEdge(5).getDirectionCode()))) {

            if(!(getEdge(1).isEliminated() || getEdge(3).isEliminated() ||getEdge(5).isEliminated())){
                try {
                    throw new Exception("There is a problem "+getNumberOfCyclics()+" "+getSubgraphCase());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }

        if(!((getEdge(2).getDirectionCode() && !getEdge(3).getDirectionCode() && getEdge(6).getDirectionCode()) ||
                (!getEdge(2).getDirectionCode() && getEdge(3).getDirectionCode() && !getEdge(6).getDirectionCode()))) {

            if(!(getEdge(2).isEliminated() || getEdge(3).isEliminated() ||getEdge(6).isEliminated())){
                try {
                    throw new Exception("There is a problem "+getNumberOfCyclics()+" "+getSubgraphCase());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
        System.out.println("check "+getSpacesNum()+" "+getSubgraphCase());

    }

    public boolean isDefault(){
        if(edges.length == 6){
            if(getEdge(1).getDirectionCode() && getEdge(2).getDirectionCode() && !getEdge(3).getDirectionCode() &&
                    getEdge(4).getDirectionCode() && getEdge(5).getDirectionCode() && getEdge(6).getDirectionCode()){
                return true;
            }
            return false;
        }else if(edges.length == 1){
            if(edges[0].getDirectionCode()){
                return true;
            }
                return false;
        }else{
            try {
                throw new Exception("The conditions should have been caught.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createEdges(){
        if(edges.length==6){
            List<AbstractMap.SimpleEntry<Integer,Integer>> edgesSorted = new ArrayList<>();
            for (int i = 0; i < edges.length; i++) {
                edgesSorted.add(new AbstractMap.SimpleEntry(edges[i].getWeight(), i+1));
            }
            edgesSorted.sort((e1, e2)->{
                return e2.getKey().compareTo(e1.getKey());
            });

            List<AbstractMap.SimpleEntry<Integer, Integer>> diagonal = new ArrayList<>();
                edgesSorted.forEach(e -> {
                    if (e.getValue() == 3 || e.getValue() == 4) {
                        diagonal.add(e);
                    }
                });

                edgesSorted.removeIf(e -> {
                    if (e.getValue() == 3 || e.getValue() == 4) {
                        return true;
                    }
                    return false;
                });

                int subgraphCase = getSubgraphCase();
                int cyclics = getNumberOfCyclics();

                switch (subgraphCase){
                    case 1:
                        if(getEdge(3).getDirectionCode() && getEdge(4).getDirectionCode()){
                            if(getEdge(1).getDirectionCode()){
                                checkForEliminationFourSpacesCase(5);
                            }else{
                                checkForEliminationFourSpacesCase(2);
                            }
                        }else if(getEdge(3).getDirectionCode() && !getEdge(4).getDirectionCode()){
                            if(getEdge(1).getDirectionCode()){
                                checkForEliminationFourSpacesCase(1);
                            }else{
                                checkForEliminationFourSpacesCase(6);
                            }
                        }else if(!getEdge(3).getDirectionCode() && getEdge(4).getDirectionCode()){
                            if(getEdge(1).getDirectionCode()){
                                checkForEliminationFourSpacesCase(6);
                            }else{
                                checkForEliminationFourSpacesCase(1);
                            }
                        }else{
                            if(getEdge(1).getDirectionCode()){
                                checkForEliminationFourSpacesCase(2);
                            }else{
                                checkForEliminationFourSpacesCase(5);
                            }
                        }
                        break;
                    case 2:
                        edgesSorted.forEach(e -> {
                            checkForEliminationFourSpacesCase(e.getValue());
                        });
                        diagonal.forEach(e -> checkForEliminationFourSpacesCase(e.getValue()));
                        break;
                    case 3:
                        if(cyclics==0){
                            diagonal.forEach(e -> checkForEliminationFourSpacesCase(e.getValue()));
                            edgesSorted.forEach(e -> {
                                checkForEliminationFourSpacesCase(e.getValue());
                            });
                        }else if(cyclics==2){
                            edgesSorted.forEach(e -> {
                                checkForEliminationFourSpacesCase(e.getValue());
                            });
                        }else if(cyclics==1){
                            if((!getEdge(1).getDirectionCode() && !getEdge(2).getDirectionCode() && !getEdge(3).getDirectionCode()) ||
                                    (getEdge(1).getDirectionCode() && !getEdge(4).getDirectionCode() && !getEdge(5).getDirectionCode()) ||
                                    (getEdge(2).getDirectionCode() && getEdge(4).getDirectionCode() && !getEdge(6).getDirectionCode()) ||
                                    (getEdge(3).getDirectionCode() && getEdge(5).getDirectionCode() && getEdge(6).getDirectionCode())){

                                diagonal.forEach(e -> checkForEliminationFourSpacesCase(e.getValue()));
                                edgesSorted.forEach(e -> {
                                    checkForEliminationFourSpacesCase(e.getValue());
                                });

                            }else{
                                if(getEdge(3).getWeight()<=getEdge(4).getWeight()){
                                    getEdge(4).reverseEdge();
                                }else{
                                    getEdge(3).reverseEdge();
                                }

                                cyclics = getNumberOfCyclics();
                                if(cyclics==0){
                                    diagonal.forEach(e -> checkForEliminationFourSpacesCase(e.getValue()));
                                    edgesSorted.forEach(e -> {
                                        checkForEliminationFourSpacesCase(e.getValue());
                                    });
                                }else if(cyclics==2){
                                    edgesSorted.forEach(e -> {
                                        checkForEliminationFourSpacesCase(e.getValue());
                                    });
                                }else{
                                    try {
                                        throw new Exception("Number of cycles is not correct after the reverse of an edge.");
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }else {
                            try {
                                throw new Exception("Number of cyclics is not correct for case 3.");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    case 4:
                        if(cyclics==0 || cyclics == 2){
                            diagonal.forEach(e -> checkForEliminationFourSpacesCase(e.getValue()));
                            edgesSorted.forEach(e -> {
                                checkForEliminationFourSpacesCase(e.getValue());
                            });
                        }else {
                            try {
                                throw new Exception("Number of cyclics is not correct for case 4.");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                }
        }
    }

    private int getSubgraphCase(){
        if((getEdge(1).getDirectionCode() && getEdge(5).getDirectionCode()
            && !getEdge(6).getDirectionCode() && !getEdge(2).getDirectionCode())
            || (!getEdge(1).getDirectionCode() && !getEdge(5).getDirectionCode()
                && getEdge(6).getDirectionCode() && getEdge(2).getDirectionCode())){
            return 1;
        }else{

            int targetedCell = -1;

            if(getEdge(1).getDirectionCode() && !getEdge(5).getDirectionCode()){
                targetedCell = 2;
            }else if(getEdge(5).getDirectionCode() && getEdge(6).getDirectionCode()){
                targetedCell = 4;
            } else if (!getEdge(6).getDirectionCode() && getEdge(2).getDirectionCode()) {
                targetedCell = 3;
            } else if (!getEdge(2).getDirectionCode() && !getEdge(1).getDirectionCode()) {
                targetedCell = 1;
            }else{
                try {
                    throw new Exception("The targeted cell could not be determined.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int diagonalCell = Math.abs(targetedCell - 5);

            if(diagonalCell==1){
                if(!getEdge(2).getDirectionCode() && !getEdge(1).getDirectionCode()){
                    return 2;
                }else if (getEdge(2).getDirectionCode() && getEdge(1).getDirectionCode()) {
                    return 4;
                }else {
                    return 3;
                }
            }else if(diagonalCell==2){
                if(getEdge(1).getDirectionCode() && !getEdge(5).getDirectionCode()){
                    return 2;
                }else if (!getEdge(1).getDirectionCode() && getEdge(5).getDirectionCode()) {
                    return 4;
                }else {
                    return 3;
                }
            }else if(diagonalCell==3){
                if(!getEdge(6).getDirectionCode() && getEdge(2).getDirectionCode()){
                    return 2;
                }else if (getEdge(6).getDirectionCode() && !getEdge(2).getDirectionCode()) {
                    return 4;
                }else {
                    return 3;
                }
            }else if (diagonalCell==4) {
                if(getEdge(6).getDirectionCode() && getEdge(5).getDirectionCode()){
                    return 2;
                }else if (!getEdge(6).getDirectionCode() && !getEdge(5).getDirectionCode()) {
                    return 4;
                }else {
                    return 3;
                }
            }else{
                try {
                    throw new Exception("The diagonal cell could not be determined.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            throw new Exception("Problem for determining the selfSubgraph case.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void checkForEliminationFourSpacesCase(int edge){
        if(!getEdge(edge).isLocked()){
            boolean g1 = canBeEliminatedOption1(edge);
            boolean g2 = canBeEliminatedOption2(edge);

            if(g1 && g2){
                if(getCostsOption1(edge)<getCostsOption2(edge)){
                    eliminateOnOption2(edge);
                }else{
                    eliminateOnOption1(edge);
                }
            }else if(g1){
                eliminateOnOption1(edge);
            }else if(g2){
                eliminateOnOption2(edge);
            }
        }
    }

    private int getCostsOption1(int edge){
        switch(edge){
            case 1:
                return getEdge(2).getWeight()+getEdge(4).getWeight();
            case 2:
                return getEdge(1).getWeight()+getEdge(4).getWeight();
            case 3:
                return getEdge(1).getWeight()+getEdge(5).getWeight();
            case 4:
                return getEdge(1).getWeight()+getEdge(2).getWeight();
            case 5:
                return getEdge(1).getWeight()+getEdge(3).getWeight();
            case 6:
                return getEdge(4).getWeight()+getEdge(5).getWeight();
            default:
                try {
                    throw new Exception("The index for edge elimination is out of bounds.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return -1;
    }

    private void eliminateOnOption1(int edge){
        getEdge(edge).eliminate();
        switch(edge){
            case 1:
                getEdge(2).lock();
                getEdge(4).lock();
                break;
            case 2:
                getEdge(1).lock();
                getEdge(4).lock();
                break;
            case 3:
                getEdge(1).lock();
                getEdge(5).lock();
                break;
            case 4:
                getEdge(1).lock();
                getEdge(2).lock();
                break;
            case 5:
                getEdge(1).lock();
                getEdge(3).lock();
                break;
            case 6:
                getEdge(4).lock();
                getEdge(5).lock();
                break;
            default:
                try {
                    throw new Exception("The index for edge elimination is out of bounds.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private boolean canBeEliminatedOption1(int edge){
        switch (edge){
            case 1:
                if (!getEdge(2).isEliminated() && !getEdge(4).isEliminated()) {
                    if(getEdge(2).getDirectionCode() && getEdge(4).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 2:
                if (!getEdge(1).isEliminated() && !getEdge(4).isEliminated()) {
                    if(getEdge(1).getDirectionCode() && !getEdge(4).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 3:
                if (!getEdge(1).isEliminated() && !getEdge(5).isEliminated()) {
                    if(getEdge(1).getDirectionCode() && !getEdge(5).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 4:
                if (!getEdge(1).isEliminated() && !getEdge(2).isEliminated()) {
                    if(!getEdge(1).getDirectionCode() && !getEdge(2).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 5:
                if (!getEdge(1).isEliminated() && !getEdge(3).isEliminated()) {
                    if(!getEdge(1).getDirectionCode() && !getEdge(3).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 6:
                if (!getEdge(4).isEliminated() && !getEdge(5).isEliminated()) {
                    if(!getEdge(4).getDirectionCode() && !getEdge(5).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            default:
                try {
                    throw new Exception("The index for edge elimination check is out of bounds.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return false;
    }

    private int getCostsOption2(int edge) {
        switch(edge){
            case 1:
                return getEdge(3).getWeight()+getEdge(5).getWeight();
            case 2:
                return getEdge(3).getWeight()+getEdge(6).getWeight();
            case 3:
                return getEdge(2).getWeight()+getEdge(6).getWeight();
            case 4:
                return getEdge(5).getWeight()+getEdge(6).getWeight();
            case 5:
                return getEdge(4).getWeight()+getEdge(6).getWeight();
            case 6:
                return getEdge(2).getWeight()+getEdge(3).getWeight();
            default:
                try {
                    throw new Exception("The index for edge elimination is out of bounds.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return -1;
    }

        private void eliminateOnOption2(int edge){
        getEdge(edge).eliminate();
        switch(edge){
            case 1:
                getEdge(3).lock();
                getEdge(5).lock();
                break;
            case 2:
                getEdge(3).lock();
                getEdge(6).lock();
                break;
            case 3:
                getEdge(2).lock();
                getEdge(6).lock();
                break;
            case 4:
                getEdge(5).lock();
                getEdge(6).lock();
                break;
            case 5:
                getEdge(4).lock();
                getEdge(6).lock();
                break;
            case 6:
                getEdge(2).lock();
                getEdge(3).lock();
                break;
            default:
                try {
                    throw new Exception("The index for edge elimination is out of bounds.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private boolean canBeEliminatedOption2(int edge){
        switch (edge){
            case 1:
                if (!getEdge(3).isEliminated() && !getEdge(5).isEliminated()) {
                    if(getEdge(3).getDirectionCode() && getEdge(5).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 2:
                if (!getEdge(3).isEliminated() && !getEdge(6).isEliminated()) {
                    if(getEdge(3).getDirectionCode() && getEdge(6).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 3:
                if (!getEdge(2).isEliminated() && !getEdge(6).isEliminated()) {
                    if(getEdge(2).getDirectionCode() && !getEdge(6).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 4:
                if (!getEdge(5).isEliminated() && !getEdge(6).isEliminated()) {
                    if(getEdge(5).getDirectionCode() && getEdge(6).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 5:
                if (!getEdge(4).isEliminated() && !getEdge(6).isEliminated()) {
                    if(getEdge(4).getDirectionCode() && !getEdge(6).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            case 6:
                if (!getEdge(2).isEliminated() && !getEdge(3).isEliminated()) {
                    if(!getEdge(2).getDirectionCode() && !getEdge(3).getDirectionCode()) {
                        return true;
                    }
                }
                break;
            default:
                try {
                    throw new Exception("The index for edge elimination check is out of bounds.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        return false;
    }

    public int getNumberOfCyclics(){
        int numberOfCyclics = 0;

        if((getEdge(1).getDirectionCode() && !getEdge(2).getDirectionCode() && getEdge(4).getDirectionCode()) ||
                (!getEdge(1).getDirectionCode() && getEdge(2).getDirectionCode() && !getEdge(4).getDirectionCode())) {
            numberOfCyclics++;
        }

        if((getEdge(4).getDirectionCode() && !getEdge(5).getDirectionCode() && getEdge(6).getDirectionCode()) ||
                (!getEdge(4).getDirectionCode() && getEdge(5).getDirectionCode() && !getEdge(6).getDirectionCode())) {
            numberOfCyclics++;
        }

        if((getEdge(1).getDirectionCode() && !getEdge(3).getDirectionCode() && getEdge(5).getDirectionCode()) ||
                (!getEdge(1).getDirectionCode() && getEdge(3).getDirectionCode() && !getEdge(5).getDirectionCode())) {
            numberOfCyclics++;
        }

        if((getEdge(2).getDirectionCode() && !getEdge(3).getDirectionCode() && getEdge(6).getDirectionCode()) ||
                (!getEdge(2).getDirectionCode() && getEdge(3).getDirectionCode() && !getEdge(6).getDirectionCode())) {
            numberOfCyclics++;
        }

        return numberOfCyclics;
    }


//    public List<T> getPartitions(T tailSpace, T headSpace, TypeSet typeSet){
//
//        //System.out.println("SIZE"+spaces.size());
//        List<T> partitions = new ArrayList<>();
//        int tailSpaceIndex = spaces.indexOf(tailSpace);
//        partitions.add(spaces.get(tailSpaceIndex));
//
//        if(spaces.length==2){
//            if(edges[tailSpaceIndex].getHead().equals(headSpace)){
//                if(edges[tailSpaceIndex].getTypeSet().equals(typeSet)){
//                    partitions.add(headSpace);
//                }
//            }else{
//                try {
//                    throw new Exception("For the case of 2 spaces, there was a problem for determining the agreement among a tail and a head space.");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }else if(spaces.length==3){
//            Optional<T> o;
//            if(tailSpaceIndex == 0){
//                o = threeSpacesCasePartition(headSpace, 1, 3, typeSet);
//                o.ifPresent(partitions::add);
//            }else if(tailSpaceIndex == 1){
//                o = threeSpacesCasePartition(headSpace, 2, 5, typeSet);
//                o.ifPresent(partitions::add);
//            }else if(tailSpaceIndex == 2){
//                o = threeSpacesCasePartition(headSpace, 4, 6, typeSet);
//                o.ifPresent(partitions::add);
//            }else{
//                try {
//                    throw new Exception("Unexpected tail index for determining the agreement");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        } else if(spaces.length==4){
//            Optional<T> o;
//            if(tailSpaceIndex == 0){
//                o = fourSpacesCasePartition(headSpace,1,3,5, typeSet);
//                o.ifPresent(partitions::add);
//            }else if(tailSpaceIndex == 1){
//                o = fourSpacesCasePartition(headSpace,2,7,9, typeSet);
//                o.ifPresent(partitions::add);
//            }else if(tailSpaceIndex == 2){
//                o = fourSpacesCasePartition(headSpace,4,8 ,11, typeSet);
//                o.ifPresent(partitions::add);
//            }else if(tailSpaceIndex == 3){
//                o = fourSpacesCasePartition(headSpace,6, 10, 12, typeSet);
//                o.ifPresent(partitions::add);
//            }else{
//                try {
//                    throw new Exception("Unexpected tail index for determining the agreement");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }
//        else{
//            try {
//                throw new Exception("Unexpected number of spaces for determining an agreement among a tail and a head space.");
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//        return partitions;
//    }

    public Edge getEdge(int index){
        return edges[index-1];
    }

//    public void getPartitionsNEW(int tailSpaceIndexLocal, int headSpaceIndexLocal, TypeSet typeSet, List<Cell> list) {
//        if (spaces.length == 4) {
//            if (tailSpaceIndexLocal == 0) {
//                switch (headSpaceIndexLocal) {
//                    case 1:
//                        if(getEdge(1).getTypeSet().equals(typeSet)){
////                            list.add((Cell) spaces[1]);
//                        }
//                        break;
//                    case 2:
//                        if(getEdge(3).getTypeSet().equals(typeSet)){
////                            list.add((Cell) spaces[2]);
//                        }
//                        break;
//                    case 3:
//                        if(getEdge(5).getTypeSet().equals(typeSet)){
////                            list.add((Cell) spaces[3]);
//                        }
//                        break;
//                    default:
//                        try {
//                            throw new Exception("The case should be 1, 2 or 3.");
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }                    }
//                } else if (tailSpaceIndexLocal == 3) {
//                    switch (headSpaceIndexLocal) {
//                        case 0:
//                            if(getEdge(6).getTypeSet().equals(typeSet)){
////                                list.add((Cell) spaces[0]);
//                            }
//                            break;
//                        case 1:
//                            if(getEdge(10).getTypeSet().equals(typeSet)){
////                                list.add((Cell) spaces[1]);
//                            }
//                            break;
//                        case 2:
//                            if(getEdge(12).getTypeSet().equals(typeSet)){
////                                list.add((Cell) spaces[2]);
//                            }
//                            break;
//                        default:
//                            try {
//                                throw new Exception("The case should be 0, 1 or 2.");
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                    }
//                } else {
//                    try {
//                        throw new Exception("Should be either 0 or 3 always.");
//                    } catch (Exception exception) {
//                        exception.printStackTrace();
//                    }
//                }
//            } else if (spaces.length == 2) {
//                if (edges[0].getTypeSet().equals(typeSet)) {
//                    if (tailSpaceIndexLocal == 0) {
////                        list.add((Cell) spaces[1]);
//                    }else{
////                        list.add((Cell) spaces[0]);
//                    }
//                }
//            } else {
//                try {
//                    throw new Exception("Unexpected number of spaces for determining an agreement among a tail and a head space.");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//    }

//    private Optional<T> fourSpacesCasePartition(T headSpace, int edge1, int edge2, int edge3, TypeSet typeSet){
//
//        Optional<T> o = null;
//        if(edges[edge1-1].getHead().equals(headSpace)){
//            if(edges[edge1-1].getTypeSet().equals(typeSet)){
//                o = Optional.of(edges[edge1-1].getHead());
//            }
//            else{
//                o = Optional.empty();
//            }
//        }
//        else if(edges[edge2-1].getHead().equals(headSpace)){
//            if(edges[edge2-1].getTypeSet().equals(typeSet)){
//                o = Optional.of(edges[edge2-1].getHead());
//            }
//            else{
//                o = Optional.empty();
//            }
//        }
//        else if(edges[edge3-1].getHead().equals(headSpace)){
//            if(edges[edge3-1].getTypeSet().equals(typeSet)){
//                o = Optional.of(edges[edge3-1].getHead());
//            }
//            else{
//                o = Optional.empty();
//            }
//        }
//        else {
//            try {
//                throw new Exception("For the case of 4 spaces, there was a problem for determining the agreement for a head space.");
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//        return o;
//    }

//    private Optional<T> threeSpacesCasePartition(T headSpace, int edge1, int edge2, TypeSet typeSet){
//
//        Optional<T> o = null;
//        if(edges[edge1-1].getHead().equals(headSpace)){
//            if(edges[edge1-1].getTypeSet().equals(typeSet)){
//                o = Optional.of(edges[edge1-1].getHead());
//            }
//            else{
//                o = Optional.empty();
//            }
//        }
//        else if(edges[edge2-1].getHead().equals(headSpace)){
//            if(edges[edge2-1].getTypeSet().equals(typeSet)){
//                o = Optional.of(edges[edge2-1].getHead());
//            }
//            else{
//                o = Optional.empty();
//            }
//        } else {
//            try {
//                throw new Exception("For the case of 3 spaces, there was a problem for determining the agreement for a head space.");
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//        return o;
//    }

    //get the hashcodes for passing a point on a given space
//    public List<T> getPartitions(T space, TypeSet typeSet, boolean outerQuarter){
//        int spaceIndex = spaces.indexOf(space);
//
//        List<T> partitions = null;
//
//        if(spaces.size()==1){
//            partitions = new ArrayList<>();
//            partitions.add(spaces.get(spaceIndex));
//        }else if(spaces.size()==2){
//            partitions = new ArrayList<>();
//            partitions.add(spaces.get(spaceIndex));
//            if(edges[spaceIndex].getTypeSet().equals(typeSet)){
//                partitions.add(edges[spaceIndex].getHead());
//            }
//        }else if(spaces.size()==4){
//            if(spaceIndex == 0){
//                partitions = fourSpacesCasePartitions(0,1,3,5, typeSet, outerQuarter, 5);
//            }else if(spaceIndex == 1){
//                partitions = fourSpacesCasePartitions(1,2,7,9, typeSet, outerQuarter, 7);
//            }else if(spaceIndex == 2){
//                partitions = fourSpacesCasePartitions(2,4,8 ,11, typeSet, outerQuarter, 8);
//            }else if(spaceIndex == 3){
//                partitions = fourSpacesCasePartitions(3,6, 10, 12, typeSet, outerQuarter, 6);
//            }else{
//                try {
//                    throw new Exception("Unexpected space index for determining its agreements");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }else{
//            try {
//                throw new Exception("Unexpected number of spaces for determining the agreements of a space.");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return partitions;
//    }

//    public List<T> getPartitionsFourSpacesCase(T space, TypeSet typeSet, boolean outerQuarter){
//        int spaceIndex = spaces.indexOf(space);
//
//        List<T> partitions = null;
//
//        if(spaces.length==4){
//            if(spaceIndex == 0){
//                partitions = fourSpacesCasePartitions(0,1,3,5, typeSet, outerQuarter, 5);
//            }else if(spaceIndex == 1){
//                partitions = fourSpacesCasePartitions(1,2,7,9, typeSet, outerQuarter, 7);
//            }else if(spaceIndex == 2){
//                partitions = fourSpacesCasePartitions(2,4,8 ,11, typeSet, outerQuarter, 8);
//            }else if(spaceIndex == 3){
//                partitions = fourSpacesCasePartitions(3,6, 10, 12, typeSet, outerQuarter, 6);
//            }else{
//                try {
//                    throw new Exception("Unexpected space index for determining its agreements");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }else{
//            try {
//                throw new Exception("Unexpected number of spaces for determining the agreements of a space.");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return partitions;
//    }

//    public void getPartitionsFourSpacesCase(int spaceIndex, TypeSet typeSet, boolean outerQuarter, List<Cell> partitions){
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

        private void fourSpacesCasePartitions(int edge1, boolean edge1Direction, int edge2, boolean edge2Direction, int edge3, boolean edge3Direction, boolean outerQuarter, List<Integer> partitions){
        //the third edge is always the diagonal
        if(getEdge(edge1).getDirectionCode()==edge1Direction && (!getEdge(edge1).isEliminated() /*|| outerQuarter*/)){
            partitions.add(getEdgeHeadInFourSpaces(edge1)/*edges[edge1-1].getHead()*/);
        }
        if(getEdge(edge2).getDirectionCode()==edge2Direction && (!getEdge(edge2).isEliminated() /*|| outerQuarter*/)){
            partitions.add(getEdgeHeadInFourSpaces(edge2)/*edges[edge2-1].getHead()*/);
        }
        if(getEdge(edge3).getDirectionCode()==edge3Direction){
            if(!outerQuarter){
                if(!getEdge(edge3).isEliminated()){
                    partitions.add(getEdgeHeadInFourSpaces(edge3)/*edges[edge3-1].getHead()*/);
                }
            }else{
                if(getEdge(edge3).isLocked()){
                    partitions.add(getEdgeHeadInFourSpaces(edge3)/*edges[edge3-1].getHead()*/);
                }
            }
        }
    }

    public void getPartitionsFourSpacesCase(int spaceIndex, boolean outerQuarter, List<Integer> partitions){
        switch(spaceIndex){
            case 0:
                fourSpacesCasePartitions(1, true, 2,true,3, true, outerQuarter, partitions);
                break;
            case 1:
                fourSpacesCasePartitions(1, false, 5,true,4, true, outerQuarter, partitions);
                break;
            case 2:
                fourSpacesCasePartitions(2, false, 6,true,4, false, outerQuarter, partitions);
                break;
            case 3:
                fourSpacesCasePartitions(5, false, 6,false,3, false, outerQuarter, partitions);
                break;
            default:
                try {
                    throw new Exception("Unexpected space index for determining its agreements");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
    }


//    public List<T> getPartitionsThreeSpacesCase(T space, TypeSet typeSet){
//        int spaceIndex = spaces.indexOf(space);
//
//        List<T> partitions = null;
//
//        if(spaces.size()==3){
//            if(spaceIndex == 0){
//                partitions = threeSpacesCasePartitions(0,1,3, typeSet);
//            }else if(spaceIndex == 1){
//                partitions = threeSpacesCasePartitions(1,2,5, typeSet);
//            }else if(spaceIndex == 2){
//                partitions = threeSpacesCasePartitions(2,4,6 , typeSet);
//            }else{
//                try {
//                    throw new Exception("Unexpected space index for determining its agreements");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }else{
//            try {
//                throw new Exception("Unexpected number of spaces for determining the agreements of a space.");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return partitions;
//    }

//    public List<T> getPartitionsTwoSpacesCase(T tailSpace, T headSpace, TypeSet typeSet){
//        int tailSpaceIndex = spaces.indexOf(tailSpace);
//
//        List<T> partitions = new ArrayList<>();
//        partitions.add(spaces[tailSpaceIndex]);
//
//        if(spaces.length==2){
//            if(edges[tailSpaceIndex].getHead().equals(headSpace)){
//                if(edges[tailSpaceIndex].getTypeSet().equals(typeSet)){
//                    partitions.add(headSpace);
//                }
//            }else{
//                try {
//                    throw new Exception("For the case of 2 spaces, there was a problem for determining the agreement among a tail and a head space.");
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//            }
//        }
//        else{
//            try {
//                throw new Exception("Unexpected number of spaces for determining an agreement among a tail and a head space.");
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//        return partitions;
//    }

//    public void getPartitionsTwoSpacesCase(Cell headSpace, TypeSet typeSet, List<Cell> partitions){
//                if(edges[0].getTypeSet().equals(typeSet)){
//                    partitions.add(headSpace);
//                }
//    }

    public void getPartitionsTwoSpacesCase(int cellIdHeadSpace, List<Integer> partitions){
        //will not be ever diagonal the two cells
        if(cellIdHeadSpace==cellsIdReference[1]){
            if(getEdge(1).getDirectionCode()){
                partitions.add(cellIdHeadSpace);
            }
        }else if(cellIdHeadSpace==cellsIdReference[0]){
            if(!getEdge(1).getDirectionCode()){
                partitions.add(cellIdHeadSpace);
            }
        }else{
            try {
                throw new Exception("The case for the two spaces should have been caught.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Agreements newAgreements(int numOfSpaces, int[] cellsIdReference, List<Cell> cells, Point point, /*ReplicationType function*/Function4<Cell, Cell,Point, Edge> function){
        return new Agreements(numOfSpaces, cellsIdReference, cells, point, function);
    }


    public int getEdgeHeadInFourSpaces(int edgeIndex){
        switch (edgeIndex){
            case 1:
                if(getEdge(edgeIndex).getDirectionCode()){
                    return cellsIdReference[0]+1;
                }else{
                    return cellsIdReference[0];
                }
            case 2:
                if(getEdge(edgeIndex).getDirectionCode()){
                    return cellsIdReference[1];
                }else{
                    return cellsIdReference[0];
                }
            case 3:
                if(getEdge(edgeIndex).getDirectionCode()){
                    return cellsIdReference[1]+1;
                }else{
                    return cellsIdReference[0];
                }
            case 4:
                if(getEdge(edgeIndex).getDirectionCode()){
                    return cellsIdReference[1];
                }else{
                    return cellsIdReference[0]+1;
                }
            case 5:
                if(getEdge(edgeIndex).getDirectionCode()){
                    return cellsIdReference[1]+1;
                }else{
                    return cellsIdReference[0]+1;
                }
            case 6:
                if(getEdge(edgeIndex).getDirectionCode()){
                    return cellsIdReference[1]+1;
                }else{
                    return cellsIdReference[1];
                }
            default:
                try {
                    throw new Exception("Wrong number of index for the case of the four spaces .");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        try {
            throw new Exception("Should have returned the edge head.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }


//    private List<T> threeSpacesCasePartitions(int spaceIndex, int edge1, int edge2, TypeSet typeSet){
//        List<T> partitions = new ArrayList<>();
//        partitions.add(spaces.get(spaceIndex));
//        if(edges[edge1-1].getTypeSet().equals(typeSet) && (!edges[edge1-1].isEliminated())){
//            partitions.add(edges[edge1-1].getHead());
//        }
//        if(edges[edge2-1].getTypeSet().equals(typeSet) && (!edges[edge2-1].isEliminated())){
//            partitions.add(edges[edge2-1].getHead());
//        }
//        return partitions;
//    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(cellsIdReference[0]).append("\n");
        sb.append(cellsIdReference[0]+1).append("\n");
        sb.append(cellsIdReference[1]).append("\n");
        sb.append(cellsIdReference[1]+1).append("\n");
        for (Edge edge : edges) {
            sb.append(edge).append("\n");
        }
        return sb.toString();
    }
//    public String toString(Function<T, Long> spaceToId){
//        StringBuilder sb = new StringBuilder();
//        sb.append("Spaces: ");
//        spaces.forEach(c-> sb.append(spaceToId.apply(c)).append(", "));
//        sb.deleteCharAt(sb.lastIndexOf(","));
//        sb.append("\n");
//
//        for (Edge<T> edge : edges) {
//            sb.append(spaceToId.apply(edge.getTail())).append(" -> ").append(spaceToId.apply(edge.getHead())).append(" TypeSet: ").append(edge.getTypeSet()).append(" Weight: ").append(edge.getWeight());
//            if(edge.isEliminated()){
//                sb.append(" (Eliminated)");
//            }
//
//            if(edge.isLocked()){
//                sb.append(" (Locked)");
//            }
//            sb.append("\n");
//        }
//        return sb.toString();
//    }

//    public Optional<T> getExtraPartition(T space1, T space2, TypeSet typeSet){
//
//        Edge<T> edge = getEdge(space2, space1);
//
//        if(edge.getTypeSet() != typeSet && edge.isEliminated()) {
//            List<T> spaces = new ArrayList<>(this.spaces);
//            spaces.remove(space1);
//            spaces.remove(space2);
//            Edge<T> edge1 = getEdge(space1,spaces.get(0));
//            Edge<T> edge2 = getEdge(space1,spaces.get(1));
//
////            if(edge1.isLocked() && edge1.getTypeSet().equals(typeSet) && edge2.isEliminated() ){
////                return Optional.of(edge1.getHead());
////            }else if(edge2.isLocked() && edge2.getTypeSet().equals(typeSet) && edge1.isEliminated()){
////                return Optional.of(edge2.getHead());
////            }else if(edge1.isLocked() && !edge2.isLocked()){
////                return Optional.of(edge1.getHead());
////            }else if(edge2.isLocked() && !edge1.isLocked()){
////                return Optional.of(edge2.getHead());
////            }
//
//            if(edge1.isLocked() && edge1.getTypeSet().equals(typeSet) && !(edge2.isLocked() && edge2.getTypeSet().equals(typeSet))){
//                return Optional.of(edge1.getHead());
//            }else if(edge2.isLocked() && edge2.getTypeSet().equals(typeSet) && !(edge1.isLocked() && edge1.getTypeSet().equals(typeSet))){
//                return Optional.of(edge2.getHead());
//            }
//
//            try {
//                throw new Exception("No locked space found");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        return Optional.empty();
//    }

    public int getExtraPartition(int edgeIndexMarked, boolean edgeMarkedDirection, int edgeIndexLocked1, boolean edgeLocked1Direction, int edgeIndexLocked2,  boolean edgeLocked2Direction){

        Edge edge = getEdge(edgeIndexMarked);

        if(edge.getDirectionCode()==edgeMarkedDirection && edge.isEliminated()) {
            Edge edge1 = getEdge(edgeIndexLocked1);
            Edge edge2 = getEdge(edgeIndexLocked2);

            if(edge1.isLocked() && edge1.getDirectionCode()==edgeLocked1Direction /*&& !(edge2.isLocked() && edge2.getTypeSet().equals(typeSet))*/){//the second condition is entailed to be true if the first is true
//                return edge1.getHead();
                return getEdgeHeadInFourSpaces(edgeIndexLocked1);
            }else if (edge2.isLocked() && edge2.getDirectionCode()==edgeLocked2Direction){//the second condition is entailed to be true if the first is true
//                return edge2.getHead();
                return getEdgeHeadInFourSpaces(edgeIndexLocked2);
            }else{
                try {
                    throw new Exception("The case for the extra partition should have been caught.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            return null;
        }
//        return Optional.empty();
       return -1;
    }

        public Edge getEdge(int tailCellId, int headCellId){

            int tailCellIdIndex = -1;
            int headCellIdIndex = -1;

            if(edges.length==6){
                if(tailCellId==cellsIdReference[0]){
                    tailCellIdIndex =0;
                }else if(tailCellId==cellsIdReference[0]+1){
                    tailCellIdIndex =1;
                }else if(tailCellId==cellsIdReference[1]){
                    tailCellIdIndex =2;
                }else if(tailCellId==cellsIdReference[1]+1){
                    tailCellIdIndex =3;

                }else{
                    try {
                        throw new Exception("Tail cell id cannot be determined");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(headCellId==cellsIdReference[0]){
                    headCellIdIndex =0;
                }else if(headCellId==cellsIdReference[0]+1){
                    headCellIdIndex =1;
                }else if(headCellId==cellsIdReference[1]){
                    headCellIdIndex =2;
                }else if(headCellId==cellsIdReference[1]+1){
                    headCellIdIndex =3;

                }else{
                    try {
                        throw new Exception("Head cell id cannot be determined");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                switch(tailCellIdIndex){
                    case 0:
                        switch(headCellIdIndex){
                            case 1:
                                return getEdge(1);
                            case 2:
                                return getEdge(2);
                            case 3:
                                return getEdge(3);
                            default:
                                try {
                                    throw new Exception("Wrong case for head cell index");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    case 1:
                        switch(headCellIdIndex){
                            case 0:
                                return getEdge(1);
                            case 2:
                                return getEdge(4);
                            case 3:
                                return getEdge(5);
                            default:
                                try {
                                    throw new Exception("Wrong case for head cell index");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    case 2:
                        switch(headCellIdIndex){
                            case 0:
                                return getEdge(2);
                            case 1:
                                return getEdge(4);
                            case 3:
                                return getEdge(6);
                            default:
                                try {
                                    throw new Exception("Wrong case for head cell index");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    case 3:
                        switch(headCellIdIndex){
                            case 0:
                                return getEdge(3);
                            case 1:
                                return getEdge(5);
                            case 2:
                                return getEdge(6);
                            default:
                                try {
                                    throw new Exception("Wrong case for head cell index");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    default:
                        try {
                            throw new Exception("Wrong case for tail cell index");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }

            }else if(edges.length==1){
                return getEdge(1);
//                if(tailCellId==cellsIdReference[0]){
//                    tailCellIdIndex =0;
//                }else if(tailCellId==cellsIdReference[1]){
//                    tailCellIdIndex =1;
//                }else{
//                    try {
//                        throw new Exception("Tail cell id cannot be determined for 2 spaces.");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if(headCellId==cellsIdReference[0]){
//                    headCellIdIndex =0;
//                }else if(headCellId==cellsIdReference[1]){
//                    headCellIdIndex =1;
//                }else{
//                    try {
//                        throw new Exception("Head cell id cannot be determined for 2 spaces.");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if(tailCellIdIndex==0 && headCellIdIndex==1){
//                    return getEdge(1);
//                }else if(tailCellIdIndex==1 && headCellIdIndex==0){
//                    return getEdge(2);
//                }else{
//                    try {
//                        throw new Exception("Wrong case for head cell index.");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

            }else{
                try {
                    throw new Exception("No edge found for the given spaces.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        return null;
    }

//    public Edge getEdge(int tailCellId, int headCellId){
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

    public void getPartitionsNEW(int tailSpaceIndexLocal, int headSpaceIndexLocal, boolean directionCode, List<Integer> list) {
        if (edges.length== 6) {
            if (tailSpaceIndexLocal == 0) {
                switch (headSpaceIndexLocal) {
                    case 1:
                        if(getEdge(1).getDirectionCode()==directionCode){
//                            list.add(String.valueOf(spaces[1]));
                            list.add(cellsIdReference[0]+1);
                        }
                        break;
                    case 2:
                        if(getEdge(2).getDirectionCode()==directionCode){
//                            list.add(String.valueOf(spaces[2]));
                            list.add(cellsIdReference[1]);

                        }
                        break;
                    case 3:
                        if(getEdge(3).getDirectionCode()==directionCode){
//                            list.add(String.valueOf(spaces[3]));
                            list.add(cellsIdReference[1]+1);

                        }
                        break;
                    default:
                        try {
                            throw new Exception("The case should be 1, 2 or 3.");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }                    }
            } else if (tailSpaceIndexLocal == 3) {
                switch (headSpaceIndexLocal) {
                    case 0:
                        if(getEdge(3).getDirectionCode()==directionCode){
//                            list.add(String.valueOf(spaces[0]));
                            list.add(cellsIdReference[0]);

                        }
                        break;
                    case 1:
                        if(getEdge(5).getDirectionCode()==directionCode){
//                            list.add(String.valueOf(spaces[1]));
                            list.add(cellsIdReference[0]+1);

                        }
                        break;
                    case 2:
                        if(getEdge(6).getDirectionCode()==directionCode){
//                            list.add(String.valueOf(spaces[2]));
                            list.add(cellsIdReference[1]);

                        }
                        break;
                    default:
                        try {
                            throw new Exception("The case should be 0, 1 or 2.");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                }
            } else {
                try {
                    throw new Exception("Should be either 0 or 3 always.");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } else if (edges.length == 1) {
            if (getEdge(1).getDirectionCode()==directionCode) {
                if (tailSpaceIndexLocal == 0) {
                    list.add(cellsIdReference[1]);
                }else{
                    list.add(cellsIdReference[0]);
                }
            }
        } else {
            try {
                throw new Exception("Unexpected number of spaces for determining an agreement among a tail and a head space.");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agreements that = (Agreements) o;
        return Objects.deepEquals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(edges);
    }
}
