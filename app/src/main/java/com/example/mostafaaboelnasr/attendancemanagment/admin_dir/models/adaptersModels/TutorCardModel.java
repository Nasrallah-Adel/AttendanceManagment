package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class TutorCardModel {
    String id, name, phone;

    public TutorCardModel() {
    }

    public TutorCardModel(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
