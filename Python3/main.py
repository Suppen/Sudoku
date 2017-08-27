##########################
# Import important stuff #
##########################

from SudokuBoard import SudokuBoard
from sudokuSolver import sudoku_solver
from time import time

##########################
# Make and solve a board #
##########################

# Make the board
board = SudokuBoard([int(v) for v in "530070000600195000098000060800060003400803001700020006060000280000419005000080079"])

# Display it
print("Initial board:")
board.display_grid()

# Start a timer
start = time()

# Do the solving
solved = sudoku_solver(board)

# Stop the timer
print("Solved in: " + str((time() - start)*1000) + " ms")

# Print the solved board
solved.display_grid()
