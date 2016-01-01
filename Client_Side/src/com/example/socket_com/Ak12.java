package com.example.socket_com;

import java.io.IOException;

import com.example.hs.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Ak12 extends Weapon implements OnCompletionListener {

	private final int max_bullets = 10;
	private AnimationDrawable reload_anim, fullReload_anim, target_anim, normal_anim, stand_anim, shoot_anim, target_shoot_anim;

	public Ak12(Context context, String name, int total_bullets) {
		
		super(context, name, total_bullets);

		current_bullets = max_bullets;
		reload_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_reload);
		fullReload_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_full_reload);
		target_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_target);
		normal_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_back_to_normal);
		stand_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_stand);
		shoot_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_shoot);
		target_shoot_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.target_shoot);
		
		sight = actContext.getResources().getDrawable( R.drawable.ak12_sight);
	} 
	
	
	@Override
	public AnimationDrawable shoot(){
		
		current_bullets--;
		total_bullets--;
		
		sound = MediaPlayer.create(actContext, R.raw.ak12_one_shoot);
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
		
		if(target_state)
			return target_shoot_anim;
		
		else
			return shoot_anim;
	}

	public void choose_this_weapon() {

	}

	@Override
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

		if(fullReload){
			sound = MediaPlayer.create(actContext, R.raw.ak12_full_reload);
			sound.start();
			return fullReload_anim;
		}
		
		else{
			sound = MediaPlayer.create(actContext, R.raw.ak12_reload);
			sound.start();
			return reload_anim;
		}
	}
	
	@Override
	public AnimationDrawable stand(){
		return stand_anim;
	}
	
	@Override
	public AnimationDrawable target(){
		return target_anim;
	}
	
	@Override
	public AnimationDrawable normal(){
		return normal_anim;
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
}
