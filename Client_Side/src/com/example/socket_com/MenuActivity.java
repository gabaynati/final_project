package com.example.socket_com;

import com.example.hs.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {



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

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}
}
