package com.example.socket_com;



import java.io.Serializable;
public class GamePacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nickName,password,injured_nickName;
	private boolean hit,connect;
	public GamePacket(String nickName,String password,boolean hit,boolean connect,String injured_nickName){
		this.connect=connect;
		this.hit=hit;
		this.injured_nickName=injured_nickName;
		this.nickName=nickName;
		this.password=password;
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
		return hit;
	}
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	public boolean isConnect() {
		return connect;
	}
	public void setConnect(boolean connect) {
		this.connect = connect;
	}

}
