"use strict";

/**************************
 * Import important stuff *
 **************************/

import chai from "chai";
import _ from "lodash";

import { SudokuBoard } from "../lib/SudokuBoard";
import { LengthError } from "../lib/errors/LengthError";

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

const CENTER_CELL_INDEX = 40;

/******************
 * Do the testing *
 ******************/

const {assert, expect, should} = chai;

describe("Constructing a SudokuBoard", function () {
	it("should throw TypeError if given an object", function () {
		expect(() => new SudokuBoard({})).to.throw(TypeError);
	});

	it("should throw LengthError if given an array of length other than SudokuBoard.CELL_COUNT", function () {
		expect(() => new SudokuBoard([])).to.throw(LengthError);
		expect(() => new SudokuBoard([..."adyfuyt4e78vgedrgr"])).to.throw(LengthError);
		expect(() => new SudokuBoard([..."qayumv9bropawys5btmawp<rabesrgjaesorifbtfyhujbrdopsunfspdiogsreiotujgwyruvbsrfvmcqwasoruhreijtfjwoq yrh fd"])).to.throw(LengthError);
	});

	it("should work when given no argument", function () {
		expect(new SudokuBoard()).to.be.an.instanceof(SudokuBoard);
	});

	it("should work when given an array of length SudokuBoard.CELL_COUNT", function () {
		// Create a string with length of SudokuBoard.CELL_COUNT
		let boardStr = new Array(SudokuBoard.CELL_COUNT).fill("1");

		expect(new SudokuBoard(boardStr)).to.be.an.instanceof(SudokuBoard);
	});
});

describe("Empty SudokuBoard", function () {
	// Create the empty board
	const board = new SudokuBoard();

	// Test it
	it("should be empty", function () {
		expect(board.filledCells).to.have.lengthOf(0);
	});

	it("should have no errors", function () {
		expect(board.errors).to.have.lengthOf(0);
	});

	it("should not be solved", function () {
		expect(board.solved).to.be.false;
	});
});

