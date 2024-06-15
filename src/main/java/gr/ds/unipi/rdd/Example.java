package gr.ds.unipi.rdd;

import java.util.*;
import java.util.stream.Collectors;

public class Example {

    private final TreeMap<Integer, Integer> cellsPartitonsMap = new TreeMap<>();



    public static void main(String args[]){

        Example ex = new Example();
        HashMap<Integer, Long> cellsCosts = new HashMap<>();
        cellsCosts.put(4, 323l);
        cellsCosts.put(1,43l);
        cellsCosts.put(100, 4l);
        cellsCosts.put(0, 500l);
        cellsCosts.put(101, 7l);
        cellsCosts.put(40, 600l);
        cellsCosts.put(20, 300l);
        ex.ltp(cellsCosts, 4);
    }

    private void ltp(Map<Integer,Long> hashMap, int numParts){
        long[] partitionsCosts = new long[numParts];

        List<Map.Entry<Integer,Long>> sortedEntries = hashMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())).collect(Collectors.toList());
        //.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
        //(e1, e2) -> e1, LinkedHashMap::new));

        sortedEntries.forEach(System.out::println);

        for (int i = 0; i < sortedEntries.size(); i++) {
            if (i <= numParts-1) {
                partitionsCosts[i] = sortedEntries.get(i).getValue();
                cellsPartitonsMap.put((int)sortedEntries.get(i).getKey().longValue(),i);
            }else{
                int index = getIndexOfMin(partitionsCosts);
                partitionsCosts[index] = partitionsCosts[index] + sortedEntries.get(i).getValue();
                cellsPartitonsMap.put((int)sortedEntries.get(i).getKey().longValue(),index);
            }
        }
        for (int i = 0; i < partitionsCosts.length; i++) {
            System.out.println("Index: "+i+ " - "+partitionsCosts[i]);
        }

        cellsPartitonsMap.forEach((k,v)->{
            System.out.println("Key: "+ k +" Value: "+v);
        });

    }

    private int getIndexOfMin(long[] partitionsCosts){
        int index = -1;
        long value = Long.MAX_VALUE;
        for (int i = 0; i < partitionsCosts.length; i++) {
            if(partitionsCosts[i] < value ){
                value = partitionsCosts[i];
                index = i;
            }
        }
        return index;
    }
}
