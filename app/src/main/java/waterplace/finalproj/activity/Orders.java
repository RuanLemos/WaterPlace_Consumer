package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import waterplace.finalproj.R;
import waterplace.finalproj.adapter.DeliveredOrderAdapter;
import waterplace.finalproj.adapter.OngoingOrderAdapter;
import waterplace.finalproj.adapter.ProductAdapter;
import waterplace.finalproj.model.Order;
import waterplace.finalproj.util.BottomNavigationManager;

public class Orders extends AppCompatActivity {

    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private List<Order> onGoingOrders = new ArrayList<>();
    private List<Order> finishedOrders = new ArrayList<>();
    private Order order;
    private String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private BottomNavigationManager bottomNavigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_orders);

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_footer);
        bottomNavigationView.setSelectedItemId(R.id.lista);
        bottomNavigationManager = new BottomNavigationManager(this);
        bottomNavigationManager.setupBottomNavigation(bottomNavigationView);

        getOrders();
    }

    private void getOrders() {
        userRef.child(userUid).child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    order = orderSnapshot.getValue(Order.class);
                    order.setOrderId(orderSnapshot.getKey());
                    if (order.getStatus().equals("Confirmado") || order.getStatus().equals("Aguardando confirmação") || order.getStatus().equals("A caminho")) {
                        onGoingOrders.add(order);
                    } else {
                        finishedOrders.add(order);
                    }
                }
                updateOngoingOrders();
                updateDeliveredOrders();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateOngoingOrders(){
        RecyclerView recyclerView = findViewById(R.id.recycler_ongoing);
        OngoingOrderAdapter adapter = new OngoingOrderAdapter(onGoingOrders, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void updateDeliveredOrders(){
        RecyclerView recyclerView = findViewById(R.id.recycle_history);
        DeliveredOrderAdapter adapter = new DeliveredOrderAdapter(finishedOrders, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}