package it.chalmers.tendu;

import it.chalmers.tendu.network.INetwork;
import it.chalmers.tendu.network.Network;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {

	private INetwork network;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        network =  new Network(this);
        Tendu tendu = new Tendu(network);
        initialize(tendu, cfg);
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		network.destroy();
	}
}
