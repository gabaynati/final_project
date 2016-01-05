package com.example.socket_com;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

public abstract class CustomAnimationDrawable extends AnimationDrawable {

    /** Handles the animation callback. */
    Handler mAnimationHandler;
    int totalDuration;
    
    public CustomAnimationDrawable(AnimationDrawable aniDrawable) {

    	totalDuration = 0;
        for (int i = 0; i < aniDrawable.getNumberOfFrames(); i++) {
            this.addFrame(aniDrawable.getFrame(i), aniDrawable.getDuration(i));
            totalDuration += this.getDuration(i);
        }
    	
    }

    @Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
    public void start() {
        super.start();
        /*
         * Call super.start() to call the base class start animation method.
         * Then add a handler to call onAnimationFinish() when the total
         * duration for the animation has passed
         */
        mAnimationHandler = new Handler();
        mAnimationHandler.postDelayed(new Runnable() {

            public void run() {
                onAnimationFinish();
            }
        }, totalDuration);

    }

    /**
     * Called when the animation finishes.
     */
    abstract void onAnimationFinish();
}
