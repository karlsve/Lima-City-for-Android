package limaCity.serverstatus;

import java.util.Hashtable;

import limaCity.App.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServerStatusItemAdapter extends BaseAdapter {
	Hashtable<String, String> serverStatusItems = new Hashtable<String, String>();
	Context mContext;

	public ServerStatusItemAdapter(Context context) {
		mContext = context;
	}

	public void addItem(String key, String value) {
		serverStatusItems.put(key, value);
	}

	public int getCount() {
		return serverStatusItems.size();
	}

	public Object getItem(int position) {
		return serverStatusItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout serverStatusItem;

		if (convertView == null) {
			serverStatusItem = (LinearLayout) LayoutInflater.from(mContext)
					.inflate(R.layout.serverstatuselementlayout, parent, false);
		} else {
			serverStatusItem = (LinearLayout) convertView;
		}

		TextView title = (TextView) serverStatusItem
				.findViewById(R.id.textViewServerStatusItemTitle);
		TextView content = (TextView) serverStatusItem
				.findViewById(R.id.textViewServerStatusItemContent);

		Object[] keys = serverStatusItems.keySet().toArray();
		if (keys[position] instanceof String) {
			String key = (String) keys[position];
			String keyprepared = key;
			title.setText(keyprepared);
			String value = serverStatusItems.get(key);
			content.setText(value);
		}
		return serverStatusItem;
	}

	public void clear() {
		serverStatusItems.clear();
	}

}
