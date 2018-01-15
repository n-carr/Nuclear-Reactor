package cp213;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A simple nuclear reactor simulation. Given a starting temperature and control
 * rods heights, attempt to control the reactor over a period of time.
 *
 * @author Nathaniel Carr
 * @version 2017-12-03
 *
 */
public class Reactor implements Runnable {

	// ---------------------------------------------------------------
	/**
	 * Enumerated type for the Reactor status. The reactor is assumed to be in
	 * OPERATING mode at the beginning of the simulation.
	 */
	public enum Status {
		FINISHED("Finished"), MELTDOWN("MELTDOWN!!"), OPERATING("Operating"), SHUTDOWN("Shutdown");

		private String statusString;

		Status(final String statusString) {
			this.statusString = statusString;
		}

		@Override
		public String toString() {
			return this.statusString;
		}
	}

	// ---------------------------------------------------------------
	// Public Constants.
	// °C - Room temperature.
	public static final double MIN_TEMP = 25;
	// °C - Meltdown if exceeded.
	public static final double MAX_TEMP = 1000;
	// Minimum temperature at which power is generated.
	public static final double MIN_POWER_TEMP = 100;
	// Maximum power in Mw output at maximum temperature.
	public static final double MAX_POWER = 800;
	// Lengths of rods in cm.
	public static final int ROD_LENGTH = 200;
	// Temperature multiplier per tick.
	public static final double TEMP_FACTOR = 1.125;
	// Range of temperature decrease/increase.
	public static final int RAND_HIGH = 3;
	public static final int RAND_LOW = 3;

	// ---------------------------------------------------------------
	/**
	 * Allows views to listen to generic changes in the model.
	 */
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	// ---------------------------------------------------------------
	/**
	 * Private properties of Reactor class.
	 */
	private int tickSpeed = 200;
	private Status status = Status.OPERATING;
	private int ticks = 0;
	private int rodsHeight = 0;
	private double temperature = 0;
	private double avgTemperature = 0;
	private double power = 0;
	private double avgPower = 0;
	private boolean rodsMoved = false;
	private boolean rodsDropped = false;

	/**
	 * Reactor constructor.
	 *
	 * @param initialTemperature
	 *            The initial temperature of the reactor.
	 * @param initialRodsHeight
	 *            The initial heights of the reactor control rods.
	 * @param tickSpeed
	 *            The speed at which the ticks will occur.
	 */
	public Reactor(final double initialTemperature, final int initialRodsHeight, final int tickSpeed) {

		this.rodsHeight = initialRodsHeight;
		this.temperature = initialTemperature;
		this.tickSpeed = tickSpeed;
		this.avgTemperature = initialTemperature;
		this.avgPower = Math.max(0, Math.min((initialTemperature - 100) * MAX_POWER / (MAX_TEMP - 100), MAX_POWER));

		if (this.temperature <= MIN_TEMP) {
			this.status = Status.SHUTDOWN;
		} else if (this.temperature >= MAX_TEMP) {
			this.status = Status.MELTDOWN;
		}

	}

	// ---------------------------------------------------------------
	/**
	 * Attaches listeners to the model.
	 *
	 * @param listener
	 *            The listener to attach to the model.
	 */
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	// ---------------------------------------------------------------
	/**
	 * Attaches listeners to the model for a particular property.
	 *
	 * @param propertyName
	 *            The name of the property to listen for.
	 * @param listener
	 *            The listener to attach to the model.
	 */
	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * Drops the rods entirely into the reactor core - i.e. set the rods lengths to
	 * the maximum rods lengths.
	 */
	public void dropRods() {

		if (this.status == Status.OPERATING && !this.rodsMoved) {
			this.rodsHeight = ROD_LENGTH;
			this.rodsMoved = true;
			this.rodsDropped = true;
		}

	}

	/**
	 * Returns the average power produced by the reactor since the start of a
	 * simulation.
	 *
	 * @return average power.
	 */
	public double getAveragePower() {

		return this.avgPower;

	}

