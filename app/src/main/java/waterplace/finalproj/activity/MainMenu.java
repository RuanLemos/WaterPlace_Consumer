package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import waterplace.finalproj.R;
import waterplace.finalproj.adapter.SupplierAdapter;
import waterplace.finalproj.listener.UserListener;
import waterplace.finalproj.model.Address;
import waterplace.finalproj.model.Supplier;
import waterplace.finalproj.model.SupplierDistance;
import waterplace.finalproj.model.User;
import waterplace.finalproj.util.DistanceUtil;

public class MainMenu extends AppCompatActivity {

    List<SupplierDistance> supplierDistances = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);
        User user = User.getInstance();
        Address address = user.getAddresses().get(0);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Suppliers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot supplierSnapshot : snapshot.getChildren()) {
                    //System.out.println("ó a id desse merda que tá bugando tudo -> " + supplierSnapshot.getKey());
                    Supplier supplier = supplierSnapshot.getValue(Supplier.class);
                    assert supplier != null;
                    supplier.setAddress(supplierSnapshot.child("Address").getValue(Address.class));
                    double distance;

                    if (supplier.getAddress() != null) {
                        distance = DistanceUtil.calcDistance(address.getLatitude(), address.getLongitude(), supplier.getAddress().getLatitude(), supplier.getAddress().getLongitude());

                        //System.out.println(supplier.getName());
                        //System.out.println(df.format(distance));
                        //System.out.println("AWAWAWAAWAWAWA");

                        //limite padrão para filtrar é 20km de distância
                        if (distance <= 20.0) {
                            String supUid = supplierSnapshot.getKey();
                            SupplierDistance supDistance = new SupplierDistance(supplier, distance, supUid);
                            supplierDistances.add(supDistance);
                        }
                    }
                }
                listSuppliers();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainMenu.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void listSuppliers() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        SupplierAdapter adapter = new SupplierAdapter(supplierDistances, MainMenu.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainMenu.this));
    }
}