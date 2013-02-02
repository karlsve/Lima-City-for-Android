package limaCity.friends;

import limaCity.App.R;
import limaCity.base.BasicActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
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
	protected void initData() {
		super.initData();
		profileOwner = this.getIntent().getStringExtra("profile");
		ListView friendPage = (ListView) findViewById(R.id.FriendsPageContent);
		friendItemAdapter = new FriendItemAdapter(this);
		friendPage.setAdapter(friendItemAdapter);
	}
	
	@Override
	protected void getData() {
		new Thread()
		{
			@Override
			public void run() {
				sessionService.getFriends(profileOwner);
			}
		}.start();
	}
	
	@Override
	protected void onDataReceived(Document document) {
		if (document != null) {
			friendItemAdapter.clear();
			friendItemAdapter.notifyDataSetChanged();
			Elements profileNodes = document.select("result");
			if (profileNodes.size() > 0) {
				Elements nodes = profileNodes.select("friend");
				for (Element node : nodes) {
					String name = node.select("name").first().text();
					friendItemAdapter.addFriendItem(name);
					friendItemAdapter.notifyDataSetChanged();
				}
			}
		}
	}
}
