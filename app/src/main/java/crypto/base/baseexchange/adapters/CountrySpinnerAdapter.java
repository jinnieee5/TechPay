package crypto.base.baseexchange.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.MessageFormat;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.CountryList;

public class CountrySpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<CountryList> countryList;

    public CountrySpinnerAdapter(Context context, List<CountryList> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    @Override
    public int getCount() { return countryList.size(); }

    @Override
    public Object getItem(int position) { return countryList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.row_simple_spinner,null);

        TextView tv_name = v.findViewById(R.id.tv_name);
        tv_name.setText(countryList.get(position).getCountryName());

        TextView tv_id = v.findViewById(R.id.tv_id);
        tv_id.setText(MessageFormat.format("{0}", countryList.get(position).getCID()));

        return v;
    }
}
