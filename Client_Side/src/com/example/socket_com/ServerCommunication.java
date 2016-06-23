package com.example.socket_com;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.location.Location;
import android.net.IpPrefix;
import android.os.AsyncTask;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;



/**some notes:
 * 1.the socket to the server is constructed in this manner:
 * player_port<---->server_port
 * the client side is binded to player_port and the server side is binded to the server_port.
 * 
 * 
 * 2.I used semaphore for thread synchronization.
 *  when a packet is sent to server with a request for data(for example: games list) the thread is blocked until the data is received from the server and then semaphore will be released.
 *  
 *  
 *  3.
 */




public class ServerCommunication {


	InetAddress serverIP;
	int serverPort=MainActivity.serverPort;
	DatagramSocket socket=null;




	/*********constructor********************************************************/
	public ServerCommunication(){
		try {
			serverIP=InetAddress.getByName(MainActivity.serverIP);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	/*****************************************************************/



	/************method that sends a packet via socket*****************************************************/
	private String writePacket(GamePacket packet){
		String response="true";

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(packet);
			byte[] data = outputStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverIP, serverPort);
			socket.send(sendPacket);

		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "IOException: " + e.toString();
		}
		//		finally{
		//			if(socket!=null)
		//				socket.close();
		//		}
		return response;
	}
	/*****************************************************************/





	//Threads:
	/*****************************************************************/
	public  class MyClientTask_OpenSocket extends AsyncTask<Void, Void, String> {
	private String response;
		@Override
		protected String doInBackground(Void... arg0) {
			try{
				socket=new DatagramSocket(MainActivity.player.getPlayerPort());
				
			 //   SocketAddress sockaddr = new InetSocketAddress(serverIP, serverPort);
				//socket.connect(sockaddr);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response = "SocketException: " + e.toString();

			}
			
	
			return response;
		}




		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}
	/*****************************************************************/

	

	
	
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
			byte[] incomingData = new byte[1024];
	
