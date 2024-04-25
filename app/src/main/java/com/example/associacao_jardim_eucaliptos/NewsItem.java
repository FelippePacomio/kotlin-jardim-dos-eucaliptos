package com.example.associacao_jardim_eucaliptos;

public class NewsItem {

    private int newsThumbail;
    private String newsTitle;

    public NewsItem(int newsThumbail, String newsTitle) {
        this.newsThumbail = newsThumbail;
        this.newsTitle = newsTitle;
    }

    public int getNewsThumbail() {
        return newsThumbail;
    }

    public String getNewsTitle() {
        return newsTitle;
    }
}
