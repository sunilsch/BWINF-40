import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;

public class Bonusaufgabe {
    // init vars for reading file
    private static int nOfCards;
    private static int nOfZaraCards;
    private static int nOfBits;
    private static long[][] cards;

    // init xorSum list
    private static final List<xorSumIdx> xorSums = new ArrayList<>();

    // init vars for combinations
    private static boolean saveXorSum = true;
    private static boolean finish = false;
    private static int c = 0;

    // init solution array
    private static short[] solution;

    // init console and file output
    private static PrintStream consoleOutput;
    private static PrintStream fileOutput;

    /**
     * This function starts the algorithm and calculates the right cards
     *
     * @param filename path of selected file : String
     */
    public static void solve_problem(String filename) throws FileNotFoundException {
        initOutput(filename);

        long start;
        long stop;

        //FIRST STEP//
        consoleOutput.println("### Read file....");
        start = System.currentTimeMillis();
        readFile(filename);
        stop = System.currentTimeMillis();
        consoleOutput.printf("### Finished! in %f seconds%n\n", ((double) stop - (double) start) / 1000);

        //SECOND STEP//
        consoleOutput.println("### Calculate xor_sums....");
        start = System.currentTimeMillis();
        initCombinations(nOfZaraCards / 2);
        stop = System.currentTimeMillis();
        consoleOutput.printf("### Finished! in %f seconds%n\n", ((double) stop - (double) start) / 1000);

        //THIRD STEP//
        consoleOutput.println("### Sort Array....");
        start = System.currentTimeMillis();
        //xorSums.sort(new xorSumIdxComparator());
        quickSort(xorSums, 0, xorSums.size() - 1);
        stop = System.currentTimeMillis();
        consoleOutput.printf("### Finished! in %f seconds%n\n", ((double) stop - (double) start) / 1000);

        //FOURTH STEP//
        saveXorSum = false;
        c = 0;
        consoleOutput.println("### Search Array...");
        start = System.currentTimeMillis();
        initCombinations((nOfZaraCards / 2) + 1);
        stop = System.currentTimeMillis();
        consoleOutput.printf("### Finished! in %f seconds%n\n", ((double) stop - (double) start) / 1000);

        //LAST STEP//
        printResult();

        partB();

    }

    /**
     * This function initializes the Output to console and file
     *
     * @param filename path of selected file : String
     */
    private static void initOutput(String filename) throws FileNotFoundException {
        consoleOutput = System.out;
        String file = filename.split("\\.")[0];
        String[] parts = file.split("\\\\");
        file = parts[parts.length - 1];
        fileOutput = new PrintStream("./beispielausgaben/" + file + ".out");
    }

    /**
     * This function prints to the console and to the output file
     *
     * @param message message that has to be printed : String
     */
    private static void printBoth(String message) {
        fileOutput.print(message);
        consoleOutput.print(message);
    }

    /**
     * This function reads the input file
     *
     * @param filename path of selected file : String
     */
    private static void readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        nOfCards = scanner.nextInt();
        nOfZaraCards = scanner.nextInt();
        nOfBits = scanner.nextInt();

        cards = new long[nOfCards][2];

