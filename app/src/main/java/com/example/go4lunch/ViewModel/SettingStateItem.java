package com.example.go4lunch.ViewModel;

public class SettingStateItem {
    private Boolean notification;
    private Float radius;

    public SettingStateItem(Boolean notification, Float radius) {
        this.radius = radius;
        this.notification = notification;
    }

    public SettingStateItem() {
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }
}
