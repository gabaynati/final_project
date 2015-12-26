package com.example.socket_com;
import java.io.IOException;

import javax.swing.JFrame;


public class Main {
	public static Game game=new Game();
	public static ServerInterface panel;
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		JFrame j=new JFrame();
		panel=new ServerInterface();
		j.add(panel);
		j.setSize(822, 850);
		j.setVisible(true);
		j.show();
		
		int registerPort = Integer.parseInt("9004");
		
		try
		{
			Thread t = new  brodcastUpdateThread(registerPort);
			t.start();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		

		
		
	//	GameDB.addPlayer("natifdfdfdfdfredefdefdd", "fdfdfd", "nati@gmail.com");
		//gameDB.connect();
		//System.out.println(gameDB.isExists("natdfdfgf"));
	}

}
