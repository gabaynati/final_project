package com.example.socket_com;
import java.awt.DisplayMode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

import javax.swing.JFrame;


public class Main {
	
	private static int serverPort = Integer.parseInt("9002");
	public static Server server=new Server(serverPort);
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
	
		
		
		
		JFrame j=new JFrame();
	

		j.add(server.getPanel());
		j.setSize(840, 973);
		j.setVisible(true);
		j.show();


		try
		{
			Thread t = new  connectThread(server);
			t.start();

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	


}
