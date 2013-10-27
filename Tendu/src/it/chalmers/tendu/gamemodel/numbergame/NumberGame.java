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

/**
 * NumberGame lists a numeric sequence for a short duration and then requires
 * players to memorize them by entering the sequence in the correct order.
 * 
 * The numbers are distributed randomly among players along with dummy numbers
 * that did not exist in the correct sequence. Any failed attempt (i.e. an
 * incorrect guess) will penalize the team by removing time.
 * 
 */
public class NumberGame extends MiniGame {
	public final static String TAG = "MiniGame";

	/**
	 * Number of players participating.
	 */
	private int playerCount;

	private int playerListSize;

	private ArrayList<Integer> answerList;

	private final Map<Integer, ArrayList<Integer>> playerLists;

	private int nbrCorrectAnswer;

	private final ArrayList<Integer> listOfNumbers;

	/** No-args constructor for reflection */
	@SuppressWarnings("unused")
	private NumberGame() {
		playerLists = null;
		listOfNumbers = null;
	};

	public NumberGame(long extraTime, Difficulty difficulty,
			Map<String, Integer> players) {
		super(difficulty, GameId.NUMBER_GAME, players);

		nbrCorrectAnswer = 0;
		playerListSize = 8;
		playerCount = players.size();

		// Create a shuffled list of integers in the range [1,99]
		listOfNumbers = new ArrayList<Integer>();
		for (int i = 1; i <= 99; i++) {
			listOfNumbers.add(i);
		}
		Collections.shuffle(listOfNumbers);

		// Create integer sequence based on difficulty and set any extra time
		initDifficulty(difficulty, extraTime);

		// Populate the player lists with their own correct numbers and then
		// fill it up with dummy numbers.
		playerLists = divideAndConquer(answerList);
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
	 * 
	 * @param nbr
	 * @return <code>true</code> if the number corresponds to the next one in
	 *         the sequence.
	 */
	public boolean checkNbr(int nbr) {
		return answerList.get(nbrCorrectAnswer) == nbr;
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

	private void initDifficulty(Difficulty difficulty, long extraTime) {
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
				answerList = createAnswer(6);
				break;
			case THREE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(9);
				break;
			case FOUR:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(12);
				break;
			case FIVE:
				this.setGameTime(25000, extraTime);
				answerList = createAnswer(15);
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
				answerList = createAnswer(8);
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
