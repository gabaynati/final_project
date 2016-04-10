package com.example.socket_com;

import java.util.Vector;

import com.example.hs.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GameInfoActivity extends Activity {
	ListView ls1,ls2;
	Vector<String> team1,team2;
	boolean joinTeam=false;
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_info_layout);
		ls1 = (ListView) findViewById (R.id.team1list);
		ls2 = (ListView) findViewById (R.id.team2list);






		if(MainActivity.currentGameTeam1==null)
			team1=new Vector<String>();
		else
			team1=MainActivity.currentGameTeam1;
		if(MainActivity.currentGameTeam2==null)
			team2=new Vector<String>();
		else
			team2=MainActivity.currentGameTeam2;
		//adding join option:
		team1.add("join team");
		team2.add("join team");






		//setting on click methods
		ls1.setOnItemClickListener(new OnItemClickListener(){



			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				String value = (String)parent.getItemAtPosition(position);
				if(value.equals("join team")){

					//checking if the player is already on the team:
					if(team1.contains(MainActivity.player.getNickName())){
						Toast.makeText(getBaseContext(), "You Already In!", Toast.LENGTH_LONG).show();
						return;
					}
					else
					{
						team1.remove(team1.size()-1);
						team2.remove(team2.size()-1);
						team1.add(MainActivity.player.getNickName());
						MainActivity.team=1;
					}



					//checking if the player is on the other team:
					if(team2.contains(MainActivity.player.getNickName())){
						team1.remove(team1.size()-1);
						team2.remove(team2.size()-1);
						team1.add(MainActivity.player.getNickName());
						MainActivity.team=1;
						team2.remove(MainActivity.player.getNickName());
					}


					onCreate(savedInstanceState);

				}

			}

		});


		ls2.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String value = (String)parent.getItemAtPosition(position); 
				if(value.equals("join team")){
					//checking if the player is already on the team:
					if(team2.contains(MainActivity.player.getNickName())){
						Toast.makeText(getBaseContext(), "You Already In!", Toast.LENGTH_LONG).show();
						return;
					}
					else
					{
						team1.remove(team1.size()-1);
						team2.remove(team2.size()-1);
						team2.add(MainActivity.player.getNickName());
						MainActivity.team=2;

					}



					//checking if the player is on the other team:
					if(team1.contains(MainActivity.player.getNickName())){
						team1.remove(team1.size()-1);
						team2.remove(team2.size()-1);
						team2.add(MainActivity.player.getNickName());
						MainActivity.team=2;
						team1.remove(MainActivity.player.getNickName());

					}

					onCreate(savedInstanceState);
				}
			}

		});









		//setting headers
		View header1 = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
		( (TextView)header1.findViewById(R.id.txtHeader)).setText("Team 1");
		ls1.addHeaderView(header1);
		View header2 = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
		( (TextView)header2.findViewById(R.id.txtHeader)).setText("Team 2");
		ls2.addHeaderView(header2);





		//setting footers:
		View footerView1 =  (View)getLayoutInflater().inflate(R.layout.listview_footer_row, null);
		ls1.addFooterView(footerView1);
		View footerView2 =  (View)getLayoutInflater().inflate(R.layout.listview_footer_row, null);
		ls2.addFooterView(footerView2);
		//setting footer on click
		((Button)footerView1.findViewById(R.id.toPlay)).setOnClickListener(joinGame);
		((Button)footerView2.findViewById(R.id.toPlay)).setOnClickListener(joinGame);




		//adding adapters
		CustomListAdapter team1_adapter=new CustomListAdapter(this, R.layout.listview_item_row, team1);
		CustomListAdapter team2_adapter=new CustomListAdapter(this, R.layout.listview_item_row, team2);
		ls1.setAdapter(team1_adapter);
		ls2.setAdapter(team2_adapter);


	}




	OnClickListener joinGame=new OnClickListener() {

		@Override
		public void onClick(View v) {
			//checking if there are at least two players:


			//joining the game
			Toast.makeText(getBaseContext(), "please wait...", Toast.LENGTH_LONG).show();
			MainActivity.server_com.JoinGame(MainActivity.currentGame, MainActivity.team);
			try {
				MainActivity.joinGameSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!MainActivity.joinGameSem.isTimedOut()){
				MainActivity.isJoinedAGame=true;
				//the game must have at least two players.
//				while(team1.size()<2 && team2.size()<2){
//					
//				}
				//moving to game interface
				Intent gameInfo = new Intent("com.example.socket_com.GAMEINTERFACE");
				startActivity(gameInfo);
			}
			else
				Toast.makeText(getBaseContext(), "Error joining the game", Toast.LENGTH_LONG).show();



		}




	};


	@Override
	protected void onDestroy(){
		super.onDestroy();
		//Quitting the game:
		if(MainActivity.isJoinedAGame){	
			MainActivity.server_com.quitGame();
		}
	}
}

