package com.example.socket_com;



import java.io.Serializable;
import java.util.Vector;
public class GamePacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int hit=0,connect=1,getGamesList=2,createGame=3,disconnect=4,joinGame=5,getGameInfo=6;
	private String nickName,password,injured_nickName;
	private int packetType;
	private int playerPort;
	private Vector<String> gamesList;
	private String gameName;
	private Vector<String> gameTeam1Players,gameTeam2Players;
	private int hitArea;
	
	public GamePacket(String nickName,String password,final int packetType,String injured_nickName,String gameName,int hitArea){
		this.packetType=packetType;
		this.injured_nickName=injured_nickName;
		this.nickName=nickName;
		this.password=password;
		this.gameName=gameName;
		this.hitArea=hitArea;
		
	}
	public Vector<String> getTeam1(){
		return this.gameTeam1Players;
	}
	public Vector<String> getTeam2(){
		return this.gameTeam2Players;
	}
	public void setTeam1(Vector<String> team){
		this.gameTeam1Players=team;
	}
	public void setTeam2(Vector<String> team){
		this.gameTeam2Players=team;
	}
	public int getHitArea(){
		return this.hitArea;
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
	public boolean isDisconnect(){
		return this.packetType==disconnect;
	}
	public boolean isJoinAGame(){
		return this.packetType==joinGame;
	}
	public boolean isCreateNewGame(){
		return this.packetType==createGame;
	}
	public boolean isGetGameInfo(){
		return this.packetType==getGameInfo;
	}
	public int getType() {
		// TODO Auto-generated method stub
		return this.packetType;
	}
	public int getPlayerPort() {
		return playerPort;
	}
	public void setPlayerPort(int playerPort) {
		this.playerPort = playerPort;
	}
}
