package it.chalmers.tendu.gamemodel;

import java.util.ArrayList;

import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.defaults.GameIds;

import com.badlogic.gdx.Gdx;

public class NumberGame extends MiniGame {

	private ArrayList<Integer> answerList;
	private ArrayList<Integer> answerAndDummyList;

	public NumberGame(int addTime, Difficulty difficulty) {
		super(addTime, difficulty, GameIds.NUMBER_GAME);

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

		// Fill numberCode with random numbers
//		for (int i = 0; i < 5; i++) {
//			numberCode.add(checkRandomNumber(numberCode));
//		}
		// // Fill randomNumbers with numberCode and random numbers
		// for (int i = 0; i < 5; i++) {
		// randomNumbers.add(checkRandomNumber(numberCode));
		// randomNumbers.add(numberCode.get(i));
		// }
	}

	public boolean checkNbr(int num) {
		
//		return numberCode.get(pos) == num;
		return true;
	}

	private ArrayList createAnswer(int length) {
		ArrayList<Integer> answerList = new ArrayList<>();
		int i=0;
		while (i<length){
		int randomNbr = 1 + (int)(Math.random()*100);
			if(!(answerList.contains(randomNbr))){
				answerList.add(randomNbr);
				i++;
			}
		}
		return answerList;
	}

	private ArrayList fillWithDummyNbr(ArrayList<Integer> list) {
		ArrayList newList = new ArrayList();
		// TODO populate list with original number and dummy numbers.
		return newList;
	}
}
