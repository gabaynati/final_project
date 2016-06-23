package com.example.hs;

import java.util.Vector;

import com.example.socket_com.GameDB;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameManagerActivity extends Activity {
	Button buttonClearDB;
	Button buttonGetUsers;
	Button buttonResetServer;
	Button buttonPrintDB;
	TextView response;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_manager);
		buttonClearDB=(Button)findViewById(R.id.clearDB);
		buttonGetUsers=(Button)findViewById(R.id.getAllUsers);
		buttonResetServer=(Button)findViewById(R.id.resetServer);
		buttonPrintDB=(Button)findViewById(R.id.printDB);
		response = (TextView)findViewById(R.id.response_);
		buttonClearDB.setOnClickListener(buttonClearDBClickListener);
		buttonGetUsers.setOnClickListener(buttonGetUsersClickListener);
		buttonResetServer.setOnClickListener(buttonResetServerClickListener);
		buttonPrintDB.setOnClickListener(buttonPrintDBClickListener);

	}
	OnClickListener buttonClearDBClickListener= new OnClickListener(){
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {
		}
	};
	OnClickListener buttonGetUsersClickListener = new OnClickListener(){
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {

		}
	};
	OnClickListener buttonResetServerClickListener = new OnClickListener(){
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {

		}
	};
	OnClickListener buttonPrintDBClickListener = new OnClickListener(){
		@SuppressLint("NewApi")
		@Override
		public void onClick(View arg0) {
			Vector<Vector<Object>> db=GameDB.printDB();
			if(db!=null)
				response.setText(db.toString());
			else
				response.setText("null");

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_manager, menu);
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
