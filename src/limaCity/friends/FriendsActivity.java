package limaCity.friends;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import limaCity.tools.ServerHandling;
import limaCity.tools.SessionHandling;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class FriendsActivity extends BasicActivity {
    FriendItemAdapter friendItemAdapter = null;
    String profileOwner = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.friendslayout);
	initVariables();
	initFriendsData();
    }

    private void initVariables() {
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    profileOwner = extras.getString("profile");
	}
    }

    private void initFriendsData() {
	ListView friendPage = (ListView) findViewById(R.id.FriendsPageContent);
	friendItemAdapter = new FriendItemAdapter(this);
	friendPage.setAdapter(friendItemAdapter);
	getFriendsData();
    }

    private void getFriendsData() {
	String session = SessionHandling.getSessionKey(this
		.getApplicationContext());
	new FriendTask(this.getApplicationContext()).execute("sid", session, "user", profileOwner,
		"action", "profile");
    }

    private class FriendTask extends AsyncTask<String, Void, Document> {

	Context context = null;

	public FriendTask(Context context) {
	    this.context = context;
	}

	@Override
	protected Document doInBackground(String... data) {
	    Document documentContent = ServerHandling.postFromServer(
		    context.getString(R.string.limaServerUrl), data);
	    return documentContent;
	}

	@Override
	protected void onPostExecute(Document result) {
	    if (result != null) {
		Log.d("friendList", result.html());
		Elements friendNodes = result.select("friends");
		if (friendNodes.size() > 0) {
		    Elements nodes = friendNodes.first().children();
		    for (Element node : nodes) {
			String name = node.text();
			friendItemAdapter.addFriendItem(name);
			friendItemAdapter.notifyDataSetChanged();
		    }
		}
	    }
	    else
	    {
		Log.d("friendList", "Something went wrong!");
	    }
	}

	@Override
	protected void onPreExecute() {

	}
    }

    @Override
    public void refreshPage() {
	friendItemAdapter.clear();
	this.getFriendsData();
    }
}
