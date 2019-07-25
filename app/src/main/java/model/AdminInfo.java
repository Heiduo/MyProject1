package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 管理员信息表
 */
public class AdminInfo extends BmobObject implements Serializable {
	public String adminId;
	public String adminName;
	public String announceId;
	public AnnounceInfo announce;
	public String getadminId() {
		return adminId;
	}
	public void setadminId(String adminId) {
		this.adminId = adminId;
	}
	public String getannounceId() {
		return announceId;
	}
	public void setannounceId(String announceId) {
		this.announceId = announceId;
	}

	public String getAdminName() {
		return adminName;
	}
	public void setadminName(String adminName) {
		this.adminName = adminName;
	}

	public AnnounceInfo getannounce() {
		return announce;
	}

	public void setannounce(AnnounceInfo announce) {
		this.announce = announce;
	}


	@Override
	public String toString() {
		return "AdminInfo{" +
				"adminId='" + adminId + '\'' +
				", adminName='" + adminName + '\'' +
				", announceId='" + announceId + '\'' +
				", announce=" + announce +
				'}';
	}
}
