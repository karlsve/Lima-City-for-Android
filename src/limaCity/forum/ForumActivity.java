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

public class ForumActivity extends BasicActivity 
{
	ForumItemAdapter forumItemAdapter = null;
	String user = "";
	String pass = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forumlayout);
		initVariables();
		initForumData();
	}

	private void initVariables() {
		Bundle extras = getIntent().getExtras();
		if(extras !=null)
		{
			user = extras.getString("user");
			pass = extras.getString("pass");
		}
	}
	
	private void initForumData() {
		ListView forumPage = (ListView) findViewById(R.id.ForumPageLayout);
		forumItemAdapter = new ForumItemAdapter(this, user, pass);
		forumPage.setAdapter(forumItemAdapter);
		getForumData();
	}

	private void getForumData() {
		new ForumTask(this).execute("user", user, "pass", pass, "type", "forum");
	}
	
	private class ForumTask extends AsyncTask<String,Void,Document> {
		
		Context context = null;
		
		public ForumTask(Context context)
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
			Elements nodes = result.select("forum").first().select("board");
			for(Element node : nodes)
			{
				String url = node.select("name").first().attr("url");
				String name = node.select("name").first().html();
				String description = node.select("description").first().html();
				Elements moderatorelements = node.select("moderatoren").first().select("moderator");
				String moderators = "";
				for(Element moderatorelement : moderatorelements)
				{
					if(moderators.length() == 0)
					{
						moderators += moderatorelement.html();
					}
					else
					{
						moderators += ", "+moderatorelement.html();
					}
				}
				forumItemAdapter.addFriendItem(url, name, description, moderators);
				forumItemAdapter.notifyDataSetChanged();
			}
		}
		
		@Override
		protected void onPreExecute() 
		{
			
		}
    }

	@Override
	public void refreshPage() {
	    forumItemAdapter.clear();
	    this.getForumData();
	}
}
