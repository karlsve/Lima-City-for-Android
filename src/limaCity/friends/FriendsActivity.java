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
import android.widget.ListView;

public class FriendsActivity extends BasicActivity 
{
	FriendItemAdapter friendItemAdapter = null;
	String profileOwner = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendslayout);
		initVariables();
		initFriendsData();
	}
	
	private void initVariables() {
		Bundle extras = getIntent().getExtras();
		if(extras !=null)
		{
			profileOwner = extras.getString("profile");
		}
	}
	
	private void initFriendsData()
	{
		ListView friendPage = (ListView) findViewById(R.id.FriendsPageContent);
		friendItemAdapter = new FriendItemAdapter(this);
		friendPage.setAdapter(friendItemAdapter);
		getFriendsData();
	}

	private void getFriendsData() {
		new FriendTask(this).execute("profile", profileOwner, "type", "about", "exclude", "groups,profile,guestbook");
	}

	private class FriendTask extends AsyncTask<String,Void,Document> {
		
		Context context = null;
		
		public FriendTask(Context context)
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
		    try
		    {
			Elements nodes = result.select("friends").first().children();
			for(Element node : nodes)
			{
				String name = node.text();
				friendItemAdapter.addFriendItem(name);
				friendItemAdapter.notifyDataSetChanged();
			}
		    }
		    catch(Exception e)
		    {
			e.printStackTrace();
		    }
		}
		
		@Override
		protected void onPreExecute() 
		{
			
		}
    }

	@Override
	public void refreshPage() {
	    friendItemAdapter.clear();
	    this.getFriendsData();
	}
}
