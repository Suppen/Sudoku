"use strict";

/**************************
 * Import important stuff *
 **************************/

import { SudokuBoard } from "./SudokuBoard";
import _ from "lodash";

/*****************************
 * The sudokuSolver function *
 *****************************/

/**
 * Solves a sudoku by recursive brute force
 *
 * @param {SudokuBoard} board	The sudoku to solve
 * @param {Integer} [startIndex]	The index to start looking for empty cells on. Defaults to 0, i.e. the first cell
 *
 * @returns {SudokuBoard}	The solved version of the input board
 *
 * @throws {Error}	If the board has an error, or the board is unsolvable
 */
function sudokuSolver(board, startIndex = 0) {
	// Check if the board is solved
	if (board.solved) {
		return board;
	} else if (board.errors.length > 0) {
		throw new Error("Sudoku board has errors");
	}

	// Find the first empty cell
	let cellIndex = startIndex;
	while (board.getCell(cellIndex).value !== 0) {
		cellIndex++;
	}

	// Handle for the solved board
	let solvedBoard = null;

	// Find out what values the cell can possibly have
	let cell = board.getCell(cellIndex);
	let possibleValues = _.difference([1, 2, 3, 4, 5, 6, 7, 8, 9],
		board.getRow(cell.row).map(({value}) => value),
		board.getCol(cell.col).map(({value}) => value),
		board.getSquare(cell.square).map(({value}) => value),
	);

	while (possibleValues.length > 0 && solvedBoard === null) {
		// Set the cell to the next value
		board.setCellValue(cellIndex, possibleValues.pop());

		try {
			// Recurse down to solve next cell
			solvedBoard = sudokuSolver(board.clone(), cellIndex+1);
		} catch (err) {
			// There were no solutions with this number
		}
	}

	// Check if the board was actually solved
	if (solvedBoard === null) {
		throw new Error("Board is unsolvable");
	}

	// The board is now solved!
	return solvedBoard;
}

/*********************
 * Export the solver *
 *********************/

export default sudokuSolver;
export { sudokuSolver };
