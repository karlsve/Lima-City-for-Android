package limaCity.chat;

public class ChatUser {
    private String nick = "";
    
    public ChatUser(String nick)
    {
	this.nick = nick;
    }
    
    public String getNick()
    {
	return nick;
    }
}
