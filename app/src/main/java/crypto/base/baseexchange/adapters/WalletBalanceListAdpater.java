package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.WalletBalancesList;
import crypto.base.baseexchange.views.WalletBalanceDetailScreen;

public class WalletBalanceListAdpater extends RecyclerView.Adapter<WalletBalanceListAdpater.MyViewHolder> {
    private Context context;
    private List<WalletBalancesList> walletBalanceList;

    public WalletBalanceListAdpater(Context context, List<WalletBalancesList> walletBalanceList) {
        this.context = context;
        this.walletBalanceList = walletBalanceList;
    }

    @NonNull
    @Override
    public WalletBalanceListAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wallet_balancelist, parent, false);
        return new WalletBalanceListAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletBalanceListAdpater.MyViewHolder holder, int position) {
        final WalletBalancesList walletBalance = walletBalanceList.get(position);

        String url = context.getResources().getString(R.string.coinImageUrl)+""+(walletBalance.getCoinName().toLowerCase()).replaceAll("\\s","")+".png";
        Log.e("url",url);
        Picasso.get().load(url).error(context.getResources().getDrawable(R.drawable.default_coin)).into(holder.iv_logo);

        holder.tv_code.setText(walletBalance.getCoinCode());
        holder.tv_name.setText(walletBalance.getCoinName());

        double d = walletBalance.getAvailable();

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(Integer.MAX_VALUE);
        holder.tv_amount.setText(nf.format(d));

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, WalletBalanceDetailScreen.class);
                it.putExtra("WalletBalanceObject",walletBalance);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() { return walletBalanceList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_main;
        ImageView iv_logo;
        TextView tv_code, tv_name, tv_amount;

        public MyViewHolder(View itemView) {
            super(itemView);

            layout_main = itemView.findViewById(R.id.layout_main);
            iv_logo = itemView.findViewById(R.id.iv_logo);
            tv_code = itemView.findViewById(R.id.tv_code);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
        }
    }
}
