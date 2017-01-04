//τρέχει ένα αρχείο και εμφανίζει πόσες φορές εμφανίζεται η ζητούμενη λέξη!


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestFile2 {

	public static void main(String[] args) {
			try {
				Scanner input0 = new Scanner(System.in);
				System.out.print("Please enter the path of a file on your computer: ");
				String file;
				file = input0.nextLine();
				Scanner fileIn = new Scanner(new File(file));
				Scanner input = new Scanner(System.in);
				int i = 0;
				String x;
				String search;
				System.out.print("Enter a word for search: ");
				search = input.nextLine();
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
				System.out.printf("Word %s was found %d times!", search,i);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

}


