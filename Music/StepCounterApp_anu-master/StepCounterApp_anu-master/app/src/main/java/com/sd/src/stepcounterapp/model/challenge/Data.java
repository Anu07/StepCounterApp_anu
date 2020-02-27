
package com.sd.src.stepcounterapp.model.challenge;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("addedBy")
    private String mAddedBy;
    @SerializedName("adminId")
    private String mAdminId;
    @SerializedName("averageDailySteps")
    private int mAverageDailySteps;
    @SerializedName("bonusPoint1")
    private int mBonusPoint1;
    @SerializedName("bonusPoint2")
    private int mBonusPoint2;
    @SerializedName("bonusPoint3")
    private int mBonusPoint3;
    @SerializedName("challengeType")
    private String mChallengeType;
    @SerializedName("count")
    private int mCount;
    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("department")
    private String mDepartment;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("endDateTime")
    private String mEndDateTime;
    @SerializedName("image")
    private String mImage;
    @SerializedName("increaseSteps")
    private int mIncreaseSteps;
    @SerializedName("is_active")
    private Boolean mIsActive;
    @SerializedName("is_deleted")
    private Boolean mIsDeleted;
    @SerializedName("is_featured")
    private Boolean mIsFeatured;
    @SerializedName("joinedIn")
    private int mJoinedIn;
    @SerializedName("name")
    private String mName;
    @SerializedName("points")
    private int mPoints;
    @SerializedName("shortDesc")
    private String mShortDesc;
    @SerializedName("startDateTime")
    private String mStartDateTime;
    @SerializedName("steps")
    private int mSteps;
    @SerializedName("updatedAt")
    private String mUpdatedAt;

    public String getInvitationType() {
        return invitationType;
    }

    public void setInvitationType(String invitationType) {
        this.invitationType = invitationType;
    }

    @SerializedName("invitationType")
    private String invitationType;
    @SerializedName("_id")
    private String m_id;


    private boolean is_completed;
    public String getAddedBy() {
        return mAddedBy;
    }

    public void setAddedBy(String addedBy) {
        mAddedBy = addedBy;
    }

    public String getAdminId() {
        return mAdminId;
    }

    public void setAdminId(String adminId) {
        mAdminId = adminId;
    }

    public int getAverageDailySteps() {
        return mAverageDailySteps;
    }

    public void setAverageDailySteps(int averageDailySteps) {
        mAverageDailySteps = averageDailySteps;
    }

    public int getBonusPoint1() {
        return mBonusPoint1;
    }

    public void setBonusPoint1(int bonusPoint1) {
        mBonusPoint1 = bonusPoint1;
    }

    public int getBonusPoint2() {
        return mBonusPoint2;
    }

    public void setBonusPoint2(int bonusPoint2) {
        mBonusPoint2 = bonusPoint2;
    }

    public int getBonusPoint3() {
        return mBonusPoint3;
    }

    public void setBonusPoint3(int bonusPoint3) {
        mBonusPoint3 = bonusPoint3;
    }

    public String getChallengeType() {
        return mChallengeType;
    }

    public void setChallengeType(String challengeType) {
        mChallengeType = challengeType;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getEndDateTime() {
        return mEndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        mEndDateTime = endDateTime;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public int getIncreaseSteps() {
        return mIncreaseSteps;
    }

    public void setIncreaseSteps(int increaseSteps) {
        mIncreaseSteps = increaseSteps;
    }

    public Boolean getIsActive() {
        return mIsActive;
    }

    public void setIsActive(Boolean isActive) {
        mIsActive = isActive;
    }

    public Boolean getIsDeleted() {
        return mIsDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        mIsDeleted = isDeleted;
    }

    public Boolean getIsFeatured() {
        return mIsFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        mIsFeatured = isFeatured;
    }

    public int getJoinedIn() {
        return mJoinedIn;
    }

    public void setJoinedIn(int joinedIn) {
        mJoinedIn = joinedIn;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPoints() {
        return mPoints;
    }

    public void setPoints(int points) {
        mPoints = points;
    }

    public String getShortDesc() {
        return mShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        mShortDesc = shortDesc;
    }

    public String getStartDateTime() {
        return mStartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        mStartDateTime = startDateTime;
    }

    public int getSteps() {
        return mSteps;
    }

    public void setSteps(int steps) {
        mSteps = steps;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

}
