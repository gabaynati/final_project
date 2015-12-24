package com.example.socket_com;

import java.util.Vector;

public class Team {
	Vector<Player> players;
	int totalScore;
	
	public String toString(){
		String str="";
		for(int i=0;i<players.size();i++){
			str+=players.elementAt(i).toString()+"\n";
		}
			return str;
	}
	
	public Team(){
		players=new Vector<Player>();
		totalScore=0;
	}
	public void addPlayer(Player player){
		players.add(player);
	}
	public int getTotalScore(){
		return this.totalScore;
	}
	public Vector<Player> getPlayers(){
		return this.players;
	}
	
}
