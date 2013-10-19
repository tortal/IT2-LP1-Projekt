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

public class NumberGame extends MiniGame {
	public final static String TAG = "MiniGame";

	private int playerCount;
	private int playerListSize;
	private ArrayList<Integer> answerList;
	private Map<Integer, ArrayList<Integer>> playerLists;
	private int nbrCorrectAnswer;

	private ArrayList<Integer> listOfNumbers;

	// For reflection.
	@SuppressWarnings("unused")
	private NumberGame() {
	};

	public NumberGame(long extraTime, Difficulty difficulty,
			Map<String, Integer> players) {
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

		setUpGamePlay(difficulty, extraTime);

		// Populate the player lists with their own correct numbers and then
		// fill it up with dummy numbers.
		playerLists = divideAndConquer(answerList);
	}

	public void setUpGamePlay(Difficulty difficulty, long extraTime) {
		if (playerCount == 1) {
			switch (difficulty) {
			case ONE:
				this.setGameTime(30000, extraTime);
				answerList = createAnswer(2);
				break;
			case TWO:
				this.setGameTime(30000, extraTime);
				answerList = createAnswer(3);
				break;
			case THREE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(4);
				break;
			case FOUR:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(6);
				break;
			case FIVE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(8);
				break;
			default:
				answerList = null;
				break;
			}
		} else if (playerCount == 2) {
			switch (difficulty) {
			case ONE:
				this.setGameTime(30000, extraTime);
				answerList = createAnswer(2);
				break;
			case TWO:
				this.setGameTime(30000, extraTime);
				answerList = createAnswer(4);
				break;
			case THREE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(6);
				break;
			case FOUR:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(8);
				break;
			case FIVE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(12);
				break;
			default:
				answerList = null;
				break;
			}
		} else if (playerCount == 3) {
			switch (difficulty) {
			case ONE:
				this.setGameTime(30000, extraTime);
				answerList = createAnswer(3);
				break;
			case TWO:
				this.setGameTime(30000, extraTime);
				answerList = createAnswer(5);
				break;
			case THREE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(7);
				break;
			case FOUR:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(10);
				break;
			case FIVE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(14);
				break;
			default:
				answerList = null;
				break;
			}
		} else if (playerCount == 4) {
			switch (difficulty) {
			case ONE:
				this.setGameTime(30000, extraTime);
				answerList = createAnswer(4);
				break;
			case TWO:
				this.setGameTime(30000, extraTime);
				answerList = createAnswer(6);
				break;
			case THREE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(8);
				break;
			case FOUR:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(12);
				break;
			case FIVE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(16);
				break;
			default:
				answerList = null;
				break;
			}
		}
	}

	/**
	 * Changes the state of the game to running However it does not start the
	 * timer
	 */
	@Override
	public void startGameTimer() {
		super.startGameTimer();
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
		return (answerList.get(nbrCorrectAnswer) == num);
	}

	/**
	 * Plusing counter for number of correct answers.
	 */
	public void guessedCorrectly() {
		nbrCorrectAnswer++;
	}

	/**
	 * return the list with the correct answers.
	 * 
	 * @return
	 */
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

	@Override
	public GameState checkGameState() {
		if (answerList.size() == nbrCorrectAnswer) {
			return GameState.WON;
		} else if (timerIsDone()) {
			return GameState.LOST;
		}
		return GameState.RUNNING;
	}

	@Override
	public GameResult getGameResult() {
		if (checkGameState() == GameState.WON
				|| checkGameState() == GameState.LOST) {
			long spentTime = (getGameTime() - getRemainingTime());
			GameResult result = new GameResult(getGameId(), spentTime,
					getRemainingTime(), checkGameState());
			return result;
		}
		return null;
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

}
