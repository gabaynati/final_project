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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import android.content.Intent;
import android.content.IntentFilter;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.BackgroundSubtractorMOG2;

import android.provider.Settings;

import com.example.hs.R;
import com.example.socket_com.GameInfoActivity.updateGameInfo_Thread;
import com.example.socket_com.ServerCommunication.MyClientTask_ListenToPakcets;
import com.example.socket_com.ServerCommunication.MyClientTask_SendPakcet;
import com.example.socket_com.SingleShotLocationProvider.GPSCoordinates;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
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
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TextView;

import org.opencv.android.*;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class GameInterface extends Activity implements OnTouchListener, OnClickListener, CvCameraViewListener2, SensorEventListener, LocationListener {
	//private Vector<HitListener_Thread> hitThreads;
	private int total_score=20;
	private Context context;
	//private MainActivity.logic MainActivity.logic;
	private final int ramBurden = 5;
	private FrameLayout frame;
	private TextView current_bulletsText, total_bulletsText, slesh;
	private ProgressBar player_life;
	private ImageButton reload, target, shoot;
	private ImageView img, bullet_hit, sight_img, board_num1, board_num2;
	private int anim_index, soundIndex, state, unUsed, shootingTime;
	private float x1, x2;
	private AnimationDrawable shoot_animation, stand_animation;
	private boolean someAnimationRun, someAnimationLoad, pressed, touched;
	private Player player=MainActivity.player;
	private Handler AnimationHandler, DrawableHandler, changeAnimation, shootHandler;
	private int[] drawableResources, sounds_frames;
	private Bitmap[] segment_animation;
	public static final int UPPER_BODY_HIT_SCORE=50,FACE_HIT_SCORE=100,LOWER_BODY_HIT_SCORE=20;


	/*************Sensors****************/
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mMagnetometer;

	private float azimuth = 0;
	private TextView sensorTest;
	/********************************************/

	/*******************GPS********************/
	private TextView gpsTest,score_lbl;
	private LocationManager location;
	private String bestProvider;

	private Location loc;
	private Location tar;

	private float degree;
	/******************************************/






	/*************OpenCV configurations********************/
	private final String face_xml_res = "lbpcascade_frontalface";
	private final String lowerBody_xml_res = "haarcascade_lowerbody";
	private final String upperBody_xml_res = "haarcascade_upperbody";
	protected static final String TAG = null;
	private Mat mGray,mRgba;
	//OpenCV Object for handling the camera
	private JavaCameraView mOpenCvCameraView;
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
	private Mat                           mHsv;
	private ColorBlobDetector             mDetector;
	private Mat                           mSpectrum;
	private Size                          SPECTRUM_SIZE;
	private Scalar                        CONTOUR_COLOR;
	private String[]                      players;
	private HashMap<String,List<MatOfPoint>>  colorsFounds;

	/*********************************************************/////




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		context = this.getApplicationContext();
		/***************************Sensors*************************************/
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		/******************************************************************/

		/******************************GPS*********************************/
		location = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = location.getBestProvider(crit, false);
		//location.requestLocationUpdates(bestProvider, 0, 0, this);
		/******************************************************************/
		//hitThreads=new Vector<HitListener_Thread>();


		///*************OpenCV***********************///
		//binding the OpenCV camera object to the layout
		mOpenCvCameraView=(JavaCameraView)findViewById(R.id.cameraPreview);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		//telling the OpenCV camera object which listener object we are using for the camera.
		//because we are implementing a camera listener object ,we are using "this" as argument
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.setOnTouchListener(this);
		//***************************************///


		MainActivity.logic = new Logic(mOpenCvCameraView);

		touched = false;


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
		score_lbl=(TextView)findViewById(R.id.score_lbl);
		score_lbl.setText("Score: " + total_score);
		AnimationHandler = new Handler();
		DrawableHandler = new Handler();
		changeAnimation = new Handler();

		someAnimationRun = false;

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




		/***service for listening to hits***/
		//starting the service
		Intent msgIntent = new Intent(this, HitService.class);
		startService(msgIntent);

		//creating a listener to the service 
		IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		ResponseReceiver reponse = new ResponseReceiver();
		registerReceiver(reponse, filter);
		/*****////


		
		
		
		/****color detection*****************/
		/*RGB rgb = new RGB(154.75, 73.671875, 219.734375);
    	Scalar scalar = new Scalar(rgb.getRed(), rgb.getGreen(), rgb.getBlue());*/

		MainActivity.currentGamePlayersColors = new HashMap<String,RGB>();
		MainActivity.currentGamePlayersColors.put("nati", new RGB(89.671875, 226.75, 158.734375));
		MainActivity.currentGamePlayersColors.put("gili", new RGB(29.203125, 244.859375, 194.359375));


		players = new String[MainActivity.currentGamePlayersColors.size()];
		colorsFounds = new HashMap<String,List<MatOfPoint>>();

		Iterator<Map.Entry<String,RGB>> it = MainActivity.currentGamePlayersColors.entrySet().iterator();
			
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<String,RGB> entry = (Map.Entry<String,RGB>)it.next();
			players[i++] = (String) entry.getKey();
		}
		/**************************************************/

		
		
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



		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);


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

	@Override
	public void onPause(){
		super.onPause();
		if (mOpenCvCameraView!=null)
			mOpenCvCameraView.disableView();


		mSensorManager.unregisterListener(this, mAccelerometer);
		mSensorManager.unregisterListener(this, mMagnetometer);



	}
	//##################CvCameraViewListener2 methods########################
	/*****************************************************************/

	@Override
	public void onCameraViewStarted(int width, int height) {
		//Initializing the intermediate Mat object which is in use for frame processing
		mGray = new Mat();
		mRgba = new Mat();

		mHsv = new Mat(height, width, CvType.CV_8UC4);
		mDetector = new ColorBlobDetector();
		mSpectrum = new Mat();
		SPECTRUM_SIZE = new Size(200, 64);
		CONTOUR_COLOR = new Scalar(0,255,0,255);

		MainActivity.logic.setMats(mGray, mRgba);
	}
	/*****************************************************************/

	@Override
	public void onCameraViewStopped() {
		//releasing them
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

		//iterate every player to find it's color
		//if the color is found ,then it saves it's image points where it found
		for(int i = 0; i < players.length; i++){

			RGB rgb = MainActivity.currentGamePlayersColors.get(players[i]);
			Scalar hsv = new Scalar(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
			
			mDetector.setHsvColor(hsv);

			mDetector.process(mRgba);
			List<MatOfPoint> contours = mDetector.getContours();//gets list of image points where the color was found
			Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);//drawing circle near the points

			colorsFounds.put(players[i], contours);
		}
		
		if(!someAnimationRun && touched){//shoot time

			for(int i = 0; i < players.length; i++){

				RGB rgb = MainActivity.currentGamePlayersColors.get(players[i]);
				Scalar hsv = new Scalar(rgb.getRed(), rgb.getGreen(), rgb.getBlue());

				mDetector.setHsvColor(hsv);

				mDetector.process(mRgba);
				List<MatOfPoint> contours = mDetector.getContours();//get the mat of points which the hsv color was found there
				Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

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


			MatOfRect lowerBodies = new MatOfRect();
			MatOfRect upperBodies = new MatOfRect();
			//MatOfRect lowerBodies = new MatOfRect();


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

			//drawing rectangles around each lower body in the frame.
			lowerBodyArray = lowerBodies.toArray();
			for (int i = 0; i < lowerBodyArray.length; i++)
				Imgproc.rectangle(mRgba, lowerBodyArray[i].tl(), lowerBodyArray[i].br(), RECT_COLOR, 3);


			//drawing rectangles around each upper body in the frame.
			upperBodyArray = upperBodies.toArray();
			for (int i = 0; i < upperBodyArray.length; i++)
				Imgproc.rectangle(mRgba, upperBodyArray[i].tl(), upperBodyArray[i].br(), RECT_COLOR, 3);

			/*
			//drawing rectangles around each lower body in the frame.
			lowerBodyArray = upperBodies.toArray();
			for (int i = 0; i < lowerBodyArray.length; i++)
				Imgproc.rectangle(mRgba, lowerBodyArray[i].tl(), lowerBodyArray[i].br(), RECT_COLOR, 3);
			 */

			
			touched = false;
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
					shootHandler.postDelayed(shootAction, shootingTime);
					shoot();
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

			shoot();
			shootHandler.postDelayed(this, shootingTime);
		}
	};
	/*****************************************************************/

	//call this method when the user shoot
	private void shoot(){

		touched = true;

		int currentBullets = (player.getWeapons()[player.getCurrentWeapon()]).getCurrentBullets();
		if(currentBullets > 0){
			someAnimationRun = true;
			executeAnimation(shoot_animation);
			player.getWeapons()[player.getCurrentWeapon()].shoot();

			//catch the detection rectangles on shoot time
			//MainActivity.logic.catchRect();

			//if this player hits someone
//			final int hitArea = MainActivity.logic.isHit(facesArray, upperBodyArray, lowerBodyArray);
//			String hit_area="";
//			switch(hitArea){
//			case Logic.UPPER_BODY_HIT:
//				hit_area="Upper body";
//				break;
//			case Logic.FACE_HIT:
//				hit_area="Head shot";
//				break;
//			case Logic.LOWER_BODY_HIT:
//				hit_area="Lower body";
//				break;
//
//
//
//
//			}	

			//hit was detected
			//if(hitArea != -1){

				String name = MainActivity.logic.specificHit(colorsFounds, players);

				if(name == null)
					name = "null";
				
				Toast toast2 = Toast.makeText(getApplicationContext(), "HIT: " + name, 1000);
				toast2.show();
				
//				switch(hitArea){
//				case Logic.UPPER_BODY_HIT:
//					total_score+=UPPER_BODY_HIT_SCORE;
//					break;
//				case Logic.FACE_HIT:
//					total_score+=FACE_HIT_SCORE;
//					break;
//				case Logic.LOWER_BODY_HIT:
//					total_score+=LOWER_BODY_HIT_SCORE;
//					break;
//				}
//				//updating the socre:
//				score_lbl.setText("Score: " + total_score);
//
//				//	drawHit();
//				Toast toast = Toast.makeText(getApplicationContext(), "HIT: " + hit_area + " " + player.getLife(), 1000);
//				toast.show();					

				//sending GPS to server:
				//				SingleShotLocationProvider.requestSingleUpdate(this, new SingleShotLocationProvider.LocationCallback() {
				//					@Override 
				//					public void onNewLocationAvailable(GPSCoordinates location) {
				//						double latitude = location.latitude;
				//						double longitude = location.longitude;
				//						Toast toast = Toast.makeText(getApplicationContext(), "latitude = " + location.latitude + "longitude " + location.longitude, 10000);
				//						toast.show();
				//						MainActivity.server_com.sendHitToServer(hitArea, azimuth, location.latitude, location.longitude);
				//					}
				//
				//				});
				/*RGB hitPlayerColor = null;
				MainActivity.server_com.sendHitToServer(hitArea,hitPlayerColor);*/


				/**********************************/


			}


			setScreen();
		}
	//}
	/*****************************************************************/


	private void drawHit(){

		Timer timer = new Timer() {

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

		((Animatable) timer).start();


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

			/*if(stand_animation == null)
				stand_animation = player.getWeapons()[player.getCurrent_weapon()].getAnimation("stand");*/

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

		segment_animation = new Bitmap[drawableResources.length];
		for(int i = 0; i < ramBurden; i++)
			segment_animation[i] = BitmapFactory.decodeResource(getResources(), drawableResources[i]);

		state = ramBurden;
		unUsed = 0;
		anim_index = 0;
		soundIndex = 0;
		sight_img.setVisibility(View.INVISIBLE);

		someAnimationRun = true;
		AnimationHandler.postDelayed(animationDisplayTask, 0);
		DrawableHandler.postDelayed(drawableTask, 0);
	}
	/*****************************************************************/

	private Runnable animationDisplayTask = new Runnable() {
		public void run() {          

			android.os.Process.setThreadPriority(Thread.MAX_PRIORITY);

			if(soundIndex < sounds_frames.length){
				if(anim_index == sounds_frames[soundIndex]){
					player.getWeapons()[player.getCurrent_weapon()].playSound(sounds_frames[soundIndex]);
					soundIndex++;
				}
			}

			img.setBackgroundDrawable(null);
			img.setImageBitmap(segment_animation[anim_index]);
			anim_index++;

			//if the animation ended
			if(anim_index == segment_animation.length){
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
				AnimationHandler.removeCallbacks(animationDisplayTask);    //kill this thread
			}

			else
				AnimationHandler.postDelayed(animationDisplayTask, 60);    //return on that task again after 60 milliseconds

		}
	};
	/*****************************************************************/

	//this runnable task is for keep update the current animation array that run
	//and clean up the bitmaps that already displayed 
	private Runnable drawableTask = new Runnable() {
		public void run() {

			android.os.Process.setThreadPriority(Thread.MIN_PRIORITY);

			//clean up the bitmaps that already displayed
			for(int i = unUsed; i < anim_index; i++){
				segment_animation[i] = null;
				unUsed++;
			}

			System.gc();

			//if there is more bitmaps to update
			if(state < segment_animation.length){
				//add one more bitmap to segment animation array
				segment_animation[state] = BitmapFactory.decodeResource(getResources(), drawableResources[state]);
				state += 1;
				DrawableHandler.postDelayed(drawableTask, 0);         ////return on that task again immediately
			}

			else{
				DrawableHandler.removeCallbacks(drawableTask);        //kill this thread

				System.gc();
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

	float[] mGravity;
	float[] mGeomagnetic;
	/*****************************************************************/

	@Override
	public void onSensorChanged(SensorEvent event) {



		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values;

		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values;

		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll

			}
		}
		azimuth = (float)Math.toDegrees(azimuth);

		if(azimuth > 90 && azimuth <= 180){
			azimuth += 90;
			float offset = Math.abs(180 - azimuth);
			azimuth = (180 - offset) * -1;
		}

		else
			azimuth += 90;

		//total_bulletsText.setText(Float.toString(azimuth));
		//sensorTest.setText(Float.toString(azimuth));

		/*azimuth += geoField.getDeclination();

		azimuth = azimuth - degree;*/

		//		sensorTest.setText(Float.toString(azimuth));
		/*if (event.sensor == mAccelerometer){
			//total_bulletsText.setText(Float.toString(event.values[1]));
		}

		else if (event.sensor == mMagnetometer) {
            //System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
           // mLastMagnetometerSet = true;
			//total_bulletsText.setText(Float.toString(event.values[0]) + "," + Float.toString(event.values[1]) + "," + Float.toString(event.values[2]));
        }*/

	}
	/*****************************************************************/

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	/*****************************************************************/

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub


		/*GeomagneticField geoField = new GeomagneticField(
		             (float) location.getLatitude(),
		             (float) location.getLongitude(),
		             (float) location.getAltitude(),
		             System.currentTimeMillis());

			azimuth += geoField.getDeclination();

			tar.setLatitude(location.getLatitude()-1);
			tar.setLongitude(location.getLongitude());

			float bearing = location.bearingTo(tar);


			gpsTest.setText(Float.toString(bearing));
			sensorTest.setText(Float.toString(azimuth));*/

		/*String msg = "New Latitude: " + location.getLatitude()
				+ ",New Longitude: " + location.getLongitude();

		total_bulletsText.setText(msg);*/
	}
	/*****************************************************************/

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	/*****************************************************************/

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}
	/*****************************************************************/

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
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