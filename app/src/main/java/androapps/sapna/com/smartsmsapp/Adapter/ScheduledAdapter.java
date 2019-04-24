package androapps.sapna.com.smartsmsapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androapps.sapna.com.smartsmsapp.Model.ScheduleMessage;
import androapps.sapna.com.smartsmsapp.R;

/**
 * Created by Hp on 10/17/2018.
 */

public class ScheduledAdapter extends RecyclerView.Adapter<ScheduledAdapter.RecyclerViewHolder> {
    private ArrayList<ScheduleMessage> arrMsg;
    LayoutInflater inflater;
    Context context;


    public ScheduledAdapter(Context context, ArrayList<ScheduleMessage> arrMsg) {
        this.arrMsg = arrMsg;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout =  inflater.inflate(R.layout.row_scheduled_msg,parent,false);
        return new RecyclerViewHolder(layout);}

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tvNumber.setText(arrMsg.get(position).getStrName());
        holder.tvDesc.setText(arrMsg.get(position).getStrMessage());
        holder.tvDateTime.setText(arrMsg.get(position).getStrDate()+ " "+arrMsg.get(position).getStrTime() );
        if(arrMsg.get(position).getStrStatus().equalsIgnoreCase("FALSE"))
        holder.tvStatus.setText("Scheduled");
        else
            holder.tvStatus.setText("Sent");
    }

    @Override
    public int getItemCount() {
        return arrMsg.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNumber,tvDesc,tvDateTime,tvStatus;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDetails);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }


    }
}
