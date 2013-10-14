package it.chalmers.tendu;

import it.chalmers.tendu.network.INetworkHandler;
import it.chalmers.tendu.network.wifip2p.WifiHandler;
import it.chalmers.tendu.network.wifip2p.WifiHandlerBtB;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {

	private INetworkHandler networkHandler;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        networkHandler = new WifiHandlerBtB(this);
        Tendu tendu = new Tendu(networkHandler);
        initialize(tendu, cfg);
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		networkHandler.destroy();
	}
}
