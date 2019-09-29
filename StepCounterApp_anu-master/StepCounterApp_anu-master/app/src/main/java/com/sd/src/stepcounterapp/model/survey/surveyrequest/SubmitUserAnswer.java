package com.sd.src.stepcounterapp.model.survey.surveyrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Awesome Pojo Generator
 * */
public class SubmitUserAnswer {
  @SerializedName("answer")
  @Expose
  private List<String> answer;
  @SerializedName("id")
  @Expose
  private String id;
  public void setId(String id){
   this.id=id;
  }
  public String getId(){
   return id;
  }
    
    public List<String> getAnswer() {
        return answer;
    }
    
    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}