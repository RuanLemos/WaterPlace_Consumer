package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import waterplace.finalproj.R;
import waterplace.finalproj.model.User;

public class Login extends AppCompatActivity {
    private ImageButton bt;
    private Button bt_log;
    private Button bt_rec;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        bt = (ImageButton) findViewById(R.id.back_arrow);
        bt_log = (Button) findViewById(R.id.bt_log_2);
        bt_rec = (Button) findViewById(R.id.bt_recover);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        bt_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        bt_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goRecover();
            }
        });
    }

    public void goBack(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void goMenu(){
        Intent i = new Intent(this,MainMenu.class);
        startActivity(i);
    }

    public void goRecover(){
        Intent i = new Intent(this, Recover.class);
        startActivity(i);
    }

    public void login(){
        String email = ((android.widget.EditText)findViewById(R.id.input_email)).getText().toString();
        String password = ((android.widget.EditText)findViewById(R.id.input_password)).getText().toString();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                    usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                goMenu();
                            } else {
                                Toast.makeText(Login.this, "Conta inválida para esta aplicação", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Exception e = task.getException();
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}