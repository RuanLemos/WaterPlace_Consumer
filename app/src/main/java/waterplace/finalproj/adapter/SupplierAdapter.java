package waterplace.finalproj.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.DecimalFormat;
import java.util.List;

import waterplace.finalproj.R;
import waterplace.finalproj.model.Supplier;
import waterplace.finalproj.model.SupplierDistance;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {

    private List<SupplierDistance> suppliers;
    private Context context;

    public SupplierAdapter(List<SupplierDistance> suppliers, Context context) {
        this.suppliers = suppliers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supplier, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupplierDistance supDistance = suppliers.get(position);
        Supplier supplier = supDistance.getSupplier();
        DecimalFormat df = new DecimalFormat("0.0");

        holder.txt_supplier_name.setText(supplier.getName());
        holder.txt_supplier_category.setText("ainda nao tem nada");
        holder.txt_supplier_distance.setText(df.format(supDistance.getDistance()) + "km");
    }

    @Override
    public int getItemCount() {
        return suppliers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_supplier_name;
        public TextView txt_supplier_category;
        public TextView txt_supplier_distance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_supplier_name = itemView.findViewById(R.id.txt_supplier_name);
            txt_supplier_category = itemView.findViewById(R.id.txt_supplier_category);
            txt_supplier_distance = itemView.findViewById(R.id.txt_supplier_distance);
        }
    }
}