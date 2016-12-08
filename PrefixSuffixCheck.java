package main;
import java.net.URL;

public class PrefixSuffixCheck {
	
	private String urlString;
	private static final String[] approvedSuffixes = {".html", ".htm", ".asp", ".aspx", ".php", ".jsp", ".jspx", ".xml", "/"};
	private static final CharSequence[] approvedContains = {".html", ".htm", ".asp", ".aspx", ".php", ".jsp", ".jspx", ".xml"};
	private static final String[] notApprovedSuffixes = {".jpeg", ".ico", ".css", ".png", ".doc", ".pdf", ".jpg", ".js", ".rdf"};
	private static final CharSequence[] notApprovedContains = {".jpeg", ".ico", ".css", ".png", ".doc", ".pdf", ".jpg", ".js", "/css", ".rdf"};
	
	public PrefixSuffixCheck (URL url) {
		urlString=url.toString();
	}
	
	public PrefixSuffixCheck (String url) {
		urlString=url;
	}
	
	public boolean suffix () {
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
			if (urlString.endsWith(approvedSuffixes[i])) {
				return true;
			}
		}
		for (i=0; i<approvedContains.length; i++) {
			if (urlString.contains(approvedContains[i])) {
				return true;
			}
		}
		return true;
	}
	
	public boolean prefix () {
		if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
			return true;
		}
		return false;
	}
	
}
