package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import waterplace.finalproj.R;
import waterplace.finalproj.model.Address;
import waterplace.finalproj.model.Order;
import waterplace.finalproj.model.Product;
import waterplace.finalproj.model.Supplier;
import waterplace.finalproj.model.User;
import waterplace.finalproj.util.DistanceUtil;

public class OrderDetails extends AppCompatActivity {

    private DatabaseReference supRef = FirebaseDatabase.getInstance().getReference("Suppliers");
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private Product product;
    private Supplier supplier;
    private Order order;
    private Address userAddress;
    private ImageButton back_arrow;
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

        back_arrow = (ImageButton) findViewById(R.id.back_arrow_6);

        back_arrow.setOnClickListener(v -> goBack());

        i = getIntent();
        order = (Order) i.getSerializableExtra("order");

        supId = order.getSupplierId();
        userId = order.getUserId();

        btn_confirm = findViewById(R.id.confirm);
        btn_confirm.setOnClickListener(v -> makeOrder());

        if (order.getStatus() != null) {
            btn_confirm.setVisibility(View.GONE);
        }

        updateUI();

    }

    public void onBackPressed() {
        goBack();
    }

    public void goBack(){
        Intent i = new Intent(this,Orders.class);
        startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(){
        loadImg();
        getUser();
        DecimalFormat pf = new DecimalFormat("0.00");
        prodAmount = findViewById(R.id.amount_tag);
        prodAmount.setText(String.valueOf(order.getQuantity()));
        deliveryType = findViewById(R.id.del_type);

        if (order.isScheduled()) {
            String deliveryDate = order.getDeliveryDateTime();
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
        serviceTax.setText("R$ " + pf.format(serviceTaxValue));
        totalValue = findViewById(R.id.total_value);
        order.setPrice(order.getSubtotal() + serviceTaxValue);
        totalValue.setText("R$ " + pf.format(order.getPrice()));
        deliveryAddress = findViewById(R.id.end_value);
        deliveryAddress.setText(order.getAddress());
    }

    private void getSupplierData(){
        System.out.println(order.getSupplierId());
        supRef.child(supId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                getProd();
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

        order.setUserId(null);

        userRef.child(userId).child("Orders").child(orderUid).setValue(order);

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

    private void getUser(){
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for (DataSnapshot addressSnapshot : snapshot.child("Addresses").getChildren()) {
                userAddress = snapshot.child("address").getValue(Address.class);
                //}
                getSupplierData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getProd(){
        //System.out.println(userId);
        //System.out.println(order.getProdId());
        supRef.child(supId).child("Products").child(order.getProdId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                product = snapshot.getValue(Product.class);
                prodName = findViewById(R.id.item_name);
                prodDesc = findViewById(R.id.amount);
                prodName.setText(product.getName());
                prodDesc.setText(product.getDesc());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadImg(){
        String location = order.getSupplierId()+"/products/"+order.getProdId();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(location);
        ImageView img = findViewById(R.id.product_pic);
        Glide.with(img.getContext()).load(storageReference).into(img);
    }
}