package com.example.socket_com;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Vector;

public class Game {
	private Team team1;
	private Team team2;
	private int team1_numOfPlayers=0,team2_numOfPlayers=0;
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
		player.setTeam(team1);
		team1_numOfPlayers++;
	}
	public void addPlayerToTeam2(Player player){
		player.setTeam(team2);
		team2_numOfPlayers++;
	}
	public boolean isConnected(String playerNickname){
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getNickName().equals(playerNickname))
				return true;
		return false;	



	}


	/*
	public Player getPlayerByNickName(String player_nickname){

	}*/
	public void playerDisconnected(String player_nickname){
		//removing player
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getNickName().equals(player_nickname)){
				if(players.elementAt(i).getTeam().equals(team1))
					team1_numOfPlayers--;
				else if(players.elementAt(i).getTeam().equals(team2))
					team2_numOfPlayers--;
				players.removeElementAt(i);
			}

	}
	public void addPlayer(Player player, int team){
		player.setCurrentGame(this);
		if(!isConnected(player.getNickName())){
			this.players.add(player);

			if(team==1)
				addPlayerToTeam1(player);
			else if(team==2)
				addPlayerToTeam2(player);


		}
	}
	public String toString(){
		String str="";
		str+=team1.toString()+"\n"+team2.toString();
		return str;

	}
	public Vector<Player> getTeam1Players(){
		Vector<Player> team1Players=new Vector<Player>();
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getTeam().equals(team1))
				team1Players.add(players.elementAt(i));
		return team1Players;
	}
	public Vector<Player> getTeam2Players(){
		Vector<Player> team2Players=new Vector<Player>();
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getTeam().equals(team2))
				team2Players.add(players.elementAt(i));
		return team2Players;


	}
	public Vector<String> getTeam1PlayersNickNames(){
		Vector<String> team1Players=new Vector<String>();
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getTeam().equals(team1))
				team1Players.add(players.elementAt(i).getNickName());
		return team1Players;
	}
	public Vector<String> getTeam2PlayersNickNames(){
		Vector<String> team2Players=new Vector<String>();
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getTeam().equals(team2))
				team2Players.add(players.elementAt(i).getNickName());
		return team2Players;
	}



	/*
	public Socket getSocketByNickName(String nickName){
		if(team1.getSocketByNickName(nickName)!=null)
			return team1.getSocketByNickName(nickName);
		else if(team2.getSocketByNickName(nickName)!=null)
			return team2.getSocketByNickName(nickName);
		else
			return null;

	}*/
	public String Hit(String Hitman_nickName,String injured_nickName){
		InetAddress hitman_address=getIPByNickName(Hitman_nickName);
		InetAddress injured_address=getIPByNickName(injured_nickName);

		String print="";
		if(hitman_address!=null && injured_address!=null)
			print="hit detected:\n"+Hitman_nickName +" shot "+injured_nickName;
		else
			print="hit detected";
		return print;

	}
	/*
	public Vector<Socket> getPlayersSockets(){
		Vector<Socket> sockets =new Vector<Socket>();
		for(int i=0;i<team1.getPlayers().size();i++){
			sockets.addElement(team1.getPlayers().elementAt(i).getSocket());
		}
		for(int i=0;i<team2.getPlayers().size();i++){
			sockets.addElement(team2.getPlayers().elementAt(i).getSocket());
		}
		return sockets;
	}*/
	/*
	public String printAllSockets(){
		String str="";
		Vector<Socket> sockets=getPlayersSockets();
		for(int i=0;i<sockets.size();i++){
			str+=sockets.elementAt(i).toString()+"\n";
		}
		return str;
	}*/
	/*
	public Player getPlayerByNickName(String player_nickname){
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getNickName().equals(player_nickname)){
				return players.elementAt(i);
			}
		return null;
	}*/

	private InetAddress getIPByNickName(String player_nickName) {
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getNickName().equals(player_nickName))
				return players.elementAt(i).getIP();

		return null;
	}
}
