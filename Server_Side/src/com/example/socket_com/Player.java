package com.example.socket_com;
import java.net.SocketAddress;
public class Player {
	private SocketAddress address;
	private String nickName;
	private int life;
	private int gameScore;
	private int killCount;
	@SuppressWarnings("unused")
	private int ammunition;
	public Player(SocketAddress socketAddress,String nickName){
		this.address=socketAddress;
		life=100;
		gameScore=0;
		killCount=0;
		this.nickName=nickName;
		ammunition=30;
	}
//	
//	public void createLocalDB(){
//		SQLiteDatabse mydatabase = openOrCreateDatabase("your database name",MODE_PRIVATE,null);
//	}
//	
	
	
	public String toString(){
		String str="";
		str+="address: "+this.address.toString() +"\n"
	         +"nickName: "+ this.nickName;
		return str;
	}
	public SocketAddress getAddress() {
		return address;
	}
	public void setAddress(SocketAddress address) {
		this.address = address;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
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
	
}
