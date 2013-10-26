package it.chalmers.tendu.gamemodel;


/**
 * A Timer class - used by {@link MiniGame}s and controllers to keep track of
 * the remaining time in a given minigame.
 * Also used by screens to time different gui related stuff
 * 
 */
public class SimpleTimer {

	public static enum STATE {
		RUNNING, PAUSED, DONE
	}

	private long totalTime;
	private long remainingTime;
	private long endTime;
	private SimpleTimer.STATE state;

	/**
	 * Creates a SimpleTimer with its state initially set to
	 * {@link STATE#PAUSED}.
	 */
	public SimpleTimer() {
		state = STATE.PAUSED;
	}

	/**
	 * Starts a timer with the specified amount of time. If the timer is already
	 * running or has ended calling this method will do nothing (return false)
	 * 
	 * @param time
	 *            time in milliseconds
	 * @return <code>true</code> if timer was started.
	 */
	public boolean start(long time) {
		if (state == STATE.RUNNING || state == STATE.DONE) {
			return false;
		}

		totalTime = time;
		remainingTime = totalTime;
		setEndTime(totalTime);
		state = STATE.RUNNING;
		return true;
	}

	/**
	 * Restarts the timer with the specified amount of time. Will overwrite any
	 * previously running timer
	 * 
	 * @param time
	 *            time in milliseconds
	 */
	public void restart(long time) {
		state = STATE.PAUSED; // start timer will change this to running
		start(time);
	}

	/**
	 * Pause the timer
	 */
	public void pause() {
		if (state == STATE.DONE) { // can't pause if done
			return;
		}
		getRemainingTime();
		state = STATE.PAUSED;
	}

	/**
	 * Resume the timer
	 */
	public void resume() {
		setEndTime(getRemainingTime());
		state = STATE.RUNNING;
	}

	/**
	 * @param time
	 *            the change in milliseconds, can be positive or negative;
	 */
	public void change(long time) {
		endTime = endTime + time;
	}

	/**
	 * Gets the remaining time
	 * 
	 * @return time left in milliseconds
	 */
	public long getRemainingTime() {
		if (state == STATE.RUNNING) {
			if (remainingTime > 0) {
				remainingTime = endTime - System.currentTimeMillis();
			} else {
				remainingTime = 0;
				state = STATE.DONE; // the timer is done
			}
		}
		return remainingTime;
	}

	private void setEndTime(long time) {
		this.endTime = time + System.currentTimeMillis();
	}

	/**
	 * Check if the timer is running
	 * 
	 * @return true if timer is running
	 */
	public boolean isRunning() {
		getRemainingTime();
		if (state == STATE.RUNNING) {
			return true;
		}

		return false;
	}

	/**
	 * Check if the timer is done
	 * 
	 * @return true if timer is done
	 */
	public boolean isDone() {
		getRemainingTime();
		if (state == STATE.DONE) {
			return true;
		}
		return false;
	}

	/**
	 * Stop the timer
	 */
	public void stop() {
		getRemainingTime();
		state = STATE.DONE;
	}
}
