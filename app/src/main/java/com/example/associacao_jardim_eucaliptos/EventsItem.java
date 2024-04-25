package com.example.associacao_jardim_eucaliptos;

public class EventsItem {

    private int eventsImage;
    private String eventsTitle;

    public EventsItem(int eventsImage, String eventsTitle) {
        this.eventsImage = eventsImage;
        this.eventsTitle = eventsTitle;
    }

    public int getEventsImage() {
        return eventsImage;
    }

    public String getEventsTitle() {
        return eventsTitle;
    }
}
