package com.example.socket_com;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.Vector;

public class Game {
	private Team team1;
	private Team team2;
	private String gameName;
	private Vector<Player> players;
	public Game(String gameName){
		players=new Vector<Player>();
		team1=new Team();
		team2=new Team();
		this.gameName=gameName;
	}

	public String getGameName(){
		return this.gameName;
	}
	public void addPlayerToTeam1(Player player){
		team1.addPlayer(player);
	}
	public void addPlayerToTeam2(Player player){
		team2.addPlayer(player);
	}
	public boolean isConnected(String playerNickname){
		return players.contains(playerNickname);
	}
	public void playerDisconnected(String player_nickname){
		Player player=getPlayerByNickName(player_nickname);
		if(player==null)
			return;
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getNickName().equals(player.getNickName())){
				players.removeElementAt(i);
				if(player.getTeam()==1)
					this.team1.removePlayer(player);
				else if(player.getTeam()==2)
					this.team2.removePlayer(player);
			}

	}
	public void addPlayer(Player player){
		if(!isConnected(player.getNickName())){
			this.players.add(player);
			int team1NumOfPlayers=team1.getNumOfPlayers();
			int team2NumOfPlayers=team2.getNumOfPlayers();
			if(team1NumOfPlayers==team2NumOfPlayers){
				player.setTeam(1);
				addPlayerToTeam1(player);
			}
			else if(team1NumOfPlayers>team2NumOfPlayers){
				player.setTeam(2);
				addPlayerToTeam2(player);
			}
			else if(team2NumOfPlayers>team1NumOfPlayers){
				player.setTeam(1);
				addPlayerToTeam1(player);
			}
		}
	}
	public String toString(){
		String str="";
		str+=team1.toString()+"\n"+team2.toString();
		return str;

	}
	public Vector<Player> getTeam1Players(){
		return this.team1.getPlayers();
	}
	public Vector<Player> getTeam2Players(){
		return this.team2.getPlayers();
	}
	public Socket getSocketByNickName(String nickName){
		if(team1.getSocketByNickName(nickName)!=null)
			return team1.getSocketByNickName(nickName);
		else if(team2.getSocketByNickName(nickName)!=null)
			return team2.getSocketByNickName(nickName);
		else
			return null;

	}
	public String Hit(String Hitman_nickName,String injured_nickName){
		Socket hitman_address=getSocketByNickName(Hitman_nickName);
		Socket injured_address=getSocketByNickName(injured_nickName);

		String print="";
		if(hitman_address!=null && injured_address!=null)
			print="hit detected:\n"+Hitman_nickName +" shot "+injured_nickName;
		else
			print="hit detected";
		return print;

	}
	public Vector<Socket> getPlayersSockets(){
		Vector<Socket> sockets =new Vector<Socket>();
		for(int i=0;i<team1.getPlayers().size();i++){
			sockets.addElement(team1.getPlayers().elementAt(i).getSocket());
		}
		for(int i=0;i<team2.getPlayers().size();i++){
			sockets.addElement(team2.getPlayers().elementAt(i).getSocket());
		}
		return sockets;
	}
	public String printAllSockets(){
		String str="";
		Vector<Socket> sockets=getPlayersSockets();
		for(int i=0;i<sockets.size();i++){
			str+=sockets.elementAt(i).toString()+"\n";
		}
		return str;
	}
	public Player getPlayerByNickName(String player_nickname){
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getNickName().equals(player_nickname)){
				return players.elementAt(i);
			}
		return null;
	}
}
