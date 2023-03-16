package me.sumo.utils;

public class TimeUtil {
	
	public static String setFormat(Integer value) {
		int remainder = value.intValue() * 1000;
	    int seconds = remainder / 1000 % 60;
	    int minutes = remainder / 60000 % 60;
	    int hours = remainder / 3600000 % 24;
	    return new StringBuilder().append(hours > 0 ? String.format("%02d:", new Object[] { Integer.valueOf(hours) }) : "").append(String.format("%02d:%02d", new Object[] { Integer.valueOf(minutes), Integer.valueOf(seconds) })).toString();
	}

}
