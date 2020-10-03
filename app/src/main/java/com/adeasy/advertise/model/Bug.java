package com.adeasy.advertise.model;

public class Bug {
    private String bugId;
    private String description;
    private String status;
    private String user;
    private String filedDate;


    public Bug() {
    }

    public String getBugId() {
        return bugId;
    }

    public void setBugId(String bugId) {
        this.bugId = bugId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFiledDate() {
        return filedDate;
    }

    public void setFiledDate(String filedDate) {
        this.filedDate = filedDate;
    }
}
