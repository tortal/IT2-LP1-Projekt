package it.chalmers.tendu.gamemodel;

import static org.junit.Assert.*;

import java.util.List;

import it.chalmers.tendu.defaults.Constants.Difficulty;

import org.junit.Test;


public class NumberGameTest {

	NumberGame nbrGameEasy;
	NumberGame nbrGameHard;


	@Test
	public void testNumberGameIntDifficulty() {
		nbrGameEasy = new NumberGame(0, Difficulty.ONE);
		nbrGameHard = new NumberGame(0, Difficulty.TWO);
		//Hard game should have 8 answers, easy should have 4
		assertTrue(nbrGameEasy.getAnswerList().size() == 4); 
		assertTrue(nbrGameHard.getAnswerList().size() == 8); 

		List <Integer> answerList = nbrGameEasy.getAnswerList();
		for(Integer i: answerList){
			assertTrue(i<= 99 && i>=0);	
		}
	}

	@Test
	public void testCheckNbrInt(){
		assertTrue(nbrGameEasy.checkNbr(nbrGameEasy.getAnswerList().get(0)));
		assertTrue(nbrGameEasy.checkNbr(-1));
		assertTrue(nbrGameEasy.checkNbr(nbrGameEasy.getAnswerList().get(1)));
	}

}
