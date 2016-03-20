package inf8405.tp2.letsmeet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by mahdi on 16-02-29.
 */
public class SignUp extends AppCompatActivity {


    // UI references.

    private EditText mEdTxtEmail;
    private EditText mEdTxtPassword;
    private EditText mEdTxtComfirmPassword;
    private Button btnCreateAccount = null;
    private Button btnBackMainMenu = null;
    private Button btnSetImage = null;
    private ImageView viewImage = null;
    private Bitmap bp = null;


    // Declare the fields
    double fCurrentLatitude;
    double fCurrentLongitude;
    private String fEmail, fPassword, fConfirmPass;
    private Bitmap fPhoto;
    private boolean fUserInsertSuccessful = false;
    private boolean fPassCaseEmpty = false, fEmailCaseEmpty = false, fEmailInvalide = false;
    private boolean fConfirmPassCaseEmpty = false, fPassNotConfirm = false, fPassInvalide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccountSU);
        btnBackMainMenu = (Button) findViewById(R.id.btnBackToMainMenuSU);
        btnSetImage = (Button) findViewById(R.id.btnSetImgSU);

        mEdTxtEmail = (EditText) findViewById(R.id.edtTxtEmailSU);
        mEdTxtPassword = (EditText) findViewById(R.id.edtTxtPasswordSU);
        mEdTxtComfirmPassword = (EditText) findViewById(R.id.edtTxtConfirmPasswordSU);
        Bundle latitudeRecu = getIntent().getExtras();
        fCurrentLatitude = latitudeRecu.getDouble("fCurrentLatitude");
        Bundle longitudeRecu = getIntent().getExtras();
        fCurrentLongitude = longitudeRecu.getDouble("fCurrentLongitude");

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUp.this, "Longitude : " + fCurrentLongitude + "Latitude : " + fCurrentLatitude, Toast.LENGTH_LONG).show();
                attemptLogin();
            }
        });

        viewImage = (ImageView) findViewById(R.id.imgVwProfileImgSU);

        btnSetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        if (savedInstanceState != null) {
            bp = savedInstanceState.getParcelable("bitmap");
        }

        if (bp != null) {
            viewImage.setImageBitmap(bp);
        }
        btnBackMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bitmap", bp);
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                bp = Bitmap.createScaledBitmap(thumbnail,64,64,false);
                Log.w("Image alpha: ", viewImage.getImageAlpha() + "");
                viewImage.setImageBitmap(bp);
                Log.w("Image alpha: ", viewImage.getImageAlpha()   +"");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Bitmap bp = Bitmap.createScaledBitmap(thumbnail,64,64,false);
                viewImage.setImageBitmap(bp);
            }
        }
    }

    private void attemptLogin() {
        View focusView = null;
        boolean cancel = false;
        fEmailCaseEmpty = fPassCaseEmpty = fPassInvalide = fEmailInvalide = fConfirmPassCaseEmpty = fPassNotConfirm = fUserInsertSuccessful = false;

        // Reset errors.
        mEdTxtEmail.setError(null);
        mEdTxtPassword.setError(null);
        mEdTxtComfirmPassword.setError(null);

        // Store values at the time of the login attempt.

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
        if (!isPasswordConfirmed(fPassword, fConfirmPass)) {
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

        if (!fPassCaseEmpty && !fPassInvalide && !fConfirmPassCaseEmpty && !fPassNotConfirm
                && !fEmailCaseEmpty && !fEmailInvalide) {
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
                if(focusView == null)
                    focusView = mEdTxtEmail;
                focusView.requestFocus();
            } else if (!fUserInsertSuccessful) {
                Toast.makeText(getApplicationContext(),
                        "Registration failed! try again, please!",
                        Toast.LENGTH_LONG).show();
                if(focusView == null)
                    focusView = mEdTxtEmail;
                focusView.requestFocus();
            }
        }

    }

    private boolean isPasswordConfirmed(String password, String confirmPass) {
        return password.matches(confirmPass);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /****************************************************************/
    private class ConnectionCode extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // Créer un nouveau utilisateur pour valider sign in

            String response = DBContent.getInstance().CreerNouvelUtilisateur(fEmail,fPassword);
            /* Ici on vérifie la validité de la création de nouvel utilisateur */
            if(response.contentEquals(Constants.UserAdded)){
                fUserInsertSuccessful = true;
                DBContent.getInstance().getActualUser().setPosition(new Position(fCurrentLatitude, fCurrentLongitude));
            }

            return null;

        }
    }

}
