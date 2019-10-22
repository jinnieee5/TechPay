package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import crypto.base.baseexchange.R;
import crypto.base.baseexchange.api.response.TicketChatList;
import crypto.base.baseexchange.utils.SharedPrefUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class TicketReplyAdapter extends RecyclerView.Adapter<TicketReplyAdapter.MyViewHolder> {
    private Context context;
    private List<TicketChatList> ticketChatLists;

    public TicketReplyAdapter(Context context, List<TicketChatList> ticketChatLists) {
        this.context = context;
        this.ticketChatLists = ticketChatLists;
    }

    @NonNull
    @Override
    public TicketReplyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ticket_chat, parent, false);
        return new TicketReplyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketReplyAdapter.MyViewHolder holder, int position) {
        TicketChatList ticketChat = ticketChatLists.get(position);

        if (ticketChat.getFromType().equalsIgnoreCase("Me")) {
            holder.layout_me.setVisibility(View.VISIBLE);
            holder.layout_support.setVisibility(View.GONE);

            if (SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.ProfilePic)!=null && !SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.ProfilePic).isEmpty()) {
                String imageURL = SharedPrefUtils.getFromPrefs(context,SharedPrefUtils.ProfilePic);
                Picasso.get().load(imageURL).placeholder(R.drawable.user).error(R.drawable.user).into(holder.iv_me);
            } else holder.iv_me.setImageDrawable(context.getDrawable(R.drawable.user));

            holder.tv_me.setText(ticketChat.getFromType());
            holder.tv_messageMe.setText(ticketChat.getMessage());
            holder.tv_dateMe.setText(ticketChat.getDate());
        }
        else if (ticketChat.getFromType().equalsIgnoreCase("Support")) {
            holder.layout_me.setVisibility(View.GONE);
            holder.layout_support.setVisibility(View.VISIBLE);

            holder.iv_support.setImageDrawable(context.getDrawable(R.drawable.support));
            holder.tv_support.setText(ticketChat.getFromType());
            holder.tv_message.setText(ticketChat.getMessage());
            holder.tv_date.setText(ticketChat.getDate());
        }
    }

    @Override
    public int getItemCount() { return ticketChatLists.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout_support, layout_me;
        private CircleImageView iv_support, iv_me;
        private TextView tv_support, tv_message, tv_date, tv_me, tv_messageMe, tv_dateMe;

        public MyViewHolder(View itemView) {
            super(itemView);

            layout_support = itemView.findViewById(R.id.layout_support);
            layout_me = itemView.findViewById(R.id.layout_me);

            iv_support = itemView.findViewById(R.id.iv_support);
            iv_me = itemView.findViewById(R.id.iv_me);

            tv_support = itemView.findViewById(R.id.tv_support);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_me = itemView.findViewById(R.id.tv_me);
            tv_messageMe = itemView.findViewById(R.id.tv_messageMe);
            tv_dateMe = itemView.findViewById(R.id.tv_dateMe);
        }
    }
}
