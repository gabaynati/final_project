package com.example.socket_com;

import java.io.IOException;

import com.example.hs.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Mp433 extends Weapon implements OnCompletionListener {

	private final int max_bullets = 6;
	private final int reload_anim_size = 61;
	private final String reload_anim_name = "reload";
	private final int frameSound1 = 9, frameSound2 = 36, frameSound3 = 51;


	public Mp433(Context context, String name, int total_bullets) {


		super(context, name, total_bullets);

		current_bullets = max_bullets;
	} 


	@Override
	public void shoot(){

		current_bullets--;
		total_bullets--;

		sound = MediaPlayer.create(actContext, R.raw.mp433_shoot_sound);
		try {
			sound.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sound.start();
	}

	public void choose_this_weapon() {

	}

	@Override
	public int[] reload(){

		if(total_bullets <= 0 || current_bullets == max_bullets || current_bullets == total_bullets)
			return null;

		if(total_bullets > max_bullets)
			current_bullets = max_bullets;

		else 
			current_bullets = total_bullets;

		return buildDrawables(reload_anim_size, reload_anim_name);
	}

	@Override
	public AnimationDrawable getAnimation(String anim){

		if(anim.equals("stand"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp443_stand_animation);

		else if(anim.equals("target"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp433_target_animation);

		else if(anim.equals("normal"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp433_normal_animation);

		else if(anim.equals("shoot"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp433_shoot_animation);

		else if(anim.equals("targetshoot"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp433_target_shoot_animation);

		else
			return null;
	}

	@Override
	public int getMaxBullets() {
		return max_bullets;
	}


	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.release();
	}

	@Override
	public int[] framesToNeedToPlay(){

		int[] frames = {frameSound1, frameSound2, frameSound3};
		
		return frames;
	}
	
	@Override
	public void playSound(int frameNum){
		
		if(frameNum == frameSound1){
		    sound = MediaPlayer.create(actContext, R.raw.mp433_reload_sound1);
			sound.start();
		}
		
		else if(frameNum == frameSound2){
			sound = MediaPlayer.create(actContext, R.raw.mp433_reload_sound2);
			sound.start();
		}
		
		else if(frameNum == frameSound3){
			sound = MediaPlayer.create(actContext, R.raw.mp433_reload_sound3);
			sound.start();
		}
	}

	private int[] buildDrawables(final int animSize, final String animName){

		int[] drawables = new int[animSize];

		for(int i = 1; i < animSize + 1; i++){
			String uri = "@drawable/" + name + "_" + animName + i;
			int imageResource = actContext.getResources().getIdentifier(uri, null, "com.example.hs");
			drawables[i-1] = imageResource;
		}
		return drawables;
	}
}
