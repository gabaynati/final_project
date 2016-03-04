package com.example.socket_com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.example.socket_com.ServerCommunication.MyClientTask_ListenToPakcets;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ServerCommunication {
	public static final int hit=0,connect=1,getGamesList=2,createGame=3,disconnect=4;



	public  class MyClientTask_ListenToPakcets extends AsyncTask<Void, Void, String> {


		String response = "";
		boolean running=true;
		private void setRunning(){
			running=!running;
		}
		@Override
		protected String doInBackground(Void... arg0) {

			GamePacket packet = null;
			while(running){

				//reading "packet" object from client
				try {
					ObjectInputStream inFromClient = new ObjectInputStream(MainActivity.socket.getInputStream());
					packet=(GamePacket) inFromClient.readObject();

				} 
				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response = "ClassNotFoundException: " + e.toString();

					return response;
				}catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response = "UnknownHostException: " + e.toString();
					return response;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					response = "IOException: " + e.toString();
					return response;
				}
				catch(Exception e){
					e.printStackTrace();
					response = "Exception: " + e.toString();
					return response;
				}



				if(packet.isHit()){
					MainActivity.player.Hit(packet.getHitArea());
					GameInterface.hitRecvied();

				}
				/*
				if(packet.isGetGamesList()){
					FindGameActivity.gameList=packet.getGamesList();
				}*/
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
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}






	public class MyClientTask_SendPakcet extends AsyncTask<GamePacket, Void, String> {


		String response = "true";
	

		@Override
		protected String doInBackground(GamePacket... arg0) {


			try {

				//writing object
				ObjectOutputStream outToServer = new ObjectOutputStream(MainActivity.socket.getOutputStream());
				outToServer.writeObject((GamePacket)arg0[0]);
				/*
				//writing texts
				DataOutputStream out = new DataOutputStream(MainActivity.socket.getOutputStream());
				out.writeUTF("I am Client");
				 */

			}

			catch (UnknownHostException e) {
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
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}

	public class MyClientTask_getGamesList extends AsyncTask<Void, Void, GamePacket> {


		String response = "success";
		GamePacket packet;
		GamePacket gameListPacket=null;

		public void setPacket(GamePacket packet){
			this.packet=packet;
		}

		@Override
		protected GamePacket doInBackground(Void... arg0) {


			try {

				//requesting game list from server
				ObjectOutputStream outToServer = new ObjectOutputStream(MainActivity.socket.getOutputStream());
				outToServer.writeObject(packet);
				/*
				//writing texts
				DataOutputStream out = new DataOutputStream(MainActivity.socket.getOutputStream());
				out.writeUTF("I am Client");
				 */
				//reading the game list from server
				ObjectInputStream inFromClient = new ObjectInputStream(MainActivity.socket.getInputStream());
				gameListPacket=(GamePacket) inFromClient.readObject();
				
			}

			catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			return gameListPacket;
		}

		@Override
		protected void onPostExecute(GamePacket result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}
	public class MyClientTask_Disconnect extends AsyncTask<Void, Void, String> {


		String response = "You have logged out",nickname,password;
		GamePacket packet;


		public MyClientTask_Disconnect(){
			this.nickname=MainActivity.player.getNickName();
			this.password=MainActivity.player.getPassword();


		}
		@Override
		protected String doInBackground(Void... arg0) {


			try {
			//	if(!MainActivity.socket.isConnected()){
				//	return "Not connected to server";
			//	}
				packet =new GamePacket(this.nickname,this.password, disconnect, "", "game 1", -1);
				//writing object
				ObjectOutputStream outToServer = new ObjectOutputStream(MainActivity.socket.getOutputStream());
				outToServer.writeObject(packet);
			}

			catch (UnknownHostException e) {
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
		@Override
		protected void onPostExecute(String result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}


	//first argument is the parameters to execute() which is passed to doInBackground ,second doesn't matter and third is the return type of doInBackground
	public class MyClientTask_Connect extends AsyncTask<Boolean, Void, String> {

		private String dstAddress;
		private int dstPort;
		private String response = "true";
		private String nickname,password;
		private boolean isConnetionSucceded;


		public MyClientTask_Connect(String addr, int port,String nickname,String password){
			this.dstAddress = addr;
			this.dstPort = port;
			this.nickname=nickname;
			this.password=password;
		}

		@Override
		protected String doInBackground(Boolean... arg0) {
			try {

				/**
				String isExists=GameDB.isExists(nickname,password);
				if(!isExists.equals("exists")){
					//textResponse.setText(isExists);
				}
				 */
				
				MainActivity.socket = new Socket(dstAddress, dstPort);
				ObjectOutputStream outToServer = new ObjectOutputStream(MainActivity.socket.getOutputStream());
				outToServer.writeObject(new GamePacket(nickname, password,GamePacket.connect,null,"",-1));


			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "IOException: " + e.toString();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "Exception: " + e.toString();
			}

			/*finally{
					if(socket != null){
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			 */

			return response;
		}

		//result is the return data from doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		public String getResponse(){
			return this.response;
		}
		public boolean isConnetionSucceded(){
			return this.isConnetionSucceded;
		}



	}



	
	public class MyClientTask_JoinGame extends AsyncTask<String, Void, String> {


		String response = "true";
		
		@Override
		protected String doInBackground(String... arg0) {


			try {

				//writing object
				ObjectOutputStream outToServer = new ObjectOutputStream(MainActivity.socket.getOutputStream());
				GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.joinGame, "", (String)arg0[0],-1);
				outToServer.writeObject(packet);
				/*
				//writing texts
				DataOutputStream out = new DataOutputStream(MainActivity.socket.getOutputStream());
				out.writeUTF("I am Client");
				 */

			}

			catch (UnknownHostException e) {
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
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

		

	}
	
	
	
	
	
	
	
	
	/**##########################################################################*/
	
	
	public boolean JoinGame(String gameName){
		MyClientTask_JoinGame joinGame_thread=new MyClientTask_JoinGame();
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=joinGame_thread.execute(gameName).get(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.equals("true");
	}
	
	
	
	
	
	

	public boolean ConnectToServer(String addr, int port,String nickname,String password){
		MyClientTask_Connect connect=new MyClientTask_Connect(addr,port, nickname,password);
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=connect.execute().get(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		//means that 
		if(result.equals("true")){
			MyClientTask_ListenToPakcets listener=new MyClientTask_ListenToPakcets();
			listener.execute();
		}*/
		return result.equals("true");
	}

	public  MyClientTask_ListenToPakcets getServerListener(){
		return new MyClientTask_ListenToPakcets();
	}

	public  MyClientTask_ListenToPakcets get(){
		return new MyClientTask_ListenToPakcets();
	}

	public MyClientTask_SendPakcet getServerDataSender(){
		return new MyClientTask_SendPakcet();
	}
	public String disconnectFromServer(){
		MyClientTask_Disconnect disconnect=new MyClientTask_Disconnect();
		String result = "";
		try {
			result=disconnect.execute().get(4000, TimeUnit.MILLISECONDS);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			result="InterruptedException:"+ e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			result="ExecutionException:"+ e.toString();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			result="TimeoutException:"+ e.toString();
		}
		Log.d("DDDDDD:","in disconnected: "+result);

		return result;
	}
	
	
	public Vector<String> getGameList(){
		GamePacket res = null;
		MyClientTask_getGamesList gameList_thread=new MyClientTask_getGamesList();
		gameList_thread.setPacket(new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), getGamesList,"", "", -1));
		try {
			res=gameList_thread.execute().get(4000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//res="InterruptedException:"+ e.toString();
			//Log.d("TAG:",res);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			//res="ExecutionException:"+ e.toString();
			//Log.d("TAG:",res);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			//res="TimeoutException:"+ e.toString();
			//Log.d("TAG:",res);
		}
		if(res==null){
			return null;
		}
		return res.getGamesList();
	}

	public String setServerListener(){
		MyClientTask_ListenToPakcets serverListener=new MyClientTask_ListenToPakcets();
		String res="true";
		try {
			res = serverListener.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res="InterruptedException: "+e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res="ExecutionExceptionn: "+e.toString();
		}
		return res;
		
	}
	public String sentHitToServer(String injured_nickname,int hitArea){
		MyClientTask_SendPakcet sendHit=new MyClientTask_SendPakcet();
		String res="true";
		try {
			GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.hit, injured_nickname, MainActivity.currentGame, hitArea);
			res = sendHit.execute(packet).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res="InterruptedException: "+e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res="ExecutionExceptionn: "+e.toString();
		}
		return res;
		
	}
	
	
}
