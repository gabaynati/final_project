package com.example.socket_com;

import com.example.hs.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

public class Srr61 extends Weapon {

	private final int max_bullets = 10;                                       //max stack size of this weapon
	private final int reload_anim_size = 21;                                 //reload animation size of this weapon
	private final String reload_anim_name = "reload";
	
	public Srr61(Context context, String name, int total_bullets) {
		super(context, name, total_bullets);
		current_bullets = max_bullets;
	}

	@Override
	public int[] framesToNeedToPlay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxBullets() {
		return max_bullets;
	}

	@Override
	public int[] reload() {

		if(total_bullets <= 0 || current_bullets == max_bullets || current_bullets == total_bullets)
			return null;

		if(total_bullets > max_bullets)
			current_bullets = max_bullets;

		else 
			current_bullets = total_bullets;

		return buildDrawables(reload_anim_size, reload_anim_name);
	}

	@Override
	public AnimationDrawable getAnimation(String anim) {

		if(anim.equals("stand"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.srr61_stand_animation);
		
		else if(anim.equals("target"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.srr61_target_animation);
		
		else if(anim.equals("normal"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.srr61_normal_animation);
		
		else if(anim.equals("shoot"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.srr61_shoot_animation);
		
		else if(anim.equals("targetshoot"))
			return (AnimationDrawable)actContext.getResources().getDrawable(R.drawable.srr61_target_shoot_animation);
		
		else
			return null;
	}

	@Override
	public void shoot() {
		
		current_bullets--;
		total_bullets--;
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

}
