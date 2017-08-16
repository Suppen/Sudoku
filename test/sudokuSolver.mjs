"use strict";

/**************************
 * Import important stuff *
 **************************/

import chai from "chai";
import _ from "lodash";

import { SudokuBoard } from "../lib/SudokuBoard";
import { sudokuSolver } from "../lib/sudokuSolver";

/***********
 * Helpers *
 ***********/

const PARTIALLY_FILLED_BOARD_ARRAY = `
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
	  .map((cell) => Number.parseInt(cell));

const SOLVED_BOARD_ARRAY = `
		534 678 912
		672 195 348
		198 342 567

		859 761 423
		426 853 791
		713 924 856

		961 537 284
		287 419 635
		345 286 179
	  `
	  .replace(/\s/g, "")
	  .split("")
	  .map((cell) => Number.parseInt(cell));

const {assert, expect, should} = chai;

/******************
 * Do the testing *
 ******************/

describe("Sudoku Solver", function () {
	// Create the initial board
	const board = new SudokuBoard(PARTIALLY_FILLED_BOARD_ARRAY);

	// Create the solved board
	const correctSolution = new SudokuBoard(SOLVED_BOARD_ARRAY);

	it("should solve the board", function () {
		// This takes some time
		this.slow(500);

		// Do the solving
		const solution = sudokuSolver(board);

		// Verify that the solution is correct
		expect(solution.equals(correctSolution)).to.be.true;
	});
});
