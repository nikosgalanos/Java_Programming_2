import java.io.IOException;
import javax.swing.JFrame;

public class TestFile4 {

	public static void main(String[] args) throws IOException {
		TestFile3 test = new TestFile3();
		test.setSize(400,400);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);

	}

}
