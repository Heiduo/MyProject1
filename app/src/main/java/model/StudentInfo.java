package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class StudentInfo extends BmobObject implements Serializable {


    /**
     * studentId : 201592284
     * studengName : 李四
     * sex : 男
     * s_class : 软1505
     * major : 网络工程
     * telephone : 18175365412
     * e_mail : 123456789@qq.com
     * projectName : 毕设题目
     * graduationdest : 工作
     * projectstage : 代码编写
     * projectprogress : 50
     * creditowed : 0
     * grade : 0
     * defenceType : 答辩资格与情况
     */

    private String studentId;
    private String studentName;
    private String sex;
    private String s_class;
    private String major;
    private String telephone;
    private String e_mail;
    private String graduationdest;
    private String destination;
    private String projectName;
    private String projectstage;
    private String projectReport;
    private int taskProgress;
    private int projectprogress;
    private int creditowed;
    private int grade;
    private int denfeceType;
    private TeacherInfo teacher;
    private TaskInfo taskInfo;
    private ProjectInfo project;
    private String adress;
    private GroupInfo group;


    @Override
    public String toString() {
        return "StudentInfo{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", sex='" + sex + '\'' +
                ", s_class='" + s_class + '\'' +
                ", major='" + major + '\'' +
                ", telephone='" + telephone + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", graduationdest='" + graduationdest + '\'' +
                ", destination='" + destination + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectstage='" + projectstage + '\'' +
                ", projectReport='" + projectReport + '\'' +
                ", taskProgress=" + taskProgress +
                ", projectprogress=" + projectprogress +
                ", creditowed=" + creditowed +
                ", grade=" + grade +
                ", denfeceType=" + denfeceType +
                ", teacher=" + teacher +
                ", taskInfo=" + taskInfo +
                ", project=" + project +
                ", adress='" + adress + '\'' +
                ", group=" + group +
                '}';
    }

    public GroupInfo getGroup() {
        return group;
    }

    public void setGroup(GroupInfo group) {
        this.group = group;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getProjectReport() {
        return projectReport;
    }

    public void setProjectReport(String projectReport) {
        this.projectReport = projectReport;
    }

    public int getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(int taskProgress) {
        this.taskProgress = taskProgress;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public TeacherInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherInfo teacher) {
        this.teacher = teacher;
    }

    public int getDenfeceType() {
        return denfeceType;
    }

    public void setDenfeceType(int denfeceType) {
        this.denfeceType = denfeceType;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudengName() {
        return studentName;
    }

    public void setStudengName(String studengName) {
        this.studentName = studengName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getS_class() {
        return s_class;
    }

    public void setS_class(String s_class) {
        this.s_class = s_class;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getGraduationdest() {
        return graduationdest;
    }

    public void setGraduationdest(String graduationdest) {
        this.graduationdest = graduationdest;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectstage() {
        return projectstage;
    }

    public void setProjectstage(String projectstage) {
        this.projectstage = projectstage;
    }

    public int getProjectprogress() {
        return projectprogress;
    }

    public void setProjectprogress(int projectprogress) {
        this.projectprogress = projectprogress;
    }

    public int getCreditowed() {
        return creditowed;
    }

    public void setCreditowed(int creditowed) {
        this.creditowed = creditowed;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

}