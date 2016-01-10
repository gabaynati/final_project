package com.example.socket_com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.hs.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class registerActivity extends Activity {
	TextView textResponse;
	EditText editTextEmail, editTextNickName,editTextPassword; 
	Button buttonRegister;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_activity);


		buttonRegister = (Button)findViewById(R.id.register);
		textResponse = (TextView)findViewById(R.id.response);
		editTextNickName=(EditText)findViewById(R.id.nickname);
		editTextPassword=(EditText)findViewById(R.id.password);
		editTextEmail=(EditText)findViewById(R.id.email);
		buttonRegister.setOnClickListener(buttonRegisterOnClickListener);

	}
	//connect button onClick method
	OnClickListener buttonRegisterOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			AlertDialog alertDialog = new AlertDialog.Builder(registerActivity.this).create();
			alertDialog.setTitle("Alert");

			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});



			String email=editTextEmail.getText().toString();
			String nickname=editTextNickName.getText().toString();
			String password=editTextPassword.getText().toString();
			if( email.isEmpty()||nickname.isEmpty()||password.isEmpty()){
				textResponse.setText("You must fill all three fields!");
				return;
			}
			registerToDBThread thread=new registerToDBThread(email, nickname, password);
			thread.execute();

		}};





		public class registerToDBThread extends AsyncTask<Void, Void, Void> {

			String email;
			String nickname;
			String password;
			String res;



			public registerToDBThread(String email,String nickname,String password){
				this.email = email;
				this.nickname = nickname;
				this.password=password;

			}

			@Override
			protected Void doInBackground(Void... arg0) {
				String dbMessage="";
				dbMessage=GameDB.addPlayer(nickname, password, email);
				if(dbMessage.equals("success")){
					res="Registration has completed successfully!\n You are now redirected to H&S Menu";
					//going back to menu
					finish();
				}
				else if(dbMessage.equals("exists"))
					res="User name is already taken\n Please choose another one";
				else
					res="there was an error: "+dbMessage;

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				textResponse.setText(res);
				super.onPostExecute(result);
			}

		}









}
