package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class StudentSelectInfo extends BmobObject implements Serializable {
    public String studentId;
    public String teacherId;
    public String projectId;
    public int score;

    public ProjectInfo project;
    public StudentInfo student;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "StudentSelectInfo{" +
                "studentId=" + studentId +
                ", teacherId=" + teacherId +
                ", projectId='" + projectId + '\'' +
                ", score=" + score +
                ", project=" + project +
                ", student=" + student +
                '}';
    }
}
