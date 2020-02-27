
package com.sd.src.stepcounterapp.model.bmi;


@SuppressWarnings("unused")
public class BMIObject {

    private String height;
    private String heightType;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private int weight;
    private String weightType;

    public BMIObject(String height, String heightType, int weight, String weightType) {
        this.height = height;
        this.heightType = heightType;
        this.weight = weight;
        this.weightType = weightType;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeightType() {
        return heightType;
    }

    public void setHeightType(String heightType) {
        this.heightType = heightType;
    }



    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }
}
