package crypto.base.baseexchange.binders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.databinding.LayoutReplyTicketBinding;

public class TicketReplyBinder {
    private Context context;
    private LayoutReplyTicketBinding binding;

    public TicketReplyBinder(Context context, LayoutReplyTicketBinding binding) {
        this.context = context;
        this.binding = binding;

        setHeader();
    }

    private void setHeader() {
        TextView tv_header = binding.includeHeader.findViewById(R.id.tv_header);
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText(context.getResources().getString(R.string.header_replyTicket));

        ImageView iv_endIcon = binding.includeHeader.findViewById(R.id.iv_endIcon);
        iv_endIcon.setVisibility(View.GONE);

        ImageView iv_back = binding.includeHeader.findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
    }
}
