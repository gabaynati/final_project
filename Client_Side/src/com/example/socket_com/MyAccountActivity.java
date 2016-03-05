package com.example.socket_com;

import java.util.Vector;

import com.example.hs.R;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

public class MyAccountActivity extends Activity {
	TextView TxtResponse,TxtViewNickname,TxtViewEmail,TxtViewRank,TxtViewTotalScore;
	Vector<Object> info=null;;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		
		TxtResponse=(TextView)findViewById(R.id.txtResponse);
		TxtViewNickname = (TextView)findViewById(R.id.nickname);
		TxtViewEmail = (TextView)findViewById(R.id.email);
		TxtViewRank=(TextView)findViewById(R.id.rank);
		TxtViewTotalScore=(TextView)findViewById(R.id.totalScore);
		

		info= GameDB.getPlayerInfo(MainActivity.player.getNickName());
	
		if(info==null){
			Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
			TxtResponse.setText("Error while getting info from DB");
			return;
		}
		
		
		
		TxtViewNickname.setText(String.valueOf("User Name: "+info.elementAt(0)));
		TxtViewEmail.setText(String.valueOf("Email Address: "+info.elementAt(2)));
		String rank="";
		switch((Integer)info.elementAt(3)){
		case 0:
			rank="Private";
			break;
		case 1:
			rank="Private 2";
			break;
		case 2:
			rank="Private First Class";
			break;
		case 3:
			rank="Specialist";
			break;
		case 4:
			rank="Corporal";
			break;
		case 5:
			rank="Sergent";
			break;
		case 6:
			rank="Staff sergent";
			break;
			
		}
		TxtViewRank.setText("Rank status:"+rank);

		TxtViewTotalScore.setText("Total Score :"+(Integer)info.elementAt(4));
	}
	@Override
	public void onResume(){
		super.onResume();

	}
	
	
}
