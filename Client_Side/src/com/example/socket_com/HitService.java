package com.example.socket_com;

import com.example.socket_com.GameInterface.ResponseReceiver;
import com.example.socket_com.SingleShotLocationProvider.GPSCoordinates;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
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

		
		
//		SingleShotLocationProvider.requestSingleUpdate(getBaseContext(), new SingleShotLocationProvider.LocationCallback() {
//		@Override 
//		public void onNewLocationAvailable(GPSCoordinates location) {
//
//
//			//Gathering this player's GPS and hitter player's GPS which has been received from server:
//			loc = new Location("thisLoc");
//			tar = new Location("hitterLoc");
//
//			loc.setLatitude(location.latitude);
//			loc.setLongitude(location.longitude);
//			tar.setLatitude(MainActivity.hitterLatitude);
//			tar.setLongitude(MainActivity.hitterLongitude);
//
//
//			float deg = logic.isInjured(loc, tar, MainActivity.hitterAzimuth);
//
//			Toast toast = Toast.makeText(getApplicationContext(), "azimuth = " + deg, 10000);
//			toast.show();
//
//			//checking if this player got shot by someone.
//			/*if(logic.isInjured(loc, tar, MainActivity.hitterAzimuth)){
//
//			}*/
//		}
//
//	
//	});
		
	
		
		
		
		
		//sending message that the player got hit
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		sendBroadcast(broadcastIntent);
		
	}



}
