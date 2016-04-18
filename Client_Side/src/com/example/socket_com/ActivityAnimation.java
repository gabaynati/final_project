package com.example.socket_com;

import java.util.zip.InflaterInputStream;

import com.example.hs.R;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ActivityAnimation  {
	private Context con;
	public ActivityAnimation(Context con){
		this.con=con;
	}
	 public void clockwise(View view){
	      
	      Animation animation1 = AnimationUtils.loadAnimation(con, R.anim.clockwise);
	      animation1.setRepeatMode(Animation.INFINITE);
	      view.startAnimation(animation1);
	   }
	   
	   public void zoom(View view){
		      Animation animation = AnimationUtils.loadAnimation(con, R.anim.animation);
		      view.startAnimation(animation);
	   }
	   
	   public void fade(View view){
	      Animation animation1 = AnimationUtils.loadAnimation(con, R.anim.fade);
	      animation1.setRepeatMode(Animation.INFINITE);

	      view.startAnimation(animation1);
	   }
	   
	   public void blink(View view){
	      Animation animation1 = AnimationUtils.loadAnimation(con, R.anim.blink);
	      view.startAnimation(animation1);
	   }
	   
	   public void move(View view){
	      Animation animation1 = AnimationUtils.loadAnimation(con, R.anim.move);
	      view.startAnimation(animation1);
	   }
	   
	   public void slide(View view){
	      Animation animation1 = AnimationUtils.loadAnimation(con, R.anim.slide);
	      view.startAnimation(animation1);
	   }
}
