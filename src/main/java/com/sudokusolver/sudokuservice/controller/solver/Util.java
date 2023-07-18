package com.sudokusolver.sudokuservice.controller.solver;
import java.util.*;
import java.util.stream.Collectors;

public final class Util{


    private Util() {
    }

    /**
     * Finds the values that 2 arrays has in common and returns a new array containing them
     * @param array1
     * @param array2
     * @return Array containing the values array1 & array2 has in common
     */
    public static List<Integer> compareHelper(List<Integer> array1, List<Integer> array2){
        return array1.stream().filter(array2::contains).collect(Collectors.toList());
    }

    /**
     * fills a matrix with given value
     * @param matrix - the board
     * @param value - usually initial value to set board
     * @return - filled board
     */
    public static int[][] fillMatrix(int[][] matrix, int value){
        for (int[] ints : matrix) {
            Arrays.fill(ints, value);
        }
        return matrix;
    }

    /**
     * finds the index of a minimum value in a matrix
     * @param availableOptionsMatrix - matrix containing the possible options per index corresponding to the sudoku board
     * @return first index where we have the least amount of options to guess what number should be there
     */
    public static int[] findSlotWithLeastOptions(int[][] availableOptionsMatrix){
        int minVal = availableOptionsMatrix[0][0];
        int[] indexWithLeastOptions = {0,0};
        for(int i = 0; i < availableOptionsMatrix.length; i++){
            for(int j = 0; j < availableOptionsMatrix[0].length; j++){
                if(availableOptionsMatrix[i][j] < minVal && availableOptionsMatrix[i][j] != 1){
                    indexWithLeastOptions = new int[]{i,j};
                    minVal = availableOptionsMatrix[i][j];
                }
            }
        }
        return indexWithLeastOptions;
    }

    public static int convertToPrime(int number){
        return switch (number) {
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 5;
            case 4 -> 7;
            case 5 -> 11;
            case 6 -> 13;
            case 7 -> 17;
            case 8 -> 19;
            case 9 -> 23;
            case 0 -> 1;
            default -> throw new RuntimeException("input not supported: " + number); //this is for the 0:s
        };
    }

    public static int convertToRegular(int number){
        return switch (number) {
            case 2 -> 1;
            case 3 -> 2;
            case 5 -> 3;
            case 7 -> 4;
            case 11 -> 5;
            case 13 -> 6;
            case 17 -> 7;
            case 19 -> 8;
            case 23 -> 9;
            default -> number;
        };
    }

    /**
     * Reduces the input into the corresponding prime numbers and returns them as an array.
     *
     * @param num number to be reduced into prime factors
     * @return an array of the prime factors
     */
    public static ArrayList<Integer> reduce(int num){
        int i = 2;
        final ArrayList<Integer> primeFactors = new ArrayList<>();
        while(i < num){
            for(i = 2; i < num; i++){
                if(num%i == 0){
                    num /= i;
                    primeFactors.add(i);
                    break;
                }
            }
        }
        primeFactors.add(num);
        return primeFactors;
    }

    public static int relevantData(int i){
        if(i<3)return 3;
        if(i<6)return 6;
        return 9;
    }

    /**
     * To convert the input to an integer array
     * @return the board
     */
    public static int[][] stringToIntegerArray(){
        int[][] retVal = new int[9][9];
        try (final Scanner scan = new Scanner(System.in)) {
            String[] line;
            int i = 0;
            int j;
            while (scan.hasNextLine()) {
                line = scan.nextLine().split(" ");
                j = 0;
                for (String value : line) {
                    retVal[i][j++] = Integer.parseInt(value);
                }
                i++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while parsing sudoku!\n " + Arrays.toString(e.getStackTrace()));
        }
        return retVal;
    }
}