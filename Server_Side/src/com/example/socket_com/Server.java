package com.example.socket_com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

public class Server {
	private Vector<Game> games;
	private ServerInterface panel;
	private  Vector<String> serverLogs;
	private Vector<Player> players;
	private int serverPort;
	public Server(int serverPort){
		this.players=new Vector<Player>();
		this.games=new Vector<Game>();
		this.serverLogs=new Vector<String>();
		displayServerInformation(serverPort);
		this.serverPort=serverPort;
		addGame(new Game("game 1"));
		addGame(new Game("game 2"));
		addGame(new Game("game 3"));
		addGame(new Game("game 4"));
		addGame(new Game("game 5"));
		addGame(new Game("game 6"));
		addGame(new Game("game 7"));
		addGame(new Game("game 8"));
		addGame(new Game("game 9"));
		addGame(new Game("game 10"));
		addGame(new Game("game 11"));
		addGame(new Game("game 12"));
		addGame(new Game("game 13"));
		addGame(new Game("game 14"));
		addGame(new Game("game 15"));
		this.panel=new ServerInterface(this);
	}
	public boolean addPlayer(Player player){
		for (int i=0;i<players.size();i++)
			if(player.getNickName().equals(players.elementAt(i).getNickName()))
				return false;
		players.add(player);
		return true;

	}
	public boolean isPlayerConnected(String player){
		for (int i=0;i<players.size();i++)
			if(player.equals(players.elementAt(i).getNickName()))
				return true;
		return false;

	}
	public boolean addGame(Game game){
		for(int i=0;i<games.size();i++)
			if(games.elementAt(i).getGameName().equals(game.getGameName()))
				return false;

		this.games.add(game);
		return true;
	}
	public void playerDisconnected(String player_nickname,String player_gameName){
		for (int i=0;i<players.size();i++)
			if(player_nickname.equals(players.elementAt(i).getNickName())){
				if(getPlayerByNickName(player_nickname).getCurrentGame()!=null)
					players.elementAt(i).getCurrentGame().playerDisconnected(player_nickname);
				players.removeElementAt(i);
			}


	}
	public int getServerPort(){
		return this.serverPort;
	}
	//getters
	public Vector<Game> getGames() {
		return games;
	}
	public void addPlayerToGame(Player player,String gameName){
		if(isPlayerJoinedAGame(player))
			return;
		Game game=getGameByName(gameName);
		if(game!=null)
			game.addPlayer(player);
	}
	public boolean isPlayerJoinedAGame(Player player){
		for(int i=0;i<games.size();i++)
			if(games.elementAt(i).equals(player.getCurrentGame()))
				return true;
		return false;

	}
	public Vector<String> getGamesIDs(){
		Vector<String> gamesIDs=new Vector<String>();
		for(int i=0;i<games.size();i++)
			gamesIDs.add(games.elementAt(i).getGameName());
		return gamesIDs;

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
	public Socket getPlayerSocketByNickName(String nickName) {
		for (int i=0;i<players.size();i++)
			if(nickName.equals(players.elementAt(i).getNickName()))
				return players.elementAt(i).getSocket();
		return null;
	}
	public Player getPlayerByNickName(String nickName) {
		for (int i=0;i<players.size();i++)
			if(nickName.equals(players.elementAt(i).getNickName()))
				return players.elementAt(i);
		return null;
	}
	public Player getPlayerBySocket(Socket player_socket) {
		for (int i=0;i<players.size();i++)
			if(player_socket.equals(players.elementAt(i).getSocket()))
				return players.elementAt(i);
		return null;
	}

}
