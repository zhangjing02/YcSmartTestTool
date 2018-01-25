package senssun.ycblelib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.czjk.blelib.model.HealthSport;
import com.czjk.blelib.utils.Constant;
import java.util.ArrayList;


public class DBTools {
    private DBOpenHelper myDBOpenHelper;
    private SQLiteDatabase db;
    public static DBTools dbTools;

    public static DBTools getInstance(Context context) {
        if (dbTools == null) {
            dbTools = new DBTools(context);
            return dbTools;
        }
        return dbTools;
    }

    public DBTools(Context context) {
        myDBOpenHelper = new DBOpenHelper(context);
        db = myDBOpenHelper.getWritableDatabase();

    }

    /**
     * 查询  指定小时 2016110907  指定天 20161109  指定月  201611
     */
    public ArrayList<HealthSport> selectData(String time) {
        String sql = "select * from " + Constant.TABLE_NAME_DATA + " where " + Constant.STEP_FIELD_DATE + " LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{time + "%"});
        ArrayList<HealthSport> healthSports = new ArrayList<HealthSport>();
        while (cursor.moveToNext()) {
            HealthSport healthSport = new HealthSport(cursor.getString(cursor.getColumnIndex(Constant.STEP_FIELD_DATE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.STEP_FIELD_STEP)),
                    cursor.getInt(cursor.getColumnIndex(Constant.STEP_FIELD_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.HEARTRATE_FIELD_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.RESTING_HEARTRATE_FIELD_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.BLOOD_FIELD_SYSTOLIC_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.BLOOD_FIELD_DIASTOLIC_VALUE)));
            healthSports.add(healthSport);
        }
        cursor.close();
        return healthSports;
    }

    public HealthSport selectLastData() {
        Cursor cursor = db.query(Constant.TABLE_NAME_DATA, null, null, null, null, null, null);
        HealthSport healthSport = null;
        if (cursor.moveToLast()) {
            healthSport = new HealthSport(cursor.getString(cursor.getColumnIndex(Constant.STEP_FIELD_DATE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.STEP_FIELD_STEP)),
                    cursor.getInt(cursor.getColumnIndex(Constant.STEP_FIELD_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.HEARTRATE_FIELD_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.RESTING_HEARTRATE_FIELD_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.BLOOD_FIELD_SYSTOLIC_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.BLOOD_FIELD_DIASTOLIC_VALUE)));
        }
        cursor.close();
        return healthSport;
    }

    public  ArrayList<HealthSport> selectAllData() {
        Cursor cursor = db.query(Constant.TABLE_NAME_DATA, null, null, null, null, null, null);
        ArrayList<HealthSport> healthSports = new ArrayList<>();
        while (cursor.moveToNext()) {
            HealthSport healthSport = new HealthSport(cursor.getString(cursor.getColumnIndex(Constant.STEP_FIELD_DATE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.STEP_FIELD_STEP)),
                    cursor.getInt(cursor.getColumnIndex(Constant.STEP_FIELD_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.HEARTRATE_FIELD_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.RESTING_HEARTRATE_FIELD_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.BLOOD_FIELD_SYSTOLIC_VALUE)),
                    cursor.getInt(cursor.getColumnIndex(Constant.BLOOD_FIELD_DIASTOLIC_VALUE)));
            healthSports.add(healthSport);
        }
        cursor.close();
        return healthSports;
    }

    public boolean saveData(HealthSport healthSport) {
        String where = Constant.STEP_FIELD_DATE + " = ?";
        String[] whereValue = {healthSport.getDate()};
        Cursor cursor = db.query(Constant.TABLE_NAME_DATA, new String[]{Constant.STEP_FIELD_DATE}, where, whereValue, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(Constant.STEP_FIELD_DATE, healthSport.getDate());
        cv.put(Constant.STEP_FIELD_STEP, healthSport.getStep());
        cv.put(Constant.STEP_FIELD_TYPE, healthSport.getType());
        cv.put(Constant.HEARTRATE_FIELD_VALUE, healthSport.getHeartValue());
        cv.put(Constant.RESTING_HEARTRATE_FIELD_VALUE, healthSport.getRestingHeartValue());
        cv.put(Constant.BLOOD_FIELD_SYSTOLIC_VALUE, healthSport.getSystolicValue());
        cv.put(Constant.BLOOD_FIELD_DIASTOLIC_VALUE, healthSport.getDiatolicValue());
        long result = 0;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                result = db.update(Constant.TABLE_NAME_DATA, cv, where, whereValue);
            } else {
                result = db.insert(Constant.TABLE_NAME_DATA, null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return result > 0;
    }

    public void deleteData(String date) {
        String where = Constant.STEP_FIELD_DATE + " = ?";
        String[] whereValue = {date};
        db.delete(Constant.TABLE_NAME_DATA, where, whereValue);
    }

    public void deleteAllData() {
        db.delete(Constant.TABLE_NAME_DATA, null, null);
    }

    public void droptable(String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void close() {
        db.close();
    }



}
