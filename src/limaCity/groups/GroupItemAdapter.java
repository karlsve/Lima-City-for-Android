package limaCity.groups;

import java.util.ArrayList;

import limaCity.App.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupItemAdapter extends BaseAdapter {

	ArrayList<String> groupItems = new ArrayList<String>();
	Context mContext = null;

	public GroupItemAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return groupItems.size();
	}

	@Override
	public Object getItem(int position) {
		return groupItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout friendListItem;

		if (convertView == null) {
			friendListItem = (LinearLayout) LayoutInflater.from(mContext)
					.inflate(R.layout.groupelementlayout, parent, false);
		} else {
			friendListItem = (LinearLayout) convertView;
		}

		TextView name = (TextView) friendListItem
				.findViewById(R.id.textViewGroupName);

		String content = groupItems.get(position);
		name.setText(content);

		return friendListItem;
	}

	public void addGroupItem(String name) {
		groupItems.add(name);
	}

	public void clear() {
		groupItems.clear();
	}

}
