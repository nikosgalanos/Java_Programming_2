import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class which helps so as to determine if the examined link has the acceptable format.
 * @author Web Masters
 */
public class PrefixSuffixCheck {

	private String urlString;
	private static final String[] APPROVED_SUFFIXES = {".html", ".htm", ".asp", ".aspx",
			".php", ".jsp", ".jspx", ".xml", "/"};
	private static final CharSequence[] APPROVED_CONTAINS = {".html", ".htm", ".asp",
			".aspx", ".php", ".jsp", ".jspx", ".xml"};
	private static final String[] NOT_APPROVED_SUFFIXES = {".jpeg", ".ico", ".css",
			".png", ".doc", ".pdf", ".jpg", ".js", ".rdf"};
	private static final CharSequence[] NOT_APPROVED_CONTAINS = {".jpeg", ".ico", ".css",
			".png", ".doc", ".pdf", ".jpg", ".js", "/css", ".rdf", "javascript"};

	/**
	 * Constructor.
	 * @param url the url we want to examine (URL Object)
	 */
	public PrefixSuffixCheck(URL url) {
		urlString = url.toString();
	}

	/**
	 * Constructor.
	 * @param url the url we want to examine (String)
	 */
	public PrefixSuffixCheck(String url) {
		urlString = url;
	}

	/**
	 * Method which checks if the given url contains approved or not suffixes.
	 * @return true if the url contains one of the approved suffixes or false if it doesn't
	 */
	protected boolean suffix() throws MalformedURLException {
		int i;
		for (i = 0; i < NOT_APPROVED_SUFFIXES.length; i++) {
			if (urlString.endsWith(NOT_APPROVED_SUFFIXES[i])) {
				return false;
			}
		}
		for (i = 0; i < NOT_APPROVED_CONTAINS.length; i++) {
			if (urlString.contains(NOT_APPROVED_CONTAINS[i])) {
				return false;
			}
		}
		for (i = 0; i < APPROVED_SUFFIXES.length; i++) {
			if (urlString.endsWith(APPROVED_SUFFIXES[i]))  {
				return true;
			}
		}
		for (i = 0; i < APPROVED_CONTAINS.length; i++) {
			if (urlString.contains(APPROVED_CONTAINS[i]))  {
				return true;
			}
		}
		return true;
	}

	/**
	 * Method which checks if the url starts with the http or th https protocol.
	 * @return true if the url starts with http or https or false if it doesn't
	 */
	protected boolean prefix() {
		return urlString.startsWith("http://") || urlString.startsWith("https://");
	}

}
