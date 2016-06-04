package com.example.socket_com;



import java.io.Serializable;
import java.util.Vector;
public class GamePacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int hit=0,connect=1,getGamesList=2,createGame=3,disconnect=4,joinGame=5,getGameInfo=6,quitGame=7,testPacket=8;
	private String nickName,password;
	private int packetType;
	private int playerPort;
	private Vector<String> gamesList;
	private String gameName;
	private Vector<String> gameTeam1Players,gameTeam2Players;
	private int hitArea;
	private int team;
	//GPS COORDINATES:
	private float azimuth;
	public float latitude, longitude;
	
	public GamePacket(String nickName,String password,final int packetType,String gameName,int hitArea){
		this.packetType=packetType;
		this.nickName=nickName;
		this.password=password;
		this.gameName=gameName;
		this.hitArea=hitArea;
		
	}
	
	public void setGPS(float latitude, float longitude){
		this.latitude=latitude;
		this.longitude=longitude;
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


	public boolean isHit() {
		return this.packetType==hit;
	}
	public boolean isTest() {
		return this.packetType==testPacket;
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
	public boolean isQuitGame(){
		return this.packetType==quitGame;
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
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}
	public float getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(float azimuth) {
		this.azimuth = azimuth;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}
