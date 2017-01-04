/* Second (successful) attempt to connect with a database created on MS SQL Management Studio
 * Purpose of this class: Get data saved in my database
 * Database Login-data are hidden on purpose
 * now can also insert data in database as well as it can (Select - From- Where) them.
 */

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DataBaseConn {
	
	//fields
	
	private static String link = "1";
	private static String path = "2";

	// setters and getters
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	
	
	public static void main(final String[] args) throws SQLException {
	
	//loads driver of sqlDB and prints success or failure message!!
	
	try {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		System.out.println("Driver Loaded");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	
	//DataBase login data
	
	final String DataBase_Path = "@@@";
	final String DataBase_Username = "@@";
	final String DataBase_PassWord = "@@";
	
	// insert data into table
	
	Connection connection = DriverManager.getConnection(DataBase_Path, DataBase_Username, DataBase_Password);
	Statement statement = connection.createStatement();
	statement.executeUpdate("INSERT INTO Result VALUES('" + link + "' , '" + path + "')"); 
	
	// fields
		String Select_Query = "SELECT * FROM Result";
	
	//get data from table
	
	try (ResultSet resultSet = statement.executeQuery(Select_Query);
		)

	{
		 ResultSetMetaData metaData = resultSet.getMetaData();
		int numberOfColumns = metaData.getColumnCount();
		System.out.printf("Customers Table of Test1 Database:%n%n");
		for (int i = 1; i <= numberOfColumns; i++)
			System.out.printf("%-45s\t", metaData.getColumnName(i));
		System.out.println();
		while (resultSet.next()) {
			for (int i = 1; i <= numberOfColumns; i++)
				System.out.printf("%-45s\t", resultSet.getObject(i));
			System.out.println();
			}
	} catch (final SQLException sqlException) {
		sqlException.printStackTrace();
	}
}
}

