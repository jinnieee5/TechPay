package crypto.base.baseexchange.binders;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.adapters.MarketPairSelectionAdapter;
import crypto.base.baseexchange.api.response.CoinPairingList;
import crypto.base.baseexchange.databinding.LayoutMarketPairselectionBinding;
import crypto.base.baseexchange.interfaces.MarketPairSelectionListener;
import crypto.base.baseexchange.utils.CoinPairSelectionData;
import crypto.base.baseexchange.views.PairingChartScreen;

public class MarketPairSelectionBinder implements MarketPairSelectionListener {
    private Context context;
    private LayoutMarketPairselectionBinding binding;
    private String callFrom;

    public MarketPairSelectionBinder(Context context, LayoutMarketPairselectionBinding binding, String callFrom) {
        this.context = context;
        this.binding = binding;
        this.callFrom = callFrom;

        List<CoinPairingList> coinPairingList = new ArrayList<>();
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());
        coinPairingList.add(new CoinPairingList());

        LinearLayoutManager sellLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvCoinPairingList.setLayoutManager(sellLayoutManager);
        binding.rvCoinPairingList.setAdapter(new MarketPairSelectionAdapter(context, coinPairingList, "", MarketPairSelectionBinder.this));
    }

    @Override
    public void onPairSelectionClick(CoinPairSelectionData coinPairSelectionData) {
        Intent intent;
        switch (callFrom) {
            case "":
                intent = new Intent(context, PairingChartScreen.class);
                context.startActivity(intent);
                break;
            case "PairingChartScreen":
                intent = new Intent(context, PairingChartScreen.class);
                context.startActivity(intent);
                break;
        }
    }
}
