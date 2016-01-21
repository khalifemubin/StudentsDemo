package mubin.khalife.studentsdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by developer on 10/7/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "students.db";

    public static final String STUDENTS_TABLE_NAME = "students";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_ROLL_NUMBER = "roll_number";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_JOINING_DATE = "joining_date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_students_table = "CREATE TABLE " + STUDENTS_TABLE_NAME + " (" + COLUMN_STUDENT_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FIRST_NAME + " TEXT" +
                "," + COLUMN_LAST_NAME + " TEXT," + COLUMN_ROLL_NUMBER + " TEXT" +
                "," + COLUMN_DOB + " TEXT," + COLUMN_JOINING_DATE + " TEXT)";

        db.execSQL(create_students_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int totalStudents() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + STUDENTS_TABLE_NAME;

        Cursor crCount = db.rawQuery(selectQuery, null);
        return crCount.getCount();
    }

    public Cursor getAllStudents() {
        try {
            /*String filter = MySQLiteHelper.JOB_ID + "=" + Integer.toString(jobID);
            String orderBy =  MySQLiteHelper.LOG_TIME + " DESC";
            Cursor cursor = database.query(MySQLiteHelper.LOG_TABLE_NAME, logTableColumns,
                    filter, null, null, null, orderBy);*/

            SQLiteDatabase db = getReadableDatabase();
            //Cursor queryCursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            return db.rawQuery("SELECT * FROM " + STUDENTS_TABLE_NAME + " ORDER BY " + COLUMN_STUDENT_ID + " DESC ", null);
        } catch (Exception ex) {
            return null;
        }
    }

    public Cursor editStudentInfo(int studentID)
    {
        try
        {
            if( studentID > 0) {
                SQLiteDatabase db = getReadableDatabase();
                String selectQuery = "SELECT * FROM " + STUDENTS_TABLE_NAME + " WHERE "+COLUMN_STUDENT_ID +" = "+ studentID;
                return db.rawQuery(selectQuery, null);
            }
            else
                return null;

        }catch (Exception ex) {
            return null;
        }
    }

    public Cursor getStudentInfo(String fname, String lname, String r_no) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            String selectQuery = "SELECT * FROM " + STUDENTS_TABLE_NAME + " WHERE 1 ";//

            if (!(fname.isEmpty()) && fname != null && fname.trim() != "")
                selectQuery += "AND ifnull(LOWER(" + COLUMN_FIRST_NAME + "),'')='" + fname.toLowerCase() + "'";

            if (!(lname.isEmpty()) && lname != null && lname.trim() != "")
                selectQuery += "AND ifnull(LOWER(" + COLUMN_LAST_NAME + "),'')='" + lname.toLowerCase() + "'";

            if (!(r_no.isEmpty()) && r_no != null && r_no.trim() != "")
                selectQuery += "AND ifnull(LOWER(" + COLUMN_ROLL_NUMBER + "),'')='" + r_no.toLowerCase() + "'";

            selectQuery = selectQuery + " ORDER BY " + COLUMN_STUDENT_ID + " ASC";

            return db.rawQuery(selectQuery, null);
        } catch (Exception ex) {
            return null;
        }
    }

    public String addStudentInfo(HashMap<String, String> queryValues) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = ft.format(dNow);

            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRST_NAME, queryValues.get("first_name"));
            values.put(COLUMN_LAST_NAME, queryValues.get("last_name"));
            values.put(COLUMN_ROLL_NUMBER, queryValues.get("roll_num"));
            values.put(COLUMN_DOB, queryValues.get("dob"));
            values.put(COLUMN_JOINING_DATE, strDate);


            int student_id = (int) db.insert(STUDENTS_TABLE_NAME, null, values);


            /*final List< Entries> entries_data = new ArrayList<Entries>() ;
            entries_data.add(new Entries(entry_id, queryValues.get("entry_title"), queryValues.get("entry_desc"), strDate));
            EntryDisplayAdapter da = null;
            da.add(entries_data);*/

            return "Student info added sucessfully!";
        } catch (Exception ex) {
            return "Operation Failed. Error :" + ex.getMessage();
        }
    }

    public String updateStudentInfo(HashMap<String, String> queryValues, int studentID) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRST_NAME, queryValues.get("first_name"));
            values.put(COLUMN_LAST_NAME, queryValues.get("last_name"));
            //values.put(COLUMN_ROLL_NUMBER, queryValues.get("roll_num"));
            values.put(COLUMN_DOB, queryValues.get("dob"));


            db.update(STUDENTS_TABLE_NAME, values, COLUMN_STUDENT_ID + " = " + studentID,null);


            /*final List< Entries> entries_data = new ArrayList<Entries>() ;
            entries_data.add(new Entries(entry_id, queryValues.get("entry_title"), queryValues.get("entry_desc"), strDate));
            EntryDisplayAdapter da = null;
            da.add(entries_data);*/

            return "Student info updated sucessfully!";
        } catch (Exception ex) {
            return "Operation Failed. Error :" + ex.getMessage();
        }
    }



    public boolean deleteStudent(int studentID) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(STUDENTS_TABLE_NAME, COLUMN_STUDENT_ID + "=" + studentID, null) > 0;
    }

}
