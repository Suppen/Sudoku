package no.suppen.sudoku;

import java.util.Set;

public class Sudoku6x6 {
	/** Symbols used in a standard 6x6 sudoku board */
	public static final Set<Integer> symbols = Set.of(1, 2, 3, 4, 5, 6);

	/** Cell groups used in a standard 6x6 sudoku board */
	public static final Set<Set<Integer>> cellGroups = Set.of(
			// Rows
			Set.of(0, 1, 2, 3, 4, 5),
			Set.of(6, 7, 8, 9, 10, 11),
			Set.of(12, 13, 14, 15, 16, 17),
			Set.of(18, 19, 20, 21, 22, 23),
			Set.of(24, 25, 26, 27, 28, 29),
			Set.of(30, 31, 32, 33, 34, 35),
			// Columns
			Set.of(0, 6, 12, 18, 24, 30),
			Set.of(1, 7, 13, 19, 25, 31),
			Set.of(2, 8, 14, 20, 26, 32),
			Set.of(3, 9, 15, 21, 27, 33),
			Set.of(4, 10, 16, 22, 28, 34),
			Set.of(5, 11, 17, 23, 29, 35),
			// Blocks
			Set.of(0, 1, 2, 6, 7, 8),
			Set.of(3, 4, 5, 9, 10, 11),
			Set.of(12, 13, 14, 18, 19, 20),
			Set.of(15, 16, 17, 21, 22, 23),
			Set.of(24, 25, 26, 30, 31, 32),
			Set.of(27, 28, 29, 33, 34, 35)
		);

	/** Size of a standard 6x6 sudoku board */
	public static final int size = 6 * 6;

	/**
	 * Checks whether or not a sudoku is a standard 6x6, as used in this class
	 * 
	 * @param sudoku The sudoku to check
	 * 
	 * @returns True if it is a standard 6x6 sudoku, false otherwise
	 */
	public static boolean isStandard6x6(Sudoku<Integer> sudoku) {
		return sudoku.size() == Sudoku6x6.size && sudoku.symbols.equals(Sudoku6x6.symbols)
				&& sudoku.cellGroups.equals(Sudoku6x6.cellGroups);
	}

	/** Creates a new, empty, 6x6 sudoku board */
	public static Sudoku<Integer> empty() {
		return new Sudoku<Integer>(Sudoku6x6.size, Sudoku6x6.symbols, Sudoku6x6.cellGroups);
	}

	/**
	 * Creates a new 6x6 sudoku board from a string. Spaces and newlines are
	 * removed. 1-6 are symbols, everything else count as empty cells
	 * 
	 * @param str The string to create the board from
	 * 
	 * @return A new 6x6 sudoku board made from the string
	 * 
	 * @throws Error If the string does not contain exactly 36 characters, not
	 *               counting spaces and newlines
	 */
	public static Sudoku<Integer> fromString(String str) {
		// Prepare the string
		String preparedStr = str.replaceAll("[ \n]", "");

		// The string must have exactly as many characters as there are cells on a 6x6
		// board
		if (preparedStr.length() != Sudoku6x6.size) {
			throw new Error("String must have exactly " + Sudoku6x6.size
					+ " characters, not counting spaces and newlines. Got " + preparedStr.length());
		}

		// Make the board
		Sudoku<Integer> sudoku = Sudoku6x6.empty();

		// Fill it with values from the string
		sudoku.cellIndices().forEach(index -> {
			int symbol = preparedStr.codePointAt(index) - '0';

			if (!Sudoku6x6.symbols.contains(symbol)) {
				return;
			}

			sudoku.setCellSymbol(index, symbol);
		});

		return sudoku;
	}

	/**
	 * Converts a standard 6x6 sudoku to a string
	 * 
	 * @param sudoku The sudoku to stringify
	 * 
	 * @return String representation of the sudoku. Empty cells are represented by
	 *         '_', filled cells by their digit
	 *         
	 * @throws If the given sudoku is not a standard 6x6 sudoku
	 */
	public static String toString(Sudoku<Integer> sudoku) {
		if (!Sudoku6x6.isStandard6x6(sudoku)) {
			throw new Error("Sudoku is not a standard 6x6 sudoku"); // TODO Proper error type
		}
		
		return sudoku.cellIndices().mapToObj(sudoku::getCellSymbol)
				.map(optSymbol -> optSymbol.map(symbol -> symbol.toString().charAt(0)).orElse('_'))
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}
	
	/**
	 * Converts a standard 6x6 sudoku to a pretty string, with spaces and newlines separating the blocks
	 * 
	 * @param sudoku The sudoku to stringify
	 * 
	 * @return String representation of the sudoku. Empty cells are represented by
	 *         '_', filled cells by their digit
	 *         
	 * @throws If the given sudoku is not a standard 6x6 sudoku
	 */
	public static String toPrettyString(Sudoku<Integer> sudoku) {
		String str = Sudoku6x6.toString(sudoku);
		
		StringBuilder prettyStr = new StringBuilder();
		for (int start = 0; start < Sudoku6x6.size; start += 3) {
			int end = start + 3;
			// Take two characters and put them into the string builder
			prettyStr.append(str.subSequence(start, end));
			
			if (end != 36) {
				if (end % 6 == 0) {
					// At the end of a line
					prettyStr.append('\n');
				} else {
					// Between two blocks
					prettyStr.append(' ');
				}
				
				if (end % 12 == 0) {
					prettyStr.append('\n');
				}
			}
		}
		
		return prettyStr.toString();
	}
}