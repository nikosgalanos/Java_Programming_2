package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

public class LinksExtractorVag {
	private static final CharSequence[] approvedContains = {".html", ".htm", ".asp", ".aspx", ".php", ".jsp", ".jspx", ".xml"};
	private URL url;
	private HashSet<String> links = new HashSet<String>(1);

	public LinksExtractorVag(URL url) {
		this.url = url;
	}

	public void linksFilter () throws IOException {
		int hrefPosition, wwwPosition, endPosition, previousSize, afterSize;
		InputStreamReader inputStream = new InputStreamReader(url.openStream());
		BufferedReader in = new BufferedReader(inputStream);
		String inputLine, link;

		while ((inputLine = in.readLine())!=null) {
			hrefPosition=inputLine.indexOf("href=\"");
			if (hrefPosition != -1) {
				wwwPosition=hrefPosition+6;
				endPosition=inputLine.indexOf("\"",wwwPosition);
				link = inputLine.substring(wwwPosition, endPosition);

				if (link.startsWith("/")) {
					link = adder(link);
				}
				else if (!link.startsWith("http")){
					boolean flag=false;
					for (int i=0; i<approvedContains.length; i++) {
						if (link.contains(approvedContains[i])) {
							flag=true;
							break;
						}
					}
					if (flag) {
						link = "/"+link;
						link = adder(link);
					}
				}

				PrefixSuffixCheck psc = new PrefixSuffixCheck(link);

				if (psc.suffix() && psc.prefix() ) {
					previousSize = links.size();
					links.add(link);
					afterSize = links.size();
					if (afterSize == previousSize +1) {
						/*System.out.println("Link: "+link+" ADDED");*/
					}
					else {
						/*System.out.println("Link: "+ link+ " ALREADY EXISTS");*/
					}
				}
				else {
					/*System.out.println("Link: "+ link +" DOESN'T FULFILL THE PREREQUISITES");*/
				}

			}
		}
	}

	public String adder (String link) {
		String urlString = url.toString();
		StringBuilder linkB = new StringBuilder(link);
		if (urlString.endsWith("/")) {
			return linkB.insert(0, urlString.substring(0, urlString.length()-1)).toString();
		}
		else {
			return linkB.insert(0, urlString).toString();
		}
	}

	public void printHashSet () {
		int j=0;
		Iterator<String> i = links.iterator();
		while(i.hasNext()) {
			j++;
	        System.out.println("Link " +j+" :"+ i.next());
	    }
	}

	public HashSet<String> accessHashSet () {
		return links;
	}

}
