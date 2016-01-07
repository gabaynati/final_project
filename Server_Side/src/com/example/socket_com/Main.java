package com.example.socket_com;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFrame;


public class Main {
	public static Game game=new Game();
	public static ServerInterface panel;
	public static  Vector<String> serverLogs= new Vector<String>();
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		serverLogs.add("server started");
		JFrame j=new JFrame();
		panel=new ServerInterface();
	
		j.add(panel);
		j.setSize(822, 973);
		j.setVisible(true);
		j.show();
		serverLogs.add("server started");

		int serverPort = Integer.parseInt("9005");
		
		try
		{
			Thread t = new  connectThread(serverPort);
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
