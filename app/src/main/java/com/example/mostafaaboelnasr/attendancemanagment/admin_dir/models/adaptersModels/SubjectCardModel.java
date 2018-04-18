package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class SubjectCardModel {
    private String id,name;

    public SubjectCardModel() {
    }

    public SubjectCardModel(String id, String name) {
        this.id = id;
        this.name = name;
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
}
