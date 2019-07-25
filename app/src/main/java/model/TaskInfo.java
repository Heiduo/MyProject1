package model;

import cn.bmob.v3.BmobObject;

import java.io.Serializable;

public class TaskInfo extends BmobObject implements Serializable {
    private int max_size;
    private int task_times;
    private StudentInfo student;

    public int getMax_size() {
        return max_size;
    }

    public void setMax_size(int max_size) {
        this.max_size = max_size;
    }

    public int getTask_times() {
        return task_times;
    }

    public void setTask_times(int task_times) {
        this.task_times = task_times;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "max_size=" + max_size +
                ", task_times=" + task_times +
                ", student=" + student +
                '}';
    }
}
