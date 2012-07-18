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
import android.util.Log;
import android.widget.ListView;

public class ThreadActivity extends BasicActivity {
    
    private ThreadItemAdapter threadItemsAdapter = null;
    
    private String thread = "";
    @SuppressWarnings("unused")
    private int page = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.threadlayout);
	super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void initData()
    {
	ListView threadPage = (ListView) findViewById(R.id.ThreadContent);
	threadItemsAdapter = new ThreadItemAdapter(this.getApplicationContext());
	threadPage.setAdapter(threadItemsAdapter);
	super.initData();
    }
    
    @Override
    protected void initVariables() {
	super.initVariables();
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    thread = extras.getString("thread");
	    page = extras.getInt("page");
	}
    }
    
    @Override
    protected void getData() {
	new ThreadTask(this.getApplicationContext()).execute("sid", session, "action", "thread", "name",
		thread);
    }
    
    private class ThreadTask extends AsyncTask<String, Void, Document>
    {
	Context context = null;
	
	public ThreadTask(Context context)
	{
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
		threadItemsAdapter.clear();
		threadItemsAdapter.notifyDataSetChanged();
		Elements threadNodes = result.select("board");
		if (threadNodes.size() > 0) {
		    Element threadNode = threadNodes.first();
		    Elements postNodes = threadNode.select("thread");
		    for (Element postNode : postNodes) {
			String type = getNodeContent("type", postNode);
			String date = getNodeContent("date", postNode);
			String content = getContentNodeContent(postNode);
			int id = Integer.parseInt(getNodeContent("id", postNode));
			String username = getNodeContent("user", postNode);
			String userdeletedstring = "false";
			if(username != "")
			{
			    userdeletedstring = postNode.select("user").first().attr("deleted");
			}
			boolean userdeleted = userdeletedstring == "true";
			threadItemsAdapter.addItem(type, date, content, id, username, userdeleted);
			threadItemsAdapter.notifyDataSetChanged();
		    }
		}
	    }
	}
    }
    
    private String getContentNodeContent(Element parentNode)
    {
	Elements contentNodes = parentNode.select("content");
	StringBuilder output = new StringBuilder();
	if(contentNodes.size() > 0)
	{
	    Element contentNode = contentNodes.first();
	    Elements contentPieceNodes = contentNode.children();
	    Log.d("tag", contentNode.html());
	    for(Element contentPieceNode : contentPieceNodes)
	    {
		Log.d("tag", contentPieceNode.tagName());
		if(contentPieceNode.tagName() == "text")
		{
		    output.append(contentPieceNode.html());
		}
		else if(contentPieceNode.tagName() == "br")
		{
		    output.append("\n");
		}
	    }
	}
	return output.toString();
    }
    
    private String getNodeContent(String nodeName, Element parentNode) {
	Elements nodes = parentNode.select(nodeName);
	if (nodes.size() > 0) {
	    return nodes.first().html();
	}
	return "";
    }
}
