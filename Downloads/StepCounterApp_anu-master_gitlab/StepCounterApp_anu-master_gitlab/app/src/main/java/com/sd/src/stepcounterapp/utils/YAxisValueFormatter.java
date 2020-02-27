package com.sd.src.stepcounterapp.utils;

import android.util.Log;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class YAxisValueFormatter extends ValueFormatter {
	
	private final String[] mRange = new String[]{
			"1","3","5","7","9","10K"
	};
	private final BarLineChartBase<?> chart;
	int i =-1 ;
	
	
	public YAxisValueFormatter(BarLineChartBase<?> chart) {
		this.chart = chart;
	}
	
	@Override
	public String getFormattedValue(float value) {
		i++;
		Log.i("Range","Val"+mRange[i]);
		return mRange[i];
		
	}
	
}
