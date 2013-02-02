package limaCity.accountManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticationService extends Service {

	private static AccountAuthenticator accountAuthenticator = null;
	
	public AuthenticationService()
	{
		super();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		if(intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT))
		{
			ret = new AccountAuthenticator(this).getIBinder();
		}
		return ret;
	}
	
	@SuppressWarnings("unused")
	private AccountAuthenticator getAuthenticator()
	{
		if(accountAuthenticator == null)
		{
			accountAuthenticator = new AccountAuthenticator(this);
		}
		return accountAuthenticator;
	}

}
