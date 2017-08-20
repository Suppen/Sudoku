"use strict";

/**************************
 * Import important stuff *
 **************************/

import _ from "lodash";

/***********
 * Helpers *
 ***********/

/**
 * Extracts the 'value' field from an object. For use as a mapping function
 *
 * @private
 */
function extractValue({value}) {return value;}

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
 * @throws {Error}	If the board is unsolvable
 */
function sudokuSolver(board, startIndex = 0) {
	// Check if the board is solved
	if (board.isSolved()) {
		return board;
	} else if (startIndex === 0 && board.getErrors().length > 0) {
		throw new Error("Board is unsolvable");
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
		_.map(board.getRow(cell.row), extractValue),
		_.map(board.getCol(cell.col), extractValue),
		_.map(board.getSquare(cell.square), extractValue)
	);

	while (possibleValues.length > 0 && solvedBoard === null) {
		// Set the cell to the next value
		board.setCellValue(cellIndex, possibleValues.pop());

		try {
			// Recurse down to solve next cell
			solvedBoard = sudokuSolver(board.clone(), cellIndex+1);
		} catch (err) {
			// There were no solutions with this number. Do nothing
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
