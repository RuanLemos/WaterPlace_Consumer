package waterplace.finalproj.adapter;

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
import waterplace.finalproj.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private String supplierUid;

    public ProductAdapter(List<Product> productList, String supplierUid) {
        this.productList = productList;
        this.supplierUid = supplierUid;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        String location = supplierUid+"/products/"+product.getUid();
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(location);

        holder.name.setText(product.getName());
        holder.price.setText(String.valueOf(product.getPrice()));
        holder.desc.setText(product.getDesc());

        Glide.with(holder.img.getContext())
                .load(storageReference)
                .into(holder.img);
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
}
