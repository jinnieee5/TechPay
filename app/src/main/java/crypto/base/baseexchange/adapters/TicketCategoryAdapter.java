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
import crypto.base.baseexchange.api.response.TicketCategoryList;

public class TicketCategoryAdapter extends BaseAdapter {
    private Context context;
    private List<TicketCategoryList> categoryList;

    public TicketCategoryAdapter(Context context, List<TicketCategoryList> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() { return categoryList.size(); }

    @Override
    public Object getItem(int position) { return categoryList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View v = inflater.inflate(R.layout.row_simple_spinner,null);

        TextView tv_name = v.findViewById(R.id.tv_name);
        tv_name.setText(categoryList.get(position).getCategoryName());

        TextView tv_id = v.findViewById(R.id.tv_id);
        tv_id.setText(MessageFormat.format("{0}", categoryList.get(position).getCatID()));

        return v;
    }
}
