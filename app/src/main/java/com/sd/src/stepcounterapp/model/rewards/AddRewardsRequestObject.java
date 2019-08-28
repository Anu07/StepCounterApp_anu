package com.sd.src.stepcounterapp.model.rewards;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class AddRewardsRequestObject{
    public AddRewardsRequestObject(List<String> selectedRewards, Boolean rewardFlag, String userId) {
        this.selectedRewards = selectedRewards;
        this.rewardFlag = rewardFlag;
        this.userId = userId;
    }
    
    public List<String> getSelectedRewards() {
        return selectedRewards;
    }
    
    public void setSelectedRewards(List<String> selectedRewards) {
        this.selectedRewards = selectedRewards;
    }
    
    @SerializedName("selectedRewards")
  @Expose
  private List<String> selectedRewards;
  @SerializedName("rewardFlag")
  @Expose
  private Boolean rewardFlag;
  @SerializedName("userId")
  @Expose
  private String userId;
  public void setRewardFlag(Boolean rewardFlag){
   this.rewardFlag=rewardFlag;
  }
  public Boolean getRewardFlag(){
   return rewardFlag;
  }
  public void setUserId(String userId){
   this.userId=userId;
  }
  public String getUserId(){
   return userId;
  }
}