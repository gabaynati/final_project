package com.example.socket_com;

import java.sql.*;

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
}
