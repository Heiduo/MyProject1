package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
* 公告信息表
*/
public class AnnounceInfo extends BmobObject implements Serializable {
    private String announceId;
    private String announceName;
    private String announceIntroduce2;
    private String announceTime;
    private String announceIntroduce;
    private String announceAdminName;
    private String state;
    private TeacherInfo teacher;

    public TeacherInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherInfo teacher) {
        this.teacher = teacher;
    }

    public String getAnnounceId() {
        return announceId;
    }

    public void setAnnounceId(String announceId) {
        this.announceId = announceId;
    }

    public String getAnnounceName() {
        return announceName;
    }

    public void setAnnounceName(String announceName) {
        this.announceName = announceName;
    }

    public String getAnnounceIntroduce2() {
        return announceIntroduce2;
    }

    public void setAnnounceIntroduce2(String announceIntroduce2) {
        this.announceIntroduce2 = announceIntroduce2;
    }

    public String getAnnounceTime() {
        return announceTime;
    }

    public void setAnnounceTime(String announceTime) {
        this.announceTime = announceTime;
    }

    public String getAnnounceIntroduce() {
        return announceIntroduce;
    }

    public void setAnnounceIntroduce(String announceIntroduce) {
        this.announceIntroduce = announceIntroduce;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getannounceId() {
        return announceId;
    }
    public void setannounceId(String announceId) {
        this.announceId = announceId;
    }
    public String getannounceName() {
        return announceName;
    }
    public void setannounceName(String announceName) {
        this.announceName = announceName;
    }
    public String getannounceIntroduce2() {
        return announceIntroduce2;
    }
    public void setannounceIntroduce2(String announceIntroduce2) {
        this.announceIntroduce2 = announceIntroduce2;
    }
    public String getannounceTime() {
        return announceTime;
    }
    public void setannounceTime(String announceTime) {
        this.announceTime = announceTime;
    }
    public String getannounceIntroduce() {
        return announceIntroduce;
    }
    public void setannounceIntroduce(String announceIntroduce) {
        this.announceIntroduce = announceIntroduce;
    }
    public String getAnnounceAdminName(){
        return announceAdminName;
    }
    public void setAnnounceAdminName(String announceAdminName) {
        this.announceAdminName = announceAdminName;
    }

    @Override
    public String toString() {
        return "AnnounceInfo{" +
                "announceId='" + announceId + '\'' +
                ", announceName='" + announceName + '\'' +
                ", announceIntroduce2='" + announceIntroduce2 + '\'' +
                ", announceTime='" + announceTime + '\'' +
                ", announceIntroduce='" + announceIntroduce + '\'' +
                ", announceAdminName='" + announceAdminName + '\'' +
                '}';
    }
}
