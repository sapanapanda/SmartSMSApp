package androapps.sapna.com.smartsmsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hp on 10/18/2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static String strCaller ="";
    public DbHelper(Context context) {
        super(context, "smartsms.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS DRAFTMSG(RNUMBER TEXT,NAME TEXT,MSG TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS SCHEDULEDMSG(RNUMBER TEXT,NAME TEXT,MSG TEXT,SDATE TEXT," +
                "STIME TEXT, STATUS TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
