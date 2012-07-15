package limaCity.base;

import java.io.IOException;
import java.util.Hashtable;

import limaCity.App.R;
import limaCity.chat.ChatManager;
import limaCity.tools.Crypto;
import limaCity.tools.SecurityKeyGeneration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BasicActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.loginlayout);
	initLogin();
    }

    private void initLogin() {
	SharedPreferences settings = getSharedPreferences(BasicData.PREFS_NAME,
		0);
	Boolean loggedIn = settings.getBoolean("loggedIn", false);
	if (loggedIn) {
	    String username = settings.getString("user", "");
	    String password = settings.getString("pass", "");
	    String masterkey = settings.getString("masterkey", "");
	    String decryptedPassword;
	    try {
		decryptedPassword = Crypto.decrypt(masterkey, password);
		initChat();
		startMainActivity(username, decryptedPassword);
	    } catch (Exception e) {
		Log.e("decrypt", e.getMessage());
	    }
	} else {
	    initLoginButton();
	}
	initLoginButton();
    }

    private void initLoginButton() {
	Button loginButton = (Button) findViewById(R.id.loginButton);
	OnClickListener loginClickListener = new OnClickListener() {
	    public void onClick(View v) {
		Log.d("click", "startlogin");
		startLoginTask();
	    }
	};
	loginButton.setOnClickListener(loginClickListener);
    }

    private void startLoginTask() {
	EditText username = (EditText) findViewById(R.id.getUsername);
	EditText password = (EditText) findViewById(R.id.getPassword);
	Hashtable<String, String> data = new Hashtable<String, String>();
	data.put("username", username.getText().toString());
	data.put("password", password.getText().toString());
	LoginTask logintask = new LoginTask(this);
	logintask.execute(data);
    }

    private class LoginTask extends AsyncTask<Object, Integer, Boolean> {

	ProgressDialog progressDialog = null;
	Context context;
	String username;
	String password;
	String session;

	public LoginTask(Context context) {
	    this.context = context;
	}

	@Override
	protected Boolean doInBackground(Object... data) {
	    boolean loggedIn = false;
	    if (data[0] instanceof Hashtable) {
		@SuppressWarnings("unchecked")
		Hashtable<String, String> loginData = (Hashtable<String, String>) data[0];

		this.username = loginData.get("username");
		this.password = loginData.get("password");
		this.session = doLogin(username, password);

		loggedIn = this.session != "";

	    }
	    Log.d("loggedin", Boolean.toString(loggedIn));
	    return loggedIn;
	}

	@Override
	protected void onPostExecute(Boolean loggedIn) {
	    progressDialog.dismiss();
	    int duration = Toast.LENGTH_SHORT;
	    Toast toast = new Toast(context);
	    if (loggedIn) {
		setLoggedIn(this.username, this.password, this.session);
		CharSequence text = "Login erfolgreich!";
		toast = Toast.makeText(context, text, duration);
		// initChat();
		startMainActivity(this.username, this.password);
	    } else {
		CharSequence text = "Login nicht erfolgreich!";
		toast = Toast.makeText(context, text, duration);
	    }
	    toast.show();
	}

	@Override
	protected void onPreExecute() {
	    progressDialog = new ProgressDialog(this.context);
	    progressDialog.setMessage("Versuche einzuloggen...");
	    progressDialog.show();
	}
    }

    public void setLoggedIn(String username, String password, String session) {
	SharedPreferences settings = getSharedPreferences(BasicData.PREFS_NAME,
		0);
	Editor settingsedit = settings.edit();
	String masterkey = settings.getString("masterkey", "");
	if (masterkey == "") {
	    SecurityKeyGeneration keyGen = new SecurityKeyGeneration(this);
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

    public String doLogin(String username, String password) {
	Document documentContent = null;
	String url = (String) getText(R.string.limaServerUrl);

	try {
	    documentContent = Jsoup
		    .connect(url)
		    .data("user", username, "pass", password, "action", "login")
		    .userAgent("Mozilla").timeout(3000).post();
	} catch (IOException e) {
	    return "";
	}

	Elements loginNodes = documentContent.select("loggedin");
	if (loginNodes.size() > 0) {
	    if (loginNodes.first().html() == "true") {
		Elements sessionNodes = documentContent.select("session");
		if (sessionNodes.size() > 0) {
		    String session = sessionNodes.first().html();
		    return session;
		}
	    }
	}
	return "";
    }

    public void initChat() {
	ChatManager.startChat(this);
    }

    public void startMainActivity(String username, String password) {
	Intent intent = new Intent(this, MainActivity.class);
	intent.putExtra("user", username);
	intent.putExtra("pass", password);
	startActivity(intent);
    }

    @Override
    public void onResume() {
	super.resumeStandard();
    }
}
