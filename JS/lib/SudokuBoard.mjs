"use strict";

/**************************
 * Import important stuff *
 **************************/

import _ from "lodash";
import { LengthError } from "./errors/LengthError";

/***********************
 * Create some helpers *
 ***********************/

/**
 * Map between row numbers and the cells in that row
 *
 * @private
 */
const ROW_TO_CELL_INDICES = new Map();
ROW_TO_CELL_INDICES.set(1, [0, 1, 2, 3, 4, 5, 6, 7, 8]);
ROW_TO_CELL_INDICES.set(2, [9, 10, 11, 12, 13, 14, 15, 16, 17]);
ROW_TO_CELL_INDICES.set(3, [18, 19, 20, 21, 22, 23, 24, 25, 26]);
ROW_TO_CELL_INDICES.set(4, [27, 28, 29, 30, 31, 32, 33, 34, 35]);
ROW_TO_CELL_INDICES.set(5, [36, 37, 38, 39, 40, 41, 42, 43, 44]);
ROW_TO_CELL_INDICES.set(6, [45, 46, 47, 48, 49, 50, 51, 52, 53]);
ROW_TO_CELL_INDICES.set(7, [54, 55, 56, 57, 58, 59, 60, 61, 62]);
ROW_TO_CELL_INDICES.set(8, [63, 64, 65, 66, 67, 68, 69, 70, 71]);
ROW_TO_CELL_INDICES.set(9, [72, 73, 74, 75, 76, 77, 78, 79, 80]);

/**
 * Map between column numbers and the cells in that column
 *
 * @private
 */
const COL_TO_CELL_INDICES = new Map();
COL_TO_CELL_INDICES.set(1, [0, 9, 18, 27, 36, 45, 54, 63, 72]);
COL_TO_CELL_INDICES.set(2, [1, 10, 19, 28, 37, 46, 55, 64, 73]);
COL_TO_CELL_INDICES.set(3, [2, 11, 20, 29, 38, 47, 56, 65, 74]);
COL_TO_CELL_INDICES.set(4, [3, 12, 21, 30, 39, 48, 57, 66, 75]);
COL_TO_CELL_INDICES.set(5, [4, 13, 22, 31, 40, 49, 58, 67, 76]);
COL_TO_CELL_INDICES.set(6, [5, 14, 23, 32, 41, 50, 59, 68, 77]);
COL_TO_CELL_INDICES.set(7, [6, 15, 24, 33, 42, 51, 60, 69, 78]);
COL_TO_CELL_INDICES.set(8, [7, 16, 25, 34, 43, 52, 61, 70, 79]);
COL_TO_CELL_INDICES.set(9, [8, 17, 26, 35, 44, 53, 62, 71, 80]);

/**
 * Map between square numbers and the cells in that square
 *
 * @private
 */
const SQUARE_TO_CELL_INDICES = new Map();
SQUARE_TO_CELL_INDICES.set(1, [0, 1, 2, 9, 10, 11, 18, 19, 20]);
SQUARE_TO_CELL_INDICES.set(2, [3, 4, 5, 12, 13, 14, 21, 22, 23]);
SQUARE_TO_CELL_INDICES.set(3, [6, 7, 8, 15, 16, 17, 24, 25, 26]);
SQUARE_TO_CELL_INDICES.set(4, [27, 28, 29, 36, 37, 38, 45, 46, 47]);
SQUARE_TO_CELL_INDICES.set(5, [30, 31, 32, 39, 40, 41, 48, 49, 50]);
SQUARE_TO_CELL_INDICES.set(6, [33, 34, 35, 42, 43, 44, 51, 52, 53]);
SQUARE_TO_CELL_INDICES.set(7, [54, 55, 56, 63, 64, 65, 72, 73, 74]);
SQUARE_TO_CELL_INDICES.set(8, [57, 58, 59, 66, 67, 68, 75, 76, 77]);
SQUARE_TO_CELL_INDICES.set(9, [60, 61, 62, 69, 70, 71, 78, 79, 80]);

/**
 * Map between board indices and cells
 *
 * @private
 */
