package limaCity.base;

import java.util.Hashtable;

import limaCity.App.R;
import limaCity.chat.ChatManager;
import limaCity.tools.Crypto;
import limaCity.tools.SessionHandling;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
		//initChat();
		startMainActivity(username, decryptedPassword);
		SessionHandling.startSession(this.getApplicationContext(), username, decryptedPassword);
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
		loggedIn = SessionHandling.startSession(context, username, password);
	    }
	    return loggedIn;
	}

	@Override
	protected void onPostExecute(Boolean loggedIn) {
	    progressDialog.dismiss();
	    int duration = Toast.LENGTH_SHORT;
	    Toast toast = new Toast(context);
	    if (loggedIn) {
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
