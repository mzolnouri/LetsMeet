package zemmahi.youssef.letsmeet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by mahdi on 16-02-29.
 */
public class SignUp extends AppCompatActivity {


    // UI references.
    private EditText mEdTxtUserName;
    private EditText mEdTxtEmail;
    private EditText mEdTxtPassword;
    private EditText mEdTxtComfirmPassword;

    private View mProgressView;
    private View mLoginFormView;

    private Button btnCreateAccount = null;
    private Button btnBackMainMenu = null;

    double fCurrentLatitude = 0.0;
    double fCurrentLongitude = 0.0;
    // Declare the fields
    private ArrayList<HashMap<String, String>> clientData;
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "password";
    private String repServer;
    private JSONObject obj;
    private static String url = "http://192.168.0.102/KWphp/Srvc_insert_new_user.php";
    private String fUserName, fUserNameRep, fEmail, fEmailRep, fPassword, fPasswordRep, fConfirmPass, fConfirmPassRep;
    private int checkJson = 0;
    private boolean fUserInsertSuccessful = false;
    private boolean fPassCaseEmpty = false, fEmailCaseEmpty = false, fEmailInvalide = false, fEmailNotFound = false;
    private boolean fConfirmPassCaseEmpty = false, fUserNameCaseEmpty = false, fPassNotConfirm = false, fPassInvalide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccountSU);
        btnBackMainMenu = (Button) findViewById(R.id.btnBackToMainMenuSU);
        mEdTxtUserName = (EditText) findViewById(R.id.edtTxtUserNameSU);
        mEdTxtEmail = (EditText) findViewById(R.id.edtTxtEmailSU);
        mEdTxtPassword = (EditText) findViewById(R.id.edtTxtPasswordSU);
        mEdTxtComfirmPassword = (EditText) findViewById(R.id.edtTxtConfirmPasswordSU);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (fCurrentLatitude == 0.0 || fCurrentLongitude == 0.0) {
////                    Intent intent = new Intent(getApplicationContext(), FindLocation.class);
////                    startActivity(intent);
//                }
//
//                Toast.makeText(SignUp.this, "Longitude : " + fCurrentLongitude + "Latitude : " + fCurrentLatitude, Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), UserList.class);
//                startActivity(intent);
//                finish();
                attemptLogin();

            }
        });
        btnBackMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void attemptLogin() {
        View focusView = null;
        boolean cancel = false;
        fEmailCaseEmpty = fPassCaseEmpty = fEmailInvalide = fEmailNotFound= fPassInvalide =  fConfirmPassCaseEmpty = fPassNotConfirm = fUserNameCaseEmpty = false ;

        // Reset errors.
        mEdTxtUserName.setError(null);
        mEdTxtEmail.setError(null);
        mEdTxtPassword.setError(null);
        mEdTxtComfirmPassword.setError(null);

        // Store values at the time of the login attempt.
        fUserName = mEdTxtUserName.getText().toString();
        fEmail = mEdTxtEmail.getText().toString();
        fPassword = mEdTxtPassword.getText().toString();
        fConfirmPass = mEdTxtComfirmPassword.getText().toString();

        // Check for a not null password, if the user entered one.
        if (TextUtils.isEmpty(fPassword)) {
            mEdTxtPassword.setError(getString(R.string.error_field_required));
            focusView = mEdTxtPassword;
            cancel = true;
            fPassCaseEmpty = true;
        }
        // Check for a valid password.
        if (!isPasswordValid(fPassword)) {
            mEdTxtPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEdTxtPassword;
            cancel = true;
            fPassInvalide = true;
        }
        // Check for a not null confirm password, if the user entered one.
        if (TextUtils.isEmpty(fConfirmPass)) {
            mEdTxtComfirmPassword.setError(getString(R.string.error_field_required));
            focusView = mEdTxtComfirmPassword;
            cancel = true;
            fConfirmPassCaseEmpty = true;
        }
        // Check for a valid confirm password .
        if (!isPasswordConfirmed(fPassword,fConfirmPass)) {
            mEdTxtComfirmPassword.setError(getString(R.string.error_password_not_matches));
            focusView = mEdTxtComfirmPassword;
            cancel = true;
            fPassNotConfirm = true;
        }
        // Check for a not null email address.
        if (TextUtils.isEmpty(fEmail) && !isEmailValid(fEmail)) {
            mEdTxtEmail.setError(getString(R.string.error_field_required));
            focusView = mEdTxtEmail;
            cancel = true;
            fEmailCaseEmpty = true;
        }
        // Check for a valid email address.
        if (!isEmailValid(fEmail)) {
            mEdTxtEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEdTxtEmail;
            cancel = true;
            fEmailInvalide = true;
        }
        // Check for a not null username.
        if (TextUtils.isEmpty(fUserName)) {
            mEdTxtUserName.setError(getString(R.string.error_field_required));
            focusView = mEdTxtUserName;
            cancel = true;
            fUserNameCaseEmpty = true;
        }
        if (!fPassCaseEmpty && !fPassInvalide && !fConfirmPassCaseEmpty && !fPassNotConfirm
                && !fEmailCaseEmpty && !fEmailInvalide && !fUserNameCaseEmpty) {
            // Creating service handler class instance
            obj = new JSONObject();
            try {
                obj.put("username", fUserName);
                obj.put("email", fEmail);
                obj.put("password", fPassword);
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

            if (fUserInsertSuccessful) {
                Toast.makeText(getApplicationContext(),
                        "Sign up successful!",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getBaseContext(), ChooseGroup.class);
                Bundle emailBundle = new Bundle();
                emailBundle.putString("mEmail", fEmail); // email
                i.putExtras(emailBundle);
                startActivity(i);
                finish();
            }
            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else if(!fUserInsertSuccessful){
                Toast.makeText(getApplicationContext(),
                        "Registration failed! try again, please!",
                        Toast.LENGTH_LONG).show();
                focusView.requestFocus();
            }
        }

    }

    private boolean isPasswordConfirmed(String password, String confirmPass) {
        return password.matches(confirmPass);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    /****************************************************************/
    private class ConnectionCode extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SignUp.this);
            pDialog.setMessage("Processing ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreqGet = new WebRequest();

            // Making a request to url and getting response
            url = "http://192.168.0.102/KWphp/Srvc_insert_new_user.php";
            url+="?username="+fUserName+"&email="+fEmail+"&password="+fPassword;
            repServer = webreqGet.makeWebServiceCall(url, WebRequest.GET, obj.toString());

            //http://192.168.0.102/KWphp/Srvc_insert_new_user.php?first_name=reza&last_name=zolnouri&email=reza@polymtl.ca&password=reza1
            //http://192.168.0.102/KWphp/Srvc_insert_user.php?first_name=m&last_name=z&email=mz@yahoo.com &password=12345

            if(repServer.matches("insert success"))
            {
                fUserInsertSuccessful = true;
            }else
                fUserInsertSuccessful = false;

            //Log.d("Response: ", "> " + repServer);

            return null;
        }
    }

}
