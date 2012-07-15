package limaCity.chat;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import limaCity.App.R;
import limaCity.base.BasicData;
import limaCity.connection.ConnectionChangeListener;
import limaCity.connection.ConnectionChangeReceiver;
import limaCity.tools.Crypto;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class ChatService extends Service {

    private ArrayList<ChatMessage> history = new ArrayList<ChatMessage>();
    private ArrayList<ChatUser> user = new ArrayList<ChatUser>();
    private String username, password, masterkey;

    private Timer timer = new Timer();
    private final int REC_INTERVAL = 2000;
    private int rec_tries = 0;

    private ChatListener chatListener;

    XMPPConnection conn = null;
    MultiUserChat muc = null;

    ConnectionChangeListener connectionListener = new ConnectionChangeListener() {

	@Override
	public void onConnectionTypeChanged() {
	    tryToReconnect();
	}

    };

    public void setChatListener(ChatListener listener) {
	chatListener = listener;
    }

    protected void checkForReconnect() {

    }

    @Override
    public IBinder onBind(Intent intent) {
	return new ChatBinder(this);
    }

    @Override
    public void onCreate() {
	initNotification();
	initConnectionListener();
	initChat();
    }

    private void initConnectionListener() {
	new ConnectionChangeReceiver(connectionListener);
    }

    @Override
    public void onDestroy() {
	muc.leave();
	conn.disconnect();
    }

    private void initChat() {
	try {
	    getSharedPrefs();
	    connectToServer();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void connectToServer() throws Exception {
	String url = getString(R.string.limaChatUrl);
	String resource = getString(R.string.chatResource);
	ConnectionConfiguration config = prepareConfig(url);
	if (conn == null) {
	    conn = new XMPPConnection(config);
	}
	conn.connect();
	if (conn.isConnected()) {
	    conn.login(username, password, resource);
	    connectToMuc();
	}
    }

    protected void tryToReconnect() {

	timer.scheduleAtFixedRate(new TimerTask() {
	    @Override
	    public void run() {
		if (rec_tries < 20) {
		    rec_tries++;
		    try {
			reconnect();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		} else {
		    this.cancel();
		}
	    }
	}, 0, REC_INTERVAL);
    }

    private ConnectionConfiguration prepareConfig(String url) {
	ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(
		url);
	if (Integer.valueOf(Build.VERSION.SDK) >= 14) {
	    connectionConfiguration.setTruststoreType("AndroidCAStore");
	    connectionConfiguration.setTruststorePassword(null);
	    connectionConfiguration.setTruststorePath(null);
	} else {
	    connectionConfiguration.setTruststoreType("BKS");
	    String path = System.getProperty("javax.net.ssl.trustStore");
	    if (path == null)
		path = System.getProperty("java.home") + File.separator + "etc"
			+ File.separator + "security" + File.separator
			+ "cacerts.bks";
	    connectionConfiguration.setTruststorePath(path);
	}
	return connectionConfiguration;
    }

    private void connectToMuc() throws Exception {
	if (conn.isAuthenticated()) {
	    String room = getString(R.string.chatRoom) + "@conference."
		    + getString(R.string.limaChatUrl);
	    muc = new MultiUserChat(conn, room);
	    setMucListener();
	    DiscussionHistory discHist = new DiscussionHistory();
	    discHist.setMaxStanzas(5);
	    muc.join(username, password, discHist,
		    SmackConfiguration.getPacketReplyTimeout());
	    getOccupants();
	}
    }

    private void setMucListener() throws Exception {
	muc.addMessageListener(new PacketListener() {

	    @Override
	    public void processPacket(Packet packet) {
		Message message = (Message) packet;
		if (message.getFrom().matches(".+/{1}.+")) {
		    String nick = message.getFrom().replaceAll(".*/", "");
		    ChatMessage chatMessage = new ChatMessage(
			    message.getBody(), nick);
		    history.add(chatMessage);
		    if (chatListener != null)
			chatListener.onMessageReceived(chatMessage);
		}
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
		if (chatListener != null)
		    chatListener.onSubjectChanged(subject, from);
	    }

	});

    }

    protected void getOccupants() {
	user.clear();
	for (Iterator<String> i = muc.getOccupants(); i.hasNext();) {
	    String userid = i.next();
	    user.add(getUser(userid));
	}
    }

    protected ChatUser getUser(String nick) {
	Occupant current = muc.getOccupant(nick);
	ChatUser chatUser = new ChatUser(current.getNick(), nick,
		current.getRole());
	return chatUser;
    }

    protected void reconnect() {
	try {
	    reconnectToXMPP();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    reconnectToMuc();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (conn.isConnected() && conn.isAuthenticated() && muc.isJoined())
	    timer.cancel();
    }

    private void reconnectToXMPP() throws Exception {
	if (!conn.isConnected()) {
	    conn.connect();
	}
	if (conn.isConnected()) {
	    conn.login(username, password);
	}
    }

    protected void reconnectToMuc() throws Exception {
	if (muc.isJoined())
	    muc.leave();
	muc = null;
	connectToMuc();
    }

    private void getSharedPrefs() throws Exception {
	SharedPreferences settings = getSharedPreferences(BasicData.PREFS_NAME,
		0);
	Boolean loggedIn = settings.getBoolean("loggedIn", false);
	if (loggedIn) {
	    username = settings.getString("user", "");
	    String encryptedPassword = settings.getString("pass", "");
	    masterkey = settings.getString("masterkey", "");
	    password = Crypto.decrypt(masterkey, encryptedPassword);
	}
	Log.d("username", username);
	Log.d("password", password);
    }

    private void initNotification() {
	Notification notification = new Notification(R.drawable.icon,
		getText(R.string.name), System.currentTimeMillis());
	Intent notificationIntent = new Intent(this,
		limaCity.base.MainActivity.class);
	notificationIntent.putExtra("user", username);
	notificationIntent.putExtra("pass", password);
	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		notificationIntent, 0);
	notification.setLatestEventInfo(this, getText(R.string.name),
		"Chat läuft", pendingIntent);
	startForeground(1337, notification);
    }

    public ArrayList<ChatMessage> getHistory() {
	return history;
    }

    public ArrayList<ChatUser> getUser() {
	return user;
    }

    public ChatSubject getSubject() {
	ChatSubject subject;
	if (muc != null)
	    subject = new ChatSubject(muc.getSubject(), "");
	else
	    subject = new ChatSubject("", "");
	return subject;
    }

    public void sendMessage(String text) {
	try {
	    // ChatMessage message = new ChatMessage(text, this.username);
	    muc.sendMessage(text);
	    // this.chatListener.onMessageReceived(message);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
