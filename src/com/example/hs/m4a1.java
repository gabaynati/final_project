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
	private AnimationDrawable target_animation, normal_animation;
	

	public M4a1(Context context, String name) {
		
		super(context, name);
		current_bullets = max_bullets;
		sound = MediaPlayer.create(actContext, R.raw.m4a1_single);
		
		img = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.img);
		stand = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.animation);
		target_animation = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.target);
		normal_animation = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.back_to_normal);
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
		return target_animation;
	}
	
	public AnimationDrawable normal(){
		return normal_animation;
	}
}
