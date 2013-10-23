/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.chalmers.tendu.network.bluetooth.clicklinkcompete;

import it.chalmers.tendu.event.EventMessage;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

/**
 * API for the Bluetooth Click, Link, Compete library. This library simplifies
 * the process of establishing Bluetooth connections and sending data in a way
 * that is geared towards multi-player games.
 * 
 * Tailored to size by the Tendu crew
 */

public class Connection {
	public static final String TAG = "Connection";

	public static final int SUCCESS = 0;

	public static final int FAILURE = 1;

	public static final int MAX_SUPPORTED = 7;

	public interface OnIncomingConnectionListener {
		public void OnIncomingConnection(BluetoothDevice device);
	}

	public interface OnMaxConnectionsReachedListener {
		public void OnMaxConnectionsReached();
	}

	public interface OnMessageReceivedListener {
		public void OnMessageReceived(BluetoothDevice device,
				EventMessage receivedObject);
	}

	public interface OnConnectionLostListener {
		public void OnConnectionLost(BluetoothDevice device);
	}

	private ConnectionService connectionService;

	public Connection(Context ctx) {

		connectionService = new ConnectionService(ctx);
	}

	public int startServer(final int maxConnections,
			OnIncomingConnectionListener oicListener,
			OnMaxConnectionsReachedListener omcrListener,
			OnMessageReceivedListener omrListener,
			OnConnectionLostListener oclListener) {

		if (maxConnections > MAX_SUPPORTED) {
			Log.e(TAG, "The maximum number of allowed connections is "
					+ MAX_SUPPORTED);
			return Connection.FAILURE;
		}

		try {
			int result = connectionService.startServer(maxConnections,
					oicListener, omcrListener, omrListener, oclListener);
			return result;
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in startServer", e);
		}
		return Connection.FAILURE;
	}

	public int connect(BluetoothDevice device,
			OnMessageReceivedListener omrListener,
			OnConnectionLostListener oclListener) {
		try {
			int result = connectionService.connect(device, omrListener,
					oclListener);
			return result;
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in connect", e);
		}
		return Connection.FAILURE;
	}

	public int sendMessage(BluetoothDevice device, EventMessage message) {

		try {
			return connectionService.sendMessage(device, message);
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in sendMessage", e);
		}
		return Connection.FAILURE;
	}

	public int broadcastMessage(EventMessage message) {
		Log.d(TAG, "broadcastMessage: " + message.toString());
		try {
			return connectionService.broadcastMessage(message);
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in broadcastMessage", e);
		}
		return Connection.FAILURE;
	}

	public String getAddress() {

		try {
			return connectionService.getAddress();
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in getAddress", e);
		}
		return "";
	}

	public String getName() {

		try {
			return connectionService.getName();
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in getVersion", e);
		}
		return "";
	}

	public void reset() {
		try {
			connectionService.reset();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopAcceptingConnections() {
		connectionService.stopAcceptingConnections();
		
	}
}
