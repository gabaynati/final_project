package com.example.socket_com;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Vector;

public class Game {
	private Team team1;
	private Team team2;
	private int team1_numOfPlayers=0,team2_numOfPlayers=0;
	private String gameName;
	private Vector<Player> players;
	private int numOfPlayers;
	private GPSLocation loc;
	
	
	public GPSLocation getLoc() {
		return loc;
	}
	public void setLoc(GPSLocation loc) {
		this.loc = loc;
	}
	/*********Constructor*****************************************************/
	public Game(String gameName,int numOfPlayers,GPSLocation loc){
		players=new Vector<Player>();
		team1=new Team();
		team2=new Team();
		this.gameName=gameName;
		this.numOfPlayers=numOfPlayers;
		this.loc=loc;

	}
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	public void setNumOfPlayers(int numOfPlayers) {
		this.numOfPlayers = numOfPlayers;
	}
	/*********method returns this game name*****************************************************/
	public String getGameName(){
		return this.gameName;
	}
	/*********method that add a player to team1*****************************************************/
	public void addPlayerToTeam1(Player player){
		player.setTeam(team1);
		team1_numOfPlayers++;
	}
	/*********method that add a player to team2*****************************************************/
	public void addPlayerToTeam2(Player player){
		player.setTeam(team2);
		team2_numOfPlayers++;
	}
	/*********method returns whether a player is connected to this game*****************************************************/
	public boolean isConnected(String playerNickname){
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getNickName().equals(playerNickname))
				return true;
		return false;	



	}
	/*********method that removes a player from the server*****************************************************/	
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
	/*********method that adds a player to this game*****************************************************/
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
	/*********method that removes a player from the game*****************************************************/	
	public void quitGame(String player){
		for(int i=0;i<players.size();i++){
			if(players.elementAt(i).getNickName().equals(player)){
				players.removeElementAt(i);
			}
		}
	}
	/*********method that returns team1 players*****************************************************/	
	public Vector<Player> getTeam1Players(){
		Vector<Player> team1Players=new Vector<Player>();
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getTeam()!=null &&players.elementAt(i).getTeam().equals(team1))
				team1Players.add(players.elementAt(i));
		return team1Players;
	}
	/*********method that returns team2 players*****************************************************/	
	public Vector<Player> getTeam2Players(){
		Vector<Player> team2Players=new Vector<Player>();
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getTeam()!=null &&players.elementAt(i).getTeam().equals(team2))
				team2Players.add(players.elementAt(i));
		return team2Players;


	}
	/*********method that returns team1 players nicknames*****************************************************/	
	public Vector<String> getTeam1PlayersNickNames(){
		Vector<String> team1Players=new Vector<String>();
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getTeam()!=null &&players.elementAt(i).getTeam().equals(team1))
				team1Players.add(players.elementAt(i).getNickName());
		return team1Players;
	}
	/*********method that returns team1 players*****************************************************/	
	public Vector<String> getTeam2PlayersNickNames(){
		Vector<String> team2Players=new Vector<String>();
		for(int i=0;i<players.size();i++)
			if(players.elementAt(i).getTeam()!=null &&players.elementAt(i).getTeam().equals(team2))
				team2Players.add(players.elementAt(i).getNickName());
		return team2Players;
	}
	/*********method that prints hit event info*****************************************************/	
	public String Hit(String Hitman_nickName){
		String print="";
		print="hit detected";
		return print;

	}

	public HashMap<String,RGB> getGamePlayersColors(){
	      HashMap<String, RGB> hmap = new HashMap<String, RGB>();
	      for(Player p: players){
	    	  hmap.put(p.getNickName(), p.getPlayerColor());
	      }
	      return hmap;
	}
	public boolean isGameFull(){
		return this.players.size()==this.numOfPlayers;
	}

}
