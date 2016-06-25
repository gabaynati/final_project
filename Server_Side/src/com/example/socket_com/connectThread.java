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
	/*********Constructor*****************************************************/	
	public  connectThread(Server server) throws IOException
	{
		this.server=server;
		socket = new DatagramSocket(Server.serverPort);
	}


	@Override
	public void run()
	{
		while(true)
		{
			/*********connecting to server via socket and listening to packets from it******************/
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
				server.updatePanel();
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


	/*********method that process a game packet*****************************************************/	
	private void processPacket(GamePacket packet,InetAddress IPAddress){
		/*********packet contains a connect request******************/
		if(packet.isConnect()){
			if(!server.isPlayerConnected(packet.getNickName())){

				//creating new Player:
				Player newPlayer=new Player(IPAddress,packet.getPlayerPort(),packet.getNickName(),packet.getPlayer_loc(),packet.getPlayerColor());

				System.out.println("new player ,port=:" + packet.getPlayerPort());
				System.out.println("gps ,lat=:" + packet.getPlayer_loc().getLatitude());
				System.out.println("gps ,lon=:" + packet.getPlayer_loc().getLongitude());

				//adding to server
				newPlayer.setNickName(packet.getNickName());
				newPlayer.setPassword(packet.getPassword());
				server.addPlayer(newPlayer);
				server.addToServerLog(packet.getNickName()+" has just connected!");


				//sending ACK:
				GamePacket ACK_packet=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.connect,"",-1);
				SendPacketThread t=new SendPacketThread(ACK_packet,newPlayer);
				t.start();
			}
		}
		/*********packet contains an Hit event******************/
		else if(packet.isHit()){
			System.out.println("HITTTTT");
			Game game=server.getGameByName(packet.getGameName());
			String hitter_nickName=packet.getNickName();
			server.addToServerLog(game.Hit(hitter_nickName));


			//writing object to the player who got hit
			Player player_=server.getPlayerByNickName(packet.getHitPlayerNickName());
			if(player_==null)
				return;
			else
			{
				GamePacket gotHitPacket=new GamePacket(player_.getNickName(), player_.getPassword(), GamePacket.hit,game.getGameName(),packet.getHitArea());
				SendPacketThread t=new SendPacketThread(gotHitPacket,player_);
				t.start();
			}

		}
		/*********packet contains a disconnect from server request******************/
		else if(packet.isDisconnect()){
			System.out.println("byeeeeeeeee");
			server.playerDisconnected(packet.getNickName());
			server.addToServerLog(packet.getNickName()+" has just disconnected!");
		}
		/*********packet contains a request for game list******************/
		else if(packet.isGetGamesList()){
			GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList,"",-1);
			Vector<String> games=server.getGamesIDsByLocation(server.getPlayerByNickName(packet.getNickName()));
			System.out.println("games: "+games.toString());
			gamesListPacket.setGamesList(games);


			//writing game List to client
			SendPacketThread t=new SendPacketThread(gamesListPacket,server.getPlayerByIP(IPAddress));
			t.start();

		}
		/*********packet is a test packet******************/
		else if(packet.isTest()){
			GamePacket test_packet=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.testPacket,"",-1);

			server.addToServerLog("test packet detected");

			//writing game List to client
			SendPacketThread t=new SendPacketThread(test_packet,server.getPlayerByIP(IPAddress));
			t.start();

		}
		/*********packet contains a request to join a game******************/
		else if(packet.isJoinAGame()){
			System.out.println(packet.getPlayerColor().toString());
			Player player=server.getPlayerByNickName(packet.getNickName());
			player.setPlayerColor(packet.getPlayerColor());
			server.addPlayerToGame(player, packet.getGameName(),packet.getTeam());
			//sending ACK:
			GamePacket joinGamePacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.joinGame,packet.getGameName(),-1);
			joinGamePacket.setTeam(packet.getTeam());
			Game game_=server.getGameByName(packet.getGameName());
			if(game_.isGameFull()){
				if(game_.getGamePlayersColors(packet.getNickName())!=null){
					System.out.println(game_.getGamePlayersColors(packet.getNickName()));
					joinGamePacket.setGamePlayersColors(game_.getGamePlayersColors(packet.getNickName()));
				}
			}
			server.updatePanel();
			SendPacketThread t=new SendPacketThread(joinGamePacket,server.getPlayerByIP(IPAddress));
			t.start();
		}
		/*********packet contains a request to create new game at the server******************/
		else if(packet.isCreateNewGame()){
			server.addGame(new Game(packet.getGameName(),packet.getGameNumOfPlayers(),packet.getGame_loc()));
			server.addToServerLog("new game: "+packet.getGameName()+"has created by: "+packet.getNickName());



			//sending new game list
			GamePacket gamesListPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGamesList,"",-1);
			gamesListPacket.setGamesList(Main.server.getGamesIDsByLocation(server.getPlayerByNickName(packet.getNickName())));
			SendPacketThread t=new SendPacketThread(gamesListPacket,server.getPlayerByIP(IPAddress));
			t.start();

		}
		/*********packet contains a request for getting the info about a game******************/
		if(packet.isGetGameInfo()){
			GamePacket gamesInfoPacket=new GamePacket(packet.getNickName(),packet.getPassword(), GamePacket.getGameInfo,"",-1);
			Game game=Main.server.getGameByName(packet.getGameName());
			System.out.println("game info:"+packet.getGameName());
			gamesInfoPacket.setTeam1(game.getTeam1PlayersNickNames());
			gamesInfoPacket.setTeam2(game.getTeam2PlayersNickNames());
			gamesInfoPacket.setGameNumOfPlayers(game.getNumOfPlayers());

			//writing game List to client
			SendPacketThread t=new SendPacketThread(gamesInfoPacket,server.getPlayerByIP(IPAddress));
			t.start();
		}
		/*********packet contains a request to quit a game******************/
		if(packet.isQuitGame()){
			server.quitGame(packet.getNickName(), packet.getGameName());
			server.updatePanel();	
		}



	}

}