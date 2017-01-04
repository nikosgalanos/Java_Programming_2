import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RejectEquals {

	public static void main(String[] args) {

		ArrayList<String> urls = new ArrayList<>();

		urls.add("mple");
		urls.add("kokkino");
		urls.add("mple");
		urls.add("kitrino");
		urls.add("kokkino");
		urls.add("mple");


		Set<String> x = new HashSet<>();

		ArrayList<String> urls2 = new ArrayList<>();


		for (String url : urls) {
			if(x.add(url) == true) {
				urls2.add(url);
			}
		}
		System.out.println(urls2);
	}
}


