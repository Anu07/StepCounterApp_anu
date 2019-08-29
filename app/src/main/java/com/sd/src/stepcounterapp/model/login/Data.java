package com.sd.src.stepcounterapp.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class Data {
	@SerializedName("firstName")
	@Expose
	private String firstName;
	@SerializedName("lastName")
	@Expose
	private String lastName;
	@SerializedName("image")
	@Expose
	private String image;
	@SerializedName("dob")
	@Expose
	private String dob;
	@SerializedName("_id")
	@Expose
	private String _id;
	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("username")
	@Expose
	private String username;
	
	public Data(String firstName, String lastName, String image, String dob, String _id, String email, String username, Boolean basicFlag, Boolean rewardFlag) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.image = image;
		this.dob = dob;
		this._id = _id;
		this.email = email;
		this.username = username;
		this.basicFlag = basicFlag;
		this.rewardFlag = rewardFlag;
	}
	
	
	public Boolean getBasicFlag() {
		return basicFlag;
	}
	
	public void setBasicFlag(Boolean basicFlag) {
		this.basicFlag = basicFlag;
	}
	
	public Boolean getRewardFlag() {
		return rewardFlag;
	}
	
	public void setRewardFlag(Boolean rewardFlag) {
		this.rewardFlag = rewardFlag;
	}
	
	@SerializedName("basicFlag")
	@Expose
	private Boolean basicFlag;
	@SerializedName("rewardFlag")
	@Expose
	private Boolean rewardFlag;
	
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public String getDob() {
		return dob;
	}
	
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String get_id() {
		return _id;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
}