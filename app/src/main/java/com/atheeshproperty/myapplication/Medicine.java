package com.atheeshproperty.myapplication;

public class Medicine {

    private int medID;
    private int userID;
    private String medName;
    private String medType;
    private String medFreq;
    private String medDose;
    private String medTime;
    private String medStartdate;
    private String medEnddate;
    private String medDuration;
    private String medDescrip;

    public Medicine() {

    }

    public Medicine(Integer medid, Integer userid, String medname, String medtype,
                    String medfreq, String meddose, String medtime,
                    String medduration,String medstartdate, String medenddate, String meddescrip) {

        medID = medid;
        userID = userid;
        medName = medname;
        medType = medtype;
        medFreq = medfreq;
        medDose = meddose;
        medTime = medtime;
        medDuration = medduration;
        medStartdate = medstartdate;
        medEnddate = medenddate;
        medDescrip = meddescrip;

    }

    public Integer getMedID() {
        return medID;
    }

    public void setMedID(Integer medID) {
        this.medID = medID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
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

    public String getMedDose() {
        return medDose;
    }

    public void setMedDose(String medDose) {
        this.medDose = medDose;
    }

    public String getMedTime() {
        return medTime;
    }

    public void setMedTime(String medTime) {
        this.medTime = medTime;
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
