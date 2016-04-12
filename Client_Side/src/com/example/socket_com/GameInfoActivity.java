package com.example.socket_com;

import java.util.Vector;

import com.example.hs.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
	CustomListAdapter team1_adapter,team2_adapter;
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_info_layout);
		ls1 = (ListView) findViewById (R.id.team1list);
		ls2 = (ListView) findViewById (R.id.team2list);

		getGameInfo();




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
					//if the user want to switch teams:
					else if	(team2.contains(MainActivity.player.getNickName())){
						//removing from the other team:
						team2.remove(MainActivity.player.getNickName());

						//joining to this team
						team1.remove(team1.size()-1);
						team1.add(MainActivity.player.getNickName());
						team1.add("join team");
						MainActivity.team=1;
					}
					else//player is not in team:
					{
						team1.remove(team1.size()-1);
						team1.add(MainActivity.player.getNickName());
						team1.add("join team");
						MainActivity.team=1;
					}






					//adding adapters
					updateLists();
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
					//checking if the player is on the other team:
					else if(team1.contains(MainActivity.player.getNickName())){
						team2.remove(team2.size()-1);
						team2.add(MainActivity.player.getNickName());
						team2.add("join team");
						MainActivity.team=2;
						team1.remove(MainActivity.player.getNickName());
					}
					else
					{
						team2.remove(team2.size()-1);
						team2.add(MainActivity.player.getNickName());
						team2.add("join team");
						MainActivity.team=2;
					}	
					//adding adapters
					updateLists();
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
		team1_adapter=new CustomListAdapter(this, R.layout.listview_item_row, team1);
		team2_adapter=new CustomListAdapter(this, R.layout.listview_item_row, team2);
		ls1.setAdapter(team1_adapter);
		ls2.setAdapter(team2_adapter);





		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				while(true){

							getGameInfo();
							updateLists();
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					
				}
			}
		});
	}

	private void getGameInfo(){
		String res= MainActivity.server_com.getGameInfo(MainActivity.currentGame);

		//blocking thread until the server responses with the data or until timeout occur.
		try {
			MainActivity.getGameInfoSem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//if timeout occurred then there is no response from the server  
		if(!MainActivity.getGameInfoSem.isTimedOut()){
			//Toast.makeText(getBaseContext(), "Please wait...", Toast.LENGTH_LONG).show();

		}
		else
			Toast.makeText(getBaseContext(), "Error while getting game info from the server", Toast.LENGTH_LONG).show();

	}

	private void updateLists(){
		team1_adapter.notifyDataSetChanged();
		team2_adapter.notifyDataSetChanged();
	}

	OnClickListener joinGame=new OnClickListener() {

		@Override
		public void onClick(View v) {
			//checking if there are at least two players:


			//joining the game
			//Toast.makeText(getBaseContext(), "please wait...", Toast.LENGTH_LONG).show();
			MainActivity.server_com.JoinGame(MainActivity.currentGame, MainActivity.team);
			try {
				MainActivity.joinGameSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!MainActivity.joinGameSem.isTimedOut()){
				MainActivity.isJoinedAGame=true;
				if(team1.size()>=2 && team2.size()>=2){
					//moving to game interface
					Intent gameInfo = new Intent("com.example.socket_com.GAMEINTERFACE");
					startActivity(gameInfo);
					finish();
					return;
				}
				else
					Toast.makeText(getBaseContext(), "You need at least two players", Toast.LENGTH_LONG).show();


				//the game must have at least two players.
				//therefore pull
				if(team1.size()<2 || team2.size()<2){


				}

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

