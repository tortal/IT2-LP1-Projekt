package it.chalmers.tendu;

import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.network.bluetooth.BluetoothHandler;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.badlogic.gdx.backends.android.AndroidApplication;

public class BluetoothActivity extends Activity {

	
	BluetoothHandler bluetoothHandler; // Should be INetworkHandler
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		
		 bluetoothHandler = new BluetoothHandler(this);
		
		
	}


	
	public void onHostButtonClicked(View v) {
		bluetoothHandler.hostSession();
	}
	
	public void onJoinButtonClicked(View v) {
		bluetoothHandler.joinGame();
	}

	public void onPingButtonClicked(View v) {
		bluetoothHandler.sendPing();
	} 
	
	public void onObjectButtonClicked(View v) {
		bluetoothHandler.sendObject(new TestObject());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		bluetoothHandler.destroy();
	}
}
