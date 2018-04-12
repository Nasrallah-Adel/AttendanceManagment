package com.example.mostafaaboelnasr.attendancemanagment.models.firebaseModels;

/**
 * Created by Mostafa Aboelnasr on 4/11/2018.
 */

public class StudentModel {
    private String id, name, phone, email, pass, age, level, specialization, section, mobDNA;

    public StudentModel() {
    }

    public StudentModel(String id, String name, String phone, String email, String pass, String age, String level, String specialization, String section, String mobDNA) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
        this.level = level;
        this.specialization = specialization;
        this.section = section;
        this.mobDNA = mobDNA;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getMobDNA() {
        return mobDNA;
    }

    public void setMobDNA(String mobDNA) {
        this.mobDNA = mobDNA;
    }
}
