package cp213;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A class to control a Reactor model. It's job is to initialize a Reactor and
 * maximize its power output while avoiding a meltdown.
 *
 * @author Nathaniel Carr
 * @version 2017-12-03
 *
 */
public class ReactorController {

	private class ModelChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(final PropertyChangeEvent arg0) {

			// React to change in the model if not suspended.
			if (!ReactorController.this.suspended) {
				ReactorController.this.react();
			}

		}
	}

	// The reactor to control.
	private Reactor model = null;

	private boolean suspended = false;

	/**
	 * Constructor.
	 *
	 * @param model
	 *            The reactor to control.
	 * @param suspended
	 *            Whether or not the ReactorController should start suspended.
	 * 
	 */
	public ReactorController(Reactor model, boolean suspended) {
		this.model = model;
		this.model.addPropertyChangeListener(new ModelChangeListener());

		this.suspended = suspended;

		// React immediately after being created if not suspended.
		if (!this.suspended) {
			this.react();
		}
	}

	/**
	 * Change whether or not the ReactorController is suspended.
	 *
	 * @param suspended
	 *            The new value for the suspended attribute.
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	/**
	 * Get the current value of the suspended attribute
	 * 
	 * @return true if suspended, false if not.
	 */
	public boolean getSuspended() {
		return this.suspended;
	}

	/**
	 * Determines which action to take.
	 */
	private void react() {

		if (mayMeltdown(this.model.getTemperature(), this.model.getRodsHeight())) {
			this.model.dropRods();
		} else if (canRaise(this.model.getTemperature(), this.model.getRodsHeight())) {
			this.model.raiseRods();
		} else if (!canNeglect(this.model.getTemperature(), this.model.getRodsHeight())) {
			this.model.lowerRods();
		}

	}

	/**
	 * Determines whether the reactor may meltdown in the next tick even if the rods
	 * are lowered.
	 */
	private boolean mayMeltdown(final double temperature, int rodsHeight) {
		return Reactor.TEMP_FACTOR * temperature - ++rodsHeight + Reactor.RAND_HIGH >= Reactor.MAX_TEMP;
	}

	private boolean canRaise(final double temperature, int rodsHeight) {

		double newTemperature = Reactor.TEMP_FACTOR * temperature - --rodsHeight + Reactor.RAND_HIGH;

		return canCool(newTemperature, temperature, rodsHeight);
	}

	private boolean canNeglect(final double temperature, int rodsHeight) {
		double newTemperature = Reactor.TEMP_FACTOR * temperature - rodsHeight + Reactor.RAND_HIGH;

		return canCool(newTemperature, temperature, rodsHeight);
	}

	private boolean canCool(double newTemperature, final double temperature, int rodsHeight) {
		boolean canCool = false;

		// Determine if new temperature can be brought down to initial temperature by
		// assuming worst-case scenario for each randomization and attempting to lower
		// the rods on each tick.
		while (newTemperature > temperature && newTemperature < Reactor.MAX_TEMP && rodsHeight <= Reactor.ROD_LENGTH) {
			newTemperature = Reactor.TEMP_FACTOR * newTemperature - ++rodsHeight + Reactor.RAND_HIGH;
		}

		// If the new temperature isn't at or about the max temperature and the rods are
		// not inserted farther than actually possible, then the temperature can be
		// brought down reliably.
		if (newTemperature < Reactor.MAX_TEMP && rodsHeight <= Reactor.ROD_LENGTH) {
			canCool = true;
		}

		return canCool;
	}

}
