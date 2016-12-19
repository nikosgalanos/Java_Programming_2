package main;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Crawl {
	
	private URL startingURL;
	private Queue<URL> links = new LinkedList<URL>();
	private HashMap<URL,String> finalList = new HashMap<URL,String>();
	private String path;
	
	public Crawl(String startingURL) throws MalformedURLException {
		this.startingURL = new URL(startingURL);
	}
	
	public void crawl() throws IOException, SQLException {
		URL tempQueueURL, tempPageURL;
		int loopCounter;
		links.add(startingURL);
		path = setPath();
		for(loopCounter=0;loopCounter<=1;loopCounter++) {
			tempQueueURL= pullFirstElement(links);
			LinksExtractorVag le = new LinksExtractorVag(tempQueueURL);
			printStatus(loopCounter, tempQueueURL);
			le.linksFilter();
			Iterator<String> i = le.accessHashSet().iterator();
			while (i.hasNext()) {
				try {
					tempPageURL=new URL(i.next());
					System.out.println("---- Link came up:    " + tempPageURL.toString());
					saveFile(tempPageURL);					
				}
				catch (IOException e) {
					continue;
				}
			}
		}
		printEndStatus();
		/*for (URL name: finalList.keySet()){
            String key =name.toString();
            String value = finalList.get(name).toString();  
            System.out.println("	" + key);
            System.out.println("------------ "+value);
		}*/
		insertDataToDB();
	}
	
	public URL pullFirstElement(Queue<URL> q) {
		return q.poll();
	}
	
	public void printStatus(int loopCount,  URL urlToBeChecked) {
		System.out.println("** Queue Size: " + links.size() + " | Links Count: " +
			+ finalList.size() + " | Checking " + (loopCount+1) + " link:    " + urlToBeChecked.toString());
	}
	
	public void printEndStatus() {
		System.out.println("END. "+ finalList.size() + " pages were saved.");
	}
	
	public void insertDataToDB() throws SQLException {
		DataBaseConn dbc = new DataBaseConn();
		dbc.insertData(finalList);
		System.out.println("Links added to DataBase!");
	}
	
	public void saveFile(URL url) throws UnsupportedEncodingException, IOException {
		if (!finalList.containsKey(url)) {
			DownloadHtml dh = new DownloadHtml(url, path);
			String directoryName=dh.parseAndSaveHtml();
			finalList.put(url, directoryName);
			links.add(url);
		}
		else {
			/*System.out.println("то кимй упаявеи гдг ч то PATH еимаи поку лецако циа ма то девхоуле");*/
			/*System.out.println("Count = " + count +" СТО КИМЙ: " +toPut.toString());*/
		}
	}
	
	public String setPath() {

		Scanner scn = new Scanner(System.in);
		System.out.println("Please enter the folder in which the html files will be saved:");
		path = scn.nextLine();
		File testFolder = new File(path);
		while (!testFolder.exists()) {
			System.out.print("This folder does not exist. Please enter a valid folder: ");
			path = scn.nextLine();
			testFolder = new File(path);
		}
		return path;
	}
		
}
