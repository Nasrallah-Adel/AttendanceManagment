package com.example.mostafaaboelnasr.attendancemanagment.admin_dir.models;

/**
 * Created by Mostafa Aboelnasr on 3/29/2018.
 */

public class sendProblemModel {
    private String problem_id, user_id, user_name, date, problem_content, advise_content, done;

    public sendProblemModel() {
    }

    public sendProblemModel(String problem_id, String user_id, String user_name, String date, String problem_content, String advise_content, String done) {
        this.problem_id = problem_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.date = date;
        this.problem_content = problem_content;
        this.advise_content = advise_content;
        this.done = done;
    }

    public String getAdvise_content() {
        return advise_content;
    }

    public void setAdvise_content(String advise_content) {
        this.advise_content = advise_content;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProblem_content() {
        return problem_content;
    }

    public void setProblem_content(String problem_content) {
        this.problem_content = problem_content;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }
}