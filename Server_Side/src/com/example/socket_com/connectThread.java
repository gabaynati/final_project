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
	private DatagramSocket socket;
	//private DatagramSocket serverSocket=server.serverSocket;
	public  connectThread(Server server) throws IOException
	{
		this.server=server;
		//serverSocket.setSoTimeout(100000000);
		socket = new DatagramSocket(Server.serverPort);
	}


	@Override
	public void run()
	{
		while(true)
		{
			try
			{


				//waiting for a packet
				byte[] incomingData = new byte[1024];
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);	
				//busy wait until 
				socket.receive(incomingPacket);





				//converting the object to array of bytes
				byte[] data = incomingPacket.getData();


				ByteArrayInputStream in = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(in);
				GamePacket packet;

				//reading "packet" object from client

				packet=(GamePacket)is.readObject();



				InetAddress IPAddress = incomingPacket.getAddress();
				int port = incomingPacket.getPort();
				System.out.println(packet.getNickName());
				System.out.println(IPAddress);
				System.out.println(port);
				System.out.println(packet.getType());






				//adding new player
				if(packet.isConnect()){
					if(!server.isPlayerConnected(packet.getNickName())){
						//printing the client IP address
						//server.getServerLogs().add("Just connected to "+ socket.getRemoteSocketAddress());

						//creating new Player:
						Player newPlayer=new Player(IPAddress,port,"");


						//adding to server
						newPlayer.setNickName(packet.getNickName());
						newPlayer.setPassword(packet.getPassword());
						server.addPlayer(newPlayer);
						server.getServerLogs().add(packet.getNickName()+" has just connected!");
						server.getPanel().update();

						//sending ACK:
						GamePacket ACK_packet=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.connect, "","",-1);
						SendPacketThread t=new SendPacketThread(ACK_packet,newPlayer);
						t.start();
						/*
						//sending gameList:
						GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList, "","",-1);
						gamesListPacket.setGamesList(Main.server.getGamesIDs());
						SendPacketThread t1=new SendPacketThread(gamesListPacket,newPlayer);
						t1.start();
						*/

					}
				}
				else if(packet.isHit()){
					System.out.println("HITTTTT");
					Game game=server.getGameByName(packet.getGameName());
					String hitter_nickName=packet.getNickName();
					String injured_nickName=packet.getInjured_nickName();
					Main.server.addToServer(game.Hit(hitter_nickName, injured_nickName));
					Player injured_player=server.getPlayerByNickName(injured_nickName);



					//writing object to the injured player
					if(injured_player!=null){
						GamePacket gotHitPacket=new GamePacket(hitter_nickName, "", GamePacket.hit, injured_nickName,game.getGameName(),packet.getHitArea());
						SendPacketThread t=new SendPacketThread(gotHitPacket,injured_player);
						t.start();
					}

				}

				else if(packet.isDisconnect()){
					System.out.println("byeeeeeeeee");
					server.playerDisconnected(packet.getNickName());
					Main.server.getServerLogs().add(packet.getNickName()+" has just disconnected!");
					Main.server.getPanel().update();
					//this.player.getSocket().close();
				}
				else if(packet.isGetGamesList()){
					GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList, "","",-1);
					gamesListPacket.setGamesList(Main.server.getGamesIDs());



					//writing game List to client
					SendPacketThread t=new SendPacketThread(gamesListPacket,server.getPlayerByIP(IPAddress));
					t.start();

				}
				else if(packet.isJoinAGame()){
					//System.out.println(packet.getGameName());
					//if(player==null)
					//	return;
					server.addPlayerToGame(server.getPlayerByNickName(packet.getNickName()), packet.getGameName());

					//System.out.println(server.getGameByName(packet.getGameName()).getPlayersSockets().firstElement().toString());
				}
				else if(packet.isCreateNewGame()){
					server.addGame(new Game(packet.getGameName()));
					Main.server.getServerLogs().add("new game: "+packet.getGameName()+"has created by: "+packet.getNickName());
					Main.server.getPanel().update();
					
					
					//sending new gameList
//					GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList, "","",-1);
//					gamesListPacket.setGamesList(Main.server.getGamesIDs());
//					for(int i=0;i<Main.server.getGamesIDs().size();i++)
//						System.out.println(Main.server.getGamesIDs().elementAt(i));
//					SendPacketThread t=new SendPacketThread(gamesListPacket,server.getPlayerByIP(IPAddress));
//					t.start();
				}
				if(packet.isGetGameInfo()){
					GamePacket gamesInfoPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList, "","",-1);
					Game game=Main.server.getGameByName(packet.getGameName());
					System.out.println("game info:"+packet.getGameName());
					gamesInfoPacket.setTeam1(game.getTeam2PlayersNickNames());
					gamesInfoPacket.setTeam2(game.getTeam2PlayersNickNames());
					/*
					for(int i=0;i<game.getTeam2PlayersNickNames().size();i++)
						System.out.println(game.getTeam2PlayersNickNames().elementAt(i));
					for(int i=0;i<game.getTeam1PlayersNickNames().size();i++)
						System.out.println(game.getTeam1PlayersNickNames().elementAt(i));
*/

					//writing game List to client
					SendPacketThread t=new SendPacketThread(gamesInfoPacket,server.getPlayerByIP(IPAddress));
					t.start();
				}
					
			}




			//server.close();
			catch(SocketTimeoutException s)
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