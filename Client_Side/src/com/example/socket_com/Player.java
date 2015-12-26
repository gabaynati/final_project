package com.example.socket_com;

public class Player {

	private final int maxLife = 100;
	private int life;
	private String rank;
	private int current_weapon;
	private Weapon[] weapons;
	
	public Player(Weapon[] weaponsList){
		
		life = maxLife;
		current_weapon = 0;
		weapons = weaponsList;
	}
	
	public int getMaxLife(){
		return maxLife;
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
