package it.chalmers.tendu.gamemodel;

import java.io.Serializable;

/** Class for sending the gamestate over the network
 * 
 * @author johnpetersson
 *
 */
public class GameStateBundle implements Serializable {
	private int gameType;
	private String gameName;
	
	public GameStateBundle() {
		gameType = 1;
		gameName = "Default game";
	}
	
	public GameStateBundle(int type, String name) {
		gameType = type;
		gameName = name;
	}

	@Override
	public String toString() {
		return "GameStateBundle [gameType=" + gameType + ", gameName="
				+ gameName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gameName == null) ? 0 : gameName.hashCode());
		result = prime * result + gameType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameStateBundle other = (GameStateBundle) obj;
		if (gameName == null) {
			if (other.gameName != null)
				return false;
		} else if (!gameName.equals(other.gameName))
			return false;
		if (gameType != other.gameType)
			return false;
		return true;
	}
	
	
	
	
}
