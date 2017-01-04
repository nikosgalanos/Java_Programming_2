public class UrlCheck {

	String motherUrl;
	String childUrl;

	public UrlCheck() {
	}

	public UrlCheck(String motherUrl , String childUrl) {

		this.motherUrl = motherUrl;
		this.childUrl = childUrl;

	}

	// elegxei an ta duo URL pou tou dvsame einai swsta
	/*public void rightInput(String motherUrl , String childUrl) throws IllegalArgumentException{

		//an den tou dwsoume prwteuon URL
		if (childUrl == null) {

			throw new IllegalArgumentException("Child Url must not be null");

		}

		//an den tou dwsoume deytereuon URL
		if (motherUrl == null) {

			throw new IllegalArgumentException("Mother Url must not be null");

		}
	} */

	public static String CorrectUrl(String motherUrl , String childUrl) {

		if (childUrl.startsWith(" ") || childUrl.startsWith("#") || childUrl.startsWith("mailto")) {
			return "-1";
		} else if (childUrl.startsWith("http")){
			return childUrl;
		} else if(childUrl.startsWith("/")){
			return motherUrl + childUrl;
		} else {
			return motherUrl + '/' + childUrl;
		}
	}
}
