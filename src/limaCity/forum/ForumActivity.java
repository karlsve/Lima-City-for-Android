package limaCity.forum;

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

public class ForumActivity extends BasicActivity {
    ForumItemAdapter forumItemAdapter = null;
    String user = "";
    String pass = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.forumlayout);
	super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
	ListView forumPage = (ListView) findViewById(R.id.ForumPageLayout);
	forumItemAdapter = new ForumItemAdapter(this, user, pass);
	forumPage.setAdapter(forumItemAdapter);
	super.initData();
    }

    @Override
    protected void getData() {
	new ForumTask(this)
		.execute("sid", session, "action", "forumlist");
    }

    private class ForumTask extends AsyncTask<String, Void, Document> {

	Context context = null;

	public ForumTask(Context context) {
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
	    Elements nodes = result.select("forum").first().select("board");
	    for (Element node : nodes) {
		String url = "";
		String name = "";
		String description = "";
		String moderators = "";

		Elements nameNodes = node.select("name");
		if (nameNodes.size() > 0) {
		    url = nameNodes.first().attr("url");
		    name = nameNodes.first().html();
		}
		Elements descriptionNodes = node.select("description");
		if (descriptionNodes.size() > 0) {
		    description = descriptionNodes.first().html();
		}

		Elements modsNodes = node.select("moderatoren");
		if (modsNodes.size() > 0) {
		    Elements modNodes = modsNodes.first().select("moderator");
		    if (modNodes.size() > 0) {
			for (Element modNode : modNodes) {
			    if (moderators.length() == 0) {
				moderators += modNode.html();
			    } else {
				moderators += ", " + modNode.html();
			    }
			}
		    }
		}
		if (url.length() > 0 && name.length() > 0) {
		    forumItemAdapter.addFriendItem(url, name, description,
			    moderators);
		    forumItemAdapter.notifyDataSetChanged();
		}
	    }
	}

	@Override
	protected void onPreExecute() {

	}
    }

    @Override
    public void refreshPage() {
	forumItemAdapter.clear();
	super.refreshPage();
    }
}
