package crypto.base.baseexchange.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.LoginHistoryList;

public class LoginHistoryAdapter extends RecyclerView.Adapter<LoginHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<LoginHistoryList> loginHistoryList;

    public LoginHistoryAdapter(Context context, List<LoginHistoryList> loginHistoryList) {
        this.context = context;
        this.loginHistoryList = loginHistoryList;
    }

    @NonNull
    @Override
    public LoginHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_login_history, parent, false);
        return new LoginHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginHistoryAdapter.MyViewHolder holder, int position) {
        LoginHistoryList loginHistory = loginHistoryList.get(position);

        if (loginHistory.getDate().trim().isEmpty()) holder.tv_date.setText("-");
        else holder.tv_date.setText(loginHistory.getDate());

        if (loginHistory.getIPAddress().trim().isEmpty()) holder.tv_ip.setText("-");
        else holder.tv_ip.setText(loginHistory.getIPAddress());

        if (loginHistory.getLocation().trim().isEmpty()) holder.tv_location.setText("-");
        else holder.tv_location.setText(loginHistory.getLocation());

        holder.tv_deviceName.setText(loginHistory.getDeviceName());
        if (loginHistory.getDeviceName().trim().equalsIgnoreCase("mobile")) holder.iv_device.setImageDrawable(context.getResources().getDrawable(R.drawable.mobile));
        else if (loginHistory.getDeviceName().trim().equalsIgnoreCase("desktop")) holder.iv_device.setImageDrawable(context.getResources().getDrawable(R.drawable.desktop));

    }

    @Override
    public int getItemCount() { return loginHistoryList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_date, tv_ip, tv_location, tv_deviceName;
        private ImageView iv_device;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            tv_ip = itemView.findViewById(R.id.tv_ip);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_deviceName = itemView.findViewById(R.id.tv_deviceName);
            iv_device = itemView.findViewById(R.id.iv_device);
        }
    }
}
