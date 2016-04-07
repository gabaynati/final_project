package com.example.socket_com;

import java.util.Vector;

import com.example.hs.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class FindGameActivity extends Activity {

	private Vector<String> gameList=MainActivity.gameList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_list_layout);

	}






	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_game, menu);
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
	protected void onResume(){
		super.onResume();
		
		
//		//getting gameList from server
//		String res=server_com.sendGameListRequest();
		gameList=MainActivity.gameList;
		if(gameList!=null){

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, gameList);

			ListView listView = (ListView) findViewById(R.id.mobile_list);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapter, View v, int position,long arg3) 
				{
					String value = (String)adapter.getItemAtPosition(position); 
					MainActivity.currentGame=value;
					String res= MainActivity.server_com.getGameInfo(value);
					if(res.equals("true")){
					Toast.makeText(getBaseContext(), "Please wait...", Toast.LENGTH_LONG).show();
					//moving to game interface
					Intent gameInfo = new Intent("com.example.socket_com.GAMEINFOACTIVITY");
					startActivity(gameInfo);
					finish();
					}
					else
						Toast.makeText(getBaseContext(), res, Toast.LENGTH_LONG).show();


				}


			});


		}
		else{
			Toast.makeText(getBaseContext(), "Error while getting game list from the server", Toast.LENGTH_LONG).show();
			finish();
		}

	}

}
