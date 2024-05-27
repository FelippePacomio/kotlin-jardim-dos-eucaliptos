package com.example.associacao_jardim_eucaliptos;

public class EventsItem {

    private String eventsImage;
    private String eventsTitle;

    public EventsItem(String eventsImage, String eventsTitle) {
        this.eventsImage = eventsImage;
        this.eventsTitle = eventsTitle;
    }

    public String getEventsImage() {
        return eventsImage;
    }

    public String getEventsTitle() {
        return eventsTitle;
    }
}
