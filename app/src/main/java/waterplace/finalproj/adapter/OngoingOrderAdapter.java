package waterplace.finalproj.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import waterplace.finalproj.R;
import waterplace.finalproj.model.Order;

public class OngoingOrderAdapter extends RecyclerView.Adapter<OngoingOrderAdapter.OrderViewHolder> {
    private List<Order> ongoingOrderList;
    private Context context;

    public OngoingOrderAdapter(List<Order> ongoingOrderList, Context context) {
        this.ongoingOrderList = ongoingOrderList;
        this.context = context;
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
        // Bind dos dados do pedido em andamento ao item da RecyclerView
        Order order = ongoingOrderList.get(position);
        // Configurar os elementos de visualização (TextViews, ImageViews, etc.) com os dados do pedido
        //holder.orderNumberTextView.setText(order.getOrderNumber());
        holder.prodName.setText(order.getProdName());

        if (order.isScheduled()) {
            holder.deliveryDesc.setText("Agendamento para " + order.getOrderDateTime());
        } else {
            holder.deliveryDesc.setText("Entrega imediata");
        }

        holder.orderPrice.setText(String.valueOf(order.getPrice()));

        if (order.getStatus().equals("Agendado")) {
            holder.orderStatus.setText("Aguardando entrega");
        } else {
            holder.orderStatus.setText("Aguardando\nConfirmação");
        }
    }

    @Override
    public int getItemCount() {
        return ongoingOrderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        // Declarar os elementos de visualização (TextViews, ImageViews, etc.) aqui

        TextView prodName;
        TextView deliveryDesc;
        TextView orderPrice;
        TextView orderStatus;
        // ...

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar os elementos de visualização aqui
            //orderNumberTextView = itemView.findViewById(R.id.text_order_number);
            prodName = itemView.findViewById(R.id.item_name);
            deliveryDesc = itemView.findViewById(R.id.prod_delivery);
            orderPrice = itemView.findViewById(R.id.prod_price);
            orderStatus = itemView.findViewById(R.id.prod_status);
            // ...
        }
    }
}
