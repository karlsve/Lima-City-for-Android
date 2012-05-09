package limaCity.chat;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ChatManager
{
    
    private Context context = null;
    private static Context staticContext = null;
    
    Boolean boundToChat = false;
    
    private ChatService chatService = null;
    
    private ChatManagerListener managerListener = null;
    
    private ChatListener listener = new ChatListener()
    {

	@Override
	public void onMessageReceived(ChatMessage message) {
	    messageReceived(message);
	}

	@Override
	public void onSubjectChanged(String subject, String from) {
	    subjectChanged(subject, from);
	}

	@Override
	public void onUserListChanged() {
	    
	}
	
    };
    
    ServiceConnection serviceConnection = new ServiceConnection()
    {

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
	    ChatBinder binder = (ChatBinder) service;
	    chatService = binder.getService();
	    chatService.setChatListener(listener);
	    boundToChat = true;
	    managerListener.onConnected();
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
	    
	}
    };

    
    public ChatManager(Context context, ChatManagerListener chatManagerListener)
    {
	this.context = context;
	this.managerListener = chatManagerListener;
    }
    
    public ChatSubject getSubject()
    {
	if(boundToChat)
	    return chatService.getSubject();
	else
	    return new ChatSubject("", "");
    }
    
    protected void subjectChanged(String subject, String from) {
	listener.onSubjectChanged(subject, from);
    }

    protected void messageReceived(ChatMessage message) 
    {
	if(managerListener != null)
	    managerListener.onMessageReceived(message);
    }
    
    public ArrayList<ChatMessage> getHistory()
    {
	if(boundToChat)
	    return chatService.getHistory();
	else
	    return new ArrayList<ChatMessage>();
    }
    
    public static void startChat(Context activity)
    {
	staticContext = activity;
	Intent service = new Intent(activity, ChatService.class);
	activity.startService(service);
    }
    
    public ArrayList<ChatUser> getUser()
    {
	if(boundToChat)
	    return chatService.getUser();
	else
	    return new ArrayList<ChatUser>();
    }

    public void bindToChat() 
    {
	Intent service = new Intent(context, ChatService.class);
	context.bindService(service, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    
    public void unbindFromChat()
    {
	if(boundToChat)
	    context.unbindService(serviceConnection);
	boundToChat = false;
    }
    
    public static void stopChat()
    {
	if(staticContext != null)
	{
    		Intent service = new Intent(staticContext, ChatService.class);
    		staticContext.stopService(service);
	}
    }

    public void sendMessage(String text) {
	if(boundToChat)
	    chatService.sendMessage(text);
    }

    public void reconnect() {
	if(boundToChat)
	    chatService.reconnect();
    }
}
