package Models;

public class ReportsModel {
    String date_time,lecture,next,previous,sap,uid,key;

    public ReportsModel(String date_time, String lecture, String next, String previous, String sap, String uid,String key) {
        this.date_time = date_time;
        this.lecture = lecture;
        this.next = next;
        this.previous = previous;
        this.sap = sap;
        this.uid = uid;
        this.key = key;
    }

    public ReportsModel(){}
    public String getDate_time() {
        return date_time;
    }

    public String getLecture() {
        return lecture;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public String getSap() {
        return sap;
    }

    public String getUid() {
        return uid;
    }

    public String getKey() {
        return key;
    }
}
