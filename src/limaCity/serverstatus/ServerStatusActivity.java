package limaCity.serverstatus;

import limaCity.App.R;
import limaCity.base.BasicActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
		new Thread()
		{
			@Override
			public void run() {
				sessionService.getServerStatus();
			}
		}.start();
	}

	@Override
	protected void onDataReceived(Document document)
	{
		if (document != null) {
			serverStatusItemAdapter.clear();
			serverStatusItemAdapter.notifyDataSetChanged();
			Elements serverStatusNodes = document.select("result");
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
}
