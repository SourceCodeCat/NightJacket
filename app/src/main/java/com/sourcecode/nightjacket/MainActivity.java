package com.sourcecode.nightjacket;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final int COMMAND_OFF = 0;
	private static final int COMMAND_ON = 1;
	private static final int COMMAND_BLINK = 4;
    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbEndpoint mEndpoint;
    private UsbInterface mUsbInterface;
    TextView tv;
    Button btnOn;
    Button btnOff;
    Button btnBlink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOn = (Button)findViewById(R.id.on);
        btnOn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendCommand(COMMAND_ON);
				
			}
		});
        btnOff = (Button)findViewById(R.id.off); 
        btnOff.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendCommand(COMMAND_OFF);
				
			}
		});
        btnBlink = (Button)findViewById(R.id.blink);
        btnBlink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendCommand(COMMAND_BLINK);
				
			}
		});
        
		tv = (TextView)findViewById(R.id.texto);
		tv.setText("aja!");        
		//TODO: Obtemos el Manager USB del sistema Android 
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		
		// TODO: Recuperamos todos los dispositvos USB detectados
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
						
		//TODO: en nuestor ejemplo solo conectamos un disposito asi que sera 
		// el unico que encontraremos.
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		if(deviceIterator.hasNext()){
			mDevice = deviceIterator.next();
		    	Log.d("MARCO", "Name: " + mDevice.getDeviceName());
		    	Log.d("MARCO", "Protocol: " + mDevice.getDeviceProtocol());
		    	Log.d("MARCO", "DeviceID: " + mDevice.getDeviceId());
		    	Log.d("MARCO", "CLASS: " + mDevice.getDeviceClass());
		    	Log.d("MARCO", "VendroID: " + mDevice.getVendorId());
		    	Log.d("MARCO", "ProductID: " + mDevice.getProductId());
		    	Log.d("MARCO", "InterfaceCount: " + mDevice.getInterfaceCount());
		    	
		    	
		    	//TODO: Solicitamos el permiso al usuario.
		    	//mUsbManager.requestPermission(mDevice, mPermissionIntent);
		    	tv.setText(mDevice.getVendorId()+"");
		}
		else
		{
			Log.e("MARCO", "Dispositvo USB no detectado.");
		}	
		
		

        
		//TODO: we should be able  to directly use the USB device we're interested in, since we already filter it with a xml
		
//		mUsbInterface = mDevice.getInterface(1);
//		Log.d("MARCO", "Interface "+mUsbInterface.getId()+", EndpointCount : " + mUsbInterface.getEndpointCount());
//		Log.d("MARCO", "Interface EP0 TYPE: " + mUsbInterface.getEndpoint(0).getType());
//		Log.d("MARCO", "Interface EP0 DIRECTION: " + mUsbInterface.getEndpoint(0).getDirection());
//		Log.d("MARCO", "Interface EP1 TYPE: " + mUsbInterface.getEndpoint(1).getType());
//		Log.d("MARCO", "Interface EP1 DIRECTION: " + mUsbInterface.getEndpoint(1).getDirection());
//		
		//mUsbInterface = mDevice.getInterface(0);
		//Log.d("MARCO", "Interface "+mUsbInterface.getId()+", EndpointCount : " + mUsbInterface.getEndpointCount());
		//Log.d("MARCO", "Interface EP0 TYPE: " + mUsbInterface.getEndpoint(0).getType());	
		
		
		
		
		
    }
    
    public void sendCommand(int command)
    {
		mUsbInterface = mDevice.getInterface(1);
		mEndpoint = mUsbInterface.getEndpoint(0);
		Log.d("MARCO", "Interface "+mUsbInterface.getId()+", EndpointCount : " + mUsbInterface.getEndpointCount());
		Log.d("MARCO", "Interface EP0 TYPE: " + mEndpoint.getType());   
		Log.d("MARCO", "Interface EP0 DIRECTION: " + mEndpoint.getDirection());
		
		mConnection = mUsbManager.openDevice(mDevice); 
		if(mConnection == null)
		{
			Log.e("MARCO", "couldn't open a connection");
		}
		else
		{
            byte[] message = new byte[1];
            message[0] = (byte)command;
            // Send command via a control request on endpoint zero
            mConnection.claimInterface(mUsbInterface, true);
            int result = mConnection.bulkTransfer(mEndpoint, message, message.length, 1000);
            Log.d("MARCO", "bulkTransferResult: " + result);
		}
		//connection.claimInterface(mUsbInterface, forceClaim);
		//connection.bulkTransfer(mEndpoint, bytes, bytes.length, TIMEOUT); //do in another thread		
    	
    }
    
}
