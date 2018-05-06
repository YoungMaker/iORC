package edu.ycp.cs482.iorc.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import edu.ycp.cs482.iorc.Apollo.Query.Exception.AuthQueryException;
import edu.ycp.cs482.iorc.Apollo.Query.Exception.QueryException;
import edu.ycp.cs482.iorc.Apollo.Query.QueryControllerProvider;
import edu.ycp.cs482.iorc.CreateAccountMutation;
import edu.ycp.cs482.iorc.LoginMutation;
import edu.ycp.cs482.iorc.LogoutMutation;
import edu.ycp.cs482.iorc.R;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private static final String LOGOUT_BOOL = "LOGOUT_BOOL";
    private static final String CREATE_ACCOUNT = "CREATE_ACCOUNT";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private AutoCompleteTextView mUnameView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mCreateAcctLink;
    private boolean creatingAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(creatingAccount){
                    attemptCreateUser();
                }else {
                    attemptLogin();
                }
            }
        });

        mCreateAcctLink = findViewById(R.id.create_acct);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mUnameView = findViewById(R.id.uname);

        final Bundle extra = getIntent().getExtras();

        if(extra != null){ //if we have intent extras
            if(extra.containsKey(LOGOUT_BOOL)){ //if we have logout bool
               if(extra.getBoolean(LOGOUT_BOOL)){ //if its true
                   logoutToken();//logout
                   getIntent().removeExtra(LOGOUT_BOOL);
               }
            }
            if(extra.containsKey(CREATE_ACCOUNT)){
                if(extra.getBoolean(CREATE_ACCOUNT)){
                    //TODO: setup for create account!
                    setupForCreateAccount();
                    Log.d("CREATE_ACCT", "Login intent set for create account");
                    getIntent().removeExtra(CREATE_ACCOUNT);
                }
            }
        }

        mCreateAcctLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!creatingAccount) {
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    intent.putExtra(CREATE_ACCOUNT, true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    intent.putExtra(CREATE_ACCOUNT, false);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupForCreateAccount(){
        mUnameView.setVisibility(View.VISIBLE);
        mConfirmPasswordView.setVisibility(View.VISIBLE);
        mCreateAcctLink.setText(R.string.login_text);
        creatingAccount = true;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptCreateUser() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mUnameView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString().toLowerCase();
        final String password = mPasswordView.getText().toString();
        final String confirm_password = mConfirmPasswordView.getText().toString();
        final String uname = mUnameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        String validPassword = isPasswordValid(password);
        String validUname = isUsernameValid(uname);

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
        }

        if (TextUtils.isEmpty(uname)) {
            mUnameView.setError(getString(R.string.error_field_required));
            focusView = mUnameView;
            cancel = true;
        } else if (!validUname.equals("")) {
            String text = getString(R.string.error_invalid_username, validUname);
            mUnameView.setError(text);
            focusView = mUnameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!validPassword.equals("")) {
            String text = getString(R.string.error_invalid_password, validPassword);
            mPasswordView.setError(text);
            focusView = mPasswordView;
            cancel = true;
        } else if(!confirm_password.equals(password)){
            String text = getString(R.string.error_invalid_password, "Passwords Must Match!");
            mConfirmPasswordView.setError(text);
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            QueryControllerProvider.getInstance().getQueryController().createAccountMutation(email, password, uname)
                    .enqueue(new ApolloCall.Callback<CreateAccountMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<CreateAccountMutation.Data> response) {
                            try {
                                QueryControllerProvider.getInstance().getQueryController().parseCreateAccountMutation(response);
                                //proceed();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        attemptLogin(); //now logs you in!
                                    }
                                });
                            }catch (QueryException e){
                                popInvalidError(e.getMessage());
                            }
                            Log.d("WORKED", "LOGGED IN");
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            popCommError();
                            Log.d("LOGIN_FAILED", "communication error");
                        }
                    });
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString().toLowerCase();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            QueryControllerProvider.getInstance().getQueryController().loginQuery(email, password)
                    .enqueue(new ApolloCall.Callback<LoginMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<LoginMutation.Data> response) {
                            try {
                                QueryControllerProvider.getInstance().getQueryController().parseLoginQuery(getApplicationContext(), response);
                                proceed();
                            }catch (AuthQueryException e){
                                popInvalidError(e.getMessage());
                            }
                            Log.d("WORKED", "LOGGED IN");
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            popCommError();
                            Log.d("LOGIN_FAILED", "communication error");
                        }
                    });
        }
    }

    private boolean isEmailValid(String email) {
        //this autogen logic should be ok for us
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private String isPasswordValid(String password) {
            if ( password.length() < 8) {
                return "Too Short!";
            }
            String upperCaseChars = "(.*[A-Z].*)";
            if (!password.matches(upperCaseChars)) {
                return "Does not contain uppercase or lowercase letters";
            }
            String lowerCaseChars = "(.*[a-z].*)";
            if (!password.matches(lowerCaseChars)) {
                return "Does not contain uppercase or lowercase letters";
            }
            String numbers = "(.*[0-9].*)";
            if (!password.matches(numbers)) {
                return "Must contain one digit 0-9 and a special character i.e: ~,!,@,#,$,%,^,&,*,?";
            }
            if(password.contains(" ")){
                return "Cannot contain spaces!";
            }
            //FIXME: will not detect ` character as special. Dunno why
            String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
            if (!password.matches(specialChars)) {
                return "Must contain one digit 0-9 and a special character i.e: ~,!,@,#,$,%,^,&,*,?";
            }
            return "";
    }

    private String isUsernameValid(String password) {
        if ( password.length() < 4) {
            return "Too Short!";
        }
        if ( password.length() > 20) {
            return "Too Long!";
        }
        if(password.contains(" ")){
            return "Cannot contain spaces!";
        }
        //FIXME: will not detect ` character as special. Dunno why
        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (password.matches(specialChars)) {
            return "Cannot contain special characters!";
        }
        return "";
    }

    private void logoutToken(){
        try {
            QueryControllerProvider.getInstance().getQueryController().logoutMuation(getApplicationContext())
                    .enqueue(new ApolloCall.Callback<LogoutMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<LogoutMutation.Data> response) {
                            try {
                                QueryControllerProvider.getInstance().getQueryController().logoutMutationParse(response, getApplicationContext());
                            }catch (AuthQueryException e){
                                Log.e("LOGOUT_FAILED", "Failed to logout");
                            }catch (QueryException e) {
                                Log.e("LOGOUT_FAILED", "Logout of user failed.");
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            Log.e("LOGOUT_FAILED", "Error communicating with server");
                        }
                    });
        } catch (AuthQueryException e) {
            Log.d("LOGOUT_FAILED", "Failed to Logout");
        }
    }

    private void popInvalidError(final String err){
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Login Failed");
                alertDialog.setMessage("Login attempt failed: " + err);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                showProgress(false); //reset
            }
        });

    }

    private void popCommError(){
        //TODO: Move to fcn
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Login Failed");
                alertDialog.setMessage("Login attempt failed: Communication Failed");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                showProgress(false); //resets view
            }
        });
    }

    private void proceed(){
        Intent intent = new Intent(this, CharacterListActivity.class);
        startActivity(intent);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

