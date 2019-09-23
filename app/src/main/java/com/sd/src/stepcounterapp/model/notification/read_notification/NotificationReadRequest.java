
package com.sd.src.stepcounterapp.model.notification.read_notification;

@SuppressWarnings("unused")
public class NotificationReadRequest {

    private String notificationId;

    public NotificationReadRequest(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
