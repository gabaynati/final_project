package com.example.socket_com;

import com.example.hs.R;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;

public class Ak12 extends Weapon {

	private final int max_bullets = 10;
	private AnimationDrawable reload_anim, fullReload_anim, target_anim, normal_anim, stand_anim;

	public Ak12(Context context, String name, int total_bullets) {
		
		super(context, name, total_bullets);

		
		current_bullets = max_bullets;
		reload_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_reload);
		fullReload_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_full_reload);
		target_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_target);
		normal_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_back_to_normal);
		stand_anim = (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.ak12_stand);
	} 
	
	
	@Override
	public void/*AnimationDrawable*/ shoot(){
		//sound.start();
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
			sound = MediaPlayer.create(actContext, R.raw.full_reload_sound);
			sound.start();
			return fullReload_anim;
		}
		
		else{
			sound = MediaPlayer.create(actContext, R.raw.reload);
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
}
