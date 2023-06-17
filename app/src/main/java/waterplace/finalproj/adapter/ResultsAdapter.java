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
import waterplace.finalproj.model.Product;
import waterplace.finalproj.model.Results;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>{

    private List<Product> productList;
    private String supplierUid;
    private Context context;

    public ResultsAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.supplierUid = supplierUid;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Product product = productList.get(position);
        String location = supplierUid+"/products/"+product.getUid();
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(location);

        holder.name.setText(product.getName());
        holder.price.setText("R$ " + product.getPrice());
        //holder.prodOwner.setText(product.getProdOwner());

        Glide.with(holder.img.getContext())
                .load(storageReference)
                .into(holder.img);
        holder.itemView.setOnClickListener(v -> onItemClick(supplierUid, product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;

        TextView prodOwner;
        ImageView img;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_prod_name);
            price = itemView.findViewById(R.id.txt_prod_price);
            prodOwner = itemView.findViewById(R.id.txt_prod_owner);
            img = itemView.findViewById(R.id.img_prod);
        }
    }

    private void onItemClick(String uid, Product product){
        Intent i = new Intent(context, BuyProduct.class);
        i.putExtra("uid", uid);
        i.putExtra("product", product);
        i.putExtra("prodId", product.getUid());
        context.startActivity(i);
    }
}



