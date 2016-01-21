package mubin.khalife.studentsdemo;

import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by developer on 10/7/2015.
 */

public class ExpandableListDataPump {
    private static Context context;

    public static HashMap<String, List<String>> getData(MainActivity obj,Cursor cr,HashMap<String, String> searchValues) {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        List<String> detailedInfo;

        DBHelper db = new DBHelper(obj);

        /*
        Cursor cr;

        if(searchValues == null) {
            cr = db.getAllStudents();
        }
        else {
            String strFname = searchValues.get("fname");
            String strLname = searchValues.get("lname");
            String strRollNo = searchValues.get("roll_num");
            cr = db.getStudentInfo(strFname,strLname,strRollNo);
        }

        */

        //if (!(cr.moveToFirst()) || cr.getCount() ==0){
        if ( cr.getCount() ==0){
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String strDOB="";
            String strDoJ="";
            Date dob = null;
            Date doj = null;

            String[] dateSuffixes =
                    //    0     1     2     3     4     5     6     7     8     9
                    { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                            //    10    11    12    13    14    15    16    17    18    19
                            "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                            //    20    21    22    23    24    25    26    27    28    29
                            "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                            //    30    31
                            "th", "st" };

            String strTempDate = "";
            int iTempDate=0;

            //while (cr.moveToNext()) {
            for (cr.moveToFirst(); !(cr.isAfterLast()); cr.moveToNext()) {
                detailedInfo = new ArrayList<String>();
                detailedInfo.add("Roll No. " + cr.getString(cr.getColumnIndex(db.COLUMN_ROLL_NUMBER)));

                try {
                    dob = sdf.parse(cr.getString(cr.getColumnIndex(db.COLUMN_DOB)));
                    doj = sdf.parse(cr.getString(cr.getColumnIndex(db.COLUMN_JOINING_DATE)));

                    SimpleDateFormat printDate = new SimpleDateFormat("d MMMM yyyy");

                    strDOB = printDate.format(dob);
                    String strSplitDob[] = strDOB.split(" ");
                    strTempDate = strSplitDob[0];
                    iTempDate = Integer.parseInt(strSplitDob[0]);
                    strTempDate = iTempDate + dateSuffixes[iTempDate];
                    strDOB = strTempDate+" "+strSplitDob[1]+" "+strSplitDob[2];

                    strDoJ = printDate.format(doj);
                    String strSplitDoJ[] = strDoJ.split(" ");
                    strTempDate = strSplitDoJ[0];
                    iTempDate = Integer.parseInt(strSplitDoJ[0]);
                    strTempDate = iTempDate + dateSuffixes[iTempDate];
                    strDoJ = strTempDate+" "+strSplitDoJ[1]+" "+strSplitDoJ[2];

                    detailedInfo.add("Date of Birth: "+strDOB);
                    detailedInfo.add("Date of Joining: "+strDoJ);

                    expandableListDetail.put(cr.getString(cr.getColumnIndex(db.COLUMN_FIRST_NAME)) + " " +
                            cr.getString(cr.getColumnIndex(db.COLUMN_LAST_NAME)), detailedInfo);

                } catch (ParseException e) {
                    strDOB = "";
                    strDoJ = "";
                    System.out.println(e.toString());
                }


            }
            //cr.close();
        }

        /*
        List<String> technology = new ArrayList<String>();
        technology.add("Beats sued for noise-cancelling tech");
        technology.add("Wikipedia blocks US Congress edits");
        technology.add("Google quizzed over deleted links");
        technology.add("Nasa seeks aid with Earth-Mars links");
        technology.add("The Good, the Bad and the Ugly");

        List<String> entertainment = new ArrayList<String>();
        entertainment.add("Goldfinch novel set for big screen");
        entertainment.add("Anderson stellar in Streetcar");
        entertainment.add("Ronstadt receives White House honour");
        entertainment.add("Toronto to open with The Judge");
        entertainment.add("British dancer return from Russia");

        List<String> science = new ArrayList<String>();
        science.add("Eggshell may act like sunblock");
        science.add("Brain hub predicts negative events");
        science.add("California hit by raging wildfires");
        science.add("Rosetta's comet seen in close-up");
        science.add("Secret of sandstone shapes revealed");

        expandableListDetail.put("TECHNOLOGY NEWS", technology);
        expandableListDetail.put("ENTERTAINMENT NEWS", entertainment);
        expandableListDetail.put("SCIENCE & ENVIRONMENT NEWS", science);
        */
        return expandableListDetail;
    }

}