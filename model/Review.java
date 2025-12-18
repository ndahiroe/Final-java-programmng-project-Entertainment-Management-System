package model;

import java.time.LocalDateTime;

public class Review {
    private int reviewID;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private LocalDateTime createdAt;
    private int userID;
    private int sponsorID;

    public Review() {}

    public int getReviewID() { return reviewID; }
    public void setReviewID(int reviewID) { this.reviewID = reviewID; }

    public String getAttribute1() { return attribute1; }
    public void setAttribute1(String attribute1) { this.attribute1 = attribute1; }

    public String getAttribute2() { return attribute2; }
    public void setAttribute2(String attribute2) { this.attribute2 = attribute2; }

    public String getAttribute3() { return attribute3; }
    public void setAttribute3(String attribute3) { this.attribute3 = attribute3; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getSponsorID() { return sponsorID; }
    public void setSponsorID(int sponsorID) { this.sponsorID = sponsorID; }
}
