package limaCity.forum;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ForumActivity extends BasicActivity {
	private ForumItemAdapter forumItemAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.forumlayout);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {
		ListView forumPage = (ListView) findViewById(R.id.ForumContent);
		forumItemAdapter = new ForumItemAdapter(this);
		forumPage.setAdapter(forumItemAdapter);
		super.initData();
	}
}
