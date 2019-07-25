package model;

import cn.bmob.v3.BmobObject;

import java.io.Serializable;

public class TaskProgress extends BmobObject implements Serializable {
    private String task_progress;
    private String teacher_opinion;
    private String signature;
    private String update_time;
    private int number;

    public String getTask_progress() {
        return task_progress;
    }

    public void setTask_progress(String task_progress) {
        this.task_progress = task_progress;
    }

    public String getTeacher_opinion() {
        return teacher_opinion;
    }

    public void setTeacher_opinion(String teacher_opinion) {
        this.teacher_opinion = teacher_opinion;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "TaskProgress{" +
                "task_progress='" + task_progress + '\'' +
                ", teacher_opinion='" + teacher_opinion + '\'' +
                ", signature='" + signature + '\'' +
                ", update_time='" + update_time + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
