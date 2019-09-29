
package com.sd.src.stepcounterapp.model.wallet;


@SuppressWarnings("unused")
public class Data {

    private String lastUpdated;
    private long remainingSteps;
    private long totalEarnings;
    
    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public long getRemainingSteps() {
        return remainingSteps;
    }
    
    public void setRemainingSteps(long remainingSteps) {
        this.remainingSteps = remainingSteps;
    }
    
    public long getTotalEarnings() {
        return totalEarnings;
    }
    
    public void setTotalEarnings(long totalEarnings) {
        this.totalEarnings = totalEarnings;
    }
}
