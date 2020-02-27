
package com.sd.src.stepcounterapp.model.survey.mysurveyresponse;

import java.util.List;
@SuppressWarnings("unused")
public class UserAnswer {

    private List<String> answer;
    private String id;

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
