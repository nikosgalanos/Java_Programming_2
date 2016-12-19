package main;

import java.io.IOException;
import java.sql.SQLException;

public class Demo {

	public static void main(String[] args) throws IOException, SQLException {
		Crawl c = new Crawl("http://www.aueb.gr/");
		c.crawl();
	}

}