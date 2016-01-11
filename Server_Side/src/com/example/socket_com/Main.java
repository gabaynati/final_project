package com.example.socket_com;
import java.awt.DisplayMode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

import javax.swing.JFrame;


public class Main {
	
	private static int serverPort = Integer.parseInt("9009");

	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Server server=new Server(serverPort);
		
		
		
		JFrame j=new JFrame();
	

		j.add(server.getPanel());
		j.setSize(822, 973);
		j.setVisible(true);
		j.show();
		//serverLogs.add("server started");


		try
		{
			Thread t = new  connectThread(server);
			t.start();

		}catch(IOException e)
		{
			e.printStackTrace();
		}


		
		
		
/*
		Socket sock = new Socket();
		game.addPlayer(new Player(sock,"nati"));
		game.addPlayer(new Player(sock,"gili"));
		
		System.out.println(game.getSocketByNickName("gili").toString());
		
		
		
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
