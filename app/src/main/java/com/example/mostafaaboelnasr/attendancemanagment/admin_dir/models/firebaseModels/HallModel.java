package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels;

import java.util.ArrayList;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class HallModel {
    private String id, name, description, high;
    private ArrayList<PointModel> points;

    public HallModel() {
    }

    public HallModel(String id, String name, String description, String high, ArrayList<PointModel> points) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.high = high;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public ArrayList<PointModel> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<PointModel> points) {
        this.points = points;
    }
}
