import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class which helps us to choose which links the crawler wants.
 *
 * @author Web Masters
 */
public class LinksEx {

	/**
	 * the url page we want to examine.
	 */
	private final URL url;
	/**
	 * the url page we want to examine as string.
	 */
	private String urlString;
	/**
	 * the list of the links came out from the examined html page.
	 */
	private HashSet<String> links = new HashSet<String>(1);
	/**
	 * the number of "/" allowed for a link.
	 */
	private static final int MAX_SIZE_PATH = 5;

	/**
	 * Constructor. Receives the url page as URL object and transforms it to a String object
	 * @param url the url page we want to examine
	 */
	public LinksEx(final URL url) {
		this.url = url;
		urlString = url.toString();
	}

	/**
	 * Method which finds all the links stored in an html page,
	 * reforms them into the appropriate way
	 * and chooses the ones we want.
	 */
	protected void linksFilter() throws IOException {
		// opens the stream
		InputStreamReader inputStream = new InputStreamReader(url.openStream());
		BufferedReader in = new BufferedReader(inputStream);

		String inputLine, link;
		ArrayList<String> arrayLinks;

		// while the source code has lines
		while ((inputLine = in.readLine()) != null) {

			arrayLinks = findLinks(inputLine);
			// remove extra not used resources
			arrayLinks.trimToSize();

			// for every link in the ArrayList
			for (int i = 0; i < arrayLinks.size(); i++) {
				link = arrayLinks.get(i);
				/*System.out.println("** Found link: " +link);*/
				link = checkLinks(link);
				if (link != null) {
					PrefixSuffixCheck psc = new PrefixSuffixCheck(link);
					if (psc.suffix() && psc.prefix() && keepSmallUrls(link)) {
						/*System.out.println("LINK ADDED: "+link);*/
						// add link to hashset
						links.add(link);
					}
				}
			}
		}
	}

	/**
	 * Method which checks a line of the html page and retrieves all the links exist.
	 * @param inputLine the line of the html to be examined
	 * @return an ArrayList of the links exist in the line
	 */
	private ArrayList<String> findLinks(final String inputLine) {
		ArrayList<String> links = new ArrayList<String>(0);
		// regural expression to find links
		String linkRegex = "href=\".*?\"";
		Pattern p = Pattern.compile(linkRegex);
		// matches the regural expression with the inputLine
		Matcher m = p.matcher(inputLine);
		// while matcher finds links
		while (m.find()) {
			// keeps from : href="http://www.aueb.gr/" this part: http://www.aueb.gr/
			links.add(inputLine.substring(m.start() + 6, m.end() - 1));
		}
		return links;
	}

	/**
	 * Method which takes a link in order to examine it and returns it into the appropriate
	 * syntax in order to get a connection.
	 * @param linkToBeChecked the link to be checked and reformed
	 * @return the corrected link
	 */
	private String checkLinks(String linkToBeChecked) {
		String[] regex = {
				"^https?://.*?\\..*?\\..*?/?((.*?/)*|(.*))$", // type 0
				"^[^/]*\\..*\\..*((/?)|((/.*/)+)){0,1}$", 	  // type 1
				"^/[^/].+[^/]/?$",							  // type 2
				"^//.*[^/]//$",								  // type 3
				"[^/]+/.+",									  // type 4
				"[/#]|(mailto:)|(\\s)"};					  // type 5
		// type 0 "http://www.aueb.gr/" or "https://www.aueb.gr/"
		// 	   or "http://www.aueb.gr/pages/" or "https://www.aueb.gr/pages/"
		//	   or "http://aueb.gr/" or "https://aueb.gr/"
		// 	   or "http://aueb.gr/pages/" or "https://aueb.gr/pages"
		// type 1 "www.aueb.gr/" or "www.aueb.gr/pages/" or "www.aueb.gr" or "www.aueb.gr/pages"
		// type 2 "/pages/anakoinoseis/det/" or "/pages/anakoinoseis/det"
		// type 3 "//pages/anakoinoseis/det/" or "//pages/anakoinoseis/det"
		// type 4 "pages/" or "pages/anakoinoseis/" or "pages/anakoinoseis"
		// type 5 "#" or "/" or " "
		Pattern p = null;
		Matcher m;
		String fixedUrl = fixUrl();
		// the flag will store the type of the link
		int flag = 999;
		// checks every regular expression so as to match the link
		for (int i = 0; i < regex.length; i++) {
			p = Pattern.compile(regex[i]);
			m = p.matcher(linkToBeChecked);
			if (m.matches()) {
				flag = i;
				/*System.out.println("	--Link :" + linkToBeChecked + " marked with FLAG: " + flag);*/
				break;
			}
		}
		if (flag == 0) {
			// returns the same
			return linkToBeChecked;
		} else if (flag == 1) {
			// returns with "http://" added to the link
			return "http://" + linkToBeChecked;
		} else if (flag == 2) {
			// returns with "http://www.aueb.gr" added to the link
			return fixedUrl + linkToBeChecked;
		} else if (flag == 3) {
			// removes one of two "/" so as to add "http://www.aueb.gr"
			linkToBeChecked = linkToBeChecked.substring(1);
			return fixedUrl + linkToBeChecked;
		} else if (flag == 4) {
			// returns "http://www.aueb.gr" plus a "/" plus the link
			return fixedUrl + "/" + linkToBeChecked;
		} else {
			// if the link doesn't match any of the regular expressions
			return null;
		}
	}
	/**
	 * Method which takes a link into this form:
	 * "https://www.aueb.gr/pages/anakoinoseis/det.html" and
	 * transforms it into "https://www.aueb.gr".
	 * @return the link into the appropriate form
	 */
	private String fixUrl() {
		String tempRegex = "http(s)?://(.*\\.){1,}[^/]*/?";
		String tempString;
		Pattern p = Pattern.compile(tempRegex);
		Matcher m = p.matcher(urlString);
		m.find();
		tempString = urlString.substring(m.start(), m.end());
		// if the link ends with "/" remove it
		if (tempString.endsWith("/")) {
			tempString = tempString.substring(0, tempString.length() - 1);
		}
		return tempString;
	}

	/**
	 * Method which counts the number of "/" in a link.
	 * @param link the link to be examined
	 * @return true if the link has the correct size, false if it hasn't
	 */
	private boolean keepSmallUrls(String link) {
		int count = 0;
		StringBuffer sb = new StringBuffer(link);
		// counts each "/"
		for (int k = 0; k < sb.length(); k++) {
			if (sb.charAt(k) == '/') {
				count++;
			}
		}
		// max "/" is a final variable
		if (count <= MAX_SIZE_PATH) {
			return true;
		}
		return false;
	}

	/**
	 * @return the links
	 */
	protected HashSet<String> getLinks() {
		return links;
	}

}
