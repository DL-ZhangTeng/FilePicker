package com.zhangteng.searchfilelibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类.
 */

public class DateUtils {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 英文简写如：2016
     */
    public static String FORMAT_Y = "yyyy";
    /**
     * 英文简写如：12
     */
    public static String FORMAT_H = "HH";
    /**
     * 英文简写如：12:01
     */
    public static String FORMAT_M = "mm";
    /**
     * 英文简写如：12:01
     */
    public static String FORMAT_HM = "HH:mm";
    /**
     * 英文简写如：1-12 12:01
     */
    public static String FORMAT_MDHM = "MM-dd HH:mm";
    /**
     * 英文简写（默认）如：2016-12-01
     */
    public static String FORMAT_YMD = "yyyy-MM-dd";
    /**
     * 英文简写（默认）如：2016-12
     */
    public static String FORMAT_YM = "yyyy-MM";
    /**
     * 英文全称  如：2016-12-01 23:15
     */
    public static String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
    /**
     * 英文全称  如：2016-12-01 23:15:06
     */
    public static String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL_SN = "yyyyMMddHHmmssS";

    /**
     * 中文简写  如：212月01日
     */
    public static String FORMAT_MD_CN = "MM月dd日";
    /**
     * 中文简写  如：2016年12月01日
     */
    public static String FORMAT_YMD_CN = "yyyy年MM月dd日";
    /**
     * 中文简写  如：2016年12月01日  12时
     */
    public static String FORMAT_YMDH_CN = "yyyy年MM月dd日 HH时";
    /**
     * 中文简写  如：2016年12月01日  12时12分
     */
    public static String FORMAT_YMDHM_CN = "yyyy年MM月dd日 HH时mm分";
    /**
     * 中文全称  如：2016年12月01日  23时15分06秒
     */
    public static String FORMAT_YMDHMS_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
    public static Calendar calendar = null;

    public static Date str2Date(String str) {
        return str2Date(str, null);
    }


    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static Calendar str2Calendar(String str) {
        return str2Calendar(str, null);
    }


    public static Calendar str2Calendar(String str, String format) {

        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;
    }


    public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }


    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }


    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }


    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }

    /**
     * 获得当前日期的字符串格式
     *
     * @param format 格式化的类型
     * @return 返回格式化之后的事件
     */
    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);

    }

    /**
     * @param time 时间
     * @return 返回时间String
     */
    // 格式到毫秒
    public static String getTime(long time, String format) {
        return new SimpleDateFormat(format).format(time);

    }


    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return 增加数个整月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();

    }

    //时间转换时间戳
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }


    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return 增加之后的天数
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();

    }


    /**
     * 获取距现在某一小时的时刻
     *
     * @param format 格式化时间的格式
     * @param h      距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * @return 获取距现在某一小时的时刻
     */
    public static String getNextHour(String format, int h) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(date.getTime() + h * 60 * 60 * 1000);
        return sdf.format(date);

    }


    /**
     * 获取时间戳
     *
     * @return 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());

    }


    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }


    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 功能描述：返回小时
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }


    /**
     * 获得默认的 date pattern
     *
     * @return 默认的格式
     */
    public static String getDatePattern() {

        return FORMAT_YMDHMS;
    }


    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        calendar = Calendar.getInstance();

        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }


    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return 提取字符串的日期
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());

    }


    /**
     * 功能描述：返回毫
     *
     * @param date 日期
     * @return 返回毫
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }


    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return 按默认格式的字符串距离今天的天数
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;

    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return 提取字符串日期
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return 按用户格式字符串距离今天的天数
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 获取当天0点时间戳
     */
    public static Long dayTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;
    }

    /**
     * 获取当月0点时间戳
     */
    public static Long monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;
    }

    /**
     * 获取当年0点时间戳
     */
    public static Long yearTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.DATE, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;
    }

    /**
     * 获取当天星期几
     */
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    /**
     * 获取当天星期几
     */
    public static int getWeekNum() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param time 时间
     * @return 返回时间String
     */
    // 格式到毫秒
    public static String getPublishTime(long time) {
        long current = System.currentTimeMillis();
        long today = dayTimeInMillis();
        long yesterday = today - 24 * 3600 * 1000;
        Date begin = new Date(time);
        Date end = new Date(current);
        long diff = end.getTime() - begin.getTime();
        if (time < yearTimeInMillis()) {
            return getTime(time, FORMAT_YMDHM);
        } else if (time < yesterday) {
            return getTime(time, FORMAT_MDHM);
        } else if (time < today) {
            return String.format("昨天 %s", getTime(time, FORMAT_HM));
        } else if (time < current - 3600 * 1000) {
            long hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            return hours + "小时前";
        } else if (time < current - 60 * 1000) {
            long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
            return minutes + "分钟前";
        } else {
            return "刚刚";
        }
    }


    public static String getRedTime(String times) {
        if ("".equals(times)) {
            return "0秒";
        }
        int time = Integer.parseInt(times) / 1000;
        // 时间差
        int second = time;
        if (second < 60) {
            return String.format("%d秒", second);
        }

        int sec = time / 60;
        if (sec < 60) {
            return String.format("%d分钟", sec);
        }

        // 秒转小时
        int hours = time / 3600;
        if (hours < 24) {
            return String.format("%d小时", hours);
        }
        //秒转天数
        int days = time / 3600 / 24;
        if (days < 30) {
            return String.format("%d天", days);
        }
        return String.format("%d秒", second);

    }
}
