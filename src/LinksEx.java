import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinksEx {
	
	private URL url;
	private String urlString;
	private HashSet<String> links = new HashSet<String>(1);
	private static final int MAX_SIZE_PATH = 5;
	
	public LinksEx(URL url) {
		this.url = url;
		urlString=url.toString();
	}
	
	public void linksFilter() throws IOException {
		InputStreamReader inputStream = new InputStreamReader(url.openStream());
		BufferedReader in = new BufferedReader(inputStream);
		String inputLine, link;
		ArrayList<String> arrayLinks;
		while ((inputLine = in.readLine())!=null) {
			arrayLinks=findLinks(inputLine);
			arrayLinks.trimToSize();
			for(int i=0; i<arrayLinks.size(); i++) {
				link=arrayLinks.get(i);
				/*System.out.println("** Found link: " +link);*/
				link=checkLinks(link);
				if (link!=null) {
					PrefixSuffixCheck psc = new PrefixSuffixCheck(link);
					if (psc.suffix() && psc.prefix() && keepSmallUrls(link)) {
						/*System.out.println("LINK ADDED: "+link);*/
						links.add(link);
					}
				}
			}
		}
	}
	
	private ArrayList<String> findLinks (String inputLine) {
		ArrayList<String> links = new ArrayList<String>(0);
		String linkRegex = "href=\".*?\"";
		Pattern p = Pattern.compile(linkRegex);
		Matcher m = p.matcher(inputLine);
		while (m.find()) {
			links.add(inputLine.substring(m.start()+6, m.end()-1));
		}
		return links;
	}
	
	private String checkLinks (String linkToBeChecked) {
		String[] regex = {"^https?://.*?\\..*?\\..*?/?((.*?/)*|(.*))$",
						  "^[^/]*\\..*\\..*((/?)|((/.*/)+)){0,1}$",
				          "^/[^/].+[^/]/?$", 
						  "^//.*[^/]//$",
						  "[^/]+/.+",
						  "[/#]|(mailto:)|(\\s)"};
		// type 0 "http://www.aueb.gr/" or "https://www.aueb.gr/" or "http://www.aueb.gr/malakas/" or "https://www.aueb.gr/malakas/"
		//	   or "http://aueb.gr/ ...
		// type 1 "www.aueb.gr/" or "www.aueb.gr/gr/" or "www.aueb.gr" or "www.aueb.gr/gr"
		// type 2 "/pages/aueb/malakas/" or "/pages/aueb/malakas"
		// type 3 "//pages/aueb/malakas/" or "//pages/aueb/malakas"
		// type 4 "pages/" or "pages/akjsaksj/" or "pages/akjsaksj"
		// type 5 "#" or "/" or " "
		Pattern p = null;
		Matcher m;
		String fixedUrl = fixUrl();
		int flag = 999;
		for(int i=0; i<regex.length; i++) {
			p=Pattern.compile(regex[i]);
			m=p.matcher(linkToBeChecked);
			if (m.matches()) {
				flag=i;
				/*System.out.println("	--Link :" + linkToBeChecked + " marked with FLAG: " + flag);*/
				break;
			}
		}
		if (flag==0) {
			return linkToBeChecked;
		}
		else if (flag==1) {
			return "http://" + linkToBeChecked;
		}
		else if (flag==2) {
			return fixedUrl + linkToBeChecked;
		}
		else if (flag==3) {
			linkToBeChecked = linkToBeChecked.substring(1);
			return fixedUrl + linkToBeChecked;
		}
		else if (flag==4) {
			return fixedUrl + "/" + linkToBeChecked;
		}
		else {
			return null;
		}
	}
	
	private String fixUrl () {
		String tempRegex="http(s)?://(.*\\.){1,}[^/]*/?";
		String tempString;
		Pattern p=Pattern.compile(tempRegex);
		Matcher m=p.matcher(urlString);
		m.find();
		tempString=urlString.substring(m.start(),m.end());
		if (tempString.endsWith("/")) {
			tempString=tempString.substring(0, tempString.length()-1);
		}
		return tempString;
	}
	
	private boolean keepSmallUrls (String link) {
		int count=0;
		StringBuffer sb = new StringBuffer(link);
		for (int k=0;k<sb.length();k++) {
			if (sb.charAt(k) == '/') {
				count++;
			}
		}
		if (count<=MAX_SIZE_PATH) {
			return true;
		}
		return false;
	}
	
	public HashSet<String> accessHashSet () {
		return links;
	}
	
}
