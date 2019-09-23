
package com.sd.src.stepcounterapp.model.survey.mysurveyresponse;

import java.util.List;
@SuppressWarnings("unused")
public class Question {

    private String _id;
    private List<Option> options;
    private String question;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
