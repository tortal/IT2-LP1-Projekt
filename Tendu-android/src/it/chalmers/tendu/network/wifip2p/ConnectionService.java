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

package it.chalmers.tendu.network.wifip2p;

import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.network.wifip2p.Connection.OnConnectionLostListener;
import it.chalmers.tendu.network.wifip2p.Connection.OnIncomingConnectionListener;
import it.chalmers.tendu.network.wifip2p.Connection.OnMaxConnectionsReachedListener;
import it.chalmers.tendu.network.wifip2p.Connection.OnMessageReceivedListener;
import it.chalmers.tendu.tbd.EventMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.RemoteException;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Service for simplifying the process of establishing Bluetooth connections and
 * sending data in a way that is geared towards multi-player games.
 */

public class ConnectionService {
	public static final String TAG = "ConnectionService";

	//private ArrayList<UUID> mUuid;

	private ArrayList<WifiP2pDevice> mBtDevices;

	private HashMap<String, Socket> mBtSockets;

	private HashMap<String, Thread> mBtStreamWatcherThreads;

	private WifiP2pManager mBtAdapter;

	private OnIncomingConnectionListener mOnIncomingConnectionListener;

	private OnMaxConnectionsReachedListener mOnMaxConnectionsReachedListener;

	private OnMessageReceivedListener mOnMessageReceivedListener;

	private OnConnectionLostListener mOnConnectionLostListener;

	private Context context;

	private UUID APP_UUID = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");

	/** Kryo Variables */
	private Kryo mKryo;

	private Output out;

	public ConnectionService(Context context) {
		//mSelf = this;
		//mBtAdapter = WifiP2pManager.getDefaultAdapter();
		mBtSockets = new HashMap<String, Socket>();
		mBtDevices = new ArrayList<WifiP2pDevice>();
		mBtStreamWatcherThreads = new HashMap<String, Thread>();

		this.context = context;
		mKryo = kryoFactory();
	}

	private Kryo kryoFactory() {
		Kryo kryo = new Kryo();

		// Register the classes we want to send over the network
		kryo.register(EventMessage.class);
		return kryo;
	}

	private class BtStreamWatcher implements Runnable {
		//private final String address;
		//private final WifiP2pDevice device;
		//private final Input in;

		//private final InputStream mmInStream;

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

		//		public BtStreamWatcher(WifiP2pDevice device) {
		//			InputStream tmpIn = null;
		//
		//			this.device = device;
		//			address = device.deviceAddress;
		//			Socket btSocket = mBtSockets.get(address);
		//
		//			// Get the Socket inputstream
		//			try {
		//				tmpIn = btSocket.getInputStream();
		//			} catch (IOException e) {
		//				Log.e(TAG, "temp sockets not created", e);
		//			}
		//			mmInStream = tmpIn;
		//			Log.d(TAG, "Establishing Input() for this address: "
		//					+ mBtSockets.get(address).getRemoteDevice().deviceAddress);
		//			in = new Input(mmInStream);
		//		}
		//
		//		public void run() {
		//			Log.d(TAG, "Started thread, waiting for input");
		//			Object receivedObject;
		//
		//			while (true) {
		//				try {
		//					receivedObject = mKryo.readObject(in, EventMessage.class);
		//					mOnMessageReceivedListener.OnMessageReceived(device, (EventMessage) receivedObject);
		//
		//				} catch (KryoException k) {
		//					Log.e(TAG, "The connection has most probably been lost");
		//					//k.printStackTrace();
		//					break;
		//				}
		//			}
		//			// If we end up outside the loop we have lost connection
		//			 mBtDevices.remove(address);
		//			 mBtSockets.remove(address);
		//			 mBtStreamWatcherThreads.remove(address);
		//			 mOnConnectionLostListener.OnConnectionLost(device);
		//		}
	}

	private class ConnectionWaiter implements Runnable {
		private String srcApp = Constants.APP_NAME;

		private int maxConnections;

		public ConnectionWaiter(int connections) {
			maxConnections = connections;
		}

