package limaCity.base;

import limaCity.App.R;
import limaCity.chat.ChatActivity;
import limaCity.friends.FriendsActivity;
import limaCity.groups.GroupsActivity;
import limaCity.profile.ProfileActivity;
import limaCity.serverstatus.ServerStatusActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends BasicActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainlayout);
		ActionBar ab = this.getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
	}
	
	@Override
	protected void initData()
	{
		super.initData();
		initProfileActivity();
		initFriendsActivity();
		initGroupsActivity();
		initForumActivity();
		initServerStatusActivity();
		initChatActivity();
	}

	private void initChatActivity() {
		LinearLayout chatItem = (LinearLayout) findViewById(R.id.LinearLayoutMainPageChatClick);
		OnClickListener chatClick = new OnClickListener() {
			public void onClick(View v) {
				Log.d("click", "chatPage");
				startChatActivity();
			}
		};

		chatItem.setOnClickListener(chatClick);
	}

	protected void startChatActivity() {
		Intent intent = new Intent(this, ChatActivity.class);
		startActivity(intent);
	}

	private void initForumActivity() {
		LinearLayout forumItem = (LinearLayout) findViewById(R.id.LinearLayoutMainPageForumClick);
		OnClickListener forumClick = new OnClickListener() {
			public void onClick(View v) {
				Log.d("click", "forumPage");
				startForumActivity();
			}
		};

		forumItem.setOnClickListener(forumClick);
	}

	protected void startForumActivity() {
	}

	private void initGroupsActivity() {
		LinearLayout groupsItem = (LinearLayout) findViewById(R.id.LinearLayoutMainPageGroupsClick);
		OnClickListener profileClick = new OnClickListener() {
			public void onClick(View v) {
				Log.d("click", "groupsPage");
				startGroupsActivity();
			}
		};

		groupsItem.setOnClickListener(profileClick);
	}

	protected void startGroupsActivity() {
		Intent intent = new Intent(this, GroupsActivity.class);
		intent.putExtra("profile", user);
		startActivity(intent);
	}

	private void initFriendsActivity() {
		LinearLayout friendsItem = (LinearLayout) findViewById(R.id.LinearLayoutMainPageFriendsClick);
		OnClickListener profileClick = new OnClickListener() {
			public void onClick(View v) {
				Log.d("click", "friendsPage");
				startFriendsActivity();
			}
		};

		friendsItem.setOnClickListener(profileClick);
	}

	protected void startFriendsActivity() {
		Intent intent = new Intent(this, FriendsActivity.class);
		intent.putExtra("profile", user);
		startActivity(intent);
	}

	private void initProfileActivity() {
		LinearLayout profileItem = (LinearLayout) findViewById(R.id.linearLayoutMainPageProfileClick);
		OnClickListener profileClick = new OnClickListener() {
			public void onClick(View v) {
				Log.d("click", "profilePage");
				startProfileActivity();
			}
		};

		profileItem.setOnClickListener(profileClick);
	}

	protected void startProfileActivity() {
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("profile", user);
		startActivity(intent);
	}

	private void initServerStatusActivity() {
		LinearLayout serverStatusItem = (LinearLayout) findViewById(R.id.LinearLayoutMainPageServerStatusClick);
		OnClickListener serverStatusClick = new OnClickListener() {
			public void onClick(View v) {
				Log.d("click", "serverStatusPage");
				startServerStatusActivity();
			}
		};

		serverStatusItem.setOnClickListener(serverStatusClick);
	}

	protected void startServerStatusActivity() {
		Intent intent = new Intent(this, ServerStatusActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.mainmenulogoutitem:
			logout();
			break;
		case R.id.mainmenuinfoitem:
			showInfo();
			break;
		}
		return true;
	}

}
