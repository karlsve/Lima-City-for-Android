package limaCity.base;

import limaCity.App.R;
import limaCity.chat.ChatActivity;
import limaCity.forum.ForumActivity;
import limaCity.friends.FriendsActivity;
import limaCity.groups.GroupsActivity;
import limaCity.profile.ProfileActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MainActivity extends BasicActivity 
{
	private String user = "";
	private String pass = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainlayout);
		initVariables();
		initProfileActivity();
		initFriendsActivity();
		initGroupsActivity();
		initForumActivity();
		initChatActivity();
	}

	private void initChatActivity() {
	    LinearLayout chatItem = (LinearLayout) findViewById(R.id.LinearLayoutMainPageChatClick);
	    OnClickListener chatClick = new OnClickListener()
	    {
		public void onClick(View v)
		{
		    Log.d("click", "chatPage");
		    startChatActivity();
		}
	    };
	    
	    chatItem.setOnClickListener(chatClick);
	}

	protected void startChatActivity() {
	    Intent intent = new Intent(this, ChatActivity.class);
	    intent.putExtra("user", user);
	    intent.putExtra("pass", pass);
	    startActivity(intent);
	}

	private void initVariables() {
		Bundle extra = this.getIntent().getExtras();
		user = extra.getString("user");
		pass = extra.getString("pass");
	}
	
	private void initForumActivity() {
		LinearLayout forumItem = (LinearLayout) findViewById(R.id.LinearLayoutMainPageForumClick);
		OnClickListener forumClick = new OnClickListener()
		{
			public void onClick(View v)
			{
				Log.d("click", "forumPage");
				startForumActivity();
			}
		};
		
		forumItem.setOnClickListener(forumClick);
	}

	protected void startForumActivity() {
		Intent intent = new Intent(this, ForumActivity.class);
		intent.putExtra("user", user);
		intent.putExtra("pass", pass);
		startActivity(intent);
	}

	private void initGroupsActivity() {
	    LinearLayout      groupsItem  =
		(LinearLayout) findViewById(R.id.LinearLayoutMainPageGroupsClick);
	    OnClickListener   profileClick = new OnClickListener() {
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
		intent.putExtra("user", user);
		intent.putExtra("pass", pass);
		startActivity(intent);
	}

	private void initFriendsActivity() {
	    LinearLayout      friendsItem  =
		(LinearLayout) findViewById(R.id.LinearLayoutMainPageFriendsClick);
	    OnClickListener   profileClick = new OnClickListener() {
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
		intent.putExtra("user", user);
		intent.putExtra("pass", pass);
		startActivity(intent);
	}

	private void initProfileActivity() {
	    LinearLayout      profileItem = 
		(LinearLayout) findViewById(R.id.linearLayoutMainPageProfileClick);
	    OnClickListener   profileClick = new OnClickListener() {
		public void onClick(View v) {
		    Log.d("click", "profilePage");
		    startProfileActivity();
		}
	    };

	    profileItem.setOnClickListener(profileClick);
	}
	
	protected void startProfileActivity()
	{
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("profile", user);
		intent.putExtra("user", user);
		intent.putExtra("pass", pass);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainmenu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
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
