package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.adaptersModels;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class HallCardModel {
    private String id, name, des;

    public HallCardModel() {
    }

    public HallCardModel(String id, String name, String des) {
        this.id = id;
        this.name = name;
        this.des = des;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
