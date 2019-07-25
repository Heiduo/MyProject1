package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class TeacherIssueInfo extends BmobObject implements Serializable {
    public String teacherId;
    public String projectId;
    public TeacherInfo teacher;
    public ProjectInfo project;

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

    public TeacherInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherInfo teacher) {
        this.teacher = teacher;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "TeacherIssueInfo{" +
                "teacherId=" + teacherId +
                ", projectId='" + projectId + '\'' +
                ", teacher=" + teacher +
                ", project=" + project +
                '}';
    }
}
