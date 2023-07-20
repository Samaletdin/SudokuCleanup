package com.sudokusolver.sudokuservice.controller.solver;

public class Matrix {
    private final int[][] board;
    private final int[] productRow;
    private final int[] productColumn;
    private final int[][] productQuadrant;

    public Matrix() {
        board = new int[9][9];
        productRow = new int[9];
        productColumn = new int[9];
        productQuadrant = new int[3][3];
    }
}
