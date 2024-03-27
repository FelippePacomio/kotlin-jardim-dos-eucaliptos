package com.example.pi_5_semestre;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);
        holder.newsTitle.setText(newsItem.getNewsTitle());
        holder.newsThumbnail.setImageResource(newsItem.getNewsThumbail());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsThumbnail;
        TextView newsTitle;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsThumbnail = itemView.findViewById(R.id.newsThumbnail);
            newsTitle = itemView.findViewById(R.id.newsTitle);
        }
    }
}