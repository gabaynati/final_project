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
	public static Game game=new Game();
	public static ServerInterface panel;
	public static  Vector<String> serverLogs= new Vector<String>();
	private static int serverPort = Integer.parseInt("9008");
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		displayServerInformation(serverPort);
		JFrame j=new JFrame();
		panel=new ServerInterface();

		j.add(panel);
		j.setSize(822, 973);
		j.setVisible(true);
		j.show();
		//serverLogs.add("server started");



		try
		{
			Thread t = new  connectThread(serverPort);
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
	
	public static void displayServerInformation(int port){
		serverLogs.add("server started");
		//getting the public ip of the server
		URL whatismyip;
		try {
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			String ip = in.readLine(); //you get the IP as a String

			//printing the server ip address
			serverLogs.add("server ip address:"+ip+" port number:"+port);   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
