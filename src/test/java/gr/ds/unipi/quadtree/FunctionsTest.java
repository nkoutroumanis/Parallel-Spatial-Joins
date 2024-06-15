package gr.ds.unipi.quadtree;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class FunctionsTest {
    @Test
    public void streamTest1() {
        Stream<Double> values = Stream.of(3d,1d,4d,1d).sorted(Comparator.comparingDouble(d -> d)).distinct();
        //values.forEach(System.out::println);
        System.out.println(Arrays.toString(values.collect(Collectors.toList()).toArray()));
    }

    @Test
    public void streamTest2() {
        Stream.of(1,4,5,23,4,6).filter(i->(i==5)).forEach(System.out::println);
    }

    @Test
    public void setTest() {
        Set<Long> numbers1 = new HashSet<>();
        numbers1.add(1L);
        numbers1.add(4L);
        numbers1.add(3L);
        numbers1.add(2L);

        Set<Long> numbers2 = new HashSet<>();
        numbers2.add(5L);
        numbers2.add(6L);
        numbers2.add(4L);
        numbers2.add(7L);

        numbers1.addAll(numbers2);
        numbers2.forEach(System.out::println);

    }
}