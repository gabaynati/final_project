package com.example.hs;

import java.io.IOException;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

public class GameInterface extends Activity {
	
	final static String TAG = "PAAR";
	private SurfaceHolder previewHolder;
	private Camera camera = null;
	private SurfaceView cameraPreview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.game_layout);
		
		cameraPreview = (SurfaceView)findViewById(R.id.cameraPreview);
        previewHolder = cameraPreview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        
        FrameLayout gameLayout = (FrameLayout)findViewById(R.id.game_layout);
        M4a1 m = new M4a1(getApplicationContext());
        gameLayout.addView(m);
        
	}

	

	 SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() {
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
  


}



