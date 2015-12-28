package com.example.socket_com;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

public abstract class Weapon {

	protected String name;
	protected Drawable sight;
	protected int total_bullets;
	protected int current_bullets;
	protected boolean target_state;
	protected Context actContext;
	protected MediaPlayer sound;

	public Weapon(Context context, String n, int t_bullets){
		
		actContext = context;
		name = n;
		total_bullets = t_bullets;
		target_state = false;
	}
	
	public Drawable getSight(){
		return sight;
	}
	
	public boolean getTargetState(){
		return target_state;
	}
	
	public void setTargetState(){
		
		if(target_state)
			target_state = false;
		
		else
			target_state = true;
	}
	
	public int getCurrentBullets(){
		return current_bullets;
	}
	
	public int getTotalBullets(){
		return total_bullets;
	}
	
	public void setCurrentBullets(){
		current_bullets--;
		total_bullets--;
	}
	
	public abstract int getMaxBullets();

	public abstract AnimationDrawable shoot();

	public abstract AnimationDrawable reload();

	public abstract AnimationDrawable stand();

	public abstract AnimationDrawable target();

	public abstract AnimationDrawable normal();	

}
