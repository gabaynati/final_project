package com.example.socket_com;

import com.example.hs.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class GameInfoActivity extends Activity {
	ListView ls1,ls2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_info_layout);
		  ls1 = (ListView) findViewById (R.id.team1list);
		  ls2 = (ListView) findViewById (R.id.team2list);
		  
		  ls1.setAdapter(new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, MainActivity.currentGameTeam1));
		  ls2.setAdapter(new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, MainActivity.currentGameTeam2));


	}
}
