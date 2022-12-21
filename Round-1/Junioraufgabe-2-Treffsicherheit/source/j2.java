import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class j2 {
    public static void calculateChanges(String filename) throws FileNotFoundException{
        //___READ-FILE___//
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int numberOfMembers = scanner.nextInt();
        int numberOfDates = scanner.nextInt();
        //___Save data into 2D-Array___//
        int[][] dataSet = new int[numberOfMembers][numberOfDates];
        for(int i = 0; i < numberOfMembers; i++){
            for(int j = 0; j < numberOfDates; j++){
                dataSet[i][j] = scanner.nextInt();
            }
        }
        //___Calculate bestAnswers 2D-Array___//
        int[] bestAnswers = new int[numberOfDates];
        for(int i = 0; i < numberOfMembers;i++){
            //___Calculate the best Answer of one Member___//
            int bestAnswer = 2;
            for(int j = 0; j < numberOfDates;j++){
                if(dataSet[i][j] < bestAnswer){
                    bestAnswer = dataSet[i][j];
                }
            }
            //___For each day add best Answer___//
            for(int j = 0; j < numberOfDates; j++){
                if(dataSet[i][j] == bestAnswer){
                    bestAnswers[j] += 1;
                }
            }
        }
        //___Calculate best Date___//
        int bestDate = 0;
        int bestDateNumber = 0;
        for(int i = 0; i < numberOfDates; i++){
            if(bestAnswers[i] > bestDateNumber){
                bestDateNumber = bestAnswers[i];
                bestDate = i+1;
            }
        }
        System.out.println("Best date: "+bestDate);
        System.out.println("Number of Entries that needs to be changed: "+(numberOfMembers-bestDateNumber));
    }
}