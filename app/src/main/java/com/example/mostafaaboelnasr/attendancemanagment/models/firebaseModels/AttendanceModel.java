package com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels;

/**
 * Created by Mostafa Aboelnasr on 4/12/2018.
 */

public class AttendanceModel {
    private String id, studentID, sessionID;

    public AttendanceModel() {
    }

    public AttendanceModel(String id, String studentID, String sessionID) {
        this.id = id;
        this.studentID = studentID;
        this.sessionID = sessionID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
