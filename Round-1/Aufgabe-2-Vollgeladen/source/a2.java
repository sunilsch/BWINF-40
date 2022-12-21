import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class a2 {
    //___Create Variables___//
    static int[] hotelDistance;
    static double[] hotelRating;
    static int totalTravelTime;
    static int numberOfHotels;
    static int dailyTravelTime = 360;
    static int[] bestHotels;
    static double bestHotelRating = 0;
    public static void calculateHotels(String filename) throws FileNotFoundException {
        //___Read-file___//
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        numberOfHotels = scanner.nextInt();
        totalTravelTime = scanner.nextInt();
        hotelDistance = new int[numberOfHotels];
        hotelRating = new double[numberOfHotels];
        for(int i = 0; i < numberOfHotels; i++){
            hotelDistance[i] = scanner.nextInt();
            hotelRating[i] = Double.parseDouble(scanner.next());
        }
        //___calculate-Hotels___//
        calculateNextHotel(new int[5],1,0);
        if(bestHotels != null) {
            //___Print-out-results
            System.out.println("These hotels are the bests:");
            for (int i = 1; i < 5; i++) {
                System.out.println(bestHotels[i] + ". Hotel at: " + hotelDistance[bestHotels[i]] + " min  Rating: " + hotelRating[bestHotels[i]]);
            }
            System.out.println("Lowest-Rating: " + bestHotelRating);
        } else {
            System.out.println("There is no possible route");
        }
    }

    private static void calculateNextHotel(int[] hotelIndexes, int day, int pastTime){
        //___Check if day is valid___//
        if(day < 5){
            //___Check for each hotel
            for(int i = (hotelIndexes[day-1] == 0) ? 0 : hotelIndexes[day-1]+1; i < numberOfHotels; i++){
                hotelIndexes[day] = i;
                //___Check if hotelDistance isn't too far away
                if(hotelDistance[i] > pastTime+dailyTravelTime){
                    break;
                //___Check if destination can be reached and if hotel rating is better than bestHotelRating___//
                } else if(hotelDistance[i]+dailyTravelTime*(5-day) < totalTravelTime || hotelRating[i] < bestHotelRating){
                    continue;
                }
                //___Calculate next Hotel___//
                calculateNextHotel(hotelIndexes,day+1, hotelDistance[i]);
            }
        } else{
            //___Check if destination is reached___//
            if(pastTime+dailyTravelTime >= totalTravelTime){
                //___Calculate Minimal Rating___//
                double minimalRating = calculateMinimalRating(hotelIndexes,0);

                //___Check if this is a better minimal Rating
                if(bestHotelRating <= minimalRating){
                    //___Check if it's the same rating then the best___//
                    if(bestHotelRating == minimalRating) {
                        //___Check the second, third and fourth worse rating is better...//
                        for (int i = 1; i < 4; i++) {
                            //___Calculate (second or third, fourth) Minimal Rating___//
                            double min1 = calculateMinimalRating(hotelIndexes, i);
                            //___Calculate (second or third, fourth) Minimal Rating of Best___//
                            double min2 = calculateMinimalRating(bestHotels, i);
                            //___Check if it's better than Best___//
                            if (min1 > min2) {
                                break;
                            //___Check if it's worse than Best___//
                            } else if (min1 < min2) {
                                return;
                            }
                        }
                    }
                    //___Set current minimalRating to best___//
                    bestHotelRating = minimalRating;
                    //___Copy hotels and set them to best___//
                    int[] copy = hotelIndexes.clone();
                    bestHotels = copy;
                }
           }
        }
    }
    private static double calculateMinimalRating(int[] hotelIndexes, int index){
        //___Create Array with all ratings of the hotels___//
        double[] ratings = new double[hotelIndexes.length-1];
        for(int i = 1; i < hotelIndexes.length; i++){
            ratings[i-1] = hotelRating[hotelIndexes[i]];
        }
        //___Sort this Array using insertion Sort___//
        bubbleSort(ratings);

        //___return rating from index (0 = Minimum, 1 = second minimum)
        return ratings[index];
    }
    private static void bubbleSort(double[] list) {
        //___Create variable Cache for swap___//
        double Cache;
        //___For each element go through the list__//
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length - 1 - i; j++) {
                //___Check If Element is greater than the other___//
                if (list[j] > list[j + 1]) {
                    //___Swap-Elements___//
                    Cache = list[j + 1];
                    list[j + 1] = list[j];
                    list[j] = Cache;
                }
            }
        }
    }
}