package com.example.socket_com;
import java.io.IOException;

import javax.swing.JFrame;


public class Main {
	public static Game game=new Game();
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		int registerPort = Integer.parseInt("9008");
		
		try
		{
			Thread t = new  brodcastUpdateThread(registerPort);
			t.start();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		

		JFrame j=new JFrame();
		ServerInterface panel=new ServerInterface();
		j.add(panel);
		j.setSize(822, 850);
		j.setVisible(true);
		j.show();
		
		
	//	GameDB.addPlayer("natifdfdfdfdfredefdefdd", "fdfdfd", "nati@gmail.com");
		//gameDB.connect();
		//System.out.println(gameDB.isExists("natdfdfgf"));
	}

}
