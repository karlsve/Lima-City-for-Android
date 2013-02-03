package limaCity.base;

import limaCity.App.R;
import limaCity.accountManager.AuthenticatorActivity;
import limaCity.chat.ChatMessage;
import limaCity.services.ChatService;
import limaCity.services.ChatService.ChatListener;
import limaCity.services.SessionService;
import limaCity.services.SessionService.SessionListener;

import org.jsoup.nodes.Document;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class BasicActivity extends SherlockActivity {

	protected String user = "";
	
	protected ChatService chatService = null;
	private boolean chatServiceBound = false;
	
	private ServiceConnection chatConnection = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			chatService = ((ChatService.ChatBinder)service).getService();
			chatServiceBound = true;
			initChatData();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			chatService = null;
			chatServiceBound = false;
		}
		
	};
	
	protected ChatListener chatListener = new ChatListener()
	{
		@Override
		public void onMessageReceived(final ChatMessage message) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onDataReceived(message);
				}
			
			});
		}

		@Override
		public void onSubjectChanged(final String subject, final String from) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onDataReceived(subject, from);
				}
			
			});
		}

		@Override
		public void onError(final String errorMessage) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onDataError(errorMessage);
				}
				
			});
		}

		@Override
		public void onUserlistChanged() {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onDataUserlistChanged();
				}
			
			});
		}

		@Override
		public void onDisconnect() {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onChatDisconnected();
				}
			
			});
		}
	};
	
	protected SessionService sessionService = null;
	private boolean sessionServiceBound = false;
	
	private ServiceConnection sessionConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			sessionService = ((SessionService.SessionBinder)service).getService();
			sessionServiceBound = true;
			initData();
			getData();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			sessionService = null;
			sessionServiceBound = false;
		}
		
	};
	
	protected void doBindService() {
		bindService(new Intent(this.getApplicationContext(), SessionService.class), sessionConnection, Context.BIND_AUTO_CREATE);
		bindService(new Intent(this.getApplicationContext(), ChatService.class), chatConnection, Context.BIND_AUTO_CREATE);
	}

	protected void doUnbindService() {
		if(sessionServiceBound) {
			unbindService(sessionConnection);
		}
		if(chatServiceBound)
		{
			unbindService(chatConnection);
		}
	}
	
	protected SessionListener sessionListener = new SessionListener()
	{

		@Override
		public void onUsernameReceived(String username)
		{
			user = username;
		}
		
		@Override
		public void onLogin(String username, String password, String sessionId) {
		}

		@Override
		public void onError(final String errorMessage) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onDataError(errorMessage);
				}
				
			});
		}

		@Override
		public void onDocumentReceived(final Document document) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onDataReceived(document);
				}
				
			});
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doBindService();
	}

	protected void initData() {
		checkAccount();
		sessionService.setListener(sessionListener);
		sessionService.getUsername();
	}
	
	protected void initChatData() {
		chatService.addListener(chatListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		doBindService();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		doUnbindService();
	}
	
	protected void checkAccount() {
		AccountManager am = AccountManager.get(this.getApplicationContext());
		Account[] accounts = am.getAccountsByType(BasicData.ACCOUNT_TYPE);
		if(accounts.length==0)
		{
			Intent intent = new Intent(this, AuthenticatorActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.activitymenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemRefresh:
			refreshPage();
			break;
		case R.id.menuItemLogout:
			logout();
			break;
		}
		return true;
	}

	public void logout() {
		this.finish();
	}

	public void showInfo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.currentVersion)).setTitle(
				getString(R.string.info));
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void refreshPage() {
		getData();
	}

	protected void getData() {

	}

	protected void onDataError(String errorMessage) {
		Toast msg = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
		msg.show();
	}
	
	protected void onDataReceived(Document document) {
		
	}
	
	protected void onDataReceived(ChatMessage message) {
		
	}
	
	protected void onDataReceived(String subject, String from)
	{
		
	}

	protected void onDataUserlistChanged() {
	}
	
	protected void onChatDisconnected() {
		
	}
}
