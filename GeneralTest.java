package main;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class GeneralTest {
	
	public static void main (String args[]) throws IOException, SQLException {
		
		URL startingURL = new URL("http://www.aueb.gr/");
		Queue<URL> links = new LinkedList<URL>();
		HashMap<URL,String> finalList = new HashMap<URL,String>();
		URL toPut, tempURL;
		String directoryName;
		int j;
		
		links.add(startingURL);
		links.add(new URL("http://www.cnn.gr/"));
		links.add(new URL("https://www.minedu.gov.gr/"));
		links.add(new URL("http://www.newsit.gr/"));
		links.add(new URL("http://www.dmst.aueb.gr/dds/"));
		
		
		
		for(j=0;j<=2;j++) {
			tempURL= links.element();
			links.poll();
			LinksExtractorVag le = new LinksExtractorVag(tempURL);
			
			System.out.println("** Queue Size: " + links.size() + " | Links Count: " + finalList.size() + " | Checking " + (j+1) + " link:    " + tempURL.toString());
			le.linksFilter();
			Iterator<String> i = le.accessHashSet().iterator();
			while (i.hasNext()) {
				try {
					toPut=new URL(i.next());
					System.out.println("---- Link came up:    " + toPut.toString());
					//picking procedure
					int count=0;
					StringBuffer sb = new StringBuffer(toPut.toString());
					for (int k=0;k<sb.length();k++) {
						if (sb.charAt(k) == '/') {
							count++;
						}
					}
					
					if (!finalList.containsKey(toPut) && count<=5) {
						DownloadHtml dh = new DownloadHtml(toPut);
						directoryName=dh.parseAndSaveHtml();
						finalList.put(toPut, directoryName);
						links.add(toPut);
					}
					else {
						/*System.out.println("то кимй упаявеи гдг ч то PATH еимаи поку лецако циа ма то девхоуле");*/
						/*System.out.println("Count = " + count +" СТО КИМЙ: " +toPut.toString());*/
					}
				}
				catch (IOException e) {
					continue;
				}
			}
		}
		
		System.out.println("END. "+ finalList.size() + " pages were saved.");
		
		DataBaseConn dbc = new DataBaseConn();
		dbc.insertData(finalList);
		
		/*for (URL name: finalList.keySet()){

            String key =name.toString();
            String value = finalList.get(name).toString();  
            System.out.println("	" + key);
            System.out.println("------------ "+value);
		}*/
		
	} 
		
}
