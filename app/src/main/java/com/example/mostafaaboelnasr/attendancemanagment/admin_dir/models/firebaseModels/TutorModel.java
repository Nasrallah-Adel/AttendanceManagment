package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models.firebaseModels;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class TutorModel {
    private String id, name, email, phone, pass;

    public TutorModel() {
    }

    public TutorModel(String id, String name, String email, String phone, String pass) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