const INDEX_TO_CELL_TEMPLATE = new Map(JSON.parse('[[0,{"row":1,"index":0,"col":1,"square":1}],[1,{"row":1,"index":1,"col":2,"square":1}],[2,{"row":1,"index":2,"col":3,"square":1}],[3,{"row":1,"index":3,"col":4,"square":2}],[4,{"row":1,"index":4,"col":5,"square":2}],[5,{"row":1,"index":5,"col":6,"square":2}],[6,{"row":1,"index":6,"col":7,"square":3}],[7,{"row":1,"index":7,"col":8,"square":3}],[8,{"row":1,"index":8,"col":9,"square":3}],[9,{"row":2,"index":9,"col":1,"square":1}],[10,{"row":2,"index":10,"col":2,"square":1}],[11,{"row":2,"index":11,"col":3,"square":1}],[12,{"row":2,"index":12,"col":4,"square":2}],[13,{"row":2,"index":13,"col":5,"square":2}],[14,{"row":2,"index":14,"col":6,"square":2}],[15,{"row":2,"index":15,"col":7,"square":3}],[16,{"row":2,"index":16,"col":8,"square":3}],[17,{"row":2,"index":17,"col":9,"square":3}],[18,{"row":3,"index":18,"col":1,"square":1}],[19,{"row":3,"index":19,"col":2,"square":1}],[20,{"row":3,"index":20,"col":3,"square":1}],[21,{"row":3,"index":21,"col":4,"square":2}],[22,{"row":3,"index":22,"col":5,"square":2}],[23,{"row":3,"index":23,"col":6,"square":2}],[24,{"row":3,"index":24,"col":7,"square":3}],[25,{"row":3,"index":25,"col":8,"square":3}],[26,{"row":3,"index":26,"col":9,"square":3}],[27,{"row":4,"index":27,"col":1,"square":4}],[28,{"row":4,"index":28,"col":2,"square":4}],[29,{"row":4,"index":29,"col":3,"square":4}],[30,{"row":4,"index":30,"col":4,"square":5}],[31,{"row":4,"index":31,"col":5,"square":5}],[32,{"row":4,"index":32,"col":6,"square":5}],[33,{"row":4,"index":33,"col":7,"square":6}],[34,{"row":4,"index":34,"col":8,"square":6}],[35,{"row":4,"index":35,"col":9,"square":6}],[36,{"row":5,"index":36,"col":1,"square":4}],[37,{"row":5,"index":37,"col":2,"square":4}],[38,{"row":5,"index":38,"col":3,"square":4}],[39,{"row":5,"index":39,"col":4,"square":5}],[40,{"row":5,"index":40,"col":5,"square":5}],[41,{"row":5,"index":41,"col":6,"square":5}],[42,{"row":5,"index":42,"col":7,"square":6}],[43,{"row":5,"index":43,"col":8,"square":6}],[44,{"row":5,"index":44,"col":9,"square":6}],[45,{"row":6,"index":45,"col":1,"square":4}],[46,{"row":6,"index":46,"col":2,"square":4}],[47,{"row":6,"index":47,"col":3,"square":4}],[48,{"row":6,"index":48,"col":4,"square":5}],[49,{"row":6,"index":49,"col":5,"square":5}],[50,{"row":6,"index":50,"col":6,"square":5}],[51,{"row":6,"index":51,"col":7,"square":6}],[52,{"row":6,"index":52,"col":8,"square":6}],[53,{"row":6,"index":53,"col":9,"square":6}],[54,{"row":7,"index":54,"col":1,"square":7}],[55,{"row":7,"index":55,"col":2,"square":7}],[56,{"row":7,"index":56,"col":3,"square":7}],[57,{"row":7,"index":57,"col":4,"square":8}],[58,{"row":7,"index":58,"col":5,"square":8}],[59,{"row":7,"index":59,"col":6,"square":8}],[60,{"row":7,"index":60,"col":7,"square":9}],[61,{"row":7,"index":61,"col":8,"square":9}],[62,{"row":7,"index":62,"col":9,"square":9}],[63,{"row":8,"index":63,"col":1,"square":7}],[64,{"row":8,"index":64,"col":2,"square":7}],[65,{"row":8,"index":65,"col":3,"square":7}],[66,{"row":8,"index":66,"col":4,"square":8}],[67,{"row":8,"index":67,"col":5,"square":8}],[68,{"row":8,"index":68,"col":6,"square":8}],[69,{"row":8,"index":69,"col":7,"square":9}],[70,{"row":8,"index":70,"col":8,"square":9}],[71,{"row":8,"index":71,"col":9,"square":9}],[72,{"row":9,"index":72,"col":1,"square":7}],[73,{"row":9,"index":73,"col":2,"square":7}],[74,{"row":9,"index":74,"col":3,"square":7}],[75,{"row":9,"index":75,"col":4,"square":8}],[76,{"row":9,"index":76,"col":5,"square":8}],[77,{"row":9,"index":77,"col":6,"square":8}],[78,{"row":9,"index":78,"col":7,"square":9}],[79,{"row":9,"index":79,"col":8,"square":9}],[80,{"row":9,"index":80,"col":9,"square":9}]]'));

