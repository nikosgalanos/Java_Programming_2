import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.net.URL;
import java.net.MalformedURLException;

public  class UrlCrawler {

	private String startingUrl = null;
	// queue which includes the urls to be examined
	Queue <String>urlQueue = new LinkedList<String>();
	// set of already examined web pages
	Set <String> visitedUrl = new HashSet<String>();
	HashMap<String,String> urlsMap = new HashMap<String,String>();

	public UrlCrawler(String urlName){
		startingUrl = urlName;
		urlQueue.add(startingUrl);
	}

	public HashMap<String,String> crawl() {

		while (visitedUrl.size() < 10 && (!urlQueue.isEmpty())) {
			String top = urlQueue.remove();
			if (visitedUrl.contains(top)) {
				continue;
			}
			URL presentURL;
			try {
				presentURL = new URL(top);
			} catch (MalformedURLException e) {
				System.out.println("1 exception caught and handled in UrlCrawler class");
				continue;
			}

			LinksExtractor le = new LinksExtractor(presentURL);
			DownloadHtml dh = new DownloadHtml(presentURL);

			HashSet<String> leResults = le.getLinks();
			String dhResults = dh.parseAndSaveHtml();

			if (leResults != null && dhResults != null) {
				visitedUrl.add(top);
				urlQueue.addAll(leResults);
				urlsMap.put(top, dhResults);
			}
		}
		return urlsMap;
	}
}
