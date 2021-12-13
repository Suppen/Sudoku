package no.suppen.sudoku;

import java.util.Set;

public class Sudoku9x9 {
	/** Symbols used in a standard 9x9 sudoku board */
	public static final Set<Integer> symbols = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

	/** Cell groups used in a standard 9x9 sudoku board */
	public static final Set<Set<Integer>> cellGroups = Set.of(
			// Rows
			Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8),
			Set.of(9, 10, 11, 12, 13, 14, 15, 16, 17),
			Set.of(18, 19, 20, 21, 22, 23, 24, 25, 26),
			Set.of(27, 28, 29, 30, 31, 32, 33, 34, 35),
			Set.of(36, 37, 38, 39, 40, 41, 42, 43, 44),
			Set.of(45, 46, 47, 48, 49, 50, 51, 52, 53),
			Set.of(54, 55, 56, 57, 58, 59, 60, 61, 62),
			Set.of(63, 64, 65, 66, 67, 68, 69, 70, 71),
			Set.of(72, 73, 74, 75, 76, 77, 78, 79, 80),
			// Columns
			Set.of(0, 9, 18, 27, 36, 45, 54, 63, 72),
			Set.of(1, 10, 19, 28, 37, 46, 55, 64, 73),
			Set.of(2, 11, 20, 29, 38, 47, 56, 65, 74),
			Set.of(3, 12, 21, 30, 39, 48, 57, 66, 75),
			Set.of(4, 13, 22, 31, 40, 49, 58, 67, 76),
			Set.of(5, 14, 23, 32, 41, 50, 59, 68, 77),
			Set.of(6, 15, 24, 33, 42, 51, 60, 69, 78),
			Set.of(7, 16, 25, 34, 43, 52, 61, 70, 79),
			Set.of(8, 17, 26, 35, 44, 53, 62, 71, 80),
			// Blocks
			Set.of(0, 1, 2, 9, 10, 11, 18, 19, 20),
			Set.of(3, 4, 5, 12, 13, 14, 21, 22, 23),
			Set.of(6, 7, 8, 15, 16, 17, 24, 25, 26),
			Set.of(27, 28, 29, 36, 37, 38, 45, 46, 47),
			Set.of(30, 31, 32, 39, 40, 41, 48, 49, 50),
			Set.of(33, 34, 35, 42, 43, 44, 51, 52, 53),
			Set.of(54, 55, 56, 63, 64, 65, 72, 73, 74),
			Set.of(57, 58, 59, 66, 67, 68, 75, 76, 77),
			Set.of(60, 61, 62, 69, 70, 71, 78, 79, 80)
		);

	/** Size of a standard 9x9 sudoku board */
	public static final int size = 9 * 9;

	/**
	 * Checks whether or not a sudoku is a standard 9x9, as used in this class
	 * 
	 * @param sudoku The sudoku to check
	 * 
	 * @returns True if it is a standard 9x9 sudoku, false otherwise
	 */
	public static boolean isStandard9x9(Sudoku<Integer> sudoku) {
		return sudoku.size() == Sudoku9x9.size && sudoku.symbols.equals(Sudoku9x9.symbols)
				&& sudoku.cellGroups.equals(Sudoku9x9.cellGroups);
	}

	/** Creates a new, empty, 9x9 sudoku board */
	public static Sudoku<Integer> empty() {
		return new Sudoku<Integer>(Sudoku9x9.size, Sudoku9x9.symbols, Sudoku9x9.cellGroups);
	}

	/**
	 * Creates a new 9x9 sudoku board from a string. Spaces and newlines are
	 * removed. 1-9 are symbols, everything else count as empty cells
	 * 
	 * @param str The string to create the board from
	 * 
	 * @return A new 9x9 sudoku board made from the string
	 * 
	 * @throws Error If the string does not contain exactly 81 characters, not
	 *               counting spaces and newlines
	 */
	public static Sudoku<Integer> fromString(String str) {
		// Prepare the string
		String preparedStr = str.replaceAll("[ \n]", "");

		// The string must have exactly as many characters as there are cells on a 9x9
		// board
		if (preparedStr.length() != Sudoku9x9.size) {
			throw new Error("String must have exactly " + Sudoku9x9.size
					+ " characters, not counting spaces and newlines. Got " + preparedStr.length());
		}

		// Make the board
		Sudoku<Integer> sudoku = Sudoku9x9.empty();

		// Fill it with values from the string
		sudoku.cellIndices().forEach(index -> {
			int symbol = preparedStr.codePointAt(index) - '0';

			if (!Sudoku9x9.symbols.contains(symbol)) {
				return;
			}

			sudoku.setCellSymbol(index, symbol);
		});

		return sudoku;
	}

	/**
	 * Converts a standard 9x9 sudoku to a string
	 * 
	 * @param sudoku The sudoku to stringify
	 * 
	 * @return String representation of the sudoku. Empty cells are represented by
	 *         '_', filled cells by their digit
	 *         
	 * @throws If the given sudoku is not a standard 9x9 sudoku
	 */
	public static String toString(Sudoku<Integer> sudoku) {
		if (!Sudoku9x9.isStandard9x9(sudoku)) {
			throw new Error("Sudoku is not a standard 9x9 sudoku"); // TODO Proper error type
		}
		
		return sudoku.cellIndices().mapToObj(sudoku::getCellSymbol)
				.map(optSymbol -> optSymbol.map(symbol -> symbol.toString().charAt(0)).orElse('_'))
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
	}
	
	/**
	 * Converts a standard 9x9 sudoku to a pretty string, with spaces and newlines separating the blocks
	 * 
	 * @param sudoku The sudoku to stringify
	 * 
	 * @return String representation of the sudoku. Empty cells are represented by
	 *         '_', filled cells by their digit
	 *         
	 * @throws If the given sudoku is not a standard 9x9 sudoku
	 */
	public static String toPrettyString(Sudoku<Integer> sudoku) {
		String str = Sudoku9x9.toString(sudoku);
		
		StringBuilder prettyStr = new StringBuilder();
		for (int start = 0; start < Sudoku9x9.size; start += 3) {
			int end = start + 3;
			// Take three characters and put them into the string builder
			prettyStr.append(str.subSequence(start, end));
			
			if (end != 81) {
				if (end % 9 == 0) {
					// At the end of a line
					prettyStr.append('\n');
				} else {
					// Between two blocks
					prettyStr.append(' ');
				}
				
				if (end % 27 == 0) {
					prettyStr.append('\n');
				}
			}
		}
		
		return prettyStr.toString();
	}
}