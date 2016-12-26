import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class Demo {

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