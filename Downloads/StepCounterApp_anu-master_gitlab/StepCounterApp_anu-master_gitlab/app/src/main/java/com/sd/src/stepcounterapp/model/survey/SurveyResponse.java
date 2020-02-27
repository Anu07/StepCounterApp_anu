
package com.sd.src.stepcounterapp.model.survey;

import java.util.List;
@SuppressWarnings("unused")
public class SurveyResponse {

    private List<Datum> data;
    private List<Datum> featured;
    private String message;
    private long status;
    
    public SurveyResponse() {
    }
    
    public List<Datum> getData() {
        return data;
    }
    
    public void setData(List<Datum> data) {
        this.data = data;
    }
    
    public List<Datum> getFeatured() {
        return featured;
    }
    
    public void setFeatured(List<Datum> featured) {
        this.featured = featured;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getStatus() {
        return status;
    }
    
    public void setStatus(long status) {
        this.status = status;
    }
}
