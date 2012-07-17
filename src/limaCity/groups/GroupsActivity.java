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
	setContentView(R.layout.groupslayout);
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
	ListView groupPage = (ListView) findViewById(R.id.GroupsPageContent);
	groupItemAdapter = new GroupItemAdapter(this);
	groupPage.setAdapter(groupItemAdapter);
	super.initData();
    }

    @Override
    protected void getData() {
	new GroupTask(this).execute("sid", session, "user", profileOwner, "action", "profile");
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
	super.refreshPage();
    }

}
