package com.example.myapplication;

public class MediaObject
{

    public MediaObject()
    {

    }

    private String description, userId, date, postId, views, userName, mediaUrl, thumbNail, postCategaries;

    public MediaObject(String description, String userId, String date, String postId,String postCategaries, String views, String userName, String mediaUrl, String thumbNail) {
        this.description = description;
        this.userId = userId;
        this.date = date;
        this.postId = postId;
        this.views = views;
        this.userName = userName;
        this.mediaUrl = mediaUrl;
        this.thumbNail = thumbNail;
        this.postCategaries = postCategaries;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getPostCategaries() {
        return postCategaries;
    }

    public void setPostCategaries(String postCategaries) {
        this.postCategaries = postCategaries;
    }
}
