package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.CoinPairingList;
import crypto.base.baseexchange.api.response.TradePairingCoinDetail;
import crypto.base.baseexchange.views.PairingChartScreen;

public class MarketPairingAdapter extends RecyclerView.Adapter<MarketPairingAdapter.MyViewHolder> {
    private Context context;
    private List<CoinPairingList> pairingList;
    private String firstCoinCode;

    public MarketPairingAdapter(Context context, List<CoinPairingList> pairingList, String firstCoinCode) {
        this.context = context;
        this.pairingList = pairingList;
        this.firstCoinCode = firstCoinCode;
    }

    @NonNull
    @Override
    public MarketPairingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_market_pairing, parent, false);
        return new MarketPairingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketPairingAdapter.MyViewHolder holder, int position) {
        final CoinPairingList pairing = pairingList.get(position);

        holder.tv_secondCoinName.setText(firstCoinCode);
        holder.tv_firstCoinName.setText(String.format(" / %s", pairing.getCoinCode()));
        holder.tv_volume.setText(String.format("Vol %s", new DecimalFormat("#.0000").format(pairing.getVolume24H())));
        holder.tv_lastPrice.setText((new DecimalFormat("#.00000000").format(pairing.getPrice())));

        double change = pairing.getChangePrice();
        if (change>=0) {
            holder.tv_24Chg.setText(String.format("+ %s", (new DecimalFormat("#.00")).format(change)));
            holder.tv_24Chg.setTextColor(context.getResources().getColor(R.color.deepGreen));
        } else {
            holder.tv_24Chg.setText((new DecimalFormat("#.00")).format(change));
            holder.tv_24Chg.setTextColor(context.getResources().getColor(R.color.deepRed));
        }

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PairingChartScreen.class);
                intent.putExtra("firstCoinCode",firstCoinCode);
                intent.putExtra("secondCoinCode",pairing.getCoinCode());
                intent.putExtra("pairingCoinDetail",new TradePairingCoinDetail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return pairingList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_secondCoinName, tv_firstCoinName, tv_volume, tv_lastPrice, tv_lastPriceInUsd, tv_24Chg;
        FrameLayout frame_chg;
        LinearLayout layout_main;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_secondCoinName = itemView.findViewById(R.id.tv_secondCoinName);
            tv_firstCoinName = itemView.findViewById(R.id.tv_firstCoinName);
            tv_volume = itemView.findViewById(R.id.tv_volume);
            tv_lastPrice = itemView.findViewById(R.id.tv_lastPrice);
            tv_lastPriceInUsd = itemView.findViewById(R.id.tv_lastPriceInUsd);
            tv_24Chg = itemView.findViewById(R.id.tv_24Chg);
            frame_chg = itemView.findViewById(R.id.frame_chg);
            layout_main = itemView.findViewById(R.id.layout_main);
        }
    }
}
