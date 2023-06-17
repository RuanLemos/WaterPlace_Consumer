package waterplace.finalproj.activity;

import static java.sql.DriverManager.println;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import waterplace.finalproj.R;
import waterplace.finalproj.model.Address;
import waterplace.finalproj.model.User;
import waterplace.finalproj.util.AddressUtil;
import waterplace.finalproj.util.BottomNavigationManager;

public class RegisterEdit extends AppCompatActivity {
    private User user = User.getInstance();
    private Address address = user.getAddresses().get(0);
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");;

    private BottomNavigationManager bottomNavigationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register_edit);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_footer);
        bottomNavigationView.setSelectedItemId(R.id.conta);
        bottomNavigationManager = new BottomNavigationManager(this);
        bottomNavigationManager.setupBottomNavigation(bottomNavigationView);

        setCampos();
    }

    public boolean verifyPhone(String phoneNumber){
        String phonePattern = "^[0-9]{2} [0-9]{9}$";
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (matcher.matches()){
            return true;
        } else {
            showError((EditText) findViewById(R.id.input_telefone), (TextView) findViewById(R.id.error_2));

            return false;
        }
    }

    public void showError(EditText inputField, TextView verificationText) {
        inputField.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF9494")));
        verificationText.setVisibility(View.VISIBLE);
    }

    private void pushEdit() {
        EditText name = findViewById(R.id.input_nome);
        EditText phone = findViewById(R.id.input_telefone);
        EditText Rua = findViewById(R.id.input_rua);
        EditText Numero = findViewById(R.id.input_num);
        EditText Complemento = findViewById(R.id.input_comp);
        EditText cep = findViewById(R.id.input_cep2);

        Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();

            user.setName(name.getText().toString());
            user.setPhone(phone.getText().toString());
            address.setAvenue(Rua.getText().toString());
            address.setNum(Integer.parseInt(Numero.getText().toString()));
            address.setComp(Complemento.getText().toString());
            address.setCep(Integer.parseInt(cep.getText().toString()));

            String urlAddress = address.getAvenue() + " " + address.getCity() + " " + address.getCep();
            double[] latlong = AddressUtil.geocode(urlAddress);
            address.setLatitude(latlong[0]);
            address.setLongitude(latlong[1]);

            double[] coords = AddressUtil.geocode(address.getAvenue() + " " + address.getNum());
            if (coords != null) {
                address.setLatitude(coords[0]);
                address.setLongitude(coords[1]);
            }
            user.setAddresses(null);
            // Salva o usuário com o UID como identificador do documento
            /*usersRef.child(uid).setValue(user)
                .addOnCompleteListener(saveTask -> {
                    if (saveTask.isSuccessful()) {
                        System.out.println("ENTRANDO NA TOCA DO DIABO");
                        usersRef.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String addressUid = usersRef.child(uid).child("Addresses").push().getKey();
                                usersRef.child(uid).child("Addresses").child(addressUid).setValue(address);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Trate o erro de acordo com sua necessidade
                            }
                        });
                        Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Exception e = saveTask.getException();
                        Toast.makeText(this, "bugo!", Toast.LENGTH_SHORT).show();
                    }
                });*/
        }
    }

    public void setCampos() {
        EditText name = findViewById(R.id.input_nome);
        name.setText(user.getName());

        EditText phone = findViewById(R.id.input_telefone);
        phone.setText(user.getPhone());

        EditText DataNasc = findViewById(R.id.input_date);
        DataNasc.setText(user.getBirthdate().toString());

        EditText Rua = findViewById(R.id.input_rua);
        Rua.setText(address.getAvenue());

        EditText Numero = findViewById(R.id.input_num);
        Numero.setText(Integer.toString(address.getNum()));

        EditText Complemento = findViewById(R.id.input_comp);
        Complemento.setText(address.getComp());

        EditText cep = findViewById(R.id.input_cep2);
        cep.setText(Integer.toString(address.getCep()));

        String telefone = phone.getText().toString();

        Button btnSalvar = findViewById(R.id.bt_reg_2);
        btnSalvar.setOnClickListener(v -> {
            if (verifyPhone(telefone)){
                pushEdit();
            }
        });
    }
}