package limaCity.forum;

import java.util.LinkedHashMap;

import limaCity.App.R;
import limaCity.tools.XmlWorker;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BoardItemAdapter extends BaseAdapter {

    LinkedHashMap<String, BoardItem> threadItems = new LinkedHashMap<String, BoardItem>();
    Context context;
    
    public BoardItemAdapter(Context context)
    {
    	this.context = context;
    }
    
    @Override
    public int getCount() {
	return threadItems.size();
    }

    @Override
    public Object getItem(int position) {
	String[] keys = new String[0];
	keys = threadItems.keySet().toArray(keys);
	String key = keys[position];
	return threadItems.get(key);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	LinearLayout boardListViewItem;
	
	if(convertView == null)
	{
	    boardListViewItem = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.boardelementlayout, parent, false);
	}
	else
	{
	    boardListViewItem = (LinearLayout) convertView;
	}
		
	TextView name = (TextView) boardListViewItem.findViewById(R.id.textViewBoardElementText);
	String[] keys = new String[0];
	keys = threadItems.keySet().toArray(keys);
	String key = keys[position];
	BoardItem item = threadItems.get(key);
	String title = XmlWorker.htmlToText(item.getName());
	name.setText(title);
	
	return boardListViewItem;
    }
	
    public void addItem(String url, String name, String views, String replies, String author, String date)
    {
	BoardItem item = new BoardItem(url, name, views, replies, author, date);
	threadItems.put(url, item);
    }

    public void clear() {
	threadItems.clear();
    }

}
