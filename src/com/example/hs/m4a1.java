package com.example.hs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.View;

public class M4a1 extends View {

	private Drawable image;
	private MediaPlayer player;

	public M4a1(Context context) {
		super(context);

		image = getResources().getDrawable(R.drawable.m4a1);
		player = MediaPlayer.create(context, R.raw.m4a1_single);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int left, top, right, bottom;

		left = canvas.getWidth()/2 - image.getIntrinsicWidth()/2;
		top = canvas.getHeight() - image.getIntrinsicHeight();;
		right = canvas.getWidth()/2 + image.getIntrinsicWidth()/2;
		bottom = canvas.getHeight();

		image.setBounds(left, top, right, bottom);
		image.draw(canvas);
	}

	public void shoot(){
		player.start();
	}

	public void choose_this_weapon(View v) {

		image = getResources().getDrawable(getResources().getIdentifier("m1", "drawable", "com.example.hs"));
		invalidate();
	}

	public void reload(){

	}

}
