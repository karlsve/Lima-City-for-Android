package limaCity.chat;

import java.lang.ref.WeakReference;

import android.os.Binder;

public class ChatBinder extends Binder {
    private WeakReference<ChatService> service;

    public ChatBinder(ChatService service) {
	this.service = new WeakReference<ChatService>(service);
    }

    public ChatService getService() {
	return this.service.get();
    }
}
