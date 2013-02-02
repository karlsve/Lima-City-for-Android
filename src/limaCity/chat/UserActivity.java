package limaCity.chat;

import java.util.ArrayList;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import android.os.Bundle;
import android.widget.ListView;

public class UserActivity extends BasicActivity {

	private UserItemAdapter userItemAdapter = null;
	private ListView userList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.chatuserlayout);
	}

	@Override
	protected void initChatData() {
		userList = (ListView) this.findViewById(R.id.listViewChatPageUserList);
		userItemAdapter = new UserItemAdapter(this);
		userList.setAdapter(userItemAdapter);
	}

	protected void userListChanged(ArrayList<ChatUser> user) {
		userItemAdapter.setUser(user);
	}

}
