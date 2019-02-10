package com.atheeshproperty.myapplication;

public class MedicineHistory {

    private int medID;
    private int userID;
    private String Tdate;
    private String medName;
    private String medDose;
    private String medType;
    private String medFreq;
    private String medTime;
    private String medmorTaken;
    private String meddayTaken;
    private String mednightTaken;
    private String medStartdate;
    private String medEnddate;
    private String medDuration;
    private String medDescrip;


    public MedicineHistory() {

    }

    public MedicineHistory(Integer medid, Integer userid, String todaydate, String medname, String meddose, String medtype,
                           String medfreq, String medtime, String medmortaken, String meddaytaken, String mednighttaken,
                           String medstartdate, String medenddate, String medduration, String meddescrip) {

        medID = medid;
        userID = userid;
        Tdate = todaydate;
        medName = medname;
        medDose = meddose;
        medType = medtype;
        medFreq = medfreq;
        medTime = medtime;
        medmorTaken = medmortaken;
        meddayTaken = meddaytaken;
        mednightTaken = mednighttaken;
        medStartdate = medstartdate;
        medEnddate = medenddate;
        medDuration = medduration;
        medDescrip = meddescrip;

    }


    public int getMedID() {
        return medID;
    }

    public void setMedID(int medID) {
        this.medID = medID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTdate() {
        return Tdate;
    }

    public void setTdate(String tdate) {
        Tdate = tdate;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedDose() {
        return medDose;
    }

    public void setMedDose(String medDose) {
        this.medDose = medDose;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getMedFreq() {
        return medFreq;
    }

    public void setMedFreq(String medFreq) {
        this.medFreq = medFreq;
    }

    public String getMedTime() {
        return medTime;
    }

    public void setMedTime(String medTime) {
        this.medTime = medTime;
    }


    public String getMedmorTaken() {
        return medmorTaken;
    }

    public void setMedmorTaken(String medmorTaken) {
        this.medmorTaken = medmorTaken;
    }

    public String getMeddayTaken() {
        return meddayTaken;
    }

    public void setMeddayTaken(String meddayTaken) {
        this.meddayTaken = meddayTaken;
    }

    public String getMednightTaken() {
        return mednightTaken;
    }

    public void setMednightTaken(String mednightTaken) {
        this.mednightTaken = mednightTaken;
    }


    public String getMedStartdate() {
        return medStartdate;
    }

    public void setMedStartdate(String medStartdate) {
        this.medStartdate = medStartdate;
    }

    public String getMedEnddate() {
        return medEnddate;
    }

    public void setMedEnddate(String medEnddate) {
        this.medEnddate = medEnddate;
    }

    public String getMedDuration() {
        return medDuration;
    }

    public void setMedDuration(String medDuration) {
        this.medDuration = medDuration;
    }

    public String getMedDescrip() {
        return medDescrip;
    }

    public void setMedDescrip(String medDescrip) {
        this.medDescrip = medDescrip;
    }

}
