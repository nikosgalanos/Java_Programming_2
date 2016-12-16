package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

public class DownloadHtml {
	private URL url;
	private static String path;
	private static final char[] ab = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
									  'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	public DownloadHtml(URL url) {
		this.url = url;
	}
	
	public static void setPath() {

		Scanner scn = new Scanner(System.in);
		System.out.print("Please enter the folder in which the html files will saved: ");
		path = scn.nextLine();
		File testFile = new File(path);
		while (!testFile.exists()) {
			System.out.print("This folder does not exist. Please enter a valid folder: ");
			path = scn.nextLine();
			testFile = new File(path);
		}
	}
	
	public String parseAndSaveHtml () throws UnsupportedEncodingException, IOException {

		String inputLine;
		
		// creates the folder's name from the domain name
		String folderName = nameFolder();	
		File folderNameFile = new File(path + "\\" + folderName);
		if (!existsFolder(folderNameFile)) {
			makeFolder(folderNameFile);
		}
		
		// creates the file with a random unique name
		String fileName = generateFileName(folderName);
		File file = new File(path + "\\" + folderName + "\\" + fileName + ".txt");
		
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
		
			return path + "\\" + folderName + "\\" + fileName + ".txt";
		} catch (IOException e) {
			file.delete();
			return null;
		}
	}
	
	// returns the domain name so as to name the folder
	public String nameFolder() {
		String stringUrl = urlToString();
		int position1 = stringUrl.indexOf("www."); 					    // position of first "w"
		int position2; 						 					        // position before the country's suffix
		
		// case link is something "http://listofrandomwords.com/"
		if (position1 == -1) {
			position1 = stringUrl.indexOf("://"); 					    // "://" is after http and before domain name
			position2 = stringUrl.indexOf(".", 6);
			return stringUrl.substring(position1 + 3, position2);		// position + 3 = first letter of domain
		} else {
			position2 = stringUrl.indexOf(".",12);
			return stringUrl.substring(position1 + 4, position2);		// position + 4 = first letter of domain
		}
	}
	
	// converts Url to String
	public String urlToString() {
		return url.toString();
	}
	
	// returns true if the folder has already been created
	public boolean existsFolder(File folderNameFile) {
		if (folderNameFile.exists()) {
			return true;
		}
		return false;
	}
	
	// creates a directory with the given name
	public void makeFolder (File folderNameFile) {
		folderNameFile.mkdir();
	}
	
	// produces the file name which has this format x000000000
	// x: random letter | 0: random digit
	public String generateFileName (String folderName) {
		StringBuilder sb = new StringBuilder(10);
		String fileName;
		for (;;) {
			Random ran1 = new Random();
			sb.append(ran1.nextInt(9));
			for (int i = 0; i < 9; i++) {
				Random ran2 = new Random();
				sb.append(ab[ran2.nextInt(26)]);
			}
			fileName = sb.toString();
			File testFile = new File(path + "\\" + folderName + "\\" + fileName + ".txt");
			if (!existsFile(testFile)) {
				break;
			}
		}
		return fileName;
	}
	
	// returns true if there is a file with the same name in the directory
	public boolean existsFile (File testFile) {
		if (testFile.exists()) {
			return true;
		}
		return false;
	}

}