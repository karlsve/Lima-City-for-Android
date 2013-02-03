package limaCity.chat;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ChatActivity extends BasicActivity {
	private ChatItemAdapter chatItemAdapter = null;
	private ListView chatList = null;

	private EditText input = null;
	private Button inputButton = null;

	private Boolean hideSoftKeyboard = true;

	private InputMethodManager imm = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatlayout);
	}

	@Override
	protected void initChatData() {
		super.initChatData();
		chatItemAdapter = new ChatItemAdapter(this);
		chatList = (ListView) findViewById(R.id.listViewChatPageLayoutContentOutput);
		chatList.setAdapter(chatItemAdapter);
		chatItemAdapter.notifyDataSetChanged();
		imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		input = (EditText) findViewById(R.id.editTextChatPageContentInput);
		inputButton = (Button) findViewById(R.id.buttonChatPageContentInput);
		input.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() == 0) {
					inputButton.setClickable(false);
				} else {
					inputButton.setClickable(true);
				}
			}
		});
		inputButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMessage(input.getText().toString());
				input.setText("");
				if (hideSoftKeyboard) {
					imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
				}
			}
		});
		chatService.getHistory(chatListener);
	}

	protected void sendMessage(String string) {
		chatService.sendMessage(string);
	}

	@Override
	protected void onDataReceived(ChatMessage message) {
		if (chatItemAdapter != null) {
			chatItemAdapter.add(message);
			chatItemAdapter.notifyDataSetChanged();
			chatList.smoothScrollToPosition(chatItemAdapter.getCount() - 1);
		}
	}
	
	@Override
	protected void onDataReceived(String subject, String from)
	{
		TextView subjectview = (TextView) findViewById(R.id.textViewChatSubject);
		subjectview.setText(subject);
	}
	
	@Override
	protected void onChatDisconnected()
	{
		if(chatItemAdapter != null)
		{
			chatItemAdapter.clearChatItems();
			chatItemAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.chatmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.chatmenurecitem:
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