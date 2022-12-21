import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

public class a1 {
    public static int numberOfParkingCars;

    public static int lowestTurnsNumber;

    //___use TreeMap instead of HashMap because of sorting___//
    public static TreeMap<Character, Integer> lowestTurns = new TreeMap<>();


    public static void calculateMovement(String filename) throws FileNotFoundException {

        //____Create-HashMap____//
        TreeMap<Integer, Character> horizontalCars = new TreeMap<>();

        //_____READ-FILE_____//
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        numberOfParkingCars = (int) scanner.nextLine().charAt(2) - 64;
        int numberOfCars = scanner.nextInt();
        for (int i = 0; i < numberOfCars; i++) {
            char car = scanner.next().charAt(0);
            int position = scanner.nextInt();
            horizontalCars.put(position, car);
        }

        //___Check best option for each car___//
        for (int car = 0; car < numberOfParkingCars; car++) {

            //_____Clear-Variable_____//
            lowestTurnsNumber = Integer.MAX_VALUE;
            lowestTurns.clear();

            //_____Check if something needs to be changed_____//
            if (horizontalCars.containsKey(car) || horizontalCars.containsKey(car - 1)) {
                //____find-car-that-needs to be changed____//
                int currentCar;
                if (horizontalCars.containsKey(car)) {
                    currentCar = car;
                } else {
                    currentCar = car - 1;
                }
                //___Copy-HashMap-for-second-try___//
                TreeMap<Integer, Character> horizontalCarsLeft = new TreeMap<>(horizontalCars);
                TreeMap<Integer, Character> horizontalCarsRight = new TreeMap<>(horizontalCars);
                //____try to move left and right___//
                calculateMovementForOneCar(car, currentCar, horizontalCarsLeft, "left", 0, new TreeMap<>(), 0);
                calculateMovementForOneCar(car, currentCar, horizontalCarsRight, "right", 0, new TreeMap<>(), 0);

                //____Check if there is no solution____//
                if (lowestTurnsNumber == Integer.MAX_VALUE) {
                    System.out.println("car " + (char) (car + 65) + " : No solution");
                    continue;
                }
            }
            //___Print-out-Best-Solution___//
            if (!(lowestTurnsNumber == Integer.MAX_VALUE)) {
                System.out.print("\ncar " + (char) (car + 65) + " -> ");
                lowestTurns.forEach((k, v) -> System.out.print(k + ":" + (v > 0 ? v + " to right   " : v * -1 + " to left    ")));
            } else {
                System.out.print("\ncar " + (char) (car + 65) + " -> nothing needs to be changed");
            }
        }
    }

    private static void calculateMovementForOneCar(int car, int currentCar, TreeMap<Integer, Character> cars, String direction, int numberOfTurns, TreeMap<Character, Integer> turns, int numberOfCarChanges) {
        //_____Check if something needs to be changed_____//
        if (cars.containsKey(car) || cars.containsKey(car - 1)) {
            //___Get-character-of-current-Car___//
            char name = cars.get(currentCar);

            //___Check if the car isn't out of bound__//
            if ((direction == "left" && currentCar != 0) || (direction == "right" && currentCar < (numberOfParkingCars - 2))) {
                //___Check if another car hasn't to move___//
                if ((!cars.containsKey(currentCar - 2) && direction == "left") || (!cars.containsKey(currentCar + 2) && direction == "right")) {
                    //___Shift car__//
                    cars.remove(currentCar);
                    currentCar -= (direction == "left" ? 1 : -1);
                    cars.put(currentCar, name);

                    //___Save movement in HashMap___//
                    if (turns.containsKey(name)) {
                        int offset = turns.get(name);
                        turns.put(name, offset - (direction == "left" ? 1 : -1));
                    } else {
                        turns.put(name, (direction == "left" ? -1 : 1));
                    }

                    //___increase number of Turns___//
                    numberOfTurns += 1;

                    //___Check if car has changed___//
                    if (numberOfCarChanges != 0) {
                        currentCar = currentCar + (direction == "left" ? 3 : -3);
                        numberOfCarChanges -= 1;
                    }
                } else {
                    //___Change-To-Next-Car___//
                    currentCar -=  (direction == "left" ? 2 : -2);
                    numberOfCarChanges += 1;
                }
            } else {
                return;
            }
            if (numberOfTurns <= lowestTurnsNumber) {
                //___Cal methode again___//
                calculateMovementForOneCar(car, currentCar, cars, direction, numberOfTurns, turns, numberOfCarChanges);
            }
        } else {
            //___check if this solution is better___//
            if (numberOfTurns < lowestTurnsNumber || (numberOfTurns==lowestTurnsNumber && turns.size() < lowestTurns.size())) {
                lowestTurnsNumber = numberOfTurns;
                lowestTurns.clear();
                lowestTurns.putAll(turns);
            }
        }
    }
}