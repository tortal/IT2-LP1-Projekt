package it.chalmers.tendu.gamemodel;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

/**
 * 
 * @author
 * 
 *         Keeps track of all the results during the current game session
 */
public class SessionResult {
	private List<GameResult> gameResults;

	public SessionResult() {
		gameResults = new ArrayList<GameResult>();
	}

	/**
	 * Add new result
	 * 
	 * @param result
	 */
	public void addResult(GameResult result) {
		gameResults.add(result);
	}

	/**
	 * 
	 * @return total mini games played this session
	 */
	public int gamesPlayed() {
		return gameResults.size();
	}

	/**
	 * @return total time spent played
	 */
	public float totalTimePlayed() {
		long time = 0;

		for (GameResult result : gameResults) {
			time = time + result.getTimePlayed();
		}
		
		return time;
	}

	/**
	 * 
	 * @return result of the last mini game played
	 */
	public GameResult getLastResult() {
		if(gameResults.size() == 0) {
			Gdx.app.log("SessionResult", "there is no result");
			return null;
		}
		
		int lastIndex = gameResults.size() - 1;
		return gameResults.get(lastIndex);
	}

	/**
	 * 
	 * @return result of the last mini game played
	 */
	public GameResult getResult(int index) {
		if (index < 0 || index > (gameResults.size() - 1)) {
			Gdx.app.log("SessionResult", "index out of bounds in getResult");
			return null;
		}
		return gameResults.get(index);
	}

	/**
	 * 
	 * @return time spent playing previous game
	 */
	public long timePlayedLastGame() {
		if(gameResults.size() == 0) {
			return 0;
		}
		
		long time = getLastResult().getTimePlayed();
		return time;
	}
	
	/**
	 * clears all results for the current session
	 */
	public void clear() {
		gameResults.clear();
	}
}
