package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.JsonUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import waterplace.finalproj.R;
import waterplace.finalproj.adapter.ResultsAdapter;
import waterplace.finalproj.adapter.SupplierAdapter;
import waterplace.finalproj.model.Address;
import waterplace.finalproj.model.Product;
import waterplace.finalproj.model.Supplier;
import waterplace.finalproj.model.SupplierDistance;
import waterplace.finalproj.model.Results;
import waterplace.finalproj.model.User;
import waterplace.finalproj.util.BottomNavigationManager;
import waterplace.finalproj.util.DistanceUtil;

public class MainMenu extends AppCompatActivity {

    User user;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //String suid =
    Address address;
    List<Results> results = new ArrayList<>();
    List<SupplierDistance> supplierDistances = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Suppliers");
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    SearchView searchView;
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

        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                    for (DataSnapshot addressSnapshot : snapshot.child("Addresses").getChildren()) {
                        address = addressSnapshot.getValue(Address.class);
                    }

                    fornecedoresProximos();
                    searchView = findViewById(R.id.searchView);
                    query();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        //Desabilita a seta
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

    private void Search(String searchText) {
        if (TextUtils.isEmpty(searchText)) {
            return;
        }

        String normaText = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);
        // Executa a consulta no Firebase
        //Query query = databaseReference.child(suid).child("Products")
        Query query = databaseReference.orderByChild("name")
                .startAt(normaText)
                .endAt(normaText + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                results.clear();
                for (DataSnapshot resultSnapshot : dataSnapshot.getChildren()) {
                    Results result = resultSnapshot.getValue(Results.class);
                    //result.setUid(dataSnapshot.child("Supplier"));
                    /*
                    if(result.getName() != null){
                        String supUid = resultSnapshot.getKey();
                        Results resultshow = new Results(result,supUid);
                        results.add(resultshow);
                    }else{
                    TextView errorTextView = findViewById(R.id.error_noresult);
                    errorTextView.setVisibility(View.VISIBLE);
                    }
                     */
                }
                listResults();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                TextView errorTextView = findViewById(R.id.error_noresult);
                errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void listSuppliers() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        SupplierAdapter adapter = new SupplierAdapter(supplierDistances, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainMenu.this));
    }

    public void listResults() {
        System.out.println("Entrou no list");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        ResultsAdapter adapter = new ResultsAdapter(results,this);
//        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainMenu.this));
    }

    public void query(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Executar a consulta quando o usuário pressionar o botão de pesquisa
                Search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    fornecedoresProximos();
                    if(supplierDistances.isEmpty()){
                        TextView errorTextView = findViewById(R.id.error_noresult);
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Search(newText);
                };
                return true;
            }

        });
    }
}