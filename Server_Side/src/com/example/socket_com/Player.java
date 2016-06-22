package com.example.socket_com;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
public class Player {
	private InetAddress ipAddr;
	private int port;
	private String nickName,password;
	private Team team;
	private Game currentGame=null;
	private GPSLocation loc;
	private RGB playerColor;
	public GPSLocation getLoc() {
		return loc;
	}
	public void setLoc(GPSLocation loc) {
		this.loc = loc;
	}
	/*********Constructor*****************************************************/	
	public Player(InetAddress ipAddr,int port,String nickName,GPSLocation loc,RGB color){
		this.ipAddr=ipAddr;
		this.port=port;
	
		this.nickName=nickName;
		this.loc=loc;
		this.playerColor=color;

	}
	/*********Getters and Setters*****************************************************/	
	public InetAddress getIP(){
		return this.ipAddr;
	}
	public String getNickName() {
		return nickName;
	}
	public String getPassword() {
		return password;
	}

	public Team getTeam() {
		return team;
	}
	public Game getCurrentGame() {
		return currentGame;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}
	public int getPort() {
		return this.port;
		// TODO Auto-generated method stub
	}
	public RGB getPlayerColor() {
		return playerColor;
	}
	public void setPlayerColor(RGB playerColor) {
		this.playerColor = playerColor;
	}



}
