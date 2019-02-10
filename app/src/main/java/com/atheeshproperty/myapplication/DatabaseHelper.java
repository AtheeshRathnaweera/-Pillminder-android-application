package com.atheeshproperty.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pillminder.db";

    //User details table
    public static final String TABLE_NAME = "UserDetails";
    public static final String UID = "ID";
    public static final String UNAME = "NAME";
    public static final String UAGE = "AGE";
    public static final String UBLOODGROUP = "BLOOD";
    public static final String UWEIGHT = "WEIGHT";
    public static final String UHEIGHT = "HEIGHT";
    public static final String UNOTES = "NOTES";
    public static final String IMAGEURL = "URL";

    //Settings table
    public static final String SETTINGS_TABLE = "SETTINGS";
    public static final String SETTINGSID = "SETID";
    public static final String MALARMTIME = "MALARMTIME";
    public static final String DALARMTIME = "DALARMTIME";
    public static final String NALARMTIME = "NALARMTIME";
    public static final String MALARMTYPE = "MALARMTYPE";
    public static final String DALARMTYPE = "DALARMTYPE";
    public static final String NALARMTYPE = "NALARMTYPE";
    public static final String NOTIFICATIONTONE = "NTONE";
    public static final String RINGTONE = "RTONE";


    //Medicine details table
    public static final String MED_TABLE_NAME = "MedDetails";
    public static final String MEDID = "MEDID";
    public static final String MEDUSERID = "USERID";
    public static final String MEDNAME = "MED_NAME";
    public static final String MEDTYPE = "TYPE";
    public static final String MEDFREQ = "FREQUENCY";
    public static final String MEDDOSE = "DOSE";
    public static final String MEDTIME = "TIME";
    public static final String MEDSTARTDATE = "STARTING_DATE";
    public static final String MEDENDDATE = "ENDING_DATE";
    public static final String MEDDURATION = "DURATION";
    public static final String MEDDESCRIP = "DESCRIPTION";

    //Medicine history table
    public static final String HIS_TABLE_NAME = "HISTORY";
    public static final String HISMEDID = "HMEDID";
    public static final String HISUSERID = "HUSERID";
    public static final String HISDATE = "HDATE";
    public static final String HISNAME = "HNAME";
    public static final String HISDOSE = "HDOSE";
    public static final String HISTYPE = "HTYPE";
    public static final String HISFREQ = "HFREQ";
    public static final String HISTIME = "HTIME";
    public static final String HISMORTAKEN = "HMORNINGTAKEN";
    public static final String HISDAYTAKEN = "HDAYTAKEN";
    public static final String HISNIGHTTAKEN = "HNIGHTTAKEN";
    public static final String HISSTARTDATE = "STARTED_DATE";
    public static final String HISENDDATE = "ENDED_DATE";
    public static final String HISDURATION = "DURATION";
    public static final String HISDESCRIP = "DESCRIPTION";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);//whenever this constructor is called the Database will be created by this method
        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MEDDETAILS_TABLE =
                "CREATE TABLE " + MED_TABLE_NAME +
                        "(" + MEDID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + MEDUSERID + " INTEGER,"
                        + MEDNAME + " TEXT,"
                        + MEDTYPE + " TEXT,"
                        + MEDFREQ + " INTEGER,"
                        + MEDDOSE + " TEXT,"
                        + MEDTIME + " TEXT,"
                        + MEDDURATION + " TEXT,"
                        + MEDSTARTDATE + " TEXT,"
                        + MEDENDDATE + " TEXT,"
                        + MEDDESCRIP + " TEXT)";

        String CREATE_USERDETAILS_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                        "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + UNAME + " TEXT,"
                        + UAGE + " INTEGER,"
                        + UBLOODGROUP + " TEXT,"
                        + UWEIGHT + " INTEGER,"
                        + UHEIGHT + " INTEGER,"
                        + IMAGEURL + " TEXT,"
                        + UNOTES + " TEXT)";

        String CREATE_SETTINGS_TABLE =
                "CREATE TABLE " + SETTINGS_TABLE +
                        "(" + SETTINGSID + " INTEGER PRIMARY KEY,"
                        + MALARMTIME + " TEXT,"
                        + DALARMTIME + " TEXT,"
                        + NALARMTIME + " TEXT,"
                        + MALARMTYPE + " TEXT,"
                        + DALARMTYPE + " TEXT,"
                        + NALARMTYPE + " TEXT,"
                        + NOTIFICATIONTONE + " TEXT,"
                        + RINGTONE + " TEXT)";

        String CREATE_MEDHISTORY_TABLE =
                "CREATE TABLE " + HIS_TABLE_NAME +
                        "(" + HISMEDID + " INTEGER,"
                        + HISUSERID + " INTEGER,"
                        + HISDATE + " TEXT,"
                        + HISNAME + " TEXT,"
                        + HISDOSE + " TEXT,"
                        + HISTYPE + " TEXT,"
                        + HISFREQ + " INTEGER,"
                        + HISTIME + " TEXT,"
                        + HISMORTAKEN + " TEXT,"
                        + HISDAYTAKEN + " TEXT,"
                        + HISNIGHTTAKEN + " TEXT,"
                        + HISSTARTDATE + " TEXT,"
                        + HISENDDATE + " TEXT,"
                        + HISDURATION + " TEXT,"
                        + HISDESCRIP + " TEXT)";

        db.execSQL(CREATE_USERDETAILS_TABLE);
        db.execSQL(CREATE_SETTINGS_TABLE);
        db.execSQL(CREATE_MEDDETAILS_TABLE);
        db.execSQL(CREATE_MEDHISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HIS_TABLE_NAME);
        onCreate(db);

    }


    public boolean insertData(String name, String age, String blood, String weight, String height, String notes, String url) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNAME, name);
        contentValues.put(UAGE, age);
        contentValues.put(UBLOODGROUP, blood);
        contentValues.put(UWEIGHT, weight);
        contentValues.put(UHEIGHT, height);
        contentValues.put(IMAGEURL, url);
        contentValues.put(UNOTES, notes);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insertDataToSettingsTable(Integer setid, String morningtime, String daytime, String nighttime, String morningtype, String daytype, String nighttype, String notiTone, String ringone) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SETTINGSID, setid);
        contentValues.put(MALARMTIME, morningtime);
        contentValues.put(DALARMTIME, daytime);
        contentValues.put(NALARMTIME, nighttime);
        contentValues.put(MALARMTYPE, morningtype);
        contentValues.put(DALARMTYPE, daytype);
        contentValues.put(NALARMTYPE, nighttype);
        contentValues.put(NOTIFICATIONTONE, notiTone);
        contentValues.put(RINGTONE, ringone);

        long res = db.insert(SETTINGS_TABLE, null, contentValues);
        db.close();

        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertDataToMedTable(Integer userid, String medname, String medtype, Integer medfreq,
                                        String meddose, String medtime, String meddure, String medstart, String medend,
                                        String meddescript) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MEDUSERID, userid);
        contentValues.put(MEDNAME, medname);
        contentValues.put(MEDTYPE, medtype);
        contentValues.put(MEDFREQ, medfreq);
        contentValues.put(MEDDOSE, meddose);
        contentValues.put(MEDTIME, medtime);
        contentValues.put(MEDSTARTDATE, medstart);
        contentValues.put(MEDENDDATE, medend);
        contentValues.put(MEDDURATION, meddure);
        contentValues.put(MEDDESCRIP, meddescript);

        long result = db.insert(MED_TABLE_NAME, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }




    }

    public boolean insertDataToHistoryTable(Integer medid, Integer userid, String todaydate, String medname,
                                            String meddose, String medtype, Integer medfreq, String medtime, String medstart, String medend, String meddure, String meddesc, String morning, String day, String night) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HISMEDID, medid);
        contentValues.put(HISUSERID, userid);
        contentValues.put(HISDATE, todaydate);
        contentValues.put(HISNAME, medname);
        contentValues.put(HISDOSE, meddose);
        contentValues.put(HISTYPE, medtype);
        contentValues.put(HISFREQ, medfreq);
        contentValues.put(HISTIME, medtime);
        contentValues.put(HISSTARTDATE, medstart);
        contentValues.put(HISENDDATE, medend);
        contentValues.put(HISDURATION, meddure);
        contentValues.put(HISDESCRIP, meddesc);
        contentValues.put(HISMORTAKEN, morning);
        contentValues.put(HISDAYTAKEN, day);
        contentValues.put(HISNIGHTTAKEN, night);

        long result = db.insert(HIS_TABLE_NAME, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean updateHistoryMedData(String medId, Integer userid, String medname, String medtype, Integer medfreq,
                                        String meddose, String medtime, String meddure, String medstart, String medend,
                                        String meddescript) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HISUSERID, userid);
        contentValues.put(HISNAME, medname);
        contentValues.put(HISTYPE, medtype);
        contentValues.put(HISFREQ, medfreq);
        contentValues.put(HISDOSE, meddose);
        contentValues.put(HISTIME, medtime);
        contentValues.put(HISSTARTDATE, medstart);
        contentValues.put(HISENDDATE, medend);
        contentValues.put(HISDURATION, meddure);
        contentValues.put(HISDESCRIP, meddescript);


        db.update(HIS_TABLE_NAME, contentValues, "HMEDID = ?", new String[]{medId});
        db.close();
        return true;

    }

    public boolean updateDataOfSettingsTable(String setid, String morningtime, String daytime, String nighttime, String morningtype, String daytype, String nighttype, String notiTone, String ringTone) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MALARMTIME, morningtime);
        contentValues.put(DALARMTIME, daytime);
        contentValues.put(NALARMTIME, nighttime);
        contentValues.put(MALARMTYPE, morningtype);
        contentValues.put(DALARMTYPE, daytype);
        contentValues.put(NALARMTYPE, nighttype);
        contentValues.put(NOTIFICATIONTONE, notiTone);
        contentValues.put(RINGTONE, ringTone);

        int res = db.update(SETTINGS_TABLE, contentValues, "SETID = ?", new String[]{setid});

        db.close();
        if (res > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getAllSettings() {

        SQLiteDatabase db = this.getReadableDatabase();
        String settings = " SELECT * FROM " + SETTINGS_TABLE;
        return db.rawQuery(settings, null);


    }

    public Cursor getAlarmTimes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String f = " SELECT MALARMTIME,DALARMTIME,NALARMTIME FROM " + SETTINGS_TABLE;
        return db.rawQuery(f, null);
    }

    public String getMorningAlarmType() {
        SQLiteDatabase db = this.getReadableDatabase();
        String alarmType = " SELECT MALARMTYPE FROM " + SETTINGS_TABLE;
        @SuppressLint("Recycle") Cursor cop = db.rawQuery(alarmType, null);

        cop.moveToFirst();
        String morning = cop.getString(cop.getColumnIndex("MALARMTYPE"));

        cop.close();
        db.close();

        return morning;
    }

    public String getDayAlarmType() {
        SQLiteDatabase db = this.getReadableDatabase();
        String alarmType = " SELECT DALARMTYPE FROM " + SETTINGS_TABLE;
        @SuppressLint("Recycle") Cursor cop = db.rawQuery(alarmType, null);

        cop.moveToFirst();
        String day = cop.getString(cop.getColumnIndex("DALARMTYPE"));

        cop.close();
        db.close();

        return day;
    }

    public String getNightAlarmType() {
        SQLiteDatabase db = this.getReadableDatabase();
        String alarmType = " SELECT NALARMTYPE FROM " + SETTINGS_TABLE;
        @SuppressLint("Recycle") Cursor cop = db.rawQuery(alarmType, null);

        cop.moveToFirst();
        String night = cop.getString(cop.getColumnIndex("NALARMTYPE"));

        cop.close();
        db.close();

        return night;
    }

    public String getRingTone(){
        SQLiteDatabase db = this.getReadableDatabase();
        String ringTone = " SELECT RTONE FROM "+ SETTINGS_TABLE;

        Cursor cop = db.rawQuery(ringTone, null);
        cop.moveToFirst();

        String ring = cop.getString(cop.getColumnIndex("RTONE"));
        cop.close();
        db.close();

        return ring;

    }

    public String getNotificationTone(){
        SQLiteDatabase db = this.getReadableDatabase();
        String ringTone = " SELECT NTONE FROM "+ SETTINGS_TABLE;

        Cursor cop = db.rawQuery(ringTone, null);
        cop.moveToFirst();

        String notify = cop.getString(cop.getColumnIndex("NTONE"));
        cop.close();
        db.close();

        return notify;

    }

    public boolean checkExisInHistoryTable(String date, String medId) {

        boolean exist;
        SQLiteDatabase db = this.getReadableDatabase();
        String line = "SELECT * FROM " + HIS_TABLE_NAME + " WHERE HMEDID = ? AND HDATE = ?";

        Cursor cursor = db.rawQuery(line, new String[]{medId, date});

        int y = cursor.getCount();

        exist = y > 0;
        cursor.close();
        db.close();
        return exist;
    }

    public List<MedicineHistory> getMedicineDataFromHistoryTable(String date) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<MedicineHistory> medData = new ArrayList<>();
        String stext = "SELECT * FROM " + HIS_TABLE_NAME + " WHERE HDATE = ?";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(stext, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                MedicineHistory med = new MedicineHistory();
                med.setMedID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HISMEDID))));
                med.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HISUSERID))));
                med.setTdate(cursor.getString(cursor.getColumnIndex(HISDATE)));
                med.setMedName(cursor.getString(cursor.getColumnIndex(HISNAME)));
                med.setMedDose(cursor.getString(cursor.getColumnIndex(HISDOSE)));
                med.setMedType(cursor.getString(cursor.getColumnIndex(HISTYPE)));
                med.setMedFreq(cursor.getString(cursor.getColumnIndex(HISFREQ)));
                med.setMedTime(cursor.getString(cursor.getColumnIndex(HISTIME)));
                med.setMedStartdate(cursor.getString(cursor.getColumnIndex(HISSTARTDATE)));
                med.setMedEnddate(cursor.getString(cursor.getColumnIndex(HISENDDATE)));
                med.setMedDuration(cursor.getString(cursor.getColumnIndex(HISDURATION)));
                med.setMedDescrip(cursor.getString(cursor.getColumnIndex(HISDESCRIP)));
                med.setMedmorTaken(cursor.getString(cursor.getColumnIndex(HISMORTAKEN)));
                med.setMeddayTaken(cursor.getString(cursor.getColumnIndex(HISDAYTAKEN)));
                med.setMednightTaken(cursor.getString(cursor.getColumnIndex(HISNIGHTTAKEN)));

                medData.add(med);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return medData;

    }



    public Integer deleteMedicineFromHistoryTable(String medid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(HIS_TABLE_NAME, "HMEDID = ? ", new String[]{medid});

    }


    public Integer deleteMedicineFromHistoryTableInLongPress(String medid, String relevantdate) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(HIS_TABLE_NAME, "HMEDID = ? AND HDATE = ?", new String[]{medid, relevantdate});

    }

    public ArrayList<String> getDatesFromHistoryTable(String medid) {//Getting dates from history table according to the id
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array = new ArrayList<String>();

        String text = "SELECT HDATE FROM " + HIS_TABLE_NAME + " WHERE HMEDID = ?";
        Cursor cur = db.rawQuery(text, new String[]{medid});


        while (cur.moveToNext()) {
            String uname = cur.getString(cur.getColumnIndex("HISDATE"));
            array.add(uname);

        }

        cur.close();
        db.close();

        return array;



    }

    public boolean updateUserProfileData(String id, String name, String age, String blood, String weight, String height, String url, String notes) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNAME, name);
        contentValues.put(UAGE, age);
        contentValues.put(UBLOODGROUP, blood);
        contentValues.put(UWEIGHT, weight);
        contentValues.put(UHEIGHT, height);
        contentValues.put(IMAGEURL, url);
        contentValues.put(UNOTES, notes);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        db.close();

        return true;

    }

    public boolean updateMedData(String medId, Integer userid, String medname, String medtype, Integer medfreq,
                                 String meddose, String medtime, String meddure, String medstart, String medend,
                                 String meddescript) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MEDUSERID, userid);
        contentValues.put(MEDNAME, medname);
        contentValues.put(MEDTYPE, medtype);
        contentValues.put(MEDFREQ, medfreq);
        contentValues.put(MEDDOSE, meddose);
        contentValues.put(MEDTIME, medtime);
        contentValues.put(MEDSTARTDATE, medstart);
        contentValues.put(MEDENDDATE, medend);
        contentValues.put(MEDDURATION, meddure);
        contentValues.put(MEDDESCRIP, meddescript);


        db.update(MED_TABLE_NAME, contentValues, "MEDID = ?", new String[]{medId});
        db.close();
        return true;

    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String GETDATA = " SELECT * FROM " + TABLE_NAME;
        Cursor res = db.rawQuery(GETDATA, null);
        return res;

    }

    public List<Medicine> getAllMedData() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Medicine> medicinesList = new ArrayList<>();

        Cursor cursor = db.query(MED_TABLE_NAME, new String[]{
                        MEDID, MEDUSERID, MEDNAME, MEDTYPE, MEDFREQ, MEDDOSE,
                        MEDTIME, MEDDURATION, MEDSTARTDATE, MEDENDDATE, MEDDESCRIP},
                null, null, null, null, MEDSTARTDATE
                        + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Medicine med = new Medicine();
                med.setMedID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEDID))));
                med.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEDUSERID))));
                med.setMedName(cursor.getString(cursor.getColumnIndex(MEDNAME)));
                med.setMedType(cursor.getString(cursor.getColumnIndex(MEDTYPE)));
                med.setMedFreq(cursor.getString(cursor.getColumnIndex(MEDFREQ)));
                med.setMedDose(cursor.getString(cursor.getColumnIndex(MEDDOSE)));
                med.setMedTime(cursor.getString(cursor.getColumnIndex(MEDTIME)));
                med.setMedDuration(cursor.getString(cursor.getColumnIndex(MEDDURATION)));
                med.setMedStartdate(cursor.getString(cursor.getColumnIndex(MEDSTARTDATE)));
                med.setMedEnddate(cursor.getString(cursor.getColumnIndex(MEDENDDATE)));
                med.setMedDescrip(cursor.getString(cursor.getColumnIndex(MEDDESCRIP)));

                medicinesList.add(med);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return medicinesList;

    }

    public Integer deleteMedicineData(String medid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(MED_TABLE_NAME, "MEDID = ?", new String[]{medid});

    }

    public boolean checkMedicineExists(String medName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qtext = "SELECT * FROM " + MED_TABLE_NAME + " WHERE MED_NAME = ?";

        Cursor cursor = db.rawQuery(qtext, new String[]{medName});

        boolean hasObject;

        if (cursor.getCount() > 0) {
            hasObject = true;

        } else {
            hasObject = false;
        }

        cursor.close();
        db.close();
        return hasObject;

    }

   /* public List<Medicine> getMedicineDataFromDate(String date) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<Medicine> medData = new ArrayList<>();
        String stext = "SELECT * FROM " + MED_TABLE_NAME + " WHERE ? BETWEEN STARTING_DATE AND ENDING_DATE";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(stext, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                Medicine med = new Medicine();
                med.setMedID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEDID))));
                med.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEDUSERID))));
                med.setMedName(cursor.getString(cursor.getColumnIndex(MEDNAME)));
                med.setMedType(cursor.getString(cursor.getColumnIndex(MEDTYPE)));
                med.setMedFreq(cursor.getString(cursor.getColumnIndex(MEDFREQ)));
                med.setMedDose(cursor.getString(cursor.getColumnIndex(MEDDOSE)));
                med.setMedTime(cursor.getString(cursor.getColumnIndex(MEDTIME)));
                med.setMedDuration(cursor.getString(cursor.getColumnIndex(MEDDURATION)));
                med.setMedStartdate(cursor.getString(cursor.getColumnIndex(MEDSTARTDATE)));
                med.setMedEnddate(cursor.getString(cursor.getColumnIndex(MEDENDDATE)));
                med.setMedDescrip(cursor.getString(cursor.getColumnIndex(MEDDESCRIP)));

                medData.add(med);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return medData;

    }*/

    public List<String> getTakenValue(String date, String medid) {

        SQLiteDatabase db = this.getReadableDatabase();
        String qtext = "SELECT * FROM " + HIS_TABLE_NAME + " WHERE HMEDID = ? AND HDATE = ?";

        Cursor cursor = db.rawQuery(qtext, new String[]{medid, date});
        List<String> array = new ArrayList<String>();

        while (cursor.moveToNext()) {
            String morning = cursor.getString(cursor.getColumnIndex("HMORNINGTAKEN"));
            array.add(morning);
            String day = cursor.getString(cursor.getColumnIndex("HDAYTAKEN"));
            array.add(day);
            String night = cursor.getString(cursor.getColumnIndex("HNIGHTTAKEN"));
            array.add(night);
        }

        cursor.close();
        db.close();
        return array;

    }

    public boolean updateMorningTaken(String date, String medid, String morning) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HISMORTAKEN, morning);

        db.update(HIS_TABLE_NAME, contentValues, "HDATE = ? AND HMEDID = ?", new String[]{date, medid});
        db.close();
        return true;
    }

    public boolean updateDayTaken(String date, String medid, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HISDAYTAKEN, day);

        db.update(HIS_TABLE_NAME, contentValues, "HDATE = ? AND HMEDID = ?", new String[]{date, medid});
        db.close();
        return true;
    }

    public boolean updateNightTaken(String date, String medid, String night) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HISNIGHTTAKEN, night);

        db.update(HIS_TABLE_NAME, contentValues, "HDATE = ? AND HMEDID = ?", new String[]{date, medid});
        db.close();
        return true;
    }

    public List<MedicineHistory> getMorningDataForNotification(String date) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<MedicineHistory> medData = new ArrayList<>();
        String stext = "SELECT * FROM " + HIS_TABLE_NAME + " WHERE HDATE = ? AND HFREQ != 3 AND HFREQ != 6 AND HFREQ != 9 AND HMORNINGTAKEN != 1";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(stext, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                MedicineHistory med = new MedicineHistory();
                med.setMedID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HISMEDID))));
                med.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HISUSERID))));
                med.setMedName(cursor.getString(cursor.getColumnIndex(HISNAME)));
                med.setMedDose(cursor.getString(cursor.getColumnIndex(HISDOSE)));
                med.setMedType(cursor.getString(cursor.getColumnIndex(HISTYPE)));
                med.setMedTime(cursor.getString(cursor.getColumnIndex(HISTIME)));

                medData.add(med);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return medData;

    }

    public List<MedicineHistory> getDayDataForNotification(String date) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<MedicineHistory> medData = new ArrayList<>();
        String stext = "SELECT * FROM " + HIS_TABLE_NAME + " WHERE HDATE = ? AND HFREQ != 1 AND HFREQ != 6 AND HFREQ != 7 AND HDAYTAKEN != 1";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(stext, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                MedicineHistory med = new MedicineHistory();
                med.setMedID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HISMEDID))));
                med.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HISUSERID))));
                med.setMedName(cursor.getString(cursor.getColumnIndex(HISNAME)));
                med.setMedDose(cursor.getString(cursor.getColumnIndex(HISDOSE)));
                med.setMedType(cursor.getString(cursor.getColumnIndex(HISTYPE)));
                med.setMedTime(cursor.getString(cursor.getColumnIndex(HISTIME)));

                medData.add(med);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return medData;

    }

    public List<MedicineHistory> getNightDataForNotification(String date) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<MedicineHistory> medData = new ArrayList<>();
        String stext = "SELECT * FROM " + HIS_TABLE_NAME + " WHERE HDATE = ? AND HFREQ != 1 AND HFREQ != 3 AND HFREQ != 4 AND HNIGHTTAKEN != 1";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(stext, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                MedicineHistory med = new MedicineHistory();
                med.setMedID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HISMEDID))));
                med.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(HISUSERID))));
                med.setMedName(cursor.getString(cursor.getColumnIndex(HISNAME)));
                med.setMedDose(cursor.getString(cursor.getColumnIndex(HISDOSE)));
                med.setMedType(cursor.getString(cursor.getColumnIndex(HISTYPE)));
                med.setMedTime(cursor.getString(cursor.getColumnIndex(HISTIME)));

                medData.add(med);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return medData;

    }

    public String getUserName(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String text = "SELECT NAME FROM " + TABLE_NAME + " WHERE ID =?";

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(text, new String[]{uid});

        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("NAME"));

        cursor.close();
        db.close();
        return name;

    }

   public SQLiteDatabase getOne(){
       SQLiteDatabase db = this.getReadableDatabase();
       return db;

    }

}
