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

/**
 * Class which performs a connection to a local database.
 * It inserts into a pre-created table the full form of the examined
 * url as well as the computer-path where the .html file of the url is saved.
 *
 * @author Web Masters
 */

public class DataBaseConn {

	// local fields to handle the received HashMap (url, path)
	private static String link;
	private static String path;

	private static String databasePath = null;
	private static String username = null;
	private static String password = null;
	private static String tableName = null;

	/**
	 * Class constructor. There is no parameter used.
	 */
	protected DataBaseConn() { }

	/**
	 * @return link saved in a HashMap, ready to be copied into the database
	 */
	protected static String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	protected static void setLink(String link) {
		DataBaseConn.link = link;
	}

	/**
	 * @return computer path which indicates the computer location where the .html
	 * file of the examined link is saved
	 */
	protected static String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	protected static void setPath(String path) {
		DataBaseConn.path = path;
	}

	/**
	 * This method's purpose is to ask the user of this app
	 * all the necessary information such as the Local database path or the log-in data
	 * in order to perform a connection to the database.
	 */
	private static void getConnectionData() {
		databasePath = getDatabasePath();
		username = getUsername();
		password  = getPassword();
		tableName = getTableName();
	}

	/**
	 * This method calls the getConnectionData method, performs the connection
	 * to the database using the obtained data and then inserts the url and file path
	 * into the correct column of the created database table.
	 * @param finalList a HashMap in which all the examined urls and their computer paths are saved
	 */
	protected void insertData(HashMap<URL, String> finalList) throws SQLException {
		// attempt to load the sql driver
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("Driver Loaded");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not Loaded - Something went wrong");
		}

		// attempt to establish a connection to the database
		Connection connection = null;
		for (;;) {
			try {
				DataBaseConn.getConnectionData();
				connection = DriverManager.getConnection(databasePath, username, password);
			} catch (SQLServerException e) {
				JOptionPane.showMessageDialog(null,
						"Error, not correct database path, username or password. Please try again.",
						null,
						JOptionPane.ERROR_MESSAGE);

				// if something bad occurs the user tries again
				continue;
			}
			break;
		}

		// check if the table's name is correct
		Statement statement = connection.createStatement();
		for (;;) {
			try {
				// insert test data
				statement.executeUpdate("INSERT INTO " + tableName + " VALUES('blank', 'blank')");
			} catch (SQLServerException e) {
				JOptionPane.showMessageDialog(null,
						"Error, not correct table name. Please try again.",
						null,
						JOptionPane.ERROR_MESSAGE);
				tableName = getTableName();
				// if something bad occurs the user tries again
				continue;
			}
			// delete inserted test data
			statement.executeUpdate("DELETE FROM " + tableName);
			statement.close();
			break;
		}

		for (URL url : finalList.keySet()) {
			statement = connection.createStatement();

			DataBaseConn.setLink(url.toString());
			DataBaseConn.setPath(finalList.get(url).toString());

			// insert url and path into the table
			statement.executeUpdate("INSERT INTO " + tableName +  " VALUES('" + DataBaseConn.getLink() + "' , '" + DataBaseConn.getPath() + "')");

			statement.close();
		}
		connection.close();
	}

	/**
	 * Method which helps to get (in a graphic way) the right database path using regular
	 * expressions to check the path and if-else blocks to handle any possible errors.
	 * @return the path of the local database
	 */
	private static String getDatabasePath() {
		String databasePath = null;
		String databasePathEx = "^jdbc:sqlserver://.+:[0-9]+;databaseName=.+$";
		Pattern p = Pattern.compile(databasePathEx);
			for (;;) {
				databasePath = JOptionPane.showInputDialog("Enter your local database path:");
				if (databasePath == null) {
					continue;
				}
			    Matcher m = p.matcher(databasePath);
				if (m.matches()) {
					break;
				} else {
					JOptionPane.showMessageDialog(null,
							"Error, not correct syntax. Please try again.",
							null,
							JOptionPane.ERROR_MESSAGE);
					continue;
				}
			}
			return databasePath;
	}

	/**
	 * Method which helps to get (in a graphic way) the username of the user using
	 * if-else blocks to handle any possible errors.
	 * @return the username in order to log in to the database
	 */
	private static String getUsername() {
		String username = null;
			for (;;) {
				username = JOptionPane.showInputDialog("Enter your username:");
				if (username == null) {
					continue;
				} else {
					break;
				}
			}
			return username;
	}

	/**
	 * Method which helps to get (in a graphic way) the password of the user using
	 * if-else blocks to handle any possible errors.
	 * @return the password in order to log in to the database
	 */
	private static String getPassword() {
		String password = null;
			for (;;) {
				password = JOptionPane.showInputDialog("Enter your password:");
				if (password == null) {
					continue;
				} else {
					break;
				}
			}
			return password;
	}

	/**
	 * Method which helps to get (in a graphic way) the table's name using
	 * if-else blocks to handle any possible errors.
	 * @return the name of the table in which we want to put the results
	 */
	private static String getTableName() {
		String tableName = null;
			for (;;) {
				tableName = JOptionPane.showInputDialog("Enter your pre-created table name:");
				if (tableName == null) {
					continue;
				} else {
					break;
				}
			}
			return tableName;
	}
}
