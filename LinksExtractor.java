import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.Set;
import java.util.HashSet;

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

	/**An assistant method that returns a String with the HTML code of the URL*/ 
	public String getString() throws IOException {
		BufferedReader buffer = null;
		buffer = new BufferedReader(new InputStreamReader(url.openStream()));
		int ch;
		StringBuilder builder = new StringBuilder();
		while ((ch = buffer.read()) != -1) {
			builder.append((char) ch);
		}
		buffer.close();
		return builder.toString();
	}

	/**A method that returns an HashSet with the links of HTML code*/ 
	public HashSet<String> getLinks() {
		HashSet<String> links = new HashSet<String>();
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new InputStreamReader(url.openStream()));
			char[] last4 = new char[5];
			boolean flag = true;
			int ch;
			while ((ch = buffer.read()) != -1) {
				if (flag) {
					last4[1] = (char) ch;
					last4[2] = (char) buffer.read();
					last4[3] = (char) buffer.read();
					last4[4] = (char) buffer.read();
					flag = false;
				} else {
					last4[1] = last4[2];
					last4[2] = last4[3];
					last4[3] = last4[4];
					last4[4] = (char) ch;
				}
				if ( last4[1] == 'h' && last4[2] == 'r' && last4[3] == 'e' && last4[4] == 'f') {
					StringBuilder link = new StringBuilder();
					buffer.read();
					buffer.read();
					while ((ch = buffer.read()) != '"') {
						link.append((char) ch);
					}
					//correct Url format
					if (UrlCheck.CorrectUrl(urlString, link.toString()) != "-1") {
						links.add(UrlCheck.CorrectUrl(urlString, link.toString()));
					}
				}
			}
			buffer.close();
		} catch (IOException e) {
			System.out.println("1 exception caught and handled in LinksExtractor class");
			return null;
		}
		return links;
	}
}



