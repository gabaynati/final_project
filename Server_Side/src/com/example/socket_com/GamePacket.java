package com.example.socket_com;
import java.io.Serializable;
import java.util.Vector;

public class GamePacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int hit=0,connect=1,getGamesList=2;
	private String nickName,password,injured_nickName;
	private int packetType;
	private Vector<String> gamesList;
	private String gameName;
	
	
	public GamePacket(String nickName,String password,final int packetType,String injured_nickName,String gameName){
		this.packetType=packetType;
		this.injured_nickName=injured_nickName;
		this.nickName=nickName;
		this.password=password;
		this.gameName=gameName;
	}
	public String getGameName(){
		return this.gameName;
	}
	public void setGamesList(Vector<String> games){
		this.gamesList=games;
	}
	public Vector<String> getGamesList(){
		return this.gamesList;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getInjured_nickName() {
		return injured_nickName;
	}
	public void setInjured_nickName(String injured_nickName) {
		this.injured_nickName = injured_nickName;
	}
	public boolean isHit() {
		return this.packetType==hit;
	}
	
	public boolean isConnect() {
		return this.packetType==connect;
	}
	public boolean isGetGamesList(){
		return this.packetType==getGamesList;
	}
	
}
