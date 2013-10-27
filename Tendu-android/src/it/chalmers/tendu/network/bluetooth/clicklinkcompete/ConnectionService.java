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

import it.chalmers.tendu.defaults.Constants;
import it.chalmers.tendu.event.EventMessage;
import it.chalmers.tendu.network.bluetooth.clicklinkcompete.Connection.OnConnectionLostListener;
import it.chalmers.tendu.network.bluetooth.clicklinkcompete.Connection.OnIncomingConnectionListener;
import it.chalmers.tendu.network.bluetooth.clicklinkcompete.Connection.OnMaxConnectionsReachedListener;
import it.chalmers.tendu.network.bluetooth.clicklinkcompete.Connection.OnMessageReceivedListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
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
 * 
 * Stolen and modified
 */
public class ConnectionService {
	public static final String TAG = "ConnectionService";

	/** Thread waiting for new connections */
	private ConnectionWaiter connectionWaiter;

	/** The connected bluetooth devices */
	private ArrayList<BluetoothDevice> mBtDevices;

	/** The connected bluetooth sockets */
	private HashMap<String, BluetoothSocket> mBtSockets;

	/** The threads waiting for messages from connected devices */
	private HashMap<String, Thread> mBtStreamWatcherThreads;

	/** Connection to android bluetooth hardware */
	private BluetoothAdapter mBtAdapter;

	private OnIncomingConnectionListener mOnIncomingConnectionListener;

	private OnMaxConnectionsReachedListener mOnMaxConnectionsReachedListener;

	private OnMessageReceivedListener mOnMessageReceivedListener;

	private OnConnectionLostListener mOnConnectionLostListener;

	/** Android context */
	private Context context;

	/** Unique identifier for app */
	private UUID APP_UUID = UUID
			.fromString("a60f35f0-b93a-11de-8a39-08002009c666");

	/**
	 * Creates a new <code>ConnectionService</code> object
	 * @param context The android context
	 */
	public ConnectionService(Context context) {
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mBtSockets = new HashMap<String, BluetoothSocket>();
		mBtDevices = new ArrayList<BluetoothDevice>();
		mBtStreamWatcherThreads = new HashMap<String, Thread>();

		this.context = context;
	}

	/** Makes shure both sender and receiver have the same version kryo object */
	private Kryo kryoFactory() {
		Kryo kryo = new Kryo();

		// Register the classes we want to send over the network
		kryo.register(EventMessage.class);
		return kryo;
	}

	/**
	 * Runnable waiting for input from a bluetooth device.
	 * It establishes an input stream and wraps it in a Kryo deserializer object
	 */
	private class BtStreamWatcher implements Runnable {
		private final String address;
		private final BluetoothDevice device;
		private final Input in;
		
		/** Kryo is a serializer/deserializer */
		private final Kryo kryo = kryoFactory(); 

		private final InputStream mmInStream;

		public BtStreamWatcher(BluetoothDevice device) {
			InputStream tmpIn = null;

			this.device = device;
			address = device.getAddress();
			BluetoothSocket btSocket = mBtSockets.get(address);

			// Get the BluetoothSocket inputstream
			try {
				tmpIn = btSocket.getInputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}
			mmInStream = tmpIn;
			Log.d(TAG, "Establishing Input() for this address: "
					+ mBtSockets.get(address).getRemoteDevice().getAddress());
			in = new Input(mmInStream);
		}

