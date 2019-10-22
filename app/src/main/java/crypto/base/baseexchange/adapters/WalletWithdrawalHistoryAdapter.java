package crypto.base.baseexchange.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.WithdrawalTransactionList;
import crypto.base.baseexchange.utils.BaseUtils;

public class WalletWithdrawalHistoryAdapter extends RecyclerView.Adapter<WalletWithdrawalHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<WithdrawalTransactionList> transactionList;

    public WalletWithdrawalHistoryAdapter(Context context, List<WithdrawalTransactionList> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public WalletWithdrawalHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wallet_withdrawalhistory, parent, false);
        return new WalletWithdrawalHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletWithdrawalHistoryAdapter.MyViewHolder holder, int position) {
        final WithdrawalTransactionList withdrawalData = transactionList.get(position);

        holder.tv_orderNo.setText(withdrawalData.getOrderNo());
        holder.tv_coinName.setText(String.format("%s- ", withdrawalData.getCoinCode()));
        holder.tv_walletAddress.setText(withdrawalData.getToWalletAddress());
        holder.tv_amount.setText(String.format("Amount - %s", withdrawalData.getAmount()));
        holder.tv_transactionFees.setText(String.format("Transaction Fes - %s", withdrawalData.getTransactionFee()));
        holder.tv_netPayable.setText(String.format("Net Payable - %s", withdrawalData.getNetPayable()));
        holder.tv_date.setText(String.format("(%s)", withdrawalData.getActivityDateAndTime()));

        String status = withdrawalData.getStatus();
        holder.tv_status.setText(status);

        holder.iv_copyWalletAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("wallet address",withdrawalData.getToWalletAddress());
                clipboard.setPrimaryClip(clip);
                BaseUtils.customToast(context,"Wallet address copied.");
            }
        });
    }

    @Override
    public int getItemCount() { return transactionList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_orderNo, tv_coinName, tv_walletAddress, tv_amount, tv_transactionFees, tv_netPayable, tv_status, tv_date;
        ImageView iv_copyWalletAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_orderNo = itemView.findViewById(R.id.tv_orderNo);
            tv_coinName = itemView.findViewById(R.id.tv_coinName);
            tv_walletAddress = itemView.findViewById(R.id.tv_walletAddress);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_transactionFees = itemView.findViewById(R.id.tv_transactionFees);
            tv_netPayable = itemView.findViewById(R.id.tv_netPayable);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_copyWalletAddress = itemView.findViewById(R.id.iv_copyWalletAddress);
        }
    }
}
