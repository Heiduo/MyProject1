package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class TeacherGroup extends BmobObject implements Serializable {

    /**
     * teacherId : 201591321
     * groupId : R01
     */

    public String teacherId;
    public String groupId;

    public TeacherInfo teacher;
    public GroupInfo group;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public TeacherInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherInfo teacher) {
        this.teacher = teacher;
    }

    public GroupInfo getGroup() {
        return group;
    }

    public void setGroup(GroupInfo group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "TeacherGroup{" +
                "teacherId=" + teacherId +
                ", groupId='" + groupId + '\'' +
                ", teacher=" + teacher +
                ", group=" + group +
                '}';
    }
}
