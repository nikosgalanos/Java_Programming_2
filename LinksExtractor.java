import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;

//Javadocs and an external method that "completes the links" are pending
public class LinksExtractor {

	private URL url = null;

	public LinksExtractor(URL x) {
		url = x;
	}

	public LinksExtractor(String x) throws MalformedURLException {
		url = new URL(x);
	}

	public URL getUrl() {
		return url;
	}

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



