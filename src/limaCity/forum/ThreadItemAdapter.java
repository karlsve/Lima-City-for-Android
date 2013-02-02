package limaCity.forum;

import java.util.ArrayList;

import limaCity.App.R;
import limaCity.tools.XmlWorker;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThreadItemAdapter extends BaseAdapter {

	ArrayList<PostItem> postItems = new ArrayList<PostItem>();

	Context context = null;

	public ThreadItemAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return postItems.size();
	}

	@Override
	public Object getItem(int index) {
		return postItems.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PostItem item = postItems.get(position);
		String type = XmlWorker.htmlToText(item.getPostType());
		boolean postdeleted = type == "deleted";

		LinearLayout threadListItem;
		if (convertView == null) {
			if (postdeleted) {
				threadListItem = (LinearLayout) LayoutInflater.from(context)
						.inflate(R.layout.postdeletedlayout, parent, false);
			} else {
				threadListItem = (LinearLayout) LayoutInflater.from(context)
						.inflate(R.layout.postlayout, parent, false);
			}
		} else {
			threadListItem = (LinearLayout) convertView;
		}

		boolean userdeleted = item.isUserDeleted();
		String usernametext = XmlWorker.htmlToText(item.getUserName());
		String creationtimetext = XmlWorker.htmlToText(item.getCreationTime());
		String contenttext = XmlWorker.htmlToText(item.getContent());
		int postid = item.getPostId();

		TextView content = (TextView) threadListItem
				.findViewById(R.id.textViewPostContent);
		content.setText(contenttext);
		TextView username = (TextView) threadListItem
				.findViewById(R.id.textViewPostUsername);
		if (userdeleted)
			username.setPaintFlags(username.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
		username.setText(usernametext);
		TextView creationtime = (TextView) threadListItem
				.findViewById(R.id.textViewPostCreationTime);
		creationtime.setText(creationtimetext);

		threadListItem = setLayoutOnClick(threadListItem, postid);

		return threadListItem;
	}

	private LinearLayout setLayoutOnClick(LinearLayout threadListItem,
			final int page) {
		OnClickListener boardClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("click", "boardpage");
				startPostActivity(page);
			}

		};
		threadListItem.setOnClickListener(boardClick);
		return threadListItem;
	}

	protected void startPostActivity(int page) {
		// TODO Auto-generated method stub

	}

	public void clear() {
		postItems.clear();
	}

	public void addItem(String type, String date, String content, int id,
			String username, boolean userdeleted) {
		PostItem item = new PostItem(content, username, date, type, id,
				userdeleted);
		postItems.add(item);
	}

}
