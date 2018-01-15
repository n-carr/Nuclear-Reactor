package cp213;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//---------------------------------------------------------------
/**
 * View the reactor model's relevant properties with textfields and labels.
 *
 * @author Nathaniel Carr
 * @version 2017-12-03
 */
@SuppressWarnings("serial")
public class SummaryPanel extends JPanel {

	// -------------------------------------------------------------------------------
	/**
	 * An inner class the listens for the changes in the model in order to update
	 * the textfields accordingly.
	 */
	private class ChangeTextListener implements PropertyChangeListener {

		@Override
		public void propertyChange(final PropertyChangeEvent arg0) {
			SummaryPanel.this.txtTicks.setText("" + SummaryPanel.this.model.getTicks());
			SummaryPanel.this.txtTemperature
					.setText(String.format("%.5f", SummaryPanel.this.model.getTemperature()) + "°C");
			SummaryPanel.this.txtPower.setText(String.format("%.5f", SummaryPanel.this.model.getPower()) + "MW");
			SummaryPanel.this.txtAvgTemperature
					.setText(String.format("%.5f", SummaryPanel.this.model.getAverageTemperature()) + "°C");
			SummaryPanel.this.txtAvgPower
					.setText(String.format("%.5f", SummaryPanel.this.model.getAveragePower()) + "MW");
		}
	}

	// The reactor to control.
	private Reactor model;

	// ---------------------------------------------------------------
	/**
	 * Private properties of SummaryPanel class.
	 */
	private JTextField txtSummary = new JTextField("SUMMARY");

	private JLabel lblTicks = new JLabel("Ticks:");
	private JLabel lblTemperature = new JLabel("Temperature:");
	private JLabel lblPower = new JLabel("Power:");
	private JLabel lblAvgTemperature = new JLabel("Avg. Temperature:");
	private JLabel lblAvgPower = new JLabel("Avg. Power:");

	private JTextField txtTicks = new JTextField("           ");
	private JTextField txtTemperature = new JTextField("           ");
	private JTextField txtPower = new JTextField("           ");
	private JTextField txtAvgTemperature = new JTextField("           ");
	private JTextField txtAvgPower = new JTextField("           ");

	/**
	 * SummaryPanel constructor.
	 *
	 * @param model
	 *            The reactor to control.
	 */
	SummaryPanel(final Reactor model) {
		this.model = model;
		this.layoutView();
		this.registerListeners();
	}

	// ---------------------------------------------------------------
	/**
	 * Uses the GridBagLayout to place the labels and textfields.
	 */
	private void layoutView() {
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		// Allow cells to fill entire frame.
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;

		// Make txtSummary double the width of the other cells.
		c.gridwidth = 2;

		c.gridx = 0;
		c.gridy = 0;
		txtSummary.setHorizontalAlignment(SwingConstants.CENTER);
		txtSummary.setEditable(false);
		txtSummary.setBackground(new Color(200, 200, 200));
		txtSummary.setFont(txtSummary.getFont().deriveFont(txtSummary.getFont().getStyle() | Font.BOLD));
		this.add(txtSummary, c);

		// Return gridwidth to normal.
		c.gridwidth = 1;

		c.gridx = 0;
		c.gridy = 1;
		lblTicks.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTicks, c);

		c.gridx = 1;
		c.gridy = 1;
		txtTicks.setHorizontalAlignment(SwingConstants.CENTER);
		txtTicks.setEditable(false);
		this.add(txtTicks, c);

		c.gridx = 0;
		c.gridy = 2;
		lblTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTemperature, c);

		c.gridx = 1;
		c.gridy = 2;
		txtTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		txtTemperature.setEditable(false);
		this.add(txtTemperature, c);

		c.gridx = 0;
		c.gridy = 3;
		lblPower.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblPower, c);

		c.gridx = 1;
		c.gridy = 3;
		txtPower.setHorizontalAlignment(SwingConstants.CENTER);
		txtPower.setEditable(false);
		this.add(txtPower, c);

		c.gridx = 0;
		c.gridy = 4;
		lblAvgTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblAvgTemperature, c);

		c.gridx = 1;
		c.gridy = 4;
		txtAvgTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		txtAvgTemperature.setEditable(false);
		this.add(txtAvgTemperature, c);

		c.gridx = 0;
		c.gridy = 5;
		lblAvgPower.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblAvgPower, c);

		c.gridx = 1;
		c.gridy = 5;
		txtAvgPower.setHorizontalAlignment(SwingConstants.CENTER);
		txtAvgPower.setEditable(false);
		this.add(txtAvgPower, c);

		this.txtTicks.setText("" + this.model.getTicks());
		this.txtTemperature.setText(String.format("%.5f", this.model.getTemperature()) + "°C");
		this.txtPower.setText(String.format("%.5f", this.model.getPower()) + "MW");
		this.txtAvgTemperature.setText(String.format("%.5f", this.model.getAverageTemperature()) + "°C");
		this.txtAvgPower.setText(String.format("%.5f", this.model.getAveragePower()) + "MW");

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
