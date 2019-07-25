package model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class GroupInfo extends BmobObject implements Serializable {

    /**
     * groupId : ID
     * groupTime : time
     * groupAddress : Address
     */

    public String groupId;
    public String groupTime;
    public String groupAddress;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(String groupTime) {
        this.groupTime = groupTime;
    }

    public String getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(String groupAddress) {
        this.groupAddress = groupAddress;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupId='" + groupId + '\'' +
                ", groupTime='" + groupTime + '\'' +
                ", groupAddress='" + groupAddress + '\'' +
                '}';
    }
}
