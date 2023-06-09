package waterplace.finalproj.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import waterplace.finalproj.R;
import waterplace.finalproj.adapter.ProductAdapter;
import waterplace.finalproj.model.Product;
import waterplace.finalproj.model.Supplier;

public class SupplierMenu extends AppCompatActivity {

    private String uid;
    private Supplier supplier;
    private ImageButton back_arrow;
    private List<Product> products = new ArrayList<>();
    private DatabaseReference supplierRef;
    private double distance;
    private TextView ratingValue;
    private RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_supplier);

        back_arrow = (ImageButton) findViewById(R.id.back_arrow_c);
        ratingValue = findViewById(R.id.nota);
        rating = findViewById(R.id.ratingBar);

        back_arrow.setOnClickListener(v -> goBack());

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        distance = Double.parseDouble(intent.getStringExtra("distance"));
        //distance = Double.parseDouble(intent.getStringExtra("distance"));
        supplierRef = FirebaseDatabase.getInstance().getReference("Suppliers").child(uid);
        supplierRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    supplier = snapshot.getValue(Supplier.class);
                    makeProdList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack(){
        Intent i = new Intent(this, MainMenu.class);
        startActivity(i);
    }

    private void setCapaImage() {
        ImageView img_capa = findViewById(R.id.image_capa);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        String location = uid+"/capa/image.jpg";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(location);
        storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Glide.with(SupplierMenu.this)
                        .load(storageReference)
                        .into(img_capa);
                img_capa.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                img_capa.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void makeProdList(){
        DatabaseReference prodRef = supplierRef.child("Products");
        prodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot prodSnapshot : snapshot.getChildren()) {
                    Product product = prodSnapshot.getValue(Product.class);
                    product.setUid(prodSnapshot.getKey());
                    products.add(product);
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            ImageView imageView = findViewById(R.id.image_capa);
            imageView.setImageURI(selectedImageUri);
        }
    }

    private void updateUI(){
        setCapaImage();

        // Nome do fornecedor
        TextView nome_supplier = findViewById(R.id.supplier_name);
        nome_supplier.setText(supplier.getName());
        DecimalFormat df = new DecimalFormat("0.0");
        if (supplier.getRating() != 0.0) {
            ratingValue.setText(df.format(supplier.getRating()));
            rating.setRating((float) supplier.getRating());
        } else {
            ratingValue.setText("Sem avaliações.");
            rating.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview_products);
        ProductAdapter adapter = new ProductAdapter(products, uid, this, supplier, distance);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
