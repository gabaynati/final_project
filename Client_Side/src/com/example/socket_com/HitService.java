package com.example.socket_com;

import com.example.socket_com.GameInterface.ResponseReceiver;
import com.example.socket_com.SingleShotLocationProvider.GPSCoordinates;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;
//this class is a service for listening to hits 
public class HitService extends IntentService{

	public HitService() {
		super("hit");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//blocking thread until hit packet received from server:
		try {
			MainActivity.hitSem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		//sending message that the player got hit
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		sendBroadcast(broadcastIntent);







	}



}
