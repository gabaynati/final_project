package com.example.hs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

public class M4a1 extends Weapon {

	private AnimationDrawable ad;

	public M4a1(Context context, String name) {
		
		super(context, name);
		image = BitmapFactory.decodeResource(actContext.getResources(), R.drawable.m4a1);
		sound = MediaPlayer.create(actContext, R.raw.m4a1_single);
		/*
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
		
		ad.addFrame(f0, 100);
		ad.addFrame(f1, 100);
		ad.addFrame(f2, 100);
		ad.addFrame(f3, 100);
		ad.addFrame(f4, 100);
		ad.addFrame(f5, 100);
		ad.addFrame(f6, 100);
		ad.addFrame(f7, 100);
		ad.addFrame(f8, 100);
		*/
	}
	
		@Override
	public void/*AnimationDrawable*/ shoot(){
		sound.start();
		//return ad;
	}

	public void choose_this_weapon() {

		
	}

	public void reload(){

	}

}
