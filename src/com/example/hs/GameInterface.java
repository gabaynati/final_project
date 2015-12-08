package com.example.hs;

import java.io.IOException;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GameInterface extends Activity implements OnTouchListener {

	final static String TAG = "PAAR";
	private SurfaceHolder previewHolder;
	private Camera camera = null;
	private SurfaceView cameraPreview;
	private Weapon m4a1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);

		cameraPreview = (SurfaceView)findViewById(R.id.cameraPreview);
		previewHolder = cameraPreview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		m4a1 = new M4a1(getApplicationContext(), "m4a1");
		ImageView img = (ImageView)findViewById(R.id.weaponView);
		img.setImageBitmap(m4a1.getImage());

		cameraPreview.setOnTouchListener(this);
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
		/*
		ImageView m = (ImageView)findViewById(R.id.imageView1);
		BitmapDrawable f0 = (BitmapDrawable)getResources().getDrawable(R.drawable.m0);
		BitmapDrawable f1 = (BitmapDrawable)getResources().getDrawable(R.drawable.m1);
		BitmapDrawable f2 = (BitmapDrawable)getResources().getDrawable(R.drawable.m2);
		BitmapDrawable f3 = (BitmapDrawable)getResources().getDrawable(R.drawable.m3);
		BitmapDrawable f4 = (BitmapDrawable)getResources().getDrawable(R.drawable.m4);
		BitmapDrawable f5 = (BitmapDrawable)getResources().getDrawable(R.drawable.m5);
		BitmapDrawable f6 = (BitmapDrawable)getResources().getDrawable(R.drawable.m6);
		BitmapDrawable f7 = (BitmapDrawable)getResources().getDrawable(R.drawable.m7);
		BitmapDrawable f8 = (BitmapDrawable)getResources().getDrawable(R.drawable.m8);
		
		AnimationDrawable ad = new AnimationDrawable();
		
		ad.addFrame(f0, 250);
		ad.addFrame(f1, 250);
		ad.addFrame(f2, 250);
		ad.addFrame(f3, 250);
		ad.addFrame(f4, 250);
		ad.addFrame(f5, 250);
		ad.addFrame(f6, 250);
		ad.addFrame(f7, 250);
		ad.addFrame(f8, 250);
		
		m.setBackgroundDrawable(ad);
		ad.start();

		
	        //	m4a1.choose_this_weapon();

	        
	  */
		
		m4a1.shoot();
		

		return false;
	}



}



