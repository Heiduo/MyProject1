package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 教师信息表
 */
public class TeacherInfo extends BmobObject implements Serializable {
	public String teacherId;
	public String teacherName;
	public String teacherSex;
	public String teacherTel;
	public String teacherEmail;
	public String teacherPost;//职称
	public GroupInfo group;   //所属小组

	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public String getTeacherEmail() {
		return teacherEmail;
	}
	public void setTeacherEmail(String teacherEmail) {
		this.teacherEmail = teacherEmail;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherSex() {
		return teacherSex;
	}
	public void setTeacherSex(String teacherSex) {
		this.teacherSex = teacherSex;
	}
	public String getTeacherTel() {
		return teacherTel;
	}
	public void setTeacherTel(String teacherTel) {
		this.teacherTel = teacherTel;
	}
	public String getTeacherPost() {
		return teacherPost;
	}
	public void setTeacherPost(String teacherPost) {
		this.teacherPost = teacherPost;
	}

	@Override
	public String toString() {
		return "TeacherInfo{" +
				"teacherId='" + teacherId + '\'' +
				", teacherName='" + teacherName + '\'' +
				", teacherSex='" + teacherSex + '\'' +
				", teacherTel='" + teacherTel + '\'' +
				", teacherEmail='" + teacherEmail + '\'' +
				", teacherPost='" + teacherPost + '\'' +
				", group=" + group +
				'}';
	}

}
