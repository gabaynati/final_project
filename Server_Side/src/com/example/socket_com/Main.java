package com.example.socket_com;
import java.io.IOException;


public class Main {
	public static Game game=new Game();
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
		

		
	//	GameDB.addPlayer("natifdfdfdfdfredefdefdd", "fdfdfd", "nati@gmail.com");
		//gameDB.connect();
		//System.out.println(gameDB.isExists("natdfdfgf"));
	}

}
