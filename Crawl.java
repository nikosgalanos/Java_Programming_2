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
	private Scanner input = new Scanner(System.in);
	private static int doubleLinks=0;
	
	public Crawl(String startingURL) throws MalformedURLException {
		this.startingURL = new URL(startingURL);
	}
	
	public void crawl() throws IOException, SQLException {
		URL urlToBeChecked, urlToBeSaved;
		int loopCounter=0, pagesCounter;
		
		// add the starting URL's to the queue
		addLinkstoQueue();
		
		// asks user for the directory to save the files
		path = setPath();
		
		// Asks the user how much time does he want to wait
		float waitingTime=getWaitingTime();
		
		// Asks the user how much links does he want per page
		int givenPages=getLinksPerPage();
		
		// returns the current time
		long startTime = System.currentTimeMillis();
		
		for(;;) {
			
			// the loop breaks if the waiting time is over or the queue is empty
			if (minutesCounter(startTime)>=waitingTime || links.size()==0) {
				break;
			}
			
			loopCounter++;
			
			// retrieves the first url of the queue 
			urlToBeChecked= pullFirstElement(links);
			
			LinksExtractorVag le = new LinksExtractorVag(urlToBeChecked);
			printStatus(loopCounter, urlToBeChecked);
			try {
				// checks for available links and chooses the links we want
				le.linksFilter();
			} catch (IOException e) {
				// if anything bad occurs the loop continues
				continue;
			}
			
			// iterator to access the HashSet of the LinksExtractorVag Class
			Iterator<String> i = le.accessHashSet().iterator();
			
			// counter for the examined pages
			pagesCounter=0;
			
			// while (the HashSet has links AND we don't exceed the up limit
			while (i.hasNext() && givenPages>pagesCounter) {
				pagesCounter++;
				try {
					
					// the HashSet stores String Objects so we transform them into URL Objects
					urlToBeSaved=new URL(i.next());
					
					System.out.println("---- Link came up:    " + urlToBeSaved.toString());
					
					// attempt to download the html code
					saveFile(urlToBeSaved);					
				}
				catch (IOException e) {
					// if anything bad occurs the loop continues
					continue;
				}
			}
		}
		// messages for the user
		printEndStatus();
		
		// connection to the database
		insertDataToDB();
	}
	
	public URL pullFirstElement(Queue<URL> q) {
		return q.poll();
	}
	
	public void printStatus(int loopCount,  URL urlToBeChecked) {
		System.out.println("** Queue Size: " + links.size() + " | Links Count: " +
			+ finalList.size() + " | Checking " + (loopCount) + " link:    " + urlToBeChecked.toString());
	}
	
	public void printEndStatus() {
		System.out.println("END. "+ finalList.size() + " pages were saved. Double links: " + doubleLinks);
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
			if (directoryName!=null) {
				finalList.put(url, directoryName);
				links.add(url);
			}
		} else {
			doubleLinks++;
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
	
	public float minutesCounter(long start) {
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
		return elapsedTimeMin;
	}
	
	public float getWaitingTime() {
		System.out.println("How much time do you want to wait? (in minutes)");
		return (float)input.nextInt(); 
	}
	
	public int getLinksPerPage() {
		System.out.println("How much links do you want per page?");
		return input.nextInt();
	}
	
	public void addLinkstoQueue () {
		try {
			links.add(startingURL);
			links.add(new URL("http://www.cnn.gr/"));
			links.add(new URL("http://www.bbc.com/"));
			links.add(new URL("http://www.newsit.gr/"));
			links.add(new URL("http://www.capital.gr/"));
			links.add(new URL("http://www.tanea.gr/"));
			links.add(new URL("http://news247.gr/"));
		} catch (MalformedURLException e) {
			System.err.println("One of the default links is not correct");
		}
		
	}
		
}