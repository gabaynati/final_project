package com.example.hs;

import java.io.IOException;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;

public class M4a1 extends Weapon {

	private final int max_bullets = 60;
	private int total_bullets = 150;
	private int current_bullets;
	private AnimationDrawable ad;
	

	public M4a1(Context context, String name) {
		
		super(context, name);
		current_bullets = max_bullets;
		image = BitmapFactory.decodeResource(actContext.getResources(), R.drawable.m4a1);
		sound = MediaPlayer.create(actContext, R.raw.m4a1_single);
		
		BitmapDrawable f0 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m0);
		BitmapDrawable f1 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m1);
		BitmapDrawable f2 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m2);
		BitmapDrawable f3 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m3);
		BitmapDrawable f4 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m4);
		BitmapDrawable f5 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m5);
		BitmapDrawable f6 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m6);
		BitmapDrawable f7 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m7);
		BitmapDrawable f8 = (BitmapDrawable)actContext.getResources().getDrawable(R.drawable.m8);
		
		ad = new AnimationDrawable();
		
		ad.addFrame(f0, 10);
		ad.addFrame(f1, 10);
		ad.addFrame(f2, 10);
		ad.addFrame(f3, 10);
		ad.addFrame(f4, 10);
		ad.addFrame(f5, 10);
		ad.addFrame(f6, 10);
		ad.addFrame(f7, 10);
		ad.addFrame(f8, 10);
		
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
		
		return ad;
	}

}
