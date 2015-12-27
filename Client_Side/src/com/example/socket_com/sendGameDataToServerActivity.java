package com.example.socket_com;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

import com.example.hs.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class sendGameDataToServerActivity extends Activity {
	TextView textResponse;
	EditText editTextNickName,editTextPassword; 
	Button buttonSendData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	
		buttonSendData = (Button)findViewById(R.id.sendData);
		textResponse = (TextView)findViewById(R.id.response);
		editTextNickName=(EditText)findViewById(R.id.nickname);
		editTextPassword=(EditText)findViewById(R.id.password);


		buttonSendData.setOnClickListener(buttonSendDataOnClickListener);


	}





	//send data button onClick method
	OnClickListener buttonSendDataOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			String nickName=editTextNickName.getText().toString();
			String password=editTextPassword.getText().toString();
			//GamePacket packet=new GamePacket(nickName, password);
		//	MyClientTask_SendObject myClientTask = new MyClientTask_SendObject(packet);
		//	myClientTask.execute();
			//textResponse.setText("");
		}

	};











	public class MyClientTask_SendObject extends AsyncTask<Void, Void, Void> {


		String response = "";


		GamePacket packet;

		MyClientTask_SendObject(GamePacket packet){

			this.packet=packet;
		}

		@Override
		protected Void doInBackground(Void... arg0) {


			try {

				//writing object
				ObjectOutputStream outToServer = new ObjectOutputStream(MainActivity.socket.getOutputStream());
				outToServer.writeObject(packet);
				/*
				//writing texts
				DataOutputStream out = new DataOutputStream(MainActivity.socket.getOutputStream());
				out.writeUTF("I am Cleint");
				 */

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			}
			/*finally
			{
				if(MainActivity.socket != null){
					try {
						MainActivity.socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}*/
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			textResponse.setText(response);
			super.onPostExecute(result);
		}

	}
}
