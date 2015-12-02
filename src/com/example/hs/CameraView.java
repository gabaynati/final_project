package com.example.hs;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private Camera camera = null;
	private SurfaceHolder mHolder;
	
	public CameraView(Context context) {
		super(context);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(mHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Camera.Parameters params = camera.getParameters();
		params.setPreviewSize(width, height);
		camera.setParameters(params);
		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera = null;
	}
	
	public boolean capture(Camera.PictureCallback jpegHandler){
		if(camera != null){
			camera.takePicture(null, null, jpegHandler);
			return true;
		}
		
		else{
			return false;
		}
	}

}
