package limaCity.chat;

import limaCity.services.ChatService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ChatBroadcastReceiver extends BroadcastReceiver {
	
	private static int networkType = -1;
	
	public static void initNetworkStatus(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		networkType = -1;
		if(info != null) {
			if(info.isConnected()) {
				networkType = info.getType();
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("BroadcastReceiver", "Network change Received");
		if(intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
			Intent chatServiceIntent = new Intent(context, ChatService.class);
			context.stopService(chatServiceIntent);
		} else if(intent.getAction().equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if(((info == null) && (networkType != -1)) || ((info != null) && (info.isConnected() == false) && (info.getType() == networkType))) {
				networkType = -1;
				Intent chatServiceIntent = new Intent(context, ChatService.class);
				chatServiceIntent.putExtra("disconnect", true);
				context.startService(chatServiceIntent);
			}
			if((info != null) && (info.isConnected() == true) && (info.getType() != networkType)) {
				networkType = info.getType();
				Intent chatServiceIntent = new Intent(context, ChatService.class);
				chatServiceIntent.putExtra("reconnect", true);
				context.startService(chatServiceIntent);
			}
		}
	}

}
