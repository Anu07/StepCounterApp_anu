
package com.sd.src.stepcounterapp.model.notificatyionlist;

@SuppressWarnings("unused")
public class Data {

    private long _V;
    private String _id;
    private String createdAt;
    private Boolean isRead;
    private String message;
    private Object readAt;
    private String subject;
    private String userId;

    public long get_V() {
        return _V;
    }

    public void set_V(long _V) {
        this._V = _V;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReadAt() {
        return readAt;
    }

    public void setReadAt(Object readAt) {
        this.readAt = readAt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
