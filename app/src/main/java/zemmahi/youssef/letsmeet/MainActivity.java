package zemmahi.youssef.letsmeet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnSignIn = null;
    private Button btnSignUP = null;
    private Button btnQuit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUP = (Button) findViewById(R.id.btnSignUp);
        btnQuit = (Button) findViewById(R.id.btnQuit);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseGroup.class);
                startActivity(intent);

            }
        });
        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);

            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
}
