package limaCity.services;

import java.util.ArrayList;
import java.util.Iterator;

import limaCity.App.R;
import limaCity.base.BasicData;
import limaCity.chat.ChatActivity;
import limaCity.chat.ChatMessage;
import limaCity.chat.ChatUser;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ChatService extends Service {

	private NotificationManager nm;
	
	public interface ChatListener {
		public void onError(String errorMessage);
		public void onSubjectChanged(String subject, String from);
		public void onMessageReceived(ChatMessage chatMessage);
		public void onUserlistChanged();
	}
	
	public class ChatBinder extends Binder {
		public ChatService getService() {
			return ChatService.this;
		}
	}
	
	private final IBinder binder = new ChatBinder();
	
	private XMPPConnection connection = null;
	private MultiUserChat muc = null;
	
	private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();
	private ArrayList<ChatUser> chatUser = new ArrayList<ChatUser>();
	private ArrayList<ChatListener> chatListener = new ArrayList<ChatListener>();
	
	@Override
	public void onCreate() {
		this.nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("ChatService", "Started with start id "+startId+": "+intent);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		connectThread();
		return binder;
	}
	
	private void connectThread() {
		new Thread()
		{
			@Override
			public void run()
			{
				connect();
			}
		}.start();
	}
	
	private void connect() {
		if(getAuthExists())
		{
			if(connection == null)
			{
				String url = this.getString(R.string.limaChatUrl);
				connection = new XMPPConnection(url);
			}
			if(!connection.isConnected())
			{
				try {
					connection.connect();
				} catch (Exception e) {
					onError("Error connecting to chat server.");
				}
			}
			String username = this.getAuthUsername();
			String password = this.getAuthPassword();
			if(!connection.isAuthenticated())
			{
				try {
					connection.login(username, password);
				} catch (Exception e) {
					onError("Error login in to chat server.");
				}
			}
			if(connection.isAuthenticated())
			{
				String room = this.getString(R.string.chatRoom)+"@conference."+this.getString(R.string.limaChatUrl);
				muc = new MultiUserChat(connection, room);
				setMucListener();
				DiscussionHistory history = new DiscussionHistory();
				history.setMaxStanzas(5);
				try {
					muc.join(username, password, history, SmackConfiguration.getPacketReplyTimeout());
				} catch (Exception e) {
					this.onError("Error joining the MultiUserChat.");
				}
				getOccupants();
			}
		}
	}
	
	public void getHistory(ChatListener listener) {
		for(ChatMessage message : chatHistory)
		{
			if(listener != null)
				listener.onMessageReceived(message);
		}
	}

	private void setMucListener() {
		muc.addMessageListener(new PacketListener() {

			@Override
			public void processPacket(Packet packet) {
				onMessageReceived(packet);
			}
			
		});
		
		muc.addParticipantListener(new PacketListener() {

			@Override
			public void processPacket(Packet packet) {
				getOccupants();
			}

		});
		
		muc.addSubjectUpdatedListener(new SubjectUpdatedListener() {

			@Override
			public void subjectUpdated(String subject, String from) {
				sendSubjectToListener(subject, from);
			}
			
		});
	}

	protected void onMessageReceived(Packet packet) {
		Log.d("LimaCityChat", "Message received");
		Message message = (Message) packet;
		if(message.getFrom().matches(".+/{1}.+"))
		{
			String nick = message.getFrom().replaceAll(".*/", "");
			ChatMessage chatmessage = new ChatMessage(message.getBody(), nick);
			chatHistory.add(chatmessage);
			
			updateServiceNotification(nick, message.getBody());
			
			sendMessageToListener(chatmessage);
		}
	}
	
	private void updateServiceNotification(String title, String content)
	{
		Intent notificationIntent = new Intent(this, ChatActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT));
		builder.setContentText(content);
		builder.setContentTitle(title);
		nm.notify(0, builder.build());
	}

	private void getOccupants() {
		chatUser.clear();
		Iterator<String> occupants = muc.getOccupants();
		for(String occupant = ""; occupants.hasNext(); occupant = occupants.next())
		{
			if(!occupant.equals(""))
				chatUser.add(getUser(occupant));
		}
	}

	private ChatUser getUser(String nick) {
		Occupant current = muc.getOccupant(nick);
		ChatUser chatUser = new ChatUser(current.getNick(), nick,
		current.getRole());
		return chatUser;
	}
	
	private boolean getAuthExists()
	{
		AccountManager am = AccountManager.get(this.getApplicationContext());
		
		Account[] accounts = am.getAccountsByType(BasicData.ACCOUNT_TYPE);
		return accounts.length>0;
	}
	
	private String getAuthUsername()
	{
		AccountManager am = AccountManager.get(this.getApplicationContext());
		
		Account[] accounts = am.getAccountsByType(BasicData.ACCOUNT_TYPE);
		Account account = accounts[0];
		return account.name;
	}
	
	private String getAuthPassword()
	{
		AccountManager am = AccountManager.get(this.getApplicationContext());
		
		Account[] accounts = am.getAccountsByType(BasicData.ACCOUNT_TYPE);
		Account account = accounts[0];
		return am.getPassword(account);
	}
	
	private void onError(String message)
	{
		sendErrorToListener(message);
	}

	private void sendMessageToListener(ChatMessage message)
	{
		for(ChatListener listener : chatListener)
		{
			if(listener != null)
				listener.onMessageReceived(message);
		}
	}
	
	private void sendErrorToListener(String message)
	{
		for(ChatListener listener : chatListener)
		{
			if(listener != null)
				listener.onError(message);
		}
	}
	
	private void sendSubjectToListener(String subject, String from)
	{
		for(ChatListener listener : chatListener)
		{
			if(listener != null)
				listener.onSubjectChanged(subject, from);
		}
	}
	
	public void addListener(ChatListener listener) {
		chatListener.add(listener);
	}

	public void sendMessage(String string) {
		try {
			muc.sendMessage(string);
		} catch(Exception e) {
			onError("Unable to send the Message");
		}
	}
}
