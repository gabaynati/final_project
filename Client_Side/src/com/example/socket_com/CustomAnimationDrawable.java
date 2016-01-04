package com.example.socket_com;

import com.example.hs.R;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

public abstract class CustomAnimationDrawable extends AnimationDrawable {

    /** Handles the animation callback. */
    Handler mAnimationHandler;
    
    public CustomAnimationDrawable(AnimationDrawable aniDrawable) {

        for (int i = 0; i < aniDrawable.getNumberOfFrames(); i++) {
            this.addFrame(aniDrawable.getFrame(i), aniDrawable.getDuration(i));
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
            	stop();
                onAnimationFinish();
            }
        }, getTotalDuration());

    }

    /**
     * Gets the total duration of all frames.
     * 
     * @return The total duration.
     */
   public int getTotalDuration() {

        int iDuration = 0;

        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            iDuration += this.getDuration(i);
        }

        return iDuration;
    }

    /**
     * Called when the animation finishes.
     */
    abstract void onAnimationFinish();
}
