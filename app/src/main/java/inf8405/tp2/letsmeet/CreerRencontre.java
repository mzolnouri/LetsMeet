package inf8405.tp2.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)
public class CreerRencontre extends AppCompatActivity {
    // UI references.
    private EditText mEdtTxtLieu1;
    private EditText mEdtTxtLieu2;
    private EditText mEdtTxtLieu3;
    private EditText mEdtTxtDescptn;
    private String mLieu1;
    private String mLieu2;
    private String mLieu3;
    private String Descptn;
    private Button mBtnCreerRencontre = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rencontre);

        mBtnCreerRencontre = (Button) findViewById(R.id.btnCreerRencontreR);
        mBtnCreerRencontre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveData();

            }
        });

    }

    private void retrieveData() {

        boolean cancel = false;
        mEdtTxtLieu1 = (EditText) findViewById(R.id.edtTxtLieu1R);
        mEdtTxtLieu2 = (EditText) findViewById(R.id.edtTxtLieu2R);
        mEdtTxtLieu3 = (EditText) findViewById(R.id.edtTxtLieu3R);
        mEdtTxtDescptn = (EditText) findViewById(R.id.edtTxtDescR);

        // Reset errors.
        mEdtTxtLieu1.setError(null);
        mEdtTxtLieu2.setError(null);
        mEdtTxtLieu3.setError(null);
        mEdtTxtDescptn.setError(null);

        // Store values at the time of the create meeting.
        mLieu1 = mEdtTxtLieu1.getText().toString();
        mLieu2 = mEdtTxtLieu2.getText().toString();
        mLieu3 = mEdtTxtLieu3.getText().toString();
        Descptn = mEdtTxtDescptn.getText().toString();

        View focusView = null;

        // Check for a not null lieu text,
        if (TextUtils.isEmpty(mLieu1)) {
            mEdtTxtLieu1.setError(getString(R.string.error_field_required));
            focusView = mEdtTxtLieu1;
            cancel = true;
        }
        if (TextUtils.isEmpty(mLieu2)) {
            mEdtTxtLieu2.setError(getString(R.string.error_field_required));
            focusView = mEdtTxtLieu2;
            cancel = true;
        }
        if (TextUtils.isEmpty(mLieu3)) {
            mEdtTxtLieu3.setError(getString(R.string.error_field_required));
            focusView = mEdtTxtLieu3;
            cancel = true;
        }
        if(!cancel){
            Toast.makeText(getApplicationContext(),
                    "Rencontre créée!",
                    Toast.LENGTH_LONG).show();
            //Intent i = new Intent(getBaseContext(), ChooseGroup.class);
            //startActivity(i);
            if(Descptn == null)
                Descptn = "";
            DBContent.getInstance().creerRencontre(mLieu1, mLieu2, mLieu3,Descptn);

            finish();

        }else{
            if(focusView == null)
                focusView = mEdtTxtLieu1;
            focusView.requestFocus();
        }
    }
}
