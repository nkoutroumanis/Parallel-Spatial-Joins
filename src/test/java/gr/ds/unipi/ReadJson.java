package gr.ds.unipi;

import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

public class ReadJson {
    @Test
    public void testRead() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("/Users/nicholaskoutroumanis/Desktop/application_1651428970594_2543"));
        String line;
        long number = 0;
        while((line = br.readLine())!=null){
            if(line.contains("\"Remote Bytes Read\":")){
                int index = line.indexOf("\"Remote Bytes Read\":");
                line = line.substring(index);
                number = number + Long.parseLong(line.substring(0,line.indexOf(",")).substring(line.indexOf(":")+1));
            }
        }
        System.out.println(number/(1024*1024));
    }
}
