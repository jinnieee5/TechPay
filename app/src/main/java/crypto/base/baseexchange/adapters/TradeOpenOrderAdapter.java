package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.TradeOpenOrderList;
import crypto.base.baseexchange.interfaces.TradeOpenOrdersListener;

public class TradeOpenOrderAdapter extends RecyclerView.Adapter<TradeOpenOrderAdapter.MyViewHolder> {
    private Context context;
    private List<TradeOpenOrderList> tradeMarketOrderList;
    private String mode;
    private TradeOpenOrdersListener tradeOpenOrdersListener;

    public TradeOpenOrderAdapter(Context context, List<TradeOpenOrderList> tradeMarketOrderList, String mode, TradeOpenOrdersListener tradeOpenOrdersListener) {
        this.context = context;
        this.tradeMarketOrderList = tradeMarketOrderList;
        this.mode = mode;
        this.tradeOpenOrdersListener = tradeOpenOrdersListener;
    }

    @NonNull
    @Override
    public TradeOpenOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trade_openorders, parent, false);
        return new TradeOpenOrderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TradeOpenOrderAdapter.MyViewHolder holder, int position) {
        final TradeOpenOrderList openOrder = tradeMarketOrderList.get(position);
        DecimalFormat df = new DecimalFormat("#.0000000");

        if (mode.equalsIgnoreCase("buy")) holder.tv_price.setTextColor(context.getColor(R.color.tradeBuyColor));
        else if (mode.equalsIgnoreCase("sell")) holder.tv_price.setTextColor(context.getColor(R.color.tradeSellColor));

        holder.tv_amount.setText(df.format(openOrder.getCoinPurchase()));
        double price = openOrder.getCoinPurchase()*openOrder.getTradeRate();
        holder.tv_price.setText(df.format(price));

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tradeOpenOrdersListener.onTradeOpenOrdersClick(openOrder, mode);
            }
        });
    }

    @Override
    public int getItemCount() { return tradeMarketOrderList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_price, tv_amount;
        private LinearLayout layout_main;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            layout_main = itemView.findViewById(R.id.layout_main);
        }
    }
}
