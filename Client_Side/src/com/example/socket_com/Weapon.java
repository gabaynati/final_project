/*
 * weapon class
 * abstract class that represent weapon
 * if we need to add new weapon, that new weapon will need to extends this class 
 * and implementation it abstract methods 
 */

package com.example.socket_com;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;

public abstract class Weapon {
	
	protected MediaPlayer sound;
	protected String name;                //the name of this weapon
	protected int total_bullets;          //the number of total bullets
	protected int current_bullets;        //the number of the bullets that remaining on current time
	protected boolean target_state;       //if the player holds the weapon on target state or not
	protected Context actContext;         //application context
	

	
	//constructor, get the application context, weapon's name and total bullets respectively 
	public Weapon(Context context, String n, int t_bullets){
		
		actContext = context;
		name = n;
		total_bullets = t_bullets;
		target_state = false;
	}
	
	//get the target state
	public boolean getTargetState(){
		return target_state;
	}
	
	//set the target state
	public void setTargetState(){
		
		target_state = !target_state;
	}
	
	//get the current number of bullets
	public int getCurrentBullets(){
		return current_bullets;
	}
	
	//get the total number of bullets
	public int getTotalBullets(){
		return total_bullets;
	}
	
	//return the frames's number that need to accompanied with sound
	public abstract int[] framesToNeedToPlay();
	
	//play sound according to frame's number
	public void playSound(int frameNum){}
	
	//get the max bullets that the weapon stack can receive
	public abstract int getMaxBullets();

	//return resources id array of reload animation bitmaps
	public abstract int[] reload();

	//return animation according to anim parameter
	public abstract AnimationDrawable getAnimation(String anim);
	
	//this methot called when the user shoot at this weapon
	public abstract void shoot();

}
