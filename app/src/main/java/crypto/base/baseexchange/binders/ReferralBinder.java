package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import crypto.base.baseexchange.R;
import crypto.base.baseexchange.adapters.ReferralRegisteredAdapter;
import crypto.base.baseexchange.databinding.LayoutReferralBinding;
import crypto.base.baseexchange.test.RegisteredReferralList;

public class ReferralBinder {
    private Context context;
    private LayoutReferralBinding binding;

    public ReferralBinder(Context context, LayoutReferralBinding binding){
        this.context = context;
        this.binding = binding;
        setHeader();
        setRecyclerView();
    }

    private void setRecyclerView() {
        ArrayList<RegisteredReferralList> arrayList = new ArrayList<>();
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));
        arrayList.add(new RegisteredReferralList("20/10/2020", "1234"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvRegisteredReferral.setLayoutManager(layoutManager);
        binding.rvRegisteredReferral.setAdapter(new ReferralRegisteredAdapter(context, arrayList));
    }

    private void setHeader() {
        TextView tv_header = binding.includeHeader.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(context.getResources().getString(R.string.header_referral));

        ImageView iv_endIcon = binding.includeHeader.findViewById(R.id.iv_endIcon);
        iv_endIcon.setVisibility(View.GONE);

        ImageView iv_back = binding.includeHeader.findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
    }



}
