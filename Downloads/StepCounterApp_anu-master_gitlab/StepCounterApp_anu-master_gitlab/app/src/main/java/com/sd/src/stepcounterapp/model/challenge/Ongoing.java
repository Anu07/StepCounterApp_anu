
package com.sd.src.stepcounterapp.model.challenge;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class Ongoing implements Parcelable {

    private long _V;
    private String _id;
    private String addedBy;
    private String adminId;
    private long bonusPoint1;
    private long bonusPoint2;
    private long bonusPoint3;
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
    private long joinedIn;
    private String name;
    private long points;
    private String shortDesc;
    private String startDateTime;
    private long steps;
    private String updatedAt;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._V);
        dest.writeString(this._id);
        dest.writeString(this.addedBy);
        dest.writeString(this.adminId);
        dest.writeLong(this.bonusPoint1);
        dest.writeLong(this.bonusPoint2);
        dest.writeLong(this.bonusPoint3);
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
        dest.writeLong(this.joinedIn);
        dest.writeString(this.name);
        dest.writeLong(this.points);
        dest.writeString(this.shortDesc);
        dest.writeString(this.startDateTime);
        dest.writeLong(this.steps);
        dest.writeString(this.updatedAt);
    }

    public Ongoing() {
    }

    protected Ongoing(Parcel in) {
        this._V = in.readLong();
        this._id = in.readString();
        this.addedBy = in.readString();
        this.adminId = in.readString();
        this.bonusPoint1 = in.readLong();
        this.bonusPoint2 = in.readLong();
        this.bonusPoint3 = in.readLong();
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
        this.joinedIn = in.readLong();
        this.name = in.readString();
        this.points = in.readLong();
        this.shortDesc = in.readString();
        this.startDateTime = in.readString();
        this.steps = in.readLong();
        this.updatedAt = in.readString();
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

    public long get_V() {
        return _V;
    }

    public void set_V(long _V) {
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

    public long getBonusPoint1() {
        return bonusPoint1;
    }

    public void setBonusPoint1(long bonusPoint1) {
        this.bonusPoint1 = bonusPoint1;
    }

    public long getBonusPoint2() {
        return bonusPoint2;
    }

    public void setBonusPoint2(long bonusPoint2) {
        this.bonusPoint2 = bonusPoint2;
    }

    public long getBonusPoint3() {
        return bonusPoint3;
    }

    public void setBonusPoint3(long bonusPoint3) {
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

    public long getJoinedIn() {
        return joinedIn;
    }

    public void setJoinedIn(long joinedIn) {
        this.joinedIn = joinedIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
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

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Creator<Ongoing> getCREATOR() {
        return CREATOR;
    }
}
