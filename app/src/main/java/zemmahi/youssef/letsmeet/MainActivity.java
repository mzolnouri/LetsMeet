package zemmahi.youssef.letsmeet;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mACTxtVwUserName;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mBtnSignIn = null;
    private Button mBtnSignUP = null;
    private Button mBtnQuit = null;

    /* Declare the fiels */
    private ArrayList<HashMap<String, String>> clientData;
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_ID = "id";
    private String repServer;
    private JSONObject obj;
    private static String url = "http://192.168.0.102/KWphp/Conncetion.php";
    private String fUsername, fUsernameRep, fPassword, fPasswordRep;
    private int checkJson = 0;
    private boolean fPassCheckedSuccessful = false;
    private boolean fUserCheckedSuccessful = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);




        mBtnSignIn = (Button) findViewById(R.id.btnSignInMMenu);
        mBtnSignUP = (Button) findViewById(R.id.btnSignUpMMenu);
        mBtnQuit = (Button) findViewById(R.id.btnQuit);
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Checking password with database */
                mACTxtVwUserName = (AutoCompleteTextView) findViewById(R.id.mACTxtVwUserNameMMenu);
                //populateAutoComplete();
                mPasswordView = (EditText) findViewById(R.id.edtTxtPasswordMMenu);
                /* We retrieve the user data from database*/
                if (mAuthTask != null) {
                    return;
                }
                // Reset errors.
                mACTxtVwUserName.setError(null);
                mPasswordView.setError(null);

                // Store values at the time of the login attempt.
                fUsername = mACTxtVwUserName.getText().toString();
                fPassword = mPasswordView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(fPassword) && !isPasswordValid(fPassword)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                    cancel = true;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(fUsername)) {
                    mACTxtVwUserName.setError(getString(R.string.error_field_required));
                    focusView = mACTxtVwUserName;
                    cancel = true;
                } else if (!isEmailValid(fUsername)) {
                    mACTxtVwUserName.setError(getString(R.string.error_invalid_email));
                    focusView = mACTxtVwUserName;
                    cancel = true;
                }
                // Creating service handler class instance
                obj = new JSONObject();
                try {
                    obj.put("email", fUsername);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Calling async task to get json
                try {
                    new ConnectionCode().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(fPassCheckedSuccessful){
                    Toast.makeText(getApplicationContext(),
                            "Password is correct :)",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getBaseContext(), ChooseGroup.class);
                    startActivity(i);
                    finish();
                }
                if(fUserCheckedSuccessful) {
                    Toast.makeText(getApplicationContext(),
                            "Your username est incorrect! Create a new account. ",
                            Toast.LENGTH_LONG).show();
                    //focusView.requestFocus();

                }else{
                    Toast.makeText(getApplicationContext(),
                            "Password is incorrect :(",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
        mBtnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();

            }
        });
        mBtnQuit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);

            }
        });
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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
            Snackbar.make(mACTxtVwUserName, "Contacts permissions are needed for providing email\n" +
                    "        completions.", Snackbar.LENGTH_INDEFINITE)
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

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mACTxtVwUserName.setAdapter(adapter);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
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
            /*********************************** Connection with database *************************************************/


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);

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
            //showProgress(false);
        }
    }
    /****************************************************************/
    /****************************************************************/
    private class ConnectionCode extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ProgressDialog pDialog;

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Connect to database for checking the password ...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreqGet = new WebRequest();

            // Making a request to url and getting response
            repServer = webreqGet.makeWebServiceCall(url, WebRequest.POST, obj.toString());

            clientData = ParseJSON(repServer);

            //Log.d("Response: ", "> " + repServer);

            return null;
        }
    }

    /**
     * Fonction pour parser le JSON
     * @param json
     * @return
     */
    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {

                // Hashmap for ListView
                ArrayList<HashMap<String, String>> userDataList = new ArrayList<HashMap<String, String>>();

                JSONArray jsonObj = new JSONArray(json);

                // tmp hashmap for single entete item
                HashMap<String, String> hashMode = new HashMap<String, String>();

                // Retrieve the retruned data
                JSONObject row = new JSONObject(jsonObj.get(0).toString());
                fPasswordRep = row.getString(TAG_PASSWORD);
                fUsernameRep = row.getString("email");   /******* change email with TAG_USERNAME after *********************************************************************/
                String idRep = row.getString(TAG_ID);
//                Toast.makeText(getApplicationContext(),
//                        "Utilisateur recived : " + fEmailRep + "Password recived : " + fPasswordRep,
//                        Toast.LENGTH_LONG).show();
                if(fPasswordRep.matches(fPassword))
                {
                    hashMode.put(TAG_USERNAME, fUsernameRep);
                    hashMode.put(TAG_PASSWORD, fPasswordRep);
                    hashMode.put(TAG_ID, idRep);
                    fPassCheckedSuccessful = true;
                }
                else
                {
                    hashMode.put(TAG_PASSWORD, fPasswordRep);
                    Toast.makeText(getApplicationContext(),
                            "Password is incorrect! I'm in ParseJson :)",
                            Toast.LENGTH_LONG).show();
                    fPassCheckedSuccessful = false;
                }
                if(fUsernameRep.matches(fUsername))
                {
                   fUserCheckedSuccessful = true;
                }
                // adding item to list
                userDataList.add(hashMode);
                return userDataList;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Password is not recived");
            return null;
        }
    }
    /****************************************************************/
}
