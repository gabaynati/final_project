package com.example.socket_com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.hs.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



	
	public class ConnectToServerActivity extends Activity {

		TextView textResponse;
		EditText editTextAddress, editTextPort,editTextNickName,editTextPassword; 
		Button buttonConnect;
		


		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);




			setContentView(R.layout.activity_main);

			editTextAddress = (EditText)findViewById(R.id.address);
			editTextPort = (EditText)findViewById(R.id.port);
			buttonConnect = (Button)findViewById(R.id.connect);
			textResponse = (TextView)findViewById(R.id.response);
			editTextNickName=(EditText)findViewById(R.id.nickname);
			editTextPassword=(EditText)findViewById(R.id.password);

			buttonConnect.setOnClickListener(buttonConnectOnClickListener);



		}






		//connect button onClick method
		OnClickListener buttonConnectOnClickListener = new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String address=editTextAddress.getText().toString();
				int port=Integer.parseInt(editTextPort.getText().toString());
				String nickname=editTextNickName.getText().toString();
				String password=editTextPassword.getText().toString();
				MyClientTask_Connect myClientTask = new MyClientTask_Connect(address,port);
				myClientTask.execute();
			}};











			public class MyClientTask_Connect extends AsyncTask<Void, Void, Void> {

				String dstAddress;
				int dstPort;
				String response = "";




				public MyClientTask_Connect(String addr, int port){
					this.dstAddress = addr;
					this.dstPort = port;

				}

				@Override
				protected Void doInBackground(Void... arg0) {

					try {
						MainActivity.socket = new Socket(dstAddress, dstPort);

						//reading hello
						ByteArrayOutputStream byteArrayOutputStream = 
								new ByteArrayOutputStream(1024);
						byte[] buffer = new byte[1024];

						int bytesRead;
						InputStream inputStream = MainActivity.socket.getInputStream();

						//						
						//						  notice:

						//						 inputStream.read() will block if no data return
						//						 
						while ((bytesRead = inputStream.read(buffer)) != -1){
							response += byteArrayOutputStream.toString("UTF-8");
						}

					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						response = "UnknownHostException: " + e.toString();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						response = "IOException: " + e.toString();
					}
					/*finally{
						if(socket != null){
							try {
								socket.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
					 */
					Intent gameEngine = new Intent("com.example.socket_com.GAMEINTERFACE");
					startActivity(gameEngine);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					textResponse.setText(response);
					super.onPostExecute(result);
				}

			}





	}