describe("Partially filled SudokuBoard", function () {
	describe("Initial state", function () {
		// Create the initial board
		const board = new SudokuBoard(PARTIALLY_FILLED_BOARD_ARRAY);

		// Test it
		it("should have no errors", function () {
			expect(board.errors).to.have.lengthOf(0);
		});

		it("should have 30 filled cells", function () {
			expect(board.filledCells).to.have.lengthOf(30);
		});

		it("should not be solved", function () {
			expect(board.solved).to.be.false;
		});
	});

	describe("Modifying the board", function () {
		describe("Illegal modifications", function () {
			// Create the initial board
			const board = new SudokuBoard(PARTIALLY_FILLED_BOARD_ARRAY);

			it("should throw a TypeError when given a non-integer index", function () {
				expect(() => board.setCellValue("", 5)).to.throw(TypeError);
				expect(() => board.setCellValue([], 5)).to.throw(TypeError);
				expect(() => board.setCellValue({}, 5)).to.throw(TypeError);
				expect(() => board.setCellValue(Symbol(), 5)).to.throw(TypeError);
				expect(() => board.setCellValue(5.3, 5)).to.throw(TypeError);
				expect(() => board.setCellValue(() => {}, 5)).to.throw(TypeError);
			});

			it("should throw a RangeError when given an index outside the range 0 - SudokuBoard.CELL_COUNT-1", function () {
				expect(() => board.setCellValue(-1, 5)).to.throw(RangeError);
				expect(() => board.setCellValue(SudokuBoard.CELL_COUNT, 5)).to.throw(RangeError);
			});

			it("should throw a TypeError when given a non-integer (and not SudokuBoard.EMPTY_VALUE) value", function () {
				expect(() => board.setCellValue(CENTER_CELL_INDEX, "")).to.throw(TypeError);
				expect(() => board.setCellValue(CENTER_CELL_INDEX, [])).to.throw(TypeError);
				expect(() => board.setCellValue(CENTER_CELL_INDEX, {})).to.throw(TypeError);
				expect(() => board.setCellValue(CENTER_CELL_INDEX, Symbol())).to.throw(TypeError);
				expect(() => board.setCellValue(CENTER_CELL_INDEX, 5.3)).to.throw(TypeError);
				expect(() => board.setCellValue(CENTER_CELL_INDEX, () => {})).to.throw(TypeError);
			});

			it("should throw a RangeError when given a value outside the range 1-9 or SudokuBoard.EMPTY_VALUE", function () {
				expect(() => board.setCellValue(CENTER_CELL_INDEX, 10)).to.throw(RangeError);
				expect(() => board.setCellValue(CENTER_CELL_INDEX, 50)).to.throw(RangeError);
				expect(() => board.setCellValue(CENTER_CELL_INDEX, -8)).to.throw(RangeError);
			});
		});

		describe("Legal modifications", function () {
			describe("Setting a non-colliding value", function () {
				// Create the initial board
				const board = new SudokuBoard(PARTIALLY_FILLED_BOARD_ARRAY);

				// Store the number of filled cells
				const filledCells = board.filledCells.length;

				it("should work and return an empty array when putting a 5 in the center cell", function () {
					let errors = board.setCellValue(CENTER_CELL_INDEX, 5);	// This permanently modifies the board
					expect(errors).to.be.instanceOf(Array);
					expect(errors).to.have.lengthOf(0);
				});

				it("should still not have any errors", function () {
					expect(board.errors).to.have.lengthOf(0);
				});

				it("should increase the number of filled cells by 1", function () {
					expect(board.filledCells.length).to.equal(filledCells + 1);
				});
			});

			describe("Setting a colliding value", function () {
				// Create the initial board
				const board = new SudokuBoard(PARTIALLY_FILLED_BOARD_ARRAY);

				// Store the number of filled cells
				const filledCells = board.filledCells.length;

				it("should work, but return a non-empty array when putting a 3 in the center cell", function () {
					let errors = board.setCellValue(CENTER_CELL_INDEX, 3);
					expect(errors).to.be.instanceOf(Array);
					expect(errors).to.not.have.lengthOf(0);
				});

				it("should now have errors", function () {
					expect(board.errors).to.not.have.lengthOf(0);
				});

				it("should increase the number of filled cells by 1", function () {
					expect(board.filledCells.length).to.equal(filledCells + 1);
				});

				describe("Removing the value again", function () {
					it("should work to unset the center cell, and it should return an empty array", function () {
						let errors = board.setCellValue(CENTER_CELL_INDEX, SudokuBoard.EMPTY_VALUE);
						expect(errors).to.have.lengthOf(0);
					});

					it("should empty the error array again", function () {
						expect(board.errors).to.have.lengthOf(0);
					});

					it("should bring the number of filled fields back down", function () {
						expect(board.filledCells.length).to.equal(filledCells);
					});
				});
			});
		});
	});
});

describe("Solved SudokuBoard", function () {
	// Create the solved board
	const board = new SudokuBoard(SOLVED_BOARD_ARRAY);

	// Test it
	it("should have no errors", function () {
		expect(board.errors).to.have.lengthOf(0);
	});

	it("should be filled", function () {
		expect(board.filledCells).to.have.lengthOf(SudokuBoard.CELL_COUNT);
	});

	it("should be solved", function () {
		expect(board.solved).to.be.true;
	});
});

describe("Filled SudokuBoard with errors", function () {
	// Create the solved board
	const board = new SudokuBoard(SOLVED_BOARD_ARRAY);

	// Change the center cell
	board.setCellValue(CENTER_CELL_INDEX, 6);

	// Test it
	it("should have errors", function () {
		expect(board.errors).to.not.have.lengthOf(0);
	});

	it("should be filled", function () {
		expect(board.filledCells).to.have.lengthOf(SudokuBoard.CELL_COUNT);
	});

	it("should not be solved", function () {
		expect(board.solved).to.be.false;
	});
});

describe("Cloning a sudoku board", function () {
	it("should equal the original board", function () {
		// Create the initial board
		const board = new SudokuBoard(PARTIALLY_FILLED_BOARD_ARRAY);

		// Clone it
		const clone = board.clone();

		// Verify that they are equal by turning them into arrays and comparing each cell
		const origCells = [...board];
		const clonedCells = [...clone];
		for (let i = 0; i < SudokuBoard.CELL_COUNT; i++) {
			expect(origCells[i]).to.deep.equal(clonedCells[i]);
		}
	});
});
