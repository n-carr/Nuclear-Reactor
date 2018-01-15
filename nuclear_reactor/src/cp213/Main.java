package cp213;

import javax.swing.JFrame;

/**
 * Methods to run a reactor or controller.
 *
 * @author Nathaniel Carr
 * @version 2017-12-03
 *
 */
public class Main {

	public static void main(String args[]) {

		final MenuView frame = new MenuView();
		frame.setSize(400, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
		frame.setVisible(true);

	}

}
