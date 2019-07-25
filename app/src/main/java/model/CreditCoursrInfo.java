package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class CreditCoursrInfo extends BmobObject implements Serializable {
    private int courseCredit;
    private String courseName;
    private StudentInfo student;

    public int getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(int courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "CreditCoursrInfo{" +
                "courseCredit=" + courseCredit +
                ", courseName='" + courseName + '\'' +
                ", student=" + student +
                '}';
    }
}
