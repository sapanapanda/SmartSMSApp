package androapps.sapna.com.smartsmsapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;

import androapps.sapna.com.smartsmsapp.DbHelper;


/**
 * Created by Hp on 10/18/2018
 */

public class AlarmReceiver extends BroadcastReceiver {
    Context context;
    String strNumber="";
    String strMsg ="";
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle b = intent.getBundleExtra("Bundle");
        strNumber = b.getString("Number");
        strMsg = b.getString("Msg");
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(strNumber, null, strMsg, null, null);
           updateData(strNumber,strMsg);

    }

    private void updateData(String strNumber, String strMsg){
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("STATUS","TRUE");
        String where = "RNUMBER =? AND MSG = ?";
        String[] whereArgs = new String[] {strNumber,strMsg};

        database.update("SCHEDULEDMSG",values,where,whereArgs);
        database.close();


    }

}