/********************
 * Create the class *
 ********************/

/**
 * A standard 9x9 sudoku board
 */
class SudokuBoard {
	/**
	 * Creates a new sudoku board
	 *
	 * @param {Integer[]} [boardArray]	An array representation of a sudoku board. Indices are left to right, top to bottom:
	 * <pre>
	 * 0 1 2 ...
	 * 9 10 11 ...
	 * ...
	 * ... 69 70 71
	 * ... 78 79 80
	 * </pre>
	 * The array must have a length 0f {@link SudokuBoard.CELL_COUNT} (81). If not set, an empty board will be created
	 *
	 * @throws {TypeError}	If the boardStr argument is not a string
	 * @throws {LengthError}	If the boardStr does not have a length of 81 ({@link SudokuBoard.CELL_COUNT})
	 */
	constructor(boardArray = new Array(SudokuBoard.CELL_COUNT).fill(SudokuBoard.EMPTY_VALUE)) {
		// Verify that the board array is actually an array
		if (!(boardArray instanceof Array)) {
			throw new TypeError("boardArray must be an array");
		}

		// Verify that the board array has a langth og SudokuBoard.CELL_COUNT
		if (boardArray.length !== SudokuBoard.CELL_COUNT) {
			throw new LengthError(`boardArray must have a length of ${SudokuBoard.CELL_COUNT}`);
		}

		/**
		 * The internal representation of the sudoku board
		 *
		 * @private
		 *
		 * @type {Integer[]}
		 */
		this._board = _.chain(boardArray)
		  .map((value) => {
			// Default to empty
			let properValue = SudokuBoard.EMPTY_VALUE;

			// Check if this is a valid value
			if (Number.isInteger(value) && value >= 1 && value <= 9) {
				properValue = value;
			}

			return properValue;
		  })
		  .value();
	}

	// Helpers for JSDoc
	/**
	 * @typedef {Object} Cell
	 *
	 * @description Full information about a cell
	 *
	 * @property {Integer} row	Row number of the cell
	 * @property {Integer} col	Column number of the cell
	 * @property {Integer} square	Square number of the cell
	 * @property {Integer} index	Board index of the cell
	 * @property {Integer} value	Value of the cell
	 *
	 * @memberof SudokuBoard
	 */

	/**
	 * Iterator for the board's cells. Iteration goes left to right, top to bottom, yielding objects with the cells' row, column, square, index and value
	 */
	[Symbol.iterator]() {
		return (function* (board) {
			for (let i = 0; i < SudokuBoard.CELL_COUNT; i++) {
				yield board.getCell(i);
			}
		}(this));
	}

	/**
	 * Gets info about a cell
	 *
	 * @param {Integer} index	Index of the cell to get info for. An integer in the range 0 - {@link SudokuBoard.CELL_COUNT}-1. Cells are ordered left to right, top to bottom
	 *
	 * @returns {Cell}	The info about the cell
	 */
	getCell(index) {
		return _.assign(_.clone(INDEX_TO_CELL_TEMPLATE.get(index)), {value: this._board[index]});
	}

	/**
	 * Gets info about multiple cells
	 *
	 * @param {Integer[]} indices	Array of the cell indices to get cells for. Indices are in the range 0 - {@link SudokuBoard.CELL_COUNT}-1. Cells are ordered left to right, top to bottom
	 *
	 * @returns {Cell[]}	The array of numbers from the cells of the given indices
	 */
	getCells(indices) {
		return indices.map((index) => this.getCell(index));
	}

