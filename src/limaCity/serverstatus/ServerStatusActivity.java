package limaCity.serverstatus;

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

public class ServerStatusActivity extends BasicActivity {

    private ServerStatusItemAdapter serverStatusItemAdapter = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.serverstatuslayout);
	super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
	ListView serverStatusPage = (ListView) findViewById(R.id.ServerStatusPageContent);
	serverStatusItemAdapter = new ServerStatusItemAdapter(this);
	serverStatusPage.setAdapter(serverStatusItemAdapter);
	super.initData();
    }

    @Override
    protected void getData() {
	new ServerStatusTask(this).execute("sid", session, "action", "serverstatus");
    }
    
    private class ServerStatusTask extends AsyncTask<String, Void, Document> {

	Context context = null;

	public ServerStatusTask(Context context) {
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
		serverStatusItemAdapter.clear();
		serverStatusItemAdapter.notifyDataSetChanged();
		Elements serverStatusNodes = result.select("serverstatus");
		if (serverStatusNodes.size() > 0) {
		    Elements nodes = serverStatusNodes.first().select("info");
		    for (Element node : nodes) {
			String name = node.attr("name");
			String content = node.attr("time");
			serverStatusItemAdapter.addItem(name, content);
			serverStatusItemAdapter.notifyDataSetChanged();
		    }
		}
	    }
	}

	@Override
	protected void onPreExecute() {

	}
    }
    
}
