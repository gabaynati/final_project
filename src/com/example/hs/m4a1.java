package com.example.hs;

import java.io.IOException;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;

public class M4a1 extends Weapon {

	private final int max_bullets = 60;
	private int total_bullets = 150;
	private int current_bullets;
	private AnimationDrawable ad;
	

	public M4a1(Context context, String name) {
		
		super(context, name);
		current_bullets = max_bullets;
		sound = MediaPlayer.create(actContext, R.raw.m4a1_single);
		
		img = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.img);
		stand = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.animation);
		ad = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.target);
	}
	
	public int getMaxBullets(){
		return max_bullets;
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
	
		@Override
	public void/*AnimationDrawable*/ shoot(){
		sound.start();
	}

	public void choose_this_weapon() {

	}

	public AnimationDrawable reload(){
		
		if(total_bullets <= 0 || current_bullets == max_bullets)
			return null;
		
		if(total_bullets > max_bullets)
			current_bullets = max_bullets;
		
		else 
			current_bullets = total_bullets;
		
		return stand;
	}
	
	public AnimationDrawable target(){
		return ad;
	}

}
