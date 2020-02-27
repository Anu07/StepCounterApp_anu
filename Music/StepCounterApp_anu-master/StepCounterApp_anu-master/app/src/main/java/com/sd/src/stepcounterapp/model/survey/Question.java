
package com.sd.src.stepcounterapp.model.survey;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
public class Question implements Parcelable {

    private String _id;
    private List<Option> options;
    private String question;
    private String questionType;

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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeList(this.options);
        dest.writeString(this.question);
        dest.writeString(this.questionType);
    }

    public Question() {
    }

    protected Question(Parcel in) {
        this._id = in.readString();
        this.options = new ArrayList<Option>();
        in.readList(this.options, Option.class.getClassLoader());
        this.question = in.readString();
        this.questionType = in.readString();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
