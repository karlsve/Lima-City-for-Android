package limaCity.groups;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import limaCity.tools.ServerHandling;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class GroupsActivity extends BasicActivity {

    GroupItemAdapter groupItemAdapter = null;
    String profileOwner = "";
    String user = "";
    String pass = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.groupslayout);
	initVariables();
	initGroupsData();
    }

    private void initVariables() {
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    user = extras.getString("user");
	    pass = extras.getString("pass");
	    profileOwner = extras.getString("profile");
	}
    }

    private void initGroupsData() {
	ListView groupPage = (ListView) findViewById(R.id.GroupsPageContent);
	groupItemAdapter = new GroupItemAdapter(this);
	groupPage.setAdapter(groupItemAdapter);
	getGroupsData();
    }

    private void getGroupsData() {
	new GroupTask(this).execute("profile", profileOwner, "type", "about",
		"exclude", "profile,guestbook,friends");
    }

    private class GroupTask extends AsyncTask<String, Void, Document> {

	Context context = null;

	public GroupTask(Context context) {
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
	    Elements groupNodes = result.select("groups");
	    if (groupNodes.size() > 0) {
		Elements nodes = groupNodes.first().children();
		for (Element node : nodes) {
		    String name = node.text();
		    groupItemAdapter.addGroupItem(name);
		    groupItemAdapter.notifyDataSetChanged();
		}
	    }
	}

	@Override
	protected void onPreExecute() {

	}
    }

    @Override
    public void refreshPage() {
	groupItemAdapter.clear();
	this.getGroupsData();
    }

}
