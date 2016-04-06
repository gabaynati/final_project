package com.example.socket_com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
	private String writePacket(GamePacket packet){
		String response="true";
		try{
			DatagramSocket socket=new DatagramSocket();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(packet);
			byte[] data = outputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, player.getIP(), player.getPort()+1);
			socket.send(sendPacket);
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
