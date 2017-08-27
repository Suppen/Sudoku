################################
# Define the SudokuBoard class #
################################

class SudokuBoard():
    """A standard 9x9 Sudoku board"""

    CELL_COUNT = 81

    EMPTY_VALUE = 0;

    _ROW_TO_CELL_INDICES = {
        1: [0, 1, 2, 3, 4, 5, 6, 7, 8],
        2: [9, 10, 11, 12, 13, 14, 15, 16, 17],
        3: [18, 19, 20, 21, 22, 23, 24, 25, 26],
        4: [27, 28, 29, 30, 31, 32, 33, 34, 35],
        5: [36, 37, 38, 39, 40, 41, 42, 43, 44],
        6: [45, 46, 47, 48, 49, 50, 51, 52, 53],
        7: [54, 55, 56, 57, 58, 59, 60, 61, 62],
        8: [63, 64, 65, 66, 67, 68, 69, 70, 71],
        9: [72, 73, 74, 75, 76, 77, 78, 79, 80]
    }

    _COL_TO_CELL_INDICES = {
        1: [0, 9, 18, 27, 36, 45, 54, 63, 72],
        2: [1, 10, 19, 28, 37, 46, 55, 64, 73],
        3: [2, 11, 20, 29, 38, 47, 56, 65, 74],
        4: [3, 12, 21, 30, 39, 48, 57, 66, 75],
        5: [4, 13, 22, 31, 40, 49, 58, 67, 76],
        6: [5, 14, 23, 32, 41, 50, 59, 68, 77],
        7: [6, 15, 24, 33, 42, 51, 60, 69, 78],
        8: [7, 16, 25, 34, 43, 52, 61, 70, 79],
        9: [8, 17, 26, 35, 44, 53, 62, 71, 80]
    }

    _SQUARE_TO_CELL_INDICES = {
        1: [0, 1, 2, 9, 10, 11, 18, 19, 20],
        2: [3, 4, 5, 12, 13, 14, 21, 22, 23],
        3: [6, 7, 8, 15, 16, 17, 24, 25, 26],
        4: [27, 28, 29, 36, 37, 38, 45, 46, 47],
        5: [30, 31, 32, 39, 40, 41, 48, 49, 50],
        6: [33, 34, 35, 42, 43, 44, 51, 52, 53],
        7: [54, 55, 56, 63, 64, 65, 72, 73, 74],
        8: [57, 58, 59, 66, 67, 68, 75, 76, 77],
        9: [60, 61, 62, 69, 70, 71, 78, 79, 80]
    }

    _INDEX_TO_CELL_TEMPLATE = {
        0: {"row": 1, "index": 0, "col": 1, "square": 1},
	1: {"row": 1, "index": 1, "col": 2, "square": 1},
	2: {"row": 1, "index": 2, "col": 3, "square": 1},
	3: {"row": 1, "index": 3, "col": 4, "square": 2},
	4: {"row": 1, "index": 4, "col": 5, "square": 2},
	5: {"row": 1, "index": 5, "col": 6, "square": 2},
	6: {"row": 1, "index": 6, "col": 7, "square": 3},
	7: {"row": 1, "index": 7, "col": 8, "square": 3},
	8: {"row": 1, "index": 8, "col": 9, "square": 3},
	9: {"row": 2, "index": 9, "col": 1, "square": 1},
	10: {"row": 2, "index": 10, "col": 2, "square": 1},
	11: {"row": 2, "index": 11, "col": 3, "square": 1},
	12: {"row": 2, "index": 12, "col": 4, "square": 2},
	13: {"row": 2, "index": 13, "col": 5, "square": 2},
	14: {"row": 2, "index": 14, "col": 6, "square": 2},
	15: {"row": 2, "index": 15, "col": 7, "square": 3},
	16: {"row": 2, "index": 16, "col": 8, "square": 3},
	17: {"row": 2, "index": 17, "col": 9, "square": 3},
	18: {"row": 3, "index": 18, "col": 1, "square": 1},
	19: {"row": 3, "index": 19, "col": 2, "square": 1},
	20: {"row": 3, "index": 20, "col": 3, "square": 1},
	21: {"row": 3, "index": 21, "col": 4, "square": 2},
	22: {"row": 3, "index": 22, "col": 5, "square": 2},
	23: {"row": 3, "index": 23, "col": 6, "square": 2},
	24: {"row": 3, "index": 24, "col": 7, "square": 3},
	25: {"row": 3, "index": 25, "col": 8, "square": 3},
	26: {"row": 3, "index": 26, "col": 9, "square": 3},
	27: {"row": 4, "index": 27, "col": 1, "square": 4},
	28: {"row": 4, "index": 28, "col": 2, "square": 4},
	29: {"row": 4, "index": 29, "col": 3, "square": 4},
	30: {"row": 4, "index": 30, "col": 4, "square": 5},
	31: {"row": 4, "index": 31, "col": 5, "square": 5},
	32: {"row": 4, "index": 32, "col": 6, "square": 5},
	33: {"row": 4, "index": 33, "col": 7, "square": 6},
	34: {"row": 4, "index": 34, "col": 8, "square": 6},
	35: {"row": 4, "index": 35, "col": 9, "square": 6},
	36: {"row": 5, "index": 36, "col": 1, "square": 4},
	37: {"row": 5, "index": 37, "col": 2, "square": 4},
	38: {"row": 5, "index": 38, "col": 3, "square": 4},
	39: {"row": 5, "index": 39, "col": 4, "square": 5},
	40: {"row": 5, "index": 40, "col": 5, "square": 5},
	41: {"row": 5, "index": 41, "col": 6, "square": 5},
	42: {"row": 5, "index": 42, "col": 7, "square": 6},
	43: {"row": 5, "index": 43, "col": 8, "square": 6},
	44: {"row": 5, "index": 44, "col": 9, "square": 6},
	45: {"row": 6, "index": 45, "col": 1, "square": 4},
	46: {"row": 6, "index": 46, "col": 2, "square": 4},
	47: {"row": 6, "index": 47, "col": 3, "square": 4},
	48: {"row": 6, "index": 48, "col": 4, "square": 5},
	49: {"row": 6, "index": 49, "col": 5, "square": 5},
	50: {"row": 6, "index": 50, "col": 6, "square": 5},
	51: {"row": 6, "index": 51, "col": 7, "square": 6},
	52: {"row": 6, "index": 52, "col": 8, "square": 6},
	53: {"row": 6, "index": 53, "col": 9, "square": 6},
	54: {"row": 7, "index": 54, "col": 1, "square": 7},
	55: {"row": 7, "index": 55, "col": 2, "square": 7},
	56: {"row": 7, "index": 56, "col": 3, "square": 7},
	57: {"row": 7, "index": 57, "col": 4, "square": 8},
	58: {"row": 7, "index": 58, "col": 5, "square": 8},
	59: {"row": 7, "index": 59, "col": 6, "square": 8},
	60: {"row": 7, "index": 60, "col": 7, "square": 9},
	61: {"row": 7, "index": 61, "col": 8, "square": 9},
	62: {"row": 7, "index": 62, "col": 9, "square": 9},
	63: {"row": 8, "index": 63, "col": 1, "square": 7},
	64: {"row": 8, "index": 64, "col": 2, "square": 7},
	65: {"row": 8, "index": 65, "col": 3, "square": 7},
	66: {"row": 8, "index": 66, "col": 4, "square": 8},
	67: {"row": 8, "index": 67, "col": 5, "square": 8},
	68: {"row": 8, "index": 68, "col": 6, "square": 8},
	69: {"row": 8, "index": 69, "col": 7, "square": 9},
	70: {"row": 8, "index": 70, "col": 8, "square": 9},
	71: {"row": 8, "index": 71, "col": 9, "square": 9},
	72: {"row": 9, "index": 72, "col": 1, "square": 7},
	73: {"row": 9, "index": 73, "col": 2, "square": 7},
	74: {"row": 9, "index": 74, "col": 3, "square": 7},
	75: {"row": 9, "index": 75, "col": 4, "square": 8},
	76: {"row": 9, "index": 76, "col": 5, "square": 8},
	77: {"row": 9, "index": 77, "col": 6, "square": 8},
	78: {"row": 9, "index": 78, "col": 7, "square": 9},
	79: {"row": 9, "index": 79, "col": 8, "square": 9},
	80: {"row": 9, "index": 80, "col": 9, "square": 9}
    }

    def __init__(self, board_list = [EMPTY_VALUE] * 81):
        """Creates a new board from a string"""

        # The board string must be 81 characters
        if len(board_list) != 81:
            raise ValueError("Board list must have a length of 81")

        # Make the board
        self._board = [x if SudokuBoard.is_valid_cell_value(x) else SudokuBoard.EMPTY_VALUE for x in board_list]
        

    @staticmethod
    def is_valid_cell_value(val):
        """Checks whether or not the given value is an integer between 1 and 9 inclusive, or is the empty value"""

        # Default to valid
        is_valid = True

        # Throw a few tests on it
        try:
            if val != SudokuBoard.EMPTY_VALUE:  # The empty value is always valid. Don't test it
                is_valid = isinstance(val, int) # The value must be an integer
                is_valid = is_valid and (val >= 1 and val <= 9)   # The value must be in the range 1-9 inclusive
        except:
            # This is not a valid value
            is_valid = False

        return is_valid

    def get_cell(self, index):
        """Gets info about a cell on the board"""

        cell = SudokuBoard._INDEX_TO_CELL_TEMPLATE[index].copy()
        cell["value"] = self._board[index]
        return cell

    def get_cells(self, indices):
        """Gets info about multiple cells on the board"""

        return [self.get_cell(index) for index in indices]

    def set_cell_value(self, index, new_val):
        """Sets the value of a cell on the board"""

        # Verify that the index is an integer
        if not isinstance(index, int):
            raise TypeError("Index must be an integer in the range 0 - SudokuBoard.CELL_COUNT-1")

        # Verify that the index is in the range 0 - SudokuBoard.CELL_COUNT-1
        if index < 0 or index >= SudokuBoard.CELL_COUNT:
            raise ValueError("Index must be an integer in the range 0 - SudokuBoard.CELL_COUNT-1")

        # Verify that the new value is valid
        if not SudokuBoard.is_valid_cell_value(new_val):
            raise Exception("New value must be an integer in the range 1-9, or SudokuBoard.EMPTY_VALUE")

        # Get the cell
        cell = self.get_cell(index)

        # Set the cell
        self._board[index] = new_val

        # Find errors caused by this
        errors = self._get_errors(cell["row"], cell["col"], cell["square"])
        # Care only about the cells with the inserted value
        errors = [cell for cell in errors if cell["value"] == new_val]
        # Remove duplicates
        errors = [dict(t) for t in set([tuple(d.items()) for d in errors])]

        return errors

    def get_row(self, num):
        """Gets the cells in a row"""

        return self.get_cells(SudokuBoard._ROW_TO_CELL_INDICES[num])

    def get_col(self, num):
        """Gets the cells in a column"""

        return self.get_cells(SudokuBoard._COL_TO_CELL_INDICES[num])

    def get_square(self, num):
        """Gets the cells in a square"""

        return self.get_cells(SudokuBoard._SQUARE_TO_CELL_INDICES[num])

    def _get_errors(self, row_num, col_num, square_num):
        """Gets errors in the given row, column and square"""

        # Handle for the cells with errors
        bad_cells = []

        for cells in [self.get_row(row_num), self.get_col(col_num), self.get_square(square_num)]:
            # Remove empty cells
            cells = [cell for cell in cells if cell["value"] != SudokuBoard.EMPTY_VALUE]

            # Check each cell individually
            count = 0
            for cell_to_check in cells:
                # Count how many cells have this value
                count = sum(map(lambda cell_being_checked: 1 if cell_being_checked["value"] == cell_to_check["value"] else 0, cells))

                # There should be just one cell with that value
                if count != 1:
                    bad_cells.append(cell_to_check)

        return bad_cells

    def get_errors(self):
        """Gets all cells with errors on the entire board"""

        # Handle for the cells with errors
        bad_cells = []

        # Find the errors in every row, column and square
        for i in range(1, 10):
            bad_cells = bad_cells + self._get_errors(i, i, i)

        # Remove duplicates
        bad_cells = [dict(t) for t in set([tuple(d.items()) for d in bad_cells])]

        return bad_cells

    def get_filled_cells(self):
        """Gets a list of all cells with another value than SudokuBoard.EMPTY_VALUE"""

        return [cell for cell in self.get_cells(range(SudokuBoard.CELL_COUNT)) if cell["value"] != SudokuBoard.EMPTY_VALUE]

    def is_solved(self):
        """Checks if the board is solved or not"""

        return len(self.get_filled_cells()) == SudokuBoard.CELL_COUNT and len(self.get_errors()) == 0

    def to_list(self):
        """Converts the board into a list"""

        return self._board[:]

    def clone(self):
        """Makes a clone of this board"""

        return SudokuBoard(self.to_list())

    def equals(self, other):
        """Cheks if this board is equal to another board"""

        # If it is the same board, it is equal
        if self is other:
            return True

        # Else check the cells
        return [cell["value"] for cell in self.get_cells(range(SudokuBoard.CELL_COUNT))] == [cell["value"] for cell in other.get_cells(range(SudokuBoard.CELL_COUNT))]

    def display_grid(self):
        """Displays the sudoku board in a grid. Mostly for debugging purposes"""

        grid = ""
        for i in range(1, 10):
            row = [str(cell["value"]) for cell in self.get_row(i)]
            row = ["_" if val == str(SudokuBoard.EMPTY_VALUE) else val for val in row]
            grid += "".join(row[0:3]) + " " + "".join(row[3:6]) + " " + "".join(row[6:9]) + "\n"

            if i % 3 == 0 and i != 9:
                grid += "\n"

        print(grid)
