package cp213;

import java.awt.GridLayout;

import javax.swing.JFrame;

/**
 * A class to act as a frame for ControlPanel, StatusPanel, and SimulationPanel.
 *
 * @author Nathaniel Carr
 * @version 2017-12-03
 *
 */
@SuppressWarnings("serial")
public class SimulationView extends JFrame {

	// ---------------------------------------------------------------
	/**
	 * Private properties of StatusPanel class. The panels displayed in this JFrame.
	 */
	private ControlPanel controlPanel;
	private StatusPanel statusPanel;
	private SummaryPanel summaryPanel;

	/**
	 * StatusPanel constructor.
	 *
	 * @param model
	 *            The reactor to pass to the JPanels.
	 * @param automatic
	 *            Whether or not the ReactorController is active. Necessary for
	 *            controlPanel.
	 * 
	 */
	SimulationView(final Reactor model, final ReactorController rc) {

		this.setTitle("Reactor Simulation");

		this.controlPanel = new ControlPanel(model, rc);
		this.statusPanel = new StatusPanel(model);
		this.summaryPanel = new SummaryPanel(model);

		this.layoutView();
	}

	// ---------------------------------------------------------------
	/**
	 * Uses the GridLayout to place the JPanels.
	 */
	private void layoutView() {

		this.setLayout(new GridLayout(1, 3));
		this.add(this.controlPanel);
		this.add(this.statusPanel);
		this.add(this.summaryPanel);

	}

}
