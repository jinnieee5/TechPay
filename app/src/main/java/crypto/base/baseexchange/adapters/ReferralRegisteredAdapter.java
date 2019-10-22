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
import crypto.base.baseexchange.test.RegisteredReferralList;

public class ReferralRegisteredAdapter extends RecyclerView.Adapter<ReferralRegisteredAdapter.MyViewHolder> {

    private Context context;
    private List<RegisteredReferralList> registeredReferralLists;

    public ReferralRegisteredAdapter(Context context, List<RegisteredReferralList> registeredReferralLists) {
        this.context = context;
        this.registeredReferralLists = registeredReferralLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_referral_registered_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RegisteredReferralList registeredReferralList = registeredReferralLists.get(position);

        holder.tv_date.setText(registeredReferralList.getDate());
        holder.tv_registered_referral.setText(registeredReferralList.getRegisteredReferral());
    }

    @Override
    public int getItemCount() {
        return registeredReferralLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tv_registered_referral;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            tv_registered_referral = itemView.findViewById(R.id.tv_registered_referrals);
        }
    }
}
