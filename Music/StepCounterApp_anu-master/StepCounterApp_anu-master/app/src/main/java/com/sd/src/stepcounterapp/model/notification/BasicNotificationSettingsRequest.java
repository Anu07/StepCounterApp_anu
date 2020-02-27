
package com.sd.src.stepcounterapp.model.notification;


@SuppressWarnings("unused")
public class BasicNotificationSettingsRequest {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    private Boolean goalAchivedNotification;
    private Boolean newChallengeNotification;
    private Boolean newSurveyNotification;

    public BasicNotificationSettingsRequest(String userId, Boolean goalAchivedNotification, Boolean newChallengeNotification, Boolean newSurveyNotification) {
        this.userId = userId;
        this.goalAchivedNotification = goalAchivedNotification;
        this.newChallengeNotification = newChallengeNotification;
        this.newSurveyNotification = newSurveyNotification;
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