        for (int i = 0; i < nOfCards; i++) {
            String s = scanner.next();
            cards[i][0] = Long.parseUnsignedLong(s.substring(0, (nOfBits / 2)), 2);
            cards[i][1] = Long.parseUnsignedLong(s.substring((nOfBits / 2) + 1), 2);
        }

    }

    /**
     * This function starts the recursive Combination function
     *
     * @param r amount of numbers per combination : int
     */
    private static void initCombinations(int r) {
        short[] a = new short[nOfCards];
        for (short i = 0; i < nOfCards; i++) {
            a[i] = i;
        }
        combination(a, new short[r], 0, 0, r);
    }

    /**
     * This function works recursive and calculates the next combination
     *
     * @param list    array of numbers to choose from : short[]
     * @param current current combination array : short[]
     * @param start   next index after last number in combination : int
     * @param index   current index of number in combination : int
     * @param r       amount of numbers per combination : int
     */
    private static void combination(short[] list, short[] current, int start, int index, int r) {
        if (!finish) {
            if (index >= r) {
                if (saveXorSum)
                    calculateXorSumAndSave(current);
                else
                    calculateXorSumAndSearch(current);
                return;
            }
            for (int i = start; i < list.length && list.length - i >= r - index; i++) {
                current[index] = list[i];
                combination(list, current, i + 1, index + 1, r);
            }
        }
    }

    /**
     * This function calculates the xorSum of a combination and stores the indices and the value into an xorSumIdx Object
     *
     * @param list list of combination indices : short[]
     */
    private static void calculateXorSumAndSave(short[] list) {
        count();
        xorSumIdx xor = new xorSumIdx(new long[2], list.clone());
        for (int x : list) {
            xor.xorSum[0] = xor.xorSum[0] ^ cards[x][0];
            xor.xorSum[1] = xor.xorSum[1] ^ cards[x][1];
        }
        xorSums.add(xor);
    }

    /**
     * This function calculates the xorSum of a combination and search the calculated xorSum for the same value
     *
     * @param list list of combination indices : short[]
     */
    private static void calculateXorSumAndSearch(short[] list) {
        count();
        long[] xorSum = new long[2];
        for (int x : list) {
            xorSum[0] = xorSum[0] ^ cards[x][0];
            xorSum[1] = xorSum[1] ^ cards[x][1];
        }
        int res = binarySearch(xorSum, 0, xorSums.size() - 1);
        if (res != -1) {
            if (validSolution(list, xorSums.get(res).idx)) {
                finish = true;
            }

        }
    }

    /**
     * This function uses binary search to search the xorSum list
     *
     * @param x value to search for : long[]
     * @param l left limit of binary search : int
     * @param r right limit of binary search : int
     * @return index of found value : int
     */
    private static int binarySearch(long[] x, int l, int r) {
        int mid = (l+r) / 2;
        if (r < l) {
            return -1;
        }
        if (xorSums.get(mid).xorSum[0] == x[0] && xorSums.get(mid).xorSum[1] == x[1]) {
            return mid;
        } else if (xorSums.get(mid).xorSum[0] > x[0]) {
            return binarySearch(x, l, (mid - 1));
        }
        return binarySearch(x, mid + 1, r);
    }

    /**
     * This function counts the number of combinations that are already calculated
     */
    private static void count() {
        c++;
        if (c % 1000000 == 0) {
            consoleOutput.printf("%d combinations calculated!\n", c);
        }
    }

    /**
     * This function checks if a solution is valid (if indices are there only once)
     *
     * @param list1 first half of indices : short[]
     * @param list2 second half of indices : short[]
     * @return if solution is valid or not : boolean
     */
    private static boolean validSolution(short[] list1, short[] list2) {
        solution = new short[nOfZaraCards + 1];
        int i;
        for (i = 0; i < list1.length; i++)
            solution[i] = list1[i];
        for (int j = 0; j < list2.length; j++)
            solution[i + j] = list2[j];
        for (int j = 0; j < solution.length - 1; j++) {
            if (solution[j] == solution[j + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * This function prints a card in binary
     *
     * @param card card that has to be printed : long[]
     * @param x    index of card : short
     */
    private static void printOutCard(long[] card, short x) {
        String pre_output = String.format("%" + ((nOfBits / 2)) + "s", Long.toUnsignedString(card[0], 2)).replace(' ', '0') + String.format("%" + ((nOfBits / 2)) + "s\n", Long.toUnsignedString(card[1], 2)).replace(' ', '0');
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < pre_output.length(); i++) {
            output.append(' ');
            output.append(pre_output.charAt(i));
        }
        printBoth(String.format("%3d :", x));
        printBoth(output.toString());
    }

    /**
     * This function prints the calculated cards
     */
    private static void printResult() {
        printBoth("Solution keys at: " + Arrays.toString(solution) + "\n");
        printBoth("Solution keys: \n");
        for (short x : solution) {
            printOutCard(cards[x], x);
        }
    }

    /**
     * This function solves partB (calculates possible cards for a week)
     */
    private static void partB() {
        long[][] solutionValues = new long[solution.length][2];
        for (int i = 0; i < solution.length; i++)
            solutionValues[i] = cards[solution[i]];
        bubbleSort(solutionValues);
        int answer = askQuestion(true);
        printBoth("Possible keys for week " + answer + ": \n");
        answer--;
        printOutCard(solutionValues[answer], solution[answer]);
        printOutCard(solutionValues[answer + 1], solution[answer + 1]);

    }

    /**
     * This function gets the users input which week has to be calculated
     *
     * @param first true if function is called for the first time : boolean
     * @return week that has to be calculated : int
     */
    private static int askQuestion(boolean first) {
        Scanner scanner = new Scanner(System.in);
        consoleOutput.print(first ? "Which week? : " : "Invalid week, value must be between 1 and " + nOfZaraCards + "? : ");
        int answer = scanner.nextInt();
        if (answer <= nOfZaraCards && answer > 0) {
            return answer;
        } else {
            return askQuestion(false);
        }
    }

    /**
     * This function implements the bubbleSort algorithm to sort the solution values
     *
     * @param list solution values : long[][]
     */
    private static void bubbleSort(long[][] list) {
        long[] Cache;
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length - 1 - i; j++) {
                if (Long.compareUnsigned(list[j][0], list[j + 1][0]) > 0) {
                    Cache = list[j + 1];
                    list[j + 1] = list[j];
                    list[j] = Cache;
                }
            }
        }
    }

    /**
     * This function sorts a List of xorSumIdx Objects by using the quickSort algorithm
     * @param list list of xorSumIdx Objects : List<xorSumIdx>
     * @param low low pointer of quickSort algorithm : int
     * @param high high pointer of quickSort algorithm : int
     */
    private static void quickSort(List<xorSumIdx> list, int low, int high) {
        if (low < high) {
            int pivot = quickSortDivideList(list, low, high);
            quickSort(list, pivot + 1, high);
            quickSort(list, low, pivot - 1);
        }
    }

    /**
     * This function sorts a list by the pivot element and is used by the quickSort algorithm
     * @param list list of xorSumIdx Objects : List<xorSumIdx>
     * @param low low pointer of quickSort algorithm : int
     * @param high high pointer of quickSort algorithm : int
     * @return index of pivot element
     */
    private static int quickSortDivideList(List<xorSumIdx> list, int low, int high) {
        long pivot = list.get(high).xorSum[0];
        int lowCache = low;
        int highCache = high;
        xorSumIdx Cache;
        while (low < high) {
            while (high > lowCache && list.get(high).xorSum[0] >= pivot) {
                high--;
            }
            while (low < highCache && list.get(low).xorSum[0] < pivot) {
                low++;
            }
            if (low < high) {
                Cache = list.get(low);
                list.set(low, list.get(high));
                list.set(high, Cache);
            }
        }
        if (list.get(low).xorSum[0] > pivot) {
            Cache = list.get(low);
            list.set(low, list.get(highCache));
            list.set(highCache, Cache);
        }
        return low;
    }
}