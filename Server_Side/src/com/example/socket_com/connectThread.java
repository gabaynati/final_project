package com.example.socket_com;
import java.net.*;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import java.io.*;
//note: in case that the two computers are on separate lan and are remote, you need to open a port forwarding on 
//your router.
public class connectThread extends Thread
{


	private Server server;
	DatagramSocket socket;
	//private DatagramSocket serverSocket=server.serverSocket;
	public  connectThread(Server server) throws IOException
	{
		this.server=server;
		//serverSocket.setSoTimeout(100000000);
		socket = new DatagramSocket(Server.serverPort);
	}

	public void run()
	{
		while(true)
		{
			try
			{

				byte[] incomingData = new byte[1024];


				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);

				socket.receive(incomingPacket);

				byte[] data = incomingPacket.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(in);
				GamePacket packet = null;

				//reading "packet" object from client

				packet=(GamePacket)is.readObject();



				InetAddress IPAddress = incomingPacket.getAddress();
				int port = incomingPacket.getPort();
				System.out.println(packet.getNickName());
				System.out.println(IPAddress);
				System.out.println(port);




				/*
				//waiting to connect from clients
				Socket socket = serverSocket.accept();//inf loop until client connects
				//note:
				//Each socket has both an OutputStream and an InputStream.
				//The client's OutputStream is connected to the server's InputStream,
				//and the client's InputStream is connected to the server's OutputStream
				 */




				//adding new player
				if(packet.isConnect()){
					if(!server.isPlayerConnected(packet.getNickName())){
						//printing the client IP address
						//server.getServerLogs().add("Just connected to "+ socket.getRemoteSocketAddress());

						Player newPlayer=new Player(IPAddress,port,"");


						//adding to server
						newPlayer.setNickName(packet.getNickName());
						newPlayer.setPassword(packet.getPassword());
						server.addPlayer(newPlayer);
						server.getServerLogs().add(packet.getNickName()+" has just connected!");
						server.getPanel().update();


						//sending gameList:
						GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList, "","",-1);
						gamesListPacket.setGamesList(Main.server.getGamesIDs());
						//writing game List to client					
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						ObjectOutputStream os = new ObjectOutputStream(outputStream);
						os.writeObject(gamesListPacket);
						data = outputStream.toByteArray();
						DatagramPacket sendPacket = new DatagramPacket(data, data.length,IPAddress, port);
						socket.send(sendPacket);
						
						
						
						/*
						//starting listener thread for the specific player which has just connected
						Thread t = new ListenToPlayersThread(newPlayer,server);
						t.start();
						 */
					}
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
							ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
							ObjectOutputStream os = new ObjectOutputStream(outputStream);
							os.writeObject(gotHitPacket);
							data = outputStream.toByteArray();
							DatagramPacket sendPacket = new DatagramPacket(data, data.length,IPAddress, port);
							socket.send(sendPacket);
						}

					}
					/*
					if(packet.isDisconnect()){
						System.out.println("byeeeeeeeee");
						server.playerDisconnected(player.getNickName(),player_gameName);
						Main.server.getServerLogs().add(packet.getNickName()+" has just disconnected!");
						Main.server.getPanel().update();
						//this.player.getSocket().close();
						return;
					}*/
					if(packet.isGetGamesList()){
						GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList, "","",-1);
						gamesListPacket.setGamesList(Main.server.getGamesIDs());
						
						
						
						//writing game List to client
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						ObjectOutputStream os = new ObjectOutputStream(outputStream);
						os.writeObject(gamesListPacket);
						data = outputStream.toByteArray();
						DatagramPacket sendPacket = new DatagramPacket(data, data.length,IPAddress, port);
						socket.send(sendPacket);

					}
					if(packet.isJoinAGame()){
						//System.out.println(packet.getGameName());
						//if(player==null)
						//	return;
						server.addPlayerToGame(server.getPlayerByNickName(packet.getNickName()), packet.getGameName());

						//System.out.println(server.getGameByName(packet.getGameName()).getPlayersSockets().firstElement().toString());
					}
					if(packet.isCreateNewGame()){
						server.addGame(new Game(packet.getGameName()));
						Main.server.getServerLogs().add("new game: "+packet.getGameName()+"has created by: "+packet.getNickName());
						Main.server.getPanel().update();

					}
				}




				//server.close();
			}catch(SocketTimeoutException s)
			{
				server.getServerLogs().add("Socket timed out!");
				server.getPanel().update();
				break;
			}catch(IOException e)
			{
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}