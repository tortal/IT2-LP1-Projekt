package it.chalmers.tendu.network;

import it.chalmers.tendu.gamemodel.MiniGame;

import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import com.badlogic.gdx.backends.android.AndroidApplication;

public class BluetoothClient implements Client {
	
	public static final int REQUEST_ENABLE_BT = 666;
	
	private Context context;
	private BluetoothAdapter mBluetoothAdapter;
	
	public BluetoothClient(Context context){
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.context=context;
	}

	@Override
	public void sendObject(Object o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joinGame(MiniGame miniGame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Server> searchTeam() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void enableBluetooth() {
		Intent enableBtIntent; 
		if (!mBluetoothAdapter.isEnabled()) {
			enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			((AndroidApplication) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); // context is wrong
		} 
	}

}
