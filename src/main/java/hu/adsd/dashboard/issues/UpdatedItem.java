package hu.adsd.dashboard.issues;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class UpdatedItem {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String changedStatusFrom;
    private String changedStatusTo;
    private int storyPoints;
    private String itemKey;
    private LocalDate lastChangedOn;
    private LocalTime lastChangedOnTime;
    private boolean isResolved;
    private String itemSummary;
    private String avatarUrl;
    private String author;
    private String itemStatus;
    private String itemType;


    //getter en setter


    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getItemSummary() {
        return itemSummary;
    }

    public void setItemSummary(String itemSummary) {
        this.itemSummary = itemSummary;
    }


    public String getChangedStatusFrom() {
        return changedStatusFrom;
    }

    public void setChangedStatusFrom(String changedStatusFrom) {
        this.changedStatusFrom = changedStatusFrom;
    }

    public String getChangedStatusTo() {
        return changedStatusTo;
    }

    public void setChangedStatusTo(String changedStatusTo) {
        this.changedStatusTo = changedStatusTo;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public LocalDate getLastChangedOn() {
        return lastChangedOn;
    }

    public void setLastChangedOn(LocalDate lastChangedOn) {
        this.lastChangedOn = lastChangedOn;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public LocalTime getLastChangedOnTime() {
        return lastChangedOnTime;
    }

    public void setLastChangedOnTime(LocalTime lastChangedOnTime) {
        this.lastChangedOnTime = lastChangedOnTime;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }
}