	/**
	 * Returns the average temperature of the reactor since the start of a
	 * simulation.
	 *
	 * @return average temperature.
	 */
	public double getAverageTemperature() {

		return this.avgTemperature;

	}

	/**
	 * Returns the reactor's current power level.
	 *
	 * @return power.
	 */
	public double getPower() {

		return this.power;

	}

	/**
	 * Returns the reactor's current rod heights.
	 *
	 * @return rodsHeight.
	 */
	public int getRodsHeight() {

		return this.rodsHeight;

	}

	/**
	 * Returns the reactor's current status.
	 *
	 * @return status.
	 */
	public Status getStatus() {

		return this.status;

	}

	/**
	 * Returns the reactor's current temperature.
	 *
	 * @return temperature.
	 */
	public double getTemperature() {

		return this.temperature;

	}

	/**
	 * Returns the number of ticks since the beginning of a simulation.
	 *
	 * @return ticks
	 */
	public int getTicks() {

		return this.ticks;

	}

	/**
	 * Lower the rod heights by one step. Rods cannot be lowered by more than one
	 * step per tick.
	 */
	public void lowerRods() {

		// Allow rods to be lowered if the reactor is operating, no other movements have
		// been made in this tick, the rods have not been dropped, and the rods are not
		// already fully inserted.
		if (this.status == Status.OPERATING && !this.rodsMoved && !this.rodsDropped && this.rodsHeight < ROD_LENGTH) {
			this.rodsHeight++;
			this.rodsMoved = true;
		}

	}

	/**
	 * Raise the rod heights by one step. Rods cannot be raised by more than one
	 * step per tick.
	 */
	public void raiseRods() {

		// Allow rods to be raised if the reactor is operating, no other movements have
		// been made in this tick, the rods have not been dropped, and the rods are not
		// already fully removed.
		if (this.status == Status.OPERATING && !this.rodsMoved && !this.rodsDropped && this.rodsHeight > 0) {
			this.rodsHeight--;
			this.rodsMoved = true;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 *
	 * Run the reactor simulation.
	 */
	@Override
	public void run() {

		try {
			while (this.status == Status.OPERATING) {
				Thread.sleep(this.tickSpeed);
				this.tick();
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Increment the simulation tick by one. Update the reactor temperature, power,
	 * and status (in that order), and allow the rods to be raised or lowered during
	 * this tick.
	 */
	public void tick() {

		// Update number of ticks.
		this.ticks++;

		// Update temperature.
		this.temperature = Math.min(Math.max(TEMP_FACTOR * this.temperature - this.rodsHeight
				+ (int) (Math.random() * (RAND_HIGH - RAND_LOW + 1)) + RAND_LOW, MIN_TEMP), MAX_TEMP);

		// Update power.
		if (this.temperature > MIN_POWER_TEMP) {
			this.power = Math.max(0, Math.min((this.temperature - 100) * MAX_POWER / (MAX_TEMP - 100), MAX_POWER));
		} else {
			this.power = 0;
		}

		// Update status.
		if (this.temperature >= MAX_TEMP) {
			this.status = Status.MELTDOWN;
		} else if (this.temperature <= MIN_TEMP) {
			this.status = Status.SHUTDOWN;
		}

		// Update averages only if still operating.
		if (this.status == Status.OPERATING) {
			this.avgTemperature = (this.avgTemperature * this.ticks + this.temperature) / (this.ticks + 1);
			this.avgPower = (this.avgPower * this.ticks + this.power) / (this.ticks + 1);

			// Allow another rod motion for the next tick.
			this.rodsMoved = false;
		}

		// Signal general property change.
		this.pcs.firePropertyChange(null, null, null);

	}

	/**
	 * Sets reactor status to FINISHED.
	 */
	public void quit() {
		this.status = Status.FINISHED;
		return;
	}

}
