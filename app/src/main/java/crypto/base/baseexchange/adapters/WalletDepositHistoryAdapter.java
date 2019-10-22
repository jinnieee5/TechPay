package crypto.base.baseexchange.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.DepositTransactionList;
import crypto.base.baseexchange.utils.BaseUtils;

public class WalletDepositHistoryAdapter extends RecyclerView.Adapter<WalletDepositHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<DepositTransactionList> transactionList;

    public WalletDepositHistoryAdapter(Context context, List<DepositTransactionList> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public WalletDepositHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wallet_deposithistory, parent, false);
        return new WalletDepositHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletDepositHistoryAdapter.MyViewHolder holder, int position) {
        final DepositTransactionList depositData = transactionList.get(position);

        holder.tv_walletAddress.setText(depositData.getFromAddress());
        holder.tv_coinPair.setText(String.format("%s - %s", depositData.getFromWallet().toUpperCase(), depositData.getToWallet().toUpperCase()));
        holder.tv_date.setText(String.format("(%s)", depositData.getGeneratedDateAndTime()));

        DecimalFormat df = new DecimalFormat("#.00000000");
        holder.tv_amount.setText(df.format(depositData.getAmount()));

        String status = depositData.getStatus();
        if (status.equalsIgnoreCase("Confirm")) {
            holder.tv_liveStatus.setVisibility(View.GONE);

            holder.tv_status.setTextColor(context.getResources().getColor(R.color.deepGreen));
            holder.tv_status.setText("Approved");
        }
        else if (status.equalsIgnoreCase("Waiting for fund")) {
            holder.tv_liveStatus.setVisibility(View.VISIBLE);

            holder.tv_status.setTextColor(context.getResources().getColor(R.color.deepYellow));
            holder.tv_status.setText(status);

            holder.tv_liveStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(depositData.getExplorerLink())
                    );
                    context.startActivity(i);
                }
            });
        }

        holder.iv_copyWalletAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("wallet address",depositData.getFromAddress());
                clipboard.setPrimaryClip(clip);
                BaseUtils.customToast(context,"Wallet address copied.");
            }
        });
    }

    @Override
    public int getItemCount() { return transactionList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_walletAddress, tv_coinPair, tv_date, tv_amount, tv_status, tv_liveStatus;
        ImageView iv_copyWalletAddress;
        LinearLayout layout_pendingStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_walletAddress = itemView.findViewById(R.id.tv_walletAddress);
            tv_coinPair = itemView.findViewById(R.id.tv_coinPair);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_liveStatus = itemView.findViewById(R.id.tv_liveStatus);
            iv_copyWalletAddress = itemView.findViewById(R.id.iv_copyWalletAddress);
        }
    }
}
