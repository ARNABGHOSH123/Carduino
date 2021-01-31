package com.example.android.hc05coonect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static com.example.android.hc05coonect.RobotCommands.mmSocket;

public class BluetoothModuleActivity extends AppCompatActivity {

    BluetoothAdapter mAdapter;
    BluetoothDevice devvice;
    Button scan_for_devices;
    ArrayAdapter<String> newDevices;
    static BluetoothModuleActivity activity;
    private AlertDialog.Builder discover,connect;
    private AlertDialog buildDiscover,buildConnect;
    private StringBuilder recDataString = new StringBuilder();
    private Handler bluetoothIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_module);
        activity = this;
        discover = new AlertDialog.Builder(this);
        connect = new AlertDialog.Builder(this);
        scan_for_devices = (Button)findViewById(R.id.button_scan);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mAdapter==null)
            Toast.makeText(getApplicationContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        if (mAdapter.isEnabled())
            mAdapter.disable();
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        try {
            startActivityForResult(intent, 1);
        } catch (Exception ex) {
            BluetoothModuleActivity.this.finish();
            System.exit(0);
        }
    }
    public static BluetoothModuleActivity getInstance()
    {
        return activity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Toast.makeText(getApplicationContext(),"Bluetooth turned on",Toast.LENGTH_SHORT).show();
            scan_for_devices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doDiscovery();
                }
            });
            setTitle("Choose bluetooth module to connect");

            ArrayAdapter<String> pairedDevices = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
            newDevices = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);

            ListView pairedDevicesList = (ListView) findViewById(R.id.paired_devices);
            pairedDevicesList.setAdapter(pairedDevices);
            pairedDevicesList.setOnItemClickListener(mDeviceListener);

            ListView newDevicesList = (ListView) findViewById(R.id.new_devices);
            newDevicesList.setAdapter(newDevices);
            newDevicesList.setOnItemClickListener(mDeviceListener);

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            this.registerReceiver(mReceiver, filter);

            // Register for broadcasts when discovery has finished
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            this.registerReceiver(mReceiver, filter);

            Set<BluetoothDevice> pairDevices = mAdapter.getBondedDevices();
            if(pairDevices.size()>0) {
                findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
                for (BluetoothDevice device : pairDevices) {
                   if (device.getName() != null && device.getBluetoothClass().getDeviceClass() != BluetoothClass.Device.COMPUTER_LAPTOP
                            && device.getBluetoothClass().getDeviceClass() != BluetoothClass.Device.PHONE_SMART
                            && device.getBluetoothClass().getDeviceClass() != BluetoothClass.Device.WEARABLE_HELMET
                            && device.getBluetoothClass().getDeviceClass() != BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET
                            && device.getBluetoothClass().getDeviceClass() != BluetoothClass.Device.COMPUTER_DESKTOP) {
                        devvice=device;
                        pairedDevices.add(device.getName() + "(SPP)(" +device.getAddress() +")\n");
                    }
                }
            }
            else
            {
                pairedDevices.add("No paired SPP devices found");
            }
            if(pairedDevices.getCount()==0)
            {
                pairedDevices.add("No paired SPP devices found");
            }

        }
        else if(resultCode==RESULT_CANCELED)
        {
            Toast.makeText(BluetoothModuleActivity.this,"Bluetooth is disabled",Toast.LENGTH_SHORT).show();
            BluetoothModuleActivity.this.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND))
            {
                BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(dev.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    if(dev.getName()!=null && dev.getBluetoothClass().getDeviceClass()!= BluetoothClass.Device.COMPUTER_LAPTOP
                            && dev.getBluetoothClass().getDeviceClass()!=BluetoothClass.Device.PHONE_SMART
                            && dev.getBluetoothClass().getDeviceClass()!=BluetoothClass.Device.WEARABLE_HELMET
                            && dev.getBluetoothClass().getDeviceClass()!=BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET
                            && dev.getBluetoothClass().getDeviceClass()!=BluetoothClass.Device.COMPUTER_DESKTOP)
                    {
                        devvice=dev;
                        boolean disc = true;
                        if(newDevices!=null)
                        {
                            for(int i=0;i<newDevices.getCount();i++)
                            {
                                if(dev.getAddress().equals(newDevices.getItem(i).substring(newDevices.getItem(i).lastIndexOf('(')+1,newDevices.getItem(i).length()-2)))
                                {
                                   disc = false;
                                    break;
                                }
                            }
                        }

                        if(disc) {
                            newDevices.add(dev.getName() + "(SPP) (" + dev.getAddress() + ")\n");
                            Log.d("Disc status: ", "Dev address is: " + newDevices.getItem(0).substring(newDevices.getItem(0).lastIndexOf('(') + 1, newDevices.getItem(0).length() - 2));
                        }
                    }
                }
            }
            else if((BluetoothAdapter.ACTION_DISCOVERY_FINISHED).equals(action))
            {
                Toast.makeText(getApplicationContext(),"Discovery finished",Toast.LENGTH_SHORT).show();
                buildDiscover.cancel();
                if(newDevices.getCount()==0)
                    newDevices.add("No devices found!");
            }
        }
    };

    public void doDiscovery()
    {
        Log.d("Discovery status: ","Discovery started");
        discover.setView(R.layout.discovery_status).setCancelable(false);
        buildDiscover = discover.create();
        buildDiscover.show();
        scan_for_devices.setVisibility(View.GONE);


        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"Discovery started",Toast.LENGTH_SHORT).show();
        if(mAdapter.isDiscovering())
        {
            mAdapter.cancelDiscovery();
        }
        mAdapter.startDiscovery();
    }

    private  AdapterView.OnItemClickListener mDeviceListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > adapterView, View view,int i, long l){
                try {
                    if(adapterView.getCount()==1 && adapterView.getItemAtPosition(0).toString().equals("No devices found!"))
                    {
                        Toast.makeText(getApplicationContext(),"No available devices",Toast.LENGTH_SHORT).show();
                    }
                    else if(adapterView.getCount()==1 && adapterView.getItemAtPosition(0).toString().equals("No paired SSP devices found"))
                    {
                        Toast.makeText(getApplicationContext(),"No paired bluetooth serial device found",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ConnectThread mConnectThread;
                        mConnectThread = new ConnectThread(devvice);
                        Log.d("TAG", "Device is: " + devvice.getName());
                        mConnectThread.start();
                        connect.setView(R.layout.connection_status).setCancelable(false);
                        buildConnect = connect.create();
                        buildConnect.show();
                    }
                }
                catch (Exception ex)
                {}

        }

    };
     class ConnectThread extends Thread {
        //private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            catch(Exception e){}
            mmSocket = tmp;
        }
        public void run() {
            mAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                BluetoothModuleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buildConnect.cancel();
                        Toast.makeText(BluetoothModuleActivity.this,"Unable to connect to bluetooth device",Toast.LENGTH_SHORT).show();
                    }
                });
                //Toast.makeText(BluetoothModuleActivity.this,"Unable to connect to bluetooth device",Toast.LENGTH_SHORT).show();
                return;
            }
            final ConnectedThread mConnectedThread;
            buildConnect.cancel();
            if (mmSocket == null) {
                //Toast.makeText(ManualActivity.this,"Bluetooth not connected",Toast.LENGTH_LONG).show();
                //startActivity(new Intent(ManualActivity.this,MainActivity.class));
            }
            mConnectedThread = new ConnectedThread();
            mConnectedThread.start();
            if(mmSocket.isConnected()) {
                BluetoothModuleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg((devvice.getName()).trim());
                        Intent intent = new Intent(BluetoothModuleActivity.this, MainActivity.class);
                        startActivity(intent);
                        BluetoothModuleActivity.this.finish();
                    }
                });
            }
            }

        public void cancel() {
            try
            {
                mmSocket.close();
            }
            catch(IOException e) { }
        }
    }
    public void showMsg(String name)
    {
        Toast.makeText(BluetoothModuleActivity.this,"Connected to device: "+name,Toast.LENGTH_SHORT).show();
    }
    class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ConnectedThread() {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {

                    tmpIn = RobotCommands.mmSocket.getInputStream();
                    tmpOut = RobotCommands.mmSocket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void write(String s) {
            try {

                mmOutStream.write(s.getBytes());

            } catch (IOException e) { }
        }
        public void read()
        {
            byte buffer[] = new byte[256];
            int bytes;
            while(true)
            {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        public void run() {
            /**byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;**/
            final int handlerState = 0;
           @SuppressLint("HandlerLeak") Handler bluetoothIn = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    if(msg.what==handlerState) {
                        String readMessage = msg.obj.toString();
                        recDataString.append(readMessage);
                        int endoflineindex = recDataString.indexOf("~");
                        if(endoflineindex > 0 ){
                            String dataInprint = recDataString.substring(0,endoflineindex);
                            int dataLength = dataInprint.length();
                            recDataString.delete(0,recDataString.length());
                        }

                    }

                }
            };

            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_help:
            {Intent intent = new Intent(this.getApplicationContext(),HelpActivity.class);
                startActivity(intent);
                return true;}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        try {
            this.unregisterReceiver(mReceiver);
        }
        catch(java.lang.IllegalArgumentException ex)
        {
            BluetoothModuleActivity.this.finish();
        }
        super.onStop();
    }
}