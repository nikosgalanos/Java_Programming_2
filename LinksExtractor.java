import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;

//An external method that "completes the links" are pending

/**A class that extracts links from an URL*/
public class LinksExtractor {

	private URL url = null;

	/**Class constructor that takes an URL as an argument*/
	public LinksExtractor(URL x) {
		url = x;
	}

	/**Class constructor that takes a String as an argument and creates an URL*/
	public LinksExtractor(String x) throws MalformedURLException {
		url = new URL(x);
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

	/**An assistant method that returns the number (int) of links in a HTML code*/
	public int hrefCounter() throws IOException {
		BufferedReader buffer = null;
		buffer = new BufferedReader(new InputStreamReader(url.openStream()));
		int f = 0;
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
				f = f + 1;
			}
		}
		buffer.close();
		return f;
	}

	/**A method that returns an ArrayList with the links of HTML code*/ 
	public ArrayList<String> getLinks() throws IOException {
		ArrayList<String> links = new ArrayList<String>();
		BufferedReader buffer = null;
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
				links.add(link.toString());
			}
		}
		buffer.close();
		return links;
	}
}


