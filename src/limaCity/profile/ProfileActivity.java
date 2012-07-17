package limaCity.profile;

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

public class ProfileActivity extends BasicActivity {
    
    protected ProfileItemAdapter profileItemAdapter = null;
    protected String profileOwner = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.profilelayout);
	super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {
	super.initVariables();
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    profileOwner = extras.getString("profile");
	}
    }

    @Override
    protected void initData() {
	TextView username = (TextView) findViewById(R.id.textViewProfilePageName);
	username.setText(profileOwner);
	ListView profilePage = (ListView) findViewById(R.id.ProfilePageContent);
	profileItemAdapter = new ProfileItemAdapter(this);
	profilePage.setAdapter(profileItemAdapter);
	super.initData();
    }

    @Override
    protected void getData() {
	new AboutTask(this).execute("sid", session, "user", profileOwner, "action", "profile");
    }

    private class AboutTask extends AsyncTask<String, Void, Document> {

	Context context = null;

	public AboutTask(Context context) {
	    this.context = context;
	}

	@Override
	protected Document doInBackground(String... data) {
	    Document documentContent = null;
	    documentContent = ServerHandling.postFromServer(
		    context.getString(R.string.limaServerUrl), data);
	    return documentContent;
	}

	@Override
	protected void onPostExecute(Document result) {
	    if (result != null) {
		profileItemAdapter.clear();
		Elements profileNodes = result.select("profile");
		if (profileNodes.size() > 0) {
		    Elements nodes = profileNodes.first().children();
		    for (Element node : nodes) {
			String name = XmlWorker
				.prepareNodeName(node.nodeName());
			String content = XmlWorker.htmlToText(node.text());
			profileItemAdapter.addProfileItem(name, content);
			profileItemAdapter.notifyDataSetChanged();
		    }
		}
	    }
	}

	@Override
	protected void onPreExecute() {

	}
    }

    @Override
    public void refreshPage() {
	profileItemAdapter.clear();
	profileItemAdapter.notifyDataSetChanged();
	super.refreshPage();
    }
}
