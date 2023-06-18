package waterplace.finalproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Calendar;

import waterplace.finalproj.R;
import waterplace.finalproj.adapter.SupplierAdapter;
import waterplace.finalproj.dialog.ScheduleDeliveryDialog;
import waterplace.finalproj.model.Address;
import waterplace.finalproj.model.Product;
import waterplace.finalproj.model.Order;
import waterplace.finalproj.model.Supplier;
import waterplace.finalproj.model.SupplierDistance;

public class BuyProduct extends AppCompatActivity {

    private String uid;
    private Order order = new Order();
    private String prodUid;
    private Product product;
    private Supplier supplier;
    private int amount = 1;
    private TextView prod_amount;
    private TextView prod_desc;
    private TextView prod_name;
    private TextView prod_price;
    private Button btn_delivery;
    private ImageButton back_arrow;
    private double requestPrice;
    private Address address;
    private String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
    private Button schedule;
    private TextView supName;
    private TextView supRating;
    private TextView supDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buy_product);

        Intent intent = getIntent();
        supplier = (Supplier) intent.getSerializableExtra("supplier");
        product = (Product) intent.getSerializableExtra("product");
        uid = intent.getStringExtra("uid");
        prodUid = intent.getStringExtra("prodId");
        Double distance = Double.valueOf(intent.getStringExtra("distance"));
        DecimalFormat df = new DecimalFormat("0.0");
        btn_delivery = findViewById(R.id.delivery);
        schedule = findViewById(R.id.schedule_delivery);
        back_arrow = (ImageButton) findViewById(R.id.back_arrow_c_2);
        supName = findViewById(R.id.supplier_name_3);
        supName.setText(supplier.getName());
        supRating = findViewById(R.id.nota_3);
        supRating.setText(String.valueOf(supplier.getRating()));
        supDistance = findViewById(R.id.distance_2);
        supDistance.setText(df.format(distance) + "km");
        updateUI();

        schedule.setOnClickListener(v -> scheduleDelivery());

        btn_delivery.setOnClickListener(v -> makeOrder());

        back_arrow.setOnClickListener(v -> goBack());

        // Adicionar OnClickListener para o botão "add_product"
        ImageView addProductButton = findViewById(R.id.add_product);
        addProductButton.setOnClickListener(v -> increaseAmount());

        // Adicionar OnClickListener para o botão "remove_product"
        ImageView removeProductButton = findViewById(R.id.remove_product);
        removeProductButton.setOnClickListener(v -> decreaseAmount());
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack(){
        Intent i = new Intent(this,SupplierMenu.class);
        i.putExtra("uid", uid);
        startActivity(i);
    }

    private void updateUI(){
        loadImg();
        // Nome do fornecedor
        prod_name = findViewById(R.id.product_name);
        prod_name.setText(product.getName());
        prod_desc = findViewById(R.id.product_desc);
        prod_desc.setText(product.getDesc());
        prod_amount = findViewById(R.id.product_amount);
        prod_amount.setText(String.valueOf(amount));
        Log.d("TESTE", prod_amount.getText().toString());
        prod_price = findViewById(R.id.price);
        updateFooter();
    }

    private void increaseAmount(){
        amount++;
        updateFooter();
    }

    private void decreaseAmount(){
        if (amount > 1) {
            amount--;
            updateFooter();
        } else {
            Toast.makeText(BuyProduct.this, "Deve ter ao menos um produto para realizar compras!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateFooter(){
        DecimalFormat pf = new DecimalFormat("0.00");
        requestPrice = product.getPrice() * amount;
        prod_amount.setText(String.valueOf(amount));
        prod_price.setText(getString(R.string.valor_r) + " " + pf.format(requestPrice));
    }

    private void goOrder(Order order) {
        Intent i = new Intent(this, OrderDetails.class);
        i.putExtra("product", product);
        i.putExtra("user address", address);
        i.putExtra("order", order);
        startActivity(i);
    }

    private void makeOrder() {
        order.setUserId(userUid);
        order.setSupplierId(uid);
        order.setProdId(prodUid);
        order.setProdName(product.getName());
        order.setSubtotal(requestPrice);
        order.setQuantity(amount);

        userRef.child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                    address = snapshot.getValue(Address.class);
                    String addressFormat = address.getAvenue() + ", " + address.getNum() + " - " + address.getComp()
                            + "\n" + address.getDistrict() + " - " + address.getCity();
                    order.setAddress(addressFormat);
                    System.out.println("Ó O ENDEREÇOOO " + address.getAvenue());
                    goOrder(order);
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadImg(){
        String location = uid+"/products/"+prodUid;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(location);
        ImageView img = findViewById(R.id.product_pic);
        Glide.with(img.getContext()).load(storageReference).into(img);
    }

    public void scheduleDelivery() {
        ScheduleDeliveryDialog dialog = new ScheduleDeliveryDialog();
        dialog.setScheduleDeliveryListener(schedule -> {
            makeOrder();
            order.setDeliveryDateTime(schedule);
            order.setScheduled(true);
        });
        dialog.show(getFragmentManager(), "ScheduleDeliveryDialog");
    }
}