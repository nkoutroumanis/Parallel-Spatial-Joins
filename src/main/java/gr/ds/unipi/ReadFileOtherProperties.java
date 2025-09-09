package gr.ds.unipi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFileOtherProperties {
    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new FileReader("./"+args[0]));
        BufferedReader br = new BufferedReader(new FileReader("/Users/nicholaskoutroumanis/Desktop/times/ROADS-LINEARWATER-goa-BF"));

        String line;
        int k = 1;
        long property = 0;

        while((line = br.readLine())!=null){

            if(line.contains("\"JVM GC Time\":")){
                int index = line.indexOf("\"JVM GC Time\":");
                String line1 = line.substring(index);
                property = property + Long.parseLong(line1.substring(0,line1.indexOf(",")).substring(line1.indexOf(":")+1));
            }

        }
        System.out.println(property/1000);
        System.out.println();
    }
}
