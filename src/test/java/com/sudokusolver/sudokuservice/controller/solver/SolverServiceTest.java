package com.sudokusolver.sudokuservice.controller.solver;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SolverServiceTest {

    @Test
    void whenProvidedSudkouShouldComplete() {
        int[][] initialMatrix = new int[9][9];
        initialMatrix[0][6] = 6;
        initialMatrix[0][7] = 8;

        initialMatrix[1][4] = 7;
        initialMatrix[1][5] = 3;
        initialMatrix[1][8] = 9;

        initialMatrix[2][0] = 3;
        initialMatrix[2][2] = 9;
        initialMatrix[2][7] = 4;
        initialMatrix[2][8] = 5;

        initialMatrix[3][0] = 4;
        initialMatrix[3][1] = 9;

        initialMatrix[4][0] = 8;
        initialMatrix[4][2] = 3;
        initialMatrix[4][4] = 5;
        initialMatrix[4][6] = 9;
        initialMatrix[4][8] = 2;

        initialMatrix[5][7] = 3;
        initialMatrix[5][8] = 6;

        initialMatrix[6][0] = 9;
        initialMatrix[6][1] = 6;
        initialMatrix[6][6] = 3;
        initialMatrix[6][8] = 8;

        initialMatrix[7][0] = 7;
        initialMatrix[7][3] = 6;
        initialMatrix[7][4] = 8;

        initialMatrix[8][1] = 2;
        initialMatrix[8][2] = 8;

        String expectedSolution = """
                1 7 2 5 4 9 6 8 3
                6 4 5 8 7 3 2 1 9
                3 8 9 2 6 1 7 4 5
                4 9 6 3 2 7 8 5 1
                8 1 3 4 5 6 9 7 2
                2 5 7 1 9 8 4 3 6
                9 6 4 7 1 5 3 2 8
                7 3 1 6 8 2 5 9 4
                5 2 8 9 3 4 1 6 7
                """;
        int[][] expected = Util.parseSudoku(expectedSolution);
        SolverService solverService = new SolverService(initialMatrix);
        int[][] actual = solverService.execute().finalMatrix();

        assertThat(actual).isEqualTo(expected);
    }
}