package androapps.sapna.com.smartsmsapp.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androapps.sapna.com.smartsmsapp.DbHelper;
import androapps.sapna.com.smartsmsapp.R;
import androapps.sapna.com.smartsmsapp.Receiver.AlarmReceiver;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleMsg extends AppCompatActivity {

    @BindView(R.id.tvTo)
    TextView tvTo;
    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.etMsg)
    EditText etMsg;

    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    int mYear, mMonth, mDay;

    String strName,strNumber;

    int setYear,setMonth,setDay, sethr,setmin;
    long strLongtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_msg);
    ButterKnife.bind(this);
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH) + 1;
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        setYear = mYear;
        setMonth = mMonth-1;
        setDay= mDay;

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schedule SMS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(getApplicationContext(),Contact.class);
                DbHelper.strCaller ="Schedule";
                startActivityForResult(intent,1);
            }
        });

        SimpleDateFormat dfTime = new SimpleDateFormat("hh:mm a");
        tvTime.setText(dfTime.format(cal.getTime()));

       // SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        tvDate.setText(mDay+"/"+mMonth+"/"+mYear);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleMsg.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                               setYear = year;
                               setMonth = monthOfYear;
                               setDay = dayOfMonth;
                                populateSetToDate(year, monthOfYear + 1, dayOfMonth);

                            }
                        }, mYear, mMonth - 1, mDay);
                datePickerDialog.show();
                //   datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()-1000);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

            }
        });

 tvTime.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         Calendar mcurrentTime = Calendar.getInstance();
         int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
         int minute = mcurrentTime.get(Calendar.MINUTE);
         TimePickerDialog mTimePicker;

         mTimePicker = new TimePickerDialog(ScheduleMsg.this, new TimePickerDialog.OnTimeSetListener() {
             @Override
             public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                 String am_pm = "";

                 sethr = selectedHour;
                 setmin = selectedMinute;
                 Calendar datetime = Calendar.getInstance();
                 datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                 datetime.set(Calendar.MINUTE, selectedMinute);

                 if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                     am_pm = "AM";
                 else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                     am_pm = "PM";

                 String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

                tvTime.setText( strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm );


             }
         }, hour, minute, false);//Yes 24 hour time
         mTimePicker.setTitle("Select Time");
         mTimePicker.show();
     }
 });



 btnSave.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         saveData();
         scheduleSms();
     }
 });
    }

    private void scheduleSms(){

        Intent intent = new Intent(ScheduleMsg.this, AlarmReceiver.class);
        Bundle b = new Bundle();
        b.putString("Number",strNumber);
        b.putString("Msg",etMsg.getText().toString());
        intent.putExtra("Bundle",b);
        Calendar cal = Calendar.getInstance();

        cal.set(setYear,setMonth,setDay,sethr,setmin);

        long strTime = cal.getTimeInMillis();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ScheduleMsg.this,0,intent,0);
        AlarmManager alarmManager =  (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,strTime,pendingIntent);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void saveData(){
        DbHelper helper = new DbHelper(getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("RNUMBER", strNumber);
        values.put("NAME",strName);
        values.put("MSG",etMsg.getText().toString());
        values.put("SDATE",tvDate.getText().toString());
        values.put("STIME",tvTime.getText().toString());
        values.put("STATUS","FALSE");


        database.insert("SCHEDULEDMSG",null,values);
        database.close();
        Toast.makeText(getApplicationContext(),"Message scheduled.", Toast.LENGTH_SHORT).show();
        finish();


    }

    public void populateSetToDate(int year, int month, int day) {
        tvDate.setText(day + "/" + month + "/" + year);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           if(requestCode== 1 && resultCode == 2){

               tvTo.setText(data.getStringExtra("Name"));
               strName = data.getStringExtra("Name");
               strNumber = data.getStringExtra("Number");
           }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
