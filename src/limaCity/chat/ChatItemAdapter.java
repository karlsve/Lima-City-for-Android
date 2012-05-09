package limaCity.chat;

import java.util.ArrayList;

import limaCity.App.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatItemAdapter extends BaseAdapter {

    ArrayList<ChatMessage> chatItems = new ArrayList<ChatMessage>();
    Context context;
    
    public ChatItemAdapter(Context context)
    {
	this.context = context;
    }
    
    @Override
    public int getCount() {
	return chatItems.size();
    }

    @Override
    public Object getItem(int position) {
	return chatItems.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	LinearLayout chatListViewItem;
	
	if(convertView == null)
	{
	    chatListViewItem = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.chatelementlayout, parent, false);
	}
	else
	{
	    chatListViewItem = (LinearLayout) convertView;
	}
	
	ChatMessage current = chatItems.get(position);
	TextView name = (TextView) chatListViewItem.findViewById(R.id.textViewChatListItemUser);
	TextView content = (TextView) chatListViewItem.findViewById(R.id.textViewChatListItemContent);
	
	name.setText(current.getFrom());
	content.setText(current.getBody());
	
	return chatListViewItem;
    }

    public void add(ChatMessage message) {
	chatItems.add(message);
    }

    public void setChatItems(ArrayList<ChatMessage> history) {
	chatItems.addAll(history);
    }

}
