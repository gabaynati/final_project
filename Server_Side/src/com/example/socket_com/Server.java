package com.example.socket_com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Vector;

public class Server {
	private Vector<Game> games;
	private ServerInterface panel;
	private  Vector<String> serverLogs;
	private Vector<Player> players;
	public static int serverPort;
	public static final int gameMaxRadius=1000;

	/*********constructor*****************************************************/	
	public Server(int serverPort){



		this.players=new Vector<Player>();
		this.games=new Vector<Game>();
		this.serverLogs=new Vector<String>();
		displayServerInformation(serverPort);
		this.serverPort=serverPort;
//		Game game1=new Game("game 1",3,new GPSLocation(0, 0));
//		try {
//			game1.addPlayer(new Player(InetAddress.getByName("192.168.2.1"), 4343, "David",new GPSLocation(0, 0)),1);
//			game1.addPlayer(new Player(InetAddress.getByName("192.168.2.2"), 4343, "Nadav",new GPSLocation(0, 0)),2);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}



	//	addGame(game1);
//		for(int i=0;i<10;i++){
//			addGame(new Game("game "+ i,4));
//		}
		this.panel=new ServerInterface(this);
	}
	/*********method that adds a player to the server*****************************************************/	
	public boolean addPlayer(Player player){
		for (int i=0;i<players.size();i++)
			if(player.getNickName().equals(players.elementAt(i).getNickName()))
				return false;
		players.add(player);
		return true;

	}
	/*********method returns whether a player is connected to ther server*****************************************************/	
	public boolean isPlayerConnected(String player){
		for (int i=0;i<players.size();i++)
			if(player.equals(players.elementAt(i).getNickName()))
				return true;
		return false;

	}
	/*********method that adds new game to the server*****************************************************/	
	public boolean addGame(Game game){
		for(int i=0;i<games.size();i++)
			if(games.elementAt(i).getGameName().equals(game.getGameName()))
				return false;

		this.games.add(game);
		return true;
	}
	/*********method removes a player from the server*****************************************************/	
	public void playerDisconnected(String player_nickname){
		for (int i=0;i<players.size();i++)
			if(player_nickname.equals(players.elementAt(i).getNickName())){
				if(getPlayerByNickName(player_nickname).getCurrentGame()!=null)
					players.elementAt(i).getCurrentGame().playerDisconnected(player_nickname);
				players.removeElementAt(i);
			}
	}
	/*********method that remove a player from a game*****************************************************/	
	public void quitGame(String player_nickname,String GameName){
		getGameByName(GameName).quitGame(player_nickname);
		for(int i=0;i<players.size();i++){
			if(players.elementAt(i).getNickName().equals(player_nickname)){
				players.elementAt(i).setCurrentGame(null);
			}
		}
	}
	/*********method that adds a player to a game*****************************************************/	
	public void addPlayerToGame(Player player,String gameName,int team){
		if(isPlayerJoinedAGame(player))
			return;
		Game game=getGameByName(gameName);
		if(game!=null)
			game.addPlayer(player,team);
	}
	public boolean isPlayerJoinedAGame(Player player){
		for(int i=0;i<games.size();i++)
			if(games.elementAt(i).equals(player.getCurrentGame()))
				return true;
		return false;

	}
	/*********method that returns Game object by its name field*****************************************************/	
	public Game getGameByName(String gameName){
		for(int i=0;i<games.size();i++){
			if(games.elementAt(i).getGameName().equals(gameName))
				return games.elementAt(i);
		}
		return null;
	}
	/*********method that adds a new log to the server log*****************************************************/	
	public void addToServerLog(String str){
		if(serverLogs.size()==20){
			serverLogs.removeAllElements();
			this.serverLogs.add("  ");
		}

		this.serverLogs.add(str);
		this.panel.update();
	}
	/*********method that updates the panel*****************************************************/	
	public void updatePanel(){
		this.panel.update();
	}
	/*********method that displays server info*****************************************************/	
	public void displayServerInformation(int port){
		serverLogs.add("Server started!");


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
	/*********method that returns player IP by its name*****************************************************/	
	public InetAddress getPlayerIPByNickName(String nickName) {
		for (int i=0;i<players.size();i++)
			if(nickName.equals(players.elementAt(i).getNickName()))
				return players.elementAt(i).getIP();
		return null;
	}
	/*********method that returns player by its nickname*****************************************************/	
	public Player getPlayerByNickName(String nickName) {
		for (int i=0;i<players.size();i++)
			if(nickName.equals(players.elementAt(i).getNickName()))
				return players.elementAt(i);
		return null;
	}
	/*********method that returns a player's object by its IP*****************************************************/	
	public Player getPlayerByIP(InetAddress player_IP) {
		for (int i=0;i<players.size();i++)
			if(player_IP.equals(players.elementAt(i).getIP()))
				return players.elementAt(i);
		return null;
	}
	/*********Getters & Setters*****************************************************/	
	public int getServerPort(){
		return this.serverPort;
	}
	public Vector<Game> getGames() {
		return games;
	}
	public Vector<Game> getGamesByLocation(GPSLocation Playerloc) {
		Vector<Game> gameByLoc=new Vector<Game>();
		for(Game i: games){
			if(isDistanceOKBetweenPlayerToGame(i.getLoc(), Playerloc))
				gameByLoc.add(i);
		}
		return gameByLoc;
	}
	public Vector<String> getGamesIDsByLocation(Player player){
		GPSLocation Playerloc=player.getLoc();
		Vector<String> gamesIDs=new Vector<String>();
		for(Game i : games)
			if(isDistanceOKBetweenPlayerToGame(i.getLoc(), Playerloc))
				gamesIDs.add(i.getGameName());
		return gamesIDs;

	}
	public ServerInterface getPanel() {
		return panel;
	}
	public Vector<String> getServerLogs() {
		return serverLogs;
	}
	public Vector<Player> getPlayers(){
		return this.players;
	}



	public boolean isDistanceOKBetweenPlayerToGame(GPSLocation gameLoc,GPSLocation playerLoc){

		return distance(gameLoc,playerLoc)<=gameMaxRadius;
	}



	private static double distance(GPSLocation one,GPSLocation two) {
		double lat1,lon1, lat2, lon2;
		lat1=one.getLatitude();
		lon1=one.getLongitude();
		lat2=two.getLatitude();
		lon2=two.getLongitude();

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1.609344;


		return (dist)*1000;
	}
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
