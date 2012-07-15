package limaCity.tools;

import java.util.Random;

import android.content.Context;
import android.provider.Settings.Secure;

public class SecurityKeyGeneration {

    private Context context = null;

    public SecurityKeyGeneration(Context context) {
	this.context = context;
    }

    public String getKey() {
	StringBuffer key = new StringBuffer();
	String[] pieces = new String[11];
	String[] userpieces = getUserSpecificInfo();
	String[] randompieces = getRandomPieces();
	for (int i = 0; i < 5; i++) {
	    if (pieces.length >= i) {
		pieces[i] = randompieces[i];
	    }
	}
	for (int i = 5; i < 11; i++) {
	    if (pieces.length >= i) {
		pieces[i] = userpieces[i - 5];
	    }
	}
	for (int i = 0; i < 11; i++) {
	    Random rand = new Random();
	    int number = rand.nextInt(10);
	    if (pieces.length >= number) {
		key.append(pieces[number]);
	    }
	}
	return key.toString();
    }

    private String[] getRandomPieces() {
	String[] pieces = new String[5];
	for (int i = 0; i < 5; i++) {
	    double randomnumber = Math.random();
	    pieces[i] = Double.toString(randomnumber);
	}
	return pieces;
    }

    private String[] getUserSpecificInfo() {
	String[] pieces = new String[6];
	pieces[0] = System.getProperty("os.name", "os.name");
	pieces[1] = System.getProperty("os.version", "os.version");
	pieces[2] = System.getProperty("user.name", "user.name");
	pieces[3] = System.getProperty("java.vendor", "java.vendor");
	pieces[4] = System.getProperty("java.version", "java.version");
	pieces[5] = Secure.getString(this.context.getContentResolver(),
		Secure.ANDROID_ID);
	return pieces;
    }

}