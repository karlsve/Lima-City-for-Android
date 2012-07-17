package limaCity.base;

import limaCity.App.R;
import limaCity.chat.ChatManager;
import limaCity.tools.SessionHandling;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BasicActivity extends Activity {

    protected String session = "";
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	initVariables();
	initData();
    }
    
    protected void initData()
    {
	getData();
    }
    
    protected void initVariables() {
	session = SessionHandling.getSessionKey(this.getApplicationContext());
    }

    @Override
    public void onResume() {
	super.onResume();
	if (!(this instanceof LoginActivity)) {
	    SharedPreferences settings = getSharedPreferences(
		    BasicData.PREFS_NAME, 0);
	    Boolean loggedIn = settings.getBoolean("loggedIn", false);
	    if (!loggedIn)
		this.finish();
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.activitymenu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.menuItemRefresh:
	    refreshPage();
	    break;
	case R.id.menuItemLogout:
	    logout();
	    break;
	}
	return true;
    }

    public void logout() {
	ChatManager.stopChat();
	SessionHandling.stopSession(this.getApplicationContext());
	this.finish();
    }

    public void showInfo() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(getString(R.string.currentVersion)).setTitle(
		getString(R.string.info));
	AlertDialog alert = builder.create();
	alert.show();
    }

    public void refreshPage() {
	getData();
    }
    
    protected void getData()
    {
	
    }

    public void resumeStandard() {
	super.onResume();
    }
}
