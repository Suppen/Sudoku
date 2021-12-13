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

class Sudoku6x6Test {
	
	private String solvedStr =
			  "512 643\n"
			+ "364 125\n"
			+ "\n"
			+ "143 256\n"
			+ "625 431\n"
			+ "\n"
			+ "236 514\n"
			+ "451 362";
	
	private String puzzleStr =
			  "_1_ 643\n"
			+ "___ 125\n"
			+ "\n"
			+ "_4_ 256\n"
			+ "6__ ___\n"
			+ "\n"
			+ "__6 __4\n"
			+ "45_ 362";


	@Test
	final void testSymbols() {
		// The symbols should be the numbers 1-6
		assertEquals(6, Sudoku6x6.symbols.size());
		for (int symbol : Set.of(1, 2, 3, 4, 5, 6)) {
			assertTrue(Sudoku6x6.symbols.contains(symbol));
		}
	}

	@Test
	final void testIsStandard6x6() {
		// A sudoku with a size different from 6x6 is not a standard 6x6
		Sudoku<Integer> sudoku;
		sudoku = new Sudoku<>(Sudoku6x6.size - 1, Sudoku6x6.symbols, Sudoku6x6.cellGroups);
		assertFalse(Sudoku6x6.isStandard6x6(sudoku));

		// A sudoku with a different set of numbers than 1-9 as symbols is not a
		// standard 6x6
		HashSet<Integer> symbols = new HashSet<>(Sudoku6x6.symbols);
		symbols.remove(1);
		symbols.add(100);
		sudoku = new Sudoku<>(Sudoku6x6.size, symbols, Sudoku6x6.cellGroups);
		assertFalse(Sudoku6x6.isStandard6x6(sudoku));

		// A sudoku with a different type of symbols than integers is not a standard 6x6
		// This will not even run, due to type mismatches. This is the way it should be
		// Set<Character> badSymbols = Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
		// 'i');
		// Sudoku<Character> badSudoku = new Sudoku<>(Sudoku6x6.size, badSymbols,
		// Sudoku6x6.cellGroups);
		// assertFalse(Sudoku6x6.isStandard6x6(badSudoku));

		// A sudoku with a different list of cell groups is not a standard 6x6
		ArrayList<Set<Integer>> cellGroups = new ArrayList<>(Sudoku6x6.cellGroups);
		cellGroups.add(Set.of(1, 2, 3, 4));
		assertFalse(Sudoku6x6.isStandard6x6(sudoku));

		// A Proper 6x6 standard sudoku is a standard 6x6 sudoku
		sudoku = new Sudoku<Integer>(Sudoku6x6.size, Sudoku6x6.symbols, Sudoku6x6.cellGroups);
		assertTrue(Sudoku6x6.isStandard6x6(sudoku));
	}

	@Test
	final void testSize() {
		// There should be 6x6 = 36 cells
		assertEquals(36, Sudoku6x6.size);
	}

	@Test
	final void testCellGroups() {
		// There should be 18 of them: 6 rows, 6 columns, 6 blocks
		assertEquals(6 * 3, Sudoku6x6.cellGroups.size());

		// Each group should have 6 indices
		for (var group : Sudoku6x6.cellGroups) {
			assertEquals(6, group.size());
		}

		// The indices in the groups should be within the range of the board's cells
		Set<Integer> indices = IntStream.range(0, Sudoku6x6.size).boxed().collect(Collectors.toSet());
		for (var group : Sudoku6x6.cellGroups) {
			for (var index : group) {
				assertTrue(indices.contains(index));
			}
		}

		// Each cell index should be present exactly three times: Once in a row, once in
		// a column, and once in a block
		HashMap<Integer, Integer> indexCounts = new HashMap<Integer, Integer>();
		for (var group : Sudoku6x6.cellGroups) {
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
		Sudoku<Integer> sudoku = Sudoku6x6.empty();

		// All the props should be the same as in the Sudoku6x6 class
		assertEquals(Sudoku6x6.symbols, sudoku.symbols);
		assertEquals(Sudoku6x6.cellGroups, sudoku.cellGroups);
		assertEquals(Sudoku6x6.size, sudoku.size());

		// All the cells should be empty
		sudoku.cellIndices().forEach(index -> assertTrue(sudoku.cellIsEmpty(index)));
	}

	@Test
	final void testFromString() {
		// Make a string with empty cells except a 5 in cell 30
		int index = 30;
		String str = "_".repeat(Sudoku6x6.size);
		char[] chars = str.toCharArray();
		chars[index] = '5';
		str = new String(chars);

		Sudoku<Integer> board = Sudoku6x6.fromString(str);

		Set<Integer> filledIndices = board.filledCellIndices().boxed().collect(Collectors.toSet());

		assertEquals(Set.of(index), filledIndices);
		assertEquals(Optional.of(5), board.getCellSymbol(index));
	}
	
	@Test
	final void testFromBadString() {
		String str = "Invalid 6x6 board";
		
		assertThrows(Error.class, () -> Sudoku6x6.fromString(str)); // TODO Proper error class
	}
	
	@Test
	final void testToString() {
		Stream.of(solvedStr, puzzleStr).forEach(str -> {
			Sudoku<Integer> sudoku = Sudoku6x6.fromString(str);
			String sudokuStr = Sudoku6x6.toString(sudoku);
			
			assertEquals(str.replaceAll("[ \n]", ""), sudokuStr);
		});
	}
	
	@Test
	final void testToStringBad() {
		Sudoku<Integer> sudoku = new Sudoku<Integer>(0, Set.of(), Set.of());
		
		assertThrows(Error.class, () -> Sudoku6x6.toString(sudoku)); // TODO Proper error class
	}
	
	@Test
	final void testToPrettyString() {
		Stream.of(solvedStr, puzzleStr).forEach(str -> {
			Sudoku<Integer> sudoku = Sudoku6x6.fromString(str);
			String sudokuStr = Sudoku6x6.toPrettyString(sudoku);
			
			assertEquals(str, sudokuStr);
		});
	}
}
