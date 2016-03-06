package zemmahi.youssef.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by mahdi on 16-02-29.
 */
public class SignUp extends AppCompatActivity {
    private Button btnCreateAccount = null;
    private Button btnBackMainMenu = null;
    double fCurrentLatitude = 0.0;
    double fCurrentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnBackMainMenu = (Button) findViewById(R.id.btnBackToMainMenu);


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fCurrentLatitude == 0.0 || fCurrentLongitude == 0.0) {
                    Intent intent = new Intent(getApplicationContext(), FindLocation.class);
                    startActivity(intent);
                }

                Toast.makeText(SignUp.this, "Longitude : " + fCurrentLongitude + "Latitude : " + fCurrentLatitude, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserList.class);
                startActivity(intent);
                finish();

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
}
