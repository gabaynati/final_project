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
	
			String email=editTextEmail.getText().toString();
			String nickname=editTextNickName.getText().toString();
			String password=editTextPassword.getText().toString();
			Toast.makeText(getBaseContext(), "Please wait...", Toast.LENGTH_LONG).show();

			if( email.isEmpty()||nickname.isEmpty()||password.isEmpty()){
				textResponse.setText("You must fill all fields!");
				return;
			}
			GameDB gameDB=new GameDB();
			String res=gameDB.registerToDB(nickname, password, email);
			textResponse.setText(res);
		}};





		





}
