public class TSPRunner {

	public static void main(String args[]) {

		// Default values
		Generation.POP_SIZE = 16;
		Generation.ELITISM = true;
		Generation.MUTATION = true;

		TSPDisplay app = new TSPDisplay(16, 4, 4, true, true);

		app.setLocation(50, 50); // left, top of initial display
		app.setSize(900, 600); // width, height of initial display
		app.setVisible(true); // start the application

	}

}
