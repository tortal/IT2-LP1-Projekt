package it.chalmers.tendu.gamemodel;

/**
 * Stateful Singleton. Represents the player of this particular device.
 */
public class Player {
	private static Player instance = null;

	private String mac; // macAdress of device/player
	private boolean host; // is the player host

	private Player() {
		mac = "";
		host = false;
	}

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
		host = isHost;
	}

	public boolean isHost() {
		return host;
	}

}
