package model;

import java.time.LocalDateTime;

public class Event {
    private int eventID;
    private String referenceID;
    private String description;
    private LocalDateTime date;
    private String status;
    private String remarks;
    private int userID;

    // Getters and Setters
    public int getEventID() { return eventID; }
    public void setEventID(int eventID) { this.eventID = eventID; }

    public String getReferenceID() { return referenceID; }
    public void setReferenceID(String referenceID) { this.referenceID = referenceID; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
}
