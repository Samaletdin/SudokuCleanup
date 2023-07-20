package com.sudokusolver.sudokuservice.controller.solver;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void whenProvidedSudokuAsStringShouldScanProperly(){
        String sudoku =
                """
                        1 0 1 0 1 0 1 0 1
                        1 0 1 0 1 0 1 0 1
                        1 0 1 0 1 0 1 0 1
                        1 0 1 0 1 0 1 0 1
                        1 0 1 0 1 0 1 0 1
                        1 0 1 0 1 0 1 0 1
                        1 0 1 0 1 0 1 0 1
                        1 0 1 0 1 0 1 0 1
                        1 0 1 0 1 0 1 0 1
                        """;
        int[][] expected = Util.update2DIntArray(new int[9][9], (num, indices) -> {
            if(indices[1]%2 == 0){
                return 1;
            }
             return 0;
        });

        int[][] actual = Util.parseSudoku(sudoku);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenProvidedFaultySudokuShouldThrow(){
        String sudoku =
                "000000000\n" +
                "000000000\n" +
                "000000000\n" +
                "000000000\n" +
                "000000000\n" +
                "000000000\n" +
                "000000000\n" +
                "000000000\n" +
                "00000000\n" ;
        assertThrows(RuntimeException.class, () -> Util.parseSudoku(sudoku));
    }
}