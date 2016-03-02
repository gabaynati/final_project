package com.example.socket_com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.hs.R;
import com.example.socket_com.ServerCommunication.MyClientTask_Connect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;




public class ConnectToServerActivity extends Activity {

	TextView textResponse;
	EditText editTextAddress, editTextPort,editTextNickName,editTextPassword; 
	Button buttonConnect,buttonJoinAGame;
	String nickname;
	String password;
	ServerCommunication server_com;
	boolean isConnectionSucceded;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




		setContentView(R.layout.activity_main);
		server_com=new ServerCommunication();
		editTextAddress = (EditText)findViewById(R.id.address);
		editTextPort = (EditText)findViewById(R.id.port);
		buttonConnect = (Button)findViewById(R.id.connect);
		buttonJoinAGame=(Button)findViewById(R.id.joinAGame);
		buttonJoinAGame.setVisibility(View.INVISIBLE);
		textResponse = (TextView)findViewById(R.id.response);
		editTextNickName=(EditText)findViewById(R.id.nickname);
		editTextPassword=(EditText)findViewById(R.id.password);

		buttonConnect.setOnClickListener(buttonConnectOnClickListener);
		buttonJoinAGame.setOnClickListener(buttonJoinAGameOnClickListener);


	}






	//connect button onClick method
	OnClickListener buttonConnectOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			//String address=editTextAddress.getText().toString();
			//int port=Integer.parseInt(editTextPort.getText().toString());
			String addr=MainActivity.serverIP;
			//MainActivity.serverPort=Integer.parseInt(editTextPort.getText().toString());
			int port=MainActivity.serverPort;
			//nickname=editTextNickName.getText().toString();
			//password=editTextPassword.getText().toString();
			nickname=MainActivity.player.getNickName();
			password=MainActivity.player.getPassword();




			isConnectionSucceded=server_com.ConnectToServer(addr, port, nickname, password);

			if(isConnectionSucceded){
				textResponse.setText("You have successfully connected to server");
				buttonJoinAGame.setVisibility(View.VISIBLE);
				buttonConnect.setVisibility(View.GONE);
				editTextPassword.setVisibility(View.GONE);
				editTextNickName.setVisibility(View.GONE);
				editTextPort.setVisibility(View.GONE);
				editTextAddress.setVisibility(View.GONE);
				MainActivity.player.setConnectedToServer(true);
			}
			else
			{
				textResponse.setText("You have faild to connected to server");
			}

		}};




		//join a game button onClick method
		OnClickListener buttonJoinAGameOnClickListener = new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//moving to Game List activity
				Intent gameList = new Intent("com.example.socket_com.GAMELIST");
				startActivity(gameList);


			}};













}

