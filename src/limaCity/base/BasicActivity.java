package limaCity.base;

import limaCity.App.R;
import limaCity.chat.ChatManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BasicActivity extends Activity 
{
    
    @Override
    public void onResume()
    {
	super.onResume();
	if(!(this instanceof LoginActivity))
	{
	    SharedPreferences settings = getSharedPreferences(BasicData.PREFS_NAME, 0);
	    Boolean loggedIn = settings.getBoolean("loggedIn", false);
	    if(!loggedIn)
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
	switch(item.getItemId())
	{
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
	SharedPreferences prefs = this.getSharedPreferences(BasicData.PREFS_NAME, 0);
	Editor edit = prefs.edit();
	edit.clear();
	edit.commit();
	this.finish();
    }
    
    public void showInfo() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(getString(R.string.currentVersion)).setTitle(getString(R.string.info));
	AlertDialog alert = builder.create();
	alert.show();
    }

    public void refreshPage()
    {
	
    }

    public void resumeStandard() {
	super.onResume();
    }
}
