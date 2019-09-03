package com.sd.src.stepcounterapp.utils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter extends ValueFormatter {
	
	private final String[] mWeeks = new String[]{
			"Mon",
			"Tue",
			"Wed",
			"Thu",
			"Fri",
			"Sat",
			"Sun"
	};
	
	private String[] mMonth = new String[]{};
	
	private final BarLineChartBase<?> chart;
	private final String format;
	
	public DayAxisValueFormatter(BarLineChartBase<?> chart, String format) {
		this.chart = chart;
		this.format=format;
	}
	public DayAxisValueFormatter(BarLineChartBase<?> chart, String format,String[] month) {
		this.chart = chart;
		this.format=format;
		this.mMonth =month;
	}
	
	@Override
	public String getFormattedValue(float value) {
		int days = (int) value;
		String array = new String();
		if(format.equalsIgnoreCase(InterConsts.MONTHLY)) {
			array = mMonth[(int) value];
		}else  {
			if(days<=7){				//TODO HIGH PRIORITY
			array = mWeeks[(int) value];
			}
		}
		
		return array;
	}
	
	
	
	
}
