package limaCity.chat;


public interface ChatListener 
{
    public void onMessageReceived(ChatMessage message);
    public void onSubjectChanged(String subject, String from);
    public void onUserListChanged();
}
