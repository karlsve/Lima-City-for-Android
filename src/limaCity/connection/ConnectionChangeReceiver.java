package limaCity.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectionChangeReceiver extends BroadcastReceiver {

    ConnectionChangeListener listener = null;

    public ConnectionChangeReceiver(ConnectionChangeListener listener) {
	this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
	if (listener != null)
	    listener.onConnectionTypeChanged();
    }

}
