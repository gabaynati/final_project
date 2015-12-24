package com.example.socket_com;
import java.io.Serializable;

public class GamePacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nickName,password;
	public GamePacket(String nickName,String password){
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
}
