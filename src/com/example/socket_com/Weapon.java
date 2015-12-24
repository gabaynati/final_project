package com.example.socket_com;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

public abstract class Weapon {

	protected String name;
	protected Context actContext;
	protected MediaPlayer sound;
	protected AnimationDrawable stand;
	protected BitmapDrawable img;
	
	public Weapon(Context context, String name){
		actContext = context;
		this.name = name;
	}
	
	public BitmapDrawable getImage(){
		return img;
	}

	public abstract void/*AnimationDrawable*/ shoot();
		
		
	
	

}
