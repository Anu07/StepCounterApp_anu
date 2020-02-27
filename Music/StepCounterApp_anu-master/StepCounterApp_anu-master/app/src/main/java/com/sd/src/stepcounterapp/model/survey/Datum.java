
package com.sd.src.stepcounterapp.model.survey;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unused")
public class Datum implements Parcelable {

    private String _id;
    private long earningToken;
    private String expireOn;
    private Boolean isAnswered;
    private Boolean isFeatured;
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
    
    public Boolean getAnswered() {
        return isAnswered;
    }
    
    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }
    
    public Boolean getFeatured() {
        return isFeatured;
    }
    
    public void setFeatured(Boolean featured) {
        isFeatured = featured;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeLong(this.earningToken);
        dest.writeString(this.expireOn);
        dest.writeValue(this.isAnswered);
        dest.writeValue(this.isFeatured);
        dest.writeString(this.name);
        dest.writeList(this.questions);
    }

    public Datum() {
    }

    protected Datum(Parcel in) {
        this._id = in.readString();
        this.earningToken = in.readLong();
        this.expireOn = in.readString();
        this.isAnswered = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isFeatured = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.name = in.readString();
        this.questions = new ArrayList<Question>();
        in.readList(this.questions, Question.class.getClassLoader());
    }

    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel source) {
            return new Datum(source);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}
