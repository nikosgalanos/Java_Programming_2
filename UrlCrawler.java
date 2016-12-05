import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public  class UrlCrawler {

	private String startingUrl = null ;

	public UrlCrawler(String urlName){

		startingUrl = urlName;
	}

	public HashMap<String,String> crawl() {

		// queue which includes the urls to be examined\
		Queue <String>urlQueue = new LinkedList<String>();
		urlQueue.add(startingUrl);

		// set of already examined web pages
		Set <String> visitedUrl = new HashSet<String>();

		HashMap<String,String> urlsMap = new HashMap<String,String>();

		while (visitedUrl.size() < 10) {

			//the connection with other parts are missing

		}

		return urlsMap;

	}
}







