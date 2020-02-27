
package com.sd.src.stepcounterapp.model.challenge.departmentchallengeresponse;

@SuppressWarnings("unused")
public class Datum implements Comparable<Datum> {

    private String departmentId;
    private String departmentName;


    public Integer getDepartmentRank() {
        return departmentRank;
    }

    public void setDepartmentRank(Integer departmentRank) {
        this.departmentRank = departmentRank;
    }

    private Integer departmentRank;
    private Integer steps;

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    @Override
    public int compareTo(Datum o) {

        if (getSteps() == 0) {
            return 0;
        }
        return getSteps().compareTo(o.getSteps());
    }
}
