package com.example.socket_com;
import java.net.*;

import javax.swing.JPanel;

import java.io.*;
//note: in case that the two computers are on separate lan and are remote, you need to open a port forwarding on 
//your router.
public class ListenToPlayersThread extends Thread
{
	private Socket player_socket;
	private Server server;
	private Game game;

	public  ListenToPlayersThread(Socket player_socket,Server server) throws IOException
	{
		this.player_socket=player_socket;
		this.server=server;
	}

	public void run()
	{
		Player res=null;
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

				res=server.getPlayerBySocket(player_socket);
				//reading "packet" object from client
				ObjectInputStream inFromClient = new ObjectInputStream(player_socket.getInputStream());
				packet=(GamePacket) inFromClient.readObject();


				//adding new player
				//if(packet.isConnect()){
				//Main.game.addPlayer(new Player(player_socket,packet.getNickName()));
				//Main.panel.update();
				//System.out.println(packet.getNickName()+"has just connected to the server");
				//System.out.println(packet.getPassword());
				//}
				game=server.getGameByName(packet.getGameName());
				//performing acts on hit event 
				if(packet.isHit()){
					String hitter_nickName=packet.getNickName();
					String injured_nickName=packet.getInjured_nickName();
					Main.server.addToServer(game.Hit(hitter_nickName, injured_nickName));
					Socket injured_player_socket=game.getSocketByNickName(injured_nickName);
					//writing object to the injured player
					GamePacket gotHitPacket=new GamePacket(hitter_nickName, "", GamePacket.hit, injured_nickName,game.getGameName(),packet.getHitArea());
					ObjectOutputStream outToServer = new ObjectOutputStream(injured_player_socket.getOutputStream());
					outToServer.writeObject(gotHitPacket);
				}
				if(packet.isDisconnect()){
					System.out.println("exit");
					game.playerDisconnected(packet.getNickName());
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
					Socket player_socket=server.getPlayerSocketByNickName(packet.getNickName());
					Player player=new Player(player_socket, packet.getNickName());
					player.setGame(packet.getGameName());
					server.addPlayerToGame(player, packet.getGameName());

					//System.out.println(server.getGameByName(packet.getGameName()).getPlayersSockets().firstElement().toString());
				}



			}catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				return;
			}catch (java.io.EOFException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				server.playerDisconnected(res);
				Main.server.getServerLogs().add(res.getNickName()+" has just disconnected!");
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