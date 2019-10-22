package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.MarketCoin;
import crypto.base.baseexchange.interfaces.CoinMarketListHeaderListener;

public class MarketCoinListHeaderAdapter extends RecyclerView.Adapter<MarketCoinListHeaderAdapter.MyViewHolder> {
    private Context context;
    private List<MarketCoin> marketCoinList;
    private int selectedPosition=-1;
    private CoinMarketListHeaderListener listener;
    private String firstCoinCode;
    private int pairingID;

    public MarketCoinListHeaderAdapter(Context context, List<MarketCoin> marketCoinList, CoinMarketListHeaderListener listener, String firstCoinCode, int pairingID) {
        this.context = context;
        this.marketCoinList = marketCoinList;
        this.listener = listener;
        this.firstCoinCode=firstCoinCode;
        this.pairingID=pairingID;
    }

    @NonNull
    @Override
    public MarketCoinListHeaderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_marketcoinlist_header, parent, false);
        return new MarketCoinListHeaderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketCoinListHeaderAdapter.MyViewHolder holder, final int position) {
        final MarketCoin marketCoin = marketCoinList.get(position);

        Log.e("view","selected position - "+selectedPosition);
        if (!firstCoinCode.isEmpty() && pairingID!=0 && firstCoinCode.equalsIgnoreCase(marketCoin.getCoinCode()) && pairingID==marketCoin.getPairingID()) {
            selectedPosition=position;
        }

        holder.tv_coinName.setText(marketCoin.getCoinCode());
        if(selectedPosition==position) holder.cv_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        else holder.cv_background.setCardBackgroundColor(context.getResources().getColor(R.color.textColorDark));

        holder.cv_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
                Log.e("click","selected position - "+selectedPosition);
                firstCoinCode="";
                pairingID=-1;
                listener.onMarketCoinClick(marketCoin.getCoinCode(), marketCoin.getPairingID(), marketCoin.getTpspmid(), false);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() { return marketCoinList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_coinName;
        private CardView cv_background;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_coinName=itemView.findViewById(R.id.tv_coinName);
            cv_background = itemView.findViewById(R.id.cv_background);
        }
    }
}
