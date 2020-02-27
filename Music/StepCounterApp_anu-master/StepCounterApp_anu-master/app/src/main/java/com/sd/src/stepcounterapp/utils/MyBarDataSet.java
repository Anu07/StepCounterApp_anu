
package com.sd.src.stepcounterapp.utils;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class MyBarDataSet extends BarDataSet {

    private int mLimit;

    public MyBarDataSet(List<BarEntry> yVals, String label, int limit) {
        super(yVals, label);
        mLimit =limit;
    }

    @Override
    public int getColor(int index) {
        if(getEntryForIndex(index).getY() < mLimit)
            return mColors.get(1);
        else{
            return mColors.get(0);
        }
    }
}