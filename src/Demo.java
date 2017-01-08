import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

/**
 * Class which contains the main method and run the program using a Crawl object.
 *
 * @author Web Masters
 */
public class Demo {

	/**
	 * Main method.
	 * @param args the first link the crawler will visit
	 */
	public static void main(String[] args) {
		Crawl c;
		try {
			c = new Crawl(args[0]);
			c.crawl();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

}
