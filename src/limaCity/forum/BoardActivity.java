package limaCity.forum;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import android.os.Bundle;
import android.widget.ListView;

public class BoardActivity extends BasicActivity {

	protected BoardItemAdapter boardItemsAdapter = null;

	protected String board = "";
	protected int page = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.boardlayout);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {
		ListView boardPage = (ListView) findViewById(R.id.BoardPageContent);
		boardItemsAdapter = new BoardItemAdapter(this);
		boardPage.setAdapter(boardItemsAdapter);
		super.initData();
	}

}
