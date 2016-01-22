/*
 * Player class
 * 
 * represent the player that correct play on this device
 */

package com.example.socket_com;

public class Player {

	private final int maxLife = 100;
	private final int upperBody_hit = 50;
	private final int lowerBody_hit = 25;
	private final int FACE_HIT = 1, UPPER_BODY_HIT = 2, LOWER_BODY_HIT = 3;
	private int life;
	private String rank;
	private int current_weapon;
	private Weapon[] weapons;
	private int gameScore;
	private int killCount;
	private int ammunition;
	private String nickName;
	private String password;


	public Player(Weapon[] weaponsList){

		life = maxLife;
		current_weapon = 0;
		weapons = weaponsList;
	}

	public Player(String nickName,String password){

		life = maxLife;
		current_weapon = 0;
		gameScore=0;
		killCount=0;
		this.nickName=nickName;
		this.password=password;
		ammunition=30;
	}
	public void Hit(int hitArea){
		if(hitArea == UPPER_BODY_HIT)
			this.life = this.life - this.upperBody_hit;
		else if(hitArea == LOWER_BODY_HIT)
			this.life = this.life - this.lowerBody_hit;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public int getCurrent_weapon() {
		return current_weapon;
	}

	public void setWeapons(Weapon[] weapons) {
		this.weapons = weapons;
	}

	public int getGameScore() {
		return gameScore;
	}

	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}

	public int getKillCount() {
		return killCount;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	public int getAmmunition() {
		return ammunition;
	}

	public void setAmmunition(int ammunition) {
		this.ammunition = ammunition;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public Weapon[] getWeapons(){
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

	public String getPassword() {
		return this.password;
	}
}
