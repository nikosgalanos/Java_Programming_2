import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class WebcrawlerFirstStage {

	public static void main(String[] args) throws IOException {
		ArrayList<String> links = new ArrayList<String>();
		
		int count=0;
		URL aueb = new URL("http://www.aueb.gr/");
		String url= "http://www.aueb.gr";
		String urlPlus= "http://www.aueb.gr/";
		
		InputStreamReader inputStream = new InputStreamReader(aueb.openStream());
		BufferedReader in = new BufferedReader(inputStream);
		String inputLine;
		
		int first_h,starting_link,ending_link;
		String final_link, final_link_case2;
		StringBuilder check_link, before_convert;
		
		while ((inputLine = in.readLine())!=null) {
			
			first_h=inputLine.indexOf("href=\"");
			
			if (first_h != -1) {
				
				starting_link=first_h+6;
				ending_link=inputLine.indexOf("\"",starting_link);
				final_link=inputLine.substring(starting_link, ending_link);
				
				if (!final_link.equals("") && !final_link.equals("#") && !final_link.startsWith("mailto:") && !final_link.endsWith(".css") 
						&& !final_link.endsWith(".ico") ) {	
					
					check_link = new StringBuilder(final_link);
					if (check_link.charAt(0)=='/') {
						before_convert=check_link.insert(0,url);
						final_link_case2= before_convert.toString();
						if (!links.contains(final_link_case2)) {
							links.add(final_link_case2);
							System.out.println(final_link_case2);
						}
						else {
							System.out.println("STOP Already Exists:_" + final_link_case2);
						}
					}
					else if (!final_link.startsWith("http")) {
						before_convert=check_link.insert(0,urlPlus);
						final_link_case2= before_convert.toString();
						if (!links.contains(final_link_case2)) {
							links.add(final_link_case2);
							System.out.println(final_link_case2);
						}
						else {
							System.out.println("STOP Already Exists:_" + final_link_case2);
						}
					}
					else {
						if (!links.contains(final_link)) {
							links.add(final_link);
							System.out.println(final_link);
						}
						else {
							System.out.println("STOP Already Exists:_" + final_link);
						}
					}
					count++;
				}
			}
		}
		System.out.println(count);
		System.out.println(links.size());
	
	}
}
