package limaCity.tools;

import java.io.IOException;

import limaCity.App.R;
import limaCity.base.BasicData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionHandling {
    
    public static boolean startSession(Context appContext, String username, String password)
    {
	boolean loggedIn = false;
	
	String session = doLogin(appContext, username, password);

	loggedIn = session != "";
	Log.d("sessionKey", session);
	Log.d("loggedin", Boolean.toString(loggedIn));
	
	if(loggedIn)
	    setLoggedIn(appContext, username, password, session);
	
	return loggedIn;
    }

    private static String doLogin(Context appContext, String username, String password) {
	Document documentContent = null;
	String url = (String) appContext.getText(R.string.limaServerUrl);

	documentContent = ServerHandling.postFromServer(url, DataBuilder.stringArray("user", username, "pass", password, "action", "login"));
	
	if(documentContent == null)
	    return "";
	
	Elements loginNodes = documentContent.select(appContext.getString(R.string.nodeNameLoggedin));
	if (loginNodes.size() > 0) {
	    if (loginNodes.first().html().contentEquals(appContext.getString(R.string.nodeContentLoggedinPositive))) {
		Elements sessionNodes = documentContent.select(appContext.getString(R.string.nodeNameSession));
		if (sessionNodes.size() > 0)
		{
		    String session = sessionNodes.first().html();
		    return session;
		}
	    }
	}
	return "";
    }
    
    private static void setLoggedIn(Context appContext, String username, String password, String session) {
	SharedPreferences settings = appContext.getSharedPreferences(BasicData.PREFS_NAME,
		0);
	Editor settingsedit = settings.edit();
	String masterkey = settings.getString("masterkey", "");
	if (masterkey == "") {
	    SecurityKeyGeneration keyGen = new SecurityKeyGeneration(appContext);
	    masterkey = keyGen.getKey();
	    settingsedit.putString("masterkey", masterkey);
	}
	settingsedit.putBoolean("loggedIn", true);
	settingsedit.putString("user", username);
	settingsedit.putString("session", session);
	String encryptedPassword;
	try {
	    encryptedPassword = Crypto.encrypt(masterkey, password);
	    settingsedit.putString("pass", encryptedPassword);
	} catch (Exception e) {
	    Log.e("crypto", e.getMessage());
	}
	settingsedit.commit();

    }
    
    public static void setSessionKey(Context appContext, String sessionKey)
    {
	SharedPreferences settings = appContext.getSharedPreferences(
		    BasicData.PREFS_NAME, 0);
	Editor settingsEditor = settings.edit();
	settingsEditor.putString("session", sessionKey);
	settingsEditor.commit();
    }
    
    public static String getSessionKey(Context appContext)
    {
	SharedPreferences settings = appContext.getSharedPreferences(
		    BasicData.PREFS_NAME, 0);
	String sessionKey = settings.getString("session", "");
	Log.d("session", sessionKey);
	return sessionKey; 
    }

    public static void stopSession(Context appContext) {
	String url = (String) appContext.getText(R.string.limaServerUrl);
	
	SharedPreferences settings = appContext.getSharedPreferences(
		    BasicData.PREFS_NAME, 0);
	String session = settings.getString("session", "");
	try {
	    Jsoup.connect(url)
	    	.data("sid", session, "action", "logout")
	    	.userAgent("Mozilla").timeout(3000).get();
	} catch (IOException e) {
	    Log.d("connectionError", e.getMessage());
	}
	Editor settingsEditor = settings.edit();
	settingsEditor.clear();
	settingsEditor.commit();
    }
    
}
