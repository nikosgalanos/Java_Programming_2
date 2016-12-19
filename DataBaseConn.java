<<<<<<< HEAD
package main;

/* This class is made for making a connection to a local database.
 * It inserts into an already created table the full form of the examined url as well as the computer-path where the .html file
 * of the url is saved.
 * The class which will create an object of this class might use the "insertData" method which will do the pre-described job.
 */
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class DataBaseConn {
	
	// fields
	private static String link;
	private static String path;
	
	// Class constructor using no arguments
	
	public DataBaseConn() {
	}
	
	// setters and getters
	
	public static String getLink() {
		return link;
	}

	public static void setLink(String Link) {
		link = Link;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String Path) {
		path = Path;
	}
		
	// method created to make the user insert the required data to make the database Connection 
	public static String[] getConnectionData() {
		Scanner input = new Scanner(System.in);
		System.out.print("Please insert your local database path: ");
		String dbPath = input.nextLine();
		System.out.print("Please insert your username: ");
		String username = input.nextLine();
		System.out.print("Please insert your password: ");
		String password  = input.nextLine();
		System.out.print("Please insert your table-name: ");
		String tableName = input.nextLine();
		String[] data = new String[4];
		data[0] = dbPath;
		data[1] = username;
		data[2] = password;
		data[3] = tableName;
		return data;
	}
		
	// method calls getConnectionData, makes the connection to the database and then inserts the url and file path
	// into the correct column of the created database table
	public void insertData(HashMap<URL,String> finale) throws SQLException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("Driver Loaded");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not Loaded - Something went wrong");
			e.printStackTrace();
		}
		String[] db = DataBaseConn.getConnectionData();
		Connection connection = DriverManager.getConnection(db[0], db[1], db[2]);
		for (URL urlName : finale.keySet()) {
			Statement statement = connection.createStatement();
			if (urlName.toString()==null) System.out.println("WRONG! "+urlName.toString());
			DataBaseConn.setLink(urlName.toString());
			if (finale.get(urlName).toString()==null) System.out.println("WRONG! "+urlName.toString());
			DataBaseConn.setPath(finale.get(urlName).toString());
			statement.executeUpdate("INSERT INTO " + db[3] +  " VALUES('" + DataBaseConn.getLink() + "' , '" + DataBaseConn.getPath() + "')");
			statement.close();
		}
		connection.close();
	}
}
=======
/* This class is made for making a connection to a local database.
 * It inserts into an already created table the full form of the examined url as well as the computer-path where the .html file
 * of the url is saved.
 * The class which will create an object of this class might use the "insertData" method which will do the pre-described job.
 */
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class DataBaseConn {
	
	// fields
	private static String link;
	private static String path;
	
	// Class constructor using no arguments
	
	public DataBaseConn() {
	}
	
	// setters and getters
	
	public static String getLink() {
		return link;
	}

	public static void setLink(String Link) {
		link = Link;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String Path) {
		path = Path;
	}
		
	// method created to make the user insert the required data to make the database Connection 
	public static String[] getConnectionData() {
		Scanner input = new Scanner(System.in);
		System.out.print("Please insert your local database path: ");
		String dbPath = input.nextLine();
		System.out.print("Please insert your username: ");
		String username = input.nextLine();
		System.out.print("Please insert your password: ");
		String password = input.nextLine();
		System.out.print("Please insert your table-name: ");
		String tableName = input.nextLine();
		input.close();
		String[] data = new String[4];
		data[0] = dbPath;
		data[1] = username;
		data[2] = password;
		data[3] = tableName;
		return data;
	}
		
	// method calls getConnectionData, makes the connection to the database and then inserts the url and file path
	// into the correct column of the created database table
	public void insertData(HashMap<String, String> finale) throws SQLException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("Driver Loaded");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not Loaded - Something went wrong");
			e.printStackTrace();
		}
		String[] db = DataBaseConn.getConnectionData();
		Connection connection = DriverManager.getConnection(db[0], db[1], db[2]);
		for (String urlName : finale.keySet()) {
			Statement statement = connection.createStatement();
			DataBaseConn.setLink(urlName);
			DataBaseConn.setPath(finale.get(urlName));
			statement.executeUpdate("INSERT INTO " + db[3] +  " VALUES('" + DataBaseConn.getLink() + "' , '" + DataBaseConn.getPath() + "')");
			statement.close();
		}
		connection.close();
	}
}
>>>>>>> 87125d70888b01f624141cf629c3b7fe6ebd965a
