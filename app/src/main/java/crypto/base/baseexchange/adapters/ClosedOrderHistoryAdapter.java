package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.UserClosedOrderList;

public class ClosedOrderHistoryAdapter extends RecyclerView.Adapter<ClosedOrderHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<UserClosedOrderList> orderHistoryList;

    public ClosedOrderHistoryAdapter(Context context, List<UserClosedOrderList> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public ClosedOrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_closedorder_history, parent, false);
        return new ClosedOrderHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClosedOrderHistoryAdapter.MyViewHolder holder, int position) {
        UserClosedOrderList closedOrder = orderHistoryList.get(position);

        DecimalFormat df = new DecimalFormat("#.00000000");

        String type = closedOrder.getType();
        holder.tv_type.setText(type);
        if (type.equalsIgnoreCase("buy")) holder.tv_type.setTextColor(context.getResources().getColor(R.color.tradeBuyColor));
        else if (type.equalsIgnoreCase("sell")) holder.tv_type.setTextColor(context.getResources().getColor(R.color.tradeSellColor));

        String approvalStatus = closedOrder.getApprovalStatus();
        holder.tv_approvalStatus.setText(approvalStatus);
        if (approvalStatus.equalsIgnoreCase("CLOSED")) holder.tv_approvalStatus.setTextColor(context.getResources().getColor(R.color.deepGreen));
        else if (approvalStatus.equalsIgnoreCase("CANCELLED")) holder.tv_approvalStatus.setTextColor(context.getResources().getColor(R.color.deepRed));

        holder.tv_coinName.setText(closedOrder.getMarket());
        holder.tv_approvalDate.setText(String.format("(%s)", closedOrder.getApprovalDate()));
        holder.tv_bidAsk.setText(df.format(closedOrder.getBid()));
        holder.tv_filled.setText(df.format(closedOrder.getFilled()));
        holder.tv_bidPrice.setText(df.format(closedOrder.getActualRate()));

        double estRate = closedOrder.getBid()*closedOrder.getActualRate();
        holder.tv_estRate.setText(df.format(estRate));
    }

    @Override
    public int getItemCount() { return orderHistoryList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_coinName, tv_approvalDate, tv_type, tv_bidAsk, tv_filled, tv_bidPrice, tv_estRate, tv_approvalStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_coinName = itemView.findViewById(R.id.tv_coinName);
            tv_approvalDate = itemView.findViewById(R.id.tv_approvalDate);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_bidAsk = itemView.findViewById(R.id.tv_bidAsk);
            tv_filled = itemView.findViewById(R.id.tv_filled);
            tv_bidPrice = itemView.findViewById(R.id.tv_bidPrice);
            tv_estRate = itemView.findViewById(R.id.tv_estRate);
            tv_approvalStatus = itemView.findViewById(R.id.tv_approvalStatus);
        }
    }
}
