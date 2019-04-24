package androapps.sapna.com.smartsmsapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androapps.sapna.com.smartsmsapp.DbHelper;
import androapps.sapna.com.smartsmsapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SendMessage extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_input)
    EditText etInput;

    @BindView(R.id.btn_send)
    Button btnSend;
    String strNumber,strName,strMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        strName = getIntent().getStringExtra("Name");
        strNumber = getIntent().getStringExtra("Number");
        strMsg = getIntent().getStringExtra("Msg");
        getSupportActionBar().setTitle(strName);

        etInput.setText(strMsg);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkCallingOrSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(SendMessage.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            2);
                else
                 sendSMS(strNumber, etInput.getText().toString());
            }
        });
    }

//    private void sendSMS(String strNumber,String strMsg)
//    {
////Getting intent and PendingIntent instance
//        Intent intent=new Intent(getApplicationContext(),SendMessage.class);
//        PendingIntent pi= PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
//
////Get the SmsManager instance and call the sendTextMessage method to send message
//        SmsManager sms=SmsManager.getDefault();
//        sms.sendTextMessage(strNumber, null, strMsg, pi,null);
//    }


    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
//        Intent intent = registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getBaseContext(), "SMS not delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.draftmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_draft:
                saveToDraft();

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDraft(){
        DbHelper helper = new DbHelper(getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RNUMBER", strNumber);
        values.put("NAME",strName);
        values.put("MSG",etInput.getText().toString());

        database.insert("DRAFTMSG",null,values);
           database.close();
           Toast.makeText(getApplicationContext(),"Message saved as Draft", Toast.LENGTH_SHORT).show();
           finish();


    }
}
