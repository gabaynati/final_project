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
	private ServerSocket serverSocket;

	private Server server;
	public  connectThread(Server server) throws IOException
	{
		this.server=server;
		serverSocket = new ServerSocket(server.getServerPort());
		serverSocket.setSoTimeout(100000000);

	}

	public void run()
	{
		while(true)
		{
			try
			{



				//waiting to connect from clients
				Socket socket = serverSocket.accept();//inf loop until client connects
				//note:
				//Each socket has both an OutputStream and an InputStream.
				//The client's OutputStream is connected to the server's InputStream,
				//and the client's InputStream is connected to the server's OutputStream

				GamePacket packet = null;
				Player newPlayer=new Player(socket,"");


				//reading "packet" object from client
				try {
					packet=(GamePacket) newPlayer.getObjectInputStream().readObject();

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//adding new player
				if(packet.isConnect()){
					if(!server.isPlayerConnected(packet.getNickName())){
						//printing the client IP address
						//server.getServerLogs().add("Just connected to "+ socket.getRemoteSocketAddress());
						
						
						
						//adding to server
						newPlayer.setNickName(packet.getNickName());
						newPlayer.setPassword(packet.getPassword());
						server.addPlayer(new Player(socket,packet.getNickName()));
						server.getServerLogs().add(packet.getNickName()+" has just connected!");
						server.getPanel().update();
								
						
					
						
						//starting listener thread for the specific player which has just connected
						Thread t = new ListenToPlayersThread(newPlayer,server);
						t.start();
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
			}
		}
	}

}