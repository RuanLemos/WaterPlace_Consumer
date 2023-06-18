package waterplace.finalproj.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import waterplace.finalproj.R;
import waterplace.finalproj.activity.BuyProduct;
import waterplace.finalproj.activity.SupplierMenu;
import waterplace.finalproj.model.Product;
import waterplace.finalproj.model.Supplier;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private String supplierUid;
    private Context context;
    private Supplier supplier;
    private double distance;

    public ProductAdapter(List<Product> productList, String supplierUid, Context context, Supplier supplier, double distance) {
        this.productList = productList;
        this.supplierUid = supplierUid;
        this.context = context;
        this.supplier = supplier;
        this.distance = distance;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        String location = supplierUid+"/products/"+product.getUid();
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(location);

        holder.name.setText(product.getName());
        holder.price.setText("R$ " + product.getPrice());
        holder.desc.setText(product.getDesc());

        Glide.with(holder.img.getContext())
                .load(storageReference)
                .into(holder.img);
        holder.itemView.setOnClickListener(v -> onItemClick(supplierUid, product, supplier, distance));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView desc;
        ImageView img;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_prod_name);
            price = itemView.findViewById(R.id.txt_prod_price);
            desc = itemView.findViewById(R.id.txt_prod_desc);
            img = itemView.findViewById(R.id.img_prod);
        }
    }

    private void onItemClick(String uid, Product product, Supplier supplier, double distance){
        Intent i = new Intent(context, BuyProduct.class);
        i.putExtra("uid", uid);
        i.putExtra("distance", String.valueOf(distance));
        i.putExtra("supplier", supplier);
        i.putExtra("product", product);
        i.putExtra("prodId", product.getUid());
        context.startActivity(i);
    }
}
