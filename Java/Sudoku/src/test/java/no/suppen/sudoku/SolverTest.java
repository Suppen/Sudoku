package no.suppen.sudoku;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class SolverTest {
	private final String solvedStr = """
					534 678 912
					672 195 348
					198 342 567

					859 761 423
					426 853 791
					713 924 856

					961 537 284
					287 419 635
					345 286 179""";

	private final String puzzleStr = """
					53_ _7_ ___
					6__ 195 ___
					_98 ___ _6_

					8__ _6_ __3
					4__ 8_3 __1
					7__ _2_ __6

					_6_ ___ 28_
					___ 419 __5
					___ _8_ _79""";

	@Test
	final void testSolveSimple() {
		// Solve a board with only one missing symbol
		Sudoku<Integer> solved = Sudoku9x9.fromString(solvedStr);

		Sudoku<Integer> almostSolved = new Sudoku<Integer>(solved);
		almostSolved.clearCellSymbol(50);

		Set<Sudoku<Integer>> solutions = Solver.solve(almostSolved).collect(Collectors.toSet());

		assertEquals(Set.of(solved), solutions);
	}

	@Test
	final void testSolveFull() {
		// Solve a full puzzle
		Sudoku<Integer> solved = Sudoku9x9.fromString(solvedStr);

		Sudoku<Integer> puzzle = Sudoku9x9.fromString(puzzleStr);

		Set<Sudoku<Integer>> solutions = Solver.solve(puzzle).collect(Collectors.toSet());

		assertEquals(Set.of(solved), solutions);
	}

	@Test
	final void testSolveMultipleSolutions() {
		// Solve a full puzzle with multiple solutions
		Sudoku<Integer> solved = Sudoku9x9.fromString(solvedStr);

		Sudoku<Integer> puzzle = Sudoku9x9.fromString(puzzleStr);
		// Remove the last two symbols, causing ambiguity in the solution
		puzzle.clearCellSymbol(79);
		puzzle.clearCellSymbol(80);

		Set<Sudoku<Integer>> solutions = Solver.solve(puzzle).collect(Collectors.toSet());

		assertTrue(solutions.size() > 1);
		assertTrue(solutions.contains(solved));
	}
	
	@Test
	final void testSolveInvalid() {
		Sudoku<Integer> sudoku = Sudoku9x9.fromString(puzzleStr);
		
		// Set the two first cells to the same symbol, so the sudoku is invalid
		sudoku.setCellSymbol(0, 1);
		sudoku.setCellSymbol(1, 1);
		
		assertTrue(sudoku.hasErrors());
		
		Set<Sudoku<Integer>> solutions = Solver.solve(sudoku).collect(Collectors.toSet());
		
		assertTrue(solutions.isEmpty());
	}
}
