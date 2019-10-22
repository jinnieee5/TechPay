package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.UserOpenOrderList;
import crypto.base.baseexchange.interfaces.OpenOrderCancelListener;

public class OpenOrderHistoryAdapter extends RecyclerView.Adapter<OpenOrderHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<UserOpenOrderList> openOrderList;
    private OpenOrderCancelListener listener;

    public OpenOrderHistoryAdapter(Context context, List<UserOpenOrderList> orderHistoryList, OpenOrderCancelListener listener) {
        this.context = context;
        this.openOrderList = orderHistoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OpenOrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_openorder_history, parent, false);
        return new OpenOrderHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenOrderHistoryAdapter.MyViewHolder holder, int position) {
        final UserOpenOrderList openOrder = openOrderList.get(position);

        DecimalFormat df = new DecimalFormat("#.00000000");

        String type = openOrder.getType();
        holder.tv_type.setText(type);
        if (type.equalsIgnoreCase("buy")) holder.tv_type.setTextColor(context.getResources().getColor(R.color.tradeBuyColor));
        else if (type.equalsIgnoreCase("sell")) holder.tv_type.setTextColor(context.getResources().getColor(R.color.tradeSellColor));

        holder.tv_coinName.setText(openOrder.getMarket());
        holder.tv_orderDate.setText(String.format("(%s)", openOrder.getOrderDate()));
        holder.tv_bidAsk.setText(df.format(openOrder.getBid()));
        holder.tv_filled.setText(df.format(openOrder.getFilled()));
        holder.tv_bidPrice.setText(df.format(openOrder.getActualRate()));

        double estRate = openOrder.getBid()*openOrder.getActualRate();
        holder.tv_estRate.setText(df.format(estRate));

        holder.frame_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCancelOrderDialog(openOrder);
            }
        });
    }

    @Override
    public int getItemCount() { return openOrderList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_coinName, tv_orderDate, tv_type, tv_bidAsk, tv_filled, tv_bidPrice, tv_estRate;
        FrameLayout frame_cancelOrder;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_coinName = itemView.findViewById(R.id.tv_coinName);
            tv_orderDate = itemView.findViewById(R.id.tv_orderDate);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_bidAsk = itemView.findViewById(R.id.tv_bidAsk);
            tv_filled = itemView.findViewById(R.id.tv_filled);
            tv_bidPrice = itemView.findViewById(R.id.tv_bidPrice);
            tv_estRate = itemView.findViewById(R.id.tv_estRate);
            frame_cancelOrder = itemView.findViewById(R.id.frame_cancelOrder);
        }
    }

    private void openCancelOrderDialog(final UserOpenOrderList openOrder) {
        final AlertDialog cancelOrderDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Are you sure ?");
        builder.setMessage("After cancel order you will not be able to recover your order.");

        builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOpenOrderClick(openOrder.getType(), openOrder.getTccbtmid()+"", openOrder.getTccstmid()+"");
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        cancelOrderDialog = builder.create();
        cancelOrderDialog.show();
    }
}
