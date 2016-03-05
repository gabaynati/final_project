package com.example.socket_com;
import java.net.*;

import javax.swing.JPanel;

import java.io.*;
//note: in case that the two computers are on separate lan and are remote, you need to open a port forwarding on 
//your router.
public class ListenToPlayersThread extends Thread
{
	private Socket player_socket;
	private String player_nickname;
	private String player_gameName;
	private Server server;
	

	public  ListenToPlayersThread(Socket player_socket,Server server) throws IOException
	{
		this.player_socket=player_socket;
		this.server=server;
	}

	public void run()
	{
		while(true)
		{

			/*
				//getting the public ip of the server
				URL whatismyip = new URL("http://checkip.amazonaws.com");
				BufferedReader in = new BufferedReader(new InputStreamReader(
						whatismyip.openStream()));
				String ip = in.readLine(); //you get the IP as a String


				//printing the server ip address
				System.out.println("server ip address:"+ip);        	 


				//waiting to connect from client
				System.out.println("Waiting for client on port " +
						serverSocket.getLocalPort() + "...");
				Socket socket = serverSocket.accept();//inf loop until client connects


				//printing the client IP address
				System.out.println("Just connected to "+ socket.getRemoteSocketAddress());



				//note:
				//Each socket has both an OutputStream and an InputStream.
				//The client's OutputStream is connected to the server's InputStream,
				//and the client's InputStream is connected to the server's OutputStream



/*
				//writing hello to client
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				out.writeUTF("You are connected to the server: "+ ip);
			 */



			try
			{
				GamePacket packet = null;

				
				//reading "packet" object from client
				ObjectInputStream inFromClient = new ObjectInputStream(player_socket.getInputStream());
				packet=(GamePacket) inFromClient.readObject();
				player_nickname=packet.getNickName();
				player_gameName=packet.getGameName();
				

				//performing acts on hit event 
				if(packet.isHit()){
					Game game=server.getGameByName(packet.getGameName());
					String hitter_nickName=packet.getNickName();
					String injured_nickName=packet.getInjured_nickName();
					Main.server.addToServer(game.Hit(hitter_nickName, injured_nickName));
					Socket injured_player_socket=server.getPlayerSocketByNickName(injured_nickName);
					//writing object to the injured player
					GamePacket gotHitPacket=new GamePacket(hitter_nickName, "", GamePacket.hit, injured_nickName,game.getGameName(),packet.getHitArea());
					ObjectOutputStream outToServer = new ObjectOutputStream(injured_player_socket.getOutputStream());
					outToServer.writeObject(gotHitPacket);
				}
				if(packet.isDisconnect()){
					System.out.println("exit");
					server.playerDisconnected(player_nickname,player_gameName);
					Main.server.getServerLogs().add(packet.getNickName()+" has just disconnected!");
					Main.server.getPanel().update();
					return;
				}
				if(packet.isGetGamesList()){
					Socket player=server.getPlayerSocketByNickName(packet.getNickName());
					GamePacket gamesList=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList, "","",-1);
					gamesList.setGamesList(Main.server.getGamesIDs());
					System.out.println(Main.server.getGamesIDs().firstElement()+"fffff");
					ObjectOutputStream outToServer = new ObjectOutputStream(player.getOutputStream());
					outToServer.writeObject(gamesList);
				}
				if(packet.isJoinAGame()){
					//System.out.println(packet.getGameName());
					Player player=server.getPlayerByNickName(player_nickname);
					if(player==null)
						return;
					server.addPlayerToGame(player, packet.getGameName());

					//System.out.println(server.getGameByName(packet.getGameName()).getPlayersSockets().firstElement().toString());
				}
				if(packet.isCreateNewGame()){
					server.addGame(new Game(packet.getGameName()));
					Main.server.getServerLogs().add("new game: "+packet.getGameName()+"has created by: "+packet.getNickName());
					Main.server.getPanel().update();
					
				}


			}catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				return;
			}catch (java.io.EOFException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("disconnect: "+player_gameName);
				System.out.println("disconnect: "+player_nickname);
				System.out.println("disconnect: "+server.getGameByName(player_gameName).getGameName());
				server.playerDisconnected(player_nickname,player_nickname);
				Main.server.getServerLogs().add(player_nickname+" has just disconnected!");
				Main.server.getPanel().update();
				return;
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	
			
			
		}
	}

}