package no.suppen.sudoku;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class Sudoku9x9Test {
	
	private String solvedStr =
			  "534 678 912\n"
			+ "672 195 348\n"
			+ "198 342 567\n"
			+ "\n"
			+ "859 761 423\n"
			+ "426 853 791\n"
			+ "713 924 856\n"
			+ "\n"
			+ "961 537 284\n"
			+ "287 419 635\n"
			+ "345 286 179";
	
	private String puzzleStr = 
			  "53_ _7_ ___\n"
			+ "6__ 195 ___\n"
			+ "_98 ___ _6_\n"
			+ "\n"
			+ "8__ _6_ __3\n"
			+ "4__ 8_3 __1\n"
			+ "7__ _2_ __6\n"
			+ "\n"
			+ "_6_ ___ 28_\n"
			+ "___ 419 __5\n"
			+ "___ _8_ _79";

	@Test
	final void testSymbols() {
		// The symbols should be the numbers 1-9
		assertEquals(9, Sudoku9x9.symbols.size());
		for (int symbol : Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9)) {
			assertTrue(Sudoku9x9.symbols.contains(symbol));
		}
	}

	@Test
	final void testIsStandard9x9() {
		// A sudoku with a size different from 9x9 is not a standard 9x9
		Sudoku<Integer> sudoku;
		sudoku = new Sudoku<>(Sudoku9x9.size - 1, Sudoku9x9.symbols, Sudoku9x9.cellGroups);
		assertFalse(Sudoku9x9.isStandard9x9(sudoku));

		// A sudoku with a different set of numbers than 1-9 as symbols is not a
		// standard 9x9
		HashSet<Integer> symbols = new HashSet<>(Sudoku9x9.symbols);
		symbols.remove(1);
		symbols.add(100);
		sudoku = new Sudoku<>(Sudoku9x9.size, symbols, Sudoku9x9.cellGroups);
		assertFalse(Sudoku9x9.isStandard9x9(sudoku));

		// A sudoku with a different type of symbols than integers is not a standard 9x9
		// This will not even run, due to type mismatches. This is the way it should be
		// Set<Character> badSymbols = Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
		// 'i');
		// Sudoku<Character> badSudoku = new Sudoku<>(Sudoku9x9.size, badSymbols,
		// Sudoku9x9.cellGroups);
		// assertFalse(Sudoku9x9.isStandard9x9(badSudoku));

		// A sudoku with a different list of cell groups is not a standard 9x9
		ArrayList<Set<Integer>> cellGroups = new ArrayList<>(Sudoku9x9.cellGroups);
		cellGroups.add(Set.of(1, 2, 3, 4));
		assertFalse(Sudoku9x9.isStandard9x9(sudoku));

		// A Proper 9x9 standard sudoku is a standard 9x9 sudoku
		sudoku = new Sudoku<Integer>(Sudoku9x9.size, Sudoku9x9.symbols, Sudoku9x9.cellGroups);
		assertTrue(Sudoku9x9.isStandard9x9(sudoku));
	}

	@Test
	final void testSize() {
		// There should be 9x9 = 81 cells
		assertEquals(9 * 9, Sudoku9x9.size);
	}

	@Test
	final void testCellGroups() {
		// There should be 27 of them: 9 rows, 9 columns, 9 blocks
		assertEquals(9 * 3, Sudoku9x9.cellGroups.size());

		// Each group should have 9 indices
		for (var group : Sudoku9x9.cellGroups) {
			assertEquals(9, group.size());
		}

		// The indices in the groups should be within the range of the board's cells
		Set<Integer> indices = IntStream.range(0, Sudoku9x9.size).boxed().collect(Collectors.toSet());
		for (var group : Sudoku9x9.cellGroups) {
			for (var index : group) {
				assertTrue(indices.contains(index));
			}
		}

		// Each cell index should be present exactly three times: Once in a row, once in
		// a column, and once in a block
		HashMap<Integer, Integer> indexCounts = new HashMap<Integer, Integer>();
		for (var group : Sudoku9x9.cellGroups) {
			for (var index : group) {
				int count = indexCounts.getOrDefault(index, 0);
				indexCounts.put(index, count + 1);
			}
		}
		for (var entry : indexCounts.entrySet()) {
			assertEquals(3, entry.getValue());
		}
	}

	@Test
	final void testEmpty() {
		Sudoku<Integer> sudoku = Sudoku9x9.empty();

		// All the props should be the same as in the Sudoku9x9 class
		assertEquals(Sudoku9x9.symbols, sudoku.symbols);
		assertEquals(Sudoku9x9.cellGroups, sudoku.cellGroups);
		assertEquals(Sudoku9x9.size, sudoku.size());

		// All the cells should be empty
		sudoku.cellIndices().forEach(index -> assertTrue(sudoku.cellIsEmpty(index)));
	}

	@Test
	final void testFromString() {
		// Make a string with empty cells except a 5 in cell 50
		int index = 50;
		String str = "_".repeat(Sudoku9x9.size);
		char[] chars = str.toCharArray();
		chars[index] = '5';
		str = new String(chars);

		Sudoku<Integer> board = Sudoku9x9.fromString(str);

		Set<Integer> filledIndices = board.filledCellIndices().boxed().collect(Collectors.toSet());

		assertEquals(Set.of(index), filledIndices);
		assertEquals(Optional.of(5), board.getCellSymbol(index));
	}
	
	@Test
	final void testFromBadString() {
		String str = "Invalid 9x9 board";
		
		assertThrows(Error.class, () -> Sudoku9x9.fromString(str)); // TODO Proper error class
	}
	
	@Test
	final void testToString() {
		Stream.of(solvedStr, puzzleStr).forEach(str -> {
			Sudoku<Integer> sudoku = Sudoku9x9.fromString(str);
			String sudokuStr = Sudoku9x9.toString(sudoku);
			
			assertEquals(str.replaceAll("[ \n]", ""), sudokuStr);
		});
	}
	
	@Test
	final void testToStringBad() {
		Sudoku<Integer> sudoku = new Sudoku<Integer>(0, Set.of(), Set.of());
		
		assertThrows(Error.class, () -> Sudoku9x9.toString(sudoku)); // TODO Proper error class
	}
	
	@Test
	final void testToPrettyString() {
		Stream.of(solvedStr, puzzleStr).forEach(str -> {
			Sudoku<Integer> sudoku = Sudoku9x9.fromString(str);
			String sudokuStr = Sudoku9x9.toPrettyString(sudoku);
			
			assertEquals(str, sudokuStr);
		});
	}
}
