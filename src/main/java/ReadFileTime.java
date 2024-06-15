import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFileTime {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("./"+args[0]));
        String line;
        long submissionTime = 0;
        long completedTime = 0;
        int k = 1;
        long totalTime = 0;

        while((line = br.readLine())!=null){
            if(line.contains("\"SparkListenerStageCompleted\"")){
                int index = line.indexOf("\"Submission Time\":");
                String line1 = line.substring(index);
                submissionTime = Long.parseLong(line1.substring(0,line1.indexOf(",")).substring(line1.indexOf(":")+1));

                index = line.indexOf("\"Completion Time\":");
                line1 = line.substring(index);
                completedTime = Long.parseLong(line1.substring(0,line1.indexOf(",")).substring(line1.indexOf(":")+1));

                System.out.println(k+" - "+ (completedTime-submissionTime)/1000);
                totalTime = totalTime + (completedTime-submissionTime)/1000;
                k++;
            }
        }
        System.out.println("Total Time: "+ totalTime);
        System.out.println();
    }
}
