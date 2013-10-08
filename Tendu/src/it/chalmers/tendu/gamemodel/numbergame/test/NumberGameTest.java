package it.chalmers.tendu.gamemodel.numbergame.test;

import static org.junit.Assert.assertTrue;
import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;

import java.util.List;

import org.junit.Test;

public class NumberGameTest {

	NumberGame nbrGameEasy;
	NumberGame nbrGameHard;

	// TODO fix setup
	@Test
	public void testNumberGameIntDifficulty() {
		// crash
		nbrGameEasy = new NumberGame(0, Difficulty.ONE, null);
		nbrGameHard = new NumberGame(0, Difficulty.TWO, null);
		// Hard game should have 8 answers, easy should have 4
		assertTrue(nbrGameEasy.getAnswerList().size() == 4);
		assertTrue(nbrGameHard.getAnswerList().size() == 8);

		List<Integer> answerList = nbrGameEasy.getAnswerList();
		for (Integer i : answerList) {
			assertTrue(i <= 99 && i >= 0);
		}
	}

	@Test
	public void testCheckNbrInt() {
		// crash
		nbrGameEasy = new NumberGame(0, Difficulty.ONE, null);
		assertTrue(nbrGameEasy.checkNbr(nbrGameEasy.getAnswerList().get(0)));
		// assertTrue(nbrGameEasy.checkNbr(-1));
		assertTrue(nbrGameEasy.checkNbr(nbrGameEasy.getAnswerList().get(1)));
	}

}
