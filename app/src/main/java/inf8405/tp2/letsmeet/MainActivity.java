package inf8405.tp2.letsmeet;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mBtnSignIn = null;
    private Button mBtnSignUP = null;
    private Button mBtnQuit = null;

    /* Declare the fiels */
    private String fEmail, fPassword;
//    private boolean mPassChecked = false;
//    private boolean mEmailChecked = false;
    private int mEmailPassChecked = 0; // 0: aucun n'est correct ou seulement pass est incorrect; 1:email incorrect; 2: les 2 sont corrects

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

                mEmailView = (EditText) findViewById(R.id.edtTxtEmailMMenu);
                mPasswordView = (EditText) findViewById(R.id.edtTxtPasswordMMenu);
                // Reset errors.
                mEmailView.setError(null);
                mPasswordView.setError(null);

                // Store values at the time of the login attempt.
                fEmail = mEmailView.getText().toString();
                fPassword = mPasswordView.getText().toString();

                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (!TextUtils.isEmpty(fPassword) && !isPasswordValid(fPassword)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                }

                // Check for a valid email address.
                if (TextUtils.isEmpty(fEmail)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                } else if (!isEmailValid(fEmail)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                }
                // Calling async task to get json
                try {
                    new ConnectionCode().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(mEmailPassChecked == 2){
                    Toast.makeText(getApplicationContext(),
                            "Password is correct :)",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getBaseContext(), ChooseGroup.class);
                    startActivity(i);
                    finish();
                }else if(mEmailPassChecked == 1) {
                    Toast.makeText(getApplicationContext(),
                            "Your email est incorrect! Create a new account. ",
                            Toast.LENGTH_LONG).show();
                    if(focusView == null)
                        focusView = mEmailView;
                    focusView.requestFocus();

                }else{
                    Toast.makeText(getApplicationContext(),
                            "Password is incorrect :(",
                            Toast.LENGTH_LONG).show();
                    if(focusView == null)
                        focusView = mPasswordView;
                    focusView.requestFocus();
                }
            }
        });
        mBtnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindLocation.class);
                startActivity(intent);
                finish();

            }
        });
        mBtnQuit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                finish();
//                System.exit(0);
                Intent intent = new Intent(getApplicationContext(), GoogleCalendarConnection.class);
                startActivity(intent);

            }
        });
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private class ConnectionCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            // Créer un nouveau utilisateur pour valider sign in
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setCourriel(fEmail);
            utilisateur.setPassword(fPassword);
            /* Ici on vérifie la validité des informations entrées */
            String response = DBContent.getInstance().authentification(fEmail,fPassword);
            if(response.contentEquals(Constants.AccessGranted)){
                mEmailPassChecked = 2;
            }else{
                if(response.contentEquals(Constants.WrongEmail))
                    mEmailPassChecked = 1;
                else
                    mEmailPassChecked = 0;
            }
            return null;
        }
    }

}
