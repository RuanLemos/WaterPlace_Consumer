package waterplace.finalproj.adapter;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

import waterplace.finalproj.R;
import waterplace.finalproj.activity.OrderDetails;
import waterplace.finalproj.dialog.ReviewDialog;
import waterplace.finalproj.model.Order;

public class DeliveredOrderAdapter extends RecyclerView.Adapter<DeliveredOrderAdapter.OrderViewHolder> {
    private List<Order> ongoingOrderList;
    private Context context;
    private Order order;
    private FragmentManager fragmentManager;
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference supRef = FirebaseDatabase.getInstance().getReference("Suppliers");

    public DeliveredOrderAdapter(List<Order> ongoingOrderList, Context context, FragmentManager fragmentManager) {
        this.ongoingOrderList = ongoingOrderList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item do pedido em andamento
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        order = ongoingOrderList.get(position);

        String location = order.getSupplierId()+"/products/"+order.getProdId();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(location);

        holder.prodName.setText(order.getProdName());

        holder.deliveryDesc.setText("Entregue dia " + order.getDeliveryDateTime());

        DecimalFormat pf = new DecimalFormat("0.00");
        holder.orderPrice.setText("R$ " + pf.format(order.getPrice()));
        System.out.println(order.getStatus());
        if (order.getReview() == 0) {
            holder.orderStatus.setVisibility(View.GONE);
            holder.btn_review.setVisibility(View.VISIBLE);
            holder.btn_review.setOnClickListener(v -> {
                makeReview(order, holder);
            });
        } else {
            holder.orderStatus.setText("Avaliado\n"+order.getReview());
        }
        //holder.orderStatus.setText(order.getStatus() + "\n" + order.getDeliveryDateTime());

        Glide.with(holder.img.getContext())
                .load(storageReference)
                .into(holder.img);

        holder.itemView.setOnClickListener(v -> onItemClick(context, order));
    }

    @Override
    public int getItemCount() {
        return ongoingOrderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView prodName;
        TextView deliveryDesc;
        TextView orderPrice;
        TextView orderStatus;
        ImageView img;
        Button btn_review;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.item_name);
            deliveryDesc = itemView.findViewById(R.id.prod_delivery);
            orderPrice = itemView.findViewById(R.id.prod_price);
            orderStatus = itemView.findViewById(R.id.prod_status);
            img = itemView.findViewById(R.id.product_pic);
            btn_review = itemView.findViewById(R.id.btn_review);
        }
    }

    private void onItemClick(Context context, Order order) {
        Intent i = new Intent(context, OrderDetails.class);
        i.putExtra("order", order);
        context.startActivity(i);
    }

    private void makeReview(Order order, OrderViewHolder holder) {
        ReviewDialog dialog = new ReviewDialog();
        dialog.setReviewDialogListener(review ->{
            order.setReview(review);
            holder.orderStatus.setVisibility(View.VISIBLE);
            holder.btn_review.setVisibility(View.GONE);
            holder.orderStatus.setText("Avaliado\n"+review);
            userRef.child(userId).child("Orders").child(order.getOrderId()).child("review").setValue(review);
            supRef.child(order.getSupplierId()).child("Orders").child(order.getOrderId()).child("review").setValue(review);
            DatabaseReference ordersRef = supRef.child(order.getSupplierId()).child("Orders");
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Order pedido;
                    double qtOrders = 0;
                    double totalRating = 0;
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        pedido = orderSnapshot.getValue(Order.class);
                        pedido.setOrderId(orderSnapshot.getKey());
                        if (pedido.getReview() != 0) {
                            qtOrders++;
                            totalRating += pedido.getReview();
                        }
                    }
                    if (qtOrders != 0) {
                        supRef.child(order.getSupplierId()).child("rating").setValue(totalRating / qtOrders);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        dialog.show(fragmentManager, "ReviewDialog");
    }
}
