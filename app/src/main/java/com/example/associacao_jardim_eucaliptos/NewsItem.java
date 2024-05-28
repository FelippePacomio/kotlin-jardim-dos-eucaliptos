package com.example.associacao_jardim_eucaliptos;

public class NewsItem {

    private String newsThumbnail;
    private String newsTitle;
    private String newsDescription;
    private String newsDate;

    public NewsItem(String newsThumbnail, String newsTitle, String newsDescription, String newsDate) {
        this.newsThumbnail = newsThumbnail;
        this.newsTitle = newsTitle;
        this.newsDescription = newsDescription;
        this.newsDate = newsDate;
    }

    public String getNewsThumbnail() {
        return newsThumbnail;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public String getNewsDate() {
        return newsDate;
    }
}
