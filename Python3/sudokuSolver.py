##########################
# Import important stuff #
##########################

from SudokuBoard import SudokuBoard

############################
# Define the Sudoku solver #
############################

def sudoku_solver(board, start_index = 0):
    """Solves a SudokuBoard"""

    # Check if the board is solved
    if board.is_solved():
        return board
    elif start_index == 0 and len(board.get_errors()) > 0:
        raise Exception("Board is unsolvable")

    # Find the first empty cell
    cell_index = start_index
    while board.get_cell(cell_index)["value"] != SudokuBoard.EMPTY_VALUE:
        cell_index += 1

    # Handle for the solved board
    solved_board = None

    # Find out what values the cell can possibly have
    cell = board.get_cell(cell_index)
    possible_values = set(range(1, 10))
    possible_values = possible_values - set([cell["value"] for cell in board.get_row(cell["row"])])
    possible_values = possible_values - set([cell["value"] for cell in board.get_col(cell["col"])])
    possible_values = possible_values - set([cell["value"] for cell in board.get_square(cell["square"])])
    possible_values = list(possible_values)

    # Try all possible values until a solution is found
    while len(possible_values) > 0 and solved_board == None:
        # Set the cell to the next possible value
        board.set_cell_value(cell_index, possible_values.pop())

        try:
            # Recurse down to solve the next cell
            solved_board = sudoku_solver(board.clone(), cell_index+1)
        except:
            # There were no solutions. Don't do anything
            pass


    # Check if the board was solved
    if solved_board == None:
        # Reset the cell
        board.set_cell_value(cell_index, SudokuBoard.EMPTY_VALUE)

        # Complain loudly
        raise Exception("Board is unsolvable")

    # The board is now solved!
    return solved_board
