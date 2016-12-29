import java.net.MalformedURLException;
import java.net.URL;
/**
 * Class which helps so as to determine if the examined link has the acceptable format
 * @author Web Masters
 */
public class PrefixSuffixCheck {
	
	private String urlString;
	private static final String[] approvedSuffixes = {".html", ".htm", ".asp", ".aspx", 
			".php", ".jsp", ".jspx", ".xml", "/"};
	private static final CharSequence[] approvedContains = {".html", ".htm", ".asp", 
			".aspx", ".php", ".jsp", ".jspx", ".xml"};
	private static final String[] notApprovedSuffixes = {".jpeg", ".ico", ".css", 
			".png", ".doc", ".pdf", ".jpg", ".js", ".rdf"};
	private static final CharSequence[] notApprovedContains = {".jpeg", ".ico", ".css", 
			".png", ".doc", ".pdf", ".jpg", ".js", "/css", ".rdf", "javascript"};
	
	/**
	 * Constructor
	 * @param url the url we want to examine (URL Object)
	 */
	public PrefixSuffixCheck (URL url) {
		urlString=url.toString();
	}
	
	/**
	 * Constructor
	 * @param url the url we want to examine (String)
	 */
	public PrefixSuffixCheck (String url) {
		urlString=url;
	}
	
	/**
	 * Method which checks if the given url contains approved or not suffixes
	 * @return true if the url contains one of the approved suffixes or false if it doesn't
	 */
	public boolean suffix () throws MalformedURLException {
		int i;
		for (i=0; i<notApprovedSuffixes.length; i++) {
			if (urlString.endsWith(notApprovedSuffixes[i])) {
				return false;
			}
		}
		for (i=0; i<notApprovedContains.length; i++) {
			if (urlString.contains(notApprovedContains[i])) {
				return false;
			}
		}
		for (i=0; i<approvedSuffixes.length; i++) {
			if (urlString.endsWith(approvedSuffixes[i]))  {
				return true;
			}	
		}
		for (i=0; i<approvedContains.length; i++) {
			if (urlString.contains(approvedContains[i]))  {
				return true;
			}
		}
		return true;
	}
	
	/**
	 * Method which checks if the url starts with the http or th https protocol
	 * @return true if the url starts with http or https or false if it doesn't
	 */
	public boolean prefix () {
		if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
			return true;
		}
		return false;
	}
	
	
	
}