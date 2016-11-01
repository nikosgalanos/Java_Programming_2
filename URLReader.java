//reading directly from a url

import java.net.*;
import java.io.*;

public class URLReader {
    public static void main(String[] args) throws Exception {

        URL aueb = new URL("http://www.aueb.gr/");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(aueb.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }
}