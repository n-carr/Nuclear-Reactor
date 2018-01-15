package cp213;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//---------------------------------------------------------------
/**
 * View the status of the reactor model and the insertion length of the cooling
 * rods through labels.
 *
 * @author Nathaniel Carr
 * @version 2017-12-03
 */
@SuppressWarnings("serial")
public class StatusPanel extends JPanel {

	// -------------------------------------------------------------------------------
	/**
	 * An inner class the listens for the changes in the model in order to update
	 * the labels accordingly.
	 */
	private class ChangeTextListener implements PropertyChangeListener {

		@Override
		public void propertyChange(final PropertyChangeEvent arg0) {
			StatusPanel.this.lblStatus.setText(StatusPanel.this.model.getStatus().toString());
			StatusPanel.this.lblInsertion.setText("Rod insertion: " + StatusPanel.this.model.getRodsHeight());
		}
	}

	// The reactor to control.
	private Reactor model;

	// ---------------------------------------------------------------
	/**
	 * Private properties of StatusPanel class.
	 */
	private JTextField txtStatus = new JTextField("STATUS");
	private JLabel lblStatus = new JLabel(" ");
	private JLabel lblInsertion = new JLabel("");

	/**
	 * StatusPanel constructor.
	 *
	 * @param model
	 *            The reactor to control.
	 */
	StatusPanel(final Reactor model) {
		this.model = model;
		this.layoutView();
		this.registerListeners();
	}

	// ---------------------------------------------------------------
	/**
	 * Uses the GridLayout to place the labels.
	 */
	private void layoutView() {
		this.setLayout(new GridLayout(6, 1));

		txtStatus.setEditable(false);
		txtStatus.setHorizontalAlignment(SwingConstants.CENTER);
		txtStatus.setBackground(new Color(200, 200, 200));
		txtStatus.setFont(txtStatus.getFont().deriveFont(txtStatus.getFont().getStyle() | Font.BOLD));
		this.add(txtStatus);

		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblStatus);

		this.add(new JLabel(""));
		this.add(new JLabel(""));
		this.add(new JLabel(""));

		lblInsertion.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblInsertion);

		lblStatus.setText(this.model.getStatus().toString());
		lblInsertion.setText("Rod insertion: " + this.model.getRodsHeight());

	}

	// ---------------------------------------------------------------
	/**
	 * Assigns listener to the model.
	 */
	private void registerListeners() {
		// Add model listener.
		this.model.addPropertyChangeListener(new ChangeTextListener());
	}

}
