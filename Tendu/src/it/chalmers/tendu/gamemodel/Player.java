package it.chalmers.tendu.gamemodel;

/**
 * Represents the player of this particular device.
 */
/**
 * The user of the device.
 * A Singleton conveniently accessible.
 *
 */
public class Player {
	private static Player instance = null;

	private String mac;
	private boolean host;

	private Player() {
		mac = null;
		host = false;
	}

	/**
	 * @return the Player-singleton. 
	 * If nothing has been set, initially MAC will be null and isHost() will return false.
	 */
	public static Player getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new Player();

		return instance;
	}

	/**
	 * @param myMac MAC-address to set.
	 */
	public void setMac(String myMac) {
		mac = myMac;
	}

	/**
	 * @return the players MAC (must have been set)
	 */
	public String getMac() {
		return mac;
	}
	
	/**
	 * 
	 * @param isHost
	 */
	public void setHost(boolean isHost) {
		host = isHost;
	}
	
	/**
	 * @return if this player has been set to be host.
	 */
	public boolean isHost() {
		return host;
	}

}