		@Override
		public void run() {
			try {

				for (int i = 0; i < Connection.MAX_SUPPORTED
						&& maxConnections > 0; i++) {
					ServerSocket serverSocket = new ServerSocket(8888);
//					ServcerSocket myServerSocket = mBtAdapter
							.listenUsingRfcommWithServiceRecord(srcApp, APP_UUID);
					Socket client = serverSocket.accept();
//					Socket myBSock = myServerSocket.accept();
					serverSocket.close(); // Close the socket now that the
					// connection has been made.

					//String address = client.getRemoteDevice().deviceAddress;
					//WifiP2pDevice device = client.getRemoteDevice();

//					mBtSockets.put(address, myBSock);
//					mBtDevices.add(device);
					Thread mBtStreamWatcherThread = new Thread(
							new BtStreamWatcher(device));
					mBtStreamWatcherThread.start();
					mBtStreamWatcherThreads
					.put(address, mBtStreamWatcherThread);
					maxConnections = maxConnections - 1;
					if (mOnIncomingConnectionListener != null) {
						mOnIncomingConnectionListener
						.OnIncomingConnection(device);
					}
				}
				if (mOnMaxConnectionsReachedListener != null) {
					mOnMaxConnectionsReachedListener.OnMaxConnectionsReached();
				}
			} catch (IOException e) {
				Log.i(TAG, "IOException in ConnectionService:ConnectionWaiter",
						e);
			}

		}

		//		public void run() {
		//			try {
		//				for (int i = 0; i < Connection.MAX_SUPPORTED
		//						&& maxConnections > 0; i++) {
		//					ServcerSocket myServerSocket = mBtAdapter
		//							.listenUsingRfcommWithServiceRecord(srcApp, APP_UUID);
		//					Socket myBSock = myServerSocket.accept();
		//					myServerSocket.close(); // Close the socket now that the
		//					// connection has been made.
		//
		//					String address = myBSock.getRemoteDevice().deviceAddress;
		//					WifiP2pDevice device = myBSock.getRemoteDevice();
		//
		//					mBtSockets.put(address, myBSock);
		//					mBtDevices.add(device);
		//					Thread mBtStreamWatcherThread = new Thread(
		//							new BtStreamWatcher(device));
		//					mBtStreamWatcherThread.start();
		//					mBtStreamWatcherThreads
		//							.put(address, mBtStreamWatcherThread);
		//					maxConnections = maxConnections - 1;
		//					if (mOnIncomingConnectionListener != null) {
		//						mOnIncomingConnectionListener
		//								.OnIncomingConnection(device);
		//					}
		//				}
		//				if (mOnMaxConnectionsReachedListener != null) {
		//					mOnMaxConnectionsReachedListener.OnMaxConnectionsReached();
		//				}
		//			} catch (IOException e) {
		//				Log.i(TAG, "IOException in ConnectionService:ConnectionWaiter",
		//						e);
		//			}
		//		}
	}

	//	private Socket getConnectedSocket(WifiP2pDevice myBtServer,
	//			UUID uuidToTry) {
	//		Socket myBSock;
	//		try {
	//			myBSock = myBtServer.createRfcommSocketToServiceRecord(uuidToTry);
	//			Log.d(TAG, "atempting connection to: " + myBSock + " Socket");
	//			myBSock.connect();
	//			return myBSock;
	//		} catch (IOException e) {
	//			Log.i(TAG, "IOException in getConnectedSocket", e);
	//		}
	//		return null;
	//	}

	public int startServer(int maxConnections,
			OnIncomingConnectionListener oicListener,
			OnMaxConnectionsReachedListener omcrListener,
			OnMessageReceivedListener omrListener,
			OnConnectionLostListener oclListener) throws RemoteException {

		mOnIncomingConnectionListener = oicListener;
		mOnMaxConnectionsReachedListener = omcrListener;
		mOnMessageReceivedListener = omrListener;
		mOnConnectionLostListener = oclListener;

		(new Thread(new ConnectionWaiter(maxConnections))).start();
		return 0;

		//		// Be discoverable
		//		Intent discoverableIntent = new Intent(
		//				WifiP2pManager.ACTION_REQUEST_DISCOVERABLE);
		//		discoverableIntent.putExtra(
		//				WifiP2pManager.EXTRA_DISCOVERABLE_DURATION, 300);
		//		// discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//		((AndroidApplication) context).startActivity(discoverableIntent);
		//		return Connection.SUCCESS;
	}

