package it.chalmers.tendu.gamemodel.numbergame;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.GameId;
import it.chalmers.tendu.gamemodel.GameResult;
import it.chalmers.tendu.gamemodel.GameState;
import it.chalmers.tendu.gamemodel.MiniGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class NumberGame extends MiniGame {

	private int playerCount;
	private int	playerListSize;
	private ArrayList<Integer> answerList;
	private Map<Integer, ArrayList<Integer>> playerLists;
	private int nbrCorrectAnswer;

	private ArrayList<Integer> listOfNumbers;

	/** No args constructor for reflection use */
	protected NumberGame() {
		super();
	};

	public NumberGame(long extraTime, Difficulty difficulty, Map<String, Integer> players) {
		super(difficulty, GameId.NUMBER_GAME, players);

		nbrCorrectAnswer = 0;
		playerListSize = 8;
		playerCount = players.size();

		// Create a list of numbers containing all numbers 1-99 an then shuffle
		// it.
		listOfNumbers = new ArrayList<Integer>();
		for (int i = 1; i < 100; i++) {
			listOfNumbers.add(i);
		}
		Collections.shuffle(listOfNumbers);

		// Create an answerList and set the game time according to difficulty.
		switch (difficulty) {
		case ONE:
			this.setStartTime(30000, extraTime);
			answerList = createAnswer(playerCount);
			break;
		case TWO:
			this.setStartTime(30000, extraTime);
			answerList = createAnswer(playerCount*2);
			break;
		case THREE:
			this.setStartTime(15000, extraTime);
			answerList = createAnswer(playerCount);
			break;
		case FOUR:
			this.setStartTime(15000, extraTime);
			answerList = createAnswer(playerCount*2);
			break;
		case FIVE:
			this.setStartTime(500, extraTime);
			answerList = createAnswer(playerCount*2);
			break;
		default:
			// TODO:
			Gdx.app.debug("NumberGame Class", "Fix this switch case");
			break;
		}

		// Populate the player lists with their own correct numbers and then
		// fill it up with dummy numbers.
		playerLists = divideAndConquer(answerList);

		Gdx.app.log("NumberGame", "Starttid = " + getGameTime());

	}
	
	/**
	 * Changes the state of the game to running
	 * However it does not start the timer
	 */
	@Override
	public void startGame() {
		super.startGame();;
		//setState(GameState.INSTRUCTING);
	}

	/**
	 * Check if the number chosen is the right one according to the answerList
	 * and sets gamestate to gameWon if all the numbers in answerList have been
	 * correctly guessed.
	 * 
	 * @param num
	 * @return
	 */
	public boolean checkNbr(int num) {
		// TODO make sure it can't go out of bounds (make it prettier)
		if (nbrCorrectAnswer < answerList.size()) {
			if (answerList.get(nbrCorrectAnswer) == num) {
				nbrCorrectAnswer++;
				if (nbrCorrectAnswer == answerList.size()) {
					gameWon();
				}
				return true;
			} else {
				this.changeTime(-3000);
				return false;
			}
		}
		return false;
	}

	public ArrayList<Integer> getAnswerList() {
		return answerList;
	}

	/**
	 * Return a players list of numbers.
	 * 
	 * @param player
	 * @return
	 */
	public ArrayList<Integer> getMyList() {
		int playerNbr = getplayerNbr();
		return playerLists.get(playerNbr);
		// return answerList;
	}

	/**
	 * Returns the numbers that have been answered correctly.
	 * 
	 * @return
	 */
	public ArrayList<Integer> getAnsweredNbrs() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < nbrCorrectAnswer; i++) {
			list.add(answerList.get(i));
		}
		return list;
	}


	/**
	 * Returns a list with random numbers from 1-99 that represents the correct
	 * answer in the game.
	 * 
	 * @param length
	 * @return
	 */
	private ArrayList<Integer> createAnswer(int length) {
		ArrayList<Integer> answerList = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			answerList.add(listOfNumbers.remove(i));
		}
		return answerList;
	}

	/**
	 * Give each player a part of the answer.
	 * 
	 * @param list
	 * @return
	 */
	private Map<Integer, ArrayList<Integer>> divideAndConquer(
			ArrayList<Integer> list) {
	
		Map<Integer, ArrayList<Integer>> newMap = new HashMap<Integer, ArrayList<Integer>>();
	
		ArrayList<Integer> temp = new ArrayList<Integer>(answerList);
	
		Collections.shuffle(temp);
	
		for (int i = 0; i < playerCount; i++) {
			ArrayList<Integer> newList = new ArrayList<Integer>();
	
			for (int j = 0; j < answerList.size() / playerCount; j++) {
				Integer r = temp.remove(0);
				newList.add(r);
			}
			popAndShuffleList(newList);
			newMap.put(i, newList);
		}
		return newMap;
	
	}

	/**
	 * Fills up an array with random numbers until there are eight different
	 * numbers in the array total and shuffles them.
	 * 
	 * @param list
	 */
	private void popAndShuffleList(ArrayList<Integer> list) {
		int length = playerListSize - list.size();
		for (int i = 0; i < length; i++) {
			list.add(listOfNumbers.remove(i));
		}
		Collections.shuffle(list);
	}

	@Override
	public GameResult getGameResult() {
		if(checkGameState() == GameState.WON || checkGameState() == GameState.LOST) {
			long spentTime = (getGameTime()-getRemainingTime());
			GameResult result = new GameResult(getGameId(), spentTime, getRemainingTime(), getGameState());
			return result;
		}
		
		return null;

	}

}
