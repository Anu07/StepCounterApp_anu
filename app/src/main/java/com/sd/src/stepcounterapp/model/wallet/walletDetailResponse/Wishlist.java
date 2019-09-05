
package com.sd.src.stepcounterapp.model.wallet.walletDetailResponse;

@SuppressWarnings("unused")
public class Wishlist {

    private long _V;
    private String _id;
    private String adminId;
    private Object createdAt;
    private String description;
    private String image;
    private Boolean isActive;
    private Boolean isDeleted;
    private String name;
    private long quantity;
    private String rewardId;
    private String shortDesc;
    private long token;
    private long totalSales;
    private String updatedAt;
    private String vendorId;
    private long wishlistCount;
    
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
    
    public String getAdminId() {
        return adminId;
    }
    
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
    
    public Object getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public Boolean getDeleted() {
        return isDeleted;
    }
    
    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getQuantity() {
        return quantity;
    }
    
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    
    public String getRewardId() {
        return rewardId;
    }
    
    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }
    
    public String getShortDesc() {
        return shortDesc;
    }
    
    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
    
    public long getToken() {
        return token;
    }
    
    public void setToken(long token) {
        this.token = token;
    }
    
    public long getTotalSales() {
        return totalSales;
    }
    
    public void setTotalSales(long totalSales) {
        this.totalSales = totalSales;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getVendorId() {
        return vendorId;
    }
    
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }
    
    public long getWishlistCount() {
        return wishlistCount;
    }
    
    public void setWishlistCount(long wishlistCount) {
        this.wishlistCount = wishlistCount;
    }
}
