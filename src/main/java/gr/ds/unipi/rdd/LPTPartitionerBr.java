package gr.ds.unipi.rdd;

import org.apache.spark.Partitioner;
import org.apache.spark.broadcast.Broadcast;

import java.util.*;
import java.util.stream.Collectors;

public class LPTPartitionerBr extends Partitioner {

    private final int numParts;
    private final Broadcast<Map<Integer, Integer>> cellsPartitonsMap; //map cells to spark partitions
    public LPTPartitionerBr(Broadcast<Map<Integer, Integer>> hashMap, int numParts){
        cellsPartitonsMap = hashMap;
        this.numParts=numParts;
    }

    public static Map<Integer, Integer> ltp(Map<Integer,Integer> hashMap, int numParts){
        Map<Integer, Integer> cellsPartitonsMap = new HashMap<>();
        int[] partitionsCosts = new int[numParts];

        List<Map.Entry<Integer,Integer>> sortedEntries = hashMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())).collect(Collectors.toList());
        System.out.println(sortedEntries.size());
        System.out.println(sortedEntries.get(sortedEntries.size()-1));
        System.out.println(sortedEntries.get(0));

        for (int i = 0; i < sortedEntries.size(); i++) {
                int index = getIndexOfMin(partitionsCosts);
                partitionsCosts[index] = partitionsCosts[index] + sortedEntries.get(i).getValue();
                cellsPartitonsMap.put(sortedEntries.get(i).getKey(),index);
        }
//        System.out.println(Arrays.toString(partitionsCosts));
        return cellsPartitonsMap;
    }

    static int getIndexOfMin(int[] partitionsCosts){
        int index = -1;
        int value = Integer.MAX_VALUE;
        for (int i = 0; i < partitionsCosts.length; i++) {
           if(partitionsCosts[i] < value ){
               value = partitionsCosts[i];
               index = i;
           }
        }
        return index;
    }

    @Override
    public int numPartitions() {
        return numParts;
    }

    @Override
    public int getPartition(Object key) {
        if(cellsPartitonsMap.getValue().containsKey((int)key)){
            return cellsPartitonsMap.getValue().get((int)key);
        }
        return ((int)key)%numParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LPTPartitionerBr that = (LPTPartitionerBr) o;

        return numParts == that.numParts;
    }
}
