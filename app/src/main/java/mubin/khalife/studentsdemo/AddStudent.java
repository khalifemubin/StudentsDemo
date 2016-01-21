package mubin.khalife.studentsdemo;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class AddStudent extends ActionBarActivity {

    int editID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Intent intent = getIntent();


        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("editID")) {
                setTitle("Update Student");
                editID = extras.getInt("editID", 0);

                //fetch data from database and populate the fields
                DBHelper db = new DBHelper(this);
                Cursor cr = db.editStudentInfo(editID);

               // Toast.makeText(this,"count = "+cr.getCount(),Toast.LENGTH_SHORT).show();


                int dbEditId = 0;

                if(cr.moveToFirst()) {
                    int cursorIndex = cr.getColumnIndex(db.COLUMN_STUDENT_ID);
                    dbEditId = cr.getInt(cursorIndex);
                    if (dbEditId == editID) {
                        //Populate the fields
                        EditText txtFname = (EditText) findViewById(R.id.txtAddFirstName);
                        cursorIndex = cr.getColumnIndex(db.COLUMN_FIRST_NAME);
                        txtFname.setText(cr.getString(cursorIndex));

                        EditText txtLname = (EditText) findViewById(R.id.txtAddLastName);
                        cursorIndex = cr.getColumnIndex(db.COLUMN_LAST_NAME);
                        txtLname.setText(cr.getString(cursorIndex));

                        DatePicker dobPicker = (DatePicker) findViewById(R.id.dobPicker);
                        cursorIndex = cr.getColumnIndex(db.COLUMN_DOB);

                        String strDate = cr.getString(cursorIndex);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d;

                        try {
                            d = dateFormat.parse(strDate);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH);
                            int day = cal.get(Calendar.DAY_OF_MONTH);

                            dobPicker.updateDate(year, month, day);

                        } catch (ParseException e) {
                            System.out.println(e.getMessage());
                        }


                        EditText txtRollNum = (EditText) findViewById(R.id.txtAddRollNumber);
                        cursorIndex = cr.getColumnIndex(db.COLUMN_ROLL_NUMBER);
                        
                        txtRollNum.setText(cr.getString(cursorIndex));
                        txtRollNum.setEnabled(false);

                    }
                }


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void btnAction(View v) {
        if (v.getId() == R.id.btnCancel)
            startActivity(new Intent(this, MainActivity.class));
        else if (v.getId() == R.id.btnSave) {
            DatePicker dobPicker = (DatePicker) findViewById(R.id.dobPicker);
            int day = dobPicker.getDayOfMonth();
            int month = dobPicker.getMonth() + 1;
            int year = dobPicker.getYear();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d;
            String strDOB;
            try {
                d = sdf.parse(year + "-" + month + "-" + day);
                strDOB = new SimpleDateFormat("yyyy-MM-dd").format(d);
            } catch (ParseException e) {
                strDOB = "";
                //strDOB = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                //Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, ex);
            }


            //Validation begins
            EditText txtFname = (EditText) findViewById(R.id.txtAddFirstName);
            String fname = txtFname.getText().toString();
            fname = fname.trim();

            EditText txtLname = (EditText) findViewById(R.id.txtAddLastName);
            String lname = txtLname.getText().toString();
            lname = lname.trim();

            EditText txtRollNum = (EditText) findViewById(R.id.txtAddRollNumber);
            String roll_num = txtRollNum.getText().toString();
            roll_num = roll_num.trim();

            if (fname.equals("")) {
                txtFname.setError("Please enter the First Name");
            } else if (lname.equals("")) {
                txtLname.setError("Please enter the Last Name");
            } else if (roll_num.equals("")) {
                txtRollNum.setError("Please enter the Roll Number");
            } else {
                //Now save
                HashMap<String, String> queryValues = new HashMap<String, String>();
                queryValues.put("first_name", fname);
                queryValues.put("last_name", lname);
                queryValues.put("roll_num", roll_num);
                queryValues.put("dob", strDOB);


                DBHelper db = new DBHelper(this);
                String resultAddEntry = "";

                if(editID == 0)
                    resultAddEntry = db.addStudentInfo(queryValues);
                else
                    resultAddEntry = db.updateStudentInfo(queryValues,editID);

                Toast.makeText(this, resultAddEntry, Toast.LENGTH_LONG).show();

                startActivity(new Intent(this, MainActivity.class));
                this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }


        }
    }
}
