/* First attempt to connect with a database created on MS SQL Management Studio
 * Purpose of this class: Get data saved in my database
 * Database Login-data are hidden on purpose
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TestDB2 {
	
	public static void main(String[] args) {
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("Driver Loaded");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		final String DATABASE_URL = "jdbc:sqlserver://@@@@@@";  // instead of @@@@@ use path of the database
		final String SELECT_QUERY = "SELECT * FROM ex1_Customer";

		try (
			Connection connection = DriverManager.getConnection(DATABASE_URL, "@@@", "@@@"); // instead of @@@ use username, password
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(SELECT_QUERY); )

		{
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			System.out.printf("Customers Table of Test1 Database:%n%n");
			for (int i = 1; i <= numberOfColumns; i++)
				System.out.printf("%-8s\t",  metaData.getColumnName(i));
			System.out.println();
			while (resultSet.next())
			{
				for (int i = 1; i <= numberOfColumns; i++)
					System.out.printf("%-8s\t", resultSet.getObject(i));
				System.out.println();
			}
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
	}
}
