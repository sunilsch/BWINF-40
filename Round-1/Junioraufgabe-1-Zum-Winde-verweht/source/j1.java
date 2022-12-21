import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class j1 {
    public static void calculateTurbines(String filename) throws FileNotFoundException{
        //___Read-File___//
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int numberOfHomes = scanner.nextInt();
        int numberOfTurbines = scanner.nextInt();

        //___Initialise-Arrays___//
        int[] xCoordinatesHomes = new int[numberOfHomes];
        int[] yCoordinatesHomes = new int[numberOfHomes];

        //___Save coordinates of Homes___//
        for(int i = 0; i < numberOfHomes; i++){
            xCoordinatesHomes[i] = scanner.nextInt();
            yCoordinatesHomes[i] = scanner.nextInt();
        }
        //___Calculate-for-each-Turbine___//
        for(int i = 0; i < numberOfTurbines; i++){

            //___Save coordinates of Turbine___//
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            //___Calculate-Height and Print-out-result___//
            System.out.print("Wind Turbine "+(i+1)+" : ");
            System.out.println("Final-Height: "+calculateTurbineHeight(x,y,xCoordinatesHomes,yCoordinatesHomes) + " m");
        }
    }
    private static double calculateTurbineHeight(int x, int y, int[] xCoordinatesHomes, int[] yCoordinatesHomes){
        //___Initialise-Minimal-Distance___//
        double minimalDistance = Integer.MAX_VALUE;

        //___Check-Distance-For-Each-Home___//
        for(int i = 0; i < xCoordinatesHomes.length; i++){
            //___calculate x and y distance___//
            int distanceX = xCoordinatesHomes[i] - x;
            int distanceY = yCoordinatesHomes[i] - y;

            //___calculate distance with the theorem of pythagoras___//
            double distance = Math.sqrt((distanceX*distanceX)+(distanceY*distanceY));
            //___check if it's lower than minimal height___/
            if(distance < minimalDistance){
                minimalDistance = distance;
            }
            /*System.out.println("\nHaus: "+(i+1));
            System.out.println("Distance X: "+distanceX);
            System.out.println("Distance Y: "+distanceY);
            System.out.println("Distance: "+distance);
            System.out.println("Min-Distance: "+minimalDistance);*/
        }
        //___Return Minimal Height___//
        return minimalDistance/10;
    }
}