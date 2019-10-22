package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.CoinPairingList;
import crypto.base.baseexchange.interfaces.MarketPairSelectionListener;
import crypto.base.baseexchange.utils.CoinPairSelectionData;

public class MarketPairSelectionAdapter extends RecyclerView.Adapter<MarketPairSelectionAdapter.MyViewHolder> {
    private Context context;
    private List<CoinPairingList> pairingList;
    private String firstCoinCode;
    private MarketPairSelectionListener listener;

    public MarketPairSelectionAdapter(Context context, List<CoinPairingList> pairingList, String firstCoinCode, MarketPairSelectionListener listener) {
        this.context = context;
        this.pairingList = pairingList;
        this.firstCoinCode = firstCoinCode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MarketPairSelectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_marketpair_selection, parent, false);
        return new MarketPairSelectionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketPairSelectionAdapter.MyViewHolder holder, int position) {
        CoinPairingList pairing = pairingList.get(position);

        if (position%3==0) {
            holder.tv_lastPrice.setTextColor(context.getResources().getColor(R.color.tradeSellColor));
        } else {
            holder.tv_lastPrice.setTextColor(context.getResources().getColor(R.color.tradeBuyColor));
        }

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPairSelectionClick(new CoinPairSelectionData());
            }
        });
    }

    @Override
    public int getItemCount() { return pairingList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_secondCoinName, tv_firstCoinName, tv_lastPrice;
        LinearLayout layout_main;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_secondCoinName = itemView.findViewById(R.id.tv_secondCoinName);
            tv_firstCoinName = itemView.findViewById(R.id.tv_firstCoinName);
            tv_lastPrice = itemView.findViewById(R.id.tv_lastPrice);
            layout_main = itemView.findViewById(R.id.layout_main);
        }
    }
}
