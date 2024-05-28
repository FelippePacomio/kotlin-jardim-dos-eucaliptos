package com.example.associacao_jardim_eucaliptos;

public class EventsItem {

    private String eventsImage;
    private String eventsTitle;
    private String eventDescription;
    private String eventDate;

    public EventsItem(String eventsImage, String eventsTitle, String eventDescription, String eventDate) {
        this.eventsImage = eventsImage;
        this.eventsTitle = eventsTitle;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
    }

    public String getEventsImage() {
        return eventsImage;
    }

    public String getEventsTitle() {
        return eventsTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }
}
