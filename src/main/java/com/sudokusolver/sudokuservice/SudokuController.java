package com.sudokusolver.sudokuservice;

import com.sudokusolver.sudokuservice.dto.SudokuRequestDto;
import com.sudokusolver.sudokuservice.solver.SolverService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SudokuController {

    @PostMapping("/solve")
    int[][] solveSudoku(@RequestBody SudokuRequestDto req){
        SolverService service = new SolverService(req.matrix());
        return service.execute();
    }
}
