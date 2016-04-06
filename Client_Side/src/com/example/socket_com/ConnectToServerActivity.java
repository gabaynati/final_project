package com.example.socket_com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.hs.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class ConnectToServerActivity extends Activity {

	TextView textResponse;
	EditText editTextNickName,editTextPassword; 
	Button buttonConnect;
	String nickname;
	String password;
	ServerCommunication server_com;
	boolean isConnectionSucceded;
	String buffer="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




		setContentView(R.layout.connect_layout);
		server_com=new ServerCommunication();
		buttonConnect = (Button)findViewById(R.id.connect);
		textResponse = (TextView)findViewById(R.id.response);
		editTextNickName=(EditText)findViewById(R.id.nickname);
		editTextPassword=(EditText)findViewById(R.id.password);

		buttonConnect.setOnClickListener(buttonConnectOnClickListener);


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
			//checking for Internet connection
			if(!isNetworkAvailable()){
				buffer="You dont have internet connection!\n Please connect to Internet";
				textResponse.setText(buffer);
				return;
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

			try{
				String res="";
				server_com=new ServerCommunication();
				res=server_com.ConnectToServer(addr, port, nickname, password);
				if(res.equals("true")){
					buffer="You have successfully connected to server";
					textResponse.setText(buffer);
					buttonConnect.setVisibility(View.GONE);
					editTextPassword.setVisibility(View.GONE);
					editTextNickName.setVisibility(View.GONE);
					MainActivity.player.setConnectedToServer(true);
					
					//setting server listener:
					server_com.setlistener();
					finish();
				}
				else{
					buffer="You have faild to connect to server";
					textResponse.setText(buffer);
				}
			}catch(NullPointerException e){
				Toast.makeText(getBaseContext(), "err", Toast.LENGTH_LONG).show();

			}



		}};















}

