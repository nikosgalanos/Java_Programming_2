package main;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class GeneralTest {
	
	public static void main (String args[]) throws IOException {
		
		URL startingURL = new URL("http://www.aueb.gr");
		Queue<URL> links = new LinkedList<URL>();
		HashMap<URL,String> finalList = new HashMap<URL,String>();
		URL toPut, tempURL;
		String directoryName;
		int j;
		
		links.add(startingURL);
		
		
		for(j=0;j<=1;j++) {
			tempURL= links.element();
			links.poll();
			LinksExtractorVag le = new LinksExtractorVag(tempURL);
			
			System.out.println("Εξετάζουμε το " + j + "o :" + tempURL.toString());
			le.linksFilter();
			Iterator<String> i = le.accessHashSet().iterator();
			while (i.hasNext()) {
				try {
				toPut=new URL(i.next());
				System.out.println("----------------Προέκυψε το :" + toPut.toString());
				if (!finalList.containsKey(toPut)) {
					DownloadHtml dh = new DownloadHtml(toPut);
					directoryName=dh.parseAndSaveHtml();
					finalList.put(toPut, directoryName);
					links.add(toPut);
				}
				
				else {
					System.out.println("TO LINK YPARXEI HDH");
				}
				}
				catch (IOException e) {
					continue;
				}
			}
		}
		
		System.out.println("Telos");
		
		for (URL name: finalList.keySet()){

            String key =name.toString();
            String value = finalList.get(name).toString();  
            System.out.println("	" + key);
            System.out.println("------------ "+value);
		}
		
	} 
		
}
