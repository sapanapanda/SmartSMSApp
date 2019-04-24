package androapps.sapna.com.smartsmsapp.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androapps.sapna.com.smartsmsapp.Activity.ScheduleMsg;
import androapps.sapna.com.smartsmsapp.Adapter.ScheduledAdapter;
import androapps.sapna.com.smartsmsapp.DbHelper;
import androapps.sapna.com.smartsmsapp.Model.ScheduleMessage;
import androapps.sapna.com.smartsmsapp.Model.VerticalSpaceItemDecoration;
import androapps.sapna.com.smartsmsapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {
@BindView(R.id.rvSchedule)
    RecyclerView rvSchedule;
@BindView(R.id.fab)
    FloatingActionButton fab;
ArrayList<ScheduleMessage> smsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this,view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvSchedule.setLayoutManager(manager);
        rvSchedule.addItemDecoration(new VerticalSpaceItemDecoration(5));

        fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ScheduleMsg.class);
            startActivity(intent);
        }
    });


    return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        readMessages();
    }

    private void readMessages(){
        smsList.clear();
        DbHelper helper = new DbHelper(getActivity().getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM SCHEDULEDMSG WHERE STATUS ='FALSE' ",null);
if(cursor.getCount()>0) {
    cursor.moveToFirst();
    do {

        ScheduleMessage sms = new ScheduleMessage();
        sms.setStrMessage(cursor.getString(cursor.getColumnIndex("MSG")));
        sms.setStrName(cursor.getString(cursor.getColumnIndex("NAME")));
        sms.setStrNumber(cursor.getString(cursor.getColumnIndex("RNUMBER")));
        sms.setStrDate(cursor.getString(cursor.getColumnIndex("SDATE")));
        sms.setStrTime(cursor.getString(cursor.getColumnIndex("STIME")));
        sms.setStrStatus(cursor.getString(cursor.getColumnIndex("STATUS")));
        smsList.add(sms);
    } while (cursor.moveToNext());
}
        cursor.close();
        database.close();


        ScheduledAdapter adapter = new ScheduledAdapter(getActivity(),smsList);
        rvSchedule.setAdapter(adapter);




    }

}
