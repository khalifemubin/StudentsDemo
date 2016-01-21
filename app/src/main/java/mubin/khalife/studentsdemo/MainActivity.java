package mubin.khalife.studentsdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    Cursor c_r;
    int editDeleteID = 0;
    DBHelper db = new DBHelper(this);
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fetch records from the database
        Cursor cr = db.getAllStudents();
        displayRecords(db,cr,null);

        /*
        final ListView studentList = (ListView) findViewById(R.id.listStudents);
        final ArrayList<String> templist = new ArrayList<String>();

        //Fetch records from the database
        DBHelper db = new DBHelper(this);
        Cursor cr = db.getAllStudents();

        if (cr != null) {
            while (cr.moveToNext()) {

            }

            cr.close();
        }
        else
            studentList.setEmptyView(findViewById(R.id.txtEmpty));
        */
    }

    public void displayRecords( final DBHelper db,Cursor cr,HashMap<String, String> searchValues)
    {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        c_r = cr;

        TextView txtEmpty = (TextView) findViewById(R.id.txtEmpty);
        LinearLayout vwWhenPopulated = (LinearLayout) findViewById(R.id.vwWhenPopulated);

        if (!(cr.moveToFirst()) || cr.getCount() ==0){
            expandableListAdapter = null;
            expandableListView.setAdapter(expandableListAdapter);

            txtEmpty.setVisibility(View.VISIBLE);
            expandableListView.setEmptyView(txtEmpty);

            if(db.totalStudents() > 0)
                vwWhenPopulated.setVisibility(View.VISIBLE);
            else
                vwWhenPopulated.setVisibility(View.INVISIBLE);

        }
        else {
            try {
                txtEmpty.setVisibility(View.INVISIBLE);

                //vwWhenPopulated.setVisibility(View.VISIBLE);

                expandableListDetail = ExpandableListDataPump.getData(this,c_r,searchValues);
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);

                expandableListAdapter.notifyDataSetChanged();

                //For ContextMenu
                //registerForContextMenu(expandableListView);
                //For ContextMenu

                expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(MainActivity.this,"Position = "+position+" and id = "+id,Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MainActivity.this,c_r.getString(1),Toast.LENGTH_SHORT).show();

                        /*for (c_r.moveToLast(); !c_r.isBeforeFirst(); c_r.moveToPrevious())
                        {
                            if (c_r.getPosition() == position)
                            {
                                int cursorIndex = c_r.getColumnIndex(db.COLUMN_LAST_NAME);
                                strTempId = c_r.getString(cursorIndex);
                                System.out.println(strTempId);
                            }
                        }*/


                        for (c_r.moveToFirst(); !(c_r.isAfterLast()); c_r.moveToNext()) {
                            if (c_r.getPosition() == position) {
                                int cursorIndex = c_r.getColumnIndex(db.COLUMN_STUDENT_ID);
                                editDeleteID  = c_r.getInt(cursorIndex);
                                break;
                            }
                        }


                        /*
                        String strTempId;
                        int iTemp = c_r.getCount();


                        if (c_r.moveToFirst()){
                            do{
                                //System.out.println(iTemp + "==" + position);
                                if (iTemp == (position+1)) {
                                    int cursorIndex = c_r.getColumnIndex(db.COLUMN_LAST_NAME);
                                    strTempId = c_r.getString(cursorIndex);
                                    //Toast.makeText(MainActivity.this, strTempId, Toast.LENGTH_SHORT).show();
                                    break;
                                }

                               iTemp--;

                            }while (c_r.moveToNext());
                        }*/

                        registerForContextMenu(parent);
                        openContextMenu(parent);
                        return true;
                    }
                });

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/
                    }
                });

                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();*/

                    }
                });

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
               /* Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                )
                        .show();*/
                        return false;
                    }
                });

            }catch (Exception ex)
            {
                Log.d("Mumbai",ex.toString());
            }
        }
    }

    public void searchRecords(View v)
    {
        EditText txtFname = (EditText) findViewById(R.id.txtSeachFirstName);
        String fname = txtFname.getText().toString();
        fname = fname.trim();

        EditText txtLname = (EditText) findViewById(R.id.txtSearchLastName);
        String lname = txtLname.getText().toString();
        lname = lname.trim();

        EditText txtRollNo = (EditText) findViewById(R.id.txtSearchRollNumber);
        String roll_num = txtRollNo.getText().toString();
        roll_num = roll_num.trim();

        HashMap<String,String> queryValues = new HashMap<String, String>();
        queryValues.put("fname",fname);
        queryValues.put("lname",lname);
        queryValues.put("roll_num", roll_num);

        Cursor cr = db.getStudentInfo(fname,lname,roll_num);

        displayRecords(db,cr,queryValues);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void goToAddStudent(View v)
    {
        Runnable mlaunchtask = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, AddStudent.class));
                pd.dismiss();
            }
        };

        pd = ProgressDialog.show(this, "Loading" /*getString(R.string.app_name)*/, "Please wait...", true, true);

        Handler h = new Handler();
        h.postDelayed(mlaunchtask, 3000);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Action");
        menu.add("Edit");
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Toast.makeText(this,"Selected position is "+iSelected,Toast.LENGTH_SHORT).show();

        if(item.getTitle().equals("Edit"))
        {
            //Toast.makeText(this,"Edit Record of id = "+editDeleteID,Toast.LENGTH_SHORT).show();
            Runnable mlaunchtask = new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, AddStudent.class);
                    intent.putExtra("editID", editDeleteID);
                    startActivity(intent);
                    pd.dismiss();
                }
            };

            pd = ProgressDialog.show(this, "Loading" /*getString(R.string.app_name)*/, "Please wait...", true, true);

            Handler h = new Handler();
            h.postDelayed(mlaunchtask, 2000);
        }
        else if (item.getTitle().equals("Delete"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Confirm Delete");
            ad.setMessage("Are you sure you want to delete this student?");
            //ad.setIcon(android.R.drawable.ic_dialog_alert);
            ad.setIcon(R.drawable.ic_dialog_alert);
            ad.setPositiveButton(R.string.txt_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //startActivity(new Intent(MainActivity.this, AddReminder.class));
                    //delete the record
                    boolean deleteResult = db.deleteStudent(editDeleteID);

                    if(deleteResult == true)
                        Toast.makeText(MainActivity.this,"Record deleted successfully!",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this,"Error Occured! Please try again later",Toast.LENGTH_SHORT).show();

                    //startActivity(new Intent(MainActivity.this, MainActivity.class));
                    Cursor cr = db.getAllStudents();
                    displayRecords(db,cr,null);

                }
            });

            ad.setNegativeButton(android.R.string.cancel, null);

            ad.show();
        }

        return super.onContextItemSelected(item);
    }
}
