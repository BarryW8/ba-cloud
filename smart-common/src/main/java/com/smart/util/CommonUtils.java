package com.smart.util;

import com.alibaba.nacos.common.utils.Md5Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用工具类
 **/
@Slf4j
public class CommonUtils {
	public static final String DATE_FMT_YYYY_MM_DD_HH_MM_SS_SSS= "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_FMT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FMT_YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 获取ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }


    /**
     * MD5加密--大写
     */
    public static String MD5Upper(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }

            return sb.toString().toUpperCase();
        } catch (Exception exception) {
        }
        return null;

    }

    /**
     * MD5加密--小写
     */
    public static String MD5Lower(String data) {
        return Md5Utils.getMD5(data.getBytes());

    }

    /**
     * 获取验证码随机数
     */
    public static String getRandomCode(int length) {

        String sources = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            sb.append(sources.charAt(random.nextInt(9)));
        }
        return sb.toString();
    }


    /**
     * 获取当前时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }


    /**
     * 生成uuid
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 获取随机长度的串
     */
    private static final String ALL_CHAR_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String getStringNumRandom(int length) {
        //生成随机数字和字母,
        Random random = new Random();
        StringBuilder saltString = new StringBuilder(length);
        for (int i = 1; i <= length; ++i) {
            saltString.append(ALL_CHAR_NUM.charAt(random.nextInt(ALL_CHAR_NUM.length())));
        }
        return saltString.toString();
    }

    /**
     * 响应json数据给前端
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) {

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json; charset=utf-8");

        try (PrintWriter writer = response.getWriter()) {
            writer.print(objectMapper.writeValueAsString(obj));
            response.flushBuffer();
        } catch (IOException e) {
            log.warn("响应json数据给前端异常:{}", e);
        }
    }

    /**
     * 当前时间后随机4位有效数
     * @return
     */
    public static String genTimesRand4() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = df.format(new Date());
        //String random = (int) (Math.random() * 10000) + "";
        String random = new Random().nextInt(10000) + "";
        return time + random;
    }

    /**
     * 获取当前时间
     */
    public static String getCurrTime(String formatStyle) {
        // yyyyMMddHHmmss
        // yyyy-MM-dd HH:mm:ss
        SimpleDateFormat df = new SimpleDateFormat(formatStyle);
        return df.format(new Date());
    }

    public static String generateImage(int saveType, String imgStr, String imgName) {
        try {
            // todo 图片信息保存
            return imgName;
        } catch (Exception e) {
            System.out.println("--本地图片保存失败---");
        }
        return "";
    }

    /***
     * 金额把元转换为分(乘以100)
     * @param amount
     * @return
     */
    public static String changeY2F(String amount) {
        String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0L;
        try {
            if (index == -1) {
                amLong = Long.valueOf(currency + "00");
            } else if (length - index >= 3) {
                amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
            } else if (length - index == 2) {
                amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
            } else {
                amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
            }
            return amLong.toString();
        } catch (Exception e) {
            return "00";
        }
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) {
        if (!amount.matches("\\-?[0-9]+")) {
            return "0.00";
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }


    /***
     * 把str的数字转换为2位小数的数字
     *
     * @param amonut
     * @return
     */
    public static String changeS2D(String amonut) {
        DecimalFormat df = new DecimalFormat("##0.00");
        String db = df.format(Double.parseDouble(amonut));
        return db;
    }

    public static String getSecsToDHMS(long seconds) {
        // 换成天
        long day = seconds / (60 * 60 * 24);
        // 总秒数-换算成天的秒数=剩余的秒数
        long hour = (seconds - (60 * 60 * 24 * day)) / 3600;
        // 剩余的秒数换算为小时
        long minute = (seconds - 60 * 60 * 24 * day - 3600 * hour) / 60;
        // 总秒数-换算成天的秒数-换算成小时的秒数=剩余的秒数
        // 剩余的秒数换算为分
        // 总秒数-换算成天的秒数-换算成小时的秒数-换算为分的秒数=剩余的秒数
        long second = seconds - 60 * 60 * 24 * day - 3600 * hour - 60 * minute;
        String result = day + "天" + hour + "小时" + minute + "分" + second + "秒";
        return result;
    }

    /**
     * 获取两个时间的间隔秒数
     * @param bgtime
     * @param endtime
     * @return
     */
    public static long getTwoDateSeconds(String bgtime,String endtime){
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//
        long seconds = 0;
        try {
            long begin = simpleFormat.parse(bgtime).getTime(); //开始时间
            long end = simpleFormat.parse(endtime).getTime();  //结束时间
            if(begin < end) {
                seconds = (end - begin)/1000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return seconds;
    }

    /***
     * 获取验证码 6位数字字母
     *
     * @param length
     * @return
     */
    public static String genCodes(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // // 输出字母还是数字
            // if ("char".equalsIgnoreCase(charOrNum)) { // 字符串
            // int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //
            // 取得大写字母还是小写字母
            // val += (char) (choice + random.nextInt(26));
            // } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
            // val += String.valueOf(random.nextInt(10));
            // }
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    /**
     * 判断是否在时间段内
     * @param bgtime
     * @param endtime
     * @return
     */
    public static boolean isContainTime(String bgtime,String endtime){
        SimpleDateFormat simpleFormat = new SimpleDateFormat("HHmmss");
        String nowDate= simpleFormat.format(new Date());
        int nowTime = Integer.parseInt(nowDate);
        try {
            long begin =Long.parseLong(bgtime.replace(":", "")); //开始时间
            long end = Long.parseLong(endtime.replace(":", ""));  //结束时间
            if(begin<=nowTime && nowTime <= end) {
               return true;
            }
        } catch (Exception e) {
        	return false;
        }
        return false;
    }

    /**
     * 获取当前月份第一天 yyyy-MM-dd
     */
    public static String getDateTimeMonthStart(){
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat df = new SimpleDateFormat(DATE_FMT_YYYY_MM_DD);
        return df.format(cale.getTime());
    }

    /**
     * 获取当前月份最后一天  yyyy-MM-dd
     */
    public static String getDateTimeMonthEnd(){
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat df = new SimpleDateFormat(DATE_FMT_YYYY_MM_DD);
        return df.format(cale.getTime());
    }

    /**
     * BigDecimal保留两位小数，不足两位补0
     */
    public static String getDecimalFormat(String str){
    	if(StringUtils.isBlank(str)) {
    		str = "0";
    	}
    	 // 四舍五入
        BigDecimal value = new BigDecimal(str).setScale(2,BigDecimal.ROUND_HALF_UP);
        // 不足两位小数补0
        DecimalFormat decimalFormat = new DecimalFormat("0.00#");
        String strVal = decimalFormat.format(value);
        return strVal;
    }
    /**
     * 字符串转日期
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		date = java.sql.Date.valueOf(str);

		return date;
	}

    /**
     * 取两个日期之间的所有日期
     * @param start
     * @param end
     * @return
     */
     public static List<String> getDateDiff(Date start, Date end) {
        List<String> list = new ArrayList<String>();
        long s = start.getTime();
        long e = end.getTime();

        Long oneDay = 1000 * 60 * 60 * 24l;

        while (s <= e) {
          start = new Date(s);
          list.add(new SimpleDateFormat("yyyy-MM-dd").format(start));
          s += oneDay;
        }
        return list;
      }

     public static List<String> getMonths(String startTime, String endTime) {
         // 返回的日期集合
         List<String> months = new ArrayList<String>();

         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
         try {
             Date start = dateFormat.parse(startTime);
             Date end = dateFormat.parse(endTime);

             Calendar tempStart = Calendar.getInstance();
             tempStart.setTime(start);

             Calendar tempEnd = Calendar.getInstance();
             tempEnd.setTime(end);
             tempEnd.add(Calendar.MONTH, +1);// 日期加1(包含结束)
             while (tempStart.before(tempEnd)) {
            	 months.add(dateFormat.format(tempStart.getTime()));
                 tempStart.add(Calendar.MONTH, 1);
             }

         } catch (ParseException e) {
             e.printStackTrace();
         }

         return months;
     }

     /**
      * 获取指定年月的第一天
      * @param year
      * @param month
      * @return
      */
     public static String getFirstDayOfMonth(int year, int month) {
         Calendar cal = Calendar.getInstance();
         //设置年份
         cal.set(Calendar.YEAR, year);
         //设置月份
         cal.set(Calendar.MONTH, month-1);
         //获取某月最小天数
         int firstDay = cal.getMinimum(Calendar.DATE);
         //设置日历中月份的最小天数
         cal.set(Calendar.DAY_OF_MONTH,firstDay);
         //格式化日期
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         return sdf.format(cal.getTime());
     }

     /**
      * 获取指定年月的最后一天
      * @param year
      * @param month
      * @return
      */
      public static String getLastDayOfMonth(int year, int month) {
          Calendar cal = Calendar.getInstance();
          //设置年份
          cal.set(Calendar.YEAR, year);
          //设置月份
          cal.set(Calendar.MONTH, month-1);
          //获取某月最大天数
          int lastDay = cal.getActualMaximum(Calendar.DATE);
          //设置日历中月份的最大天数
          cal.set(Calendar.DAY_OF_MONTH, lastDay);
          //格式化日期
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          return sdf.format(cal.getTime());
      }
}
