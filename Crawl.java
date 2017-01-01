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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Class which connects all the other classes of the package
 * 
 * @author Web Masters
 */
public class Crawl {
	
	// links which the user inserts
	private URL startingURL;
	// queue where links to be checked are stored
	private Queue<URL> linksQueue = new LinkedList<URL>();
	// hashmap which contains the final list of the links and paths
	private HashMap<URL,String> finalList = new HashMap<URL,String>();
	private String path;
	private static int doubleLinks=0;
	
	/**
	 * Constructor
	 * @param startingURL the url which is received from the user
	 */
	public Crawl(String startingURL) throws MalformedURLException {
		this.startingURL = new URL(startingURL);
	}
	
	/**
	 * Method which does the crawling, the basic job of a web crawler
	 */
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
			if (minutesCounter(startTime)>=waitingTime || linksQueue.size()==0) {
				break;
			}
			
			loopCounter++;
			
			// retrieves the first url of the queue 
			urlToBeChecked= pullFirstElement(linksQueue);
			
			/*LinksExtractorVag le = new LinksExtractorVag(urlToBeChecked);*/
			LinksEx le = new LinksEx(urlToBeChecked);
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
			
			// while (the HashSet has links AND we don't exceed the upper limit
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
	
	/**
	 * Method which returns the first link and removes it from the queue
	 * @param q the queue of the links to be examined
	 * @return the first element (link) of the queue
	 */
	private URL pullFirstElement(Queue<URL> q) {
		return q.poll();
	}
	
	/**
	 * Method which prints useful details of the proccess
	 * @param loopCount the number of the loops so far
	 * @param urlToBeChecked the url to be examined
	 */
	private void printStatus(int loopCount,  URL urlToBeChecked) {
		System.out.println("** Queue Size: " + linksQueue.size() + " | Links Count: " +
			+ finalList.size() + " | Checking " + (loopCount) + " link:    " + urlToBeChecked.toString());
	}
	
	/**
	 * Method which prints the number of the links saves and also the links which showed up more than one times
	 */
	private void printEndStatus() {
		System.out.println("END. "+ finalList.size() + " pages were saved. Double links: " + doubleLinks);
	}
	
	/**
	 * Method which makes the connection with the database
	 */
	private void insertDataToDB() throws SQLException {
		DataBaseConn dbc = new DataBaseConn();
		dbc.insertData(finalList);
		System.out.println("Links added to DataBase!");
	}
	
	/**
	 * Method which contributes to the downloading
	 * @param url the url whose html code will be downloaded
	 */
	private void saveFile(URL url) throws UnsupportedEncodingException, IOException {
		// if url isn't alredy in the list
		if (!finalList.containsKey(url)) {
			
			// download the html page
			DownloadHtml dh = new DownloadHtml(url, path);
			String directoryName=dh.parseAndSaveHtml();
			
			// if no exception is thrown
			if (directoryName!=null) {
				// put url into hashmap
				finalList.put(url, directoryName);
				// put url into the queue in order to be examined
				linksQueue.add(url);
			}
		// if url has shown up already
		} else {
			doubleLinks++;
		}
	}
	
	/**
	 * Method which uses java swing in order to help the user choose the download folder
	 * in which the html pages will be saved
	 * @return the path of the choosen folder
	 */
	private String setPath() {
		File folder;
		JOptionPane.showMessageDialog(null, "Please enter the folder in which the html files will be saved.");
		JFileChooser fc = new JFileChooser();

		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		for(;;) {
			int returnVal = fc.showOpenDialog(null);
			// if the user chooses a folder
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				folder = fc.getSelectedFile();
				break;
			}
			// if the user presses cancel button
			else if (returnVal == JFileChooser.CANCEL_OPTION) {
				continue;
			}
		}
		return folder.toString();
	}
	
	/**
	 * Method which counts the the minutes passed since the crawl started
	 * @param start the time that the crawl started
	 * @return how much time passed since the crawl started
	 */
	private float minutesCounter(long start) {
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		
		// conversion from millisecond to minutes
		float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
		return elapsedTimeMin;
	}
	
	/**
	 * Method which asks for the user (in a graphic way) to enter the time which is willing to wait
	 * @return the process time
	 */
	private float getWaitingTime() {
		String min = null;
		for(;;) {
			try {
				min = JOptionPane.showInputDialog("How much time do you want to wait? (in minutes)");
				Integer.parseInt(min);
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Error, null or not an integer. Please try again.", null, JOptionPane.ERROR_MESSAGE);
				continue;
			}
			break;
		}
		return Float.parseFloat(min);
	}
	
	/**
	 * Method which asks for the user (in a graphic way) to enter the links he want per html page
	 * @return the number of links per html page
	 */
	private int getLinksPerPage() {
		String pages = null;
		for(;;) {
			try {
				pages = JOptionPane.showInputDialog("How much links do you want per page?");
				Integer.parseInt(pages);
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Error, null or not an integer. Please try again.", null, JOptionPane.ERROR_MESSAGE);
				continue;
			}
			break;
		}
		return Integer.parseInt(pages);
	}
	
	/**
	 * Method which adds the starting link and the rest of links into the queue
	 */
	private void addLinkstoQueue () {
		linksQueue.add(startingURL);
		try {
			linksQueue.add(new URL("http://www.cnn.gr/"));
			linksQueue.add(new URL("http://www.bbc.com/"));
			linksQueue.add(new URL("http://www.newsit.gr/"));
			linksQueue.add(new URL("http://www.capital.gr/"));
			linksQueue.add(new URL("http://www.tanea.gr/"));
			linksQueue.add(new URL("http://news247.gr/"));
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "Error with the links inserted.", null, JOptionPane.ERROR_MESSAGE);
		}	
	}
		
}