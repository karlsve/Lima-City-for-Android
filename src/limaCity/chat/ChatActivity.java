package limaCity.chat;

import limaCity.App.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends BasicChatActivity 
{
    private ChatItemAdapter chatItemAdapter = null;
    private ListView chatList = null;
    
    private EditText input = null;
    private Button inputButton = null;
    
    private Boolean hideSoftKeyboard = true;
    
    private InputMethodManager imm = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.chatlayout);
	layoutInitDone = true;
	initInput();
    }
    
    private void initInput() {
	imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	input = (EditText) findViewById(R.id.editTextChatPageContentInput);
	inputButton = (Button) findViewById(R.id.buttonChatPageContentInput);
	input.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {}
            @Override
            public void beforeTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {}
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() == 0) {
                    inputButton.setEnabled(false);
                } else {
                    inputButton.setEnabled(true);
                }
            }
        });
	inputButton.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v) {
		manager.sendMessage(input.getText().toString());
		input.setText("");
		if(hideSoftKeyboard)
		{
		    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
		}
	    }
	    
	});
    }

    @Override
    protected void messageReceived(ChatMessage message) {
	if(chatItemAdapter != null)
	{
	    chatItemAdapter.add(message);
	    chatItemAdapter.notifyDataSetChanged();
	    chatList.smoothScrollToPosition(chatItemAdapter.getCount() - 1);
	}
    }

    @Override
    protected void boundToChat() {
	while(!layoutInitDone);
	chatList = (ListView) findViewById(R.id.listViewChatPageLayoutContentOutput);
	chatItemAdapter = new ChatItemAdapter(this);
	chatItemAdapter.setChatItems(manager.getHistory());
	chatList.setAdapter(chatItemAdapter);
	chatItemAdapter.notifyDataSetChanged();
	
	TextView subject = (TextView) findViewById(R.id.textViewChatSubject);
	ChatSubject chatSubject = manager.getSubject();
	subject.setText(chatSubject.getSubject());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.chatmenu, menu);
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch(item.getItemId())
	{
	case R.id.chatmenurecitem:
	    if(manager != null)
		manager.reconnect();
	    break;
	case R.id.chatmenulogoutitem:
	    logout();
	    break;
	case R.id.chatmenuuserlist:
	    goToUserList();
	    break;
	}
	return true;
    }

    private void goToUserList() {
	Intent intent = new Intent(this, limaCity.chat.UserActivity.class);
	startActivity(intent);
    }
    
}