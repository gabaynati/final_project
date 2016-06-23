package com.example.socket_com;

import java.util.Vector;

import com.example.hs.R;
import com.example.hs.R.id;
import com.example.hs.R.layout;
import com.example.hs.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Testing_Activity extends Activity {
	ListView testsListView;
	TextView txtResponse;
	int sentPackets=0;

	Vector<String> test_list=new Vector<String>();



	public enum tests{
		stressTestOnServer,serverResponseTimeTest,stressTestOnDB,DBServerResponseTimeTest,correctEventsSynchronizationTest
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing_);
		testsListView=(ListView)findViewById(R.id.testsListView);
		txtResponse = (TextView)findViewById(R.id.txtRspn);


		if(!MainActivity.isConnected)
			txtResponse.setText("You must First Log in!");
		else
			txtResponse.setText("You have logged in-run tests");



		test_list.add("stressTestOnServer");
		test_list.add("serverResponseTimeTest");
		test_list.add("stressTestOnDB");
		test_list.add("DBServerResponseTimeTest");
		test_list.add("correctEventsSynchronizationTest");


		//setting list adapter
		CustomListAdapter adapter=new CustomListAdapter(this, R.layout.listview_item_row,test_list);
		testsListView.setAdapter(adapter);
		testsListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,long arg3) 
			{
				if(!MainActivity.isConnected){
					txtResponse.setText("You are not logged in!");
					return;

				}
				txtResponse.setText("Please wait...");
				String res="";
				String test_name = (String)adapter.getItemAtPosition(position);
				tests test=tests.valueOf(test_name);
				switch(test){
				case serverResponseTimeTest:
					res=serverResponseTimeTest();
					txtResponse.setText(res);
					break;
				case stressTestOnServer:
					res=stressTestOnServer();
					txtResponse.setText(res);
					break;

				case stressTestOnDB:
					res=stressTestOnDB();
					txtResponse.setText(res);
					break;
				case DBServerResponseTimeTest:
					res=DBServerResponseTimeTest();
					txtResponse.setText(res);
					break;
				case correctEventsSynchronizationTest:
					res=correctEventsSynchronizationTest();
					txtResponse.setText(res);
					break;
				
				}

			}
		});

	}








	/********Testing functions**********/
	//this function will perform stress check on the server by sending many test packets
	public String stressTestOnServer(){
		String str="";
		int numberOfPackets=400;
		int succededPackets=0;
		sentPackets=0;



		long start=System.currentTimeMillis();

		//sending packets
		for(int i=0;i<numberOfPackets;i++){
			MainActivity.server_com.sendTestPacket();
			try {
				MainActivity.testSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!MainActivity.testSem.isTimedOut())
				succededPackets++;
			sentPackets++;

			txtResponse.setText("number of packets sent: "+sentPackets);
			txtResponse.invalidate();


		}



		str="test packets sent: "+numberOfPackets+"  ,packets success: " +succededPackets +"\n"+"time in milliseconds: "+(System.currentTimeMillis()-start);

		return str;
	}



	//this function will measure response time of a packet exchange between server and client
	public String serverResponseTimeTest(){
		String str="";


		long start=System.currentTimeMillis();

		//sending packet

		MainActivity.server_com.sendTestPacket();
		try {
			MainActivity.testSem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(MainActivity.testSem.isTimedOut())
			str="error while sending test packet to server";
		else
			str="test packets sent succesfully, time in milliseconds: "+(System.currentTimeMillis()-start);

		return str;
	}


	//this function will perform stress check on the DB server by sending many queries.
	public String stressTestOnDB(){
		String str="";


		long start=System.currentTimeMillis();

		//sending packet

		int number_of_queries=10; 
		int success_attempts=0;
		int res;
		for(int i=0;i<number_of_queries;i++){
			res=GameDB.isExists("nati", "1234");
			if(res!=GameDB.DBERROR)
				success_attempts++;

		}

		str="test queries sent: "+number_of_queries+"  ,queries success: " +success_attempts +"\n"+"time in milliseconds: "+(System.currentTimeMillis()-start);

		return str;
	}

	//this function will measure response time of a execution of an query
	public String DBServerResponseTimeTest(){
		String str="";


		long start=System.currentTimeMillis();

		//sending packet


		int res;

		res=GameDB.isExists("nati", "1234");
		if(res!=GameDB.DBERROR)
			str="test queries sent succesfully!"+"\n"+ "time in milliseconds: "+(System.currentTimeMillis()-start);
		else
			str="error communicating with DB server";


		return str;
	}
	

	
	//this function will test a complete game events for checking correct synchronization of the events between the server and client
	public String correctEventsSynchronizationTest(){
		String str="All Events succesfully synchronaized!";
		
//		
//		
//		
//		//joining a game
//		MainActivity.server_com.JoinGame("game 2", 1);
//		try {
//			MainActivity.joinGameSem.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(MainActivity.joinGameSem.isTimedOut())
//			str="error at joining a game";
//		MainActivity.currentGame="game 2";
//		
//		
//		//quitting game
//		MainActivity.server_com.quitGame();
//		try {
//			MainActivity..acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(MainActivity.testSem.isTimedOut())
//			str="error at qutting game";
//		
//		
//		
//		
//		//logging out
//		MainActivity.server_com.sendTestPacket();
//		try {
//			MainActivity.testSem.acquire();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(MainActivity.testSem.isTimedOut())
//			str="error at logging out";
//		
		
		
	
		
		
		
		
		
		return str;
	}
















	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.testing_, menu);
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
