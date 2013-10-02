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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Service for simplifying the process of establishing Bluetooth connections and
 * sending data in a way that is geared towards multi-player games.
 */

public class ConnectionService {
    public static final String TAG = "net.clc.bt.ConnectionService";

    private ArrayList<UUID> mUuid;

    private ConnectionService mSelf;

    private String mApp; // Assume only one app can use this at a time; may

    // change this later

    //private IConnectionCallback mCallback;

    private ArrayList<BluetoothDevice> mBtDevices;

    private HashMap<String, BluetoothSocket> mBtSockets;

    private HashMap<String, Thread> mBtStreamWatcherThreads;

    private BluetoothAdapter mBtAdapter;

    public ConnectionService() {
        mSelf = this;
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        mApp = "";
        mBtSockets = new HashMap<String, BluetoothSocket>();
        mBtDevices = new ArrayList<BluetoothDevice>();
        mBtStreamWatcherThreads = new HashMap<String, Thread>();
        mUuid = new ArrayList<UUID>();
        // Allow up to 7 devices to connect to the server
        mUuid.add(UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
        mUuid.add(UUID.fromString("503c7430-bc23-11de-8a39-0800200c9a66"));
        mUuid.add(UUID.fromString("503c7431-bc23-11de-8a39-0800200c9a66"));
        mUuid.add(UUID.fromString("503c7432-bc23-11de-8a39-0800200c9a66"));
        mUuid.add(UUID.fromString("503c7433-bc23-11de-8a39-0800200c9a66"));
        mUuid.add(UUID.fromString("503c7434-bc23-11de-8a39-0800200c9a66"));
        mUuid.add(UUID.fromString("503c7435-bc23-11de-8a39-0800200c9a66"));
    }

//    @Override
//    public IBinder onBind(Intent arg0) {
//        return mBinder;
//    }

    private class BtStreamWatcher implements Runnable {
        private String address;
        private BluetoothDevice device;
        
        private Handler handler = new Handler(Looper.getMainLooper());

        public BtStreamWatcher(BluetoothDevice device) {
            this.device = device;
        	address = device.getAddress();
        }

        public void run() {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            BluetoothSocket bSock = mBtSockets.get(address);
            try {
                InputStream instream = bSock.getInputStream();
                int bytesRead = -1;
                String message = "";
                while (true) {
                    message = "";
                    bytesRead = instream.read(buffer);
                    if (bytesRead != -1) {
                        while ((bytesRead == bufferSize) && (buffer[bufferSize - 1] != 0)) {
                            message = message + new String(buffer, 0, bytesRead);
                            bytesRead = instream.read(buffer);
                        }
                        message = message + new String(buffer, 0, bytesRead - 1); // Remove
                        // the
                        // stop
                        // marker
                        
                       //handler.post()
                        
                        mCallback.messageReceived(device, message);
                        //mCallback.messageReceived(device, "Test");
                    }
                }
            } catch (IOException e) {
                Log.i(TAG,"IOException in BtStreamWatcher - probably caused by normal disconnection",e);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in BtStreamWatcher while reading data", e);
            }
            // Getting out of the while loop means the connection is dead.
            try {
                mBtDevices.remove(address);
                mBtSockets.remove(address);
                mBtStreamWatcherThreads.remove(address);
                mCallback.connectionLost(device);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in BtStreamWatcher while disconnecting", e);
            }
        }
    }

    private class ConnectionWaiter implements Runnable {
        private String srcApp;

        private int maxConnections;

        public ConnectionWaiter(String theApp, int connections) {
            srcApp = theApp;
            maxConnections = connections;
        }

        public void run() {
            try {
                for (int i = 0; i < Connection.MAX_SUPPORTED && maxConnections > 0; i++) {
                    BluetoothServerSocket myServerSocket = mBtAdapter
                            .listenUsingRfcommWithServiceRecord(srcApp, mUuid.get(i));
                    BluetoothSocket myBSock = myServerSocket.accept();
                    myServerSocket.close(); // Close the socket now that the
                    // connection has been made.

                    String address = myBSock.getRemoteDevice().getAddress();
                    BluetoothDevice device = myBSock.getRemoteDevice();
                    
                    mBtSockets.put(address, myBSock);
                    mBtDevices.add(device);
                    Thread mBtStreamWatcherThread = new Thread(new BtStreamWatcher(device));
                    mBtStreamWatcherThread.start();
                    mBtStreamWatcherThreads.put(address, mBtStreamWatcherThread);
                    maxConnections = maxConnections - 1;
                    if (mCallback != null) {
                        mCallback.incomingConnection(device);
                    }
                }
                if (mCallback != null) {
                    mCallback.maxConnectionsReached();
                }
            } catch (IOException e) {
                Log.i(TAG, "IOException in ConnectionService:ConnectionWaiter", e);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in ConnectionService:ConnectionWaiter", e);
            }
        }
    }

    private BluetoothSocket getConnectedSocket(BluetoothDevice myBtServer, UUID uuidToTry) {
        BluetoothSocket myBSock;
        try {
            myBSock = myBtServer.createRfcommSocketToServiceRecord(uuidToTry);
            myBSock.connect();
            return myBSock;
        } catch (IOException e) {
            Log.i(TAG, "IOException in getConnectedSocket", e);
        }
        return null;
    }

    private final IConnection.Stub mBinder = new IConnection.Stub() {
        public int startServer(String srcApp, int maxConnections) throws RemoteException {
            if (mApp.length() > 0) {
                return Connection.FAILURE;
            }
            mApp = srcApp;
            (new Thread(new ConnectionWaiter(srcApp, maxConnections))).start();
            
            //            Intent i = new Intent();
//            i.setClass(mSelf, StartDiscoverableModeActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
            
        	Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
    		discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(discoverableIntent);
            return Connection.SUCCESS;
        }

        public int connect(String srcApp, BluetoothDevice device) throws RemoteException {
            if (mApp.length() > 0) {
                return Connection.FAILURE;
            }
            mApp = srcApp;
            BluetoothDevice myBtServer = mBtAdapter.getRemoteDevice(device.getAddress());
            BluetoothSocket myBSock = null;

            for (int i = 0; i < Connection.MAX_SUPPORTED && myBSock == null; i++) {
                for (int j = 0; j < 3 && myBSock == null; j++) {
                    myBSock = getConnectedSocket(myBtServer, mUuid.get(i));
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
            mBtStreamWatcherThreads.put(device.getAddress(), mBtStreamWatcherThread);
            return Connection.SUCCESS;
        }

        public int broadcastMessage(String srcApp, String message) throws RemoteException {
            if (!mApp.equals(srcApp)) {
                return Connection.FAILURE;
            }
            for (int i = 0; i < mBtDevices.size(); i++) {
                sendMessage(srcApp, mBtDevices.get(i), message);
            }
            return Connection.SUCCESS;
        }

        public String getConnections(String srcApp) throws RemoteException {
            if (!mApp.equals(srcApp)) {
                return "";
            }
            String connections = "";
            for (int i = 0; i < mBtDevices.size(); i++) {
                connections = connections + mBtDevices.get(i) + ",";
            }
            return connections;
        }

        public int getVersion() throws RemoteException {
            try {
                PackageManager pm = mSelf.getPackageManager();
                PackageInfo pInfo = pm.getPackageInfo(mSelf.getPackageName(), 0);
                return pInfo.versionCode;
            } catch (NameNotFoundException e) {
                Log.e(TAG, "NameNotFoundException in getVersion", e);
            }
            return 0;
        }

        public int registerCallback(String srcApp, IConnectionCallback cb) throws RemoteException {
            if (!mApp.equals(srcApp)) {
                return Connection.FAILURE;
            }
            mCallback = cb;
            return Connection.SUCCESS;
        }

        public int sendMessage(String srcApp, BluetoothDevice destination, String message)
                throws RemoteException {
            if (!mApp.equals(srcApp)) {
                return Connection.FAILURE;
            }
            try {
                BluetoothSocket myBsock = mBtSockets.get(destination.getAddress());
                if (myBsock != null) {
                    OutputStream outStream = myBsock.getOutputStream();
                    byte[] stringAsBytes = (message + " ").getBytes();
                    stringAsBytes[stringAsBytes.length - 1] = 0; // Add a stop
                    // marker
                    outStream.write(stringAsBytes);
                    return Connection.SUCCESS;
                }
            } catch (IOException e) {
                Log.i(TAG, "IOException in sendMessage - Dest:" + destination.getName() + ", Msg:" + message,
                        e);
            }
            return Connection.FAILURE;
        }

        public void shutdown(String srcApp) throws RemoteException {
            try {
                for (int i = 0; i < mBtDevices.size(); i++) {
                    BluetoothSocket myBsock = mBtSockets.get(mBtDevices.get(i));
                    myBsock.close();
                }
                mBtSockets = new HashMap<String, BluetoothSocket>();
                mBtStreamWatcherThreads = new HashMap<String, Thread>();
                mBtDevices = new ArrayList<BluetoothDevice>();
                mApp = "";
            } catch (IOException e) {
                Log.i(TAG, "IOException in shutdown", e);
            }
        }

        public int unregisterCallback(String srcApp) throws RemoteException {
            if (!mApp.equals(srcApp)) {
                return Connection.FAILURE;
            }
            mCallback = null;
            return Connection.SUCCESS;
        }

        public String getAddress() throws RemoteException {
            return mBtAdapter.getAddress();
        }
        
        public String getName() throws RemoteException {
            return mBtAdapter.getName();
        }
    };

}
