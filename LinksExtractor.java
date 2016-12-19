import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**A class that extracts links from an URL*/
public class LinksExtractor {

	private URL url = null;
	private String urlString;

	/**Class constructor that takes an URL as an argument*/
	public LinksExtractor(URL x) {
		url = x;
		urlString = url.toString();
	}

	/**Class constructor that takes a String as an argument and creates an URL*/
	public LinksExtractor(String x) throws MalformedURLException {
		url = new URL(x);
		urlString = x;
	}

	/**Returns URL*/
	public URL getUrl() {
		return url;
	}

	/**Returns a HashSet<String> with the links are contained in the URL*/
	public HashSet<String> getLinks() {

		HashSet<String> links = new HashSet<String>();
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new InputStreamReader(url.openStream()));
			Queue<Character> sample = new LinkedList<Character>();
			boolean flag = true;
			int ch;
			String finalLink;
			while ((ch = buffer.read()) != -1) {
				if (flag) {
					sample.add(conv(ch));
					for (int i = 0; i < 4; i++) {
						sample.add(conv(buffer.read()));
					}
					flag = false;
				} else {
					sample.remove();
					sample.add(conv(ch));
				}
				if (equalsHref(sample)) {
					StringBuilder link = new StringBuilder();
					buffer.read();
					while ((ch = buffer.read()) != '"') {
						link.append(conv(ch));
					}
					//System.out.println(link);
					finalLink = UrlCheck.CorrectUrl(urlString, link.toString());
					PrefixSuffixCheck check = new PrefixSuffixCheck(finalLink);
					if (check.suffix() && check.prefix()) {
						links.add(finalLink);
					}
				}
			}
			buffer.close();
		} catch (IOException e) {
			//System.out.println("1 exception caught and handled in LinksExtractor class");
			return null;
		}
		return links;
	}

	public char conv(int x) {
		return (char) x;
	}

	public boolean equalsHref(Queue<Character> q) {
		Queue<Character> href = new LinkedList<Character>();
		href.add('h');
		href.add('r');
		href.add('e');
		href.add('f');
		href.add('=');
		if (q.equals(href)) {
			return true;
		} else {
			return false;
		}
	}
}


