package com.example.associacao_jardim_eucaliptos;

public class NewsItem {

    private String newsThumbail;
    private String newsTitle;

    public NewsItem(String newsThumbail, String newsTitle) {
        this.newsThumbail = newsThumbail;
        this.newsTitle = newsTitle;
    }

    public String getNewsThumbnail() {
        return newsThumbail;
    }

    public String getNewsTitle() {
        return newsTitle;
    }
}
