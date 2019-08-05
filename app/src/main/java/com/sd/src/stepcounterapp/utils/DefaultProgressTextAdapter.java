package com.sd.src.stepcounterapp.utils;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public final class DefaultProgressTextAdapter implements CircularProgressIndicator.ProgressTextAdapter {
	
	@Override
	public String formatText(double currentProgress) {
		return "Today\n" + (int) currentProgress + "\n tokens of 10";
	}
}
