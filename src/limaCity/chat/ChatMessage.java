package limaCity.chat;

public class ChatMessage 
{
    private String body = "";
    private String from = "";
    
    public ChatMessage(String body, String from)
    {
	this.body = body;
	this.from = from;
    }
    
    public String getBody()
    {
	return body;
    }
    
    public String getFrom()
    {
	return from;
    }
}
