package com.example.hs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

public abstract class Weapon {

	protected String name;
	protected Context actContext;
	protected MediaPlayer sound;
	protected Bitmap image;
	
	public Weapon(Context context, String name){
		actContext = context;
		this.name = name;
	}
	
	public Bitmap getImage(){
		return image;
	}

	public abstract void/*AnimationDrawable*/ shoot();
		
		
	
	

}
