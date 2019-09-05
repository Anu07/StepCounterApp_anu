
package com.sd.src.stepcounterapp.model.wallet.walletDetailResponse;

import java.util.List;

@SuppressWarnings("unused")
public class Data {

    private long days;
    
    public List<Purchased> getPurchased() {
        return purchased;
    }
    
    public void setPurchased(List<Purchased> purchased) {
        this.purchased = purchased;
    }
    
    private List<Purchased> purchased;
    private List<Redeemed> redeemed;
    private long steps;
    private long totalEarnings;
    private long totalGenerated;
    private String updatedAt;
    private List<Wishlist> wishlist;
    
    public long getDays() {
        return days;
    }
    
    public void setDays(long days) {
        this.days = days;
    }
    
   
    
    public long getSteps() {
        return steps;
    }
    
    public void setSteps(long steps) {
        this.steps = steps;
    }
    
    public long getTotalEarnings() {
        return totalEarnings;
    }
    
    public void setTotalEarnings(long totalEarnings) {
        this.totalEarnings = totalEarnings;
    }
    
    public long getTotalGenerated() {
        return totalGenerated;
    }
    
    public void setTotalGenerated(long totalGenerated) {
        this.totalGenerated = totalGenerated;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<Wishlist> getWishlist() {
        return wishlist;
    }
    
    public void setWishlist(List<Wishlist> wishlist) {
        this.wishlist = wishlist;
    }
    
    public List<Redeemed> getRedeemed() {
        return redeemed;
    }
    
    public void setRedeemed(List<Redeemed> redeemed) {
        this.redeemed = redeemed;
    }
}
