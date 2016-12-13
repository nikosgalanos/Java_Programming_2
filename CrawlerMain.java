import java.util.HashMap;
import java.sql.SQLException;

public class CrawlerMain {

	public static void main(String[] args) throws SQLException {

		String seedURL = "http://www.aueb.gr/";
		UrlCrawler crawler = new UrlCrawler(seedURL);
		HashMap<String,String> urlsMap = crawler.crawl();
		DataBaseConn dbc = new DataBaseConn();
		dbc.insertData(urlsMap);
	}
}
//https://www.ego4u.com/en/cram-up/grammar/passive
//https://docs.oracle.com/javase/7/docs/api/java/util/AbstractMap.html#toString()
//http://aueb.gr/