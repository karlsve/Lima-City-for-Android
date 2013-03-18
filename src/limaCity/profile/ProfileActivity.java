package limaCity.profile;

import limaCity.App.R;
import limaCity.base.BasicActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class ProfileActivity extends BasicActivity {

	protected ProfileItemAdapter profileItemAdapter = null;
	protected String profileOwner = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.profilelayout);
	}

	@Override
	protected void initData() {
		super.initData();
		profileOwner = this.getIntent().getExtras().getString("profile");
		TextView username = (TextView) this.findViewById(R.id.textViewProfilePageName);
		username.setText(profileOwner);
		ListView profilePage = (ListView) this.findViewById(R.id.ProfilePageContent);
		profileItemAdapter = new ProfileItemAdapter(this);
		profilePage.setAdapter(profileItemAdapter);
	}
	
	@Override
	protected void getData()
	{
		new Thread()
		{
			@Override
			public void run() {
				sessionService.getProfile(profileOwner);
			}
		}.start();
	}
	
	@Override
	protected void onDataReceived(Document document) {
		if (document != null) {
			QuickContactBadge badge = (QuickContactBadge) findViewById(R.id.quickContactBadgeProfile);
			profileItemAdapter.clear();
			profileItemAdapter.notifyDataSetChanged();
			Elements profileNodes = document.select("result");
			if (profileNodes.size() > 0) {
				Elements nodes = profileNodes.select("element");
				for (Element node : nodes) {
					String name = node.select("name").first().text();
					String content = "";
					if(node.attr("type").equals("text"))
					{
						content = node.select("content").first().text();
						if(name.contentEquals("Jabber")) {
							Uri.Builder builder = new Uri.Builder();
							builder.scheme("imto");
							builder.appendPath("lima-city");
							builder.appendPath("jabber");
							builder.appendQueryParameter("user", content);
							badge.assignContactUri(builder.build());
							Log.d("URI", builder.toString());
						}
						profileItemAdapter.addProfileItem(name, content);
						profileItemAdapter.notifyDataSetChanged();
					} else if(node.attr("type").equals("messenger")) {
					} else if(node.attr("type").equals("boards")) {
						Elements boards = node.select("board");
						for(Element board : boards) {
							content += board.select("name").first().text()+"\n";
						}
						profileItemAdapter.addProfileItem(name, content);
						profileItemAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}
}
