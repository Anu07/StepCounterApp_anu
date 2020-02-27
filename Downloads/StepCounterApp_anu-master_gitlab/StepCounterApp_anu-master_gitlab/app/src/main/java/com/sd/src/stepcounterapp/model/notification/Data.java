
package com.sd.src.stepcounterapp.model.notification;

@SuppressWarnings("unused")
public class Data {

    private String _id;
    private Boolean goalAchivedNotification;
    private Boolean newChallengeNotification;
    private Boolean newSurveyNotification;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Boolean getGoalAchivedNotification() {
        return goalAchivedNotification;
    }

    public void setGoalAchivedNotification(Boolean goalAchivedNotification) {
        this.goalAchivedNotification = goalAchivedNotification;
    }

    public Boolean getNewChallengeNotification() {
        return newChallengeNotification;
    }

    public void setNewChallengeNotification(Boolean newChallengeNotification) {
        this.newChallengeNotification = newChallengeNotification;
    }

    public Boolean getNewSurveyNotification() {
        return newSurveyNotification;
    }

    public void setNewSurveyNotification(Boolean newSurveyNotification) {
        this.newSurveyNotification = newSurveyNotification;
    }
}
