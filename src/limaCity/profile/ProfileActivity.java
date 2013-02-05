package limaCity.profile;

import limaCity.App.R;
import limaCity.base.BasicActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.widget.ListView;
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
			profileItemAdapter.clear();
			profileItemAdapter.notifyDataSetChanged();
			Elements profileNodes = document.select("result");
			if (profileNodes.size() > 0) {
				Elements nodes = profileNodes.select("element");
				for (Element node : nodes) {
					if(node.attr("type").equals("text"))
					{
						String name = node.select("name").first().text();
						String content = node.select("content").first().text();
						profileItemAdapter.addProfileItem(name, content);
						profileItemAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	}
}
