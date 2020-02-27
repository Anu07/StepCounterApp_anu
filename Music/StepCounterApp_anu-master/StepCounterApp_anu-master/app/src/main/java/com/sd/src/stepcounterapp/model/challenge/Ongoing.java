
package com.sd.src.stepcounterapp.model.challenge;

import android.os.Parcel;
import android.os.Parcelable;

public class Ongoing implements Parcelable {

    private int _V;
    private String _id;
    private String addedBy;
    private String adminId;
    private int bonusPoint1;
    private int bonusPoint2;
    private int bonusPoint3;
    private String challengeType;
    private String department;
    private String description;
    private String endDateTime;
    private String image;
    private Boolean isActive;
    private Boolean isCompleted;
    private Boolean isDeleted;
    private Boolean isEnd;
    private Boolean isFeatured;
    private Boolean isStop;
    private int joinedIn;
    private String name;
    private int points;
    private String shortDesc;
    private String startDateTime;
    private int steps;
    private String updatedAt;

    public String getInvitationType() {
        return invitationType;
    }

    public void setInvitationType(String invitationType) {
        this.invitationType = invitationType;
    }

    private String invitationType;


    public Ongoing() {
    }


    public int get_V() {
        return _V;
    }

    public void set_V(int _V) {
        this._V = _V;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public int getBonusPoint1() {
        return bonusPoint1;
    }

    public void setBonusPoint1(int bonusPoint1) {
        this.bonusPoint1 = bonusPoint1;
    }

    public int getBonusPoint2() {
        return bonusPoint2;
    }

    public void setBonusPoint2(int bonusPoint2) {
        this.bonusPoint2 = bonusPoint2;
    }

    public int getBonusPoint3() {
        return bonusPoint3;
    }

    public void setBonusPoint3(int bonusPoint3) {
        this.bonusPoint3 = bonusPoint3;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getEnd() {
        return isEnd;
    }

    public void setEnd(Boolean end) {
        isEnd = end;
    }

    public Boolean getFeatured() {
        return isFeatured;
    }

    public void setFeatured(Boolean featured) {
        isFeatured = featured;
    }

    public Boolean getStop() {
        return isStop;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
    }

    public int getJoinedIn() {
        return joinedIn;
    }

    public void setJoinedIn(int joinedIn) {
        this.joinedIn = joinedIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._V);
        dest.writeString(this._id);
        dest.writeString(this.addedBy);
        dest.writeString(this.adminId);
        dest.writeInt(this.bonusPoint1);
        dest.writeInt(this.bonusPoint2);
        dest.writeInt(this.bonusPoint3);
        dest.writeString(this.challengeType);
        dest.writeString(this.department);
        dest.writeString(this.description);
        dest.writeString(this.endDateTime);
        dest.writeString(this.image);
        dest.writeValue(this.isActive);
        dest.writeValue(this.isCompleted);
        dest.writeValue(this.isDeleted);
        dest.writeValue(this.isEnd);
        dest.writeValue(this.isFeatured);
        dest.writeValue(this.isStop);
        dest.writeInt(this.joinedIn);
        dest.writeString(this.name);
        dest.writeInt(this.points);
        dest.writeString(this.shortDesc);
        dest.writeString(this.startDateTime);
        dest.writeInt(this.steps);
        dest.writeString(this.updatedAt);
        dest.writeString(this.invitationType);
    }

    protected Ongoing(Parcel in) {
        this._V = in.readInt();
        this._id = in.readString();
        this.addedBy = in.readString();
        this.adminId = in.readString();
        this.bonusPoint1 = in.readInt();
        this.bonusPoint2 = in.readInt();
        this.bonusPoint3 = in.readInt();
        this.challengeType = in.readString();
        this.department = in.readString();
        this.description = in.readString();
        this.endDateTime = in.readString();
        this.image = in.readString();
        this.isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isCompleted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isDeleted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isEnd = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isFeatured = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isStop = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.joinedIn = in.readInt();
        this.name = in.readString();
        this.points = in.readInt();
        this.shortDesc = in.readString();
        this.startDateTime = in.readString();
        this.steps = in.readInt();
        this.updatedAt = in.readString();
        this.invitationType = in.readString();
    }

    public static final Parcelable.Creator<Ongoing> CREATOR = new Parcelable.Creator<Ongoing>() {
        @Override
        public Ongoing createFromParcel(Parcel source) {
            return new Ongoing(source);
        }

        @Override
        public Ongoing[] newArray(int size) {
            return new Ongoing[size];
        }
    };
}
