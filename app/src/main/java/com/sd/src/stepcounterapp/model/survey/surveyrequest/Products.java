package com.sd.src.stepcounterapp.model.survey.surveyrequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class Products{
  @SerializedName("question")
  @Expose
  private String question;
    
    public List<String> getAnswer() {
        return answer;
    }
    
    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
    
    @SerializedName("answer")
  @Expose
  private List<String> answer;
  @SerializedName("option3")
  @Expose
  private String option3;
  @SerializedName("option4")
  @Expose
  private String option4;
  @SerializedName("option1")
  @Expose
  private String option1;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("option2")
  @Expose
  private String option2;
  @SerializedName("quesType")
  @Expose
  private String quesType;
  public void setQuestion(String question){
   this.question=question;
  }
  public String getQuestion(){
   return question;
  }
  public void setOption3(String option3){
   this.option3=option3;
  }
  public String getOption3(){
   return option3;
  }
  public void setOption4(String option4){
   this.option4=option4;
  }
  public String getOption4(){
   return option4;
  }
  public void setOption1(String option1){
   this.option1=option1;
  }
  public String getOption1(){
   return option1;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setOption2(String option2){
   this.option2=option2;
  }
  public String getOption2(){
   return option2;
  }
  public void setQuesType(String quesType){
   this.quesType=quesType;
  }
  public String getQuesType(){
   return quesType;
  }
}