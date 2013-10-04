package it.chalmers.tendu.tbd;

public class C {

	public enum Tag  {
		COMMAND, REQUEST, DEFAULT, ACCESS_MODEL;
	}
	
	public enum Msg  {
		PLAYERS_CONNECTED, LOBBY_READY, LOAD_THIS_GAME, NUMBER_GUESS;
	}
}
