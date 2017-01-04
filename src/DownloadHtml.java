import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Random;

/**
 * Class which performs the connection with the webpage, downloads the source code of the page and writes it into a new file.
 * The name of the file is produced automatically using an object from the Random class.
 * The file is placed in a folder whose name comes from the domain of the webpage.
 * 
 * @author Web Masters
 */

public class DownloadHtml {
	private URL url;
	private String path;
	private static final char[] ab = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
									  'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	private static final String[] randomWords = {"roof", "threatening", "hurried", "friend", "chance", "befitting",
			"eight", "occur", "lamp", "rainstorm", "macho", "crime", "toe", "pest", "beds", "color", "geese", "intend",
			"yoke", "bent", "wide-eyed", "tame", "warm", "middle", "wandering", "trace", "perpetual", "limit", "literate",
			"furtive"};
	/**
	 * Constructor
	 * @param url: the URL of the page we want to save
	 * @param path: the local path of the folder in which the data will be stored.
	 */
	public DownloadHtml(URL url, String path) {
		this.url = url;
		
		// comes to a form "C:\Vaggelis\Desktop\DownloadHtml" so we transform it into "C:\Vaggelis\Desktop\DownloadHtml\"
		this.path = path+"\\";
	}
	
	/**
	 * Method which creates a folder (if needed) and saves into it the html page line by line.
	 * @return the local path of the file saved
	 */
	public String parseAndSaveHtml() {
		// the line of the html code
		String inputLine;
		
		// creates the folder's name from the domain name
		String folderName = nameFolder();	
		File folderNameFile = new File(path + folderName);
		
		// checks if there is already a folder with the same name
		if (! existsFolder(folderNameFile)) {
			makeFolder(folderNameFile);
		}
			
		// creates the file with a random unique name
		String fileName = generateFileName(folderName);
		File file = new File(path + folderName + "\\" + fileName + ".txt");
		
		try {
			// streams which help to read from the url
			InputStreamReader is = new InputStreamReader(url.openStream(), "UTF-8");
			BufferedReader br = new BufferedReader(is);
			br.mark(0);
			
			// streams which help to write from the url
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
					
			// reads line by line the html code
			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine);
				bw.newLine();
			}
			br.close();
			bw.close(); //closes the writer stream
			
			return path + folderName + "\\" + fileName + ".txt";
		} catch (IOException e) {
			file.delete();
			return null;
		}
		
	}
	
	/**
	 * Method which produces a name for a new folder and handles any possible exceptions
	 * @return the name of the new folder created
	 */
	private String nameFolder() {
		String stringUrl = url.toString();
		int position1 = stringUrl.indexOf("://www."); 				    // position of first ":"
		int position2; 						 					        // position before the country's suffix
		
		try {
			// case link is something "http://listofrandomwords.com/"
			if (position1 == -1) {
				position1 = stringUrl.indexOf("://"); 					    // position of ":"
				position2 = stringUrl.indexOf(".", 6);						// position of first "."
				return stringUrl.substring(position1 + 3, position2);   // position + 3 = first letter of domain
			} else {
				position2 = stringUrl.indexOf(".", 12);						// position of second "."
				return stringUrl.substring(position1 + 7, position2);	// position + 7 = first letter of domain
			}
		} catch (StringIndexOutOfBoundsException e) {
			Random ran = new Random(randomWords.length-1);
			return randomWords[ran.nextInt()];
		}
	}
	
	/**
	 * Method which helps to check if a folder exists
	 * @param folderNameFile the name of the folder which we want to know if already exists
	 * @return true if the folder exists or false if the folder doesn't exist
	 */
	private boolean existsFolder(File folderNameFile) {
		if (folderNameFile.exists()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method which makes a folder with a specific name
	 * @param folderNameFile File object which contains the path of the new folder we want to create
	 */
	private void makeFolder (File folderNameFile) {
		folderNameFile.mkdir();
	}
	

	/**
	 * Method which produces a random file name which has this format: x000000000 (x: random alphabet letter, 0: random digit 0-9)
	 * @param folderName the name of the folder in which we want to put the new file
	 * @return the new file's name
	 */
	private String generateFileName (String folderName) {
		StringBuilder sb = new StringBuilder(10);
		String fileName;
		
		for (;;) {
			
			// produce the random number 0-9
			Random ran1 = new Random();
			
			// put the number in the StringBuilder
			sb.append(ran1.nextInt(9));
			
			for (int i = 0; i < 9; i++) {
				// produce random alphabet letters
				Random ran2 = new Random();
				
				// put the letters in the StringBuilder
				sb.append(ab[ran2.nextInt(26)]);
			}
			
			fileName = sb.toString();
			
			// create the file
			File testFile = new File(path + folderName + "\\" + fileName + ".txt");
			
			// test if the file exists
			if (! existsFile(testFile)) {
				break;
			}
		}
		return fileName;
	}
	
	/**
	 * Method which checks if a file already exists in a directory
	 * @param testFile File object which contains the path of the file we want to create
	 * @return true if the file already exists or false if the file is not contained in the given directory
	 */
	private boolean existsFile (File testFile) {
		if (testFile.exists()) {
			return true;
		}
		return false;
	}

}