package com.sudokusolver.sudokuservice.controller.solver;

import com.sudokusolver.sudokuservice.controller.dto.SudokuResponseDto;

import java.util.Arrays;
import java.util.List;

public class SolverService {

    public enum Status{
        PRIME_NUMBER_CONVERSION,
        CALCULATING,
        UPDATING_BOARD,
        COMPARING,
        GUESSING,
        COMPLETED,
        NORMALISING_NUMBERS,
        MULTIPLE_ANSWERS,
        ERROR;
    }

    private final int[][] board;
    private final int[] productRow;
    private final int[] productColumn;
    private final int[][] productQuadrant;
    private int[][] availableOptions;
    private static final int[][] possibleSolution = new int[9][];

    private boolean stillWorkToDo;
    private boolean hasUpdatedThisIteration;
    private boolean noSolution;
    private boolean running;
    private static boolean completedOnce = false;
    private boolean multipleAnswers = false;

    private static final int TIMEOUT_IN_MILLIS = 2000;
    private static long SYSTEM_TIMEOUT_TIME;

    private final int fullBoardValue = 2*3*5*7*11*13*17*19*23;

    private Status currentStatus;

    public SolverService(int[][] matrix){
        board = matrix;
        noSolution = false;
        productQuadrant = new int[3][3];
        productColumn = new int[9];
        productRow = new int[9];
        running = true;
        availableOptions = new int[9][9];
        availableOptions = Util.fillMatrix(availableOptions, fullBoardValue);
        currentStatus = Status.PRIME_NUMBER_CONVERSION;
    }

    /**
     * state machine for the SolverService
     */
    public void run(){
        if(System.currentTimeMillis() > SYSTEM_TIMEOUT_TIME){
            resetStaticVariables();
            throw new RuntimeException("Something's iffy!");
        }
        switch (currentStatus) {
            case PRIME_NUMBER_CONVERSION -> {
                convertNumbers();
                currentStatus = Status.CALCULATING;
            }
            case CALCULATING -> {
                calculateCurrent();
                currentStatus = Status.UPDATING_BOARD;
            }
            case UPDATING_BOARD -> {
                hasUpdatedThisIteration = false;
                updateBoard();
                if (!stillWorkToDo) {
                    currentStatus = Status.NORMALISING_NUMBERS;
                } else if (!hasUpdatedThisIteration) {
                    currentStatus = Status.COMPARING;
                } else {
                    currentStatus = Status.CALCULATING;
                }
                if (noSolution) {
                    currentStatus = Status.ERROR;
                }
            }
            case COMPARING -> {
                compareBoard();
                if (hasUpdatedThisIteration) {
                    currentStatus = Status.CALCULATING;
                } else {
                    currentStatus = Status.GUESSING;
                }
            }
            case GUESSING -> running = false;
            case NORMALISING_NUMBERS -> {
                convertNumbersBack();
                currentStatus = Status.COMPLETED;
            }
            case COMPLETED -> {
                System.out.println("SUCCESS!");
                if (completedOnce) {
                    currentStatus = Status.MULTIPLE_ANSWERS;
                    break;
                }
                handleSuccessCase();
                running = false;
            }
            case MULTIPLE_ANSWERS -> {
                System.out.println("Non-unique");
                multipleAnswers = true;
                running = false;
            }
            case ERROR -> {
                System.out.println("Find another job");
                running = false;
            }
        }
    }

    /**
     * Execute method, takes a sudoku matrix as parameter and solves the sudoku.
     * @return 9*9 matrix with solved sudoku or empty if no solution available.
     */
    public SudokuResponseDto execute(){
        SYSTEM_TIMEOUT_TIME = System.currentTimeMillis() + TIMEOUT_IN_MILLIS;
        final SolverService solverService = new SolverService(copyMatrix(board));
        solverService.resetStaticVariables();
        while(solverService.running){
            solverService.run();
        }

        handeSpecialIteration(solverService);

        if(solverService.multipleAnswers) {
            solverService.currentStatus = Status.MULTIPLE_ANSWERS;
            return new SudokuResponseDto(board, Status.MULTIPLE_ANSWERS);
        } else if (solverService.completedOnce) {
            solverService.currentStatus = Status.COMPLETED;
            printFinalSolution();
            return new SudokuResponseDto(possibleSolution.clone(), Status.COMPLETED);
        } else {
            return new SudokuResponseDto(board, Status.ERROR);
        }

    }

