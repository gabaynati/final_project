package com.example.socket_com;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//you need to install JDBC: download it and then go to Properties, go to Java Build Path, select Libraries tab, and Add External JARs.
public class GameDB {
	// Create a variable for the connection string.

	// Declare the JDBC objects.
	public static Connection con = null;
	public static Statement stmt = null;
	public static ResultSet rs = null;
	public static String dbConnectionString="jdbc:sqlserver://hns.database.windows.net:1433;database=hns;user=hns@hns;password={Seattle12};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";



	public static boolean connect(){

		try {
			// Establish the connection.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(dbConnectionString);

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
	
	
	
	
	public static boolean addPlayer(String nickname,String password,String email){
		if(isExists(nickname))
			return false;
		try {
			// Establish the connection.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(dbConnectionString);

			// Create and execute an SQL statement that returns some data.
			String SQL = "INSERT INTO Players VALUES ('"+nickname+"','"+password+"','"+email+"',0,0);";
			stmt = con.createStatement();
			stmt.executeUpdate(SQL);
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
	
	
	
	public static boolean isExists(String nickname){
		try {
			// Establish the connection.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(dbConnectionString);

			// Create and execute an SQL statement that returns some data.
			String SQL = "select * from Players where player_nickname='"+nickname+"'";
			stmt = con.createStatement();
			rs=stmt.executeQuery(SQL);
			if(rs.next())
				return true;

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
		return false;

	}
}
