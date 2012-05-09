package limaCity.chat;

import java.util.ArrayList;


public interface ChatManagerListener {
    public void onConnected();
    public void onMessageReceived(ChatMessage message);
    public void onSubjectUpdate(String subject);
    void onUserListChanged(ArrayList<ChatUser> user);
}
