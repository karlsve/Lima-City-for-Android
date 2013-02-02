package limaCity.services;

import limaCity.App.R;
import limaCity.base.BasicData;
import limaCity.tools.DataBuilder;
import limaCity.tools.ServerHandling;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class SessionService extends Service {
	
	@SuppressWarnings("unused")
	private NotificationManager nm;
	
	public interface SessionListener {
		public void onLogin(String username, String password, String sessionId);
		public void onDocumentReceived(Document document);
		public void onError(String errorMessage);
		public void onUsernameReceived(String username);
	}
	
	public class SessionBinder extends Binder {
		public SessionService getService() {
			return SessionService.this;
		}
	}
	
	private final IBinder binder = new SessionBinder();
	
	SessionListener listener = null;
	
	@Override
	public void onCreate() {
		this.nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("SessionService", "Started with start id "+startId+": "+intent);
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public void doLogin(String username, String password)
	{
		String url = (String) this.getText(R.string.limaServerUrl);

		JSONObject json = new JSONObject();
		try {
			json.put("username", username);
			json.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String[] data = DataBuilder.stringArray("proc", "login", "args", json.toString());
		Document document = ServerHandling.postFromServer(url, data);
		
		if(document == null)
		{
			if(listener != null)
				listener.onError("Server request returned empty object.");
		}
		else
		{
			Elements loginNodes = document.select(this.getText(R.string.nodeNameLoggedin).toString());
			if(loginNodes.first().text().equals(this.getText(R.string.nodeContentLoggedinNegative)))
			{
				if(listener != null);
					listener.onError("Login not successfull.");
			}
			else
			{
				Elements sessionNodes = document.select(this.getText(R.string.nodeNameSession).toString());
				String sessionId = sessionNodes.first().text();
				if(listener != null)
					listener.onLogin(username, password, sessionId);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void getProfile(String user)
	{
		String url = (String) this.getText(R.string.limaServerUrl);
		
		AccountManager am = AccountManager.get(this.getApplicationContext());
		
		Account[] accounts = am.getAccountsByType(BasicData.ACCOUNT_TYPE);
		
		if(accounts.length>0)
		{
			Account account = accounts[0];
			AccountManagerCallback<Bundle> callback = null;
			Handler handler = null;
			AccountManagerFuture<Bundle> amfb = am.getAuthToken(account, account.type, false, callback, handler);
			try
			{
				Bundle bundle = amfb.getResult();
				String authToken = bundle.getString("authToken");
				
				JSONObject json = new JSONObject();
				try {
					json.put("sid", authToken);
					json.put("user", user);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				String[] data = DataBuilder.stringArray("proc", "getAboutOfProfile", "args", json.toString());
				
				Document document = ServerHandling.postFromServer(url, data);
				if(document.select("notloggedin").size()>0)
				{
					if(listener != null)
						listener.onError("User not logged in.");
				}
				else
				{
					if(listener != null)
						listener.onDocumentReceived(document);
				}
			}
			catch(Exception e)
			{
				if(listener != null)
					listener.onError(e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void getFriends(String user)
	{
		String url = (String) this.getText(R.string.limaServerUrl);
		
		AccountManager am = AccountManager.get(this.getApplicationContext());
		
		Account[] accounts = am.getAccountsByType(BasicData.ACCOUNT_TYPE);
		
		if(accounts.length>0)
		{
			Account account = accounts[0];
			AccountManagerCallback<Bundle> callback = null;
			Handler handler = null;
			AccountManagerFuture<Bundle> amfb = am.getAuthToken(account, account.type, false, callback, handler);
			try
			{
				Bundle bundle = amfb.getResult();
				String authToken = bundle.getString("authToken");
				
				JSONObject json = new JSONObject();
				try {
					json.put("sid", authToken);
					json.put("user", user);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				String[] data = DataBuilder.stringArray("proc", "getFriendsOfProfile", "args", json.toString());
				
				Document document = ServerHandling.postFromServer(url, data);
				if(document.select("notloggedin").size()>0)
				{
					if(listener != null)
						listener.onError("User not logged in.");
				}
				else
				{
					if(listener != null)
						listener.onDocumentReceived(document);
				}
			}
			catch(Exception e)
			{
				if(listener != null)
					listener.onError(e.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void getGroups(String user) {
		String url = (String) this.getText(R.string.limaServerUrl);
		
		AccountManager am = AccountManager.get(this.getApplicationContext());
		
		Account[] accounts = am.getAccountsByType(BasicData.ACCOUNT_TYPE);
		
		if(accounts.length>0)
		{
			Account account = accounts[0];
			AccountManagerCallback<Bundle> callback = null;
			Handler handler = null;
			AccountManagerFuture<Bundle> amfb = am.getAuthToken(account, account.type, false, callback, handler);
			try
			{
				Bundle bundle = amfb.getResult();
				String authToken = bundle.getString("authToken");
				
				JSONObject json = new JSONObject();
				try {
					json.put("sid", authToken);
					json.put("user", user);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				String[] data = DataBuilder.stringArray("proc", "getGroupsOfProfile", "args", json.toString());
				
				Document document = ServerHandling.postFromServer(url, data);
				if(document.select("notloggedin").size()>0)
				{
					if(listener != null)
						listener.onError("User not logged in.");
				}
				else
				{
					if(listener != null)
						listener.onDocumentReceived(document);
				}
			}
			catch(Exception e)
			{
				if(listener != null)
					listener.onError(e.getMessage());
			}
		}
	}
	
	public void getServerStatus()
	{
		String url = (String) this.getText(R.string.limaServerUrl);
				
		String[] data = DataBuilder.stringArray("proc", "getServerStatus");
		
		Document document = ServerHandling.postFromServer(url, data);
		if(listener != null)
			listener.onDocumentReceived(document);
	}
	
	public void getUsername() {
		AccountManager am = AccountManager.get(this.getApplicationContext());
		
		Account[] accounts = am.getAccountsByType(BasicData.ACCOUNT_TYPE);
		
		if(accounts.length>0)
		{
			Account account = accounts[0];
			if(listener != null)
				listener.onUsernameReceived(account.name);
		}
		else
		{
			Log.d("user", "Username could not be loaded.");
			if(listener != null)
				listener.onError("Username could not be loaded.");
		}
	}

	public void setListener(SessionListener sessionListener) {
		listener = sessionListener;
	}

}
