package it.chalmers.tendu.tbd;

public class C {

	public enum Tag  {
		COMMAND, REQUEST, DEFAULT, SERVER;
	}
	
	public enum Msg  {
		PLAYERS_CONNECTED, LOBBY_READY, LOAD_THIS_GAME, CORRECT_NUMBER_GUESS;
	}
}
