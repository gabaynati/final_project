package com.example.socket_com;

import java.util.Vector;

import com.example.hs.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
		
		
		//setting headers
		
		
        View header1 = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
       ( (TextView)header1.findViewById(R.id.txtHeader)).setText("Team 1");
        ls1.addHeaderView(header1);
        
        
        View header2 = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        ( (TextView)header2.findViewById(R.id.txtHeader)).setText("Team 2");
        ls2.addHeaderView(header2);
        
        
		Vector<String> team1,team2;
		if(MainActivity.currentGameTeam1==null)
			team1=new Vector<String>();
		else
			team1=MainActivity.currentGameTeam1;
		if(MainActivity.currentGameTeam2==null)
			team2=new Vector<String>();
		else
			team2=MainActivity.currentGameTeam2;

		
		
		PlayerListAdapter team1_adapter=new PlayerListAdapter(this, R.layout.listview_item_row, team1);
		PlayerListAdapter team2_adapter=new PlayerListAdapter(this, R.layout.listview_item_row, team2);
		ls1.setAdapter(team1_adapter);
		ls2.setAdapter(team2_adapter);


	}
}
