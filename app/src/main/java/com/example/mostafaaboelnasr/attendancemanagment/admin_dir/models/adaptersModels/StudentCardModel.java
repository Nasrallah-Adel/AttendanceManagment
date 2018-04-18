package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class StudentCardModel {
    private String id, name, level;

    public StudentCardModel() {
    }

    public StudentCardModel(String id, String name, String level) {
        this.id = id;
        this.name = name;
        this.level = level;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
