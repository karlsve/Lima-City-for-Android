package limaCity.forum;

import java.util.Hashtable;

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

public class BoardActivity extends BasicActivity {
	
	BoardItemAdapter boardItemsAdapter = null;
	Hashtable<String, String> profileItems = new Hashtable<String, String>();
	String user = "";
	String pass = "";
	String board = "";
	String page = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boardlayout);
		initVariables();
		initBoardData();
	}

	private void initVariables() {
		Bundle extras = getIntent().getExtras();
		if(extras !=null)
		{
			user = extras.getString("user");
			pass = extras.getString("pass");
			board = extras.getString("board");
			page = extras.getString("page");
		}
	}
	
	private void initBoardData() {
		ListView boardPAge = (ListView) findViewById(R.id.BoardPageContent);
		boardItemsAdapter = new BoardItemAdapter(this);
		boardPAge.setAdapter(boardItemsAdapter);
		getBoardData();
	}

	private void getBoardData() {
		new BoardTask(this).execute("user", user, "pass", pass, "type", "board", "board", board);
		Log.d("board", board);
	}
	
	private class BoardTask extends AsyncTask<String,Void,Document> {
		
		Context context = null;
		
		public BoardTask(Context context)
		{
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
			Element board = result.select("board").first();
			Elements nodes = board.select("thread");
			for(Element node : nodes)
			{
				String url = node.select("url").first().html();
				String name = node.select("name").first().html();
				String views = node.select("views").first().html();
				String replies = node.select("replies").first().html();
				String author = node.select("author").first().html();
				String date = node.select("date").first().html();
				boardItemsAdapter.addItem(url, name, views, replies, author, date);
				boardItemsAdapter.notifyDataSetChanged();
			}
		}
		
		@Override
		protected void onPreExecute() 
		{
			
		}
    }

	@Override
	public void refreshPage() {
	    boardItemsAdapter.clear();
	    getBoardData();
	}

}
