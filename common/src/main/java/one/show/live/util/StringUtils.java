package one.show.live.util;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;

import one.show.live.po.POMember;


public class StringUtils {

  public static final String EMPTY = "";

  /** 用于生成文件 */
  private static final double KB = 1024.0;
  public static final double MB = 1048576.0;
  private static final double GB = 1073741824.0;

  public static char chatAt(String pinyin, int index) {
    if (pinyin != null && pinyin.length() > 0) return pinyin.charAt(index);
    return ' ';
  }

  /** 获取字符串宽度 */
  public static float GetTextWidth(String Sentence, float Size) {
    if (isEmpty(Sentence)) return 0;
    TextPaint FontPaint = new TextPaint();
    FontPaint.setTextSize(Size);
    return FontPaint.measureText(Sentence.trim()) + (int) (Size * 0.1); // 留点余地
  }

  /**
   * 拼接数组
   */
  public static String join(final List<String> array, final String separator) {
    StringBuffer result = new StringBuffer();
    if (array != null && array.size() > 0) {
      for (String str : array) {
        result.append(str);
        result.append(separator);
      }
      result.delete(result.length() - 1, result.length());
    }
    return result.toString();
  }

  /**
   * 拼接数组
   *
   * @param filter 过滤掉某些字符
   */
  public static String join(final List<String> array, final String filter, final String separator) {
    StringBuffer result = new StringBuffer();
    if (array != null && array.size() > 0) {
      for (String str : array) {
        result.append(str.replace(filter, ""));
        result.append(separator);
      }
      result.delete(result.length() - 1, result.length());
    }
    return result.toString();
  }

  public static String join(final String str, final String filter, final String separator) {
    StringBuffer result = new StringBuffer();
    if (StringUtils.isNotEmpty(str)) {
      result.append(str.replace(filter, ""));
      result.append(separator);
      result.delete(result.length() - 1, result.length());
    }
    return result.toString();
  }

  public static String join(final Iterator<String> iter, final String separator) {
    StringBuffer result = new StringBuffer();
    if (iter != null) {
      while (iter.hasNext()) {
        String key = iter.next();
        result.append(key);
        result.append(separator);
      }
      if (result.length() > 0) result.delete(result.length() - 1, result.length());
    }
    return result.toString();
  }

