package com.example.socket_com;

import com.example.hs.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGameActivity extends Activity {
	TextView textResponse;
	EditText editTextGameName, editTextNumOfPlayers; 
	Button buttonCreateGame;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_game_layout);


		buttonCreateGame = (Button)findViewById(R.id.createGame);
		textResponse = (TextView)findViewById(R.id.response);
		editTextGameName=(EditText)findViewById(R.id.gameName);
		editTextNumOfPlayers=(EditText)findViewById(R.id.numOfPlayers);
		buttonCreateGame.setOnClickListener(buttonCreateGameOnClickListener);

	}
	//connect button onClick method
	OnClickListener buttonCreateGameOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
	
			String gameName=editTextGameName.getText().toString();
			String NumOfPlayers=editTextNumOfPlayers.getText().toString();
			Toast.makeText(getBaseContext(), "Please wait...", Toast.LENGTH_LONG).show();

			//checking the fields
			if( gameName.isEmpty()||NumOfPlayers.isEmpty()){
				textResponse.setText("You must fill all fields!");
				return;
			}
			if(!isInteger(NumOfPlayers, 10)){
				textResponse.setText("The number of players is not integer!");
				return;
			}
				
			String res=MainActivity.server_com.createNewGame(gameName,Integer.parseInt(NumOfPlayers),MainActivity.loc);
			try {
				MainActivity.getGameListSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!MainActivity.getGameListSem.isTimedOut()){
				//MainActivity.gameList.add(gameName);
				textResponse.setText("Game Created successfully");
				}
			else
			textResponse.setText("ERROR while creating new game!");

		}};
		
		private boolean isInteger(String s, int radix) {
		    if(s.isEmpty()) return false;
		    for(int i = 0; i < s.length(); i++) {
		        if(i == 0 && s.charAt(i) == '-') {
		            if(s.length() == 1) return false;
		            else continue;
		        }
		        if(Character.digit(s.charAt(i),radix) < 0) return false;
		    }
		    return true;
		}

}
