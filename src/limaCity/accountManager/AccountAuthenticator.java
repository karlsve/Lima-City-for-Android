package limaCity.accountManager;

import limaCity.App.R;
import limaCity.tools.DataBuilder;
import limaCity.tools.ServerHandling;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;

public class AccountAuthenticator extends AbstractAccountAuthenticator {

	private Context context;
	
	public AccountAuthenticator(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
			String authTokenType, String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {

		final Bundle result;
		final Intent intent;
		
		intent = new Intent(this.context, AuthenticatorActivity.class);
		intent.putExtra(Constants.ACCOUNT_TYPE, authTokenType);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		
		result = new Bundle();
		result.putParcelable(AccountManager.KEY_INTENT, intent);
		
		return result;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
			String authTokenType, Bundle options) throws NetworkErrorException {
		final Bundle result;
		result = new Bundle();
		
		AccountManager am = AccountManager.get(this.context);
		String username = account.name;
		String password = am.getPassword(account);
		
		String url = (String) this.context.getText(R.string.limaServerUrl);

		JSONObject json = new JSONObject();
		try {
			json.put("username", username);
			json.put("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Document document = ServerHandling.postFromServer(url, DataBuilder.stringArray("proc", "login", "args", json.toString()));
		
		Elements loginNodes = document.select(this.context.getText(R.string.nodeNameLoggedin).toString());
		if(loginNodes.first().text().equals(this.context.getText(R.string.nodeContentLoggedinPositive)))
		{
			String sessionKey = document.select(this.context.getText(R.string.nodeNameSession).toString()).first().text();
			result.putString("user", username);
			result.putString("authToken", sessionKey);
		}
		
		return result;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
			String[] features) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

}
