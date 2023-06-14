package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import waterplace.finalproj.util.BottomNavigationManager;
import waterplace.finalproj.util.DistanceUtil;

public class MainMenu extends AppCompatActivity {

    User user = User.getInstance();
    Address address = user.getAddresses().get(0);
    List<SupplierDistance> supplierDistances = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Suppliers");
    private BottomNavigationManager bottomNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_footer);
        bottomNavigationManager = new BottomNavigationManager(this);
        bottomNavigationManager.setupBottomNavigation(bottomNavigationView);

        fornecedoresProximos();

        SearchView searchView = findViewById(R.id.searchView);

        // Configurar o ouvinte de eventos para a SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Executar a consulta quando o usuário pressionar o botão de pesquisa
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    fornecedoresProximos();
                } else {
                    performSearch(newText);
                };
                return true;
            }

        });
    }


    private void fornecedoresProximos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Limpar os resultados anteriores
                supplierDistances.clear();
                for (DataSnapshot supplierSnapshot : snapshot.getChildren()) {
                    System.out.println("ó a id desse merda que tá bugando tudo -> " + supplierSnapshot.getKey());
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

    private void performSearch(String searchText) {
        // Limpar a pesquisa se o texto estiver vazio
        if (TextUtils.isEmpty(searchText)) {
            // Limpar os resultados anteriores
            // Atualizar a interface do usuário com os resultados da pesquisa
            return;
        }

        // Executar a consulta no Firebase Realtime Database
        Query query = databaseReference.orderByChild("name").equalTo(searchText);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Limpar os resultados anteriores
                supplierDistances.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Supplier supplier = snapshot.getValue(Supplier.class);
                    assert supplier != null;
                    supplier.setAddress(snapshot.child("Address").getValue(Address.class));
                    double distance;

                    if (supplier.getAddress() != null) {
                        distance = DistanceUtil.calcDistance(address.getLatitude(), address.getLongitude(), supplier.getAddress().getLatitude(), supplier.getAddress().getLongitude());

                        //System.out.println(supplier.getName());
                        //System.out.println(df.format(distance));
                        //System.out.println("AWAWAWAAWAWAWA");

                        //limite padrão para filtrar é 20km de distância
                        if (distance <= 20.0) {
                            String supUid = snapshot.getKey();
                            SupplierDistance supDistance = new SupplierDistance(supplier, distance, supUid);
                            supplierDistances.add(supDistance);
                        }
                    }

                }
                listSuppliers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Lidar com erros de consulta
                Toast.makeText(MainMenu.this, "Erro na consulta: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void listSuppliers() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        SupplierAdapter adapter = new SupplierAdapter(supplierDistances, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainMenu.this));
    }
}