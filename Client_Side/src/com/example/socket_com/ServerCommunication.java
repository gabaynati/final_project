package com.example.socket_com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;


import android.os.AsyncTask;

public class ServerCommunication {
	
	public  class MyClientTask_ListenToPakcets extends AsyncTask<Void, Void, Void> {


		String response = "";

		@Override
		protected Void doInBackground(Void... arg0) {

			GamePacket packet = null;


			//reading "packet" object from client
			try {
				ObjectInputStream inFromClient = new ObjectInputStream(MainActivity.socket.getInputStream());
				packet=(GamePacket) inFromClient.readObject();

			} 
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			}



			if(packet.isHit()){
				MainActivity.player.Hit();
			}
			/*finally
			{
				if(MainActivity.socket != null){
					try {
						MainActivity.socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}*/
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}






	public class MyClientTask_SendPakcet extends AsyncTask<Void, Void, Void> {


		String response = "";
		GamePacket packet;

		
		public void setPacket(GamePacket packet){
			this.packet=packet;
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {


			try {

				//writing object
				ObjectOutputStream outToServer = new ObjectOutputStream(MainActivity.socket.getOutputStream());
				outToServer.writeObject(packet);
				/*
				//writing texts
				DataOutputStream out = new DataOutputStream(MainActivity.socket.getOutputStream());
				out.writeUTF("I am Client");
				 */

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			}
			/*finally
			{
				if(MainActivity.socket != null){
					try {
						MainActivity.socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}*/
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}
	
	
	

	public MyClientTask_ListenToPakcets getServerListener(){
		return new MyClientTask_ListenToPakcets();
	}

	
	public MyClientTask_SendPakcet getServerDataSender(){
		return new MyClientTask_SendPakcet();
	}
	
}
