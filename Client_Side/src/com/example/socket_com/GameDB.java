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


	public static Vector<Object> getPlayerInfo(String nickname){
		Vector<Object> res=null;
		getPlayerInfoFromDBThread getInfo=new getPlayerInfoFromDBThread();
		try {
			res=getInfo.execute(nickname).get(3000, TimeUnit.MILLISECONDS);
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





	public static String addPlayer(String nickname,String password,String email){
		if(isExists(nickname,password).equals("exists"))
			return "exists";
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			// Establish the connection.
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


	public static String isExists(String nickname,String password){
		try {
			// Establish the connection.
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(dbConnectionString,"","");

			// Create and execute an SQL statement that returns some data.
			String SQL = "select * from Players where player_nickname='"+nickname+"'";
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





	public class registerToDBThread extends AsyncTask<String, Void, String> {


		String res;

		@Override
		protected String doInBackground(String... arg0) {
			String dbMessage="";
			dbMessage=GameDB.addPlayer((String)arg0[0], (String)arg0[0], (String)arg0[0]);
			if(dbMessage.equals("success")){
				res="Registration has completed successfully!\n You are now redirected to H&S Menu";
			}
			else if(dbMessage.equals("exists"))
				res="User name is already taken\n Please choose another one";
			else
				res="there was an error: "+dbMessage;

			return res;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}

	public String registerToDB(String nickname,String password,String email){
		registerToDBThread regDB=new registerToDBThread();
		String res="";
		try {
			res = regDB.execute(nickname,password,email).get(4000, TimeUnit.MILLISECONDS);
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


}
