package com.example.socket_com;
import java.net.Socket;
import java.net.SocketAddress;
public class Player {
	private Socket socket;
	private String nickName,password;
	private int life;
	private int gameScore;
	private int killCount;
	@SuppressWarnings("unused")
	private int ammunition;
	private Team team;
	private Game currentGame=null;
	public Player(Socket socket,String nickName){
		this.socket=socket;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String newPassword) {
		this.password=newPassword;
	}
	public Game getGame(){
		return this.currentGame;
	}
	public void setGame(Game game){
		this.currentGame=game;
	}
	public String toString(){
		String str="";
		str+="address: "+this.socket.toString() +"\n"
	         +"nickName: "+ this.nickName;
		return str;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Team getTeam(){
		return this.team;
	}
	public void setTeam(Team team){
		this.team=team;
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
