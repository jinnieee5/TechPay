package crypto.base.baseexchange.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.TicketPriorityList;

public class TicketPriorityAdapter extends BaseAdapter {
    private Context context;
    private List<TicketPriorityList> priorityList;

    public TicketPriorityAdapter(Context context, List<TicketPriorityList> priorityList) {
        this.context = context;
        this.priorityList = priorityList;
    }

    @Override
    public int getCount() { return priorityList.size(); }

    @Override
    public Object getItem(int position) { return priorityList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.row_simple_spinner,null);

        TextView tv_name = v.findViewById(R.id.tv_name);
        tv_name.setText(priorityList.get(position).getText());

        TextView tv_id = v.findViewById(R.id.tv_id);
        tv_id.setText(priorityList.get(position).getValue());

        return v;
    }
}
