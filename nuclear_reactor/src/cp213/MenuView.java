package cp213;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * A class to act as a menu for the simulation. Allows user to choose initial
 * values for temperature and rodsHeight and select whether they want to run
 * ReactorController.
 *
 * @author Nathaniel Carr
 * @version 2017-12-03
 *
 */
@SuppressWarnings("serial")
public class MenuView extends JFrame {

	// ---------------------------------------------------------------
	/**
	 * Allow user to complete their selections and run the simulation.
	 *
	 * @author Nathaniel Carr
	 * @version 2017-12-03
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			double temperature = (double) MenuView.this.spnTemperature.getValue();
			int rodsHeight = (int) MenuView.this.spnRodsHeight.getValue();
			boolean automatic = MenuView.this.chkAutomatic.isSelected();
			int tickSpeed = (int) MenuView.this.spnTickSpeed.getValue();

			// Create Reactor
			Reactor reactor = new Reactor(temperature, rodsHeight, tickSpeed);

			// Create ReactorController
			ReactorController rc = new ReactorController(reactor, !automatic);

			// Create SimulationView
			SimulationView frame = new SimulationView(reactor, rc);

			// Layout and display SimulationView
			frame.setSize(675, 225);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocation(0, 0);
			frame.setVisible(true);

			// Start reactor.
			threadPool.execute(reactor);

			MenuView.this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			MenuView.this.setVisible(false);
			MenuView.this.dispose();

		}

	}

	// ---------------------------------------------------------------
	/**
	 * Private properties of StatusPanel class.
	 */

	private final static ExecutorService threadPool = Executors.newCachedThreadPool();

	private JLabel lblTemperature = new JLabel("Initial temperature (°C):");
	private JLabel lblRodsHeight = new JLabel("Initial rod insertion length (cm):");
	private JLabel lblTickSpeed = new JLabel("Tick speed (larger is slower):");
	private JSpinner spnTemperature = new JSpinner(new SpinnerNumberModel(26, Reactor.MIN_TEMP, Reactor.MAX_TEMP, 0.1));
	private JSpinner spnRodsHeight = new JSpinner(new SpinnerNumberModel(0, 0, Reactor.ROD_LENGTH, 1));
	private JSpinner spnTickSpeed = new JSpinner(new SpinnerNumberModel(200, 1, 5000, 50));
	private JCheckBox chkAutomatic = new JCheckBox("Automate (via ReactorController)");
	private JButton btnStart = new JButton("Start Simulation");

	/**
	 * StatusPanel constructor.
	 */
	MenuView() {
		this.layoutView();
		this.registerListeners();
	}

	// ---------------------------------------------------------------
	/**
	 * Uses the GridBagLayout to place the labels, spinners, checkboxes, and
	 * buttons.
	 */
	private void layoutView() {
		this.setTitle("Reactor Simulation Menu");

		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		// Allow cells to fill entire frame.
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		lblTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTemperature, c);

		c.gridx = 0;
		c.gridy = 1;
		lblRodsHeight.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblRodsHeight, c);

		c.gridx = 0;
		c.gridy = 2;
		lblTickSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTickSpeed, c);

		c.gridx = 0;
		c.gridy = 3;
		chkAutomatic.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(chkAutomatic, c);

		c.gridx = 1;
		c.gridy = 0;
		this.add(spnTemperature, c);

		c.gridx = 1;
		c.gridy = 1;
		this.add(spnRodsHeight, c);

		c.gridx = 1;
		c.gridy = 2;
		this.add(spnTickSpeed, c);

		c.gridx = 1;
		c.gridy = 3;
		btnStart.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(btnStart, c);
	}

	// ---------------------------------------------------------------
	/**
	 * Assigns listener to the button.
	 */
	private void registerListeners() {
		this.btnStart.addActionListener(new ButtonListener());
	}

}
