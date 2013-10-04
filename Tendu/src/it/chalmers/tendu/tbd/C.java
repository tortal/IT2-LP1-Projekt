package it.chalmers.tendu.tbd;

public class C {

	public enum Tag  {
		COMMAND_AS_HOST, HOST_COMMANDED, REQUEST_AS_CLIENT, CLIENT_REQUESTED, DEFAULT, ACCESS_MODEL;
	}
	
	public enum Msg  {
		PLAYERS_CONNECTED, LOBBY_READY, LOAD_THIS_GAME, NUMBER_GUESS, UPDATE_MODEL;
	}
}

