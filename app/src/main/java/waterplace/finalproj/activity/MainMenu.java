package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import waterplace.finalproj.R;
import waterplace.finalproj.listener.UserListener;
import waterplace.finalproj.model.Address;
import waterplace.finalproj.model.Supplier;
import waterplace.finalproj.model.User;
import waterplace.finalproj.util.DistanceUtil;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);
        System.out.println("aaaaa");
        //System.out.println(user);
        User user = User.getInstance();
        Log.d("NOME DO MANO", user.getName());
        Address address = user.getAddresses().get(0);
        System.out.println(address.getLatitude() + "AAHHAHAHAHAHHA");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Suppliers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot supplierSnapshot : snapshot.getChildren()) {
                    Supplier supplier = supplierSnapshot.getValue(Supplier.class);
                    assert supplier != null;
                    supplier.setAddress(supplierSnapshot.child("Address").getValue(Address.class));
                    double distance = 0;
                    DecimalFormat df = new DecimalFormat("0.0");
                    if (supplier.getAddress() != null) {
                        distance = DistanceUtil.calcDistance(address.getLatitude(), address.getLongitude(), supplier.getAddress().getLatitude(), supplier.getAddress().getLongitude());
                        System.out.println(supplier.getName());
                        System.out.println(df.format(distance));
                        System.out.println("AWAWAWAAWAWAWA");
                        //limite padrão para filtrar é 20km de distância
                        if (distance <= 20.0) {
                            System.out.println(supplier.getName());
                            System.out.println(df.format(distance));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainMenu.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}