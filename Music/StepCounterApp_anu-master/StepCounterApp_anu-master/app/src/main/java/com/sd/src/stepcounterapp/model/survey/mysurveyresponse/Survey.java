
package com.sd.src.stepcounterapp.model.survey.mysurveyresponse;

import java.util.List;
@SuppressWarnings("unused")
public class Survey {

    private String _id;
    private long earningToken;
    private String expireOn;
    private String name;
    private List<Question> questions;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getEarningToken() {
        return earningToken;
    }

    public void setEarningToken(long earningToken) {
        this.earningToken = earningToken;
    }

    public String getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(String expireOn) {
        this.expireOn = expireOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
