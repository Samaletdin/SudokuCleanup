package com.sudokusolver.sudokuservice.controller;

import com.sudokusolver.sudokuservice.controller.dto.SudokuRequestDto;
import com.sudokusolver.sudokuservice.controller.solver.SolverService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("sudoku")
public class SudokuController {

    @PostMapping("/solve")
    int[][] solveSudoku(@RequestBody SudokuRequestDto req){
        SolverService service = new SolverService(req.matrix());
        return service.execute();
    }

    @GetMapping("/hello")
    String hello(){
        return "hello world!";
    }
}
