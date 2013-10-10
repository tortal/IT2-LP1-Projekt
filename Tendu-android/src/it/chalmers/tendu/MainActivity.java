package it.chalmers.tendu;

import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.network.bluetooth.BluetoothHandler;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;

		INetworkHandler netHandler = new BluetoothHandler(this);
		initialize(new Tendu(netHandler), cfg);
	}
}
