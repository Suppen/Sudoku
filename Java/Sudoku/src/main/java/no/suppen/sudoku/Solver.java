package no.suppen.sudoku;

import java.util.Set;
import java.util.stream.Stream;

public class Solver {
	/**
	 * Finds all solutions to a sudoku
	 * 
	 * @param <S>   Type of symbols the sudoku can have
	 * @param sudoku The sudoku to solve. Will not be mutated
	 * 
	 * @return A stream of all possible solutions to the sudoku
	 */
	public static <S> Stream<Sudoku<S>> solve(Sudoku<S> sudoku) {
		// A sudoku with errors has no solutions
		if (sudoku.hasErrors()) {
			System.out.println("Error");
			return Stream.empty();
		}

		// Do not mess with the original
		Sudoku<S> boardCopy = new Sudoku<>(sudoku);

		// If it is already solved, no need to do anything
		if (boardCopy.isSolved()) {
			return Stream.of(boardCopy);
		}

		// Fill all cells with only one possible candidate
		boolean changed = boardCopy.emptyCellIndices().anyMatch(i -> {
			Set<S> candidates = boardCopy.getCandidatesForCell(i);
			if (candidates.size() == 1) {
				// Is there an easier way to extract one (the only) element from a set?
				for (S symbol : candidates) {
					boardCopy.setCellSymbol(i, symbol);
				}
				return true;
			}
			return false;
		});

		// If any cells were filled, keep going with the now more filled sudoku
		if (changed) {
			return Solver.solve(boardCopy);
		}

		// Otherwise brute force is needed
		return boardCopy.emptyCellIndices().boxed()
				.flatMap(i -> boardCopy.getCandidatesForCell(i).stream().flatMap(candidate -> {
					boardCopy.setCellSymbol(i, candidate);
					return Solver.solve(boardCopy);
				}));
	}
}
