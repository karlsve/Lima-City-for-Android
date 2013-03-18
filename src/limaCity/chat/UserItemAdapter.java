package limaCity.chat;

import java.util.ArrayList;

import limaCity.App.R;
import limaCity.profile.ProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserItemAdapter extends BaseAdapter {

	private ArrayList<ChatUser> userItems = new ArrayList<ChatUser>();
	Context context;

	public UserItemAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return userItems.size();
	}

	@Override
	public Object getItem(int position) {
		return userItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout userListViewItem;

		if (convertView == null) {
			userListViewItem = (LinearLayout) LayoutInflater.from(context)
					.inflate(R.layout.chatuserelementlayout, parent, false);
		} else {
			userListViewItem = (LinearLayout) convertView;
		}

		ChatUser current = userItems.get(position);
		TextView name = (TextView) userListViewItem
				.findViewById(R.id.textViewUserListItemName);

		name.setText(current.getNick());
		
		userListViewItem = setLayoutOnClick(userListViewItem, current.getUsername());

		return userListViewItem;
	}
	
	private LinearLayout setLayoutOnClick(LinearLayout layout, final String user) {
		OnClickListener profileClick = new OnClickListener() {
			public void onClick(View v) {
				Log.d("click", "profilePage");
				startProfileActivity(user);
			}
		};

		layout.setOnClickListener(profileClick);
		return layout;
	}

	protected void startProfileActivity(String user) {
		Intent intent = new Intent(context, ProfileActivity.class);
		intent.putExtra("profile", user);
		context.startActivity(intent);
	}

	public void addUser(ChatUser user) {
		userItems.add(user);
	}

	public void setUser(ArrayList<ChatUser> user) {
		userItems.clear();
		userItems = user;
		this.notifyDataSetChanged();
	}

}
