package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.TradeHistoryList;

public class TradeOrderHistoryAdapter extends RecyclerView.Adapter<TradeOrderHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<TradeHistoryList> orderHistoryList;

    public TradeOrderHistoryAdapter(Context context, List<TradeHistoryList> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public TradeOrderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trade_orderhistory, parent, false);
        return new TradeOrderHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TradeOrderHistoryAdapter.MyViewHolder holder, int position) {
        TradeHistoryList orderHistoryData = orderHistoryList.get(position);
        DecimalFormat df = new DecimalFormat("#.000000");

        holder.tv_timeValue.setText(String.format("(%s)", orderHistoryData.getTime()));
        holder.tv_secondCoinQtyValue.setText(df.format(orderHistoryData.getCoinPurchase()));
        holder.tv_bidPriceValue.setText(df.format(orderHistoryData.getCoinPrice()));

        double total = orderHistoryData.getCoinPurchase()*orderHistoryData.getCoinPrice();
        holder.tv_estTotalValue.setText(df.format(total));

        String type = orderHistoryData.getTradeType();
        holder.tv_typeValue.setText(type);
        if (type.equalsIgnoreCase("buy")) {
            holder.tv_typeValue.setTextColor(context.getResources().getColor(R.color.tradeBuyColor));
        } else if (type.equalsIgnoreCase("sell")) {
            holder.tv_typeValue.setTextColor(context.getResources().getColor(R.color.tradeSellColor));
        }
    }

    @Override
    public int getItemCount() { return orderHistoryList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_typeValue, tv_timeValue, tv_secondCoinQtyValue, tv_bidPriceValue, tv_estTotalValue;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_typeValue = itemView.findViewById(R.id.tv_typeValue);
            tv_timeValue = itemView.findViewById(R.id.tv_timeValue);
            tv_secondCoinQtyValue = itemView.findViewById(R.id.tv_secondCoinQtyValue);
            tv_bidPriceValue = itemView.findViewById(R.id.tv_bidPriceValue);
            tv_estTotalValue = itemView.findViewById(R.id.tv_estTotalValue);
        }
    }
}
