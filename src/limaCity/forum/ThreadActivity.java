package limaCity.forum;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ThreadActivity extends BasicActivity {

	private ThreadItemAdapter threadItemsAdapter = null;
	
	@SuppressWarnings("unused")
	private int page = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.threadlayout);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {
		ListView threadPage = (ListView) findViewById(R.id.ThreadContent);
		threadItemsAdapter = new ThreadItemAdapter(this.getApplicationContext());
		threadPage.setAdapter(threadItemsAdapter);
		super.initData();
	}
}
