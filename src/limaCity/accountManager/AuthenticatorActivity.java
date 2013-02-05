package limaCity.accountManager;

import org.jsoup.nodes.Document;

import limaCity.App.R;
import limaCity.base.BasicData;
import limaCity.services.SessionService;
import limaCity.services.SessionService.SessionListener;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	
	public static final String PARAM_AUTHTOKEN_TYPE = "auth.token";
	
	private SessionService sessionService;
	private boolean sessionServiceBound = false;
	
	private ServiceConnection sessionConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			sessionService = ((SessionService.SessionBinder)service).getService();
			sessionServiceBound = true;
			sessionService.setListener(sessionListener);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			sessionService = null;
			sessionServiceBound = false;
		}
		
	};
	
	private void doBindService() {
		bindService(new Intent(this.getApplicationContext(), SessionService.class), sessionConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void doUnbindService() {
		if(sessionServiceBound) {
			unbindService(sessionConnection);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticationlayout);
		doBindService();
		initListener();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		doUnbindService();
	}

	private void initListener() {
		EditText viewUsername = (EditText) this.findViewById(R.id.editTextAuthUsername);
		EditText viewPassword = (EditText) this.findViewById(R.id.editTextAuthPassword);
		
		viewUsername.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable editable) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
		});
		
		viewPassword.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable editable) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
		});
	}

	protected void enableSubmitIfReady() {
		boolean usernameReady = ((EditText) this.findViewById(R.id.editTextAuthUsername)).getText().toString().length()>2;
		boolean passwordReady = ((EditText) this.findViewById(R.id.editTextAuthPassword)).getText().toString().length()>5;
		
		if(usernameReady && passwordReady)
		{
			((Button) this.findViewById(R.id.buttonAuthLogin)).setClickable(true);
		}
		else
		{
			((Button) this.findViewById(R.id.buttonAuthLogin)).setClickable(false);
		}
	}

	public void onCancelClick(View v) {
		this.finish();
	}
	
	private SessionListener sessionListener = new SessionListener()
	{

		@Override
		public void onLogin(final String username, final String password, final String sessionId) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onAuthSuccessfull(username, password, sessionId);
				}
				
			});
		}

		@Override
		public void onError(final String errorMessage) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable(){

				@Override
				public void run() {
					onAuthError(errorMessage);
				}
				
			});
		}

		@Override
		public void onDocumentReceived(Document document) {
			
		}

		@Override
		public void onUsernameReceived(String username) {
			
		}
		
	};
	
	public void onSaveClick(View v)
	{
		EditText viewUsername = (EditText) this.findViewById(R.id.editTextAuthUsername);
		EditText viewPassword = (EditText) this.findViewById(R.id.editTextAuthPassword);
		
		final String username = viewUsername.getText().toString();
		final String password = viewPassword.getText().toString();
		
		new Thread()
		{
			@Override
			public void run() {
				sessionService.doLogin(username, password);
			}
		}.start();
	}

	protected void onAuthError(String errorMessage) {
		Toast msg = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
		msg.show();
	}

	protected void onAuthSuccessfull(String username, String password, String sessionId) {
		String accountType = this.getIntent().getStringExtra(PARAM_AUTHTOKEN_TYPE);
		if(accountType == null)
		{
			accountType = BasicData.ACCOUNT_TYPE;
		}
		
		final Account account = new Account(username, accountType);
		AccountManager am = AccountManager.get(this);
		am.addAccountExplicitly(account, password, null);
		
		final Intent intent = new Intent();
		intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
		intent.putExtra(AccountManager.KEY_AUTHTOKEN, sessionId);
		this.setAccountAuthenticatorResult(intent.getExtras());
		this.setResult(RESULT_OK, intent);
		this.finish();
	}
}