    private void resetStaticVariables() {
        currentStatus = Status.PRIME_NUMBER_CONVERSION;
        noSolution = false;
        multipleAnswers = false;
        completedOnce = false;
    }

    private static void handeSpecialIteration(final SolverService sodsolv) {
        if (sodsolv.currentStatus == Status.COMPLETED) {
            return;
        } else {
            executeHelper(new SolverService(sodsolv.board));
        }
    }

    /**
     * Recursive method that tries to solve a sudoku using match and compare and then
     * guesses the inputs recursively (using the slots with the least alternatives) checks all to see if
     * there are multiple solutions.
     * @param solverService new solverService with updated board.
     * @return true if solved
     */
    private static void executeHelper(SolverService solverService){
        System.out.println("NEW ITERATION");
        solverService.currentStatus = Status.CALCULATING;
        while(solverService.running){
            solverService.run();
        }
        if(solverService.currentStatus == Status.GUESSING){
            final int[] indexWithLeastOptions = Util.findSlotWithLeastOptions(solverService.availableOptions);
            int row = indexWithLeastOptions[0];
            int column = indexWithLeastOptions[1];

            for(int i = 0; i < solverService.availableOptions[row][column]; i++){

                if(solverService.multipleAnswers){
                    break;
                }
                solverService.updateSingle(row, column, i);
                executeHelper(new SolverService(copyMatrix(solverService.board)));
            }
        }
    }

    private static int[][] copyMatrix(final int[][] initialBoard) {
        final int[][] arrayCopy = new int[9][];
        for (int i = 0; i < 9; i++) {
            arrayCopy[i] = Arrays.copyOf(initialBoard[i], initialBoard[i].length);
        }
        return arrayCopy;
    }

    private void savePossibleSolution() {
        for (int i = 0; i < 9; i++) {
            possibleSolution[i] = Arrays.copyOf(board[i], board[i].length);
        }
    }

    private void handleSuccessCase() {
        savePossibleSolution();
        boardPrinter();
        completedOnce = true;
    }


