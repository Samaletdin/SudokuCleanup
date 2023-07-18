package com.sudokusolver.sudokuservice;

import com.sudokusolver.sudokuservice.controller.solver.SolverService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SudokuServiceApplicationTests {

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



        SolverService service = new SolverService(initialMatrix);
        service.execute(initialMatrix);
    }

}
