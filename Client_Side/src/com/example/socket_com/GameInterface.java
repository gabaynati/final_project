package com.example.socket_com;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import com.example.hs.R;
import com.example.socket_com.sendGameDataToServerActivity.MyClientTask_SendObject;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.TextView;

public class GameInterface extends Activity implements OnTouchListener, OnClickListener, Runnable {

	final static String TAG = "PAAR";
	private SurfaceHolder previewHolder;
	private Camera camera = null;
	private SurfaceView cameraPreview;
	private TextView current_bulletsText, total_bulletsText;
	private ProgressBar player_life;
	private ImageButton reload, target;
	private ImageView img;
	private boolean pressed = false;
	private int img_w,img_h;
	private AnimationDrawable animation;
	private boolean someAnimationRun;
	private Player player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);

		cameraPreview = (SurfaceView)findViewById(R.id.cameraPreview);
		previewHolder = cameraPreview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		img = (ImageView)findViewById(R.id.weaponView);
		reload = (ImageButton)findViewById(R.id.reload);
		target = (ImageButton)findViewById(R.id.target);
		player_life = (ProgressBar)findViewById(R.id.life_progress);
		total_bulletsText = (TextView)findViewById(R.id.bullets_total);
		current_bulletsText = (TextView)findViewById(R.id.bullets_condition);
		
		someAnimationRun = false;
		
		Weapon ak12 = new Ak12(getApplicationContext(), "Ak12", 150);
		Weapon[] wl = new Weapon[1];
		wl[0] = ak12;
		player = new Player(wl);

		player_life.setMax(player.getMaxLife());

		animation = (player.getWeaponds())[player.getCurrentWeapon()].stand();
		setAnimation(animation);
		img_w = img.getWidth();
		img_h = img.getHeight();

		setScreen();

		cameraPreview.setOnTouchListener(this);
		reload.setOnClickListener(this);
		target.setOnClickListener(this);
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


	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if(!someAnimationRun){
			
			switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				pressed = true;
				break;

			case MotionEvent.ACTION_UP:
				pressed = false;
				break;
			}

			int currentBullets = (player.getWeaponds()[player.getCurrentWeapon()]).getCurrentBullets();
			if(pressed & currentBullets > 0){
				(player.getWeaponds()[player.getCurrentWeapon()]).shoot();
				(player.getWeaponds()[player.getCurrentWeapon()]).setCurrentBullets();	
				setScreen();
			}
		}

		return pressed;
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

	private void setScreen(){		

		int currentBullets = (player.getWeaponds()[player.getCurrentWeapon()]).getCurrentBullets();
		int totalBullets = (player.getWeaponds()[player.getCurrentWeapon()]).getTotalBullets();
		if(currentBullets == 0 && totalBullets >0)
			reload();
		String c_B = String.valueOf(currentBullets);
		String t_B = String.valueOf(totalBullets);
		current_bulletsText.setText(c_B);
		total_bulletsText.setText("/" + t_B);
	}

	private void setAnimation(AnimationDrawable anim){

		if(anim != null){
			img.setImageDrawable(null);
			if(anim.isRunning())
				((AnimationDrawable)(img.getBackground())).stop();

			int imgSize;
			if((player.getWeaponds()[player.getCurrentWeapon()]).getTargetState()){
				imgSize = getMaxScreenSize();
				setImgSize(imgSize, imgSize);
			}

			else
				setImgSize(img_w, img_h);

			img.setBackgroundDrawable(anim);
			anim.start();
		}
	}

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

	private void setImgSize(int width, int height){

		img.setMaxWidth(width);
		img.setMaxHeight(height);
		img.setMinimumWidth(width);
		img.setMinimumHeight(height);
	}

	private void reload(){
/*
		AnimationDrawable anim = (player.getWeaponds()[player.getCurrentWeapon()]).reload();

		if(anim != null){

			if((player.getWeaponds()[player.getCurrentWeapon()]).getTargetState()){
				setImgSize(img_w, img_h);
				animation.stop();
				animation = (player.getWeaponds()[player.getCurrentWeapon()]).target();
			}

			else{
				animation.stop();
				animation = (player.getWeaponds()[player.getCurrentWeapon()]).stand();
			}
			executeAnimation(anim);
		}



		*/
		GamePacket packet=new GamePacket(MainActivity.nickName, MainActivity.password,true,false,"gili");
		MyClientTask_SendObject myClientTask = new MyClientTask_SendObject(packet);
		myClientTask.execute();
	}

	private void targetState(){

		AnimationDrawable anim;

		if((player.getWeaponds()[player.getCurrentWeapon()]).getTargetState()){
			anim = (player.getWeaponds()[player.getCurrentWeapon()]).normal();
			(player.getWeaponds()[player.getCurrentWeapon()]).setTargetState();
			animation.stop();
			animation = (player.getWeaponds()[player.getCurrentWeapon()]).stand();
			executeAnimation(anim);
		}

		else{
			anim = (player.getWeaponds()[player.getCurrentWeapon()]).target();
			(player.getWeaponds()[player.getCurrentWeapon()]).setTargetState();
			setAnimation(anim);
		}		
	}


	private int getMaxScreenSize(){

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		if(width < height)
			return width;
		else
			return height;
	}

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