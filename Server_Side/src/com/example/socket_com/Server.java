package com.example.socket_com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

public class Server {
	private Vector<Game> games;
	private ServerInterface panel;
	private  Vector<String> serverLogs;
	private int serverPort;
	public Server(int serverPort){
		this.games=new Vector<Game>();
		this.serverLogs=new Vector<String>();
		displayServerInformation(serverPort);
		this.serverPort=serverPort;
		addGame(new Game("game 1"));
		this.panel=new ServerInterface(this);
	}
	public void addGame(Game game){
		this.games.add(game);
	}
	
	public int getServerPort(){
		return this.serverPort;
	}
	//getters
	public Vector<Game> getGames() {
		return games;
	}
	public Game getGameByName(String gameName){
		for(int i=0;i<games.size();i++){
			if(games.elementAt(i).getGameName().equals(gameName))
				return games.elementAt(i);
		}
		return null;
	}
	public ServerInterface getPanel() {
		return panel;
	}

	public Vector<String> getServerLogs() {
		return serverLogs;
	}
	public void addToServer(String str){
		this.serverLogs.addElement(str);
		this.panel.update();
	}
	
	
	public void displayServerInformation(int port){
		serverLogs.add("Server started!");
		//serverLogs.add("server startedgffffffffffffffggfgfffffffffffffffffffffff");

		
		//getting the public ip of the server
		URL whatismyip;
		try {
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			String ip = in.readLine(); //you get the IP as a String

			//printing the server ip address
			serverLogs.add("Server IP address:"+ip+", port number:"+port);   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
