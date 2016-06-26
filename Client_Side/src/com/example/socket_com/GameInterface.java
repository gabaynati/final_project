/*
 * GameInterface - Activity
 * 
 * this activity is on foreground while the game occur
 */

package com.example.socket_com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import android.content.Intent;
import android.content.IntentFilter;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

import com.example.hs.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

import org.opencv.android.*;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class GameInterface extends Activity implements OnTouchListener, OnClickListener, CvCameraViewListener2 {

	private final int ramBorden = 5;
	private int total_score=20;
	private Context context;
	private FrameLayout frame;
	private TextView current_bulletsText, total_bulletsText, slesh, score_lbl;
	private ProgressBar player_life;
	private ImageButton reload, target, shoot;
	private ImageView img, bullet_hit, sight_img, board_num1, board_num2;
	private int soundIndex, writer, reader, anim_index, shootingTime, nSpace, state;
	private float x1, x2;
	private AnimationDrawable shoot_animation, stand_animation;
	private boolean someAnimationRun, someAnimationLoad, touched;
	private Player player=MainActivity.player;
	private Handler AnimationHandler, DrawableHandler, changeAnimation, shootHandler, checkForHitHandler;
	private int[] drawableResources, sounds_frames;
	private Bitmap[] segment_animation;
	private Semaphore semaphore, process_semaphore;
	public static final int UPPER_BODY_HIT_SCORE=50,FACE_HIT_SCORE=100,LOWER_BODY_HIT_SCORE=20;

	/**********************************OpenCV configurations***************************************/

	//OpenCV Object for handling the camera
	private JavaCameraView mOpenCvCameraView;
	protected static final String TAG = null;
	private Mat mGray,mRgba;


	//human detectors - haar cascade algorithm
	private final String face_xml_res = "lbpcascade_frontalface";
	private final String lowerBody_xml_res = "haarcascade_lowerbody";
	private final String upperBody_xml_res = "haarcascade_upperbody";


	//files for face detection
	private File                   faceCascadeFile, uBodyCascadeFile, lBodyCascadeFile;
	private CascadeClassifier      faceDetector, uBodyDetector, lBodyDetector;
	private float                  mRelativeDetectorSize_lower   = 0.1f;
	private float                  mRelativeDetectorSize_upperBody   = 0.1f;
	private int                    mAbsoluteDetectorSize_lower   = 0;
	private int                    mAbsoluteDetectorSize_upperBody   = 0;
	private static final Scalar    RECT_COLOR     = new Scalar(0, 255, 0, 255);
	private Rect[]                 facesArray, facesArrayWhileShoot;
	private Rect[]                 upperBodyArray, upperBodyArrayWhileShoot;
	private Rect[]                 lowerBodyArray, lowerBodyArrayWhileShoot;


	//colors detector
	private Mat                               mHsv;
	private ColorBlobDetector                 mDetector;
	private Mat                               mSpectrum;
	private Size                              SPECTRUM_SIZE;
	private Scalar                            CONTOUR_COLOR;
	private String[]                          players;
	private HashMap<String,List<MatOfPoint>>  colorsFounds;

	/****************************************************************************************************/




	//called when the activity created at first time
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		context = this.getApplicationContext();


		/******************OpenCV***********************/
		//binding the OpenCV camera object to the layout
		mOpenCvCameraView=(JavaCameraView)findViewById(R.id.cameraPreview);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		//telling the OpenCV camera object which listener object we are using for the camera.
		//because we are implementing a camera listener object ,we are using "this" as argument
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.setOnTouchListener(this);
		/***********************************************/


		/**************************Layout components*****************************/
		frame = (FrameLayout)findViewById(R.id.game_layout);
		img = (ImageView)findViewById(R.id.weaponView);
		sight_img = (ImageView)findViewById(R.id.sight);
		board_num1 = (ImageView)findViewById(R.id.board_num1);
		board_num2 = (ImageView)findViewById(R.id.board_num2);
		reload = (ImageButton)findViewById(R.id.reload);
		target = (ImageButton)findViewById(R.id.target);
		shoot = (ImageButton)findViewById(R.id.shoot);
		total_bulletsText = (TextView)findViewById(R.id.bullets_total);
		current_bulletsText = (TextView)findViewById(R.id.bullets_condition);
		slesh = (TextView)findViewById(R.id.slesh);
		/************************************************************************/


		MainActivity.logic = new Logic(mOpenCvCameraView);

		someAnimationRun = false;
		touched = false;

		score_lbl=(TextView)findViewById(R.id.score_lbl);
		score_lbl.setText("Score: " + total_score);

		AnimationHandler = new Handler();
		DrawableHandler = new Handler();
		changeAnimation = new Handler();
		checkForHitHandler = new Handler();

		semaphore = new Semaphore(1);
		process_semaphore = new Semaphore(1);


		Weapon mp412 = new Mp412(getApplicationContext(), "mp412", 60);
		Weapon srr61 = new Srr61(getApplicationContext(), "srr61", 60);
		Weapon[] wl = new Weapon[2];
		wl[0] = mp412;
		wl[1] = srr61;
		player.setWeapons(wl);
		shootingTime = (player.getWeapons()[player.getCurrentWeapon()]).shootingTime();


		shoot_animation = (player.getWeapons()[player.getCurrent_weapon()].getAnimation("shoot"));
		executeAnimation(player.getWeapons()[player.getCurrent_weapon()].getAnimation("choose"));

		reload.setOnClickListener(this);
		target.setOnClickListener(this);
		shoot.setOnTouchListener(this);


		

		//*************service for listening to hits*****************//*
		//starting the service
		Intent msgIntent = new Intent(this, HitService.class);
		startService(msgIntent);
		startService(msgIntent);
		startService(msgIntent);

		//creating a listener to the service 
		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		ResponseReceiver reponse = new ResponseReceiver();
		registerReceiver(reponse, filter);
		//***********************************************************//*

		 



		/*********************players colors*****************/


		Toast.makeText(getBaseContext(), MainActivity.currentGamePlayersColors.toString(), Toast.LENGTH_LONG).show();

		colorsFounds = new HashMap<String,List<MatOfPoint>>();
		players = new String[MainActivity.currentGamePlayersColors.size()];

		Iterator<Map.Entry<String,RGB>> it = MainActivity.currentGamePlayersColors.entrySet().iterator();

		int i = 0;
		while (it.hasNext()) {
			Map.Entry<String,RGB> entry = (Map.Entry<String,RGB>)it.next();
			players[i] = (String) entry.getKey();
			i++;
		}
		//**************************************************//



	}

	/*****************************************************************/

	//the following object is a listener object that keeps track to the binding process between the activity to the OpenCV service.
	private BaseLoaderCallback mLoaderCallback=new BaseLoaderCallback(this) {

		//this callback is called when the OpenCV service is successfully binded to the activity.
		@Override
		public void onManagerConnected(int status){
			switch(status){

			//if the binded is succeeded we will we enable the camera
			case LoaderCallbackInterface.SUCCESS:
			{
				Log.i(TAG, "OpenCV loaded successfully");
				mOpenCvCameraView.enableView();

				initialDetector(lowerBody_xml_res);
				initialDetector(upperBody_xml_res);	
				//initialDetector(lowerBody_xml_res);

				break;
			}
			//if the binding failed
			default:
			{
				super.onManagerConnected(status);
			}
			}
		}

		//initialize the java detector
		private void initialDetector(String xmlRes){

			File CascadeFile;
			CascadeClassifier JavaDetector;

			try {

				// load cascade file from application resources
				int resId = getResources().getIdentifier(xmlRes, "raw", "com.example.hs");
				InputStream is = getResources().openRawResource(resId);
				File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
				CascadeFile = new File(cascadeDir, xmlRes + ".xml");
				FileOutputStream os = new FileOutputStream(CascadeFile);

				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}

				is.close();
				os.close();

				JavaDetector = new CascadeClassifier(CascadeFile.getAbsolutePath());
				if (JavaDetector.empty()) {
					Log.e(TAG, "Failed to load cascade classifier");
					JavaDetector = null;
				} else
					Log.i(TAG, "Loaded cascade classifier from " + CascadeFile.getAbsolutePath());
				cascadeDir.delete();

				if(xmlRes.equals(lowerBody_xml_res)){
					lBodyCascadeFile = CascadeFile;
					lBodyDetector = JavaDetector;
				}

				else if(xmlRes.equals(upperBody_xml_res)){
					uBodyCascadeFile = CascadeFile;
					uBodyDetector = JavaDetector;
				}
				/*
				else if(xmlRes.equals(lowerBody_xml_res)){
					lBodyCascadeFile = CascadeFile;
					lBodyDetector = JavaDetector;
				}

				 */
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
			}
		}

	};

	/*****************************************************************/

	//the activity must connect to the opencv service at the onResume callback.
	//onResume is called right after it is started but before it is available to the user.
	@Override
	public void onResume(){
		super.onResume();
		player_life = (ProgressBar)findViewById(R.id.life_progress);
		player_life.setMax(player.getMaxLife());
		player_life.setProgress(player.getLife());

		//running hit event listener:
		//hitThreads.add((HitListener_Thread) new HitListener_Thread().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR));

		//the following call binds the activity with the opencv service.
		//the third argument is a listener object that keeps track to the binding process.
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,this , mLoaderCallback);
	}
	/*****************************************************************/

	//we are overriding the onDestroy callback of the activity ,because we want to disable the camera when the activity is destroyed.
	public void exitGameSettings(){
		if (mOpenCvCameraView!=null)
			mOpenCvCameraView.disableView();

		//quitting game
		if(MainActivity.isConnected){
			//quitting game at server
			MainActivity.server_com.quitGame();
			//saving total score at DB
			GameDB.updateScoreInDB(player.getNickName(), total_score);

		}
	}
	/*****************************************************************/

	@Override
	public void onDestroy(){
		super.onDestroy();
		exitGameSettings();

	}
	/*****************************************************************/

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		exitGameSettings();
		finish();
	}
	/*****************************************************************/

	//called when the activity goes to background
	@Override
	public void onPause(){
		super.onPause();
		if (mOpenCvCameraView!=null)
			mOpenCvCameraView.disableView();
	}

	/******************************CvCameraViewListener2 methods*******************************/

	//called when the openCV camera component is started
	@Override
	public void onCameraViewStarted(int width, int height) {

		//Initializing the intermediate Mat object which is in use for frame processing
		mGray = new Mat();
		mRgba = new Mat();

		/***********colors detector components****************/
		mHsv = new Mat(height, width, CvType.CV_8UC4);
		mDetector = new ColorBlobDetector();
		mSpectrum = new Mat();
		SPECTRUM_SIZE = new Size(200, 64);
		CONTOUR_COLOR = new Scalar(0,255,0,255);
		/*****************************************************/

		MainActivity.logic.setMats(mGray, mRgba);
	}
	/*****************************************************************/

	//called when the openCV camera component is stopped
	@Override
	public void onCameraViewStopped() {

		//releasing Mats object
		mGray.release();
		mRgba.release();
		mHsv.release();
	}



	//this method is called when the camera delivers a frame.
	//before this callback is called , the data from the camera is ripped out to a CvCameraViewFrame object, which 
	//is passed into this callback as an argument called inputFrame.

	/*****************************************************************/

	//an object of CvCameraViewFrame is made of two OpenCV Mat Objects.
	//a Mat Object is just a 2d array containing the data of the frame in two color space : RGB or gray
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

		mRgba=inputFrame.rgba();
		mGray=inputFrame.gray();

		MainActivity.logic.setMats(mGray, mRgba);

		//on shooting time
		if(touched){

			//clear all colors from the map (not relevant contours)
			//colorsFounds.clear();
			
			//for each player search is specific color
			for(int i = 0; i < players.length; i++){

				RGB rgb = MainActivity.currentGamePlayersColors.get(players[i]);
				Scalar hsv = new Scalar(rgb.getRed(), rgb.getGreen(), rgb.getBlue());

				mDetector.setHsvColor(hsv);

				//search the color on the input frame
				mDetector.process(mRgba);
				List<MatOfPoint> contours = mDetector.getContours();

			 //*************draw the contours on the screen - test only**********//
				//Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

				//if color found add it to the map
				if(!contours.isEmpty())
					colorsFounds.put(players[i], contours);
			}
			  

			if (mAbsoluteDetectorSize_lower == 0) {
				int height = mGray.rows();
				if (Math.round(height * mRelativeDetectorSize_lower) > 0) {
					mAbsoluteDetectorSize_lower = Math.round(height * mRelativeDetectorSize_lower);
				}
			}

			if (mAbsoluteDetectorSize_upperBody == 0) {
				int height = mGray.rows();
				if (Math.round(height * mRelativeDetectorSize_upperBody) > 0) {
					mAbsoluteDetectorSize_upperBody = Math.round(height * mRelativeDetectorSize_upperBody);
				}
			}


			//allocate new catch mats for the human detectors
			MatOfRect lowerBodies = new MatOfRect();
			MatOfRect upperBodies = new MatOfRect();
			//MatOfRect lowerBodies = new MatOfRect();

			System.gc();


			//running face detecting on the frame:
			if (lBodyDetector != null)
				//this function takes in a gray scale image and returns rectangles
				//that bound the faces (if any).
				lBodyDetector.detectMultiScale(mGray, lowerBodies, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
						new Size(mAbsoluteDetectorSize_lower, mAbsoluteDetectorSize_lower), new Size());

			//running upper body detecting on the frame:
			if (uBodyDetector != null)
				//this function takes in a gray scale image and returns rectangles
				//that bound the upper body (if any).
				uBodyDetector.detectMultiScale(mGray, upperBodies, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
						new Size(mAbsoluteDetectorSize_upperBody, mAbsoluteDetectorSize_upperBody), new Size());

			/*
			//running lower body detecting on the frame:
			if (lBodyDetector != null)
				//this function takes in a gray scale image and returns rectangles
				//that bound the lower body (if any).
				lBodyDetector.detectMultiScale(mGray, lowerBodies, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
						new Size(mAbsoluteDetectorSize_upperBody, mAbsoluteDetectorSize_upperBody), new Size());

			 */


			//get array of points
			lowerBodyArray = lowerBodies.toArray();

			/*******************************drawing rectangles - test only***********************************/
			//for (int i = 0; i < lowerBodyArray.length; i++)
			//Imgproc.rectangle(mRgba, lowerBodyArray[i].tl(), lowerBodyArray[i].br(), RECT_COLOR, 3);
			/************************************************************************************************/

			//get array of points
			upperBodyArray = upperBodies.toArray();

			/*******************************drawing rectangles - test only***********************************/
			//for (int i = 0; i < upperBodyArray.length; i++)
			//Imgproc.rectangle(mRgba, upperBodyArray[i].tl(), upperBodyArray[i].br(), RECT_COLOR, 3);
			/************************************************************************************************/

			//get array of points
			//lowerBodyArray = upperBodies.toArray();

			/*******************************drawing rectangles - test only***********************************/
			//for (int i = 0; i < lowerBodyArray.length; i++)
			//Imgproc.rectangle(mRgba, lowerBodyArray[i].tl(), lowerBodyArray[i].br(), RECT_COLOR, 3);
			/************************************************************************************************/




			//if this player hits someone - hitArea = area index
			Thread thread = new Thread() {
				@Override
				public void run() {

					//MainActivity.logic.isHit(facesArray, upperBodyArray, lowerBodyArray, colorsFounds);

					touched = false;

				}

			};

			thread.start();
		}

		return mRgba;
	}

	////############################################///
	/*****************************************************************/

	//call when the user touch the screen, implementation of OnTouchListener interface
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		//if the player press on the screen
		if(v.getId() == mOpenCvCameraView.getId()){

			switch (event.getAction()){

			//when press on the screen
			case MotionEvent.ACTION_DOWN: 
			{
				x1 = event.getX();

				break;
			}

			//when release the screen
			case MotionEvent.ACTION_UP: 
			{
				if(!someAnimationRun){

					x2 = event.getX();

					//Left to Right Swap Performed
					if (x1 < x2){

						someAnimationLoad = true;
						someAnimationRun = true;
						shoot_animation = null;
						System.gc();

						if((player.getWeapons()[player.getCurrent_weapon()]).target_state)
							sight_img.setVisibility(View.VISIBLE);

						executeReplaceAnimation(player.getWeapons()[player.getCurrent_weapon()].getAnimation("drop"));

						player.chengeCurrentWeapon(-1);
						shootingTime = (player.getWeapons()[player.getCurrentWeapon()]).shootingTime();
					}


					//Right to Left Swap Performed
					if (x1 > x2){

						someAnimationLoad = true;
						someAnimationRun = true;
						shoot_animation = null;
						System.gc();

						if((player.getWeapons()[player.getCurrent_weapon()]).target_state)
							sight_img.setVisibility(View.VISIBLE);

						executeReplaceAnimation(player.getWeapons()[player.getCurrent_weapon()].getAnimation("drop"));

						player.chengeCurrentWeapon(1);
						shootingTime = (player.getWeapons()[player.getCurrentWeapon()]).shootingTime();
					}

					break;

				}
			}
			}
		}

		//if the user press on the shooting button
		else if(v.getId() == shoot.getId()){

			switch (event.getAction()){

			//when press on the button
			case MotionEvent.ACTION_DOWN: 
			{
				if(!someAnimationRun && !someAnimationLoad){

					shootHandler = new Handler();
					shootHandler.postDelayed(shootAction, 0);
				}
				break;
			}

			//when release the button 
			case MotionEvent.ACTION_UP: 
			{
				if (shootHandler == null)
					return true;
				shootHandler.removeCallbacks(shootAction);
				shootHandler = null;

				break;
			}

			}
		}


		return true;
	}
	/*****************************************************************/

	//continue shooting while the shooting button is pressing according to the current weapon shooting time  
	private Runnable shootAction = new Runnable() {
		@Override
		public void run() {

			touched = true;
			shoot();
			shootHandler.postDelayed(this, shootingTime);
		}
	};
	/*****************************************************************/

	//call this method when the user shoot
	private void shoot(){


		int currentBullets = (player.getWeapons()[player.getCurrentWeapon()]).getCurrentBullets();
		if(currentBullets > 0){

			someAnimationRun = true;
			executeAnimation(shoot_animation);
			player.getWeapons()[player.getCurrentWeapon()].shoot();


			checkForHitHandler.postDelayed(hitCheck, 100);						

			MainActivity.server_com.sendHitToServer(hitArea,name);

		}

		//update screen
		setScreen();

	}
	/*****************************************************************/

	//runnable to check if the player hit somebody
	private Runnable hitCheck = new Runnable() {
		@Override
		public void run() {

			//busy wait - wait until all detection process are done  
			while(touched);

			//get the current hit area and injured player
			int hitArea = MainActivity.logic.getArea();
			String injured = MainActivity.logic.getInjured();

			//hit detected
			if(hitArea != -1){

				drawHit();

				switch(hitArea){
				case Logic.UPPER_BODY_HIT:
					total_score+=UPPER_BODY_HIT_SCORE;
					break;
				case Logic.FACE_HIT:
					total_score+=FACE_HIT_SCORE;
					break;
				case Logic.LOWER_BODY_HIT:
					total_score+=LOWER_BODY_HIT_SCORE;
					break;
				}
				//updating the score:
				score_lbl.setText("Score: " + total_score);

				//player detected
				if(!(injured.equals(""))){
					Toast toast = Toast.makeText(getApplicationContext(), "HIT: " + injured, 1000);
					toast.show();

					//send data to server
					MainActivity.server_com.sendHitToServer(hitArea, injured);
				}
			}
		}
	};

	//drawing bullet hole when there is a hit
	private void drawHit(){

		Timer timer = new Timer(200) {

			public void onFinish() {
				// TODO Auto-generated method stub
				frame.removeView(bullet_hit);
			}


		};

		int size = MainActivity.logic.getSizeOfRect();
		bullet_hit = new ImageView(getApplicationContext());
		bullet_hit.setImageResource(R.drawable.bullet_hit);
		bullet_hit.setX(mOpenCvCameraView.getWidth()/2 - bullet_hit.getWidth()/2);
		bullet_hit.setY(mOpenCvCameraView.getHeight()/2 - bullet_hit.getHeight()/2);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size/10, size/10);


		bullet_hit.setLayoutParams(params);
		frame.addView(bullet_hit);


		timer.start();
	}

	/*****************************************************************/

	public void hitRecvied(){
		//player is dead
		if(player.getLife()<=0){
			player_life.setProgress(0);
			Toast toast = Toast.makeText(getApplicationContext(), "GAME OVER!!!!", 1000);
			toast.show();
			exitGameSettings();
			finish();
		}
		else{
			player_life.setProgress(player.getLife());
			Toast toast = Toast.makeText(getApplicationContext(), "YOU GOT HITTED!!!!", 1000);
			toast.show();
			//starting the server again
			Intent msgIntent = new Intent(this, HitService.class);
			startService(msgIntent);

		}
	}
	/*****************************************************************/

	//on button click, implementation of OnClickListener interface
	@Override
	public void onClick(View v) {

		switch(v.getId()) {

		case R.id.reload:

			if(!someAnimationRun && !someAnimationLoad)
				reload();
			break;

		case R.id.target:

			if(!someAnimationRun && !someAnimationLoad)
				targetState();

			break;
		}

	}

	//set the screen information - player life, bullets state etc
	//if player need to reload return true, else return false
	private boolean setScreen(){		

		int currentBullets = (player.getWeapons()[player.getCurrentWeapon()]).getCurrentBullets();
		int totalBullets = (player.getWeapons()[player.getCurrentWeapon()]).getTotalBullets();

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

		if(currentBullets == 0 && totalBullets > 0)
			return true;

		return false;
	}
	/*****************************************************************/

	//operate the animation parameter anim on img
	private void setAnimation(String anim){

		AnimationDrawable animation;

		if(anim.equals("stand")){

			animation = stand_animation;
		}

		else if(anim.equals("shoot"))
			animation = shoot_animation;


		else
			animation = player.getWeapons()[player.getCurrent_weapon()].getAnimation(anim);


		if(animation != null){

			img.setImageDrawable(null);
			if(animation.isRunning())
				animation.stop();

			img.setBackgroundDrawable(null);
			img.setImageBitmap(null);
			img.setBackgroundDrawable(animation);

			animation.start();
			someAnimationLoad = false;
		}
	}
	/*****************************************************************/

	//execute the animation parameter anim once
	private void executeAnimation(AnimationDrawable anim){

		CustomAnimationDrawable CustomAnimation = new CustomAnimationDrawable(anim) {
			@Override
			void onAnimationFinish() {


				if(setScreen())
					reload();

				else
					if(!player.getWeapons()[player.getCurrent_weapon()].target_state){
						if(stand_animation == null)
							stand_animation = player.getWeapons()[player.getCurrent_weapon()].getAnimation("stand");
						setAnimation("stand");
					}
				someAnimationRun = false;
			}
		};



		img.setBackgroundDrawable(CustomAnimation);
		someAnimationRun = true;

		CustomAnimation.start();
	}
	/*****************************************************************/
	//execute the animation parameter anim once
	private void executeReplaceAnimation(AnimationDrawable anim){

		CustomAnimationDrawable CustomAnimation = new CustomAnimationDrawable(anim) {
			@Override
			void onAnimationFinish() {

				stand_animation = null;

				executeAnimation(player.getWeapons()[player.getCurrent_weapon()].getAnimation("choose"));

				Thread thread = new Thread() {
					@Override
					public void run() {

						shoot_animation = (player.getWeapons()[player.getCurrent_weapon()].getAnimation("shoot"));
					}

				};

				thread.start();

			}
		};



		img.setBackgroundDrawable(CustomAnimation);
		someAnimationRun = true;

		CustomAnimation.start();
	}
	/*****************************************************************/

	//execute the animation in segment_animation once
	private void executeSegmentsAnimation(){ 

		segment_animation = new Bitmap[ramBorden];

		writer = 0;
		reader = 0;
		soundIndex = 0;
		anim_index = 0;
		state = 0;
		nSpace = ramBorden;
		sight_img.setVisibility(View.INVISIBLE);

		someAnimationRun = true;

		//start threads
		DrawableHandler.postDelayed(drawableTask, 0);
		AnimationHandler.postDelayed(animationDisplayTask, 0);
	}
	/*****************************************************************/

	//display the already images of the animation
	private Runnable animationDisplayTask = new Runnable() {
		public void run() {          

			android.os.Process.setThreadPriority(Thread.MAX_PRIORITY);
			//if(anim_index < drawableResources.length){
				if(state < drawableResources.length)
					while(nSpace >= ramBorden);

				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(soundIndex < sounds_frames.length){
					if(anim_index == sounds_frames[soundIndex]){
						player.getWeapons()[player.getCurrent_weapon()].playSound(sounds_frames[soundIndex]);
						soundIndex++;
					}
				}

				if(anim_index < drawableResources.length){
					img.setBackgroundDrawable(null);
					img.setImageBitmap(segment_animation[reader]);

					reader = (reader + 1) % ramBorden;
					anim_index++;
					nSpace++;

					System.gc();
				}

				//if the animation ended
				else{
					segment_animation = null;
					drawableResources = null;      
					sounds_frames = null;         
					System.gc();	
					setAnimation("stand");	
					setScreen();
					sight_img.setVisibility(View.VISIBLE);

					if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState())
						changeAnimation.post(changeAnimationTask);		

					someAnimationRun = false;

					semaphore.release();
					AnimationHandler.removeCallbacks(animationDisplayTask);    //kill this thread
					return;
				}

				semaphore.release();
				AnimationHandler.postDelayed(animationDisplayTask, 50);    //return on that task again after 50 milliseconds

			
			}
			//}

		};
		/*****************************************************************/

		//this runnable task is for keep update the current animation array that run
		//and clean up the bitmaps that already displayed 
		private Runnable drawableTask = new Runnable() {
			public void run() {

				android.os.Process.setThreadPriority(Thread.MAX_PRIORITY);

				while(nSpace <= 0);

				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//if there is more bitmaps to update
				if(state < drawableResources.length){
					//write bitmap to segment animation array
					segment_animation[writer] = BitmapFactory.decodeResource(getResources(), drawableResources[state]);
					writer = (writer + 1) % ramBorden;

					state++;
					nSpace--;
					semaphore.release();
					DrawableHandler.postDelayed(drawableTask, 0);         ////return on that task again immediately
				}

				else{
					semaphore.release();
					DrawableHandler.removeCallbacks(drawableTask);        //kill this thread
				}

			}
		};
		/*****************************************************************/

		//this runnable task is for change the animations references that need to execute immediacy
		//this task on separate thread to not disturb the camera frames continuous
		private Runnable changeAnimationTask = new Runnable() {
			public void run() {	

				shoot_animation = null;

				System.gc();

				if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState())
					shoot_animation = (player.getWeapons()[player.getCurrentWeapon()]).getAnimation("shoot");


				else
					shoot_animation = (player.getWeapons()[player.getCurrentWeapon()]).getAnimation("targetshoot");



				(player.getWeapons()[player.getCurrentWeapon()]).setTargetState();
			}
		};

		/*****************************************************************/

		//reload the weapon
		private void reload(){

			if(player.getWeapons()[player.getCurrent_weapon()].target_state){
				shoot_animation = null;
				System.gc();
			}

			drawableResources = player.getWeapons()[player.getCurrent_weapon()].reload();

			if(drawableResources != null)
				executeSegmentsAnimation();

			sounds_frames = (player.getWeapons()[player.getCurrentWeapon()]).framesToNeedToPlay();
		}
		/*****************************************************************/

		//switch to target state and vice versa
		private void targetState(){


			//if the weapon already on target state
			if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState()){
				changeAnimation.post(changeAnimationTask);
				((AnimationDrawable)(img.getBackground())).stop();
				executeAnimation((player.getWeapons()[player.getCurrentWeapon()]).getAnimation("normal"));
				sight_img.setVisibility(View.VISIBLE);
			}

			else{
				changeAnimation.post(changeAnimationTask);
				setAnimation("target");
				sight_img.setVisibility(View.INVISIBLE);
			}	
		}


		/*****************************************************************/
		//	//this thread used to check if this player got shot by someone
		//	public  class HitListener_Thread extends AsyncTask<Void, Void, String> {
		//		private String response;
		//		private boolean run=true;
		//		@Override
		//		protected String doInBackground(Void... arg0) {
		//			while(run){
		//				
		////				if (isCancelled()) break;
		////				
		////				//blocking thread until hit packet received from server:
		////				try {
		////					MainActivity.hitSem.acquire();
		////				} catch (InterruptedException e) {
		////					// TODO Auto-generated catch block
		////					e.printStackTrace();
		////				}		
		////
		////
		////				 
		////
		////
		////				publishProgress();
		//
		//			}
		//			return response;
		//		}
		//
		//
		//
		//		public void stop() {
		//			run=false;
		//			
		//		}
		//
		//
		//
		//		@Override
		//		protected void onProgressUpdate(Void... v) {
		//			super.onProgressUpdate(v);
		//			hitRecvied();
		////			SingleShotLocationProvider.requestSingleUpdate(context, new SingleShotLocationProvider.LocationCallback() {
		////				@Override 
		////				public void onNewLocationAvailable(GPSCoordinates location) {
		////
		////
		////					//Gathering this player's GPS and hitter player's GPS which has been received from server:
		////					loc = new Location("thisLoc");
		////					tar = new Location("hitterLoc");
		////
		////					loc.setLatitude(location.latitude);
		////					loc.setLongitude(location.longitude);
		////					tar.setLatitude(MainActivity.hitterLatitude);
		////					tar.setLongitude(MainActivity.hitterLongitude);
		////
		////
		////					float deg = MainActivity.logic.isInjured(loc, tar, MainActivity.hitterAzimuth);
		////
		////					Toast toast = Toast.makeText(getApplicationContext(), "azimuth = " + deg, 10000);
		////					toast.show();
		////
		////					//checking if this player got shot by someone.
		////					/*if(MainActivity.logic.isInjured(loc, tar, MainActivity.hitterAzimuth)){
		////						hitRecvied();
		////
		////					}*/
		////					hitRecvied();
		////				}
		////			});
		//		}
		//
		//		@Override
		//		protected void onPostExecute(String result) {
		//			super.onPostExecute(result);
		//		}
		//
		//	}
		/*****************************************************************/
		//this class is a listener to the hit service
		public class ResponseReceiver extends BroadcastReceiver {


		public static final String ACTION_RESP = "hit";

		@Override
		public void onReceive(Context context, Intent intent) {
			hitRecvied();

		}
	}
		/*****************************************************************/
	}