	/**
	 * Sets the value of a cell. The cells are numbered left to right, top to bottom
	 *
	 * @param {Integer} index	Index of the cell to set. An integer in the range 0 - {@link SudokuBoard.CELL_COUNT}-1
	 * @param {Integer} newVal	New value to set in the cell. An integer in the range 1-9, or {@link SudokuBoard.EMPTY_VALUE} to unset
	 *
	 * @returns {Cell[]}	A list of errors introduced by the new value
	 *
	 * @throws {TypeError}	If the index or new value is not an integer
	 * @throws {RangeError}	If the index is not in the range 0 - {@link SudokuBoard.CELL_COUNT}-1, or the new value is not in the range 1-9
	 */
	setCellValue(index, newVal) {
		// Verify that the index is an integer
		if (!Number.isInteger(index)) {
			throw new TypeError("Index must be an integer in the range 0 - SudokuBoard.CELL_COUNT-1");
		}

		// Verify that the index is within the range 0 - SudokuBoard.CELL_COUNT-1
		if (index < 0 || index >= SudokuBoard.CELL_COUNT) {
			throw new RangeError("Index must be an integer in the range 0 - SudokuBoard.CELL_COUNT-1");
		}

		// SudokuBoard.EMPTY_VALUE is a valid value
		if (newVal !== SudokuBoard.EMPTY_VALUE) {

			// Verify that the new value is an integer
			if (!Number.isInteger(newVal)) {
				throw new TypeError("The new value must be an integer in the range 1-9, or SudokuBoard.EMPTY_VALUE");
			}

			// Verify that the value is in the range 1-9, or is SudokuBoard.EMPTY_VALUE
			if (newVal < 1 || newVal > 9) {
				throw new RangeError("The new value must be an integer in the range 1-9, or SudokuBoard.EMPTY_VALUE");
			}
		}

		// Set the value
		this._board[index] = newVal;

		// Check if the change made any errors on the board
		let cell = INDEX_TO_CELL_TEMPLATE.get(index);

		// Find and return the errors
		return _.chain(this._getErrors(cell.row, cell.col, cell.square))
		  .filter(({value}) => value === newVal)	// Only the errors created by this number
		  .uniqBy(({index}) => index)
		  .value();
	}

	/**
	 * Checks a bunch of cells for errors (duplicates)
	 *
	 * @param {Integer} rowNum	Row number to check for errors. Integer in the range 1-9
	 * @param {Integer} colNum	Column number to check for errors. Integer in the range 1-9
	 * @param {Integer} squareNum	Square number to check for errors. Integer in the range 1-9
	 *
	 * @returns {Cell[]}	A list of cells with errors on the board. There will be duplicates
	 *
	 * @private
	 */
	_getErrors(rowNum, colNum, squareNum) {
		return _.chain([this.getRow(rowNum), this.getCol(colNum), this.getSquare(squareNum)])
		  .map((cells) => {
			return _.chain(cells)
			  .filter(({value}) => value !== SudokuBoard.EMPTY_VALUE)	// Ignore empty cells
			  .reduce((dupes, cellToCheck, i, arr) => {
				// Count the number of occurences of this number in the array
				let count = _.reduce(arr, (count, cellBeingChecked) => {
					if (cellBeingChecked.value === cellToCheck.value) {
						count++;
					}
					return count;
				}, 0);

				// Check if there are more than one of this number
				if (count > 1) {
					// Get the cell's info and put it in the dupes array
					dupes.push(this.getCell(cellToCheck.index));
				}

				// Pass on the dupe array
				return dupes;
			  }, [])
			  .value();
		  })
		  .flatten()
		  .value();
	}

	/**
	 * Gets the values of a row in the sudoku board. Row numbering:
	 * <pre>
	 * 1
	 * 2
	 * 3
	 * ...
	 * </pre>
	 * The cells are ordered left to right
	 *
	 * @param {Integer} num	Row number. An integer in the range 1-9
	 *
	 * @returns {Cell[]}	The row in the sudoku board
	 *
	 * @readonly
	 */
	getRow(num) {
		return this.getCells(ROW_TO_CELL_INDICES.get(num));
	}

	/**
	 * Gets a column in the sudoku board. Column numbering:
	 * <pre>
	 * 1 2 3 ...
	 * </pre>
	 * The cells are ordered top to bottom
	 *
	 * @param {Integer} num	Column number. An integer in the range 1-9
	 *
	 * @returns {Cell[]}	The column in the sudoku board
	 *
	 * @readonly
	 */
	getCol(num) {
		return this.getCells(COL_TO_CELL_INDICES.get(num));
	}

