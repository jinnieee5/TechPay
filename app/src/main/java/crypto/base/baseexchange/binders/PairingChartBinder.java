package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import crypto.base.baseexchange.api.response.TradePairingCoinDetail;
import crypto.base.baseexchange.databinding.LayoutPairingChartBinding;
import crypto.base.baseexchange.views.HomeScreen;

public class PairingChartBinder {
    private Context context;
    private LayoutPairingChartBinding binding;
    private TradePairingCoinDetail pairingCoinDetail;
    private String firstCoinCode, secondCoinCode;

    public PairingChartBinder(final Context context, LayoutPairingChartBinding binding, TradePairingCoinDetail pairingCoinDetail, String firstCoinCode, String secondCoinCode) {
        this.context = context;
        this.binding = binding;
        this.pairingCoinDetail = pairingCoinDetail;
        this.firstCoinCode = firstCoinCode;
        this.secondCoinCode = secondCoinCode;

        setData();

        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setSupportZoom(false);
        String urlStart = "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_d4bc2&symbol=";
        String urlEnd = "&interval=30&saveimage=0&toolbarbg=131722&studies=ROC%40tv-basicstudies%1FStochasticRSI%\n" +
                "40tv-basicstudies%1FMASimple%40tv-basicstudies&theme=Dark&style=1&timezone=Asia%2FKolkata&withdateranges=1&studies_overrides=%7B%7D&overrides=%7B%22paneProperties.background%\n" +
                "22%3A%22%23131722%22%2C%22paneProperties.vertGridProperties.color%22%3A%22%23131722%22%2C%22paneProperties.horzGridProperties.color%22%3A%22%23131722%22%2C%22symbolWatermarkProperties.\n" +
                "transparency%22%3A90%2C%22scalesProperties.textColor%22%3A%22rgba(255%2C255%2C255%2C.8)%22%2C%22mainSeriesProperties.candleStyle.drawBorder%22%3Afalse%7D&enabled_features=%5B%5D&\n" +
                "disabled_features=%5B%5D&locale=e&utm_source=techpay.io&utm_medium=widget&utm_campaign=chart&utm_term=";

        if (firstCoinCode.equalsIgnoreCase("BTK") || secondCoinCode.equalsIgnoreCase("BTK")) {
            String url = "https://techpay.io/trading-chart?CurrencyPair=" + firstCoinCode + "_" + secondCoinCode;
            binding.webview.loadUrl(url);
        } else {
            if (firstCoinCode.equalsIgnoreCase("USDT")) firstCoinCode = "USD";
            String urlCoin = secondCoinCode + "" + firstCoinCode;
            Log.e("url", urlStart + urlCoin + urlEnd + urlCoin);
            binding.webview.loadUrl(urlStart + urlCoin + urlEnd + urlCoin);
        }
    }

    private void setData() {
        binding.tvHeader.setText(String.format("%s / %s", firstCoinCode, secondCoinCode));

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });

        binding.frameBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
                Intent intent = new Intent(context, HomeScreen.class);
                intent.putExtra("openFragmentName","trades");
                intent.putExtra("viewPagerSelection","buy");
                intent.putExtra("headerSelection",firstCoinCode);
                intent.putExtra("spinnerSelection",secondCoinCode);
                context.startActivity(intent);
            }
        });

        binding.frameSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
                Intent intent = new Intent(context, HomeScreen.class);
                intent.putExtra("openFragmentName","trades");
                intent.putExtra("viewPagerSelection","sell");
                intent.putExtra("headerSelection",firstCoinCode);
                intent.putExtra("spinnerSelection",secondCoinCode);
                context.startActivity(intent);
            }
        });

        binding.layoutHeaderPairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeScreen.class);
                intent.putExtra("openFragmentName","market");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
    }
}
