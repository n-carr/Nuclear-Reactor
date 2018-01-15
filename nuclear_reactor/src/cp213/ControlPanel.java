package cp213;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//---------------------------------------------------------------
/**
 * View and update the reactor model with buttons that allow the user to end the
 * simulation and (if not set to automated in its initialization) drop, raise,
 * and lower the rods.
 *
 * @author Nathaniel Carr
 * @version 2017-12-03
 */
@SuppressWarnings("serial")
public class ControlPanel extends JPanel {

	// ---------------------------------------------------------------
	/**
	 * An inner class that uses an ActionListener to access the buttons. It sets the
	 * model values when the button is pressed.
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// Detemines which button was pressed.
			String action = ((JButton) e.getSource()).getText();

			switch (action) {

			case "End Simulation":
				ControlPanel.this.model.quit();
				break;

			case "Drop Rods":
				ControlPanel.this.model.dropRods();
				break;

			case "Lower Rods":
				ControlPanel.this.model.lowerRods();
				break;

			case "Raise Rods":
				ControlPanel.this.model.raiseRods();
				break;

			case "Automatic On":
				synchronized (ControlPanel.this.rc) {
					ControlPanel.this.rc.setSuspended(false);
					ControlPanel.this.fixPanel();
				}
				break;

			case "Automatic Off":
				synchronized (ControlPanel.this.rc) {
					ControlPanel.this.rc.setSuspended(true);
					ControlPanel.this.fixPanel();
				}
				break;

			}

		}

	}

	// -------------------------------------------------------------------------------
	/**
	 * An inner class the listens for the model to stop operating and disables all
	 * buttons when this occurs.
	 */
	private class OperatingListener implements PropertyChangeListener {

		@Override
		public void propertyChange(final PropertyChangeEvent arg0) {
			// Disable every button if the reactor is no longer operating.
			if (ControlPanel.this.model.getStatus() != Reactor.Status.OPERATING) {
				ControlPanel.this.btnEnd.setEnabled(false);
				ControlPanel.this.btnDrop.setEnabled(false);
				ControlPanel.this.btnRaise.setEnabled(false);
				ControlPanel.this.btnLower.setEnabled(false);
				ControlPanel.this.btnSwitchMode.setEnabled(false);
			}
		}
	}

	// The reactor to control.
	private Reactor model;

	// The reactorcontroller.
	private ReactorController rc;

	// ---------------------------------------------------------------
	/**
	 * Private properties of ControlPanel class.
	 */
	private JTextField txtControls = new JTextField("CONTROLS");
	private JButton btnEnd = new JButton("End Simulation");
	private JButton btnDrop = new JButton("Drop Rods");
	private JButton btnRaise = new JButton("Raise Rods");
	private JButton btnLower = new JButton("Lower Rods");
	private JButton btnSwitchMode = new JButton("Automatic On");

	/**
	 * ControlPanel constructor.
	 *
	 * @param model
	 *            The reactor to control.
	 * @param rc
	 *            The reactorcontroller to control.
	 */
	ControlPanel(final Reactor model, final ReactorController rc) {
		this.model = model;
		this.rc = rc;

		this.layoutView();
		this.registerListeners();

		// Remove the functionality of all buttons except btnEnd if the simulation is
		// being controlled by a ReactorController.
		fixPanel();

	}

	private void fixPanel() {
		// Remove the functionality of all buttons except btnEnd if the simulation is
		// being controlled by a ReactorController. Add functionality otherwise.
		if (model.getStatus() == Reactor.Status.OPERATING) {
			if (rc.getSuspended()) {
				btnSwitchMode.setText("Automatic On");
				btnDrop.setEnabled(true);
				btnRaise.setEnabled(true);
				btnLower.setEnabled(true);
			} else {
				btnSwitchMode.setText("Automatic Off");
				btnDrop.setEnabled(false);
				btnRaise.setEnabled(false);
				btnLower.setEnabled(false);
			}
		}
	}

	// ---------------------------------------------------------------
	/**
	 * Uses the GridLayout to place the buttons.
	 */
	private void layoutView() {

		this.setLayout(new GridLayout(6, 1));

		txtControls.setHorizontalAlignment(SwingConstants.CENTER);
		txtControls.setEditable(false);
		txtControls.setBackground(new Color(200, 200, 200));
		txtControls.setFont(txtControls.getFont().deriveFont(txtControls.getFont().getStyle() | Font.BOLD));
		this.add(txtControls);

		this.add(btnSwitchMode);

		this.add(btnEnd);
		this.add(btnDrop);
		this.add(btnRaise);
		this.add(btnLower);
	}

	// ---------------------------------------------------------------
	/**
	 * Assigns listeners to the view widgets and the model.
	 */
	private void registerListeners() {

		// Add widget listeners.
		this.btnEnd.addActionListener(new ButtonListener());
		this.btnDrop.addActionListener(new ButtonListener());
		this.btnRaise.addActionListener(new ButtonListener());
		this.btnLower.addActionListener(new ButtonListener());
		this.btnSwitchMode.addActionListener(new ButtonListener());

		// Add model listener.
		this.model.addPropertyChangeListener(new OperatingListener());
	}

}
