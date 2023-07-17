package com.sudokusolver.sudokuservice.solver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
public final class Util{


    private Util() {
    }

    /**
     * Finds the values that 2 arrays has in common and returns a new array containing them
     * @param array1
     * @param array2
     * @return Array containing the values array1 & array2 has in common
     */
    public static ArrayList<Integer> compareHelper(Iterable<Integer> array1, Iterable<Integer> array2){
        final ArrayList<Integer> same = new ArrayList<>();
        for(int int1 : array1){
            for(int int2 : array2){
                if(int1 == int2){
                    same.add(int1);
                }
            }
        }

        return removeDuplicate(same);
    }

    /**
     * fills a matrix with given value
     * @param matrix
     * @param value
     * @return
     */
    public static int[][] fillMatrix(int[][] matrix, int value){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                matrix[i][j] = value;
            }
        }
        return matrix;
    }

    /**
     * finds the index of a minimum value in a matrix
     * @param matrix
     * @return
     */
    public static int[] findLeastAmountOfSLotOptions(int[][] matrix){
        int minVal = matrix[0][0];
        int[] retVal = {0,0};
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                if(matrix[i][j] < minVal && matrix[i][j] != 1){
                    retVal = new int[]{i,j};
                    minVal = matrix[i][j];
                }
            }
        }
        return retVal;
    }

    /**
     * removes the duplicate values in an array
     * @param list
     * @return
     */
    private static ArrayList<Integer> removeDuplicate(ArrayList<Integer> list){
        int i = 0;
        int j;
        while(i < list.size()){
            j = i+1;
            while(j < list.size()){
                if(Objects.equals(list.get(i), list.get(j))){
                    list.remove(j);
                    j--;
                }
                j++;
            }
            i++;
        }
        return list;
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