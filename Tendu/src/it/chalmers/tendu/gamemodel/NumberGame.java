package it.chalmers.tendu.gamemodel;

import java.util.ArrayList;

import it.chalmers.tendu.defaults.Defaults;
import it.chalmers.tendu.defaults.GameIds;

public class NumberGame extends MiniGame {
	
	private ArrayList<Integer> numberCode;
	private ArrayList<Integer> randomNumbers;
	
	
	public NumberGame(int addTime, int difficulty) {
		super(addTime, difficulty, GameIds.NUMBER_GAME);
		
		numberCode = new ArrayList<Integer>(); 
		randomNumbers = new ArrayList<Integer>();

		switch (difficulty) {
		case Defaults.DIFFICULTY_ONE:
			this.addTime(35);
			break;
		case Defaults.DIFFICULTY_TWO:
			this.addTime(20);
			break;
		}
		
		//Fill numberCode with random numbers
		for(int i = 0; i < 5; i++){
			numberCode.add(checkRandomNumber(numberCode));
		}	
		//Fill randomNumbers with random numbers
		for(int i = 0; i < 5; i++){
			randomNumbers.add(checkRandomNumber(numberCode));
		}
		
	}
	
	public boolean checkNumber(int num, int pos){
		return numberCode.get(pos) == num; 
	}
	
	private int checkRandomNumber(ArrayList list){
		int randomNum = 1 + (int)(Math.random()*100);
		if(list.contains(randomNum)){
			return checkRandomNumber(list);
		}else{
			return randomNum; 
		}
	}

}
