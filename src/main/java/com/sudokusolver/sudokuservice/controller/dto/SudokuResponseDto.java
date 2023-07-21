package com.sudokusolver.sudokuservice.controller.dto;

import com.sudokusolver.sudokuservice.controller.solver.SolverService;

public record SudokuResponseDto(int[][] finalMatrix, SolverService.Status status) {
}
