package com.example.socket_com;



import com.example.hs.R;

import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;




public class ConnectToServerActivity extends Activity {

	TextView textResponse;
	EditText editTextNickName,editTextPassword; 
	Button buttonConnect;
	String nickname;
	String password;
	ProgressBar progBar;
	private int prgBarProgress=10;
	boolean isTryingToConnect=false;
	String buffer="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




		setContentView(R.layout.connect_layout);
		buttonConnect = (Button)findViewById(R.id.connect);
		textResponse = (TextView)findViewById(R.id.response);
		editTextNickName=(EditText)findViewById(R.id.nickname);
		editTextPassword=(EditText)findViewById(R.id.password);
		progBar=(ProgressBar)findViewById(R.id.connectProgBar);
		buttonConnect.setOnClickListener(buttonConnectOnClickListener);
		progBar.setProgress(10);



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

			//checking for Internet connection
			if(!isNetworkAvailable()){
				buffer="You dont have internet connection!\nPlease connect to Internet";
				textResponse.setText(buffer);
				return null;
			}



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



			//trying to connect to server
			res=MainActivity.server_com.ConnectToServer(addr, port, nickname, password);
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


				finish();
			}
			else{
				buffer="You have faild to connect to server";
				textResponse.setText(buffer);
			}

		}

	}






}

