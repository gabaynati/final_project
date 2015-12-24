package com.example.socket_com;

import android.content.Context;

public class Player {

	private int life;
	private String rank;
	private int current_weapon;
	private Weapon[] weapons;
	
	public Player(Context context){
		life = 100;
		current_weapon = 0;
		weapons = new Weapon[1];
		
		Weapon m4a1 = new Ak12(context, "m4a1");
		weapons[0] = m4a1;
	}
	
	public int getLife(){
		return life;
	}
	
	public int getCurrentWeapon(){
		return current_weapon;
	}
	
	public Weapon[] getWeaponds(){
		return weapons;
	}
	
	public void setLife(int newLife){
		life = newLife;
	}
	
	/*
	 * set the current weapon index at the weapons array
	 * if i = 1 take the next weapon
	 * if i = -1 take the previous weapon
	 * else do nothing
	 */
	public void setCurrentWeapon(int i){
		
		if(i == 1)
			current_weapon = (current_weapon + i) % weapons.length;
		
		else if(i == -1){
			current_weapon = current_weapon + i;
			if(current_weapon < 0)
				current_weapon = weapons.length - 1;
		}
	}
}