	public int connect(WifiP2pDevice device,
			OnMessageReceivedListener omrListener,
			OnConnectionLostListener oclListener) throws RemoteException {

		mOnMessageReceivedListener = omrListener;
		mOnConnectionLostListener = oclListener;

		//		WifiP2pDevice myBtServer = mBtAdapter.getRemoteDevice(device
		//				.deviceAddress);
		//		Socket myBSock = null;
		//
		//		for (int i = 0; i < Connection.MAX_SUPPORTED && myBSock == null; i++) {
		//			for (int j = 0; j < 3 && myBSock == null; j++) {
		//				myBSock = getConnectedSocket(myBtServer, APP_UUID);
		//				if (myBSock == null) {
		//					try {
		//						Thread.sleep(200);
		//					} catch (InterruptedException e) {
		//						Log.e(TAG, "InterruptedException in connect", e);
		//					}
		//				}
		//			}
		//		}
		//		if (myBSock == null) {
		//			return Connection.FAILURE;
		//		}
		//
		//		mBtSockets.put(device.deviceAddress, myBSock);
		//		mBtDevices.add(device);
		//		Thread mBtStreamWatcherThread = new Thread(new BtStreamWatcher(device));
		//		mBtStreamWatcherThread.start();
		//		mBtStreamWatcherThreads
		//				.put(device.deviceAddress, mBtStreamWatcherThread);
		//		return Connection.SUCCESS;
		return 0;
	}

	public int broadcastMessage(EventMessage message) throws RemoteException {
		for (int i = 0; i < mBtDevices.size(); i++) {
			Log.d(TAG, "sendMessage(): " + mBtDevices.get(i).deviceAddress
					+ " Message: " + message);
			sendMessage(mBtDevices.get(i), message);
		}
		return Connection.SUCCESS;
	}

	public String getConnections() throws RemoteException {
		String connections = "";
		for (int i = 0; i < mBtDevices.size(); i++) {
			connections = connections + mBtDevices.get(i) + ",";
		}
		return connections;
	}

	/**
	 * Sends a message to a specific WifiP2pDevice
	 * 
	 * @param destination
	 *            The destination device
	 * @param message
	 *            The message to send
	 * @return Connection.FAILURE or Connection.SUCCESS
	 * @throws RemoteException
	 */
	public int sendMessage(WifiP2pDevice destination, EventMessage message)
			throws RemoteException {
		Log.d(TAG,
				"sendMessage: " + message.toString() + " to "
						+ destination.deviceAddress);
		Kryo tempKryo = kryoFactory();

		String address = destination.deviceAddress;
		Socket btSocket = mBtSockets.get(address);
		try {
			out = new Output(btSocket.getOutputStream());
		} catch (IOException e1) {
			Log.i(TAG,
					"IOException in sendMessage - Dest:"
							+ destination.deviceName + ", Msg:" + message, e1);
			return Connection.FAILURE;
		}

		tempKryo.writeObject(out, message);
		out.flush();

		return Connection.SUCCESS;
	}

	public void shutdown() throws RemoteException {
		try {
			for (int i = 0; i < mBtDevices.size(); i++) {
				Socket myBsock = mBtSockets.get(mBtDevices.get(i));
				if (myBsock != null) {
					myBsock.close();
				}
			}
			mBtSockets = new HashMap<String, Socket>();
			mBtStreamWatcherThreads = new HashMap<String, Thread>();
			mBtDevices = new ArrayList<WifiP2pDevice>();
		} catch (IOException e) {
			Log.i(TAG, "IOException in shutdown", e);
		}
	}

	public String getAddress() throws RemoteException {
		return "Test";//mBtAdapter.deviceAddress;
	}

	public String getName() throws RemoteException {
		return "Test";//mBtAdapter.deviceName;
	}
}
