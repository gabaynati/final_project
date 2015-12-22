package com.example.hs;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameInterface extends Activity implements OnTouchListener, OnClickListener, Runnable {

	final static String TAG = "PAAR";
	private SurfaceHolder previewHolder;
	private Camera camera = null;
	private SurfaceView cameraPreview;
	private int currentBullets, totalBullets;
	private Weapon m4a1;
	private TextView current_bulletsText, total_bulletsText;
	private ProgressBar player_life;
	private ImageButton reload, target;
	private ImageView img;
	private AnimationDrawable animation;
	private boolean pressed = false, target_state;
	private BitmapDrawable image;
	private int img_w,img_h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);

		cameraPreview = (SurfaceView)findViewById(R.id.cameraPreview);
		previewHolder = cameraPreview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		m4a1 = new Ak12(getApplicationContext(), "Ak12");

		img = (ImageView)findViewById(R.id.weaponView);
		reload = (ImageButton)findViewById(R.id.reload);
		target = (ImageButton)findViewById(R.id.target);
		player_life = (ProgressBar)findViewById(R.id.life_progress);

		player_life.setMax(100);
		image = m4a1.getImage();

		img.setImageDrawable(image);
		img_w = img.getWidth();
		img_h = img.getHeight();
		total_bulletsText = (TextView)findViewById(R.id.bullets_total);
		current_bulletsText = (TextView)findViewById(R.id.bullets_condition);

		setScreen();
		target_state = false;

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

		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			pressed = true;
			break;

		case MotionEvent.ACTION_UP:
			pressed = false;
			break;
		}

		if(pressed & currentBullets > 0){
			m4a1.shoot();
			((Ak12)m4a1).setCurrentBullets();	
			setScreen();
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

		currentBullets = ((Ak12) m4a1).getCurrentBullets();
		totalBullets = ((Ak12) m4a1).getTotalBullets();
		if(currentBullets == 0 && totalBullets >0)
			reload();
		String c_B = String.valueOf(currentBullets);
		String t_B = String.valueOf(totalBullets);
		current_bulletsText.setText(c_B);
		total_bulletsText.setText("/" + t_B);
	}


	@Override
	public void run() {
		runOnUiThread(new Runnable(){
			public void run() {  	
				animation.start();
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

		if((animation = ((Ak12)m4a1).reload()) != null){
			setScreen();
			img.setImageDrawable(null);
			if(animation.isRunning())
				((AnimationDrawable)(img.getBackground())).stop();

			img.setBackgroundDrawable(animation);
			setImgSize(img_w, img_h);
			animation.start();
		}
	}
	
	private void targetState(){
		
		if(target_state)
			animation = ((Ak12)m4a1).normal();

		else
			animation = ((Ak12)m4a1).target();

		if(animation != null){
			setScreen();
			img.setImageDrawable(null);
			if(animation.isRunning())
				((AnimationDrawable)(img.getBackground())).stop();

			img.setBackgroundDrawable(animation);

			if(!target_state){
				target_state = true;
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				int width = size.x;
				int height = size.y;
				int newSize;

				if(width < height)
					newSize = width;
				else
					newSize = height;

				setImgSize(newSize, newSize);
			}

			else{
				target_state = false;
				setImgSize(img_w, img_h);
			}
			animation.start();	
		}
	}
}