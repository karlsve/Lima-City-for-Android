package limaCity.chat;

import java.util.ArrayList;

import limaCity.App.R;
import limaCity.base.BasicActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
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

	private UserItemAdapter userItemAdapter = null;
	private ListView userList = null;

	private EditText input = null;
	private Button inputButton = null;
	
	private Boolean userlistShown = false;
	private Boolean animationRunning = false;
	private View mainLayout;
	private int third;
	private AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) {
			if(userlistShown)
			{
		        mainLayout.layout(mainLayout.getLeft() - third, mainLayout.getTop(), mainLayout.getLeft() - third + mainLayout.getMeasuredWidth(), mainLayout.getTop() + mainLayout.getMeasuredHeight());
		        userlistShown = false;
			}
			else
			{
				mainLayout.layout(mainLayout.getLeft() + third, mainLayout.getTop(), mainLayout.getLeft() + third + mainLayout.getMeasuredWidth(), mainLayout.getTop() + mainLayout.getMeasuredHeight());
				userlistShown = true;
			}
			animationRunning = false;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {
			animationRunning = true;
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.chatlayout);
		initAnimation();
	}
	
	@SuppressWarnings("deprecation")
	private void initAnimation() {
		mainLayout = (View)this.findViewById(R.id.linearLayoutChatPageContent);
        third = this.getWindowManager().getDefaultDisplay().getWidth() / 3;
	}

	public void toggleSlide() {
		
		if(userlistShown)
		{
	        TranslateAnimation slide = new TranslateAnimation(mainLayout.getLeft() - third, mainLayout.getLeft() - (2*third), 0, 0);
	        slide.setDuration(500);
	        slide.setFillEnabled(true);
	        slide.setAnimationListener(animationListener);
	        mainLayout.startAnimation(slide);
		}
		else
		{
	        TranslateAnimation slide = new TranslateAnimation(mainLayout.getLeft(), mainLayout.getLeft() + third, 0, 0);
	        slide.setDuration(500);
	        slide.setFillEnabled(true);
	        slide.setAnimationListener(animationListener);
	        mainLayout.startAnimation(slide);
		}
	}

	@Override
	protected void initChatData() {
		super.initChatData();
		input = (EditText) this.findViewById(R.id.editTextChatPageContentInput);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		chatItemAdapter = new ChatItemAdapter(this);
		chatList = (ListView) this.findViewById(R.id.listViewChatPageLayoutContentOutput);
		chatList.setAdapter(chatItemAdapter);
		chatItemAdapter.notifyDataSetChanged();

		userList = (ListView) this.findViewById(R.id.listViewChatPageUserList);
		userItemAdapter = new UserItemAdapter(this);
		userList.setAdapter(userItemAdapter);
		
		inputButton = (Button) this.findViewById(R.id.buttonChatPageContentInput);
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
			}
		});
		chatService.getHistory(chatListener);
		chatService.getUserlist(chatListener);
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
		TextView subjectview = (TextView) this.findViewById(R.id.textViewChatSubject);
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
	protected void onDataUserlistChanged(ArrayList<ChatUser> chatUser) {
		userItemAdapter.setUser(chatUser);
		userItemAdapter.notifyDataSetChanged();
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
		case R.id.chatmenuuseritem:
			if(!animationRunning)
				toggleSlide();
			break;
		}
		return true;
	}

}