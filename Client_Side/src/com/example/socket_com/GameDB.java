package com.example.socket_com;

import java.sql.*;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
//you need to install JDBC: download it and then go to Properties, go to Java Build Path, select Libraries tab, and Add External JARs.
public class GameDB {
	// Create a variable for the connection string.

	// Declare the JDBC objects.
	public static Connection con = null;
	public static Statement stmt = null;
	public static ResultSet rs = null;
	public static String dbConnectionString="jdbc:jtds:sqlserver://hns.database.windows.net:1433/hns;database=hns;integratedSecurity=true;user=hns;password=Seattle12";


	
	
	
	
	
	
	//Threads which communicate with DB
	/********M***************S*****************************************************/
	/**********E************D*****************************************************/
	/*************T*******O*******************************************************/
	/*****************H***********************************************************/
	/*****************************************************************/
	public static class getPlayerInfoFromDBThread extends AsyncTask<String, Void, Vector<Object>> {



		@Override
		protected Vector<Object> doInBackground(String... arg0) {
			Vector<Object> res=null;
			try {
				
				// Establish the connection.
				Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(dbConnectionString,"","");

				// Create and execute an SQL statement that returns some data.
				String SQL = "select * from Players where player_nickname='"+(String)arg0[0]+"'";
				stmt = con.createStatement();
				rs=stmt.executeQuery(SQL);
				if(rs.next()){
					res=new Vector<Object>();
					res.add(rs.getString(1));
					res.add(rs.getString(2));
					res.add(rs.getString(3));
					res.add(rs.getInt(4));
					res.add(rs.getInt(5));
				}

			}
			// Handle any errors that may have occurred.
			catch (Exception e) {
				e.printStackTrace();
				Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
				return null;
			}
			finally {
				if (rs != null) try { rs.close(); } catch(Exception e) {}
				if (stmt != null) try { stmt.close(); } catch(Exception e) {}
				if (con != null) try { con.close(); } catch(Exception e) {}
			}
			return res;

		}

		@Override
		protected void onPostExecute(Vector<Object> result) {
			super.onPostExecute(result);
		}

	}
	/*****************************************************************/

	

	/*****************************************************************/
	public static class registerToDBThread extends AsyncTask<String, Void, String> {


		String res;

		@Override
		protected String doInBackground(String... arg0) {
			String dbMessage="";
			dbMessage=addPlayer((String)arg0[0], (String)arg0[0], (String)arg0[0]);
			if(dbMessage.equals("success")){
				res="Registration has completed successfully!\nYou are now redirected to H&S Menu";
			}
			else
				res="there was an error: "+dbMessage;

			return res;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		private String addPlayer(String nickname,String password,String email){
		
			try {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				// Establish the connection.
				Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(dbConnectionString,"","");

				// Create and execute an SQL statement that returns some data.
				String SQL = "INSERT INTO dbo.Players VALUES ('"+nickname+"','"+password+"','"+email+"',0,0);";
				stmt = con.createStatement();
				stmt.executeUpdate(SQL);
			}
			// Handle any errors that may have occurred.
			catch (Exception e) {
				Log.e("YOUR_APP_LOG_TAG", "I got an error", e);

				return e.getMessage();
			}
			finally {
				if (rs != null) try { rs.close(); } catch(Exception e) {}
				if (stmt != null) try { stmt.close(); } catch(Exception e) {}
				if (con != null) try { con.close(); } catch(Exception e) {}
			}
			return "success";

		}


	}
	/*****************************************************************/

	
	/*****************************************************************/
	public static class checkIsExistsInDBThread extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				// Establish the connection.
				Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(dbConnectionString,"","");

				// Create and execute an SQL statement that returns some data.
				String SQL = "select * from Players where player_nickname='"+params[0]+"'";
				stmt = con.createStatement();
				rs=stmt.executeQuery(SQL);
				if(rs.next())
					return "exists";

			}
			// Handle any errors that may have occurred.
			catch (Exception e) {
				e.printStackTrace();
				Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
				return e.getMessage();
			}
			finally {
				if (rs != null) try { rs.close(); } catch(Exception e) {}
				if (stmt != null) try { stmt.close(); } catch(Exception e) {}
				if (con != null) try { con.close(); } catch(Exception e) {}
			}
			return "notExists";

		}


	}
	/*****************************************************************/







	//Methods which uses above threads to communicate the DB
	/********M***************S*****************************************************/
	/**********E************D*****************************************************/
	/*************T*******O*******************************************************/
	/*****************H***********************************************************/
	
	
	/*****************************************************************/
	public static String isExists(String nickname,String password){
		checkIsExistsInDBThread DB_thread=new checkIsExistsInDBThread();
		String res="";
		try {
			res = DB_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,nickname,password).get(4000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			res="InterruptedException: "+e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			res="ExecutionException: "+e.toString();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			res="TimeoutException: "+e.toString();
		}
		return res;
	}
	/*****************************************************************/

	
	
	/*****************************************************************/
	public static Vector<Object> getPlayerInfo(String nickname){
		Vector<Object> res=null;
		getPlayerInfoFromDBThread getInfo=new getPlayerInfoFromDBThread();
		try {
			res=getInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,nickname).get(3000, TimeUnit.MILLISECONDS);
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
		return res;

	}
	/*****************************************************************/

	
	/*****************************************************************/
	public static boolean connect(){

		try {

			// Establish the connection.
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(dbConnectionString,"","");



			// Create and execute an SQL statement that returns some data.
			String SQL = "SELECT * FROM dbo.Players";
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL);

			// Iterate through the data in the result set and display it.
			while (rs.next()) {
				System.out.println(rs.getString(1) + " " + rs.getString(2)+ " " + rs.getString(3));
			}
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			if (rs != null) try { rs.close(); } catch(Exception e) {}
			if (stmt != null) try { stmt.close(); } catch(Exception e) {}
			if (con != null) try { con.close(); } catch(Exception e) {}
		}
		return true;

	} 
	/*****************************************************************/

	
	
	/*****************************************************************/
	public static String registerToDB(String nickname,String password,String email){
		registerToDBThread regDB=new registerToDBThread();
		String res="";
		try {
			res = regDB.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,nickname,password,email).get(4000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			res="InterruptedException: "+e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			res="ExecutionException: "+e.toString();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			res="TimeoutException: "+e.toString();
		}
		return res;

	}
	/*****************************************************************/

	
	
}
