package com.dinamic.ivan.parsers;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dinamic.ivan.exc.GoogleAuthError;
import com.dinamic.ivan.BuildConfig;
import com.dinamic.ivan.entities.Expense;
import com.dinamic.ivan.R;
import com.dinamic.ivan.entities.Sale;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.koushikdutta.async.future.FutureCallback;

import java.util.Arrays;
import java.util.List;


public class GmailParser extends BaseParser {
    private static class Options {
        boolean useHistory = true;
        boolean useIncomming = true;

        FutureCallback<List<Expense>> onExpenses;
        FutureCallback<List<Sale>>    onSales;
    }

    public static class Builder {
        private Options opts = new Options();

        public Builder useHistory  (Boolean use) { opts.useHistory   = (use == null ? opts.useHistory   : use); return this; }
        public Builder useIncomming(Boolean use) { opts.useIncomming = (use == null ? opts.useIncomming : use); return this; }

        public Builder onExpenses(FutureCallback<List<Expense>> callback) { opts.onExpenses = callback; return this; }
        public Builder onSales   (FutureCallback<List<Sale>>    callback) { opts.onSales    = callback; return this; }

        public GmailParser build(Context context) {
            return new GmailParser(context, opts);
        }
    }

    private final static String LOG_TAG = "GOOGLE_API";
    private final static int REUQEST_CODE_GOOGLE_SIGN_IN = 2;
    private final static List<String> GOOGLE_API_SCOPES = Arrays.asList(
        "https://www.googleapis.com/auth/gmail.readonly"
    );

    private Options opts = new Options();

    public GmailParser(Context context, Options opts) {
        super(context);

        this.opts = (opts == null ? this.opts : opts);

        if (opts.useHistory)
            parseGmail();

        // TODO: on new messages
    }

    public void parseGmail() {
        GoogleSignInOptions.Builder signinOptionsBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);

        for (String scope: GOOGLE_API_SCOPES)
            signinOptionsBuilder.requestScopes(new Scope(scope));

        signinOptionsBuilder.requestServerAuthCode(context.getString(R.string.server_web_client_id), false);
        signinOptionsBuilder.requestIdToken(context.getString(R.string.server_web_client_id));

        GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signinOptionsBuilder.build())
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        ((Activity) context).startActivityForResult(signInIntent, REUQEST_CODE_GOOGLE_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if (requestCode == REUQEST_CODE_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                if (BuildConfig.DEBUG)
                    Log.i(LOG_TAG, "Auth success: " + result.getStatus());

                analyser.analyseGmail(account);
            }
            else {
                if (BuildConfig.DEBUG)
                    Log.e(LOG_TAG, "Account auth fail: " + result.getStatus());

                throw new GoogleAuthError();
            }
        }
    }
}
