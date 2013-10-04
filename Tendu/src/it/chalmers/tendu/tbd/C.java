package it.chalmers.tendu.tbd;

public class C {

	public enum Tag  {
		COMMAND, REQUEST, DEFAULT, TO_SELF;
	}
	
	public enum Msg  {
		PLAYERS_CONNECTED, LOBBY_READY, LOAD_THIS_GAME, NUMBER_GUESS;
	}
}
