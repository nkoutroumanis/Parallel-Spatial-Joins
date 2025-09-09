package gr.ds.unipi;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadSparkLogs {
    @Test
    public void testRead() throws IOException {


        System.out.println(SparkLogParser.getPropertyMax("/Users/nicholaskoutroumanis/Desktop/application_1745265013717_0105","\"Peak Execution Memory\""));
    }
}
