package com.sudokusolver.sudokuservice.controller;

import com.sudokusolver.sudokuservice.controller.dto.SudokuRequestDto;
import com.sudokusolver.sudokuservice.controller.dto.SudokuResponseDto;
import com.sudokusolver.sudokuservice.controller.solver.SolverService;
import org.springframework.util.concurrent.FutureUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Timer;
import java.util.concurrent.*;

@RestController()
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("sudoku")
public class SudokuController {

    @PostMapping("/solve")
    SudokuResponseDto solveSudoku(@RequestBody SudokuRequestDto req){
        SolverService solverService = new SolverService(req.matrix());
        try {
            return solverService.execute();
        } catch (RuntimeException e){
            return new SudokuResponseDto(req.matrix(), SolverService.Status.ERROR);
        }
    }

    @GetMapping("/hello")
    String hello(){
        return "hello world!";
    }
}
