package com.example.socket_com;

import java.net.Socket;

import com.example.hs.R;
import com.example.hs.R.id;
import com.example.hs.R.layout;
import com.example.hs.R.menu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {
	
	//*********server configurations****************/
	public static Socket socket;
	public static String serverIP="84.109.200.21";
	public static int serverPort=9000;
	public static Player player=new Player("gili","1234");
	//*************************************************/
	
	Button buttonConnectToServer;
	Button buttonRegisterToSystem;
	Button buttonToGame;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_layout);

		buttonConnectToServer = (Button)findViewById(R.id.connectToServer);
		buttonRegisterToSystem = (Button)findViewById(R.id.registerToSystem);
		buttonToGame = (Button)findViewById(R.id.toGame);


		
		
		buttonConnectToServer.setOnClickListener(buttonConnectToServerOnClickListener);
		buttonRegisterToSystem.setOnClickListener(buttonRegisterToSystemOnClickListener);
		buttonToGame.setOnClickListener(buttonToGameOnClickListener);

	}


	//********buttons on clicks***********/
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
			//moving to tegister to server
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
}
