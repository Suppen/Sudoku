package no.suppen.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class capable of representing any sudoku with a fixed number of cells, a set
 * of valid symbols, and a set of sets of cells which must contain unique
 * symbols for the sudoku to be considered solved. It does not care about the
 * puzzle's layout, nor is it limited to 9x9 sudokus, but it does support them.
 * 
 * @author simen
 *
 * @param <S> Type of symbols in the sudoku
 */
public class Sudoku<S> {
	/** Set of possible symbols for the sudoku */
	public Set<S> symbols;
	/** The sudoku's cells */
	private List<Optional<S>> cells;
	/** The sudoku's cell groups */
	public Set<Set<Integer>> cellGroups;

	private void construct(int size, Set<S> symbols, Set<Set<Integer>> cellGroups) {
		// Make a board and fill it with empty cells
		cells = new ArrayList<Optional<S>>(size);
		IntStream.range(0, size).forEach(_i -> cells.add(Optional.empty()));

		// Store the symbol set
		this.symbols = symbols;

		// Clone the cell groups to make them immutable
		this.cellGroups = cellGroups.stream().map(Set::copyOf).collect(Collectors.toSet());
	}

	/**
	 * Creates a new sudoku board
	 * 
	 * @param size       Number of cells to have on the board
	 * @param symbols    Set of possible symbols to have in the cells
	 * @param cellGroups List of sets of cell groups. All cells in a cell group must
	 *                   be filled and unique for the board to count as solved
	 */
	public Sudoku(int size, Set<S> symbols, Set<Set<Integer>> cellGroups) {
		construct(size, symbols, cellGroups);
	}

	/**
	 * Creates a new copy of a sudoku board
	 * 
	 * @param orig The original to make a copy of
	 */
	public Sudoku(Sudoku<S> orig) {
		construct(orig.size(), orig.symbols, orig.cellGroups);

		// Copy the values from the original to this
		orig.filledCellIndices().forEach(i -> setCellSymbol(i, orig.getCellSymbol(i).get()));
	}

	@Override
	public int hashCode() {
		return Objects.hash(cells, cellGroups, symbols);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sudoku<?> other = (Sudoku<?>) obj;
		return Objects.equals(cells, other.cells) && Objects.equals(cellGroups, other.cellGroups)
				&& Objects.equals(symbols, other.symbols);
	}

	/** Number of cells on the board */
	public int size() {
		return cells.size();
	}

	/**
	 * A stream of the indices on the board
	 * 
	 * @returns A stream of the indices of all the cells on the board
	 */
	public IntStream cellIndices() {
		return IntStream.range(0, size());
	}

	/**
	 * Sets a cell to a symbol
	 * 
	 * @param index  Index of the cell to set
	 * @param symbol The symbol to put in the cell. Must be part of the board's
	 *               symbol set
	 */
	public void setCellSymbol(int index, S symbol) {
		// Verify the symbol is part of the symbol set
		if (!symbols.contains(symbol)) {
			throw new InvalidSymbolException();
		}

		// Update the board
		cells.set(index, Optional.of(symbol));
	}

	/**
	 * Clears the value of a cell
	 * 
	 * @param index Index of the cell to clear
	 */
	public void clearCellSymbol(int index) {
		cells.set(index, Optional.empty());
	}

	/**
	 * Gets the value of a cell
	 * 
	 * @param index Index of the cell to get the value of
	 * 
	 * @return The value of the cell
	 */
	public Optional<S> getCellSymbol(int index) {
		return cells.get(index);
	}

	/**
	 * Checks whether or not a cell is empty
	 * 
	 * @param index Index of the cell to check
	 * 
	 * @return True if the cell is empty, false otherwise
	 */
	public boolean cellIsEmpty(int index) {
		return getCellSymbol(index).isEmpty();
	}

	/**
	 * Checks whether or not a cell is filled
	 * 
	 * @param index Index of the cell to check
	 * 
	 * @return True if the cell is filled, false otherwise
	 */
	public boolean cellIsFilled(int index) {
		return !cellIsEmpty(index);
	}

	/**
	 * Gets a set of indices of cells which affect the value of a given cell
	 * 
	 * @param index Index of the cell to get the linked cell indices of
	 * 
	 * @return Set of indices of cells which affect the given cell
	 */
	public IntStream linkedCellIndices(int index) {
		return cellGroups
				// Take the union of all sets in the list containing the index
				.stream().filter(group -> group.contains(index)).flatMap(Set::stream)
				// Do not include the cell itself
				.filter(i -> i != index)
				// Turn it into an IntStream
				.mapToInt(i -> i);
	}

	/**
	 * Gets the symbols which can be placed in a cell without causing errors
	 * 
	 * @param index Index of the cell to get candidates for
	 * 
	 * @return Set of possible values for the cell
	 */
	public Set<S> getCandidatesForCell(int index) {
		Set<S> presentSymbols =
				// Get the indices of all cells affecting this one
				linkedCellIndices(index).boxed()
						// Get their symbols
						.map(this::getCellSymbol)
						// Ignore the empty ones
						.filter(Optional::isPresent)
						// Make a set of the existing symbols
						.map(Optional::get).collect(Collectors.toSet());

		// Take the difference between the set of possible symbols and the present
		// symbols
		return symbols.stream().filter(symbol -> !presentSymbols.contains(symbol)).collect(Collectors.toSet());
	}

	/**
	 * Gets the indices of all empty cells on the board
	 * 
	 * @return Stream of indices of all empty cells on the board
	 */
	public IntStream emptyCellIndices() {
		return cellIndices().filter(this::cellIsEmpty);
	}

	/**
	 * Gets the indices of all filled cells on the board
	 * 
	 * @returns Stream of incides of all filled cells on the board
	 */
	public IntStream filledCellIndices() {
		return cellIndices().filter(this::cellIsFilled);
	}

	/** Checks whether or not all cells on the board has been filled */
	public boolean isFilled() {
		return emptyCellIndices().count() == 0;
	}

	/**
	 * Checks whether or not a cell is valid
	 * 
	 * @param index Index of the cell to check
	 * 
	 * @return True if the cell is empty or if not other cell's symbol collides with
	 *         it
	 */
	public boolean cellIsValid(int index) {
		return getCellSymbol(index).map(symbol -> getCandidatesForCell(index).contains(symbol))
				// If the cell is empty it is valid
				.orElse(true);
	}

	/**
	 * Checks whether or not there are any errors on the board
	 * 
	 * @return True if there are any errors, false otherwise
	 */
	public boolean hasErrors() {
		return !cellIndices().allMatch(this::cellIsValid);
	}

	/**
	 * Checks whether or not the board is solved
	 * 
	 * @return True if the board is solved, false otherwise
	 */
	public boolean isSolved() {
		return isFilled() && !hasErrors();
	}
}
