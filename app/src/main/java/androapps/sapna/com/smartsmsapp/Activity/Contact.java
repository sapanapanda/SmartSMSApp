package androapps.sapna.com.smartsmsapp.Activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import androapps.sapna.com.smartsmsapp.DbHelper;
import androapps.sapna.com.smartsmsapp.Model.ContactModel;
import androapps.sapna.com.smartsmsapp.Model.VerticalSpaceItemDecoration;
import androapps.sapna.com.smartsmsapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Contact extends AppCompatActivity {
    ArrayList<ContactModel> smsList = new ArrayList<>();

    ArrayList<ContactModel> sortList = new ArrayList<>();

    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SearchView searchView;
    SearchView.OnQueryTextListener queryTextListener;

    boolean isSorted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rvMessages.setLayoutManager(manager);
        rvMessages.addItemDecoration(new VerticalSpaceItemDecoration(5));
        if (checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(Contact.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    1);
       else
           readContact();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        readContact();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    onSearchDisplayData(newText);

                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void onSearchDisplayData(String newText) {
        newText = newText.toLowerCase();

       sortList.clear();

        if (!newText.isEmpty()) {
            isSorted = true;
            for (int i = 0; i < smsList.size(); i++) {

                final String name = smsList.get(i).getStrName().toLowerCase();
                final String number = smsList.get(i).getStrNumber().toLowerCase();

                if (name.contains(newText) || number.contains(newText)) {
                    sortList.add(smsList.get(i));
                }
            }
            ContactRecyclerAdapter adapter = new ContactRecyclerAdapter(getApplicationContext(),sortList);
            rvMessages.setAdapter(adapter);
            adapter.notifyDataSetChanged();  // data set changed
        } else {
            isSorted = false;
            ContactRecyclerAdapter adapter = new ContactRecyclerAdapter(getApplicationContext(),smsList);
            rvMessages.setAdapter(adapter);
            adapter.notifyDataSetChanged();  // data set changed
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }






    private void readContact(){
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNo="";
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                       phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                    }

                    ContactModel model = new ContactModel();
                    model.setStrName(name);
                    model.setStrNumber(phoneNo);
                    smsList.add(model);
                    pCur.close();

                }
            }
        }
        if(cur!=null){
            cur.close();
        }


//        // Create Inbox box URI
//        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null, null,null, null);
//
//
//
//        // Read the sms data and store it in the list
//        if(cursor.moveToFirst()) {
//            for(int i=0; i < cursor.getCount(); i++) {
//                Message sms = new Message();
//                sms.setStrNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
//                sms.setStrMessage(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
//                smsList.add(sms);
//
//                cursor.moveToNext();
//            }
//
//        }
//        cursor.close();

        ContactRecyclerAdapter adapter = new ContactRecyclerAdapter(getApplicationContext(),smsList);
        rvMessages.setAdapter(adapter);




    }

    public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.RecyclerViewHolder> {
        private ArrayList<ContactModel> arrContact;
        LayoutInflater inflater;
        Context context;

        public ContactRecyclerAdapter(Context context, ArrayList<ContactModel> arrContact) {
            this.arrContact = arrContact;
            this.context = context;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout =  inflater.inflate(R.layout.row_msg,parent,false);
            return new RecyclerViewHolder(layout);}

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.tvNumber.setText(arrContact.get(position).getStrNumber());
            holder.tvName.setText(arrContact.get(position).getStrName());

            // Using the library provide class to generate colours.
            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

            // Changes the colour using the name as a key
            int color = colorGenerator.getColor(position);

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .bold()
                    .endConfig()
                    .buildRound(arrContact.get(position).getStrName().substring(0,1), color);

            holder.ivImage.setImageDrawable(drawable);

        }

        @Override
        public int getItemCount() {
            return arrContact.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView tvName,tvNumber;

            public ImageView ivImage;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                tvName = itemView.findViewById(R.id.tvName);
                tvNumber = itemView.findViewById(R.id.tvDetails);
                ivImage = itemView.findViewById(R.id.ivImage);


            }

            @Override
            public void onClick(View view) {

                if(DbHelper.strCaller =="Schedule"){

                    Intent intent = new Intent(getApplicationContext(), ScheduleMsg.class);

                    intent.putExtra("Name",arrContact.get(getLayoutPosition()).getStrName());
                    intent.putExtra("Number",arrContact.get(getLayoutPosition()).getStrNumber());
                    setResult(2,intent);
                    finish();

                }else{
                    Intent intent = new Intent(getApplicationContext(), SendMessage.class);
                    intent.putExtra("Name",arrContact.get(getLayoutPosition()).getStrName());
                    intent.putExtra("Number",arrContact.get(getLayoutPosition()).getStrNumber());
                    intent.putExtra("Msg","");
                    startActivity(intent);
                }}
        }
    }

}

