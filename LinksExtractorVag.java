import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;

public class LinksExtractorVag {
	public static final int MAX_SIZE_PATH=5;
	private URL url;
	private HashSet<String> links = new HashSet<String>(1);
	private CharSequence[] approvedContains = {".html", ".htm", ".asp", 
		".aspx", ".php", ".jsp", ".jspx", ".xml"};

	public LinksExtractorVag(URL url) {
		this.url = url;
	}

	public void linksFilter () throws IOException{
		InputStreamReader inputStream = new InputStreamReader(url.openStream());
		BufferedReader in = new BufferedReader(inputStream);
		String inputLine, link;
		String[] temp = new String[2];
		int temp1;

		while ((inputLine = in.readLine())!=null) {
			temp1=0;
			for(;;) {
				try {
					temp=findLink(inputLine,temp1);
				} catch (StringIndexOutOfBoundsException e){
					break;
				}
				if (temp[1]==null) {
					break;
				}
				temp1 = Integer.parseInt(temp[1]);
				link=checkFirstSlash(temp[0]);
				PrefixSuffixCheck psc = new PrefixSuffixCheck(link);
					
				if (psc.suffix() && psc.prefix() && keepSmallUrls(link)) {
					/*System.out.println("LINK ADDED: "+link);*/
					links.add(link);
				} else {
					/*System.out.println("DOESN'T FULFILL THE PREREQUISITES: "+ link);*/
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

	public HashSet<String> accessHashSet () {
		return links;
	}
	
	public boolean keepSmallUrls (String link) {
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
	
	public String checkFirstSlash (String link) {
		if (link.startsWith("/") && !link.startsWith("//")) {
			/*System.out.println("LINK STARTS WITH /: "+ link);*/
			link = adder(link);
			/*System.out.println("FIXED LINK: " + link);*/
		}
		else if (link.startsWith("//")) {
			/* Do nothing */
		}
		else if (!link.startsWith("http")){
			/*System.out.println("LINK DOESNT START WITH HTTP: "+ link);*/
			boolean flag=false;
			for (int i=0; i<approvedContains.length; i++) {
				if (link.contains(approvedContains[i])) {
					flag=true;
					break;
				}
			}
			if (flag) {
				/*System.out.println("LINK CONTAINS APPROVED CONTAINS: "+ link);*/
				link = "/"+link;
				link = adder(link);
				/*System.out.println("FIXED LINK: " + link);*/
			}
		}
		
		return link;
	}
	
	public String[] findLink (String inputLine, int firstHrefIndex) {
		Integer hrefPosition, wwwPosition, endPosition;
		String temp[] = {null,null};
		String link;
		hrefPosition=inputLine.indexOf("href=\"", firstHrefIndex+1);
		if (hrefPosition != -1) {
			wwwPosition=hrefPosition+6;
			endPosition=inputLine.indexOf("\"",wwwPosition);
			link = inputLine.substring(wwwPosition, endPosition);
			temp[0]=link; temp[1]=hrefPosition.toString();
			return temp;
		}
		return temp;
	}
	
}