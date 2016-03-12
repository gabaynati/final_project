package com.example.socket_com;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SendPacketThread extends Thread  {
	private GamePacket packet;
	private Player player;
	public SendPacketThread(GamePacket packet,Player player){
		this.packet=packet;
		this.player=player;
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
			ObjectOutputStream outToClient = player.getObjectOutputStream();
			outToClient.writeObject(packet);
			outToClient.flush();
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
