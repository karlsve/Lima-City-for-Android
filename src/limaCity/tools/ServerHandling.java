package limaCity.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.util.Log;

public class ServerHandling {

    public static Document postFromServer(String url, String... data) {
	Document documentContent = null;
	if (data != null) {
	    try {
		documentContent = Jsoup.connect(url).data(data)
			.userAgent("Mozilla").timeout(6000).post();
	    } catch (Exception e) {
		Log.d("postFromServer", e.getMessage());
	    }
	}
	return documentContent;
    }
    
    public static Document getFromServer(String url, String... data)
    {
	Document documentContent = null;
	if (data != null) {
	    try {
		documentContent = Jsoup.connect(url).data(data)
			.userAgent("Mozilla").timeout(6000).post();
	    } catch (Exception e) {
		Log.d("getFromServer", e.getMessage());
	    }
	}
	return documentContent;
    }
}