	/**
	 * Gets a square in the sudoku board. Square numbering:\n
	 * <pre>
	 * 1 2 3
	 * 4 5 6
	 * 7 8 9
	 * </pre>
	 * The cells are ordered left to right, top to bottom
	 *
	 * @param {Integer} num	Square number. An integer in the range 1-9
	 *
	 * @returns {Cell[]}	The column in the sudoku board
	 *
	 * @readonly
	 */
	getSquare(num) {
		return this.getCells(SQUARE_TO_CELL_INDICES.get(num));
	}

	/**
	 * Converts the board to a boardStr, which can again be used to construct a sudoku board
	 *
	 * @returns {String}	A string representing the sudoku board
	 */
	toString() {
		return this._board.join("");
	}

	/**
	 * Creates a list of all cells which currently have errors
	 *
	 * @returns {Cell[]}	All cells which have errors
	 */
	getErrors() {
		// Handle for the errors
		let errors = [];

		// Find the errors in every row, column and square
		for (let i = 1; i <= 9; i++) {
			errors = _.concat(
				errors,
				this._getErrors(i, i, i)
			);
		}

		// Remove dupes
		errors = _.uniqBy(errors, ({index}) => index);

		// All errors found
		return errors;
	}

	/**
	 * Creates a list of currently filled cells
	 *
	 * @returns {Cell[]}	A list of currently filled cells
	 */
	getFilledCells() {
		// The list of filled cells
		let filledCells = [];

		// Iterate over all the cells to find filled ones
		for (let cell of this) {
			if (cell.value !== SudokuBoard.EMPTY_VALUE) {
				filledCells.push(cell);
			}
		}

		// All done!
		return filledCells;
	}

	/**
	 * Checks whether or not the board is solved
	 *
	 * @returns {Boolean}	True if the board is solved, false otherwise
	 */
	isSolved() {
		// The board is solved if all cells are filled and there are no errors
		return this.getFilledCells().length === SudokuBoard.CELL_COUNT && this.getErrors().length === 0;
	}

	/**
	 * Displays a grid in the console. For debugging purposes
	 */
	displayGrid() {
		let grid = "";
		for (let i = 1; i <= 9; i++) {
			let row = this.getRow(i);
			grid += `${row.slice(0, 3).map(({value}) => value).join("")}  ${row.slice(3, 6).map(({value}) => value).join("")}  ${row.slice(6, 9).map(({value}) => value).join("")}\n`;

			if (i % 3 === 0 && i !== 9) {
				grid += "\n";
			}
		}
		grid = grid.replace(new RegExp(SudokuBoard.EMPTY_VALUE, "g"), "_");

		console.log(grid);
	}

	/**
	 * Creates an array from the board
	 *
	 * @return {Integer[]}	The board as an integer array
	 */
	toArray() {
		return _.clone(this._board);
	}

	/**
	 * Clones the sudoku board. This is slow
	 *
	 * @returns {SudokuBoard}	A clone of this sudoku board
	 */
	clone() {
		return new SudokuBoard(this.toArray());
	}

	/**
	 * Checks if this sudoku board is equal to another
	 *
	 * @param {SudokuBoard} board	The board to compare this to
	 *
	 * @returns {Boolean}	True if the boards are equal, false otherwise
	 *
	 * @throws {TypeError}	If something other than a SudokuBoard was given
	 */
	equals(board) {
		// Verify that the board is actually a SudokuBoard
		if (!(board instanceof SudokuBoard)) {
			throw new TypeError("board must be an instance of SudokuBoard");
		}

		// Handle for the result
		let areEqual = true;

		// Get iterators for both boards
		let thisIter = this[Symbol.iterator]();
		let boardIter = board[Symbol.iterator]();

		// Compare them
		let done = false;
		do {
			// Get next iteration of both boards
			let a = thisIter.next();
			let b = boardIter.next();

			// Compare them and check if the iterator is done
			areEqual = _.isEqual(a.value, b.value);
			done = a.done;
		} while (areEqual && !done);

		// All done!
		return areEqual;
	}

	/**
	 * The value representing an empty cell. Has the value 0
	 *
	 * @type Integer
	 *
	 * @constant
	 */
	static get EMPTY_VALUE() {
		return 0;
	}

	/**
	 * Number of cells on a board. Has the value 81
	 *
	 * @type Integer
	 *
	 * @constant
	 */
	static get CELL_COUNT() {
		return 81;
	}
}

/********************
 * Export the class *
 ********************/

export default SudokuBoard;
export { SudokuBoard };
