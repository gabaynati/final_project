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
	public static String dbConnectionString="jdbc:jtds:sqlserver://hnsgame.database.windows.net:1433/hns;database=hns;integratedSecurity=true;user=hns;password=Seattle12";
	public static final int USER_EXISTS=1,DBERROR=2,USER_NOT_EXISTS=3,REGISTRATION_SUCCEEDED=4;







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
	public static class registerToDBThread extends AsyncTask<String, Void, Integer> {



		@Override
		protected Integer doInBackground(String... arg0) {
			return  addPlayer((String)arg0[0], (String)arg0[1], (String)arg0[2]);

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
		}
		private int addPlayer(String nickname,String password,String email){

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
			catch(SQLException se){
				return USER_EXISTS;		
			}
			catch (Exception e) {
				Log.e("YOUR_APP_LOG_TAG", "I got an error", e);

				return DBERROR;
			}
			finally {
				if (rs != null) try { rs.close(); } catch(Exception e) {}
				if (stmt != null) try { stmt.close(); } catch(Exception e) {}
				if (con != null) try { con.close(); } catch(Exception e) {}
			}
			return REGISTRATION_SUCCEEDED;

		}


	}
	/*****************************************************************/


	/*****************************************************************/
	public static class checkIsExistsInDBThread extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// Establish the connection.
				Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(dbConnectionString,"","");

				// Create and execute an SQL statement that returns some data.
				String SQL = "select * from Players where player_nickname='"+params[0]+"' and player_password='"+params[1]+"'";
				stmt = con.createStatement();
				rs=stmt.executeQuery(SQL);
				if(rs.next())
					return rs.getInt("player_port");

			}
			// Handle any errors that may have occurred.
			catch (Exception e) {
				e.printStackTrace();
				Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
				return DBERROR;

			}
			finally {
				if (rs != null) try { rs.close(); } catch(Exception e) {}
				if (stmt != null) try { stmt.close(); } catch(Exception e) {}
				if (con != null) try { con.close(); } catch(Exception e) {}
			}
			return USER_NOT_EXISTS;

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
		}


	}
	/*****************************************************************/



	/*****************************************************************/
	public static class updateScoreInDBThread extends AsyncTask<Object, Void, String> {

		@Override
		protected String doInBackground(Object... params) {
			try {
				// Establish the connection.
				Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(dbConnectionString,"","");

				// Create and execute an SQL statement that returns some data.
				String SQL = "update Players set player_score=player_score+"+(Integer)params[1]+" where player_nickname='"+(String)params[0]+"'";
				stmt = con.createStatement();
				stmt.executeUpdate(SQL);
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




	public static class PrintDBThread extends AsyncTask<String, Void, Vector<Vector<Object>> > {



		@Override
		protected Vector<Vector<Object>> doInBackground(String... arg0) {
			Vector<Vector<Object>> res=new Vector<Vector<Object>>();
			try {

				// Establish the connection.
				Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(dbConnectionString,"","");

				// Create and execute an SQL statement that returns some data.
				String SQL = "select * from Players";
				stmt = con.createStatement();
				rs=stmt.executeQuery(SQL);
				while(rs.next()){
					Vector<Object> user=new Vector<Object>();

					user.add(rs.getString("player_nickname"));
					user.add(rs.getString("player_password "));
					user.add(rs.getString("player_email"));
					user.add(rs.getInt("player_rank"));
					user.add(rs.getInt("player_score"));
					user.add(rs.getInt("player_port"));
					res.add(user);
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
		protected void onPostExecute(Vector<Vector<Object>> result) {
			super.onPostExecute(result);
		}

	}



	/*****************************************************************/






	//Methods which uses above threads to communicate the DB
	/********M***************S*****************************************************/
	/**********E************D*****************************************************/
	/*************T*******O*******************************************************/
	/*****************H***********************************************************/


	/*****************************************************************/
	public static int isExists(String nickname,String password){
		checkIsExistsInDBThread DB_thread=new checkIsExistsInDBThread();
		int res=0;
		try {
			res = DB_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,nickname,password).get(4000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			res=DBERROR;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			res=DBERROR;
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			res=DBERROR;
		}
		return res;
	}
	/*****************************************************************/

	/*****************************************************************/
	//	public static String clearDB(){
	//		checkIsExistsInDBThread DB_thread=new checkIsExistsInDBThread();
	//		String res="";
	//		try {
	//			res = DB_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get(4000, TimeUnit.MILLISECONDS);
	//		} catch (InterruptedException e) {
	//			// TODO Auto-generated catch block
	//			res="InterruptedException: "+e.toString();
	//		} catch (ExecutionException e) {
	//			// TODO Auto-generated catch block
	//			res="ExecutionException: "+e.toString();
	//		} catch (TimeoutException e) {
	//			// TODO Auto-generated catch block
	//			res="TimeoutException: "+e.toString();
	//		}
	//		return res;
	//	}
	/*****************************************************************/
	/*****************************************************************/
	//	public static String getAllUsers(){
	//		checkIsExistsInDBThread DB_thread=new checkIsExistsInDBThread();
	//		String res="";
	//		try {
	//			res = DB_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get(4000, TimeUnit.MILLISECONDS);
	//		} catch (InterruptedException e) {
	//			// TODO Auto-generated catch block
	//			res="InterruptedException: "+e.toString();
	//		} catch (ExecutionException e) {
	//			// TODO Auto-generated catch block
	//			res="ExecutionException: "+e.toString();
	//		} catch (TimeoutException e) {
	//			// TODO Auto-generated catch block
	//			res="TimeoutException: "+e.toString();
	//		}
	//		return res;
	//	}
	/*****************************************************************/
	/*****************************************************************/
	public static Vector<Vector<Object>> printDB(){
		PrintDBThread DB_thread=new PrintDBThread();
		Vector<Vector<Object>> db = null;
		try {
			db = DB_thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get(4000, TimeUnit.MILLISECONDS);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//	res="InterruptedException: "+e.toString();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			//	res="ExecutionException: "+e.toString();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			//res="TimeoutException: "+e.toString();
		}
		return db;
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
	public static int registerToDB(String nickname,String password,String email){
		registerToDBThread regDB=new registerToDBThread();
		int res;
		try {
			res = regDB.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,nickname,password,email).get(4000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			res=DBERROR;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			res=DBERROR;
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			res=DBERROR;
		}
		return res;

	}
	/*****************************************************************/


	/*****************************************************************/
	public static String updateScoreInDB(String nickname,int newScore){
		updateScoreInDBThread regDB=new updateScoreInDBThread();
		String res="";
		try {
			res = regDB.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,nickname,newScore).get(4000, TimeUnit.MILLISECONDS);
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
