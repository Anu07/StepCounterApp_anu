
package com.sd.src.stepcounterapp.model.survey.mysurvey;

import java.util.List;
public class Datum {

    private long _V;
    private String _id;
    private String attendAt;
    private List<Survey> survey;
    private String surveyCategoryId;
    private List<UserAnswer> userAnswer;
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

    public String getAttendAt() {
        return attendAt;
    }

    public void setAttendAt(String attendAt) {
        this.attendAt = attendAt;
    }

    public List<Survey> getSurvey() {
        return survey;
    }

    public void setSurvey(List<Survey> survey) {
        this.survey = survey;
    }

    public String getSurveyCategoryId() {
        return surveyCategoryId;
    }

    public void setSurveyCategoryId(String surveyCategoryId) {
        this.surveyCategoryId = surveyCategoryId;
    }

    public List<UserAnswer> getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(List<UserAnswer> userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
