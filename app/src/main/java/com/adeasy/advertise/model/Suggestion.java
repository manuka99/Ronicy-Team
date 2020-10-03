package com.adeasy.advertise.model;

public class Suggestion {
    private String suggestionID;
    private String suggestionDescription;
    private String filedDate;
    private String status;

    public Suggestion() {
    }

    public String getSuggestionID() {
        return suggestionID;
    }

    public void setSuggestionID(String suggestionID) {
        this.suggestionID = suggestionID;
    }

    public String getSuggestionDescription() {
        return suggestionDescription;
    }

    public void setSuggestionDescription(String suggestionDescription) {
        this.suggestionDescription = suggestionDescription;
    }

    public String getFiledDate() {
        return filedDate;
    }

    public void setFiledDate(String filedDate) {
        this.filedDate = filedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
