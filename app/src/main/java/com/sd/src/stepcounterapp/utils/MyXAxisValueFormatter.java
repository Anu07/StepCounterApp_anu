package com.sd.src.stepcounterapp.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyXAxisValueFormatter extends ValueFormatter {
	
	private String[] mValues;
	
	public MyXAxisValueFormatter(String[] values) {
		this.mValues = values;
	}
	
	@Override
	public String getFormattedValue(float value, AxisBase axis) {
		// "value" represents the position of the label on the axis (x or y)
		return "Test";
	}
	
	/** this is only needed if numbers are returned, else return 0 */
//	@Override
//	public int getDecimalDigits() { return 0; }
}