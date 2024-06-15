import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("./"+args[1]));
        String line;
        long number = 0;
        while((line = br.readLine())!=null){
            if(line.contains(args[0])){
                int index = line.indexOf(args[0]);
                line = line.substring(index);
                number = number + Long.parseLong(line.substring(0,line.indexOf(",")).substring(line.indexOf(":")+1));
            }
        }
        System.out.println(number/(1024*1024));
    }
}
