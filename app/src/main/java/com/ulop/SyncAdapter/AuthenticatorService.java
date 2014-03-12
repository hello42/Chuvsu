package com.ulop.SyncAdapter;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    private static final String TAG = "GenericAccountService";
    private static final String ACCOUNT_TYPE = "com.ulop.SyncAdapter";
    public static final String ACCOUNT_NAME = "sync";


    private Authenticator mAuthenticator;

    public AuthenticatorService() {
    }

    public static Account GetAccount() {
        // Note: Normally the account name is set to the user's identity (username or email
        // address). However, since we aren't actually using any user accounts, it makes more sense
        // to use a generic string in this case.
        //
        // This string should *not* be localized. If the user switches locale, we would not be
        // able to locate the old account, and may erroneously register multiple accounts.
        final String accountName = ACCOUNT_NAME;
        return new Account(accountName, ACCOUNT_TYPE);
    }

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mAuthenticator.getIBinder();
    }
}
