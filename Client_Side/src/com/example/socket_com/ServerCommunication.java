package com.example.socket_com;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



import android.os.AsyncTask;
import android.util.Log;


public class ServerCommunication {
	public static final int hit=0,connect=1,getGamesList=2,createGame=3,disconnect=4;

	public static boolean isOutputStreamOpened=false,isInputStreamOpened=false;
	public static ObjectOutputStream outToServer;
	//this method is used to prevent two thread from writing to the socket simultaneously.
	private synchronized String writePacket(GamePacket packet){
		String response="true";


		//Opening output stream
		if(!isOutputStreamOpened){
			try {
				outToServer = new ObjectOutputStream(MainActivity.out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isOutputStreamOpened=true;
		}




		try{
			outToServer.writeObject(packet);
		}catch (UnknownHostException e) {
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





	//Threads:
	/*****************************************************************/
	public  class MyClientTask_ListenToPakcets extends AsyncTask<Void, Void, String> {


		String response = "true";
		boolean running=true;
		public void setRunning(){
			running=!running;
		}
		@Override
		protected String doInBackground(Void... arg0) {

			GamePacket packet = null;

			ObjectInputStream inFromClient = null;
			//Opening input stream
			if(!isInputStreamOpened){
				try {
					inFromClient= new ObjectInputStream(MainActivity.in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isInputStreamOpened=true;
			}


			while(running){

				//reading "packet" object from client
				try {
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
				if(packet.isGetGamesList()){
					MainActivity.gameList=packet.getGamesList();
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
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}
	/*****************************************************************/




	/*****************************************************************/
	public class MyClientTask_SendPakcet extends AsyncTask<GamePacket, Void, String> {


		String response = "true";


		@Override
		protected String doInBackground(GamePacket... args) {
			response=writePacket((GamePacket)args[0]);
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}
	/*****************************************************************/





	/*****************************************************************/
	//first argument is the parameters to execute() which is passed to doInBackground ,second doesn't matter and third is the return type of doInBackground
	public class MyClientTask_Connect extends AsyncTask<Boolean, Void, String> {

		private String dstAddress;
		private int dstPort;
		private String response = "true";
		private String nickname,password;


		public MyClientTask_Connect(String addr, int port,String nickname,String password){
			this.dstAddress = addr;
			this.dstPort = port;
			this.nickname=nickname;
			this.password=password;
		}

		@Override
		protected String doInBackground(Boolean... arg0) {
			try {
				//initializing socket properties
				MainActivity.socket = new Socket(dstAddress, dstPort);
				MainActivity.in=MainActivity.socket.getInputStream();
				MainActivity.out=MainActivity.socket.getOutputStream();


				GamePacket packet=new GamePacket(nickname, password,GamePacket.connect,"","",-1);

				response=writePacket(packet);

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
			/************/
			//setting serverListener
			//setlistener();
			/************/


		}

	}
	/*****************************************************************/














	//Methods which uses above threads to communicate server
	/********M***************S*****************************************************/
	/**********E************D*****************************************************/
	/*************T*******O*******************************************************/
	/*****************H***********************************************************/



	/*****************************************************************/
	public String JoinGame(String gameName){
		MyClientTask_SendPakcet joinGame_thread=new MyClientTask_SendPakcet();
		GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.joinGame, "", gameName,-1);
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=joinGame_thread.execute(packet).get(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			result="InterruptedException: "+e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			result="InterruptedException: "+e.toString();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			result="InterruptedException: "+e.toString();
		}

		return result;
	}
	/*****************************************************************/






	/*****************************************************************/
	public String ConnectToServer(String addr, int port,String nickname,String password){
		MyClientTask_Connect connect=new MyClientTask_Connect(addr,port, nickname,password);
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=connect.execute().get(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			result="InterruptedException: "+e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			result="InterruptedException: "+e.toString();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			result="InterruptedException: "+e.toString();
		}

		return result;
	}
	/*****************************************************************/





	/*****************************************************************/
	public String disconnectFromServer(){
		MyClientTask_SendPakcet disconnect=new MyClientTask_SendPakcet();
		GamePacket packet =new GamePacket(MainActivity.player.getNickName(),MainActivity.player.getPassword(), GamePacket.disconnect, "", "game 1", -1);
		String result = "";
		try {
			result=disconnect.execute((GamePacket)packet).get(4000, TimeUnit.MILLISECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			result="InterruptedException:"+ e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			result="ExecutionException:"+ e.toString();
		}
		catch (TimeoutException e) {
			// TODO Auto-generated catch block
			result="TimeoutException:"+ e.toString();
		}
		//Log.d("DDDDDD:","in disconnected: "+result);

		return result;
	}
	/*****************************************************************/




	/*****************************************************************/
	public String sendGameListRequest(){
		String res = null;
		MyClientTask_SendPakcet gameList_thread=new MyClientTask_SendPakcet();
		GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), getGamesList,"", "", -1);
		try {
			res=gameList_thread.execute(packet).get(4000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			res="InterruptedException:"+ e.toString();
			//Log.d("TAG:",res);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			res="ExecutionException:"+ e.toString();
			//Log.d("TAG:",res);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			res="TimeoutException:"+ e.toString();
			//Log.d("TAG:",res);
		}
		return res;
	}
	/*****************************************************************/




	/*****************************************************************/
	public String sendHitToServer(String injured_nickname,int hitArea) {
		MyClientTask_SendPakcet sendHit=new MyClientTask_SendPakcet();
		String res="true";
		try {
			GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.hit, injured_nickname, MainActivity.currentGame, hitArea);
			res = sendHit.execute(packet).get(4000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res="InterruptedException: "+e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res="ExecutionException: "+e.toString();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res="TimeoutException: "+e.toString();
		}
		return res;

	}
	/*****************************************************************/




	/*****************************************************************/
	public String createNewGame(String newGameName){
		MyClientTask_SendPakcet createNewGame=new MyClientTask_SendPakcet();
		String res="true";
		try {
			GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.createGame, "", newGameName, -1);
			res = createNewGame.execute(packet).get();
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
	/*****************************************************************/




	/*****************************************************************/
	public void setlistener(){
		(new MyClientTask_ListenToPakcets()).execute();
		return;
	}
	/*****************************************************************/





}
