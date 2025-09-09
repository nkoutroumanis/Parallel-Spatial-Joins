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

        long jobSubmissionTime = 0;
        long jobCompletedTime = 0;

        while((line = br.readLine())!=null){

            if(line.contains("\"SparkListenerJobStart\"")){
                int index = line.indexOf("\"Submission Time\":");
                String line1 = line.substring(index);
                jobSubmissionTime = Long.parseLong(line1.substring(0,line1.indexOf(",")).substring(line1.indexOf(":")+1));
            }

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

            if(line.contains("\"SparkListenerJobEnd\"")){
                int index = line.indexOf("\"Completion Time\":");
                String line1 = line.substring(index);
                jobCompletedTime = Long.parseLong(line1.substring(0,line1.indexOf(",")).substring(line1.indexOf(":")+1));
            }

        }

        System.out.println("Total Time: "+ (jobCompletedTime-jobSubmissionTime)/1000 + " - Rest Time: "+((jobCompletedTime-jobSubmissionTime) - (completedTime-submissionTime))/1000  +" ("+((((jobCompletedTime-jobSubmissionTime) - (completedTime-submissionTime))/(double)(jobCompletedTime-jobSubmissionTime))*100)+"%) - Join time:" + (completedTime-submissionTime)/1000 +" ("+((double)(completedTime-submissionTime)/(jobCompletedTime-jobSubmissionTime))*100 +"%)");
        System.out.println();
    }
}
