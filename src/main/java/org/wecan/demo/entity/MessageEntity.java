package org.wecan.demo.entity;

import java.util.List;

public class MessageEntity {
    private int id;
    private String username;
    private String text;
    private int pid;
    private String createTime;
    private String updateTime;
    private int userId;
    private List<MessageEntity> childContent;

    public MessageEntity() {
    }


    public MessageEntity(String text, int id, String updateTime,int userId) {
        this.text = text;
        this.id = id;
        this.updateTime = updateTime;
        this.userId = userId;
    }

    public MessageEntity(String username, String text, int pid, String createTime, int userId) {
        this.username = username;
        this.text = text;
        this.pid = pid;
        this.createTime = createTime;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<MessageEntity> getChildContent() {
        return childContent;
    }

    public void setChildContent(List<MessageEntity> childContent) {
        this.childContent = childContent;
    }
}
