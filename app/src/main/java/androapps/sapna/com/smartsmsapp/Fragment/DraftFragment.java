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

import androapps.sapna.com.smartsmsapp.Activity.Contact;
import androapps.sapna.com.smartsmsapp.Adapter.DraftRecyclerAdapter;
import androapps.sapna.com.smartsmsapp.DbHelper;
import androapps.sapna.com.smartsmsapp.Model.Message;
import androapps.sapna.com.smartsmsapp.Model.VerticalSpaceItemDecoration;
import androapps.sapna.com.smartsmsapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DraftFragment extends Fragment {
    ArrayList<Message> smsList = new ArrayList<Message>();

    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;

    @BindView(R.id.fab)
    FloatingActionButton fab;
       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_draft, container, false);
           ButterKnife.bind(this,view);
           LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
           rvMessages.setLayoutManager(manager);
           rvMessages.addItemDecoration(new VerticalSpaceItemDecoration(5));

           fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(getActivity(),Contact.class);
                   DbHelper.strCaller ="Message";
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
//        // Create Inbox box URI
//        Uri inboxURI = Uri.parse("content://sms/draft");
//
//// List required columns
//        String[] reqCols = new String[] { "_id", "address", "body" };
//
//// Get Content Resolver object, which will deal with Content Provider
//        ContentResolver cr = getActivity().getContentResolver();
//
//// Fetch Inbox SMS Message from Built-in Content Provider
//        Cursor c = cr.query(inboxURI, reqCols, null, null, null);
//
//
//        // Read the sms data and store it in the list
//        if(c.moveToFirst()) {
//            for(int i=0; i < c.getCount(); i++) {
//                Message sms = new Message();
//                sms.setStrMessage(c.getString(c.getColumnIndexOrThrow("body")).toString());
//                sms.setStrNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
//                smsList.add(sms);
//
//                c.moveToNext();
//            }
//        }
//        c.close();
        smsList.clear();

        DbHelper helper = new DbHelper(getActivity().getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM DRAFTMSG",null);

        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {

                Message sms = new Message();
                sms.setStrMessage(cursor.getString(cursor.getColumnIndex("MSG")));
                sms.setStrName(cursor.getString(cursor.getColumnIndex("NAME")));
                sms.setStrNumber(cursor.getString(cursor.getColumnIndex("RNUMBER")));
                smsList.add(sms);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();


        DraftRecyclerAdapter adapter = new DraftRecyclerAdapter(getActivity().getApplicationContext(),smsList);
        rvMessages.setAdapter(adapter);




    }

}
