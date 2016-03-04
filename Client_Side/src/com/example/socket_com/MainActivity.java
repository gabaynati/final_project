package com.example.socket_com;

import java.net.Socket;

import com.example.hs.R;
import com.example.hs.R.id;
import com.example.hs.R.layout;
import com.example.hs.R.menu;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {

	//*********server configurations****************/
	public static Socket socket;
	public static String serverIP="192.168.1.14";
	public static int serverPort=9000;
	public static Player player=new Player("gili","1234");
	public static String enemy="nati";
	//*************************************************/

	Button buttonConnectToServer;
	Button buttonRegisterToSystem;
	Button buttonToGame;
	Button buttonJoinAGame;
	Button buttonLogOut;
	Button buttonExit;
	TextView txtResponse;
	private MediaPlayer mediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);

		buttonConnectToServer = (Button)findViewById(R.id.connectToServer);
		buttonRegisterToSystem = (Button)findViewById(R.id.registerToSystem);
		buttonToGame = (Button)findViewById(R.id.toGame);
		buttonJoinAGame = (Button)findViewById(R.id.joinAGame);
		buttonLogOut=(Button)findViewById(R.id.logOut);
		buttonExit=(Button)findViewById(R.id.exit);
		txtResponse = (TextView)findViewById(R.id.txtResponse);


		buttonConnectToServer.setOnClickListener(buttonConnectToServerOnClickListener);
		buttonRegisterToSystem.setOnClickListener(buttonRegisterToSystemOnClickListener);
		buttonToGame.setOnClickListener(buttonToGameOnClickListener);
		buttonJoinAGame.setOnClickListener(buttonJoinAGameOnClickListener);
		buttonLogOut.setOnClickListener(buttonLogOutOnClickListener);
		buttonExit.setOnClickListener(buttonExitOnClickListener);

		//playing audio
		mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.background_audio);
		mediaPlayer.start(); 
	}


	//********buttons on clicks***********/


	//disconnect button onClick method
	OnClickListener buttonLogOutOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			String result="";
			//disconnecting from server
			if(!MainActivity.player.isConnectedToServer()){
				txtResponse.setText("You have not logged in yet");
				//Log.d("DDDDDD:", "NOT CONNECTED");
				return;

			}
			//Log.d("DDDDDD:", "CONNECTED");


			ServerCommunication server_com=new ServerCommunication();
			result=server_com.disconnectFromServer();
			txtResponse.setText(result);
			buttonJoinAGame.setVisibility(View.GONE);
			buttonLogOut.setVisibility(View.GONE);
			buttonConnectToServer.setVisibility(View.VISIBLE);
			buttonRegisterToSystem.setVisibility(View.VISIBLE);
			MainActivity.player.setConnectedToServer(false);
		}
	};

	//exit button onClick method
	OnClickListener buttonExitOnClickListener = new OnClickListener(){
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {
			buttonLogOut.callOnClick();
			finish();
			System.exit(0);
		}
	};
	
	
	//join a game button onClick method
	OnClickListener buttonJoinAGameOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Intent connectToServer = new Intent("com.example.socket_com.GAMELISTACTIVITY");
			startActivity(connectToServer);
		}
	};

	//connect button onClick method
	OnClickListener buttonConnectToServerOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			//moving to connect to server
			Intent connectToServer = new Intent("com.example.socket_com.CONNECTTOSERVERACTIVITY");
			startActivity(connectToServer);

		}
	};
	//connect button onClick method
	OnClickListener buttonRegisterToSystemOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			//moving to register to server
			Intent RegistertoServer = new Intent("com.example.socket_com.REGISTERACTIVITY");
			startActivity(RegistertoServer);

		}
	};
	//connect button onClick method
	OnClickListener buttonToGameOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			//moving to game interface
			Intent gameInterface = new Intent("com.example.socket_com.GAMEINTERFACE");
			startActivity(gameInterface);

		}
	};
	/*************************************/







	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();


	}
	@Override
	protected void onPause(){
		super.onPause();
		mediaPlayer.pause();

	}
	protected void onResume(){
		super.onPause();
		mediaPlayer.start();
		if(MainActivity.player.isConnectedToServer()){
			txtResponse.setText("Logged in as: "+MainActivity.player.getNickName());
			buttonJoinAGame.setVisibility(View.VISIBLE);
			buttonLogOut.setVisibility(View.VISIBLE);
			buttonConnectToServer.setVisibility(View.GONE);
			buttonRegisterToSystem.setVisibility(View.GONE);
		}

	}
}
