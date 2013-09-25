package it.chalmers.tendu;

import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.network.bluetooth.BluetoothHandler;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class BluetoothActivity extends Activity {

	INetworkHandler bluetoothHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		
		 bluetoothHandler = new BluetoothHandler(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth, menu);
		return true;
	}

	public void onHostButtonClicked(View v) {
		bluetoothHandler.hostSession();
	}
	
	public void onJoinButtonClicked(View v) {
		bluetoothHandler.joinGame();
	}

}
