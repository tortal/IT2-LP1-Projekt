package it.chalmers.tendu.gamemodel.test;

import static org.junit.Assert.assertTrue;
import it.chalmers.tendu.defaults.Constants.Difficulty;
import it.chalmers.tendu.gamemodel.numbergame.NumberGame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class NumberGameTest {

	NumberGame nbrGameEasy;
	NumberGame nbrGameHard;

	// TODO fix setup
	@Before
	public void setUp() {
		Map<String, Integer> players = new HashMap<String, Integer>();
		players.put("player1", 0);
		players.put("player2", 1);

		nbrGameEasy = new NumberGame(0, Difficulty.ONE, players);
		nbrGameHard = new NumberGame(0, Difficulty.TWO, players);
	}

	@Test
	public void testNumberGameIntDifficulty() {
		// Hard game should have 8 answers, easy should have 4
		assertTrue(nbrGameEasy.getAnswerList().size() == 2);
		assertTrue(nbrGameHard.getAnswerList().size() == 4);

		List<Integer> answerList = nbrGameEasy.getAnswerList();
		for (Integer i : answerList) {
			assertTrue(i <= 99 && i >= 0);
		}
	}

	@Test
	public void testCheckNbrInt() {
		assertTrue(nbrGameEasy != null);
		assertTrue(nbrGameEasy.checkNbr(nbrGameEasy.getAnswerList().get(0)));
		assertTrue(!nbrGameEasy.checkNbr(-1));
	}

}
