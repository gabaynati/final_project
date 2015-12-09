package com.example.hs;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
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

public class GameInterface extends Activity implements OnTouchListener, OnClickListener {

	final static String TAG = "PAAR";
	private SurfaceHolder previewHolder;
	private Camera camera = null;
	private SurfaceView cameraPreview;
	private int currentBullets, totalBullets;
	private Weapon m4a1;
	private TextView current_bulletsText, max_bulletsText, total_bulletsText;
	private ProgressBar player_life;
	private ImageButton reload;
	private ImageView img;
	private boolean pressed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);

		cameraPreview = (SurfaceView)findViewById(R.id.cameraPreview);
		previewHolder = cameraPreview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		m4a1 = new M4a1(getApplicationContext(), "m4a1");
		
		img = (ImageView)findViewById(R.id.weaponView);
		reload = (ImageButton)findViewById(R.id.reload);
		player_life = (ProgressBar)findViewById(R.id.life_progress);
		 
		player_life.setMax(100);
		img.setImageBitmap(m4a1.getImage());

		total_bulletsText = (TextView)findViewById(R.id.bullets_total);
		current_bulletsText = (TextView)findViewById(R.id.bullets_condition);

		setScreen();
		
		cameraPreview.setOnTouchListener(this);
		reload.setOnClickListener(this);
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
			((M4a1)m4a1).setCurrentBullets();	
			setScreen();
		}

		return pressed;
	}


	@Override
	public void onClick(View v) {

		AnimationDrawable animation;

		switch(v.getId()) {
		case R.id.reload:
			if((animation = ((M4a1)m4a1).reload()) != null){
				setScreen();
				img.setImageDrawable(null);
				img.setBackgroundDrawable(animation);
				animation.start();				
			}
			break;
		}	
	}

	private void setScreen(){		
		
		currentBullets = ((M4a1) m4a1).getCurrentBullets();
		totalBullets = ((M4a1) m4a1).getTotalBullets();
		String c_B = String.valueOf(currentBullets);
		String t_B = String.valueOf(totalBullets);
		current_bulletsText.setText(c_B);
		total_bulletsText.setText("/" + t_B);
	}

}



