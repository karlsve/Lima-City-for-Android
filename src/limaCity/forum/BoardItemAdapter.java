package limaCity.forum;

import java.util.ArrayList;

import limaCity.App.R;
import limaCity.tools.XmlWorker;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BoardItemAdapter extends BaseAdapter {

	ArrayList<BoardItem> threadItems = new ArrayList<BoardItem>();
	Context context;

	public BoardItemAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return threadItems.size();
	}

	@Override
	public Object getItem(int position) {
		return threadItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout boardListViewItem;

		if (convertView == null) {
			boardListViewItem = (LinearLayout) LayoutInflater.from(context)
					.inflate(R.layout.boardelementlayout, parent, false);
		} else {
			boardListViewItem = (LinearLayout) convertView;
		}

		BoardItem item = threadItems.get(position);
		String titletext = XmlWorker.htmlToText(item.getName());
		String authortext = item.getAuthor();
		String datetext = item.getDate();
		String url = item.getUrl();

		TextView name = (TextView) boardListViewItem
				.findViewById(R.id.textViewBoardElementText);
		name.setText(titletext);
		TextView author = (TextView) boardListViewItem
				.findViewById(R.id.textViewBoardElementUserName);
		author.setText(authortext);
		TextView date = (TextView) boardListViewItem
				.findViewById(R.id.textViewBoardElementDate);
		date.setText(datetext);

		boardListViewItem = setLayoutOnClick(boardListViewItem, url, 1);

		return boardListViewItem;
	}

	private LinearLayout setLayoutOnClick(LinearLayout boardListViewItem,
			final String title, final int page) {
		OnClickListener boardClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("click", "threadpage");
				startThreadActivity(title, page);
			}

		};
		boardListViewItem.setOnClickListener(boardClick);
		return boardListViewItem;
	}

	protected void startThreadActivity(String thread, int page) {
		Intent intent = new Intent(context, ThreadActivity.class);
		intent.putExtra("thread", thread);
		intent.putExtra("page", page);
		context.startActivity(intent);
	}

	public void addItem(String url, String name, String views, String replies,
			String author, String date) {
		BoardItem item = new BoardItem(url, name, views, replies, author, date);
		threadItems.add(item);
	}

	public void clear() {
		threadItems.clear();
	}

}
