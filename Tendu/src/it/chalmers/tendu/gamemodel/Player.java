package it.chalmers.tendu.gamemodel;

/**
 * Stateful Singleton. Represents the player of this particular device.
 */
public class Player {
	private static Player instance = null;

	/**
	 * MAC-address of this device. To be set manually by the platform-specific
	 * network implementation.
	 */
	private String mac;
	/**
	 * TRUE if this device is acting as server.
	 */
	private boolean isHost;

	private Player() {
		mac = "";
		isHost = false;
	}

	/**
	 * @return the Player singleton.
	 */
	public static Player getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new Player();

		return instance;
	}

	public void setMac(String myMac) {
		mac = myMac;
	}

	public String getMac() {
		return mac;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public boolean isHost() {
		return isHost;
	}

}
