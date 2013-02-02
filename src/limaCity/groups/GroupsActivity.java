package limaCity.groups;

import limaCity.App.R;
import limaCity.base.BasicActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.widget.ListView;

public class GroupsActivity extends BasicActivity {

	GroupItemAdapter groupItemAdapter = null;
	String profileOwner = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.groupslayout);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {
		super.initData();
		profileOwner = this.getIntent().getStringExtra("profile");
		ListView groupPage = (ListView) findViewById(R.id.GroupsPageContent);
		groupItemAdapter = new GroupItemAdapter(this);
		groupPage.setAdapter(groupItemAdapter);
	}
	
	@Override
	protected void getData() {
		new Thread()
		{
			@Override
			public void run() {
				sessionService.getGroups(profileOwner);
			}
		}.start();
	}
	
	@Override
	protected void onDataReceived(Document document)
	{
		if (document != null) {
			groupItemAdapter.clear();
			groupItemAdapter.notifyDataSetChanged();
			Elements profileNodes = document.select("result");
			if (profileNodes.size() > 0) {
				Elements nodes = profileNodes.select("group");
				for (Element node : nodes) {
					String name = node.select("name").first().text();
					groupItemAdapter.addGroupItem(name);
					groupItemAdapter.notifyDataSetChanged();
				}
			}
		}
	}

}
