package com.example.socket_com;
import java.net.*;

import javax.swing.JPanel;

import java.io.*;
//note: in case that the two computers are on separate lan and are remote, you need to open a port forwarding on 
//your router.
public class ListenToPlayersThread extends Thread
{
	private Player player;
	private String player_gameName;
	private Server server;
	private boolean isInputStreamDefined=false;
	private ObjectInputStream inFromClient;

	public  ListenToPlayersThread(Player player,Server server) throws IOException
	{
		this.player=player;
		this.server=server;
		inFromClient= player.getObjectInputStream();
	}


	@Override
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

				/*
				
				//sending games list
				GamePacket gamesList=new GamePacket(player.getNickName(),player.getPassword(), GamePacket.getGamesList, "","",-1);
				gamesList.setGamesList(Main.server.getGamesIDs());
				Thread sendGameList=new SendPacketThread(gamesList, player.getSocket());
				sendGameList.start();

*/



				
				GamePacket packet = null;
			
			

		
				//reading "packet" object from client
				packet=(GamePacket) inFromClient.readObject();
				player_gameName=packet.getGameName();

				
				
				
				System.out.println(packet.getType()+"type packet");
				//performing acts on hit event 
				if(packet.isHit()){
					System.out.println("HITTTTT");
					Game game=server.getGameByName(packet.getGameName());
					String hitter_nickName=packet.getNickName();
					String injured_nickName=packet.getInjured_nickName();
					Main.server.addToServer(game.Hit(hitter_nickName, injured_nickName));
					Player injured_player=server.getPlayerByNickName(injured_nickName);

					//writing object to the injured player
					if(injured_player!=null){
						GamePacket gotHitPacket=new GamePacket(hitter_nickName, "", GamePacket.hit, injured_nickName,game.getGameName(),packet.getHitArea());
						Thread sendHitPacket=new SendPacketThread(gotHitPacket,injured_player);
						sendHitPacket.start();
						//ObjectOutputStream outToClient = new ObjectOutputStream(injured_player_socket.getOutputStream());
						//outToClient.writeObject(gotHitPacket);
					}
				}
				if(packet.isDisconnect()){
					System.out.println("byeeeeeeeee");
					server.playerDisconnected(player.getNickName(),player_gameName);
					Main.server.getServerLogs().add(packet.getNickName()+" has just disconnected!");
					Main.server.getPanel().update();
					this.player.getSocket().close();
					return;
				}
				if(packet.isGetGamesList()){
					GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList, "","",-1);
					gamesListPacket.setGamesList(Main.server.getGamesIDs());
					//writing game List to client
					Thread sendGameListPacket=new SendPacketThread(gamesListPacket,player);
					sendGameListPacket.start();
					//ObjectOutputStream outToClient = new ObjectOutputStream(player_socket.getOutputStream());
					//outToClient.writeObject(gamesList);
				}
				if(packet.isJoinAGame()){
					//System.out.println(packet.getGameName());
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
//				System.out.println("disconnect: "+player_gameName);
//				System.out.println("disconnect: "+player_nickname);
				server.playerDisconnected(player.getNickName(),player_gameName);
				Main.server.getServerLogs().add(player.getNickName()+" has just disconnected!");
				Main.server.getPanel().update();
				return;
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}



		}
	}

}