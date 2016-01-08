package com.example.socket_com;
import java.net.*;

import javax.swing.JPanel;

import java.io.*;
//note: in case that the two computers are on separate lan and are remote, you need to open a port forwarding on 
//your router.
public class ListenToPlayersThread extends Thread
{
	private Socket player_socket;
	private Game game;

	public  ListenToPlayersThread(Socket player_socket) throws IOException
	{
		this.player_socket=player_socket;
		game=Main.game;
	}

	public void run()
	{
		while(true)
		{
			try
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




				GamePacket packet = null;


				//reading "packet" object from client
				ObjectInputStream inFromClient = new ObjectInputStream(player_socket.getInputStream());
				try {

					packet=(GamePacket) inFromClient.readObject();

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//adding new player
				//if(packet.isConnect()){
				//Main.game.addPlayer(new Player(player_socket,packet.getNickName()));
				//Main.panel.update();
				//System.out.println(packet.getNickName()+"has just connected to the server");
				//System.out.println(packet.getPassword());
				//}
				
				
				//performing acts on hit event
				//else 
				if(packet.isHit()){
					String hitter_nickName=packet.getNickName();
					String injured_nickName=packet.getInjured_nickName();
					game.Hit(hitter_nickName, injured_nickName);
					Socket injured_player_socket=game.getSocketByNickName(injured_nickName);
					//writing object to the injured player
					GamePacket gotHitPacket=new GamePacket(hitter_nickName, "", true, false, injured_nickName);
					ObjectOutputStream outToServer = new ObjectOutputStream(injured_player_socket.getOutputStream());
					outToServer.writeObject(gotHitPacket);
				}


				//System.out.println(Main.game.toString());

				/*
				/////////////////////////
				String response="";
				//reading hello
				ByteArrayOutputStream byteArrayOutputStream = 
						new ByteArrayOutputStream(1024);
				byte[] buffer = new byte[1024];

				int bytesRead;
				InputStream inputStream = server.getInputStream();

//				
//				  notice:
//				  inputStream.read() will block if no data return
//				 
				while ((bytesRead = inputStream.read(buffer)) != -1){
					byteArrayOutputStream.write(buffer, 0, bytesRead);
					response += byteArrayOutputStream.toString("UTF-8");
				}
				System.out.println(response);
				//////////////////////////////

				 */

				//server.close();
			}catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				break;
			}catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
		}
	}

}