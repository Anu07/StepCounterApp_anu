
package com.sd.src.stepcounterapp.model.challenge;

public class Datum {

    private long _V;
    private String _id;
    private long averageSteps;
    private MyChallenge challenge;
    private String challengeId;
    private String completedAt;
    private String createdAt;
    private long initialSteps;
    private Object invitationId;
    private Boolean isCompleted;
    private Boolean isEnd;
    private Boolean isStop;
    private String joinedAt;
    private String joinedDate;
    private Object stopAt;
    private long targetSteps;
    private String updatedAt;
    private String userId;

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

    public long getAverageSteps() {
        return averageSteps;
    }

    public void setAverageSteps(long averageSteps) {
        this.averageSteps = averageSteps;
    }

    public MyChallenge getChallenge() {
        return challenge;
    }

    public void setChallenge(MyChallenge challenge) {
        this.challenge = challenge;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getInitialSteps() {
        return initialSteps;
    }

    public void setInitialSteps(long initialSteps) {
        this.initialSteps = initialSteps;
    }

    public Object getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Object invitationId) {
        this.invitationId = invitationId;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Boolean getEnd() {
        return isEnd;
    }

    public void setEnd(Boolean end) {
        isEnd = end;
    }

    public Boolean getStop() {
        return isStop;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Object getStopAt() {
        return stopAt;
    }

    public void setStopAt(Object stopAt) {
        this.stopAt = stopAt;
    }

    public long getTargetSteps() {
        return targetSteps;
    }

    public void setTargetSteps(long targetSteps) {
        this.targetSteps = targetSteps;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
