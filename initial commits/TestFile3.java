import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.nio.file.DirectoryStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class TestFile3 extends JFrame {
	private final JTextArea outputArea;
	
	public TestFile3() throws IOException
	{
		super("JFileChooser Demo");
		outputArea = new JTextArea();
		add(new JScrollPane(outputArea));
		getPath();
	}
	
	public void getPath() throws IOException {
		Path path = getFileOrDirectoryPath();
		if (path != null && Files.exists(path)) {
			Scanner fileIn = new Scanner(new File(path.toString()));
			Scanner input = new Scanner(System.in);
			int i = 0;
			String x;
			String search;
			search = JOptionPane.showInputDialog("Enter a word for search");
			while (fileIn.hasNext()) {
				x = fileIn.next();
				if (x.endsWith(".")) {
					System.out.println();
				}
				if (x.matches(search)) {
					i++;
				}
				System.out.printf("%s ", x);
			}
			System.out.println();
			System.out.println();
			JOptionPane.showMessageDialog(null, "Word " + search + " was found " + i + " times!", "Result", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(this, path.getFileName() + "does not exist.", "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	}
	
	private Path getFileOrDirectoryPath() {
		JFileChooser filePicker = new JFileChooser();
		filePicker.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int res = filePicker.showOpenDialog(this);
		if (res == JFileChooser.CANCEL_OPTION)
			System.exit(1);
		return filePicker.getSelectedFile().toPath();
	}
}
	
	

