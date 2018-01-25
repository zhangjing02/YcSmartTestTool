package senssun.ycblelib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.czjk.blelib.utils.BleLog;
import com.czjk.blelib.utils.Constant;


public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "czjk";
    private static final int DB_VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DATA);
        BleLog.i("创建数据库");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DB_NAME);
    }

    // 数据表
    private static final String CREATE_TABLE_DATA = "CREATE TABLE "
            + Constant.TABLE_NAME_DATA + " ("
            + Constant.STEP_FIELD_DATE + " TEXT primary key, "
            + Constant.STEP_FIELD_TYPE + " INTEGER,"
            + Constant.STEP_FIELD_STEP + " INTEGER,"
            + Constant.HEARTRATE_FIELD_VALUE + " INTEGER,"
            + Constant.RESTING_HEARTRATE_FIELD_VALUE + " INTEGER,"
            + Constant.BLOOD_FIELD_SYSTOLIC_VALUE + " INTEGER,"
            + Constant.BLOOD_FIELD_DIASTOLIC_VALUE + " INTEGER);";
}
