package limaCity.profile;

import java.util.Hashtable;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import limaCity.tools.ServerHandling;
import limaCity.tools.XmlWorker;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends BasicActivity
{
	ProfileItemAdapter profileItemAdapter = null;
	Hashtable<String, String> profileItems = new Hashtable<String, String>();
	String profileOwner = "";
	String user = "";
	String pass = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profilelayout);
		initVariables();
		initProfileData();
	}

	private void initVariables() {
		Bundle extras = getIntent().getExtras();
		if(extras !=null)
		{
			profileOwner = extras.getString("profile");
			user = extras.getString("user");
			pass = extras.getString("pass");
		}
	}

	private void initProfileData() {
		TextView username = (TextView) findViewById(R.id.textViewProfilePageName);
		username.setText(profileOwner);
		ListView profilePage = (ListView) findViewById(R.id.ProfilePageContent);
		profileItemAdapter = new ProfileItemAdapter(this);
		profilePage.setAdapter(profileItemAdapter);
		getProfileData();
	}

	private void getProfileData() 
	{
		new AboutTask(this).execute("profile", profileOwner, "type", "about", "exclude", "guestbook,friends,groups");
	}
	
	private class AboutTask extends AsyncTask<String,Void,Document> {
		
		Context context = null;
		
		public AboutTask(Context context) {
			this.context = context;
		}

		@Override
		protected Document doInBackground(String... data) {
        	Document documentContent = null;
    		documentContent = ServerHandling.postFromServer(context.getString(R.string.limaServerUrl), data);
    		return documentContent;
		}
		
		@Override
		protected void onPostExecute(Document result) 
		{
			if(result != null)
			{
				profileItemAdapter.clear();
				Elements nodes = result.select("profile").first().children();
				for(Element node : nodes)
				{
					String name = XmlWorker.prepareNodeName(node.nodeName());
					String content = XmlWorker.htmlToText(node.text());
					profileItemAdapter.addProfileItem(name, content);
					profileItemAdapter.notifyDataSetChanged();
				}
			}
		}
		
		@Override
		protected void onPreExecute() 
		{
			
		}
    }

	@Override
	public void refreshPage() {
	    profileItemAdapter.clear();
	    this.getProfileData();
	}
}
