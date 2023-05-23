package waterplace.finalproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import waterplace.finalproj.R;

public class Recover extends AppCompatActivity {

    private Button bt_send;

    private ImageButton bt;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recover);

        bt_send = (Button) findViewById(R.id.bt_send);
        bt = (ImageButton) findViewById(R.id.back_arrow_2);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    public void sendEmail() {
        String email = ((android.widget.EditText) findViewById(R.id.input_email_2)).getText().toString();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email);
        goConfirm();
    }
    public void goConfirm(){
        Intent i = new Intent(this, Confirm.class);
        startActivity(i);
    }

    public void goBack(){
        Intent i = new Intent(this,Login.class);
        startActivity(i);
    }
}