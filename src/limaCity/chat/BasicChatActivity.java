package limaCity.chat;

import java.util.ArrayList;

import limaCity.base.BasicActivity;
import android.os.Bundle;

public class BasicChatActivity extends BasicActivity {

    protected Boolean layoutInitDone = false;
    protected ChatManager manager = null;

    ChatManagerListener listener = new ChatManagerListener() {

	@Override
	public void onConnected() {
	    boundToChat();
	}

	@Override
	public void onMessageReceived(ChatMessage message) {
	    messageReceived(message);
	}

	@Override
	public void onSubjectUpdate(String subject) {
	    subjectUpdateReceived(subject);
	}

	@Override
	public void onUserListChanged(ArrayList<ChatUser> user) {
	    userListChanged(user);
	}

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	bindToChat();
    }

    @Override
    public void onResume() {
	super.onResume();
	if (manager != null)
	    manager.reconnect();
	else
	    bindToChat();
    }

    @Override
    public void onPause() {
	unbindFromChat();
	super.onPause();
    }

    @Override
    public void onDestroy() {
	unbindFromChat();
	super.onDestroy();
    }

    protected void bindToChat() {
	manager = new ChatManager(this, listener);
	manager.bindToChat();
    }

    protected void boundToChat() {

    }

    protected void messageReceived(ChatMessage message) {

    }

    protected void subjectUpdateReceived(String subject) {

    }

    protected void userListChanged(ArrayList<ChatUser> user) {

    }

    private void unbindFromChat() {
	manager.unbindFromChat();
    }

}
