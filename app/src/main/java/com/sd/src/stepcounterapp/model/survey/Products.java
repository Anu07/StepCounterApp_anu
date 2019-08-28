package com.sd.src.stepcounterapp.model.survey;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class Products implements Parcelable {
    
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("quesType")
    @Expose
    private String quesType;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("option1")
    @Expose
    private String option1;
    @SerializedName("option2")
    @Expose
    private String option2;
    @SerializedName("option3")
    @Expose
    private String option3;
    @SerializedName("option4")
    @Expose
    private String option4;
    @SerializedName("answer")
    @Expose
    private List<String> answer = null;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getQuesType() {
        return quesType;
    }
    
    public void setQuesType(String quesType) {
        this.quesType = quesType;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getOption1() {
        return option1;
    }
    
    public void setOption1(String option1) {
        this.option1 = option1;
    }
    
    public String getOption2() {
        return option2;
    }
    
    public void setOption2(String option2) {
        this.option2 = option2;
    }
    
    public String getOption3() {
        return option3;
    }
    
    public void setOption3(String option3) {
        this.option3 = option3;
    }
    
    public String getOption4() {
        return option4;
    }
    
    public void setOption4(String option4) {
        this.option4 = option4;
    }
    
    public List<String> getAnswer() {
        return answer;
    }
    
    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.quesType);
        dest.writeString(this.question);
        dest.writeString(this.option1);
        dest.writeString(this.option2);
        dest.writeString(this.option3);
        dest.writeString(this.option4);
        dest.writeStringList(this.answer);
    }
    
    public Products() {
    }
    
    protected Products(Parcel in) {
        this.id = in.readString();
        this.quesType = in.readString();
        this.question = in.readString();
        this.option1 = in.readString();
        this.option2 = in.readString();
        this.option3 = in.readString();
        this.option4 = in.readString();
        this.answer = in.createStringArrayList();
    }
    
    public static final Parcelable.Creator<Products> CREATOR = new Parcelable.Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel source) {
            return new Products(source);
        }
        
        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };
}