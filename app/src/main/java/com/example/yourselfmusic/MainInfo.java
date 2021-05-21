package com.example.yourselfmusic;

public class MainInfo {

    public MainInfo() {
    }

    private String status_artist;
    private String status_name;
    private String status_thumbnail;
    private String status_timestamp;
    private String status_download_link;

    public MainInfo(String status_artist, String status_name, String status_thumbnail, String status_timestamp, String status_download_link, String status_video_url) {
        this.status_artist = status_artist;
        this.status_name = status_name;
        this.status_thumbnail = status_thumbnail;
        this.status_timestamp = status_timestamp;
        this.status_download_link = status_download_link;
        this.status_video_url = status_video_url;
    }

    public String getStatus_artist() {
        return status_artist;
    }

    public void setStatus_artist(String status_artist) {
        this.status_artist = status_artist;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getStatus_thumbnail() {
        return status_thumbnail;
    }

    public void setStatus_thumbnail(String status_thumbnail) {
        this.status_thumbnail = status_thumbnail;
    }

    public String getStatus_timestamp() {
        return status_timestamp;
    }

    public void setStatus_timestamp(String status_timestamp) {
        this.status_timestamp = status_timestamp;
    }

    public String getStatus_download_link() {
        return status_download_link;
    }

    public void setStatus_download_link(String status_download_link) {
        this.status_download_link = status_download_link;
    }

    public String getStatus_video_url() {
        return status_video_url;
    }

    public void setStatus_video_url(String status_video_url) {
        this.status_video_url = status_video_url;
    }

    private String status_video_url;
}
