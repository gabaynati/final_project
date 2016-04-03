/*
 * Mp412 gun class
 */

package com.example.socket_com;

import java.io.IOException;

import com.example.hs.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Mp412 extends Weapon {

	private final int max_bullets = 6;                                       //max stack size of this weapon
	private final int reload_anim_size = 61;                                 //reload animation size of this weapon
	private final int shootingTime = 500;                                    //shooting time at mili seconds
	private final String reload_anim_name = "reload";
	private final int frameSound1 = 9, frameSound2 = 36, frameSound3 = 51;   //frames number that need to play sound when displayed


	//constructor, parameters: application context, weapon name, total bullets
	public Mp412(Context context, String name, int total_bullets) {

		super(context, name, total_bullets);
		current_bullets = max_bullets;
	} 


	//called when the user shoot at this weapon
	@Override
	public void shoot(){

		current_bullets--;
		total_bullets--;

		sound = MediaPlayer.create(actContext, R.raw.mp412_shoot_sound);
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

	//return resources id array of reload animation bitmaps
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

	//return animation according to anim parameter
	@Override
	public AnimationDrawable getAnimation(String anim){

		if(anim.equals("drop")){
			target_state = false;
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp412_drop_animation);
		}
		
		else if(anim.equals("choose"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp412_choose_animation);
			
		else if(anim.equals("stand"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp412_stand_animation);

		else if(anim.equals("target"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp412_target_animation);

		else if(anim.equals("normal"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp412_normal_animation);

		else if(anim.equals("shoot"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp412_shoot_animation);

		else if(anim.equals("targetshoot"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.mp412_target_shoot_animation);

		else
			return null;
	}

	//get the max bullets that the weapon stack can receive
	@Override
	public int getMaxBullets() {
		return max_bullets;
	}

	//return the frames's number that need to accompanied with sound
	@Override
	public int[] framesToNeedToPlay(){

		int[] frames = {frameSound1, frameSound2, frameSound3};
		
		return frames;
	}
	
	//play sound according to frame's number
	@Override
	public void playSound(int frameNum){
		
		if(frameNum == frameSound1){
		    sound = MediaPlayer.create(actContext, R.raw.mp412_reload_sound1);
			sound.start();
		}
		
		else if(frameNum == frameSound2){
			sound = MediaPlayer.create(actContext, R.raw.mp412_reload_sound2);
			sound.start();
		}
		
		else if(frameNum == frameSound3){
			sound = MediaPlayer.create(actContext, R.raw.mp412_reload_sound3);
			sound.start();
		}
	}

	//return resources id array of animation bitmaps according to parameters animSize and animName
	private int[] buildDrawables(final int animSize, final String animName){

		int[] drawables = new int[animSize];

		for(int i = 1; i < animSize + 1; i++){
			String uri = "@drawable/" + name + "_" + animName + i;
			int imageResource = actContext.getResources().getIdentifier(uri, null, "com.example.hs");
			drawables[i-1] = imageResource;
		}
		return drawables;
	}


	@Override
	public int shootingTime() {
		return shootingTime;
	}
}
