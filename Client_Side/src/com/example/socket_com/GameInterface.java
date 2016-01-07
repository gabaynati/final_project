package com.example.socket_com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

import com.example.hs.R;
import com.example.socket_com.ServerCommunication.MyClientTask_ListenToPakcets;
import com.example.socket_com.ServerCommunication.MyClientTask_SendPakcet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

import org.opencv.android.*;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class GameInterface extends Activity implements OnTouchListener, OnClickListener, Runnable,CvCameraViewListener2 {

	private final int ramBurden = 5;

	private TextView current_bulletsText, total_bulletsText, slesh;
	private ProgressBar player_life;
	private ImageButton reload, target;
	private ImageView img, sight_img, board_num1, board_num2;
	private int anim_index, soundIndex, state, unUsed;
	private AnimationDrawable shoot_animation, stand_animation;
	private boolean someAnimationRun;
	private Player player=MainActivity.player;
	private Handler mAnimationHandler, DrawableHandler;
	private int[] drawableResources, sounds_frames;
	private Bitmap[] segment_animation;


	//****server communication configuration*********///
	private ServerCommunication serverCom;
	private MyClientTask_ListenToPakcets serverListener;
	private MyClientTask_SendPakcet serverDataSender;
	//**************************************************//






	///*************OpenCV configurations********************//
	protected static final String TAG = null;
	private Mat mGray,mRgba;
	//OpenCV Object for handling the camera
	private JavaCameraView mOpenCvCameraView;
	//files for face detection
	private File                   mCascadeFile;
	private CascadeClassifier      mJavaDetector;
	private float                  mRelativeFaceSize   = 0.15f;
	private int                    mAbsoluteFaceSize   = 0;
	private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
	private Rect[]                 facesArray;
	private Rect[]                 facesArrayWhileShoot;

	//the following object is a listener object that keeps track to the binding process between the activity to the OpenCv service.
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

				//initialzing the java face detector
				try {
					// load cascade file from application resources
					InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
					File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
					mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
					FileOutputStream os = new FileOutputStream(mCascadeFile);

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = is.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					is.close();
					os.close();

					mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
					if (mJavaDetector.empty()) {
						Log.e(TAG, "Failed to load cascade classifier");
						mJavaDetector = null;
					} else
						Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
					cascadeDir.delete();

				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
				}

				break;
			}
			//if the binding failed
			default:
			{
				super.onManagerConnected(status);
			}
			}
		}

	};
	//*********************************************************/////


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);





		///*************OpenCV***********************///
		//binding the OpenCV camera object to the layout
		mOpenCvCameraView=(JavaCameraView)findViewById(R.id.cameraPreview);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		//telling the OpenCV camera object which listener object we are using for the camera.
		//because we are implementing a camera listener object ,we are using "this" as argument
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.setOnTouchListener(this);
		//***************************************///



		//************Server Communication*************************//
		serverCom=new ServerCommunication();
		serverListener=serverCom.getServerListener();
		serverDataSender=serverCom.getServerDataSender();
		//initiate server listener
		serverListener.execute();
		//*************************************//




		img = (ImageView)findViewById(R.id.weaponView);
		sight_img = (ImageView)findViewById(R.id.sight);
		board_num1 = (ImageView)findViewById(R.id.board_num1);
		board_num2 = (ImageView)findViewById(R.id.board_num2);
		reload = (ImageButton)findViewById(R.id.reload);
		target = (ImageButton)findViewById(R.id.target);
		player_life = (ProgressBar)findViewById(R.id.life_progress);
		total_bulletsText = (TextView)findViewById(R.id.bullets_total);
		current_bulletsText = (TextView)findViewById(R.id.bullets_condition);
		slesh = (TextView)findViewById(R.id.slesh);

		mAnimationHandler = new Handler();
		DrawableHandler = new Handler();

		someAnimationRun = false;

		Weapon mp412 = new Mp412(getApplicationContext(), "mp412", 60);
		Weapon[] wl = new Weapon[1];
		wl[0] = mp412;
		player.setWeapons(wl);

		player_life.setMax(player.getMaxLife());

		stand_animation = player.getWeapons()[player.getCurrent_weapon()].getAnimation("stand");
		shoot_animation = (player.getWeapons()[player.getCurrentWeapon()]).getAnimation("shoot");
		setAnimation("stand");
		setScreen();


		reload.setOnClickListener(this);
		target.setOnClickListener(this);




	}


	//the activity must connect to the opencv service at the onResume callback.
	//onResume is called right after it is started but before it is available to the user.
	public void onResume(){
		super.onResume();
		//the following call binds the activity with the opencv service.
		//the third argument is a listener object that keeps track to the binding process.
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,this , mLoaderCallback);
	}

	//we are overriding the onDestroy callback of the activity ,because we want to disable the camera when the activity is destroyed.
	@Override
	public void onDestroy(){
		super.onDestroy();
		if (mOpenCvCameraView!=null)
			mOpenCvCameraView.disableView();
	}

	//##################CvCameraViewListener2 methods########################

	@Override
	public void onCameraViewStarted(int width, int height) {
		//Initializing the intermediate Mat object which is in use for frame processing
		mGray = new Mat();
		mRgba = new Mat();
	}
	@Override
	public void onCameraViewStopped() {
		//releasing them
		mGray.release();
		mRgba.release();
	}

	//this method is called when the camera delivers a frame.
	//before this callback is called , the data from the camera is ripped out to a CvCameraViewFrame object, which 
	//is passed into this callback as an argument called inputFrame.


	//an object of CvCameraViewFrame is made of two OpenCV Mat Objects.
	//a Mat Object is just a 2d array containing the data of the frame in two color space : RGB or gray
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		mRgba=inputFrame.rgba();
		mGray=inputFrame.gray();


		if (mAbsoluteFaceSize == 0) {
			int height = mGray.rows();
			if (Math.round(height * mRelativeFaceSize) > 0) {
				mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
			}
		}

		//
		MatOfRect faces = new MatOfRect();



		//running face detecting on the frame:

		if (mJavaDetector != null)
			//this function takes in a gray scale image and returns rectangles
			//that bound the faces (if any).
			mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
					new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());


		//drawing rectangles around each face in the frame.
		facesArray = faces.toArray();
		for (int i = 0; i < facesArray.length; i++)
			Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);

		return mRgba;


	}
	////############################################///

	//call when the user touch the screen
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if(!someAnimationRun){

			facesArrayWhileShoot = new Rect[facesArray.length];

			for(int i = 0; i < facesArray.length; i++)
				facesArrayWhileShoot[i] = new Rect(facesArray[i].tl(), facesArray[i].br());
			/*	switch (event.getAction()){
			//if the user still touch the screen
			case MotionEvent.ACTION_DOWN:

				break;

				//if the user left the screen
			case MotionEvent.ACTION_UP:

				break;
			}*/

			int currentBullets = (player.getWeapons()[player.getCurrentWeapon()]).getCurrentBullets();
			if(currentBullets > 0){
				someAnimationRun = true;
				executeAnimation(shoot_animation);
				player.getWeapons()[player.getCurrentWeapon()].shoot();
				
				//if hit is detected
				if(isHit()){
					Toast toast = Toast.makeText(getApplicationContext(), "HIT:"+player.getLife(), 1000);
					toast.show();
					//sending to server a GamePacket packet which contains information about the hit event
					GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(),true,false,"nati");
					serverDataSender.setPacket(packet);
					if(serverDataSender.getStatus()==Status.PENDING)
						serverDataSender.execute();
					else
					serverDataSender.doInBackground();
					
				}


				setScreen();
			}
		}

		return false;
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


	//operate the animation anim on img
	private void setAnimation(String anim){

		AnimationDrawable animation;

		if(anim.equals("stand"))
			animation = stand_animation;

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
		}
	}

	//execute the animation anim once
	private void executeAnimation(AnimationDrawable anim){

		CustomAnimationDrawable CustomAnimation = new CustomAnimationDrawable(anim) {
			@Override
			void onAnimationFinish() {

				someAnimationRun = false;

				if(setScreen())
					reload();

				else
					if(!player.getWeapons()[player.getCurrent_weapon()].target_state)
						setAnimation("stand");
			}
		};

		img.setBackgroundDrawable(CustomAnimation);
		someAnimationRun = true;

		CustomAnimation.start();
	}

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
		mAnimationHandler.postDelayed(mUpdateTimeTask, 0);
		DrawableHandler.postDelayed(drawableTask, 0);
	}

	private Runnable mUpdateTimeTask = new Runnable() {
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

			if(anim_index == segment_animation.length){
				segment_animation = null;
				drawableResources = null;
				sounds_frames = null;
				System.gc();	
				setAnimation("stand");	
				setScreen();
				sight_img.setVisibility(View.VISIBLE);

				if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState()){
					(player.getWeapons()[player.getCurrentWeapon()]).setTargetState();
					shoot_animation = (player.getWeapons()[player.getCurrentWeapon()]).getAnimation("shoot");
					System.gc();
				}

				someAnimationRun = false;
				mAnimationHandler.removeCallbacks(mUpdateTimeTask);
			}

			else
				mAnimationHandler.postDelayed(mUpdateTimeTask, 60);

		}
	};

	private Runnable drawableTask = new Runnable() {
		public void run() {

			android.os.Process.setThreadPriority(Thread.MIN_PRIORITY);

			for(int i = unUsed; i < anim_index; i++){
				segment_animation[i] = null;
				unUsed++;
			}

			System.gc();

			if(state < segment_animation.length){
				segment_animation[state] = BitmapFactory.decodeResource(getResources(), drawableResources[state]);
				state += 1;
				DrawableHandler.postDelayed(drawableTask, 0);
			}

			else{
				DrawableHandler.removeCallbacks(drawableTask);

				System.gc();
			}
		}
	};



	@Override
	public void run() {


		runOnUiThread(new Runnable() { 
			public void run() 
			{                          
			} 
		});

	}

	//reload the weapon
	private void reload(){


		drawableResources = player.getWeapons()[player.getCurrent_weapon()].reload();

		if(drawableResources != null)
			executeSegmentsAnimation();

		sounds_frames = (player.getWeapons()[player.getCurrentWeapon()]).framesToNeedToPlay();

		/*if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState()){
			(player.getWeapons()[player.getCurrentWeapon()]).setTargetState();
			shoot_animation = (player.getWeapons()[player.getCurrentWeapon()]).getAnimation("shoot");
			System.gc();
		}*/

		
	}

	//switch to target state and vice versa
	private void targetState(){

		AnimationDrawable anim;

		//if the weapon already on target state
		if((player.getWeapons()[player.getCurrentWeapon()]).getTargetState()){
			shoot_animation = (player.getWeapons()[player.getCurrentWeapon()]).getAnimation("shoot");
			System.gc();
			(player.getWeapons()[player.getCurrentWeapon()]).setTargetState();
			((AnimationDrawable)(img.getBackground())).stop();
			executeAnimation((player.getWeapons()[player.getCurrentWeapon()]).getAnimation("normal"));
			sight_img.setVisibility(View.VISIBLE);
		}

		else{
			shoot_animation = (player.getWeapons()[player.getCurrentWeapon()]).getAnimation("targetshoot");
			System.gc();
			(player.getWeapons()[player.getCurrentWeapon()]).setTargetState();
			setAnimation("target");
			sight_img.setVisibility(View.INVISIBLE);
		}		
	}

	private org.opencv.core.Rect getSightRenge(){

		double left, top, right, bottom;
		Point screenSize = getScreenSize();

		left = screenSize.x/2 - sight_img.getWidth()/2;
		top = screenSize.y/2 - sight_img.getHeight()/2;
		right = left + sight_img.getWidth();
		bottom = top + sight_img.getHeight();

		org.opencv.core.Point p1 = new org.opencv.core.Point(left, top); 
		org.opencv.core.Point p2 = new org.opencv.core.Point(right, bottom);

		return new org.opencv.core.Rect(p1, p2);
	}

	private Point getScreenSize(){

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int screenHeight = displaymetrics.heightPixels;
		int screenWidth = displaymetrics.widthPixels;

		return new Point(screenWidth, screenHeight);
	}

	private boolean isHit(){


		for (int i = 0; i < facesArrayWhileShoot.length; i++){

			if(player.getWeapons()[player.getCurrent_weapon()].target_state){

				Point screenSize = getScreenSize();
				Point sightPoint = new Point(screenSize.x/2, screenSize.y/2);

				if(sightPoint.inside(facesArrayWhileShoot[i]))
					return true;
			}

			else{

				Rect sightRenge = getSightRenge();
				Point tl, tr, bl, br;

				tl = facesArrayWhileShoot[i].tl();
				br = facesArrayWhileShoot[i].br();
				tr = new org.opencv.core.Point(br.x, tl.y);
				bl = new org.opencv.core.Point(tl.x, br.y);

				if(tl.inside(sightRenge) || br.inside(sightRenge) || tr.inside(sightRenge) || bl.inside(sightRenge))
					return true;
			}

		}

		return false;
	}









}