  /**
   * 判断字符串是否为空,或者为字符串的NULL,null
   *
   * @return false不为空
   */
  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0 || str.equalsIgnoreCase("null");
  }

  public static boolean isEmptyTrim(String s) {
    return s == null || "".equals(s.trim());
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  public static String trim(String str) {
    return str == null ? EMPTY : str.trim();
  }

  public static String ifNullReturnEmpty(String s){
    if(s == null)
      return "";
    return s;
  }

  /**
   * 判断对象是否为空
   *
   * @return false不为空
   */
  public static boolean isNullOrEmpty(final Object str) {
    return (str == null || str.toString().length() == 0);
  }

  /**
   * 判断一组字符串是否有一个为空
   */
  public static boolean isNullOrEmpty(final String... strs) {
    if (strs == null || strs.length == 0) {
      return true;
    }
    for (String str : strs) {
      if (str == null || str.length() == 0) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断子字符串是否有出现在指定字符串中
   */
  public static boolean find(String str, String c) {
    if (isNullOrEmpty(str)) {
      return false;
    }
    return str.indexOf(c) > -1;
  }

  public static boolean findIgnoreCase(String str, String c) {
    if (isNullOrEmpty(str)) {
      return false;
    }
    return str.toLowerCase().indexOf(c.toLowerCase()) > -1;
  }

  /**
   * 比较两个字符串是否相
   */
  public static boolean equals(String str1, String str2) {
    if (str1 == str2) return true;

    if (str1 == null) str1 = "";
    return str1.equals(str2);
  }

  /**
   * 比较两个字符串是否相，忽略大小写
   */
  public static boolean equalsIgnoreCase(String str1, String str2) {
    if (str1 == str2) return true;

    if (str1 == null) str1 = "";
    return str1.equalsIgnoreCase(str2);
  }

  /** 空值替换 */
  public static String replaceEmpty(String str, String defaultValue) {
    return isEmpty(trim(str)) ? defaultValue : str;
  }

  public static boolean isBlank(String s) {
    return TextUtils.isEmpty(s);
  }

  /**
   * 转换文件大小
   */
  public static String generateFileSize(long size) {
    String fileSize;
    if (size < KB) {
      fileSize = size + "B";
    } else if (size < MB) {
      fileSize = String.format("%.1f", size / KB) + "KB";
    } else if (size < GB) {
      fileSize = String.format("%.1f", size / MB) + "MB";
    } else {
      fileSize = String.format("%.1f", size / GB) + "GB";
    }

    return fileSize;
  }

  /** 查找字符串，找到返回，没找到返回空 */
  public static String findString(String search, String start, String end) {
    try {
      if (StringUtils.isNotEmpty(search) && StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(
          end)) {
        int start_len = start.length();
        int start_pos = StringUtils.isNotEmpty(start) ? search.indexOf(start) : 0;
        if (start_pos > -1) {
          int end_pos =
              StringUtils.isNotEmpty(end) ? search.indexOf(end, start_pos + start_len) : -1;
          if (end_pos > -1) return search.substring(start_pos + start.length(), end_pos);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "";
  }

  /**
   * 截取字符串
   *
   * @param search 待搜索的字符串
   * @param start 起始字符串 例如：<title>
   * @param end 结束字符串 例如：</title>
   */
  public static String substring(String search, String start, String end, String defaultValue) {
    int start_len = start.length();
    int start_pos = StringUtils.isEmpty(start) ? 0 : search.indexOf(start);
    if (start_pos > -1) {
      int end_pos = StringUtils.isEmpty(end) ? -1 : search.indexOf(end, start_pos + start_len);
      if (end_pos > -1) {
        return search.substring(start_pos + start.length(), end_pos);
      } else {
        return search.substring(start_pos + start.length());
      }
    }
    return defaultValue;
  }

  /**
   * 截取字符串
   *
   * @param search 待搜索的字符串
   * @param start 起始字符串 例如：<title>
   * @param end 结束字符串 例如：</title>
   */
  public static String substring(String search, String start, String end) {
    return substring(search, start, end, "");
  }

  /**
   * 拼接字符串
   */
  public static String concat(String... strs) {
    StringBuffer result = new StringBuffer();
    if (strs != null) {
      for (String str : strs) {
        if (str != null) result.append(str);
      }
    }
    return result.toString();
  }

  /** 获取中文字符个数 */
  public static int getChineseCharCount(String str) {
    String tempStr;
    int count = 0;
    for (int i = 0; i < str.length(); i++) {
      tempStr = String.valueOf(str.charAt(i));
      if (tempStr.getBytes().length == 3) {
        count++;
      }
    }
    return count;
  }



  private static boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
      return true;
    }
    return false;
  }

  // 完整的判断中文汉字和符号
  public static boolean isChinese(String strName) {
    char[] ch = strName.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (isChinese(c)) {
        return true;
      }
    }
    return false;
  }


  /** 获取指定宽度的可见字符 如果有多余 则用'...'表示 */
  public static String getNewVisibleStringAt(int width, Paint paint, String text) {
    try {
      if (text == null || text.length() == 0) {
        return "";
      }
      StringBuilder stringBuilder = new StringBuilder();
      int length = text.length(); // 字符串的长度
      char[] msg_chr = text.toCharArray(); // 存储text的字符形式
      int x = 0;
      int start = 0; // 字符串的开始位置
      int index = 0; // 当前检索字符的索引
      while (index <= length - 1) {
        int tempWidth = (int) paint.measureText(msg_chr, index, 1);
        x += tempWidth;
        if (x > width) {
          String temp1 = new String(msg_chr, start, index - start);
          if (index <= length - 1) {
            // 有省略
            stringBuilder.append(temp1 + "...");
          } else {
            stringBuilder.append(temp1);
          }
          return stringBuilder.toString();
        }
        index++;
      }
      // 画最后的子字符串
      index = length;
      if (index > start) {// 如果字符串不为空
        stringBuilder.append(new String(msg_chr, start, index - start));
      }
      return stringBuilder.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private int getSubLength(String content, int maxLength) {
    if (StringUtils.isEmpty(content)) return 0;
    int chineseCount = 0;
    double englishCount = 0;

    String tempStr;

    for (int i = content.length() - 1; i >= 0; i--) {
      tempStr = content.substring(0, i);
      chineseCount = StringUtils.getChineseCharCount(tempStr);// 中文字符
      englishCount = StringUtils.getEnglishCount(tempStr) / (double) 2;// 英文字符

      if (maxLength - chineseCount - englishCount >= 0.0) {
        return i;
      }
    }

    return 0;
  }

  /** 获取英文字符个数 */
  public static int getEnglishCount(String str) {
    String tempStr;
    int count = 0;
    for (int i = 0; i < str.length(); i++) {
      tempStr = String.valueOf(str.charAt(i));
      if (!(tempStr.getBytes().length == 3)) {
        count++;
      }
    }
    return count;
  }

  public static String encode(String url) {
    try {
      return URLEncoder.encode(url, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return url;
  }

  /**
   * Helper function for making null strings safe for comparisons, etc.
   *
   * @return (s == null) ? "" : s;
   */
  public static String makeSafe(String s) {
    return (s == null) ? "" : s;
  }


  public static boolean isCorrectTextCount(String string, int startCount, int endCount) {
    String content = StringUtils.trim(string);
    int chineseCount = StringUtils.getChineseCharCount(content);//中文字符
    double englishCount = StringUtils.getEnglishCount(content) / (double) 2;//英文字符
    //			double englishCount2 = (double) englishCount / (double) 2;

    double totalCount = chineseCount + englishCount;
    if (totalCount >= startCount && totalCount <= endCount) {

      return true;
    }

    return false;
  }

  public static String getUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  //清除回车字符
  public static String removeEnter(String resource, char ch) {
    if (StringUtils.isEmpty(resource)) {
      return "";
    }
    StringBuffer buffer = new StringBuffer();
    int position = 0;
    char currentChar;
    int index = 0;
    while (position < resource.length()) {
      currentChar = resource.charAt(position++);
      if (currentChar == ch) {
        if (index == 0) {
          index++;
          buffer.append(" ");
        } else {
          buffer.append("");
        }
      } else {
        index = 0;
        buffer.append(currentChar);
      }
    }
    return buffer.toString();
  }

  /**
   * 加粗全部字体
   * @param str
   * @return
   */
  public static SpannableString bold(String str) {
    SpannableString spanString = new SpannableString(str);
    StyleSpan span = new StyleSpan(Typeface.BOLD);//加粗
    spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spanString;
  }

  /**
   * 将金额格式化成三位一个逗号的
   * @param in
   * @return
     */
  public static String formattingNumbers(int in){
    DecimalFormat df = new DecimalFormat("#,###");
    String amount = df.format(in);
    return amount;
  }
}