			while (running) {

				try {

					DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
					socket.receive(incomingPacket);
					byte[] data = incomingPacket.getData();
					ByteArrayInputStream in = new ByteArrayInputStream(data);
					ObjectInputStream is = new ObjectInputStream(in);
					packet = (GamePacket) is.readObject();


				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				catch (UnknownHostException e) {
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

				processPacket(packet);

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



		private void processPacket(GamePacket packet) {

			if(packet.isConnect()){
				//waking the blocked thread which is used to connect to the server
				MainActivity.connectSem.release();
			}

			else if(packet.isHit()){
				MainActivity.player.Hit(packet.getHitArea());


				MainActivity.hitSem.release();

			}
			else if(packet.isGetGamesList()){
				MainActivity.gameList=packet.getGamesList();
				//waking the blocked thread which request the data
				MainActivity.getGameListSem.release();
			}
			else if(packet.isGetGameInfo()){
				MainActivity.currentGameTeam1=packet.getTeam1();
				MainActivity.currentGameTeam2=packet.getTeam2();
				MainActivity.currentGameNumOfPlayers=packet.getGameNumOfPlayers();
				//waking the blocked thread which request the data
				MainActivity.getGameInfoSem.release();
			}
			else if(packet.isJoinAGame()){
				MainActivity.team=packet.getTeam();
				//waking the blocked thread which request the data
				MainActivity.joinGameSem.release();
			}
			else if(packet.isTest()){
				MainActivity.testSem.release();
			}
		}

	}

	public synchronized void setDone() {

		MainActivity.flag= true;

		this.notifyAll();
	}

	/*****************************************************************/




	/*****************************************************************/
	//first argument is the parameters to execute() which is passed to doInBackground ,second doesn't matter and third is the return type of doInBackground
	public class MyClientTask_SendPakcet extends AsyncTask<GamePacket, Void, String> {
		String response = "true";

		boolean running=false;
		@Override
		protected String doInBackground(GamePacket... args) {
			response=writePacket((GamePacket)args[0]);
			return response;
		}
		//result is the return data from doInBackground()
		@Override
		protected void onPostExecute(String result) {
			//textResponse.setText(response);
			super.onPostExecute(result);
		}

	}
	/*****************************************************************/


















	//Methods which uses above threads to communicate the server
	/********M***************S*****************************************************/
	/**********E************D*****************************************************/
	/*************T*******O*******************************************************/
	/*****************H***********************************************************/




	/*****************************************************************/
	public String ConnectToServer(String addr, int port,String nickname,String password,GPSLocation loc){
		MyClientTask_SendPakcet connect_thread=new MyClientTask_SendPakcet();
		GamePacket packet=new GamePacket(nickname, password,GamePacket.connect,"",-1);
		packet.setPlayerPort(MainActivity.player.getPlayerPort());
		packet.setPlayer_loc(loc);
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=connect_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get(3000, TimeUnit.MILLISECONDS);
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
	public String JoinGame(String gameName,int team,RGB playerColor){
		MyClientTask_SendPakcet joinGame_thread=new MyClientTask_SendPakcet();
		GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.joinGame, gameName,-1);
		packet.setTeam(team);
		packet.setPlayerColor(playerColor);
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=joinGame_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get(3000, TimeUnit.MILLISECONDS);
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
	public String getGameInfo(String gameName){
		MyClientTask_SendPakcet getGameInfo_thread=new MyClientTask_SendPakcet();
		GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.getGameInfo,  gameName,-1);
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=getGameInfo_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get(3000, TimeUnit.MILLISECONDS);
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
		GamePacket packet =new GamePacket(MainActivity.player.getNickName(),MainActivity.player.getPassword(), GamePacket.disconnect, "", -1);
		String result = "";
		try {
			result=disconnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get(3000, TimeUnit.MILLISECONDS);

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
		GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.getGamesList, "", -1);
		try {
			res=gameList_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get(4000, TimeUnit.MILLISECONDS);
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




	/**
	 * @param f 
	 * @param d 
	 * @param azimut ***************************************************************/
	public String sendHitToServer(int hitArea,RGB color ) {
		MyClientTask_SendPakcet sendHit=new MyClientTask_SendPakcet();
		String res="true";
		try {
			GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.hit, MainActivity.currentGame, hitArea);
			packet.setHitPlayerColor(color);
			res = sendHit.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get(4000, TimeUnit.MILLISECONDS);
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
	public String createNewGame(String newGameName,int numOfPlayers,GPSLocation gameLoc){
		MyClientTask_SendPakcet createNewGame=new MyClientTask_SendPakcet();
		String res="true";
		try {
			GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.createGame, newGameName, -1);
			packet.setGameNumOfPlayers(numOfPlayers);
			packet.setGame_loc(gameLoc);
			res = createNewGame.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get();
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
		(new MyClientTask_ListenToPakcets()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		return;
	}
	
	
	
	
	/*****************************************************************/
	public String openSocket(){
		MyClientTask_OpenSocket openSocket_thread=new MyClientTask_OpenSocket();
		String res="true";
		try {
			res=openSocket_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get(4000, TimeUnit.MILLISECONDS);
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

	
	
	
	public String quitGame() {
		MyClientTask_SendPakcet quitGame_thread=new MyClientTask_SendPakcet();
		GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.quitGame, MainActivity.currentGame,-1);
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=quitGame_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get(3000, TimeUnit.MILLISECONDS);
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
		
		
		MainActivity.team=-1;
		MainActivity.isJoinedAGame=false;
		//setting back the life on the player
		MainActivity.player.setLife(100);
		return result;	
		
	}
	/*****************************************************************/

	
	
	
	/*****************************************************************/
	public String sendTestPacket() {
		MyClientTask_SendPakcet thread=new MyClientTask_SendPakcet();
		GamePacket packet=new GamePacket(MainActivity.player.getNickName(), MainActivity.player.getPassword(), GamePacket.testPacket, MainActivity.currentGame,-1);
		String result = "";
		//execute returns the AsyncTask itself and get() returns the result from doInBackground() with timeout
		try {
			result=thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,packet).get(3000, TimeUnit.MILLISECONDS);
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

}
