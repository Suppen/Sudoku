package no.suppen.sudoku;

public class Main {
	public static void main(String[] args) {
		var sudoku = Sudoku9x9.empty();

		var solutions = Solver.solve(sudoku);

		solutions.forEach(solution -> {
			System.out.println(Sudoku9x9.toPrettyString(solution));
			System.out.println("-----------");
		});
	}
}