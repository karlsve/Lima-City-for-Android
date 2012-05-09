package limaCity.friends;

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

public class FriendItemAdapter extends BaseAdapter {

	ArrayList<String> friendItems = new ArrayList<String>();
	Context mContext = null;
	
	public FriendItemAdapter(Context context)
	{
		mContext = context;
	}
	
	@Override
	public int getCount() {
		return friendItems.size();
	}

	@Override
	public Object getItem(int position) {
		return friendItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout friendListItem;
		
		if(convertView == null)
		{
			friendListItem = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.friendelementlayout, parent, false);
		}
		else
		{
			friendListItem = (LinearLayout) convertView;
		}
		
		TextView name = (TextView) friendListItem.findViewById(R.id.textViewFriendName);
		
		String content = friendItems.get(position);
		name.setText(content);
		friendListItem = setLayoutOnClick(friendListItem, content);
		return friendListItem;
	}
	
	private LinearLayout setLayoutOnClick(LinearLayout layout, final String user)
	{
		OnClickListener   profileClick = new OnClickListener() {
            public void onClick(View v) {
                Log.d("click", "profilePage");
                startProfileActivity(user);
            }
        };

        layout.setOnClickListener(profileClick);
		return layout;
	}
	
	protected void startProfileActivity(String user)
	{
		Intent intent = new Intent(mContext, ProfileActivity.class);
		intent.putExtra("profile", user);
    	mContext.startActivity(intent);
	}

	public void addFriendItem(String name) {
		friendItems.add(name);
	}

	public void clear() {
	    friendItems.clear();
	}

}
