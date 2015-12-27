package com.example.socket_com;

import java.net.SocketAddress;
import java.util.Vector;

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
	public void addPlayer(Player player){
		int team1NumOfPlayers=team1.getNumOfPlayers();
		int team2NumOfPlayers=team2.getNumOfPlayers();
		if(team1NumOfPlayers==team2NumOfPlayers)
			addPlayerToTeam1(player);
		else if(team1NumOfPlayers>team2NumOfPlayers){
			addPlayerToTeam2(player);
		}
		else if(team2NumOfPlayers>team1NumOfPlayers){
			addPlayerToTeam1(player);
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
	public SocketAddress getAddressByNickName(String nickName){
		if(team1.getAddressByNickName(nickName)!=null)
			return team1.getAddressByNickName(nickName);
		else if(team2.getAddressByNickName(nickName)!=null)
			return team2.getAddressByNickName(nickName);
		else
			return null;
		
	}
	public void Hit(String Hitman_nickName,String injured_nickName){
		SocketAddress hitman_address=getAddressByNickName(Hitman_nickName);
		SocketAddress injured_address=getAddressByNickName(injured_nickName);
		
		String print="hit detected:\n"+Hitman_nickName +":"+hitman_address.toString()+"\n"
				+"shot "+injured_nickName+":"+injured_address.toString();
		System.out.println(print);

	}
}
