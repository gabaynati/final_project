package com.example.socket_com;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.example.hs.R;
import com.example.hs.R.id;
import com.example.hs.R.layout;
import com.example.hs.R.menu;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	//*********server configurations****************/
	public static String serverIP="192.168.43.191";
	public static int serverPort=9001;
	public static int playerPort=9004;
	public static Player player=new Player("nati","1234");
	public static String enemy="gili";
	public static String currentGame=null;
	public static int team=-1;
	public static boolean isJoinedAGame=false;
	public static Vector<String> currentGameTeam1,currentGameTeam2;
	public static Vector<String> gameList;
	public static ServerCommunication server_com=new ServerCommunication();
	public static mySemaphore connectSem=new mySemaphore(0);
	public static mySemaphore getGameListSem=new mySemaphore(0);
	public static mySemaphore getGameInfoSem=new mySemaphore(0);
	public static mySemaphore joinGameSem=new mySemaphore(0);
	public static Semaphore hitSem=new Semaphore(0);
	public static boolean flag;
	public static float hitterAzimuth;
	public static float hitterLatitude;
	public static float hitterLongitude;
	public static boolean isConnected=false;

	//*************************************************/

	Button buttonConnectToServer;
	Button buttonRegisterToSystem;
	Button buttonToGame;
	Button buttonJoinAGame;
	Button buttonCreateAGame;
	Button buttonLogOut;
	Button buttonExit;
	Button buttonMyAccount;
	TextView txtResponse;
	ImageView background;
	private MediaPlayer mediaPlayer_background,mediaPlayer_buttonClick;
	ActivityAnimation anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);
		

	
		
		
		
		buttonConnectToServer = (Button)findViewById(R.id.connectToServer);
		buttonRegisterToSystem = (Button)findViewById(R.id.registerToSystem);
		buttonToGame = (Button)findViewById(R.id.toGame);
		buttonJoinAGame = (Button)findViewById(R.id.joinAGame);
		buttonCreateAGame = (Button)findViewById(R.id.createAGame);
		buttonLogOut=(Button)findViewById(R.id.logOut);
		buttonExit=(Button)findViewById(R.id.exit);
		buttonMyAccount=(Button)findViewById(R.id.myAccount);
		txtResponse = (TextView)findViewById(R.id.txtResponse);
		background=(ImageView)findViewById(R.id.backImage);

		buttonConnectToServer.setOnClickListener(buttonConnectToServerOnClickListener);
		buttonRegisterToSystem.setOnClickListener(buttonRegisterToSystemOnClickListener);
		buttonToGame.setOnClickListener(buttonToGameOnClickListener);
		buttonJoinAGame.setOnClickListener(buttonJoinAGameOnClickListener);
		buttonCreateAGame.setOnClickListener(buttonCreateAGameOnClickListener);
		buttonLogOut.setOnClickListener(buttonLogOutOnClickListener);
		buttonMyAccount.setOnClickListener(buttonMyAccountOnClickListener);
		buttonExit.setOnClickListener(buttonExitOnClickListener);
		
		
		//playing audio
		mediaPlayer_buttonClick=MediaPlayer.create(getBaseContext(), R.raw.srr61_shoot_sound);
		mediaPlayer_background = MediaPlayer.create(getBaseContext(), R.raw.background_audio);
		mediaPlayer_background.start();
		
		
		//animation:
		anim=new ActivityAnimation(getApplicationContext());
		
	}


	
	
	
	
	//********buttons on clicks***********/


	//disconnect button onClick method
	OnClickListener buttonLogOutOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			
			//playing sound:
			mediaPlayer_buttonClick.start();
			
			
			String result="";
			//disconnecting from server
			if(!MainActivity.player.isConnectedToServer()){
				txtResponse.setText("You have not logged in yet");
				//Log.d("DDDDDD:", "NOT CONNECTED");
				return;

			}
			//Log.d("DDDDDD:", "CONNECTED");


			result=server_com.disconnectFromServer();
			if(result.equals("true"))
				txtResponse.setText("You have disconneted from server");
			MainActivity.isConnected=false;

			buttonJoinAGame.setVisibility(View.GONE);
			buttonCreateAGame.setVisibility(View.GONE);
			buttonLogOut.setVisibility(View.GONE);
			buttonMyAccount.setVisibility(View.GONE);
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
			//playing sound:
			mediaPlayer_buttonClick.start();
			buttonLogOut.callOnClick();
			finish();
			System.exit(0);
		}
	};
	//exit button onClick method
	OnClickListener buttonMyAccountOnClickListener = new OnClickListener(){
		
		@Override
		public void onClick(View arg0) {
			//playing sound:
			mediaPlayer_buttonClick.start();
			Intent myAccount = new Intent("com.example.socket_com.MYACCOUNTACTIVITY");
			startActivity(myAccount);
		}
	};
	
	//join a game button onClick method
	OnClickListener buttonJoinAGameOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			//playing sound:
			mediaPlayer_buttonClick.start();
			Intent connectToServer = new Intent("com.example.socket_com.FINDGAMEACTIVITY");
			startActivity(connectToServer);
		}
	};
	
	//create a game button onClick method
	OnClickListener buttonCreateAGameOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			//playing sound:
			mediaPlayer_buttonClick.start();
			Intent connectToServer = new Intent("com.example.socket_com.CREATEAGAMEACTIVITY");
			startActivity(connectToServer);
		}
	};

	//connect button onClick method
	OnClickListener buttonConnectToServerOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			//playing sound:
			mediaPlayer_buttonClick.start();
			//moving to connect to server
			Intent connectToServer = new Intent("com.example.socket_com.CONNECTTOSERVERACTIVITY");
			startActivity(connectToServer);

		}
	};
	//connect button onClick method
	OnClickListener buttonRegisterToSystemOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			//playing sound:
			mediaPlayer_buttonClick.start();
			//moving to register to server
			Intent RegistertoServer = new Intent("com.example.socket_com.REGISTERACTIVITY");
			startActivity(RegistertoServer);

		}
	};
	//toGame button onClick method
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
		//disconnecting from server
		if(isConnected)
		server_com.disconnectFromServer();
		finish();
		System.exit(0);
	}
	@Override
	protected void onPause(){
		super.onPause();
		mediaPlayer_background.pause();

	}
	@Override
	protected void onResume(){
		super.onResume();
		anim.fade(background);
		mediaPlayer_background.start();
		if(MainActivity.player.isConnectedToServer()){
			txtResponse.setText("Logged in as: "+MainActivity.player.getNickName());
			buttonJoinAGame.setVisibility(View.VISIBLE);
			buttonCreateAGame.setVisibility(View.VISIBLE);
			buttonLogOut.setVisibility(View.VISIBLE);
			buttonConnectToServer.setVisibility(View.GONE);
			buttonRegisterToSystem.setVisibility(View.GONE);
			buttonMyAccount.setVisibility(View.VISIBLE);
		}

	}
}
