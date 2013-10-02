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

package it.chalmers.tendu.network.clicklinkcompete;

import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.network.NetworkMessage;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

/**
 * API for the Bluetooth Click, Link, Compete library. This library simplifies
 * the process of establishing Bluetooth connections and sending data in a way
 * that is geared towards multi-player games.
 */

public class Connection {
	public static final String TAG = "net.clc.bt.Connection";

	public static final int SUCCESS = 0;

	public static final int FAILURE = 1;

	public static final int MAX_SUPPORTED = 7;

	public interface OnConnectionServiceReadyListener {
		public void OnConnectionServiceReady();
	}

	public interface OnIncomingConnectionListener {
		public void OnIncomingConnection(BluetoothDevice device);
	}

	public interface OnMaxConnectionsReachedListener {
		public void OnMaxConnectionsReached();
	}

	public interface OnMessageReceivedListener {
		public void OnMessageReceived(BluetoothDevice device, NetworkMessage message);
	}

	public interface OnConnectionLostListener {
		public void OnConnectionLost(BluetoothDevice device);
	}

	private OnConnectionServiceReadyListener mOnConnectionServiceReadyListener;

	private OnIncomingConnectionListener mOnIncomingConnectionListener;

	private OnMaxConnectionsReachedListener mOnMaxConnectionsReachedListener;

	private OnMessageReceivedListener mOnMessageReceivedListener;

	private OnConnectionLostListener mOnConnectionLostListener;

	private ServiceConnection mServiceConnection;

	private Context mContext;

	private String mPackageName = Constants.APP_NAME;

	private boolean mStarted = false;

	private ConnectionService connectionService;

	public Connection(Context ctx, OnConnectionServiceReadyListener ocsrListener) {
		mOnConnectionServiceReadyListener = ocsrListener;
		mContext = ctx;
		mPackageName = ctx.getPackageName();

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
		mOnIncomingConnectionListener = oicListener;
		mOnMaxConnectionsReachedListener = omcrListener;
		mOnMessageReceivedListener = omrListener;
		mOnConnectionLostListener = oclListener;
		try {
			int result = connectionService.startServer(mPackageName,
					maxConnections, oicListener, omcrListener, omrListener,
					oclListener);
			// mIconnection.registerCallback(mPackageName, mIccb);
			return result;
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in startServer", e);
		}
		return Connection.FAILURE;
	}

	public int connect(BluetoothDevice device,
			OnMessageReceivedListener omrListener,
			OnConnectionLostListener oclListener) {

		mOnMessageReceivedListener = omrListener;
		mOnConnectionLostListener = oclListener;
		try {
			int result = connectionService.connect(device, omrListener, oclListener);
			return result;
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in connect", e);
		}
		return Connection.FAILURE;
	}

	public int sendMessage(BluetoothDevice device, NetworkMessage message) {

		try {
			return connectionService.sendMessage(device, message);
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in sendMessage", e);
		}
		return Connection.FAILURE;
	}

	public int broadcastMessage(NetworkMessage message) {

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

	public void shutdown() {
		try {
			mStarted = false;
			if (connectionService != null) {
				connectionService.shutdown();
			}
			mContext.unbindService(mServiceConnection);
		} catch (RemoteException e) {
			Log.e(TAG, "RemoteException in shutdown", e);
		}
	}
}