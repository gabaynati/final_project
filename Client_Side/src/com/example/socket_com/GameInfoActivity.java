package com.example.socket_com;

import java.util.Iterator;
import java.util.Vector;

import com.example.hs.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
	Vector<String> tempTeam1,tempTeam2;
	CustomListAdapter team1_adapter,team2_adapter;
	Vector<updateGameInfo_Thread> updateThreads=new Vector<updateGameInfo_Thread>();
	boolean isJoinedAGame=false;
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







		//setting on click methods
		ls1.setOnItemClickListener(new OnItemClickListener(){



			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				String value = (String)parent.getItemAtPosition(position);
				if(value.equals("join team")){

					/*
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
						//team1.remove(team1.size()-1);
						team1.add(MainActivity.player.getNickName());
						//	team1.add("join team");
						MainActivity.team=1;
					}
					else//player is not in team:
					{
						//team1.remove(team1.size()-1);
						team1.add(MainActivity.player.getNickName());
						//	team1.add("join team");
						MainActivity.team=1;
					}






					//adding adapters
					updateLists();
					 */
				}

			}

		});


		ls2.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String value = (String)parent.getItemAtPosition(position); 
				if(value.equals("join team")){
					/*
					//checking if the player is already on the team:
					if(team2.contains(MainActivity.player.getNickName())){
						Toast.makeText(getBaseContext(), "You Already In!", Toast.LENGTH_LONG).show();
						return;
					}
					//checking if the player is on the other team:
					else if(team1.contains(MainActivity.player.getNickName())){
						//	team2.remove(team2.size()-1);
						team2.add(MainActivity.player.getNickName());
						//team2.add("join team");
						MainActivity.team=2;
						team1.remove(MainActivity.player.getNickName());
					}
					else
					{
						//team2.remove(team2.size()-1);
						team2.add(MainActivity.player.getNickName());
						//team2.add("join team");
						MainActivity.team=2;
					}	
					//adding adapters
					updateLists();
					 */
				}
			}

		});









		//setting headers
		View header1 = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
		( (TextView)header1.findViewById(R.id.txtHeader)).setText("Team 1");

		ls1.addHeaderView(header1,null, false);
		View header2 = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
		( (TextView)header2.findViewById(R.id.txtHeader)).setText("Team 2");
		ls2.addHeaderView(header2,null, false);





		//setting footers:
		View footerView1 =  (View)getLayoutInflater().inflate(R.layout.listview_footer_row, null);
		ls1.addFooterView(footerView1);
		View footerView2 =  (View)getLayoutInflater().inflate(R.layout.listview_footer_row, null);
		ls2.addFooterView(footerView2);
		//setting footer on click
		((Button)footerView1.findViewById(R.id.toPlay)).setOnClickListener(joinGame);
		((Button)footerView2.findViewById(R.id.toPlay)).setOnClickListener(joinGame);
		((Button)footerView1.findViewById(R.id.joinTeam)).setOnClickListener(joinTeam1);
		((Button)footerView2.findViewById(R.id.joinTeam)).setOnClickListener(joinTeam2);

		//adding adapters
		team1_adapter=new CustomListAdapter(this, R.layout.listview_item_row, team1);
		team2_adapter=new CustomListAdapter(this, R.layout.listview_item_row, team2);

		ls1.setAdapter(team1_adapter);
		ls2.setAdapter(team2_adapter);






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
	private void addNewPlayerToTeams(Vector<String> to,Vector<String> from){
		for(int i=0;i<from.size();i++){
			if(!to.contains(from.elementAt(i)) &&!from.elementAt(i).equals("join team"))
				to.add(0, from.elementAt(i));
		}

	}



	private void updateLists(){

		Vector<String> temp_team1=new Vector<String>();
		Vector<String> temp_team2=new Vector<String>();




		if(MainActivity.currentGameTeam1==null)
			temp_team1=new Vector<String>();
		else{

			temp_team1=MainActivity.currentGameTeam1;
			addNewPlayerToTeams(team1, temp_team1);

		}
		if(MainActivity.currentGameTeam2==null)
			temp_team2=new Vector<String>();
		else{

			temp_team2=MainActivity.currentGameTeam2;
			addNewPlayerToTeams(team2, temp_team2);
		}


		team1_adapter.notifyDataSetChanged();
		team2_adapter.notifyDataSetChanged();





	}

	OnClickListener joinTeam1=new OnClickListener() {

		@Override
		public void onClick(View v) {
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
				//team1.remove(team1.size()-1);
				team1.add(MainActivity.player.getNickName());
				//	team1.add("join team");
				MainActivity.team=1;
			}
			else//player is not in team:
			{
				//team1.remove(team1.size()-1);
				team1.add(MainActivity.player.getNickName());
				//	team1.add("join team");
				MainActivity.team=1;
			}

			team1_adapter.notifyDataSetChanged();
			team2_adapter.notifyDataSetChanged();

		}

	};
	OnClickListener joinTeam2=new OnClickListener() {

		@Override
		public void onClick(View v) {
			//checking if the player is already on the team:
			if(team2.contains(MainActivity.player.getNickName())){
				Toast.makeText(getBaseContext(), "You Already In!", Toast.LENGTH_LONG).show();
				return;
			}
			//checking if the player is on the other team:
			else if(team1.contains(MainActivity.player.getNickName())){
				//	team2.remove(team2.size()-1);
				team2.add(MainActivity.player.getNickName());
				//team2.add("join team");
				MainActivity.team=2;
				team1.remove(MainActivity.player.getNickName());
			}
			else
			{
				//team2.remove(team2.size()-1);
				team2.add(MainActivity.player.getNickName());
				//team2.add("join team");
				MainActivity.team=2;
			}	
			team1_adapter.notifyDataSetChanged();
			team2_adapter.notifyDataSetChanged();

		}

	};
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
				isJoinedAGame=true;
				MainActivity.isJoinedAGame=true;
				if(team1.size()>=1 && team2.size()>=1){
					//moving to game interface
					Intent gameInfo = new Intent("com.example.socket_com.GAMEINTERFACE");
					startActivity(gameInfo);
					finish();
					return;
				}
				else
					Toast.makeText(getBaseContext(), "You need at least two players", Toast.LENGTH_LONG).show();




			}
			else
				Toast.makeText(getBaseContext(), "Error joining the game", Toast.LENGTH_LONG).show();



		}




	};


	@Override
	protected void onDestroy(){
		super.onDestroy();

		//stopping update thread
		stopThreads();



	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//Quitting the game:
		if(isJoinedAGame){	
			MainActivity.server_com.quitGame();
			//clearing game variables
			MainActivity.currentGame=null;
			MainActivity.currentGameTeam1=null;
			MainActivity.currentGameTeam2=null;

		}


		//stopping update thread
		stopThreads();


	}
	@Override
	protected void onPause(){
		super.onPause();


	}
	@Override
	protected void onResume(){
		super.onResume();

		//statring the thread which pulls the server for change in game info
		updateThreads.add((updateGameInfo_Thread) new updateGameInfo_Thread().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR));

		MainActivity.currentGameTeam1=null;
		MainActivity.currentGameTeam2=null;
		updateLists();



	}
	public class updateGameInfo_Thread extends AsyncTask<Void, Void, String> {
		private String response;
		@Override
		protected String doInBackground(Void... arg0) {
			while(true){
				if(isCancelled())
					break;  
				getGameInfo();
				publishProgress();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return response;
		}




		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		@Override
		protected void onProgressUpdate(Void... v) {
			super.onProgressUpdate(v);
			updateLists();
		}

	}

	private void stopThreads(){
		for (updateGameInfo_Thread i : updateThreads){
			i.cancel(false);
		}
	}


}

