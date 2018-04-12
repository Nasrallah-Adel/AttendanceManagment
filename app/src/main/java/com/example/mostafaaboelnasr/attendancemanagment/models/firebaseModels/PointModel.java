package com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class PointModel {
    String x, y, z;

    public PointModel() {
    }

    public PointModel(String x, String y, String z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }
}
