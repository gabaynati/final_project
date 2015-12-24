package com.example.socket_com;
public class Game {
	private Team team1;
	private Team team2;
	public Game(){
		team1=new Team();
		team2=new Team();
	}
	public void addPlayerToTeam1(Player player){
		team1.addPlayer(player);
	}
	public void addPlayerToTeam2(Player player){
		team2.addPlayer(player);
	}
	public String toString(){
		String str="";
		str+=team1.toString()+"\n"+team2.toString();
		return str;
		
	}
}
