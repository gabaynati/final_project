package com.example.socket_com;




import com.example.hs.R;
import com.example.socket_com.SingleShotLocationProvider.GPSCoordinates;

import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;




public class ConnectToServerActivity extends Activity {

	TextView textResponse;
	ImageView background;
	EditText editTextNickName,editTextPassword; 
	Button buttonConnect;
	String nickname;
	String password;
	ProgressBar progBar;
	private int prgBarProgress=10;
	boolean isTryingToConnect=false;
	String buffer="";
	ActivityAnimation anim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




		setContentView(R.layout.connect_layout);
		buttonConnect = (Button)findViewById(R.id.connect);
		background=(ImageView)findViewById(R.id.imageView);
		textResponse = (TextView)findViewById(R.id.response);
		editTextNickName=(EditText)findViewById(R.id.nickname);
		editTextPassword=(EditText)findViewById(R.id.password);
		progBar=(ProgressBar)findViewById(R.id.connectProgBar);
		buttonConnect.setOnClickListener(buttonConnectOnClickListener);
		progBar.setProgress(10);
		//zoom();
		//move();
		anim=new ActivityAnimation(getApplicationContext());
		
		GPSTracker gps = new GPSTracker(getBaseContext());

		if(gps.canGetLocation()){ // gps enabled} // return boolean true/false
			MainActivity.loc=new GPSLocation(gps.getLatitude(), gps.getLongitude());
		}

	}
	private void setProgressByTimer(){

		new AsyncTask<Void, Void, Void>() {

			int progressFactor=10;



			@Override
			protected Void doInBackground(Void... params) {
				while(prgBarProgress<100){				
					prgBarProgress+=progressFactor;
					publishProgress();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				}
				return null;
			}	


			@Override
			protected void onProgressUpdate(Void... v) {
				super.onProgressUpdate(v);
				progBar.setProgress(prgBarProgress);
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				progBar.setProgress(10);
				prgBarProgress=10;
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}




	//connect button onClick method
	OnClickListener buttonConnectOnClickListener = new OnClickListener(){


		@Override
		public void onClick(View arg0) {






			//running progressBar timer:
			textResponse.setText("please wait...");
			//checking for Internet connection
			if(!isNetworkAvailable()){
				buffer="You dont have internet connection!\nPlease connect to Internet";
				textResponse.setText(buffer);
				return;
			}
			//trying to connect to server
			new connectThread().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,arg0);



		}

	};




	public class connectThread extends AsyncTask<View, Void, Void> {
		View view;
		boolean isConnectionSucced=false;
		@Override
		protected Void doInBackground(View... params) {
			//setting the onclick method off
			view=params[0];
			view.setClickable(false);





			String addr=MainActivity.serverIP;
			int port=MainActivity.serverPort;
			//nickname=editTextNickName.getText().toString();
			//password=editTextPassword.getText().toString();
			nickname=MainActivity.player.getNickName();
			password=MainActivity.player.getPassword();


			/*
//checking if the user name exists in DB
String isExists=GameDB.isExists(nickname,password);
if(!isExists.equals("exists")){
	textResponse.setText("User name not registered!");
	return;

}
			 */


			String res="";
			//setting server listener:
			MainActivity.server_com.openSocket();
			MainActivity.server_com.setlistener();


		
//			//getting gps coordinates:
//			SingleShotLocationProvider.requestSingleUpdate(getBaseContext(), new SingleShotLocationProvider.LocationCallback() {
//			@Override 
//			public void onNewLocationAvailable(GPSCoordinates location) {
//
//
//				//Gathering this player's GPS and hitter player's GPS which has been received from server:
//				loc=new GPSLocation(location.latitude, location.longitude);
//		
//			}
//		});
			
			
			//trying to connect to server
			res=MainActivity.server_com.ConnectToServer(addr, port, nickname, password,MainActivity.loc);
			//blocking thread until the server responses with the data or until timeout occur.


			setProgressByTimer();

			try {
				MainActivity.connectSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!MainActivity.connectSem.isTimedOut())
				isConnectionSucced=true;
			return null;

		}


		@Override
		protected void onProgressUpdate(Void... v) {
			super.onProgressUpdate(v);


		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			view.setClickable(true);

			//if timeout occurred then there is no response from the server  
			if(isConnectionSucced){
				buffer="You have successfully connected to server";
				textResponse.setText(buffer);
				buttonConnect.setVisibility(View.GONE);
				editTextPassword.setVisibility(View.GONE);
				editTextNickName.setVisibility(View.GONE);
				MainActivity.player.setConnectedToServer(true);
				MainActivity.isConnected=true;

				finish();
			}
			else{
				MainActivity.isConnected=false;
				buffer="You have faild to connect to server";
				textResponse.setText(buffer);
			}

		}

	}
	@Override
	protected void onResume(){
		super.onResume();
		anim.clockwise(background);
	}



}

