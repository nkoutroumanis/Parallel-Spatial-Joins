package gr.ds.unipi.rdd;

import org.apache.spark.Partitioner;

import java.util.*;
import java.util.stream.Collectors;

public class LPTPartitioner extends Partitioner {

    private final int numParts;
    private final TreeMap<Integer, Integer> cellsPartitonsMap; //map cells to spark partitions
    public LPTPartitioner(Map<Long,Long> hashMap, int numParts){
        cellsPartitonsMap = new TreeMap<>();
        ltp(hashMap, numParts);
        this.numParts=numParts;
    }

    private void ltp(Map<Long,Long> hashMap, int numParts){
        long[] partitionsCosts = new long[numParts];

        List<Map.Entry<Long,Long>> sortedEntries = hashMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())).collect(Collectors.toList());
                //.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        //(e1, e2) -> e1, LinkedHashMap::new));

        for (int i = 0; i < sortedEntries.size(); i++) {
            if (i <= this.numParts-1) {
                partitionsCosts[i] = sortedEntries.get(i).getValue();
                cellsPartitonsMap.put(sortedEntries.get(i).getKey().intValue(),i);
            }else{
                int index = getIndexOfMin(partitionsCosts);
                partitionsCosts[index] = partitionsCosts[index] + sortedEntries.get(i).getValue();
                cellsPartitonsMap.put(sortedEntries.get(i).getKey().intValue(),index);
            }
        }
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

    @Override
    public int numPartitions() {
        return numParts;
    }

    @Override
    public int getPartition(Object key) {
        if(cellsPartitonsMap.containsKey((int)key)){
            return cellsPartitonsMap.get((int)key);
        }
        return ((int)key)%numParts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LPTPartitioner that = (LPTPartitioner) o;

        return numParts == that.numParts;
    }
}
