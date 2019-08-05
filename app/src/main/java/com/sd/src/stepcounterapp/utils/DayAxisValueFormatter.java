package com.sd.src.stepcounterapp.utils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter extends ValueFormatter {
	
	private final String[] mMonths = new String[]{
			"Mon",
			"Tue",
			"Wed",
			"Thu",
			"Fri",
			"Sat",
			"Sun"
	};
	
	private final BarLineChartBase<?> chart;
	
	public DayAxisValueFormatter(BarLineChartBase<?> chart) {
		this.chart = chart;
	}
	
	@Override
	public String getFormattedValue(float value) {
		
		int days = (int) value;
		
		String monthName = mMonths[(int) value];
		return monthName ;
	}
	
	
	
	
}
