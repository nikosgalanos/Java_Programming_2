/** Class which performs a connection to a local database.
 * It inserts into a pre-created table the full form of the examined url as well as the computer-path where the .html file
 * of the url is saved.
 * The class which will create an object of this class might use the "insertData" method which will do the pre-described job.
 * 
 * @author Web Masters
 * 
 */
import java.awt.Component;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.microsoft.sqlserver.jdbc.SQLServerException;

public class DataBaseConn {
	
	// local fields to handle the examined url the .html computer-path
	private static String link;
	private static String path;
	private static Component parentComponent = null;
	private static String databasePath = null;
	private static String username = null;
	private static String password = null;
	private static String tableName = null;
	
	/** Class constructor
	 * 
	 * There is no parameter used. The class which will create an object of this class might use the insertData method
	 * on this particular object
	 */
	
	public DataBaseConn() {
	}
	
	/** setters and getters 
	 * so as to handle the local fields
	 * 
	 */
	
	/**
	 * 
	 * @return link: link saved in a HashMap, ready to be copied into the database
	 */
	public static String getLink() {
		return link;
	}

	public static void setLink(String Link) {
		link = Link;
	}

	/**
	 * 
	 * @return path: computer path which indicates the computer location where the .html file of the examined link is saved
	 */
	public static String getPath() {
		return path;
	}

	public static void setPath(String Path) {
		path = Path;
	}
		
	/** This method's purpose is to ask the user of this app
	 *  all the necessary information such as the Local database path or the log-in data
	 *  in order to perform a connection to the database. It uses a user-interface environment to be more user-friendly.
	 *  @return data: a table in which the obtained data are temporarily saved so that they can be used in the insertData method
	 * 
	 */
	public static void getConnectionData() {
		databasePath = getDatabasePath();
		username = getUsername();
		password  = getPassword();
		tableName = getTableName();
	}
		
	/** This method calls the getConnectionData method, 
	 * performs the connection to the database using the obtained data
	 *  and then inserts the url and file path
	 *  into the correct column of the created database table
	 * @param finale: a HashMap in which all the examined urls and their computer paths are saved
	 * method scans the HashMap and copies the keys and their values into the database
	 * @throws SQLException
	 */
	public void insertData(HashMap<URL,String> finale) throws SQLException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("Driver Loaded");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not Loaded - Something went wrong");
			e.printStackTrace();
		}
		Connection connection = null;
		for(;;) {
			try {
				DataBaseConn.getConnectionData();
				connection = DriverManager.getConnection(databasePath, username, password);
			}
			catch (SQLServerException e) {
				JOptionPane.showMessageDialog(parentComponent, "Error, not correct database path, username or password. Please try again.");
				continue;
			}
			break;
		}
		
		Statement statement = connection.createStatement();
		for (;;) {
			try {
				statement.executeUpdate("INSERT INTO " + tableName + " VALUES('blank', 'blank')");
			}
			catch (SQLServerException e) {
				JOptionPane.showMessageDialog(parentComponent, "Error, not correct table name. Please try again.");
				tableName=getTableName();
				continue;
			}
			statement.executeUpdate("DELETE FROM " + tableName);
			statement.close();
			break;
		}
		
		for (URL urlName : finale.keySet()) {
			statement = connection.createStatement();
			/*if (urlName.toString()==null) System.out.println("WRONG! "+urlName.toString());*/
			DataBaseConn.setLink(urlName.toString());
			/*if (finale.get(urlName).toString()==null) System.out.println("WRONG! "+urlName.toString());*/
			DataBaseConn.setPath(finale.get(urlName).toString());
			statement.executeUpdate("INSERT INTO " + tableName +  " VALUES('" + DataBaseConn.getLink() + "' , '" + DataBaseConn.getPath() + "')");
			statement.close();
		}
		connection.close();
	}
	
	public static String getDatabasePath() {
		String databasePath = null;
		String databasePathEx="^jdbc:sqlserver://.+:1025;databaseName=.+$";
		Pattern p = Pattern.compile(databasePathEx);
			for(;;) {
				databasePath = JOptionPane.showInputDialog("Enter your local database path:");
				if (databasePath==null) {
					continue;
				}
			    Matcher m = p.matcher(databasePath);
				if (m.matches()) {
					break;
				}
				else {
					JOptionPane.showMessageDialog(parentComponent, "Error, not correct syntax. Please try again.");
					continue;
				}
			}
			return databasePath;
	}
	
	public static String getUsername() {
		String username = null;
			for(;;) {
				username = JOptionPane.showInputDialog("Enter your username:");
				if (username==null) {
					continue;
				}
				else {
					break;
				}
			}
			return username;
	}
	
	public static String getPassword() {
		String password = null;
			for(;;) {
				password = JOptionPane.showInputDialog("Enter your password:");
				if (password==null) {
					continue;
				}
				else {
					break;
				}
			}
			return password;
	}
	
	public static String getTableName() {
		String tableName = null;
			for(;;) {
				tableName = JOptionPane.showInputDialog("Enter your pre-created table name:");
				if (tableName==null) {
					continue;
				}
				else {
					break;
				}
			}
			return tableName;
	}
}