    /**
     * Prints the board as answer
     *
     */
    private void boardPrinter(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(possibleSolution[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    private static void printFinalSolution(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(possibleSolution[i][j] + " ");
            }
            System.out.print("\n");
        }
    }


    /**
     * General update method, checks corresponding row, column and quadrant to
     * see if there is only one feasible answer to put in and updates the board.
     * @param row
     * @param column
     * @param index
     */
    private void updateSingle(int row, int column, int index){
        final List<Integer> rowPrimes = Util.primeNumberFactorize(fullBoardValue/productRow[row]);
        final List<Integer> columnPrimes = Util.primeNumberFactorize(fullBoardValue/productColumn[column]);
        final List<Integer> quadrantPrimes = matchNumbersQuadrant(row,column);

        final List<Integer> commonValues = matchNumbersHelper(rowPrimes, columnPrimes, quadrantPrimes);
        if(index == -1){
            stillWorkToDo = true;
            availableOptions[row][column] = commonValues.size();
            if(commonValues.size() == 0){
                noSolution = true;
            }
            if(commonValues.size()==1){
                availableOptions[row][column] = fullBoardValue;
                hasUpdatedThisIteration = true;
                board[row][column] = commonValues.get(0);
            }
        }
        else{
            board[row][column] = commonValues.get(index);
        }
    }

    /**
     * Iterates through the board, looking for open slots and evaluates them. If a
     * relevant number is found it will then update it.
     */
    private void updateBoard(){
        stillWorkToDo = false;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(board[i][j] == 1){
                    updateSingle(i, j, -1);
                }
            }
        }
    }

    /**
     * Iterates through the board to compare the feasible inputs for each quadrant. E.g. if
     * a number is excluded from all open slots except one it will update the only option for
     * given slot.
     */
    private void compareBoard(){
        hasUpdatedThisIteration = false;

//        board = Util.update2DIntArray(board, (num, indices) -> compare(indices[0], indices[1]));
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(board[i][j] == 1){
                    board[i][j] = compare(i,j);
                }
            }
        }
    }

    private int compare(int row, int column){
        List<Integer> available = matchNumbersQuadrant(row, column);
        available = Util.compareHelper(available, Util.primeNumberFactorize(fullBoardValue/productColumn[column]));
        available = Util.compareHelper(available, Util.primeNumberFactorize(fullBoardValue/productRow[row]));
//        available = excluder(available, row, column);

        if (available.size() == 1) {
            hasUpdatedThisIteration = true;
            availableOptions[row][column] = fullBoardValue;
            return available.get(0);
        }
        return 1;
    }

    /**
     * Help method for compare(), compares the available numbers to the unavailable to
     * the other slots in given quadrant.
     * @param row
     * @param column
     * @param available
     * @return
     */
    private List<Integer> excluder(List<Integer> available, int row, int column){
        final int rows = Util.findQuadrantBoardIndex(row);
        final int columns = Util.findQuadrantBoardIndex(column);
        for(int x = rows-3; x < rows; x++){
            for(int y = columns-3; y < columns; y++){
                if(board[x][y] == 1 && !(x == row && y == column)){
                    available = Util.compareHelper(available, matcher(x,y));
                }

            }
        }
        return available;
    }

    /**
     * Helper method for excluder, checks which numbers are unavailable to given row and column
     * @param row
     * @param column
     * @return Array with the unavailable inputs for nearby slots
     */
    private List<Integer> matcher(int row, int column){
        final List<Integer> filled = Util.primeNumberFactorize(productRow[row]);
        filled.addAll(Util.primeNumberFactorize(productColumn[column]));
        return filled;
    }


    /**
     * Helper method for finding what values might be valid for a slot. Matches the arraylists with available prime numbers
     * for the row, column and quadrant relating to a slot and checks if there is only one answer.
     * @param row current values of the row of the slot we're testing
     * @param column current values of the column of the slot we're testing
     * @param quadrant current values of the quadrant of the slot we're testing
     * @return  the numbers available for the slot which then updates the board
     */
    private List<Integer> matchNumbersHelper(List<Integer> row, List<Integer> column, List<Integer> quadrant){
        List<Integer> sameVal =  Util.compareHelper(row, column);
        return Util.compareHelper(sameVal, quadrant);
    }


    /**
     * Helper method to match numbers, used for readability
     * @param row of slot to be evaluated
     * @param column of slot to be evaluated
     * @return  list with available prime numbers to match
     */
    private List<Integer> matchNumbersQuadrant(int row, int column){
        final int quadrantX = findQuadrant(row);
        final int quadrantY = findQuadrant(column);

        return Util.primeNumberFactorize(fullBoardValue/productQuadrant[quadrantX][quadrantY]);
    }

    private static int findQuadrant(int row) {
        if(row <3) return 0;
        if(row <6) return 1;
        return 2;
    }


    /**
     * Calculates the products of the rows and columns and quadrants for
     * every part of the board. This is so we can use prime number factorization
     * to evaluate which numbers are available for every slot and match.
     *
     */
    private void calculateCurrent(){

        //These loops calculate the row and column products separately
        for(int i = 0 ; i < 9; i++){
            productColumn[i] = 1;
            productRow[i] = 1;
            for(int j = 0; j < 9; j++){
                productRow[i] *= board[i][j];
                productColumn[i] *= board[j][i];
            }
        }

        //OBS I know this looks really messy, but I feel like 9 for loops is even messier...
        //complexity wise it is about as heavy as the double loop above.
        for(int i = 0; i < 3; i++){
            final int addX = i*3;
            for(int j = 0; j < 3; j++){
                final int addY = j*3;
                productQuadrant[i][j] = 1;
                for(int x = 0; x < 3; x++){
                    for(int y = 0; y < 3; y ++){
                        productQuadrant[i][j] *= board[x + addX][y + addY];
                    }
                }
            }
        }

    }





    /**
     * I'm using the Goedels approach in which we use prime numbers
     * to deduce which integers are missing from each row/column or quadrant.
     *
     */
    private void convertNumbers(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                board[i][j] = Util.convertToPrime(board[i][j]);
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void convertNumbersBack(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                board[i][j] = Util.convertToRegular(board[i][j]);
            }
        }
    }
}
