package com.example.hs;

import java.io.IOException;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;

public class M4a1 extends Weapon {

	private final int max_bullets = 10;
	private int total_bullets = 13;
	private int current_bullets;
	private AnimationDrawable reload_anim, fullReload_anim, target_anim, normal_anim;
	

	public M4a1(Context context, String name) {
		
		super(context, name);
		current_bullets = max_bullets;
		sound = MediaPlayer.create(actContext, R.raw.full_reload_sound);
		
		img = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.img);
		reload_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_reload);
		fullReload_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_full_reload);
		target_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_target);
		normal_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_back_to_normal);
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
		//sound.start();
	}

	public void choose_this_weapon() {

	}

	public AnimationDrawable reload(){
		
		if(total_bullets <= 0 || current_bullets == max_bullets || current_bullets == total_bullets)
			return null;
		
		boolean fullReload = true;
		if(current_bullets > 0)
			fullReload = false;
		
		if(total_bullets > max_bullets)
			current_bullets = max_bullets;
		
		else 
			current_bullets = total_bullets;

		sound.start();
		if(fullReload)
			return fullReload_anim;
		else
			return reload_anim;
	}
	
	public AnimationDrawable target(){

		return target_anim;
	}
	
	public AnimationDrawable normal(){

		return normal_anim;
	}
}
