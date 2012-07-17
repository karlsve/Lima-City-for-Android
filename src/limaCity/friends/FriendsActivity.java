package limaCity.friends;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import limaCity.tools.ServerHandling;

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
	setContentView(R.layout.friendslayout);
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
	ListView friendPage = (ListView) findViewById(R.id.FriendsPageContent);
	friendItemAdapter = new FriendItemAdapter(this);
	friendPage.setAdapter(friendItemAdapter);
	super.initData();
    }

    @Override
    protected void getData() {
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
	super.refreshPage();
    }
}
