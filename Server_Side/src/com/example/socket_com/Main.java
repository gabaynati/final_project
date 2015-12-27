package com.example.socket_com;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.NetworkChannel;

import javax.swing.JFrame;


public class Main {
	public static Game game=new Game();
	public static ServerInterface panel;
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

	
		JFrame j=new JFrame();
		panel=new ServerInterface();
	
		j.add(panel);
		j.setSize(1300, 750);
		j.setVisible(true);
		j.show();

		int registerPort = Integer.parseInt("9008");
		
		try
		{
			Thread t = new  connectThread(registerPort);
			t.start();

		}catch(IOException e)
		{
			e.printStackTrace();
		}


		 
		/*
		try {
			Main.game.addPlayer(new Player(Inet4Address.getLocalHost(), "fdfdfd"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		

	

		//gameDB.connect();
		//System.out.println(gameDB.isExists("natdfdfgf"));
	}

}
