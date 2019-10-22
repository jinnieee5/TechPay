package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.TopReferralData;
import crypto.base.baseexchange.test.RegisteredReferralList;

public class TopReferralPartnersAdapter extends RecyclerView.Adapter<TopReferralPartnersAdapter.MyViewHolder> {

    private Context context;
    List<TopReferralData> list;

    public TopReferralPartnersAdapter(Context context, List<TopReferralData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_top_referral_partners_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TopReferralData data = list.get(position);
        holder.tvTopReferralIds.setText(data.getTradesAccountNumber());
        holder.tvTotalReferrals.setText(data.getTotalReferralMember() + "");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopReferralIds, tvTotalReferrals;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTopReferralIds = itemView.findViewById(R.id.tv_top_referrals_ids);
            tvTotalReferrals = itemView.findViewById(R.id.tv_total_referralss);
        }
    }
}
