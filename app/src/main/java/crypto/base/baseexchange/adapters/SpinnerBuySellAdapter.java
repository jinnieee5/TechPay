package crypto.base.baseexchange.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.CoinPairingList;

public class SpinnerBuySellAdapter extends BaseAdapter {
    private Context context;
    private String firstCoinCode="";
    private List<CoinPairingList> coinPairingLists;

    public SpinnerBuySellAdapter(Context context, String firstCoinCode, List<CoinPairingList> coinPairingLists) {
        this.context = context;
        this.firstCoinCode = firstCoinCode;
        this.coinPairingLists = coinPairingLists;
    }

    @Override
    public int getCount() { return coinPairingLists.size(); }

    @Override
    public Object getItem(int position) { return coinPairingLists.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.row_buysell_spinner,null);

        if (!coinPairingLists.isEmpty()) {
            CoinPairingList secondCoin = coinPairingLists.get(position);
            TextView tv_firstCoin = v.findViewById(R.id.tv_firstCoin);
            TextView tv_secondCoin = v.findViewById(R.id.tv_secondCoin);
            TextView tv_secondCoinTCLMID = v.findViewById(R.id.tv_secondCoinTCLMID);
            TextView tv_secondCoinPurchaseTypeID = v.findViewById(R.id.tv_secondCoinPurchaseTypeID);

            String secondCoinTCLMID = coinPairingLists.get(position).getTclmid()+"";
            String secondCoinPurchaseTypeID = coinPairingLists.get(position).getPurchaseTypeID()+"";

            tv_firstCoin.setText(firstCoinCode.toUpperCase());
            tv_secondCoin.setText(secondCoin.getCoinCode().toUpperCase());
            tv_secondCoinTCLMID.setText(secondCoinTCLMID);
            tv_secondCoinPurchaseTypeID.setText(secondCoinPurchaseTypeID);

        } else Log.e("Adapter","list empty");

        return v;
    }
}
