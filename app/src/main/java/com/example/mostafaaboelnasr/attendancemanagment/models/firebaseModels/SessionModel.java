package com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels;

/**
 * Created by Mostafa Aboelnasr on 4/12/2018.
 */

public class SessionModel {
    private String id, tutorID, date, subID, hallID;

    public SessionModel() {
    }

    public SessionModel(String id, String tutorID, String date, String subID, String hallID) {
        this.id = id;
        this.tutorID = tutorID;
        this.date = date;
        this.subID = subID;
        this.hallID = hallID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTutorID() {
        return tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    public String getHallID() {
        return hallID;
    }

    public void setHallID(String hallID) {
        this.hallID = hallID;
    }
}
