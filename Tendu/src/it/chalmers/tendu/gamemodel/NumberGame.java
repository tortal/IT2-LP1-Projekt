package it.chalmers.tendu.gamemodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.defaults.GameIds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

public class NumberGame extends MiniGame {

	private static int playerCount = 4;
	private ArrayList<Integer> answerList;
	private Map<Integer, ArrayList<Integer>> playerLists;
	private int nbrCorrectAnswer;

	public NumberGame(int addTime, Difficulty difficulty) {
		super(addTime, difficulty, GameIds.NUMBER_GAME);
		nbrCorrectAnswer = 0;
		switch (difficulty) {
		case ONE:
			this.addTime(30);
			answerList = createAnswer(4);
			break;
		case TWO:
			this.addTime(30);
			answerList = createAnswer(8);
			break;
		default:
			// TODO:
			Gdx.app.debug("NumberGame Class", "Fix this switch case");
		}
		playerLists = divideAndConquer(answerList);
	}

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
				return false;
			}
		}
		return false;
	}

	private ArrayList<Integer> createAnswer(int length) {
		ArrayList<Integer> answerList = new ArrayList<Integer>();
		int i = 0;
		while (i < length) {
			int randomNbr = 1 + (int) (Math.random() * 99);
			if (!(answerList.contains(randomNbr))) {
				answerList.add(randomNbr);
				i++;
			}
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

		if (list.size() == 4) {
			for (int i = 0; i < playerCount; i++) {
				ArrayList<Integer> newList = (ArrayList<Integer>) list.subList(
						i, i);
				popAndShuffleList(newList);
				newMap.put(i, newList);
			}
		}
		if (list.size() == 8) {
			for (int i = 0; i < playerCount; i++) {
				ArrayList<Integer> newList = (ArrayList<Integer>) list.subList(
						i, list.size() - i);
				popAndShuffleList(newList);
				newMap.put(i, newList);
			}
		}
		return newMap;

	}

	private void popAndShuffleList(ArrayList<Integer> list) {
		int i = 0;
		while (i < (8 - list.size())) {
			int randomNbr = 1 + (int) (Math.random() * 99);
			if (!(list.contains(randomNbr))) {
				list.add(randomNbr);
				i++;
			}
		}
		Collections.shuffle(list);
	}

	public ArrayList<Integer> getAnswerList() {
		return answerList;
	}

	// public ArrayList<Integer> getDummyList() {
	// return answerAndDummyList;
	// }

}
