package cn.snowsoft.iot.module.cps.utils;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期工具类
 */
@Component
public class DataUtil {
	private static Log log = LogFactory.getLog(DataUtil.class);

	/**
	 * 获取本周的开始时间
	 *
	 * @return Date
	 */
	public static Date getBeginDayOfWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}

	/**
	 * 获取本周的结束时间
	 *
	 * @return Date
	 */
	public static Date getEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}

	/**
	 * 获取某个日期的开始时间
	 *
	 * @param d 日期
	 * @return Timestamp
	 */
	public static Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 获取某个日期的结束时间
	 *
	 * @param d 时间
	 * @return Timestamp
	 */
	public static Timestamp getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 获取当月开始时间和结束时间
	 *
	 * @return Map
	 */
	public static Map getMonthTime() {
		Long startTime = getMonthStartTime();
		Long endTime = getMonthEndTime();
		DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
		String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
		Map map = new HashMap();
		map.put("startDate", startTimeStr);
		map.put("endDate", endTimeStr);
		return map;
	}

	/**
	 * 获取当月的结束时间戳
	 *
	 * @return Long
	 */
	public static Long getMonthEndTime() {
		Long currentTime = System.currentTimeMillis();

		String timeZone = "GMT+8:00";
		Calendar calendar = Calendar.getInstance(); // 获取当前日期
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.setTimeInMillis(currentTime);
		calendar.add(Calendar.YEAR, 0);
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // 获取当前月最后一天
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取当月开始时间戳
	 *
	 * @return Long
	 */
	public static Long getMonthStartTime() {
		Long currentTime = System.currentTimeMillis();
		String timeZone = "GMT+8:00";
		Calendar calendar = Calendar.getInstance(); // 获取当前日期
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.setTimeInMillis(currentTime);
		calendar.add(Calendar.YEAR, 0);
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为1号,当前日期既为本月第一天
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取今天的开始时间
	 *
	 * @return String
	 */
	public String getTodayStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return sdf.format(todayStart.getTime());
	}

	/**
	 * 获取今天的结束时间
	 *
	 * @return String
	 */
	public String getTodayEndTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return sdf.format(todayEnd.getTime());
	}

	/**
	 * 判断当前日期是否大于某个日期
	 *
	 * @param date yyyy-MM-dd
	 * @return boolean
	 */
	public static boolean afterDate(String date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//把String转为LocalDate
		LocalDate localTime = LocalDate.parse(date, dtf);
		//判断当前日期是否大于指定日期
		return LocalDate.now().isAfter(localTime);
	}

	/**
	 * 输入两个时间，一个是开始时间，另一个是结束时间
	 * 两者相比较，判断是不是超过31天
	 *
	 * @param startTime startTime
	 * @param endTime   endTime
	 * @return boolean
	 */
	public static boolean checkOverdue(String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date end = sdf.parse(endTime);
			Date start = sdf.parse(startTime);
			long day = 60 * 1000 * 60 * 24; // 1天
			if (end.getTime() - (day * 31) > start.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 输入两个时间，一个是开始时间，另一个是结束时间
	 * 两者相比较，判断是不是超过一年
	 *
	 * @param startTime startTime
	 * @param endTime   endTime
	 * @return boolean
	 */
	public static boolean checkOverdueYear(String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date end = sdf.parse(endTime);
			Date start = sdf.parse(startTime);
			long day = 60 * 1000 * 60 * 24; // 1天
			if (end.getTime() - (day * 364) > start.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取某一时间 前多少天的日期
	 *
	 * @param n    -n为后 n为前
	 * @param time 传入的时间
	 * @return 返回的时间
	 */
	public static String beforeAfterDay(int n, String time) {
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beforeAfterDay = null;
		try {
			Calendar starCal = Calendar.getInstance();
			starCal.setTime(sourceFormat.parse(time));
			starCal.add(Calendar.DATE, n);
			beforeAfterDay = sourceFormat.format(starCal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return beforeAfterDay;
	}

	/**
	 * String转Date
	 *
	 * @param s 传入的String的时间
	 * @return Date类型
	 */
	public static Date transferString2Date(String s) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
		} catch (ParseException e) {
			log.error("时间转换错误, string = {}", e);
		}
		return date;
	}

	/**
	 * 在原日期的基础上增加小时数
	 *
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date addHour(Date date, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, i);
		Date newDate = c.getTime();
		return newDate;
	}


}
