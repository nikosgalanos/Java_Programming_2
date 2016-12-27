/** Class which performs a connection to a local database.
 * It inserts into a pre-created table the full form of the examined url as well as the computer-path where the .html file
 * of the url is saved.
 * The class which will create an object of this class might use the "insertData" method which will do the pre-described job.
 * 
 * @author Web Masters
 * 
 */
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class DataBaseConn {
	
	// local fields to handle the examined url the .html computer-path
	private static String link;
	private static String path;
	
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
	public static String[] getConnectionData() {
		String dbPath = JOptionPane.showInputDialog("Enter your local database path:");
		String username = JOptionPane.showInputDialog("Enter your username:");
		String password  = JOptionPane.showInputDialog("Enter your password:");
		String tableName = JOptionPane.showInputDialog("Enter your pre-created table name:");
		String[] data = new String[4];
		data[0] = dbPath;
		data[1] = username;
		data[2] = password;
		data[3] = tableName;
		return data;
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
