package com.example.socket_com;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendPacketThread extends Thread  {
	private GamePacket packet;
	private Socket socket;
	public SendPacketThread(GamePacket packet,Socket socket){
		this.packet=packet;
		this.socket=socket;
	}
	@Override
	public void run()
	{
	
		String res=writePacket(this.packet);
		System.out.println("sendddddd: "+res);
	
	}
	//this method is used to prevent two thread from writing to the socket simultaneously.
	private synchronized String writePacket(GamePacket packet){
		String response="true";
		try{
			ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
			outToClient.writeObject(packet);
		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "UnknownHostException: " + e.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "IOException: " + e.toString();
		}
		return response;
	}

	
}
