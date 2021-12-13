package no.suppen.sudoku;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SudokuTest {
	private Sudoku<Integer> sudoku;
	
	private String solvedStr =
			  "534 678 912"
			+ "672 195 348"
			+ "198 342 567"
			
			+ "859 761 423"
			+ "426 853 791"
			+ "713 924 856"
			
			+ "961 537 284"
			+ "287 419 635"
			+ "345 286 179";
	
	private String errorStr =
			  "534 678 912"
			+ "672 195 348"
			+ "198 342 567"
			
			+ "859 761 423"
			+ "426 813 791" // The center symbol should be a 5 to be valid
			+ "713 924 856"
			
			+ "961 537 284"
			+ "287 419 635"
			+ "345 286 179";
	
	@BeforeEach
	final void before() {
		sudoku = Sudoku9x9.empty();
	}

	@Test
	final void testConstructor() {
		sudoku = new Sudoku<>(Sudoku9x9.size, Sudoku9x9.symbols, Sudoku9x9.cellGroups);
		
		// Check that the properties are the same as what was given
		assertEquals(Sudoku9x9.size, sudoku.size());
		assertEquals(Sudoku9x9.symbols, sudoku.symbols);
		assertEquals(Sudoku9x9.cellGroups, sudoku.cellGroups);
		
		// Verify the board is empty
		assertTrue(sudoku.cellIndices().allMatch(sudoku::cellIsEmpty));
	}

	@Test
	final void testCopyConstructor() {
		// Set some random cells so the board is not empty
		sudoku.setCellSymbol(40, 5);
		
		Sudoku<Integer> copy = new Sudoku<>(sudoku);
		
		assertFalse(sudoku == copy);
		assertEquals(sudoku, copy);
	}
	
	@Test
	final void testEquals() {
		assertTrue(sudoku.equals(sudoku));
		assertFalse(sudoku.equals(null));
		assertFalse(sudoku.equals(new Object()));
		
		var copy = new Sudoku<>(sudoku);
		
		assertFalse(sudoku == copy);
		assertTrue(sudoku.equals(copy));
		
		copy.setCellSymbol(50, 5);
		
		assertFalse(sudoku.equals(copy));
	}

	@Test
	final void testSize() {
		assertEquals(Sudoku9x9.size, sudoku.size());
	}

	@Test
	final void testCellIndices() {
		Object[] expectedIndices = IntStream.range(0, sudoku.size())
				.boxed()
				.collect(Collectors.toList())
				.toArray();
		Object[] actualIndices = sudoku.cellIndices()
				.boxed()
				.collect(Collectors.toList())
				.toArray();
		
		assertArrayEquals(expectedIndices, actualIndices);
	}

	@Test
	final void testCellSymbolManipulators() {
		int index = 50;
		int symbol = 5;
		
		assertEquals(Optional.empty(), sudoku.getCellSymbol(index));
		
		sudoku.setCellSymbol(index, symbol);
		
		assertEquals(Optional.of(5), sudoku.getCellSymbol(index));
		
		sudoku.clearCellSymbol(index);
		
		assertEquals(Optional.empty(), sudoku.getCellSymbol(index));
	}

	@Test
	final void testCellStateCheckers() {
		int index = 50;
		
		assertEquals(Optional.empty(), sudoku.getCellSymbol(index));

		assertTrue(sudoku.cellIsEmpty(index));
		assertFalse(sudoku.cellIsFilled(index));
		
		sudoku.setCellSymbol(index, 5);
		
		assertFalse(sudoku.cellIsEmpty(index));
		assertTrue(sudoku.cellIsFilled(index));
	}

	@Test
	final void testLinkedCellIndices() {
		int index = 0;
		
		Set<Integer> row = Set.of(0, 1, 2, 3, 4 , 5, 6, 7, 8);
		Set<Integer> col = Set.of(0, 9, 18, 27, 36, 45, 54, 63, 72);
		Set<Integer> block = Set.of(0, 1, 2, 9, 10, 11, 18, 19, 20);
		
		HashSet<Integer> expected = new HashSet<Integer>();
		expected.addAll(row);
		expected.addAll(col);
		expected.addAll(block);
		expected.remove(index);
		
		Set<Integer> linkedCellIndices = sudoku.linkedCellIndices(index).boxed().collect(Collectors.toSet());
		assertEquals(expected, linkedCellIndices);
	}

	@Test
	final void testGetCandidatesForCell() {
		int index = 50;
		int symbol = 5;
		
		// Any cell on the empty board should have the full set of symbols as candidates
		sudoku.cellIndices().forEach(i -> assertEquals(sudoku.symbols, sudoku.getCandidatesForCell(i)));
		
		// Set one cell on the board, to get some with fewer candidates
		sudoku.setCellSymbol(index, symbol);
		
		// The cell should still have the same candidates
		assertEquals(sudoku.symbols, sudoku.getCandidatesForCell(index));
		
		// All linked cells should now have one fewer candidates
		HashSet<Integer> expectedCandidates = new HashSet<Integer>(sudoku.symbols);
		expectedCandidates.remove(symbol);
		sudoku.linkedCellIndices(index).forEach(linkedIndex -> {
			assertEquals(expectedCandidates, sudoku.getCandidatesForCell(linkedIndex));
		});
	}
	
	@Test
	final void testSetInvalidCellSymbol() {
		int symbol = 10;
		assertFalse(sudoku.symbols.contains(symbol));
		
		assertThrows(InvalidSymbolException.class, () -> sudoku.setCellSymbol(0, symbol));
	}

	@Test
	final void testEmptyAndFilledCellIndices() {
		int symbol = 5;
		
		// Every cell should be empty to begin with
		HashSet<Integer> expectedEmptyIndices = new HashSet<>();
		sudoku.cellIndices().forEach(expectedEmptyIndices::add);
		
		// No cells should be filled to begin with
		HashSet<Integer> expectedFilledIndices = new HashSet<>();
		
		// For each cell, verify the board's empty and filled index streams are correct, then fill the cell and try again
		sudoku.cellIndices().forEach(index -> {
			// Get empty and filled indices as sets
			Set<Integer> filledIndices = sudoku.filledCellIndices()
				.boxed()
				.collect(Collectors.toSet());
			Set<Integer> emptyIndices = sudoku.emptyCellIndices()
				.boxed()
				.collect(Collectors.toSet());
			
			// Verify it matches the expected sets
			assertEquals(expectedFilledIndices, filledIndices);
			assertEquals(expectedEmptyIndices, emptyIndices);
			
			// Set this cell to something and add the index to the expected-set
			sudoku.setCellSymbol(index, symbol);
			expectedFilledIndices.add(index);
			expectedEmptyIndices.remove(index);
		});
	}

	@Test
	final void testIsFilled() {
		sudoku.cellIndices().forEach(index -> {
			assertFalse(sudoku.isFilled()); // Note that it is checked before filling a cell
			
			sudoku.setCellSymbol(index, 5);
		});
		
		// All cells should now be filled
		assertTrue(sudoku.isFilled());
	}

	@Test
	final void testCellIsValid() {
		int index = 50;
		int symbol = 5;
		
		// An empty cell is valid
		assertTrue(sudoku.cellIsEmpty(index));
		assertTrue(sudoku.cellIsValid(index));
		
		// A cell with no collisions is valid
		sudoku.setCellSymbol(index, symbol);
		assertTrue(sudoku.cellIsValid(index));
		
		// A cell with collisions is invalid
		sudoku.linkedCellIndices(index)
			.limit(1)
			.forEach(collidingIndex -> {
				sudoku.setCellSymbol(collidingIndex, symbol);
			
				assertFalse(sudoku.cellIsValid(index));
			});
	}

	@Test
	final void testHasErrors() {
		int index1 = 50;
		int index2 = 49;
		int symbol = 5;
		
		// The empty board has no errors
		assertFalse(sudoku.hasErrors());
		
		// A board with one number has no errors
		sudoku.setCellSymbol(index1, symbol);
		assertFalse(sudoku.hasErrors());
		
		// A board with two of one symbol in the same cell groups is invalid
		sudoku.setCellSymbol(index2, symbol);
		assertTrue(sudoku.hasErrors());
		
		// A filled board with one bad number has errors
		sudoku = Sudoku9x9.fromString(errorStr);
		assertTrue(sudoku.hasErrors());
	}

	@Test
	final void testIsSolved() {
		// The empty board is not solved
		assertFalse(sudoku.isSolved());
		
		// A filled board with an error is not solved
		sudoku = Sudoku9x9.fromString(errorStr);
		assertFalse(sudoku.isSolved());
		
		// A filled board without errors is solved
		sudoku = Sudoku9x9.fromString(solvedStr);
		assertTrue(sudoku.isSolved());
	}

}
