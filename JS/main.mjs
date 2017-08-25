"use strict";

/**************************
 * Import important stuff *
 **************************/

import { SudokuBoard } from "./lib/SudokuBoard";
import { sudokuSolver } from "./lib/sudokuSolver";

/************
 * Do stuff *
 ************/

// Create the board
let board = new SudokuBoard(`
	53_ _7_ ___
	6__ 195 ___
	_98 ___ _6_

	8__ _6_ __3
	4__ 8_3 __1
	7__ _2_ __6

	_6_ ___ 28_
	___ 419 __5
	___ _8_ _79
  `
  .replace(/\s/g, "")
  .split("")
  .map((cell) => Number.parseInt(cell)));

// Print it
console.log("Initial board:");
board.displayGrid();
console.log();

// Solve it!
console.time("Solved in");
board = sudokuSolver(board);
console.timeEnd("Solved in");

// Display the result
console.log();
console.log("Result:");
board.displayGrid();
