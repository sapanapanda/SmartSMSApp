package androapps.sapna.com.smartsmsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androapps.sapna.com.smartsmsapp.Activity.SendMessage;
import androapps.sapna.com.smartsmsapp.Model.Message;
import androapps.sapna.com.smartsmsapp.R;

/**
 * Created by Hp on 10/17/2018.
 */

public class DraftRecyclerAdapter extends RecyclerView.Adapter<DraftRecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<Message> arrMsg;
    LayoutInflater inflater;
    Context context;


    public DraftRecyclerAdapter(Context context, ArrayList<Message> arrMsg) {
        this.arrMsg = arrMsg;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout =  inflater.inflate(R.layout.row_msg,parent,false);
        return new RecyclerViewHolder(layout);}

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.tvNumber.setText(arrMsg.get(position).getStrName());
        holder.tvDesc.setText(arrMsg.get(position).getStrMessage());
    }

    @Override
    public int getItemCount() {
        return arrMsg.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvNumber,tvDesc;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvNumber = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDetails);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, SendMessage.class);
            intent.putExtra("Name",arrMsg.get(getLayoutPosition()).getStrName());
            intent.putExtra("Number",arrMsg.get(getLayoutPosition()).getStrNumber());
            intent.putExtra("Msg",arrMsg.get(getLayoutPosition()).getStrMessage());
            context.startActivity(intent);
        }
    }
}
