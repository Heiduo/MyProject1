package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class ProjectInfo extends BmobObject implements Serializable {

    /**
     * projectId : p213
     * projectName : 为美好的世界
     * projectTeacher : 王二
     * projectRequire :
     */

    private String projectId;
    private String projectName;
    private String projectTeacher;
    private String projectRequire;
    private String projectType;

    private String projectEnglish;
    private String designRequire;
    private String workImportance;
    private String timeArrange;
    private String referenceDetail;

    public StudentInfo student;
    public TeacherInfo teacher;

    public String getWorkImportance() {
        return workImportance;
    }

    public void setWorkImportance(String workImportance) {
        this.workImportance = workImportance;
    }

    public String getTimeArrange() {
        return timeArrange;
    }

    public void setTimeArrange(String timeArrange) {
        this.timeArrange = timeArrange;
    }

    public String getReferenceDetail() {
        return referenceDetail;
    }

    public void setReferenceDetail(String referenceDetail) {
        this.referenceDetail = referenceDetail;
    }

    public String getDesignRequire() {
        return designRequire;
    }

    public void setDesignRequire(String designRequire) {
        this.designRequire = designRequire;
    }

    public String getProjectEnglish() {
        return projectEnglish;
    }

    public void setProjectEnglish(String projectEnglish) {
        this.projectEnglish = projectEnglish;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }


    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    public TeacherInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherInfo teacher) {
        this.teacher = teacher;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectTeacher() {
        return projectTeacher;
    }

    public void setProjectTeacher(String projectTeacher) {
        this.projectTeacher = projectTeacher;
    }

    public String getProjectRequire() {
        return projectRequire;
    }

    public void setProjectRequire(String projectRequire) {
        this.projectRequire = projectRequire;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
                "projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectTeacher='" + projectTeacher + '\'' +
                ", projectRequire='" + projectRequire + '\'' +
                ", projectType='" + projectType + '\'' +
                ", projectEnglish='" + projectEnglish + '\'' +
                ", designRequire='" + designRequire + '\'' +
                ", workImportance='" + workImportance + '\'' +
                ", timeArrange='" + timeArrange + '\'' +
                ", referenceDetail='" + referenceDetail + '\'' +
                ", student=" + student +
                ", teacher=" + teacher +
                '}';
    }
}
