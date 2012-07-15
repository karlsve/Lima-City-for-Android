package limaCity.forum;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

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

public class ForumItemAdapter extends BaseAdapter {

    ArrayList<String> forumItemNames = new ArrayList<String>();
    LinkedHashMap<String, ForumItem> forumItems = new LinkedHashMap<String, ForumItem>();
    Context context = null;
    String user = "";
    String pass = "";

    public ForumItemAdapter(Context context, String user, String pass) {
	this.context = context;
	this.user = user;
	this.pass = pass;
    }

    @Override
    public int getCount() {
	return forumItemNames.size();
    }

    @Override
    public Object getItem(int position) {
	return forumItemNames.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	LinearLayout forumListItem;
	if (convertView == null)
	    forumListItem = (LinearLayout) LayoutInflater.from(context)
		    .inflate(R.layout.forumelementlayout, parent, false);
	else
	    forumListItem = (LinearLayout) convertView;

	String url = forumItemNames.get(position);
	String nametext = XmlWorker.htmlToText(forumItems.get(url).getName());
	String descriptiontext = XmlWorker.htmlToText(forumItems.get(url)
		.getDescription());
	String moderatorstext = forumItems.get(url).getModerators();

	TextView name = (TextView) forumListItem
		.findViewById(R.id.textViewBoardTitle);
	name.setText(nametext);
	TextView description = (TextView) forumListItem
		.findViewById(R.id.textViewBoardDescription);
	description.setText(descriptiontext);
	TextView moderators = (TextView) forumListItem
		.findViewById(R.id.textViewBoardModerator);
	moderators.setText(moderatorstext);
	forumListItem = setLayoutOnClick(forumListItem, url, "1");

	return forumListItem;
    }

    private LinearLayout setLayoutOnClick(LinearLayout forumListItem,
	    final String url, final String page) {
	OnClickListener boardClick = new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Log.d("click", "boardpage");
		startBoardActivity(url, page);
	    }

	};
	forumListItem.setOnClickListener(boardClick);
	return forumListItem;
    }

    protected void startBoardActivity(String board, String page) {
	Intent intent = new Intent(context, BoardActivity.class);
	intent.putExtra("user", user);
	intent.putExtra("pass", pass);
	intent.putExtra("board", board);
	intent.putExtra("page", page);
	context.startActivity(intent);
    }

    public void addFriendItem(String url, String name, String description,
	    String moderators) {
	Hashtable<String, String> output = new Hashtable<String, String>();
	output.put("name", name);
	output.put("description", description);
	output.put("moderators", moderators);
	forumItemNames.add(url);
	ForumItem item = new ForumItem(url, name, description, moderators);
	forumItems.put(url, item);
    }

    public void clear() {
	forumItems.clear();
    }

}
