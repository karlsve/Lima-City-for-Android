package limaCity.profile;

import java.util.Hashtable;

import limaCity.App.R;
import limaCity.tools.XmlWorker;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileItemAdapter extends BaseAdapter {
    Hashtable<String, String> profileItems = new Hashtable<String, String>();
    Context mContext;

    public ProfileItemAdapter(Context context) {
	mContext = context;
    }

    public void addProfileItem(String key, String value) {
	profileItems.put(key, value);
    }

    public int getCount() {
	return profileItems.size();
    }

    public Object getItem(int position) {
	return profileItems.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	LinearLayout profileListItem;

	if (convertView == null) {
	    profileListItem = (LinearLayout) LayoutInflater.from(mContext)
		    .inflate(R.layout.profileelementlayout, parent, false);
	} else {
	    profileListItem = (LinearLayout) convertView;
	}

	TextView title = (TextView) profileListItem
		.findViewById(R.id.textViewProfileItemTitle);
	TextView content = (TextView) profileListItem
		.findViewById(R.id.textViewProfileItemContent);

	Object[] keys = profileItems.keySet().toArray();
	if (keys[position] instanceof String) {
	    String key = (String) keys[position];
	    String keyprepared = XmlWorker.firstCharToUpperCase(key);
	    title.setText(keyprepared);
	    String value = profileItems.get(key);
	    content.setText(value);
	}
	return profileListItem;
    }

    public void clear() {
	profileItems.clear();
    }

}