		public void run() {
			Log.d(TAG, "Started thread, waiting for input");
			Object receivedObject;

			while (true) {
				try {
					receivedObject = kryo.readObject(in, EventMessage.class);
					mOnMessageReceivedListener.OnMessageReceived(device,
							(EventMessage) receivedObject);

				} catch (KryoException k) {
					Log.e(TAG, "The connection has most probably been lost");
					 k.printStackTrace();
					break;
				}
			}
			// If we end up outside the loop we have lost connection
			
			mBtDevices.remove(address);
			mBtSockets.remove(address);
			mBtStreamWatcherThreads.remove(address);
			mOnConnectionLostListener.OnConnectionLost(device);
			if (in != null) {
				in.close();
			}
			if (mmInStream != null) {
				try {
					mmInStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Runnable waiting for incoming connections. 
	 * Establishes the connection and starts a new thread to listen to messages from it.  
	 * Sets of the onIncomingConnectionListener on establishment of connection.
	 */
	private class ConnectionWaiter implements Runnable {
		private String srcApp = Constants.APP_NAME;

		private int maxConnections;

		public ConnectionWaiter(int connections) {
			maxConnections = connections;
		}

		private BluetoothServerSocket myServerSocket;
		public void stopAcceptingConnections() {
			if (myServerSocket != null) {
				try {
					myServerSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void run() {
			try {
				for (int i = 0; i < Connection.MAX_SUPPORTED
						&& maxConnections > 0; i++) {
					myServerSocket = mBtAdapter
							.listenUsingRfcommWithServiceRecord(srcApp,
									APP_UUID);
					BluetoothSocket myBSock = myServerSocket.accept();
					myServerSocket.close(); // Close the socket now that the
					// connection has been made.

					String address = myBSock.getRemoteDevice().getAddress();
					BluetoothDevice device = myBSock.getRemoteDevice();

					mBtSockets.put(address, myBSock);
					mBtDevices.add(device);
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
				Log.i(TAG, "IOException in ConnectionWaiter, No more waiting for connections");
			}
		}
	}

	/**
	 * Returns a connected bluetoothsocket
	 * @param myBtServer The server device to connect to 
	 * @param uuidToTry The unique app id to look for
	 * @return The connected socket
	 */
	private BluetoothSocket getConnectedSocket(BluetoothDevice myBtServer,
			UUID uuidToTry) {
		BluetoothSocket myBSock;
		try {
			myBSock = myBtServer.createRfcommSocketToServiceRecord(uuidToTry);
			Log.d(TAG, "atempting connection to: " + myBSock + " Socket");
			myBSock.connect();
			return myBSock;
		} catch (IOException e) {
			Log.i(TAG, "IOException in getConnectedSocket", e);
		}
		return null;
	}

	/**
	 * Starts a server
	 * @param maxConnections maximum allowed connections
	 * @param oicListener Listener for incoming connections
	 * @param omcrListener Listener for when max connections are reached
	 * @param omrListener Listener for received messages
	 * @param oclListener Listener for when a connection is lost
	 * @return Whether the creation was successful or not
	 * @throws RemoteException
	 */
	public int startServer(int maxConnections,
			OnIncomingConnectionListener oicListener,
			OnMaxConnectionsReachedListener omcrListener,
			OnMessageReceivedListener omrListener,
			OnConnectionLostListener oclListener) throws RemoteException {

		mOnIncomingConnectionListener = oicListener;
		mOnMaxConnectionsReachedListener = omcrListener;
		mOnMessageReceivedListener = omrListener;
		mOnConnectionLostListener = oclListener;


		connectionWaiter = (new ConnectionWaiter(maxConnections));
		(new Thread(connectionWaiter)).start();

		// Be discoverable
		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		// discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		((AndroidApplication) context).startActivity(discoverableIntent);
		return Connection.SUCCESS;
	}

	/**
	 * Attempts to establish a connection to a bluetooth device
	 * @param device The bluetooth device
	 * @param omrListener Listener for received messages
	 * @param oclListener Listener for when the connection is lost
	 * @return Whether the connection was successful or not
	 * @throws RemoteException
	 */
	public int connect(BluetoothDevice device,
			OnMessageReceivedListener omrListener,
			OnConnectionLostListener oclListener) throws RemoteException {

		mOnMessageReceivedListener = omrListener;
		mOnConnectionLostListener = oclListener;

		BluetoothDevice myBtServer = mBtAdapter.getRemoteDevice(device
				.getAddress());
		BluetoothSocket myBSock = null;

		for (int i = 0; i < Connection.MAX_SUPPORTED && myBSock == null; i++) {
			for (int j = 0; j < 3 && myBSock == null; j++) {
				myBSock = getConnectedSocket(myBtServer, APP_UUID);
				if (myBSock == null) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						Log.e(TAG, "InterruptedException in connect", e);
					}
				}
			}
		}
		if (myBSock == null) {
			return Connection.FAILURE;
		}

		mBtSockets.put(device.getAddress(), myBSock);
		mBtDevices.add(device);
		Thread mBtStreamWatcherThread = new Thread(new BtStreamWatcher(device));
		mBtStreamWatcherThread.start();
		mBtStreamWatcherThreads
		.put(device.getAddress(), mBtStreamWatcherThread);
		return Connection.SUCCESS;
	}

	
	/**
	 * Send a message to all connected bluetooth devices
	 * @param message The message to be sent
	 * @return Whether the broadcast was successful or not
	 */
	public int broadcastMessage(EventMessage message) throws RemoteException {
		for (int i = 0; i < mBtDevices.size(); i++) {
			Log.d(TAG, "sendMessage(): " + mBtDevices.get(i).getAddress()
					+ " Message: " + message);
			sendMessage(mBtDevices.get(i), message);
		}
		return Connection.SUCCESS;
	}

	/**
	 * Sends a message to a specific bluetooth device
	 * 
	 * @param destination
	 *            The destination device
	 * @param message
	 *            The message to send
	 * @return Connection.FAILURE or Connection.SUCCESS
	 * @throws RemoteException
	 */
	public int sendMessage(BluetoothDevice destination, EventMessage message)
			throws RemoteException {
		Log.d(TAG,
				"sendMessage: " + message.toString() + " to "
						+ destination.getAddress());
		Kryo tempKryo = kryoFactory();
		Output out;
		
		String address = destination.getAddress();
		BluetoothSocket btSocket = mBtSockets.get(address);
		try {
			out = new Output(btSocket.getOutputStream());
		} catch (IOException e1) {
			Log.i(TAG,
					"IOException in sendMessage - Dest:"
							+ destination.getName() + ", Msg:" + message, e1);
			return Connection.FAILURE;
		}

		//if (btSocket.isConnected()) {
			tempKryo.writeObject(out, message);
			out.flush();
			
		//}

		return Connection.SUCCESS;
	}

	/**
	 * Resets the <code>ConnectionService</code>
	 * @throws RemoteException
	 */
	public void reset() throws RemoteException {
		try {
			for (int i = 0; i < mBtDevices.size(); i++) {
				BluetoothSocket myBsock = mBtSockets.get(mBtDevices.get(i));
				if (myBsock != null) {
					myBsock.close();
				}
			}
			mBtSockets = new HashMap<String, BluetoothSocket>();
			mBtStreamWatcherThreads = new HashMap<String, Thread>();
			mBtDevices = new ArrayList<BluetoothDevice>();
		} catch (IOException e) {
			Log.i(TAG, "IOException in reset", e);
		}
	}

	/**
	 * Returns the local mac-address
	 * @return The mac-address
	 * @throws RemoteException
	 */
	public String getAddress() throws RemoteException {
		return mBtAdapter.getAddress();
	}
	
	/**
	 * Returns the local device friendly name
	 * @return The name
	 */
	public String getName() throws RemoteException {
		return mBtAdapter.getName();
	}

	/**
	 * Cease to listen for incoming connections 
	 * No more matchmaking.
	 */
	public void stopAcceptingConnections() {
		if (connectionWaiter != null) { 
			connectionWaiter.stopAcceptingConnections();
		}

	}
}
