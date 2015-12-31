package com.example.socket_com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import com.example.hs.R;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class GameInterface extends Activity implements OnTouchListener, OnClickListener, Runnable {

	final static String TAG = "PAAR";
	private SurfaceHolder previewHolder;
	private Camera camera = null;
	private SurfaceView cameraPreview;
	private TextView current_bulletsText, total_bulletsText, slesh;
	private ProgressBar player_life, bullets;
	private ImageButton reload, target;
	private ImageView img, sight_img, board_num1, board_num2;
	private boolean pressed = false;
	private int img_w,img_h;
	private AnimationDrawable animation;
	private Drawable sight;
	private boolean someAnimationRun;
	private Player player=MainActivity.player;
	private MyClientTask_ListenToPakcets listener;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);

		cameraPreview = (SurfaceView)findViewById(R.id.cameraPreview);
		previewHolder = cameraPreview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		img = (ImageView)findViewById(R.id.weaponView);
		sight_img = (ImageView)findViewById(R.id.sight);
		board_num1 = (ImageView)findViewById(R.id.board_num1);
		board_num2 = (ImageView)findViewById(R.id.board_num2);
		reload = (ImageButton)findViewById(R.id.reload);
		target = (ImageButton)findViewById(R.id.target);
		player_life = (ProgressBar)findViewById(R.id.life_progress);
		bullets = (ProgressBar)findViewById(R.id.bullets_progress);
		total_bulletsText = (TextView)findViewById(R.id.bullets_total);
		current_bulletsText = (TextView)findViewById(R.id.bullets_condition);
		slesh = (TextView)findViewById(R.id.slesh);
		
		someAnimationRun = false;

		Weapon ak12 = new Ak12(getApplicationContext(), "Ak12", 150);
		Weapon[] wl = new Weapon[1];
		wl[0] = ak12;
		player.setWeapons(wl);

		sight_img.setImageDrawable(player.getWeapons()[player.getCurrentWeapon()].getSight());
		player_life.setMax(player.getMaxLife());
		bullets.setMax(player.getWeapons()[player.getCurrentWeapon()].getTotalBullets());

		animation = (player.getWeapons())[player.getCurrentWeapon()].stand();
		setAnimation(animation);

		BitmapDrawable bd=(BitmapDrawable) this.getResources().getDrawable(R.drawable.img);
		img_h = bd.getBitmap().getHeight();
		img_w = bd.getBitmap().getWidth();

		setViewMargin(img, getScreenSize("width") - getScreenSize("width")/2, -1, -1, -1);

		setScreen();

		cameraPreview.setOnTouchListener(this);
		reload.setOnClickListener(this);
		target.setOnClickListener(this);
		
		
		
		//listener=new MyClientTask_ListenToPakcets();
		//listener.execute();
	}

	//set view margin by l - left, t - top, r - right, b - bottom
	private void setViewMargin(View view, int l, int t, int r, int b){

		LayoutParams params = (LayoutParams) view.getLayoutParams();

		if(l != -1)
			params.leftMargin = l;

		if(t != -1)
			params.topMargin = t;

		if(r != -1)
			params.rightMargin = r;

		if(b != -1)
			params.bottomMargin = b;

	}


	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		public void surfaceCreated(SurfaceHolder holder) {
			camera = Camera.open();
			try {
				camera.setPreviewDisplay(previewHolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {

			Camera.Parameters params = camera.getParameters();
			params.setPreviewSize(width, height);
			camera.setParameters(params);
			camera.startPreview();
		}


		public void surfaceDestroyed(SurfaceHolder holder) {
			camera.stopPreview();
			camera = null;
		}

	};


	//call whene the user touch the screen
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if(!someAnimationRun){

			switch (event.getAction()){
			//if the user still touch the screen
			case MotionEvent.ACTION_DOWN:
				pressed = true;
				break;

				//if the user left the screen
			case MotionEvent.ACTION_UP:
				pressed = false;
				break;
			}

			int currentBullets = (player.getWeapons()[player.getCurrentWeapon()]).getCurrentBullets();
			if(pressed & currentBullets > 0){
				AnimationDrawable anim = (player.getWeapons()[player.getCurrentWeapon()]).shoot();

				if(player.getWeapons()[player.getCurrentWeapon()].target_state)
					setAnimation(anim);

				else
					executeAnimation(anim);

				(player.getWeapons()[player.getCurrentWeapon()]).setCurrentBullets();
				bullets.setProgress((player.getWeapons()[player.getCurrentWeapon()]).getTotalBullets());
				setScreen();
			}
		}

		return false;
		//return pressed;
	}


	@Override
	public void onClick(View v) {

		switch(v.getId()) {

		case R.id.reload:

			reload();
			break;

		case R.id.target:

			targetState();
			break;
		}

	}

	//set the screen information - player life, bullets state etc
	private void setScreen(){		

		int currentBullets = (player.getWeapons()[player.getCurrentWeapon()]).getCurrentBullets();
		int totalBullets = (player.getWeapons()[player.getCurrentWeapon()]).getTotalBullets();
		if(currentBullets == 0 && totalBullets >0)
			reload();
		String c_B = String.valueOf(currentBullets);
		String t_B = String.valueOf(totalBullets);

		if(currentBullets >= 100){
			board_num1.setVisibility(View.GONE);
			board_num2.setVisibility(View.GONE);
		}

		else if(currentBullets < 100 && currentBullets >= 10){
			board_num1.setVisibility(View.VISIBLE);
			board_num2.setVisibility(View.GONE);
		}

		else{
			board_num1.setVisibility(View.VISIBLE);
			board_num2.setVisibility(View.VISIBLE);
		}

		if(totalBullets >= 100)
			slesh.setText("/");

		else if(totalBullets < 100 && totalBullets >= 10)
			slesh.setText("/  ");

		else
			slesh.setText("/   ");

		current_bulletsText.setText(c_B);
		total_bulletsText.setText(t_B);
	}

	//operate the animation anim on img
	private void setAnimation(AnimationDrawable anim){

		if(anim != null){
			img.setImageDrawable(null);
			if(anim.isRunning())
				((AnimationDrawable)(img.getBackground())).stop();

			int imgSize;
			if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState()){
				imgSize = getScreenSize("max");
				setImgSize(imgSize, imgSize);
				setViewMargin(img, (getScreenSize("width") - imgSize) / 2, -1, -1, -1);
			}

			else{
				setImgSize(img_w, img_h);
				setViewMargin(img, getScreenSize("width") - getScreenSize("width")/2, -1, -1, -1);
				sight_img.setVisibility(View.VISIBLE);
			}

			img.setBackgroundDrawable(anim);
			anim.start();
		}
	}

	//execute the animation anim once
	private void executeAnimation(AnimationDrawable anim){

		someAnimationRun = true;

		CustomAnimationDrawable CustomAnimation = new CustomAnimationDrawable(anim) {
			@Override
			void onAnimationFinish() {

				setScreen();
				animation.stop();
				setAnimation(animation);

				someAnimationRun = false;
			}
		};
		img.setBackgroundDrawable(CustomAnimation);
		CustomAnimation.start();
	}

	@Override
	public void run() {
		runOnUiThread(new Runnable(){
			public void run() {  	


			}
		});

	}

	//set the size of img to width, height
	private void setImgSize(int width, int height){

		img.setMaxWidth(width);
		img.setMaxHeight(height);
		img.setMinimumWidth(width);
		img.setMinimumHeight(height);
	}

	//reload the weapon
	private void reload(){

		AnimationDrawable anim = (player.getWeapons()[player.getCurrentWeapon()]).reload();

		if(anim != null){

			//if the weapon on target state
			if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState()){
				setImgSize(img_w, img_h);
				setViewMargin(img, getScreenSize("width") - getScreenSize("width")/2, -1, -1, -1);
				animation.stop();
				animation = (player.getWeapons()[player.getCurrentWeapon()]).target();
			}

			else{
				animation.stop();
				animation = (player.getWeapons()[player.getCurrentWeapon()]).stand();
			}

			sight_img.setVisibility(View.INVISIBLE);
			executeAnimation(anim);
		}




		GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(),true,false,"gili");
		MyClientTask_SendPakcet  myClientTask = new MyClientTask_SendPakcet(packet);
		myClientTask.execute(); 
	}

	//switch to target state and vice versa
	private void targetState(){

		AnimationDrawable anim;

		//if the weapon already on target state
		if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState()){
			anim = (player.getWeapons()[player.getCurrentWeapon()]).normal();
			(player.getWeapons()[player.getCurrentWeapon()]).setTargetState();
			animation.stop();
			animation = (player.getWeapons()[player.getCurrentWeapon()]).stand();
			executeAnimation(anim);
		}

		else{
			anim = (player.getWeapons()[player.getCurrentWeapon()]).target();
			(player.getWeapons()[player.getCurrentWeapon()]).setTargetState();
			sight_img.setVisibility(View.INVISIBLE);
			setAnimation(anim);
		}		
	}


	//get the size of the screen according to sizeOf
	private int getScreenSize(String sizeOf){

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		if(sizeOf.equals("width"))
			return width;

		else if(sizeOf.equals("height"))
			return height;

		else if(sizeOf.equals("max")){

			if(width < height)
				return width;
			else
				return height;
		}

		return 0;
	}










	public class MyClientTask_ListenToPakcets extends AsyncTask<Void, Void, Void> {


		String response = "";



		MyClientTask_ListenToPakcets(){


		}

		@Override
		protected Void doInBackground(Void... arg0) {

			GamePacket packet = null;


			//reading "packet" object from client
			try {
				ObjectInputStream inFromClient = new ObjectInputStream(MainActivity.socket.getInputStream());
				packet=(GamePacket) inFromClient.readObject();

			} 
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			}
			
			
			
			if(packet.isHit()){
				MainActivity.player.Hit();
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
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}












	public class MyClientTask_SendPakcet extends AsyncTask<Void, Void, Void> {


		String response = "";


		GamePacket packet;

		MyClientTask_SendPakcet(GamePacket packet){

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
				out.writeUTF("I am Client");
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
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}
}