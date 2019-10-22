package crypto.base.baseexchange.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.MessageFormat;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.TimeInForceData;

public class TimeInForceSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<TimeInForceData> timeInForceDataList;

    public TimeInForceSpinnerAdapter(Context context, List<TimeInForceData> timeInForceDataList) {
        this.context = context;
        this.timeInForceDataList = timeInForceDataList;
    }

    @Override
    public int getCount() { return timeInForceDataList.size(); }

    @Override
    public Object getItem(int position) { return timeInForceDataList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.row_simple_spinner,null);

        TextView tv_name = v.findViewById(R.id.tv_name);
        tv_name.setTextSize(12f);
        tv_name.setSingleLine(true);
        tv_name.setTextColor(context.getResources().getColor(R.color.white));
        tv_name.setText(timeInForceDataList.get(position).getTimeInForce());

        TextView tv_id = v.findViewById(R.id.tv_id);
        tv_id.setText(MessageFormat.format("{0}", timeInForceDataList.get(position).getTtifmid()));

        LinearLayout layout_main = v.findViewById(R.id.layout_main);
        layout_main.setBackgroundColor(context.getResources().getColor(R.color.headerColor));

        return v;
    }
}
