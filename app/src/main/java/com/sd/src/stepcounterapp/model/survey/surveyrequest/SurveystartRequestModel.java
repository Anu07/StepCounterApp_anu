package com.sd.src.stepcounterapp.model.survey.surveyrequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sd.src.stepcounterapp.model.survey.Data;

import java.util.List;
/**
 {
 "userId":"5d24496cdc18c5d737a69d0d",
 "surveyId":"5d35904708cc29292ea6936b",
 "survey":{
 "_id": "5d35904708cc29292ea6936b",
 "name": "travel preferences",
 "expireOn": "2020-11-21T04:32:18.758Z",
 "earningToken": 10,
 "products": [
 {
 "_id": "5d35a8c4ea9c4b2fc26aa2d1",
 "quesType": "checkbox",
 "question": "test123456789",
 "option1": "opt1",
 "option2": "opt2",
 "option3": "opt3",
 "option4": "opt4",
 "answer": [
 "opt1",
 "opt3"
 ]
 },
 {
 "_id": "5d35a8c4ea9c4b2fc26aa2d2",
 "quesType": "radio",
 "question": "test123",
 "option1": "opt1",
 "option2": "opt2",
 "option3": "opt3",
 "option4": "opt4",
 "answer": [
 "opt1"
 ]
 }
 ]
 },
 "userAnswer":[
 {"id":"5d35a8c4ea9c4b2fc26aa2d1", "answer":["opt1","opt2"]},
 {"id":"5d35a8c4ea9c4b2fc26aa2d2", "answer":["opt1"]}
 ]
 }
 * */
public class SurveystartRequestModel{
  @SerializedName("surveyId")
  @Expose
  private String surveyId;
  @SerializedName("userAnswer")
  @Expose
  private List<UserAnswer> userAnswer;
  @SerializedName("survey")
  @Expose
  private Data survey;
  @SerializedName("userId")
  @Expose
  private String userId;
    
    public SurveystartRequestModel(String surveyId, List<UserAnswer> userAnswer, Data survey, String userId) {
        this.surveyId = surveyId;
        this.userAnswer = userAnswer;
        this.survey = survey;
        this.userId = userId;
    }
    
    public void setSurveyId(String surveyId){
   this.surveyId=surveyId;
  }
  public String getSurveyId(){
   return surveyId;
  }
  public void setUserAnswer(List<UserAnswer> userAnswer){
   this.userAnswer=userAnswer;
  }
  public List<UserAnswer> getUserAnswer(){
   return userAnswer;
  }
  public void setSurvey(Data survey){
   this.survey=survey;
  }
  public Data getSurvey(){
   return survey;
  }
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
}