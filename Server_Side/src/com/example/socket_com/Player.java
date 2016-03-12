package com.example.socket_com;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
public class Player {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
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
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		life=100;
		gameScore=0;
		killCount=0;
		this.nickName=nickName;
		ammunition=30;
	}

	public Socket getSocket() {
		return socket;
	}
	public ObjectOutputStream getObjectOutputStream() {
		return out;
	}
	public ObjectInputStream getObjectInputStream() {
		return in;
	}
	public String getNickName() {
		return nickName;
	}
	public String getPassword() {
		return password;
	}
	public int getLife() {
		return life;
	}
	public int getGameScore() {
		return gameScore;
	}
	public int getKillCount() {
		return killCount;
	}
	public int getAmmunition() {
		return ammunition;
	}
	public Team getTeam() {
		return team;
	}
	public Game getCurrentGame() {
		return currentGame;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public void setObjectOutputStream(ObjectOutputStream out) {
		this.out = out;
	}
	public void setObjectInputStream(ObjectInputStream in) {
		this.in = in;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}
	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}
	public void setAmmunition(int ammunition) {
		this.ammunition = ammunition;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}



}
