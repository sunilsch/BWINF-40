import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class a4 {

    //___number of runs___//
    static int numberOfRuns = 10000;
    public static void calculate(String filename) throws FileNotFoundException {
        //___Read-file___//
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int numberOfCubes = scanner.nextInt();
        int[][] cubes = new int[numberOfCubes][];
        boolean[] calculatedCubes = new boolean[numberOfCubes];
        int[][] results = new int[numberOfCubes][numberOfCubes];
        for(int i = 0; i < numberOfCubes; i++){
            int numberOfSides = scanner.nextInt();
            cubes[i] = new int[numberOfSides];
            for(int j = 0; j < numberOfSides; j++){
                cubes[i][j] = scanner.nextInt();
            }
        }

        //___Calculate for each Cube___//
        for (int i = 0; i < numberOfCubes; i++){
            for(int j = 0; j < numberOfCubes; j++){
                //___Check if Cube is already calculated___//
                if(calculatedCubes[j]){
                    //___reverse results___//
                    if(results[j][i] != 2){
                        results[i][j] = results[j][i] ^ 1;
                    } else {
                        results[i][j] = 2;
                    }
                    continue;
                //___Check if Cube i and j are the same___//
                } else if(i == j){
                    results[i][j] = -1;
                    continue;
                }
                //___Check if cube contains a six___//
                if(!checkSix(cubes[i])){
                    results[i][j] = 0;
                    if(!checkSix(cubes[j])){
                        results[i][j] = 2;
                    }
                    continue;
                } else if(!checkSix(cubes[j])){
                    results[i][j] = 1;
                    continue;
                }

                int numberOfWins = 0;
                int numberOfLoses = 0;
                int start = 0;

                //___Calculate x times___//
                for(int k = 0; k < numberOfRuns; k++){
                    int result = simulateOneGame(new int[][]{cubes[i], cubes[j]},start);
                    //___Check result___//
                    if(result == 0){
                        numberOfWins+=1;
                    } else if(result == 1){
                        numberOfLoses+=1;
                    }
                    //___change player with XOR Operator___//
                    start = start ^ 1;
                }

                //___Calculate relative difference
                double difference = (double)numberOfWins/(double)numberOfLoses;
                //___set results to array___//
                if(difference > 0.8 && difference < 1.2){
                    results[i][j] = 2;
                } else if(numberOfWins > numberOfLoses){
                    results[i][j] = 0;
                } else if(numberOfWins < numberOfLoses){
                    results[i][j] = 1;
                }

            }
            //___set Cube to calculated___//
            calculatedCubes[i] = true;
        }
        //___print out result___//
        printOutResults(cubes, results);

    }
    private static int simulateOneGame(int[][] cube, int player){
        //___create new game___//
        Game game = new Game(cube);
        //___start game___//
        while(!game.isFinished()){
            //___get Random number from cube___//
            int number = getRandom(cube[player]);
            //___Move token___//
            boolean again = game.move(number,player);
            //___Switch player___//
            if(!again){
                player = player ^ 1;
            }

        }
        //___return winner___//
        return game.whoHasWon();
    }
    private static int getRandom(int[] cube){
        Random random = new Random();
        //___get Random index between 0 and cube length___//
        int randomInt = random.nextInt(cube.length);
        //___return number from index___//
        return cube[randomInt];
    }
    private static void printOutResults(int[][] cubes, int[][] results){
        //___Print out cubes___//
        for(int i = 0; i < cubes.length; i++){
            System.out.println("Würfel "+(i+1)+" : "+Arrays.toString(cubes[i]));
        }
        System.out.println();
        System.out.print("             ");
        for(int i = 0; i < cubes.length; i++){
            System.out.print((i+1)+"      ");
        }
        System.out.println();
        //___Print out result-matrix___//
        for(int i = 0; i < cubes.length; i++){
            System.out.println("Würfel "+(i+1)+" : "+Arrays.toString(results[i])
            .replace("-1","-----")
            .replace("0","win  ")
            .replace("1","lose ")
            .replace("2","equal"));
        }
    }
    private static boolean checkSix(int[] cubes){
        //___Check each number of Cube___//
        for (int cube : cubes) {
            //___Check if number is 6____//
            if (cube == 6) {
                return true;
            }
        }
        return false;
    }
}