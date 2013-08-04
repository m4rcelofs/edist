package com.erep.dist.erep;

import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.erep.dist.cache.Cacheable;

public class eRepTimeCalculator {

	public static final LocalDate		eRepLaunch		= new LocalDate(2007,
																11, 20);
	public static final DateTimeZone	eRepTimeZone	= DateTimeZone
																.forID("America/Los_Angeles");

	public static int geteRepDay() {
		LocalDate today = new LocalDate(eRepTimeZone);
		return new eRepTimeCalculator().daysBetween(eRepLaunch, today);
	}

	public static int toErepDate(LocalDate date) {
		return new eRepTimeCalculator().daysBetween(eRepLaunch, date);
	}

	@Cacheable
	private Integer daysBetween(LocalDate first, LocalDate last) {
		return Days.daysBetween(first, last).getDays();
	}

	public static int geteRepHour() {
		LocalTime eRepTime = new LocalTime(eRepTimeZone);
		return eRepTime.getHourOfDay();
	}
}
