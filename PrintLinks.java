import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;

public class PrintLinks {
	public static void main(String args[]) throws MalformedURLException, IOException {
		URL url = new URL("http://www.aueb.gr/");
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
				System.out.println(link.toString());
				System.out.println();
			}
		}
		buffer.close();
	}
}