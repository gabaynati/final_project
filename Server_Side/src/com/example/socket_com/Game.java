package com.example.socket_com;

import java.net.Socket;
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
	public Socket getSocketByNickName(String nickName){
		if(team1.getSocketByNickName(nickName)!=null)
			return team1.getSocketByNickName(nickName);
		else if(team2.getSocketByNickName(nickName)!=null)
			return team2.getSocketByNickName(nickName);
		else
			return null;
		
	}
	public void Hit(String Hitman_nickName,String injured_nickName){
		Socket hitman_address=getSocketByNickName(Hitman_nickName);
		Socket injured_address=getSocketByNickName(injured_nickName);
		
		String print="";
		if(hitman_address!=null && injured_address!=null)
		print="hit detected:\n"+Hitman_nickName +":"+hitman_address.toString()+"\n"
		            +"shot "+injured_nickName+":"+injured_address.toString();
		else
			print="hit detected";
		System.out.println(print);

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
}
