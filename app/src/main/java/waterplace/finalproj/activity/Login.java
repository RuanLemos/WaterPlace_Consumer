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
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            if (task.isSuccessful()) {
                DatabaseReference usersRef = database.child("Users");
                usersRef.orderByChild("email").equalTo(email)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(Login.this, "Conta inválida para esta aplicação", Toast.LENGTH_SHORT).show();
                                } else {
                                    goMenu();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle Realtime Database query cancellation
                            }
                        });
                //goMenu();
            } else {
                Exception e = task.getException();
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*public void login() {
        String email = ((android.widget.EditText)findViewById(R.id.input_email)).getText().toString();
        String password = ((android.widget.EditText)findViewById(R.id.input_password)).getText().toString();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                if (signInMethods != null && !signInMethods.isEmpty()) {
                    // A conta existe, faça o login
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(loginTask -> {
                        if (loginTask.isSuccessful()) {
                            // Login bem-sucedido, vá para a próxima tela
                            goMenu();
                        } else {
                            // Login falhou, exiba uma mensagem de erro para o usuário
                            Exception e = loginTask.getException();
                            Toast.makeText(Login.this, "Falha no login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // A conta não existe, exiba uma mensagem de erro para o usuário
                    Toast.makeText(Login.this, "Conta não encontrada", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Ocorreu um erro ao verificar a existência da conta
                Exception e = task.getException();
                Toast.makeText(Login.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    } */
}