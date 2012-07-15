package limaCity.chat;

import java.util.ArrayList;

import limaCity.App.R;
import android.os.Bundle;
import android.widget.ListView;

public class UserActivity extends BasicChatActivity {

    private UserItemAdapter userItemAdapter = null;
    private ListView userList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	this.setContentView(R.layout.chatuserlayout);
	this.layoutInitDone = true;
    }

    @Override
    protected void boundToChat() {
	while (!this.layoutInitDone)
	    ;
	userList = (ListView) this.findViewById(R.id.listViewChatPageUserList);
	userItemAdapter = new UserItemAdapter(this);
	userItemAdapter.setUser(this.manager.getUser());
	userList.setAdapter(userItemAdapter);
    }

    @Override
    protected void userListChanged(ArrayList<ChatUser> user) {
	userItemAdapter.setUser(user);
    }

}
