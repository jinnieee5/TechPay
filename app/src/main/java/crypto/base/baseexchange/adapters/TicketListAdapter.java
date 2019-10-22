package crypto.base.baseexchange.adapters;

import android.content.Context;
import android.content.Intent;
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
import crypto.base.baseexchange.api.response.TicketList;
import crypto.base.baseexchange.views.TicketReplyScreen;
import de.hdodenhof.circleimageview.CircleImageView;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.MyViewHolder> {
    private Context context;
    private List<TicketList> ticketList;

    public TicketListAdapter(Context context, List<TicketList> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ticket_list, parent, false);
        return new TicketListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketListAdapter.MyViewHolder holder, int position) {
        final TicketList ticket = ticketList.get(position);

        if (ticket.getPriority().equalsIgnoreCase("Low")) holder.iv_ticketPriorityColor.setBackground(context.getDrawable(R.drawable.shape_priority_low));
        else if (ticket.getPriority().equalsIgnoreCase("Normal")) holder.iv_ticketPriorityColor.setBackground(context.getDrawable(R.drawable.shape_priority_medium));
        else if (ticket.getPriority().equalsIgnoreCase("High")) holder.iv_ticketPriorityColor.setBackground(context.getDrawable(R.drawable.shape_priority_high));

        String imageURLs[] = ticket.getTmImage().split(",");
        if (!imageURLs[0].isEmpty()) Picasso.get().load(imageURLs[0]).placeholder(R.drawable.ticket).error(R.drawable.ticket).into(holder.iv_ticketImage);

        holder.tv_priority.setText(ticket.getPriority());
        holder.tv_ticketNo.setText(String.format("#%s", ticket.getTicketNo()));
        holder.tv_ticketStatus.setText(ticket.getStatus());
        holder.tv_subject.setText(ticket.getSubject());

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, TicketReplyScreen.class);;
                it.putExtra("tmid",ticket.getTmid()+"");
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() { return ticketList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_ticketImage, iv_ticketPriorityColor;
        private TextView tv_priority, tv_ticketNo, tv_ticketStatus, tv_subject;
        private ConstraintLayout layout_main;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_main = itemView.findViewById(R.id.layout_main);

            iv_ticketImage = itemView.findViewById(R.id.iv_ticketImage);
            iv_ticketPriorityColor = itemView.findViewById(R.id.iv_ticketPriorityColor);

            tv_priority = itemView.findViewById(R.id.tv_priority);
            tv_ticketNo = itemView.findViewById(R.id.tv_ticketNo);
            tv_ticketStatus = itemView.findViewById(R.id.tv_ticketStatus);
            tv_subject = itemView.findViewById(R.id.tv_subject);
        }
    }
}
