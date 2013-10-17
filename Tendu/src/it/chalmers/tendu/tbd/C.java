package it.chalmers.tendu.tbd;

public class C {

	public enum Tag {
		TEST, COMMAND_AS_HOST, HOST_COMMANDED, REQUEST_AS_CLIENT, CLIENT_REQUESTED, DEFAULT, TO_SELF, NETWORK_NOTIFICATION;
	}

	public enum Msg {

		TEST, ALL_PLAYERS_CONNECTED, LOBBY_READY, LOAD_GAME, NUMBER_GUESS, UPDATE_MODEL, PLAYER_CONNECTED, PLAYER_DISCONNECTED, CONNECTION_LOST, REMOVE_TIME, PLAYER_READY, UPDATE_LOBBY_MODEL, START_MINI_GAME, GAME_SESSION_MODEL, SHAPE_SENT, LOCK_ATTEMPT, LOCK_FINISHED, CREATE_SCREEN, WAITING_TO_START_GAME, SOUND_WIN, SOUND_LOST, SOUND_SUCCEED, SOUND_FAIL, INTERIM_FINISHED, GAME_RESULT, SHOW_INTERIM_SCREEN, SHOW_GAME_OVER_SCREEN, PLAYER_REPLAY_READY, RETURN_MAIN_MENU, RESTART;



	}
}
