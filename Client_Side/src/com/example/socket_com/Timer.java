package com.example.socket_com;

import android.os.Handler;

public abstract class Timer {

	private Handler handler;
	private int duration;        //time in miliseconds
	
	//constructor, the parameter is the time duration
	public Timer(int dur){
		
		this.duration = dur;
	}
	
	//start to count
	public void start(){
		
		 handler = new Handler();
	        handler.postDelayed(new Runnable() {

	            public void run() {
	                onFinish();
	            }
	        }, duration);

	}
	
	//after the the duration is over
	public abstract void onFinish();
}
