package it.chalmers.tendu.gamemodel;

import java.util.ArrayList;
import java.util.List;

public enum GameId {
	NUMBER_GAME(0,1,2,3), SHAPES_GAME(2,3,4,5,6);

	
	private List<Integer> acceptedLevels = new ArrayList<Integer>();
	
	
	private GameId(Integer... integers){
		for (Integer i : integers){
			acceptedLevels.add(i);
		}

	}
	
	
}
