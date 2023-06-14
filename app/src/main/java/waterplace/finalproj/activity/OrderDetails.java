package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import waterplace.finalproj.R;
import waterplace.finalproj.model.Address;
import waterplace.finalproj.model.Order;
import waterplace.finalproj.model.Product;
import waterplace.finalproj.model.Supplier;
import waterplace.finalproj.util.DistanceUtil;

public class OrderDetails extends AppCompatActivity {

    private DatabaseReference supRef = FirebaseDatabase.getInstance().getReference("Suppliers");
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private Product product;
    private Supplier supplier;
    private Order order;
    private Address userAddress;
    private TextView supName;
    private TextView supDistance;
    private TextView supRating;
    private TextView prodName;
    private TextView prodDesc;
    private TextView prodAmount;
    private TextView deliveryType;
    private TextView itemsPrice;
    private TextView subtotalValue;
    private TextView deliveryTax;
    private TextView serviceTax;
    private TextView totalValue;
    private TextView deliveryAddress;
    private Intent i;
    private String userId;
    private String supId;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_order_details);

        i = getIntent();

        product = (Product) i.getSerializableExtra("product");
        order = (Order) i.getSerializableExtra("order");
        userAddress = (Address) i.getSerializableExtra("user address");

        updateUI();

        btn_confirm = findViewById(R.id.confirm);
        btn_confirm.setOnClickListener(v -> makeOrder());
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(){
        DecimalFormat pf = new DecimalFormat("0.00");
        getSupplierData();
        prodName = findViewById(R.id.item_name);
        prodName.setText(product.getName());
        prodDesc = findViewById(R.id.amount);
        prodDesc.setText(product.getDesc());
        prodAmount = findViewById(R.id.amount_tag);
        prodAmount.setText(String.valueOf(order.getQuantity()));
        deliveryType = findViewById(R.id.del_type);

        if (order.isScheduled()) {
            String deliveryDate = "Entrega agendada para " + order.getDeliveryDateTime();
            deliveryType.setText(deliveryDate);
        } else {
            deliveryType.setText("Entrega imediata");
        }

        itemsPrice = findViewById(R.id.item_price);
        itemsPrice.setText("R$ " + pf.format(order.getSubtotal()));
        subtotalValue = findViewById(R.id.subtotal_value);
        subtotalValue.setText("R$ " + pf.format(order.getSubtotal()));
        deliveryTax = findViewById(R.id.tax_del_value);
        deliveryTax.setText("Grátis");
        serviceTax = findViewById(R.id.tax_serv_value);
        double serviceTaxValue = 1.90;
        serviceTax.setText("R$ " + serviceTaxValue);
        totalValue = findViewById(R.id.total_value);
        order.setPrice(order.getSubtotal() + serviceTaxValue);
        totalValue.setText("R$ " + pf.format(order.getPrice()));
        deliveryAddress = findViewById(R.id.end_value);
        deliveryAddress.setText(order.getAddress());
    }

    private void getSupplierData(){
        supRef.child(order.getSupplierId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                supplier = snapshot.getValue(Supplier.class);
                supName = findViewById(R.id.supplier_name_2);
                supName.setText(supplier.getName());
                supRating = findViewById(R.id.nota_2);
                supRating.setText(String.valueOf(supplier.getRating()));
                System.out.println("testezinho " + userAddress.getLatitude());
                supplier.setAddress(snapshot.child("Address").getValue(Address.class));
                System.out.println("testezinho dois " + supplier.getAddress().getLatitude());
                double distance = DistanceUtil.calcDistance(userAddress.getLatitude(), userAddress.getLongitude(), supplier.getAddress().getLatitude(), supplier.getAddress().getLongitude());
                DecimalFormat df = new DecimalFormat("0.0");
                supDistance = findViewById(R.id.distance);
                supDistance.setText(df.format(distance) + "km");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void makeOrder() {

        if (!order.isScheduled()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            String formattedDateTime = now.format(formatter);
            order.setOrderDateTime(formattedDateTime);
        }
        System.out.println("testando");
        order.setStatus("Aguardando confirmação");

        String orderUid = FirebaseDatabase.getInstance().getReference().push().getKey();

        userId = order.getUserId();
        order.setUserId(null);

        userRef.child(userId).child("Orders").child(orderUid).setValue(order);

        supId = order.getSupplierId();
        order.setUserId(userId);
        order.setSupplierId(null);

        supRef.child(supId).child("Orders").child(orderUid).setValue(order);

        Toast.makeText(OrderDetails.this, "Pedido realizado com sucesso", Toast.LENGTH_SHORT).show();

        goOrders();
    }

    private void goOrders(){
        Intent i = new Intent(this, Orders.class);
        startActivity(i);
    }
}
