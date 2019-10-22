package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.databinding.LayoutCoinWithdrawalBinding;

public class CoinWithdrawalBinder {
    private Context context;
    private LayoutCoinWithdrawalBinding binding;

    public CoinWithdrawalBinder(Context context, LayoutCoinWithdrawalBinding binding) {
        this.context = context;
        this.binding = binding;

        setHeader();
    }

    private void setHeader() {
        ImageView iv_back = binding.layoutHeader.findViewById(R.id.iv_back);
        ImageView iv_endIcon = binding.layoutHeader.findViewById(R.id.iv_endIcon);
        TextView tv_header = binding.layoutHeader.findViewById(R.id.tv_header);

        iv_back.setVisibility(View.VISIBLE);
        iv_endIcon.setVisibility(View.GONE);
        tv_header.setVisibility(View.VISIBLE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });

        tv_header.setText("Withdrawal");
    }
}
