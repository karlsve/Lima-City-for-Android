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

public class BoardActivity extends BasicActivity {

    protected BoardItemAdapter boardItemsAdapter = null;

    protected String board = "";
    protected String page = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.boardlayout);
	super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {
	super.initVariables();
	Bundle extras = getIntent().getExtras();
	if (extras != null) {
	    board = extras.getString("board");
	    page = extras.getString("page");
	}
    }

    @Override
    protected void initData() {
	ListView boardPage = (ListView) findViewById(R.id.BoardPageContent);
	boardItemsAdapter = new BoardItemAdapter(this);
	boardPage.setAdapter(boardItemsAdapter);
	super.initData();
    }

    @Override
    protected void getData() {
	new BoardTask(this).execute("sid", session, "action", "board", "name",
		board);
    }

    private class BoardTask extends AsyncTask<String, Void, Document> {

	Context context = null;

	public BoardTask(Context context) {
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
		boardItemsAdapter.clear();
		boardItemsAdapter.notifyDataSetChanged();
		Elements boardNodes = result.select("board");
		if (boardNodes.size() > 0) {
		    Element boardNode = boardNodes.first();
		    Elements threadNodes = boardNode.select("thread");
		    for (Element threadNode : threadNodes) {
			String url = getNodeContent("url", threadNode);
			String name = getNodeContent("name", threadNode);
			String views = getNodeContent("views", threadNode);
			String replies = getNodeContent("replies", threadNode);
			String author = getNodeContent("author", threadNode);
			String date = getNodeContent("date", threadNode);
			if (url.length() > 0 && name.length() > 0) {
			    boardItemsAdapter.addItem(url, name, views,
				    replies, author, date);
			    boardItemsAdapter.notifyDataSetChanged();
			}
		    }
		}
	    }
	}

	@Override
	protected void onPreExecute() {

	}
    }

    protected String getNodeContent(String nodeName, Element parentNode) {
	Elements nodes = parentNode.select(nodeName);
	if (nodes.size() > 0) {
	    return nodes.first().html();
	}
	return "";
    }

}
