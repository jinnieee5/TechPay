package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.WalletBalancesList;
import crypto.base.baseexchange.databinding.LayoutWalletBalancedetailBinding;
import crypto.base.baseexchange.views.CoinDepositScreen;
import crypto.base.baseexchange.views.CoinWithdrawalScreen;

public class WalletBalanceDetailBinder {
    private Context context;
    private LayoutWalletBalancedetailBinding binding;
    private WalletBalancesList walletBalance;

    public WalletBalanceDetailBinder(Context context, LayoutWalletBalancedetailBinding binding, WalletBalancesList walletBalance) {
        this.context = context;
        this.binding = binding;
        this.walletBalance = walletBalance;

        setHeader();
        setLayout();
    }

    private void setHeader() {
        TextView tv_header = binding.layoutWalletHeader.findViewById(R.id.tv_header);
        ImageView iv_back = binding.layoutWalletHeader.findViewById(R.id.iv_back);
        ImageView iv_endIcon = binding.layoutWalletHeader.findViewById(R.id.iv_endIcon);

        tv_header.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_endIcon.setVisibility(View.GONE);

        tv_header.setText(context.getString(R.string.header_balance));

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    private void setLayout() {
        String url = "https://techpayexchange-backend.techpay.io/CoinImages/"+(walletBalance.getCoinName().toLowerCase()).replaceAll("\\s","")+".png";
        Log.e("url",url);
        Picasso.get().load(url).error(context.getResources().getDrawable(R.drawable.default_coin)).into(binding.ivLogo);

        DecimalFormat df = new DecimalFormat("#.00000000");

        binding.tvCoinCode.setText(walletBalance.getCoinCode());
        binding.tvCoinName.setText(String.format("(%s)", walletBalance.getCoinName()));
        binding.tvTotalValue.setText(df.format(walletBalance.getTotalIncome()));
        binding.tvAvailableValue.setText(df.format(walletBalance.getAvailable()));
        binding.tvInOrderValue.setText(df.format(walletBalance.getTradeValue()));

        String change = new DecimalFormat("#.00").format(walletBalance.getPercentageChange());
        binding.tvChangeValue.setText(change+" %");
        if (change.startsWith("-")) binding.tvChangeValue.setTextColor(context.getResources().getColor(R.color.deepRed));
        else binding.tvChangeValue.setTextColor(context.getResources().getColor(R.color.deepGreen));

        double estUsdtValue = (walletBalance.getAvailable())*walletBalance.getPriceInUSD();
        binding.tvEstUsdtValue.setText(df.format(estUsdtValue));

        double estBtcValue = (walletBalance.getAvailable())*walletBalance.getPriceInBTC();
        binding.tvEstBtcValue.setText(df.format(estBtcValue));

        binding.frameDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoinDepositScreen.class);
                intent.putExtra("WalletBalanceObject",walletBalance);
                context.startActivity(intent);
            }
        });

        binding.frameWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoinWithdrawalScreen.class);
                intent.putExtra("WalletBalanceObject",walletBalance);
                context.startActivity(intent);
            }
        });
    }
}
