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
				System.out.println(packet.getNickName());
				System.out.println(IPAddress);
				System.out.println(packet.getPlayerPort());
				System.out.println(packet.getType());
				processPacket(packet, IPAddress);
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




	private void processPacket(GamePacket packet,InetAddress IPAddress){
		//adding new player
		if(packet.isConnect()){
			if(!server.isPlayerConnected(packet.getNickName())){
				//printing the client IP address
				//server.getServerLogs().add("Just connected to "+ socket.getRemoteSocketAddress());

				//creating new Player:
				Player newPlayer=new Player(IPAddress,packet.getPlayerPort(),"");

				System.out.println("new player ,port=:" + packet.getPlayerPort());
				//adding to server
				newPlayer.setNickName(packet.getNickName());
				newPlayer.setPassword(packet.getPassword());
				server.addPlayer(newPlayer);
				server.getServerLogs().add(packet.getNickName()+" has just connected!");
				server.getPanel().update();

				//sending ACK:
				GamePacket ACK_packet=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.connect,"",-1);
				SendPacketThread t=new SendPacketThread(ACK_packet,newPlayer);
				t.start();



			}
		}
		else if(packet.isHit()){
			System.out.println("HITTTTT");
			Game game=server.getGameByName(packet.getGameName());
			String hitter_nickName=packet.getNickName();
			Main.server.addToServer(game.Hit(hitter_nickName));



			//writing object to all players except the hitter

			GamePacket gotHitPacket=new GamePacket(hitter_nickName, "", GamePacket.hit,game.getGameName(),packet.getHitArea());
			Vector<Player> players=Main.server.getPlayers();
			for(int i=0;i<players.size();i++){
				if(players.elementAt(i).getNickName()!=hitter_nickName){
					SendPacketThread t=new SendPacketThread(gotHitPacket,players.elementAt(i));
					t.start();
				}
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
			GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList,"",-1);
			gamesListPacket.setGamesList(Main.server.getGamesIDs());


			//writing game List to client
			SendPacketThread t=new SendPacketThread(gamesListPacket,server.getPlayerByIP(IPAddress));
			t.start();

		}
		else if(packet.isJoinAGame()){
			server.addPlayerToGame(server.getPlayerByNickName(packet.getNickName()), packet.getGameName(),packet.getTeam());
			//sending ACK:
			GamePacket joinGamePacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.joinGame,packet.getGameName(),-1);
			joinGamePacket.setTeam(packet.getTeam());
			Main.server.getPanel().update();
			SendPacketThread t=new SendPacketThread(joinGamePacket,server.getPlayerByIP(IPAddress));
			t.start();
		}
		else if(packet.isCreateNewGame()){
			server.addGame(new Game(packet.getGameName()));
			Main.server.getServerLogs().add("new game: "+packet.getGameName()+"has created by: "+packet.getNickName());
			Main.server.getPanel().update();
		}
		if(packet.isGetGameInfo()){
			GamePacket gamesInfoPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGameInfo,"",-1);
			Game game=Main.server.getGameByName(packet.getGameName());
			System.out.println("game info:"+packet.getGameName());
			gamesInfoPacket.setTeam1(game.getTeam1PlayersNickNames());
			gamesInfoPacket.setTeam2(game.getTeam2PlayersNickNames());

			//writing game List to client
			SendPacketThread t=new SendPacketThread(gamesInfoPacket,server.getPlayerByIP(IPAddress));
			t.start();
		}
		if(packet.isQuitGame()){
			Main.server.quitGame(packet.getNickName(), packet.getGameName());
			Main.server.getPanel().update();
		}



	}

}